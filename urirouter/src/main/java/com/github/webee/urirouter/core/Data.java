package com.github.webee.urirouter.core;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by webee on 17/2/21.
 */

public class Data {
    // 可远程传递数据
    public Bundle bundle;
    // 本地数据
    private Map<String, Object> data;

    public Data() {
        bundle = new Bundle();
        data = new HashMap<>();
    }

    public Data(Bundle bundle) {
        if (bundle != null) {
            this.bundle = bundle;
        } else {
            this.bundle = new Bundle();
        }
        data = new HashMap<>();
    }

    public Data put(String key, Object val) {
        data.put(key, val);
        return this;
    }

    public Data putAll(Data dt) {
        bundle.putAll(dt.bundle);
        data.putAll(dt.data);
        return this;
    }

    public <T> T get(String key) {
        Object val = data.get(key);
        if (val == null) {
            val = bundle.get(key);
        }
        return (T) val;
    }

    public boolean containsKey(String key) {
        return data.containsKey(key) || bundle.containsKey(key);
    }

    @Override
    public String toString() {
        return "Data{" +
                "bundle=" + bundle +
                ", data=" + data +
                '}';
    }
}
