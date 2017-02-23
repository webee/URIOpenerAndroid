package com.github.webee.urirouter.middlewares;

import android.net.Uri;
import android.os.Bundle;

import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Request;
import com.github.webee.urirouter.core.RouteContext;

import java.util.List;
import java.util.Set;

import static com.github.webee.urirouter.core.Request.EXTRA_QUERY_PARAMS;

/**
 * Created by webee on 17/2/20.
 */

public class QueryParamsMiddleware extends ParseParamsMiddleware {
    public QueryParamsMiddleware() {
        super("T");
    }

    public QueryParamsMiddleware(String typeSep) {
        super(typeSep);
    }

    @Override
    public Handler process(final Handler next) {
        return new MiddlewareHandler(this, next) {
            @Override
            public void handling(Handler next, RouteContext ctx) {
                Request request = ctx.request;
                Uri uri = ctx.request.uri;
                Set<String> keys = uri.getQueryParameterNames();
                if (keys.size() > 0) {
                    Bundle queryParamsData = new Bundle();
                    for (String key : keys) {
                        List<String> vals = uri.getQueryParameters(key);
                        for (int i = 0; i < vals.size(); i++) {
                            ValueParsers.parse(queryParamsData, key, i == 0 ? null : String.valueOf(i+1), TYPE_SEP_REGEX, vals.get(i), parsers);
                        }
                    }

                    request.data.putBundle(EXTRA_QUERY_PARAMS, queryParamsData);
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
