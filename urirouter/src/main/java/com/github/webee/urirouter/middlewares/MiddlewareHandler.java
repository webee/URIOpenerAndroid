package com.github.webee.urirouter.middlewares;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;

/**
 * Created by webee on 17/2/23.
 */

public abstract class MiddlewareHandler implements Handler {
    private Middleware middleware;
    private Handler handler;

    public MiddlewareHandler(Middleware middleware, Handler handler) {
        this.middleware = middleware;
        this.handler = handler;
    }

    public abstract void handling(Handler next, Context ctx);

    @Override
    public void handle(Context ctx) {
        handling(handler, ctx);
    }

    @Override
    public String toString() {
        return String.format("%s\n\t->%s", middleware.toString(), handler.toString());
    }
}
