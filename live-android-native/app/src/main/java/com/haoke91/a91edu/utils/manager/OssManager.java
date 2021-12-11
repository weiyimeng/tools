package com.haoke91.a91edu.utils.manager;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.Utils;
import com.gaosiedu.live.sdk.android.api.storage.token.LiveStorageTokenRequest;
import com.gaosiedu.live.sdk.android.api.storage.token.LiveStorageTokenResponse;
import com.gaosiedu.live.sdk.android.api.user.base.ossUploadToken.LiveUserBaseOssUploadTokenRequest;
import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.haoke91.a91edu.GlobalConfig;
import com.haoke91.a91edu.entities.CreateUpLoadBean;
import com.haoke91.a91edu.entities.StorageInfo;
import com.haoke91.a91edu.entities.UpLoadResultBean;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.JsonCallBack;
import com.haoke91.a91edu.net.ResponseCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/9/18 下午5:39
 * 修改人：weiyimeng
 * 修改时间：2018/9/18 下午5:39
 * 修改备注：
 */
public class OssManager {
    private String endpoint = "http://oss-cn-beijing.aliyuncs.com/";
    private String bucket = "91haoke-image";
    private static OssManager instance = null;
    private OSS oss;
    
    private OssManager() {
        initOss();
    }
    
    
    public static OssManager getInstance() {
        if (instance == null) {
            synchronized (OssManager.class) {
                if (instance == null) {
                    instance = new OssManager();
                }
            }
        }
        return instance;
    }
    
    
    private void initOss() {
        OSSCredentialProvider credentialProvider = new OSSFederationCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() {
                LiveUserBaseOssUploadTokenRequest request = new LiveUserBaseOssUploadTokenRequest();
                request.setUserId(UserManager.getInstance().getUserId());
                String pos = Api.getInstance().postSyn(request, "");
                try {
                    JSONObject jsonObject = new JSONObject(pos);
                    JSONObject data = jsonObject.optJSONObject("data");
                    return new OSSFederationToken(data.optString("accessKeyId"), data.optString("accessKeySecret"), data.optString("securityToken"), data.optString("expirationTime"));
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                
                // 您需要在这里实现获取一个FederationToken，并构造成OSSFederationToken对象返回
                // 如果因为某种原因获取失败，可直接返回nil
                //                OSSFederationToken * token;
                // 下面是一些获取token的代码，比如从您的server获取
                return null;
            }
        };
        
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        //开启可以在控制台看到日志，并且会支持写入手机sd卡中的一份日志文件位置在SDCard_path\OSSLog\logs.csv  默认不开启
        //日志会记录oss操作行为中的请求数据，返回数据，异常信息
        //例如requestId,response header等
        //android_version：5.1  android版本
        //mobile_model：XT1085  android手机型号
        //network_state：connected  网络状况
        //network_type：WIFI 网络连接类型
        //具体的操作行为信息:
        //[2017-09-05 16:54:52] - Encounter local execpiton: //java.lang.IllegalArgumentException: The bucket name is invalid.
        //A bucket name must:
        //1) be comprised of lower-case characters, numbers or dash(-);
        //2) start with lower case or numbers;
        //3) be between 3-63 characters long.
        //------>end of log
        //        OSSLog.enableLog();
        
        
        oss = new OSSClient(Utils.getApp(), endpoint, credentialProvider);
    }
    
    
    //    @Deprecated
    //    public void upLoadFile(List<String> paths, ResponseCallback<String> callback) {
    //        boolean isSuccess = false;
    //        if (paths != null && paths.size() > 0) {
    //            for (int i = 0; i < paths.size(); i++) {
    //                uploadFile(paths.get(i), callback);
    //            }
    //        }
    //    }
    
