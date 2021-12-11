package com.gaosiedu.live.sdk.android.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * MyApplication
 *
 * @author lianyutao
 * @date 2018/8/21
 * @description ${DESCRIPTION}
 */
public class LiveSdkParameterizedType implements ParameterizedType {

    private final Class raw;

    private final Type[] types;

    public LiveSdkParameterizedType(Class raw, Type[] types) {
        this.raw = raw;
        this.types = types;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return types;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
