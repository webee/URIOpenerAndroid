package com.github.webee.uriopener.openctxprocessors;

import android.net.Uri;

import com.github.webee.uriopener.core.OpenContext;
import com.github.webee.uriopener.core.OpenContextProcessor;
import com.github.webee.uriopener.handlers.ActivityHandler;

/**
 * Created by webee on 17/2/23.
 */

public class ExtractActivityRequestCodeOpenCtxProcessor implements OpenContextProcessor {
    @Override
    public OpenContext process(OpenContext ctx) {
        Uri uri = ctx.uri;
        if (uri != null) {
            try {
                String val = uri.getQueryParameter(ActivityHandler.QUERY_PARAM_NAME_ACTIVITY_REQUEST_CODE);
                if (val != null) {
                    int code = Integer.parseInt(val);
                    ctx.setCtxData(ActivityHandler.ctxData(ctx.ctxData).withRequestCode(code).build());
                }
            } catch (Throwable r) {
                r.printStackTrace();
            }
        }
        return ctx;
    }
}
