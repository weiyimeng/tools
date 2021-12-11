package com.haoke91.a91edu.entities;

import java.util.List;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/12/5 3:34 PM
 * 修改人：weiyimeng
 * 修改时间：2018/12/5 3:34 PM
 * 修改备注：
 */
public class StorageInfo {
    
    /**
     * status : 1
     * errorCode : 0
     * errorMessage :
     * body : [{"fileNameOrSuffix":"源文件.png","uploadInfo":{"url":"https://upload.qiniup.com","bodyArrs":[{"key":"token","value":"WR5JtPxdsdYOgladSBDXZl3idmqjBzqkX5E-UDfG:Uu6FfWdYPEV4z0UQyWeKF9mgmhA=:eyJpbnNlcnRPbmx5IjoxLCJjYWxsYmFja0JvZHlUeXBlIjoiYXBwbGljYXRpb24vanNvbiIsInNjb3BlIjoiYXh4LXFpdXJpZGVzaXl1OkI6MTAxOTpLLzE1NDM5MzkyMDAvOWY3ZDIwMTg5MTExNDk1NmIwMDUzMTIxMDA2NjZiNjAucG5nIiwiY2FsbGJhY2tVcmwiOiJodHRwOi8vMjE5LjE0Mi4xMDQuMTMxOjkwMTkvY2FsbGJhY2svcWluaXUvdXBsb2FkIiwiZGVhZGxpbmUiOjE1NDQwMDk1NjgsImNhbGxiYWNrQm9keSI6IntcImJ1Y2tldFwiOlwiJChidWNrZXQpXCIsXCJvcmlnaW5hbEZpbGVOYW1lXCI6XCLmupDmlofku7YucG5nXCIsXCJmbmFtZVwiOlwiJChmbmFtZSlcIixcImJ1Y2tldFR5cGVcIjpcIlFJTklVXCIsXCJmc2l6ZVwiOlwiJChmc2l6ZSlcIixcImJ1c2luZXNzS2V5XCI6XCJIQU9LRVwiLFwiZXRhZ1wiOlwiJChldGFnKVwiLFwibWltZVR5cGVcIjpcIiQobWltZVR5cGUpXCIsXCJ1cGxvYWRTaWduYXR1cmVTb3VyY2VcIjpcIk9QRU5fQVBJX1VQTE9BRFwiLFwia2V5XCI6XCIkKGtleSlcIn0ifQ=="},{"key":"fileName","value":"源文件.png"},{"key":"key","value":"B:1019:K/1543939200/9f7d201891114956b005312100666b60.png"}],"fileKey":"file"}}]
     */
    
    private int status;
    private int errorCode;
    private String errorMessage;
    private List<BodyBean> body;
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public List<BodyBean> getBody() {
        return body;
    }
    
    public void setBody(List<BodyBean> body) {
        this.body = body;
    }
    
    public static class BodyBean {
        /**
         * fileNameOrSuffix : 源文件.png
         * uploadInfo : {"url":"https://upload.qiniup.com","bodyArrs":[{"key":"token","value":"WR5JtPxdsdYOgladSBDXZl3idmqjBzqkX5E-UDfG:Uu6FfWdYPEV4z0UQyWeKF9mgmhA=:eyJpbnNlcnRPbmx5IjoxLCJjYWxsYmFja0JvZHlUeXBlIjoiYXBwbGljYXRpb24vanNvbiIsInNjb3BlIjoiYXh4LXFpdXJpZGVzaXl1OkI6MTAxOTpLLzE1NDM5MzkyMDAvOWY3ZDIwMTg5MTExNDk1NmIwMDUzMTIxMDA2NjZiNjAucG5nIiwiY2FsbGJhY2tVcmwiOiJodHRwOi8vMjE5LjE0Mi4xMDQuMTMxOjkwMTkvY2FsbGJhY2svcWluaXUvdXBsb2FkIiwiZGVhZGxpbmUiOjE1NDQwMDk1NjgsImNhbGxiYWNrQm9keSI6IntcImJ1Y2tldFwiOlwiJChidWNrZXQpXCIsXCJvcmlnaW5hbEZpbGVOYW1lXCI6XCLmupDmlofku7YucG5nXCIsXCJmbmFtZVwiOlwiJChmbmFtZSlcIixcImJ1Y2tldFR5cGVcIjpcIlFJTklVXCIsXCJmc2l6ZVwiOlwiJChmc2l6ZSlcIixcImJ1c2luZXNzS2V5XCI6XCJIQU9LRVwiLFwiZXRhZ1wiOlwiJChldGFnKVwiLFwibWltZVR5cGVcIjpcIiQobWltZVR5cGUpXCIsXCJ1cGxvYWRTaWduYXR1cmVTb3VyY2VcIjpcIk9QRU5fQVBJX1VQTE9BRFwiLFwia2V5XCI6XCIkKGtleSlcIn0ifQ=="},{"key":"fileName","value":"源文件.png"},{"key":"key","value":"B:1019:K/1543939200/9f7d201891114956b005312100666b60.png"}],"fileKey":"file"}
         */
        
        private String fileNameOrSuffix;
        private UploadInfoBean uploadInfo;
        
        public String getFileNameOrSuffix() {
            return fileNameOrSuffix;
        }
        
