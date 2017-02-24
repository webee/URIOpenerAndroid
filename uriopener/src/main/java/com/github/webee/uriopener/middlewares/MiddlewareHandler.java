package com.github.webee.uriopener.middlewares;

import com.github.webee.uriopener.core.RouteContext;
import com.github.webee.uriopener.core.Handler;
import com.github.webee.uriopener.core.Middleware;

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
