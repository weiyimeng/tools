package com.haoke91.a91edu.utils.imageloader;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.net.Api;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/9 上午9:58
 * 修改人：weiyimeng
 * 修改时间：2018/7/9 上午9:58
 * 修改备注：
 */
public class GlideUtils {
    
    
    public GlideUtils(Context context) {
        //        this.context = context;
    }
    
    public static void load(Context context, String imageUrl, ImageView view) {
        if (TextUtils.isEmpty(imageUrl)) {
            Glide.with(context).load(R.mipmap.empty_small).into(view);
            return;
        }
        if (!imageUrl.startsWith("https") && !imageUrl.startsWith("http") && !imageUrl.startsWith("/storage")) {
            //https://img.91haoke.com/upload/v21/user/portrait/7919/1521165372525_195.jpg
            imageUrl = Api.baseImgUrl + imageUrl;
        }
        Glide.with(context)
            .load(imageUrl)
            .apply(new RequestOptions().error(R.mipmap.empty_small).placeholder(R.mipmap.empty_small)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).fitCenter())
            .into(view);
    }
    
    public static void loadHead(Context context, String imageUrl, ImageView view) {
        if (TextUtils.isEmpty(imageUrl)) {
            Glide.with(context).load(R.mipmap.study_image_head).into(view);
            return;
        }
        if (!imageUrl.startsWith("https") && !imageUrl.startsWith("http") && !imageUrl.startsWith("/storage")) {
            //https://img.91haoke.com/upload/v21/user/portrait/7919/1521165372525_195.jpg
            imageUrl = Api.baseImgUrl + imageUrl;
        }
        Glide.with(context)
            .load(imageUrl + "?imageView2/0/w/100/h/100")
            .apply(new RequestOptions().error(R.mipmap.study_image_head).placeholder(R.mipmap.study_image_head)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).fitCenter())
            .into(view);
    }
    
    public static void loadLocal(Context context, String imageUrl, ImageView view) {
        
        Glide.with(context)
            .load(imageUrl)
            .apply(new RequestOptions().error(R.mipmap.empty_small)
                .placeholder(R.mipmap.empty_small)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).fitCenter())
            .into(view);
    }
}
