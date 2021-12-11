package com.haoke91.a91edu.presenter;

import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.mine.LiveSccUserWrongQuestionBookDetailRequest;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.mine.LiveSccUserWrongQuestionBookDetailResponse;
import com.haoke91.a91edu.model.BaseModel;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.view.WrongExamBookView;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/29 16:53
 */
public class WrongExamBookPresenter extends BasePresenter<BaseModel, WrongExamBookView> {
    public WrongExamBookPresenter(BaseModel model, WrongExamBookView view){
        super(model, view);
    }
    
    public void getWrongList(){
        LiveSccUserWrongQuestionBookDetailRequest request = new LiveSccUserWrongQuestionBookDetailRequest();
        request.setUserId(UserManager.getInstance().getUserId()+"");
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionBookDetailResponse.class, new ResponseCallback<LiveSccUserWrongQuestionBookDetailResponse>() {
            @Override
            public void onResponse(LiveSccUserWrongQuestionBookDetailResponse date, boolean isFromCache){
                if(date.getData()!=null && date.getData().getList()!=null){
                    mView.onSuccessGetBook();
                }
            }
        }, "");
    }
}
