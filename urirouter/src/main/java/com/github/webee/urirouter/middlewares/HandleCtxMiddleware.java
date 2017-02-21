package com.github.webee.urirouter.middlewares;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;

/**
 * Created by webee on 17/2/22.
 */

public class HandleCtxMiddleware implements Middleware {
    private Handler handler;

    public HandleCtxMiddleware(Handler handler) {
        this.handler = handler;
    }

    @Override
    public Handler process(final Handler next) {
        return new Handler() {
            @Override
            public void handle(Context ctx) {
                if (handler != null) {
                    handler.handle(ctx);
                }
                next.handle(ctx);
            }
        };
    }
}