    /**
     * 提交作業
     *
     * @param filePath
     * @param callback
     * @return
     */
    public boolean uploadHomework(String filePath, String id, final ResponseCallback<String> callback) {
        String objectKey = GlobalConfig.OSSHOMEWORKPATH + id + File.separator + System.currentTimeMillis() + "_" + new Random().nextInt(1000) + "." + ImageUtils.getImageType(filePath);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, objectKey, filePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                if (callback != null) {
                    callback.downloadProgress(currentSize / totalSize);
                }
            }
        });
        final String finalObjectKey = objectKey.replace("upload/", "");
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                
                if (callback != null) {
                    callback.onSuccess(finalObjectKey);
                }
            }
            
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                if (callback != null) {
                    callback.onError();
                }
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Logger.e(serviceException.getErrorCode());
                    Logger.e(serviceException.getRequestId());
                    Logger.e(serviceException.getHostId());
                    Logger.e(serviceException.getRawMessage());
                }
            }
        });
        return false;
    }
    
    public boolean uploadFile(String filePath, String objectKey, final ResponseCallback<String> callback) {
        //        String objectKey = GlobalConfig.OSSIMGPATH + UserManager.getInstance().getUserId() + File.separator + System.currentTimeMillis() + "." + ImageUtils.getImageType(filePath);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, objectKey, filePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                if (callback != null) {
                    callback.downloadProgress(currentSize / totalSize);
                }
            }
        });
        final String finalObjectKey = objectKey.replace("upload/", "");
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                
                if (callback != null) {
                    callback.onSuccess(finalObjectKey);
                }
            }
            
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                //                Logger.e("fail===" + serviceException.getRawMessage());
                if (callback != null) {
                    callback.onError();
                }
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Logger.e(serviceException.getErrorCode());
                    Logger.e(serviceException.getRequestId());
                    Logger.e(serviceException.getHostId());
                    Logger.e(serviceException.getRawMessage());
                }
            }
        });
        return false;
    }
    
    
    public void getUpLoadConfig(final ResponseCallback<StorageInfo> callback, final List<String> paths) {
        final LiveStorageTokenRequest request = new LiveStorageTokenRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        Api.getInstance().post(request, LiveStorageTokenResponse.class, new com.haoke91.a91edu.net.ResponseCallback<LiveStorageTokenResponse>() {
            @Override
            public void onResponse(final LiveStorageTokenResponse date, boolean isFromCache) {
                
                CreateUpLoadBean createUpLoadBean = new CreateUpLoadBean();
                createUpLoadBean.setBusinessKey(date.getData().getBusinessKey());
                ArrayList<CreateUpLoadBean.FileInfo> fileInfos = new ArrayList<>();
                for (String info : paths) {
                    fileInfos.add(new CreateUpLoadBean.FileInfo(info, 1));
                    createUpLoadBean.setUploadFiles(fileInfos);
                }
                String body = new Gson().toJson(createUpLoadBean);
                //                final String body = "{\"businessKey\":\"HAOKE\",\"uploadFiles\":[{\"fileNameOrSuffix\":\"源文件.jpg\",\"amount\":1}]}";
                final HttpHeaders headers = new HttpHeaders();
                headers.put("X-Storage-Authorization", date.getData().getAuthorization());
                OkGo.<String>post(date.getData().getUrl()).headers(headers).upJson(body).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            StorageInfo storageInfo = new Gson().fromJson(response.body(), StorageInfo.class);
                            if (storageInfo != null && storageInfo.getStatus() == 1) {
                                if (callback != null) {
                                    callback.onSuccess(storageInfo);
                                }
                            } else {
                                if (callback != null) {
                                    callback.onError();
                                }
                            }
                        } catch (Exception e) {
                        
                        }
                        
                        //                        List<StorageInfo.BodyBean> bodyBeans = storageInfo.getBody();
                        //                        for (StorageInfo.BodyBean bodyBean : bodyBeans) {
                        //                            StorageInfo.BodyBean.UploadInfoBean uploadInfo = bodyBean.getUploadInfo();
                        //
                        //                            HttpParams httpParams = new HttpParams();
                        //                            List<StorageInfo.BodyBean.UploadInfoBean.BodyArrsBean> bodyArrs = uploadInfo.getBodyArrs();
                        //
                        //                            for (StorageInfo.BodyBean.UploadInfoBean.BodyArrsBean bodyArr : bodyArrs) {
                        //                                httpParams.put(bodyArr.getKey(), bodyArr.getValue());
                        //                            }
                        //                            //                               httpParams.put("file", new File(path));
                        //                            OkGo.<String>post(uploadInfo.getUrl()).params(httpParams).execute(new StringCallback() {
                        //                                @Override
                        //                                public void onSuccess(Response<String> response) {
                        //
                        //                                }
                        //
                        //                                @Override
                        //                                public void uploadProgress(Progress progress) {
                        //                                    Log.e("tag", "progress==" + progress.currentSize);
                        //                                }
                        //                            });
                        //                        }
                    }
                    
                    @Override
                    public void onError(Response<String> response) {
                        if (callback != null) {
                            callback.onError();
                        }
                    }
                });
            }
            
            @Override
            public void onError() {
                if (callback != null) {
                    callback.onError();
                }
                
            }
        }, "");
        
    }
    
    public void upLoad(StorageInfo.BodyBean bodyBean, String path, final ResponseCallback<String> callback) {
        HttpParams httpParams = new HttpParams();
        List<StorageInfo.BodyBean.UploadInfoBean.BodyArrsBean> bodyArrs = bodyBean.getUploadInfo().getBodyArrs();
        
        for (StorageInfo.BodyBean.UploadInfoBean.BodyArrsBean bodyArr : bodyArrs) {
            httpParams.put(bodyArr.getKey(), bodyArr.getValue());
        }
        httpParams.put("file", new File(path));
        OkGo.<String>post(bodyBean.getUploadInfo().getUrl()).params(httpParams).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                
                UpLoadResultBean upLoadResultBean = new Gson().fromJson(response.body(), UpLoadResultBean.class);
                if (upLoadResultBean == null) {
                    if (callback != null) {
                        callback.onError();
                    }
                }
                if (upLoadResultBean.getStatus() == 1) {
                    if (callback != null) {
                        callback.onSuccess(upLoadResultBean.getBody().getKey());
                    }
                } else {
                    if (callback != null) {
                        callback.onError();
                    }
                }
            }
            
            @Override
            public void uploadProgress(Progress progress) {
                if (callback != null) {
                    callback.downloadProgress(progress.currentSize / progress.totalSize);
                }
                Log.e("tag", "progress==" + progress.currentSize);
            }
        });
    }
    
    
    public abstract static class ResponseCallback<T> {
        /**
         * @param date date
         */
        public void onSuccess(T date) {
        }
        
        /**
         * @param fraction 下载的进度
         */
        public void downloadProgress(float fraction) {
        }
        
        public void onError() {
        }
    }
}

