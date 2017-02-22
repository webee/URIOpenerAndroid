package com.github.webee.urirouter.test;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;
import com.github.webee.urirouter.core.Router;
import com.github.webee.urirouter.core.URIRouters;
import com.github.webee.urirouter.handlers.ActivityHandler;
import com.github.webee.urirouter.middlewares.HandleCtxMiddleware;
import com.github.webee.urirouter.middlewares.LogMiddleware;
import com.github.webee.urirouter.middlewares.LoginMiddleware;
import com.github.webee.urirouter.middlewares.PathParamsMiddleware;
import com.github.webee.urirouter.middlewares.QueryParamsMiddleware;
import com.github.webee.urirouter.openers.BrowserOpener;
import com.github.webee.urirouter.openers.LogOpener;
import com.github.webee.urirouter.openers.SchemeHostFilterOpener;
import com.github.webee.urirouter.test.openers.MyUnhandledOpener;

/**
 * Created by webee on 17/2/20.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 设置openers
        URIRouters.insertOpener(new LogOpener(),
                new SchemeHostFilterOpener("",
                        "hyperwood:///", "http://hyperwood.com", "https://hyperwood.com")
                );
        URIRouters.appendOpener(new BrowserOpener());
        URIRouters.appendOpener(new MyUnhandledOpener());

        // 设置路由和路由中间件
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
        // 权限中间件
        Middleware loginMiddleware = new LoginMiddleware("/login/", new LoginMiddleware.IsLoginChecker() {
            @Override
            public boolean check(android.content.Context context) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                return sharedPref.getBoolean(LoginActivity.KEY_IS_LOGIN, false);
            }
        });

        userRouter.use(loginMiddleware);
        userRouter.add("/:uid/:age@int/", ActivityHandler.create(UserActivity.class));
        userRouter.add("/*", ActivityHandler.create(TestActivity.class));

        // test router.
        Router testRouter = root.mount("/test", true);
        testRouter.add("/", ActivityHandler.create(TestActivity.class));
        testRouter.add("/result/", ActivityHandler.create(ResultActivity.class), loginMiddleware);
        testRouter.add("/hello", new Handler() {
            @Override
            public void handle(Context ctx) {
                Toast.makeText(ctx.context, "Hello", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
