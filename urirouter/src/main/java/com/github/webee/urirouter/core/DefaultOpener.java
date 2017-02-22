package com.github.webee.urirouter.core;

import android.net.Uri;
import android.os.Bundle;

/**
 * Created by webee on 17/2/22.
 */

public class DefaultOpener implements Opener {
    @Override
    public boolean open(android.content.Context context, Uri uri, Route route, Data ctxData, Bundle reqData) {
        if (route != null) {
            Request request = new Request(uri, route.pathParams);
            if (reqData != null) {
                request.data.putAll(reqData);
            }

            Context ctx = new Context(context, request, null);
            if (ctxData != null) {
                ctx.data.putAll(ctxData);
            }
            route.handler.handle(ctx);
            return true;
        }
        return false;
    }
}
