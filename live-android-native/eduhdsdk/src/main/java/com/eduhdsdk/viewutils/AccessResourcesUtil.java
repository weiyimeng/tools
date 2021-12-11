package com.eduhdsdk.viewutils;

import android.content.Context;
import android.content.res.TypedArray;

import com.eduhdsdk.R;

public class AccessResourcesUtil {

    public static int[] getRes(Context context) {
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.loading);
        int len = typedArray.length();
        int[] resId = new int[len];
        for (int i = 0; i < len; i++) {
            resId[i] = typedArray.getResourceId(i, -1);
        }
        typedArray.recycle();
        return resId;
    }
}
