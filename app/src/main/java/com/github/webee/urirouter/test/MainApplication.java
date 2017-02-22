package com.github.webee.urirouter.test;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;
import com.github.webee.urirouter.core.Router;
import com.github.webee.urirouter.core.URIRouters;
import com.github.webee.urirouter.handlers.ActivityHandler;
import com.github.webee.urirouter.middlewares.HandleCtxMiddleware;
import com.github.webee.urirouter.middlewares.LogMiddleware;
import com.github.webee.urirouter.middlewares.PathParamsMiddleware;
import com.github.webee.urirouter.middlewares.QueryParamsMiddleware;
import com.github.webee.urirouter.openers.LogOpener;
import com.github.webee.urirouter.test.middlewares.LoginMiddleware;

/**
 * Created by webee on 17/2/20.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        URIRouters.registerOpener(new LogOpener());
        Router root = URIRouters.root;
        root.use(new LogMiddleware(),
                new PathParamsMiddleware(),
                new QueryParamsMiddleware(),
                new HandleCtxMiddleware(new Handler() {
                    @Override
                    public void handle(Context ctx) {
                        ctx.setData(ActivityHandler.ctxData(ctx.data).withFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).build());
                    }
                }));

        root.add("/", ActivityHandler.create(MainActivity.class));
        root.add("/login/", ActivityHandler.create(LoginActivity.class));
        root.add("/xxxx/*", ActivityHandler.create(NotImplementedActivity.class));

        // user router.
        Router userRouter = root.mount("/user");
        Middleware loginMiddleware = new LoginMiddleware("/login/");
        userRouter.use(loginMiddleware);
        userRouter.add("/:uid/:age@int/", ActivityHandler.create(UserActivity.class));
        userRouter.add("/*", ActivityHandler.create(TestActivity.class));

        // test router.
        Router testRouter = root.mount("/test", true);
        testRouter.add("/", ActivityHandler.create(TestActivity.class));
        testRouter.add("/result/", ActivityHandler.create(ResultActivity.class), loginMiddleware);
        testRouter.add("/toast", new Handler() {
            @Override
            public void handle(Context ctx) {
                Toast.makeText(ctx.context, ctx.request.uri.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
