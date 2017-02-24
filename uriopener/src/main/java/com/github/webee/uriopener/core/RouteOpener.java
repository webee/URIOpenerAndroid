package com.github.webee.uriopener.core;

/**
 * Created by webee on 17/2/22.
 */

public class RouteOpener implements Opener {
    public final Router root = new Router();

    @Override
    public boolean open(OpenContext ctx) {
        Route route = root.find(ctx.uri.getPath());
        if (route != null) {
            RouteContext routeCtx = route.genRouteContext(ctx);
            route.handler.handle(routeCtx);
            return true;
        }
        return false;
    }
}