        public void setFileNameOrSuffix(String fileNameOrSuffix) {
            this.fileNameOrSuffix = fileNameOrSuffix;
        }
        
        public UploadInfoBean getUploadInfo() {
            return uploadInfo;
        }
        
        public void setUploadInfo(UploadInfoBean uploadInfo) {
            this.uploadInfo = uploadInfo;
        }
        
        public static class UploadInfoBean {
            /**
             * url : https://upload.qiniup.com
             * bodyArrs : [{"key":"token","value":"WR5JtPxdsdYOgladSBDXZl3idmqjBzqkX5E-UDfG:Uu6FfWdYPEV4z0UQyWeKF9mgmhA=:eyJpbnNlcnRPbmx5IjoxLCJjYWxsYmFja0JvZHlUeXBlIjoiYXBwbGljYXRpb24vanNvbiIsInNjb3BlIjoiYXh4LXFpdXJpZGVzaXl1OkI6MTAxOTpLLzE1NDM5MzkyMDAvOWY3ZDIwMTg5MTExNDk1NmIwMDUzMTIxMDA2NjZiNjAucG5nIiwiY2FsbGJhY2tVcmwiOiJodHRwOi8vMjE5LjE0Mi4xMDQuMTMxOjkwMTkvY2FsbGJhY2svcWluaXUvdXBsb2FkIiwiZGVhZGxpbmUiOjE1NDQwMDk1NjgsImNhbGxiYWNrQm9keSI6IntcImJ1Y2tldFwiOlwiJChidWNrZXQpXCIsXCJvcmlnaW5hbEZpbGVOYW1lXCI6XCLmupDmlofku7YucG5nXCIsXCJmbmFtZVwiOlwiJChmbmFtZSlcIixcImJ1Y2tldFR5cGVcIjpcIlFJTklVXCIsXCJmc2l6ZVwiOlwiJChmc2l6ZSlcIixcImJ1c2luZXNzS2V5XCI6XCJIQU9LRVwiLFwiZXRhZ1wiOlwiJChldGFnKVwiLFwibWltZVR5cGVcIjpcIiQobWltZVR5cGUpXCIsXCJ1cGxvYWRTaWduYXR1cmVTb3VyY2VcIjpcIk9QRU5fQVBJX1VQTE9BRFwiLFwia2V5XCI6XCIkKGtleSlcIn0ifQ=="},{"key":"fileName","value":"源文件.png"},{"key":"key","value":"B:1019:K/1543939200/9f7d201891114956b005312100666b60.png"}]
             * fileKey : file
             */
            
            private String url;
            private String fileKey;
            private List<BodyArrsBean> bodyArrs;
            
            public String getUrl() {
                return url;
            }
            
            public void setUrl(String url) {
                this.url = url;
            }
            
            public String getFileKey() {
                return fileKey;
            }
            
            public void setFileKey(String fileKey) {
                this.fileKey = fileKey;
            }
            
            public List<BodyArrsBean> getBodyArrs() {
                return bodyArrs;
            }
            
            public void setBodyArrs(List<BodyArrsBean> bodyArrs) {
                this.bodyArrs = bodyArrs;
            }
            
            public static class BodyArrsBean {
                /**
                 * key : token
                 * value : WR5JtPxdsdYOgladSBDXZl3idmqjBzqkX5E-UDfG:Uu6FfWdYPEV4z0UQyWeKF9mgmhA=:eyJpbnNlcnRPbmx5IjoxLCJjYWxsYmFja0JvZHlUeXBlIjoiYXBwbGljYXRpb24vanNvbiIsInNjb3BlIjoiYXh4LXFpdXJpZGVzaXl1OkI6MTAxOTpLLzE1NDM5MzkyMDAvOWY3ZDIwMTg5MTExNDk1NmIwMDUzMTIxMDA2NjZiNjAucG5nIiwiY2FsbGJhY2tVcmwiOiJodHRwOi8vMjE5LjE0Mi4xMDQuMTMxOjkwMTkvY2FsbGJhY2svcWluaXUvdXBsb2FkIiwiZGVhZGxpbmUiOjE1NDQwMDk1NjgsImNhbGxiYWNrQm9keSI6IntcImJ1Y2tldFwiOlwiJChidWNrZXQpXCIsXCJvcmlnaW5hbEZpbGVOYW1lXCI6XCLmupDmlofku7YucG5nXCIsXCJmbmFtZVwiOlwiJChmbmFtZSlcIixcImJ1Y2tldFR5cGVcIjpcIlFJTklVXCIsXCJmc2l6ZVwiOlwiJChmc2l6ZSlcIixcImJ1c2luZXNzS2V5XCI6XCJIQU9LRVwiLFwiZXRhZ1wiOlwiJChldGFnKVwiLFwibWltZVR5cGVcIjpcIiQobWltZVR5cGUpXCIsXCJ1cGxvYWRTaWduYXR1cmVTb3VyY2VcIjpcIk9QRU5fQVBJX1VQTE9BRFwiLFwia2V5XCI6XCIkKGtleSlcIn0ifQ==
                 */
                
                private String key;
                private String value;
                
                public String getKey() {
                    return key;
                }
                
                public void setKey(String key) {
                    this.key = key;
                }
                
                public String getValue() {
                    return value;
                }
                
                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }
}
