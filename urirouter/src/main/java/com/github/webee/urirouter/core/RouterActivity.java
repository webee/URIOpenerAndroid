package com.github.webee.urirouter.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class RouterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            URIRouters.open(this, intent.getData(), null, intent.getExtras());
        }
        finish();
    }
}
