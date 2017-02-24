package com.github.webee.uriopener.test;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.github.webee.uriopener.core.Data;
import com.github.webee.uriopener.core.Handler;
import com.github.webee.uriopener.core.Middleware;
import com.github.webee.uriopener.core.RouteContext;
import com.github.webee.uriopener.core.RouteOpener;
import com.github.webee.uriopener.core.Router;
import com.github.webee.uriopener.core.URIOpeners;
import com.github.webee.uriopener.handlers.ActivityHandler;
import com.github.webee.uriopener.middlewares.CtxDataProcessor;
import com.github.webee.uriopener.middlewares.FlattenParamsMiddleware;
import com.github.webee.uriopener.middlewares.LogMiddleware;
import com.github.webee.uriopener.middlewares.LoginMiddleware;
import com.github.webee.uriopener.middlewares.PathParamsMiddleware;
import com.github.webee.uriopener.middlewares.ProcessCtxDataMiddleware;
import com.github.webee.uriopener.middlewares.QueryParamsMiddleware;
import com.github.webee.uriopener.openctxprocessors.ExtractActivityRequestCodeOpenCtxProcessor;
import com.github.webee.uriopener.openers.BrowserOpener;
import com.github.webee.uriopener.openers.LogOpener;
import com.github.webee.uriopener.openers.SchemeHostFilterOpener;
import com.github.webee.uriopener.test.openers.MyUnhandledOpener;

/**
 * Created by webee on 17/2/20.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        URIOpeners.init(this);
        // 设置open context processors.
        URIOpeners.addContextProcessor(new ExtractActivityRequestCodeOpenCtxProcessor());

        // 设置openers
        URIOpeners.appendOpener(new LogOpener(),
                // FIXME:
                // 为了在app内部使用URIRouters打开任意链接
                // 除了从外部进入的请求, 其它不匹配的将使用ACTION_VIEW打开
                new SchemeHostFilterOpener("",
                        "hyperwood:///", "http://hyperwood.com", "https://hyperwood.com")
                );
        // 路由打开器
        RouteOpener routeOpener = new RouteOpener();
        URIOpeners.appendOpener(routeOpener);
        // 无法打开的web链接使用浏览器打开
        URIOpeners.appendOpener(new BrowserOpener());
        // 无法处理的请求
        URIOpeners.appendOpener(new MyUnhandledOpener());

        Router root = routeOpener.root;
        // 初始化某些处理器
        ActivityHandler.initRoutes(root);
        // 设置路由和路由中间件
        root.use(new LogMiddleware(),
                new PathParamsMiddleware(),
                new QueryParamsMiddleware(),
                new ProcessCtxDataMiddleware(new CtxDataProcessor() {
                    @Override
                    public Data process(Data data) {
                        return ActivityHandler.ctxData(data).withFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).build();
                    }
                }));

        root.add("/", ActivityHandler.create(MainActivity.class));
        root.add("/login/", ActivityHandler.create(LoginActivity.class));
        root.add("/todo/*", ActivityHandler.create(NotImplementedActivity.class));

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
        Router testRouter = root.mount("/test");
        testRouter.add("/", ActivityHandler.create(TestActivity.class));
        testRouter.add("/result/", ActivityHandler.create(ResultActivity.class), loginMiddleware);
        testRouter.add("/params/:name/:uid@int/:adult@bool/", ActivityHandler.create(TestActivity.class),
                new FlattenParamsMiddleware("adult=>isAdult", "tall=>height"));
        testRouter.add("/hello", new Handler() {
            @Override
            public void handle(RouteContext ctx) {
                Toast.makeText(ctx.context, "Hello", Toast.LENGTH_SHORT).show();
            }
        });

        // xxx router.
        // xxx router is autonomy, which means it does not inherit middlewares.
        Router xxxRouter = root.mount("/xxx", true);
        xxxRouter.use(new LogMiddleware());
        xxxRouter.add("/test", ActivityHandler.create(TestActivity.class));
    }
}
