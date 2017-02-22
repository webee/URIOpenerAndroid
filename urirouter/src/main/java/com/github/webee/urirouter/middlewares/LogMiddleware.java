package com.github.webee.urirouter.middlewares;

import android.util.Log;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;

/**
 * Created by webee on 17/2/20.
 */

public class LogMiddleware implements Middleware {
    public static final String TAG = "HANDLER.LOG";

    @Override
    public Handler process(final Handler next) {
        return new Handler() {
            @Override
            public void handle(Context ctx) {
                Log.d(TAG, String.format("START: %d", System.currentTimeMillis()));
                Log.d(TAG, String.format("context: %s", ctx.context));
                Log.d(TAG, String.format("ctxData: %s", ctx.data));
                Log.d(TAG, String.format("request: %s", ctx.request));
                Log.d(TAG, String.format("response: %s", ctx.response));
                Log.d(TAG, "...");

                next.handle(ctx);
            }
        };
    }
}
