package com.github.webee.urirouter.middlewares;

import com.github.webee.urirouter.core.RouteContext;
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

    public abstract void handling(Handler next, RouteContext ctx);

    @Override
    public void handle(RouteContext ctx) {
        handling(handler, ctx);
    }

    @Override
    public String toString() {
        return String.format("%s\n\t->%s", middleware.toString(), handler.toString());
    }
}
