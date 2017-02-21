package com.github.webee.urirouter.middlewares;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Data;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;

/**
 * Created by webee on 17/2/22.
 */

public class CtxDataMiddleware implements Middleware {
    private Data ctxData;

    public CtxDataMiddleware(Data data) {
        ctxData = data;
    }

    @Override
    public Handler process(final Handler next) {
        return new Handler() {
            @Override
            public void handle(Context ctx) {
                if (ctxData != null) {
                    ctx.data.putAll(ctxData);
                }
                next.handle(ctx);
            }
        };
    }
}
