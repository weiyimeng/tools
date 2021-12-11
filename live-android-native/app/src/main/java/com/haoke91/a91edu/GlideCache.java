package com.haoke91.a91edu;

import android.content.Context;
import android.os.Parcelable;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/7 下午8:06
 * 修改人：weiyimeng
 * 修改时间：2018/7/7 下午8:06
 * 修改备注：
 */
@GlideModule
public class GlideCache extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
    }
}
