package com.github.webee.urirouter.openers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.github.webee.urirouter.core.Data;
import com.github.webee.urirouter.core.Opener;

/**
 * Created by webee on 17/2/22.
 */

public class LogOpener implements Opener {
    public static final String TAG = "OPENER.LOG";

    @Override
    public boolean open(Context context, Uri uri, Data ctxData, Bundle reqData) {
        Log.d(TAG, String.format("START: %d", System.currentTimeMillis()));
        Log.d(TAG, String.format("context: %s", context));
        Log.d(TAG, String.format("uri: %s", uri));
        Log.d(TAG, String.format("ctxData: %s", ctxData));
        Log.d(TAG, String.format("reqData: %s", reqData));
        Log.d(TAG, "...");
        return false;
    }
}
