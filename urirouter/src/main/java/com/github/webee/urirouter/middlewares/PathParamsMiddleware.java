package com.github.webee.urirouter.middlewares;

import android.os.Bundle;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Param;
import com.github.webee.urirouter.core.Request;

import java.util.List;

import static com.github.webee.urirouter.core.Request.EXTRA_PATH_PARAMS;

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
        return new Handler() {
            @Override
            public void handle(Context ctx) {
                Request request = ctx.request;
                List<Param> pathParams = request.pathParams;
                if (pathParams.size() > 0) {
                    Bundle data = request.data;
                    Bundle pathParamsData = new Bundle();
                    for (Param param : pathParams) {
                        ValueParsers.parse(pathParamsData, param.name, TYPE_SEP_REGEX, param.value, parsers);
                    }
                    data.putBundle(EXTRA_PATH_PARAMS, pathParamsData);
                }

                next.handle(ctx);
            }
        };
    }
}
