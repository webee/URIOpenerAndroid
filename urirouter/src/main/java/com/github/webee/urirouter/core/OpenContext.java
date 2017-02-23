package com.github.webee.urirouter.core;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by webee on 17/2/23.
 */

public class OpenContext {
    public Context context;
    public Uri uri;
    public Data ctxData;
    public Bundle reqData;

    public OpenContext(android.content.Context context, Uri uri, Data ctxData, Bundle reqData) {
        this.context = context;
        this.uri = uri;
        this.ctxData = ctxData;
        this.reqData = reqData;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setCtxData(Data ctxData) {
        this.ctxData = ctxData;
    }

    public void setReqData(Bundle reqData) {
        this.reqData = reqData;
    }
}
