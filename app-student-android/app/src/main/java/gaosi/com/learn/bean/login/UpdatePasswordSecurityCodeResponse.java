package gaosi.com.learn.bean.login;

import com.gsbaselib.base.bean.BaseData;

/**
 * 同一个账号下的用户列表返回结果
 * Created by pingfu on 2018/4/27.
 */
public class UpdatePasswordSecurityCodeResponse extends BaseData {
    private int isBeiXiao;

    public int getIsBeiXiao() {
        return isBeiXiao;
    }

    public void setIsBeiXiao(int isBeiXiao) {
        this.isBeiXiao = isBeiXiao;
    }
}
