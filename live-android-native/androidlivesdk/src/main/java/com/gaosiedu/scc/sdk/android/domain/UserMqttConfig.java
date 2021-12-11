package com.gaosiedu.scc.sdk.android.domain;

/**
 * @author chengsa
 * @date 2018/11/20
 * @comment
 */
public class UserMqttConfig {

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * cleanSession
     */
    private Boolean cleanSession;

    /**
     *客户端id
     */
    private String clientId;


    /**
     * 组id
     */
    private String groupId;


    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 父级目录
     */
    private String topic;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Boolean getCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(Boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
