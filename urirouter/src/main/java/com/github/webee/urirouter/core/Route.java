package com.github.webee.urirouter.core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by webee on 17/2/17.
 */

public class Route {
    public List<Param> pathParams = new LinkedList<>();
    public Handler handler;
    private boolean finalized = false;

    private Route(Handler handler, Param... pathParams) {
        this.handler = handler;
        this.pathParams.addAll(Arrays.asList(pathParams));
    }

    private Route(Route route, Param... pathParams) {
        this(route.handler, pathParams);
        this.pathParams.addAll(route.pathParams);
    }

    public void setFinalized() {
        finalized = true;
    }

    public Route applyMiddlewares(Middleware... middlewares) {
        return applyMiddlewares(Arrays.asList(middlewares));
    }

    public Route applyMiddlewares(List<Middleware> middlewares) {
        if (!finalized) {
            for (int i = middlewares.size() - 1; i >= 0; i--) {
                handler = middlewares.get(i).process(handler);
            }
        }
        // 路由生成已经结束
        return this;
    }

    public static Route create(Handler handler, Param... pathParams) {
        if (handler == null) {
            return null;
        }
        return new Route(handler, pathParams);
    }

    public static Route create(Route route, Param... pathParams) {
        if (route == null) {
            return null;
        }
        return new Route(route, pathParams);
    }

    @Override
    public String toString() {
        return "Route{" +
                "pathParams=" + pathParams +
                ", hasHandler=" + (handler != null) +
                ", finalized=" + finalized +
                '}';
    }
}
