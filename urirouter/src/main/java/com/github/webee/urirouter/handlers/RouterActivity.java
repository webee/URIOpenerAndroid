package com.github.webee.urirouter.handlers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.webee.urirouter.core.Data;
import com.github.webee.urirouter.core.URIRouters;

/**
 * external entry.
 */
public class RouterActivity extends Activity {
    private static final String IS_FROM_EXTERNAL = RouterActivity.class.getName() + ".is_from_external";
    private static Data ctxData = new Data();

    static {
        ctxData.put(IS_FROM_EXTERNAL, true);
    }

    public static void setCtxData(Data data) {
        ctxData.putAll(data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            Bundle extras = intent.getExtras();

            URIRouters.open(this, uri, ctxData, extras);
        }
        finish();
    }

    public static boolean isFromExternal(Data ctxData) {
        if (ctxData != null) {
            Boolean t = ctxData.get(IS_FROM_EXTERNAL);
            return t != null && t;
        }
        return false;
    }
}
