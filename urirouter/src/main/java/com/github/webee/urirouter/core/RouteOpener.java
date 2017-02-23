package com.github.webee.urirouter.core;

import android.net.Uri;
import android.os.Bundle;

/**
 * Created by webee on 17/2/22.
 */

public class RouteOpener implements Opener {
    @Override
    public boolean open(android.content.Context context, Uri uri, Data ctxData, Bundle reqData) {
        Route route = URIRouters.root.find(uri.getPath());
        if (route != null) {
            RouteContext ctx = route.genContext(context, uri, ctxData, reqData);
            route.handler.handle(ctx);
            return true;
        }
        return false;
    }
}
