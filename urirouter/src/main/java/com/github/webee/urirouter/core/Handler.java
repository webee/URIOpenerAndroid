package com.github.webee.urirouter.core;

/**
 * Created by webee on 17/2/17.
 */

public interface Handler {
    void handle(RouteContext ctx);
}
