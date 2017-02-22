package com.github.webee.urirouter.handlers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.webee.urirouter.core.Data;
import com.github.webee.urirouter.core.URIRouters;

public class RouterActivity extends Activity {
    private static Data ctxData;

    public static void setCtxData(Data data) {
        ctxData = data;
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
}
