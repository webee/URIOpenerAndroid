package com.github.webee.uriopener.middlewares;

import android.os.Bundle;

import com.github.webee.uriopener.core.RouteContext;
import com.github.webee.uriopener.core.Handler;
import com.github.webee.uriopener.core.Param;
import com.github.webee.uriopener.core.Request;

import java.util.List;

import static com.github.webee.uriopener.core.Request.EXTRA_PATH_PARAMS;

/**
 * Created by webee on 17/2/20.
 */

public class PathParamsMiddleware extends ParseParamsMiddleware {
    public PathParamsMiddleware() {
        super();
    }

    public PathParamsMiddleware(String typeSep) {
        super(typeSep);
    }

    @Override
    public Handler process(final Handler next) {
        return new MiddlewareHandler(this, next) {
            @Override
            public void handling(Handler next, RouteContext ctx) {
                Request request = ctx.request;
                List<Param> pathParams = request.pathParams;
                if (pathParams.size() > 0) {
                    Bundle pathParamsData = new Bundle();
                    for (Param param : pathParams) {
                        ValueParsers.parse(pathParamsData, param.name, TYPE_SEP_REGEX, param.value, parsers);
                    }
                    request.data.putBundle(EXTRA_PATH_PARAMS, pathParamsData);
                }

                next.handle(ctx);
            }
        };
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "typeSeq='" + TYPE_SEP_REGEX + '\'' +
                "}";
    }
}
