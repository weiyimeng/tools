
package com.haoke91.a91edu.utils.manager;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gaosiedu.live.sdk.android.api.common.dictionary.list.LiveDictionaryListResponse;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.gaosiedu.live.sdk.android.domain.PartnerUserDoamin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoke91.a91edu.GlobalConfig;
import com.haoke91.a91edu.entities.AddressBean;
import com.haoke91.a91edu.entities.UserInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class UserManager {
    private static final String TAG = "UserManager";
    
    private static final String KEY_USER_INFO = "key_user_info";//缓存用户信息
    
    private static UserManager mUserManager;
    
    private UserManager() {
    
    }
    
    public static UserManager getInstance() {
        if (mUserManager == null) {
            mUserManager = new UserManager();
        }
        return mUserManager;
    }
    
    private UserInfo currentUser;
    
    /**
     * 判断是否登录
     *
     * @return
     */
    public boolean isLogin() {
        currentUser = getLoginUser();
        return (!ObjectUtils.isEmpty(currentUser)) && currentUser.getId() != 0;
    }
    
    
    /**
     * 返回当前登录用户
     *
     * @return
     */
    public UserInfo getLoginUser() {
        if (currentUser == null) {
            currentUser = (UserInfo) CacheDiskUtils.getInstance().getSerializable(KEY_USER_INFO);
        }
        if (currentUser == null) {
            currentUser = new UserInfo();
        }
        return currentUser;
    }
    
    
    /**
     * 返回当前登录用户的id
     *
     * @return
     */
    public int getUserId() {
        //        return 38261;
        //        return 5904;
        try {
            int uid = 0;
            if (isLogin() && currentUser != null) {
                uid = currentUser.getId();
            } else {
                if (getLoginUser() != null) {
                    uid = getLoginUser().getId();
                    
                }
            }
            return uid;
            
        } catch (Exception e) {
        
        }
        return 0;
        
    }
    
    
    /**
     * 注销 清空数据 发送注销事件
     */
    public void logout(Context context) {
        saveUserInfo(null);
        
        //        JPushInterface.setAlias(context, "", new TagAliasCallback() {
        //            @Override
        //            public void gotResult(int i, String s, Set<String> set) {
        //
        //            }
        //        });
    }
    
    
    /**
     * 保存用户信息
     */
    public void saveUserInfo(UserInfo user) {
        try {
            this.currentUser = user;
            CacheDiskUtils.getInstance().put(KEY_USER_INFO, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存用户信息
     */
    public void saveToken(String token) {
        SPUtils.getInstance().put("token", token);
    }
    
    public String getToken() {
        return SPUtils.getInstance().getString("token");
    }
    
    public void clearToken() {
        SPUtils.getInstance().remove("token");
    }
    
    public void clearUserInfo() {
        this.currentUser = null;
        CacheDiskUtils.getInstance().remove(KEY_USER_INFO);
        clearToken();
    }
    
    /**
     * 保存课程信息
     *
     * @param date
     */
    public void saveCourseInfo(ArrayList<DictionaryDomain> date) {
        CacheDiskUtils.getInstance().put(GlobalConfig.COURSE_LIST, date);
        
    }
    
    /**
     * 保存学期信息
     *
     * @param date
     */
    public void saveTermInfo(ArrayList<DictionaryDomain> date) {
        CacheDiskUtils.getInstance().put(GlobalConfig.TERM_LIST, date);
        
    }
    
    public ArrayList<DictionaryDomain> getWelCourseInfo() {
        ArrayList<DictionaryDomain> date = (ArrayList<DictionaryDomain>) CacheDiskUtils.getInstance().getSerializable(GlobalConfig.COURSE_LIST);
        
        return date;
        
    }
    
    public ArrayList<DictionaryDomain> getCourseInfo() {
        ArrayList<DictionaryDomain> date = getWelCourseInfo();
        if (ObjectUtils.isEmpty(date)) {
            String value = "[{\"dicName\":\"语文\",\"dicTypeValue\":\"subject\",\"dicValue\":\"1\"},{\"dicName\":\"数学\",\"dicTypeValue\":\"subject\",\"dicValue\":\"2\"},{\"dicName\":\"英语\",\"dicTypeValue\":\"subject\",\"dicValue\":\"3\"},{\"dicName\":\"物理\",\"dicTypeValue\":\"subject\",\"dicValue\":\"4\"},{\"dicName\":\"化学\",\"dicTypeValue\":\"subject\",\"dicValue\":\"5\"},{\"dicName\":\"科学\",\"dicTypeValue\":\"subject\",\"dicValue\":\"11\"},{\"dicName\":\"升学\",\"dicTypeValue\":\"subject\",\"dicValue\":\"10\"}]";
            date = new Gson().fromJson(value, new TypeToken<List<DictionaryDomain>>() {
            }.getType());
        }
        return date;
        
    }
    
    public ArrayList<DictionaryDomain> getWelTermInfo() {
        ArrayList<DictionaryDomain> date = (ArrayList<DictionaryDomain>) CacheDiskUtils.getInstance().getSerializable(GlobalConfig.TERM_LIST);
        return date;
        
    }
    
    
    public ArrayList<DictionaryDomain> getTermInfo() {
        ArrayList<DictionaryDomain> date = getWelTermInfo();
        if (ObjectUtils.isEmpty(date)) {
            String value = "[{\"dicName\":\"春季课\",\"dicTypeValue\":\"term\",\"dicValue\":\"1\"},{\"dicName\":\"暑期课\",\"dicTypeValue\":\"term\",\"dicValue\":\"2\"},{\"dicName\":\"秋季课\",\"dicTypeValue\":\"term\",\"dicValue\":\"3\"},{\"dicName\":\"寒假课\",\"dicTypeValue\":\"term\",\"dicValue\":\"4\"}]";
            date = new Gson().fromJson(value, new TypeToken<List<DictionaryDomain>>() {
            }.getType());
        }
        return date;
    }
    //    public ArrayList<DictionaryDomain> getTermInfo() {
    //        String value = "[{\"dicName\":\"长线课\",\"dicTypeValue\":\"term\",\"dicValue\":\"1\"},{\"dicName\":\"短线课\",\"dicTypeValue\":\"term\",\"dicValue\":\"2\"},{\"dicName\":\"公开课\",\"dicTypeValue\":\"term\",\"dicValue\":\"3\"}]\n";
    //        return new Gson().fromJson(value, new TypeToken<List<DictionaryDomain>>() {
    //        }.getType());
    //    }
    //    private static final int[] COURSES = {0, 1, 2, -1};//课程
    
    public ArrayList<DictionaryDomain> getCourseType() {
        ArrayList<DictionaryDomain> courseTypes = new ArrayList<>();
        courseTypes.add(new DictionaryDomain("全部课程", "0"));
        courseTypes.add(new DictionaryDomain("未结课程", "1"));
        courseTypes.add(new DictionaryDomain("已结课程", "2"));
        courseTypes.add(new DictionaryDomain("已退课程", "-1"));
        
        return courseTypes;
    }
    
    public void cleanCache() {
        try {
            FileUtils.deleteFile(GlobalConfig.defaultCache);
            FileUtils.deleteFile(GlobalConfig.defaultImage);
            File file = new File(GlobalConfig.defaultCache);
            if (!file.exists()) {
                file.mkdirs();
            }
            File image = new File(GlobalConfig.defaultImage);
            if (!image.exists()) {
                image.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cleanImageCache() {
        FileUtils.deleteFile(GlobalConfig.defaultImage);
        try {
            File image = new File(GlobalConfig.defaultImage);
            if (!image.exists()) {
                image.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}




