package com.github.webee.uriopener.openctxprocessors;

import android.app.Application;

import com.github.webee.uriopener.core.OpenContext;
import com.github.webee.uriopener.core.OpenContextProcessor;

/**
 * Created by webee on 17/2/24.
 */

public class UseAppContextOpenCtxProcessor implements OpenContextProcessor {
    private Application app;

    public UseAppContextOpenCtxProcessor(Application app) {
        this.app = app;
    }

    @Override
    public OpenContext process(OpenContext ctx) {
        if (ctx.context == null) {
            ctx.setContext(app);
        }
        return ctx;
    }
}
