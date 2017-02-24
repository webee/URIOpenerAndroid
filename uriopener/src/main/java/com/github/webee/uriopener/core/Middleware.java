package com.github.webee.uriopener.core;

/**
 * Created by webee on 17/2/20.
 */

public interface Middleware {
    Handler process(Handler next);
}
