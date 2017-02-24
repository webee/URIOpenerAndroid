package com.github.webee.uriopener.middlewares;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.github.webee.uriopener.core.Handler;
import com.github.webee.uriopener.core.Middleware;
import com.github.webee.uriopener.core.Route;
import com.github.webee.uriopener.core.RouteContext;
import com.github.webee.uriopener.core.Router;
import com.github.webee.uriopener.core.URIOpeners;
import com.github.webee.uriopener.handlers.ActivityHandler;
import com.github.webee.uriopener.handlers.ArbitrationProxyActivity;

/**
 * Created by webee on 17/2/20.
 */

public class LoginMiddleware implements Middleware {
    private static final Handler LOGIN_HANDLER = new Handler() {
        @Override
        public void handle(RouteContext ctx) {
        }
    };

    private Uri loginUri;
    private Router loginRouter;
    private IsLoginChecker isLoginChecker;

    public LoginMiddleware(String loginPath, IsLoginChecker isLoginChecker) {
        this.loginUri = Uri.parse(loginPath);
        this.loginRouter = new Router().add(loginUri, LOGIN_HANDLER);
        this.isLoginChecker = isLoginChecker;
    }

    @Override
    public Handler process(final Handler next) {
        return new MiddlewareHandler(this, next) {
            @Override
            public void handling(Handler next, RouteContext ctx) {
                Route route = loginRouter.find(ctx.request.uri);
                if (!(route != null && route.handler == LOGIN_HANDLER)) {
                    // 不是登录页面
                    if (!isLoginChecker.check(ctx.context)) {
                        Log.d("LOGIN MID", "not login");
                        // 跳转到登录
                        Bundle data = new Bundle();
                        data.putParcelable(ArbitrationProxyActivity.EXTRA_TARGET, ctx.request.uri);
                        data.putBundle(ArbitrationProxyActivity.EXTRA_TARGET_CTX_DATA, ctx.data.bundle);
                        data.putBundle(ArbitrationProxyActivity.EXTRA_TARGET_REQ_DATA, ctx.request.data);

                        data.putParcelable(ArbitrationProxyActivity.EXTRA_ARBITRATOR, loginUri);

                        URIOpeners.route(ActivityHandler.ARBITRATION_PROXY_PATH)
                                .withContext(ctx.context)
                                .withCtxData(ctx.data)
                                .withReqData(data)
                                .open();
                        return;
                    }
                }
                next.handle(ctx);
            }
        };
    }

    public interface IsLoginChecker {
        boolean check(android.content.Context context);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "loginUri='" + loginUri + '\'' +
                '}';
    }
}
