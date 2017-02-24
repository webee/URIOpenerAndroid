package com.github.webee.uriopener.openers;

import android.util.Log;

import com.github.webee.uriopener.core.OpenContext;
import com.github.webee.uriopener.core.Opener;

/**
 * Created by webee on 17/2/22.
 */

public class LogOpener implements Opener {
    public static final String TAG = "OPENER.LOG";

    @Override
    public boolean open(OpenContext ctx) {
        Log.d(TAG, String.format("START: %d", System.currentTimeMillis()));
        Log.d(TAG, String.format("context: %s", ctx.context));
        Log.d(TAG, String.format("uri: %s", ctx.uri));
        Log.d(TAG, String.format("ctxData: %s", ctx.ctxData));
        Log.d(TAG, String.format("reqData: %s", ctx.reqData));
        Log.d(TAG, "...");
        return false;
    }
}
