package com.github.webee.urirouter.middlewares;

import android.util.Log;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;

/**
 * Created by webee on 17/2/20.
 */

public class LogMiddleware implements Middleware {
    @Override
    public Handler process(final Handler next) {
        return new Handler() {
            @Override
            public void handle(Context ctx) {
                Log.d("ROUTER.LOG", String.format("from: %s", ctx.context.toString()));
                Log.d("ROUTER.LOG", String.format("to: %s", ctx.request.uri.toString()));
                Log.d("ROUTER.LOG", String.format("ctx: %s", ctx.data));
                Log.d("ROUTER.LOG", String.format("req: %s", ctx.request.data));

                next.handle(ctx);
            }
        };
    }
}
