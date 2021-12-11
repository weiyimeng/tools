package com.haoke91.a91edu.presenter;

import com.haoke91.a91edu.model.BaseModel;
import com.haoke91.a91edu.view.ExamCollectionView;

/**
 * 项目名称：91HaoKe_Android
 * 类描述： 错题本
 * 创建人：shichengxiang
 * 创建时间：2018/8/29 2:36
 */
public class ExamCollectionPresenter extends BasePresenter<BaseModel, ExamCollectionView> {
    
    public ExamCollectionPresenter(BaseModel model, ExamCollectionView view){
        super(model, view);
    }
    
    /**
     * 请求错题本
     */
    public void requestExamList(){
    
    }
}
