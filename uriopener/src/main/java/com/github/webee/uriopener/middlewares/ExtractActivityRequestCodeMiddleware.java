package com.github.webee.uriopener.middlewares;

import android.net.Uri;

import com.github.webee.uriopener.core.Handler;
import com.github.webee.uriopener.core.Middleware;
import com.github.webee.uriopener.core.RouteContext;
import com.github.webee.uriopener.handlers.ActivityHandler;

/**
 * Created by webee on 17/2/24.
 */

public class ExtractActivityRequestCodeMiddleware implements Middleware {
    @Override
    public Handler process(Handler next) {
        return new MiddlewareHandler(this, next) {
            @Override
            public void handling(Handler next, RouteContext ctx) {
                Uri uri = ctx.request.uri;
                if (uri != null) {
                    try {
                        String val = uri.getQueryParameter(ActivityHandler.QUERY_PARAM_NAME_ACTIVITY_REQUEST_CODE);
                        if (val != null) {
                            int code = Integer.parseInt(val);
                            ctx.setData(ActivityHandler.ctxData(ctx.data).withRequestCode(code).build());
                        }
                    } catch (Throwable r) {
                        r.printStackTrace();
                    }
                }
                next.handle(ctx);
            }
        };
    }
}
