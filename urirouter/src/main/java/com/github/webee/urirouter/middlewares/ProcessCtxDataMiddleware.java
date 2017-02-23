package com.github.webee.urirouter.middlewares;

import com.github.webee.urirouter.core.RouteContext;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;

/**
 * Created by webee on 17/2/22.
 */

public class ProcessCtxDataMiddleware implements Middleware {
    private CtxDataProcessor processor;

    public ProcessCtxDataMiddleware(CtxDataProcessor processor) {
        this.processor = processor;
    }

    @Override
    public Handler process(final Handler next) {
        return new MiddlewareHandler(this, next) {
            @Override
            public void handling(Handler next, RouteContext ctx) {
                if (processor != null) {
                    ctx.setData(processor.process(ctx.data));
                }
                next.handle(ctx);
            }
        };
    }
}
