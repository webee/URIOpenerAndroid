package com.github.webee.urirouter.middlewares;

import android.net.Uri;
import android.os.Bundle;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;
import com.github.webee.urirouter.core.Param;
import com.github.webee.urirouter.core.Request;

import java.util.List;
import java.util.Set;

import static com.github.webee.urirouter.core.Request.EXTRA_QUERY_PARAMS;

/**
 * Created by webee on 17/2/20.
 */

public class QueryParamsMiddleware implements Middleware {
    private String TYPE_SEP_REGEX = "T";

    public QueryParamsMiddleware() {
    }

    public QueryParamsMiddleware(String typeSep) {
        TYPE_SEP_REGEX = typeSep;
    }

    @Override
    public Handler process(final Handler next) {
        return new Handler() {
            @Override
            public void handle(Context ctx) {
                Request request = ctx.request;
                List<Param> pathParams = request.pathParams;
                Uri uri = ctx.request.uri;
                Set<String> keys = uri.getQueryParameterNames();
                if (keys.size() > 0) {
                    Bundle queryParamsData = new Bundle();
                    for (String key : keys) {
                        List<String> vals = uri.getQueryParameters(key);
                        for (int i = 0; i < vals.size(); i++) {
                            ValueParser.parse(queryParamsData, key, i == 0 ? null : String.valueOf(i+1), TYPE_SEP_REGEX, vals.get(i));
                        }
                    }

                    request.data.putBundle(EXTRA_QUERY_PARAMS, queryParamsData);
                }

                next.handle(ctx);
            }
        };
    }
}
