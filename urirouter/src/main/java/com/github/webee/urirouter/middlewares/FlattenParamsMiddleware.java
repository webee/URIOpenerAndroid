package com.github.webee.urirouter.middlewares;

import android.os.Bundle;
import android.os.Parcelable;

import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;
import com.github.webee.urirouter.core.RouteContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.github.webee.urirouter.core.Request.EXTRA_PATH_PARAMS;
import static com.github.webee.urirouter.core.Request.EXTRA_QUERY_PARAMS;

/**
 * Created by webee on 17/2/23.
 */

public class FlattenParamsMiddleware implements Middleware {
    private Map<String, String> keyMappings;

    /**
     * init with key mappings.
     * @param mappings ...["keyA=>keyB"]
     */
    public FlattenParamsMiddleware(String ...mappings) {
        if (mappings.length > 0) {
            keyMappings = new HashMap<>();
            for (String mapping : mappings) {
                String[] parts = mapping.split("=>");
                if (parts.length == 2) {
                    keyMappings.put(parts[0], parts[1]);
                }
            }
        }
    }

    @Override
    public Handler process(Handler next) {
        return new MiddlewareHandler(this, next) {
            @Override
            public void handling(Handler next, RouteContext ctx) {
                Bundle data = ctx.request.data;
                // path params
                if (data.containsKey(EXTRA_PATH_PARAMS)) {
                    Bundle pathParams = data.getBundle(EXTRA_PATH_PARAMS);
                    data.remove(EXTRA_PATH_PARAMS);
                    flatten(data, pathParams);
                }

                // query params
                if (data.containsKey(EXTRA_QUERY_PARAMS)) {
                    Bundle queryParams = data.getBundle(EXTRA_QUERY_PARAMS);
                    data.remove(EXTRA_QUERY_PARAMS);
                    flatten(data, queryParams);
                }
                next.handle(ctx);
            }
        };
    }

    private void flatten(Bundle data, Bundle params) {
        for (String key : params.keySet()) {
            String targetKey = keyMappings != null ? keyMappings.get(key) : null;
            if (targetKey == null) {
                targetKey = key;
            }

            Object val = params.get(key);
            if (val instanceof Serializable) {
                data.putSerializable(targetKey, (Serializable) val);
            } else if (val instanceof Parcelable) {
                data.putParcelable(targetKey, (Parcelable) val);
            }
        }
    }
}
