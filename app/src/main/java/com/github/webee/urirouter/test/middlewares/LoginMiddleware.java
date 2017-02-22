package com.github.webee.urirouter.test.middlewares;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Middleware;
import com.github.webee.urirouter.core.URIRouters;
import com.github.webee.urirouter.handlers.ActivityHandler;
import com.github.webee.urirouter.handlers.ProxyActivity;
import com.github.webee.urirouter.test.LoginActivity;

/**
 * Created by webee on 17/2/20.
 */

public class LoginMiddleware implements Middleware {
    private String loginPath;

    public LoginMiddleware(String loginPath) {
        this.loginPath = loginPath;
    }

    @Override
    public Handler process(final Handler next) {
        return new Handler() {
            @Override
            public void handle(Context ctx) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx.context);
                boolean isLogin = sharedPref.getBoolean(LoginActivity.KEY_IS_LOGIN, false);
                if (!isLogin) {
                    Log.d("LOGIN MID", "not login");
                    // 跳转到登录
                    Bundle data = new Bundle();
                    data.putParcelable(ProxyActivity.EXTRA_REFERRER, ctx.request.uri);
                    data.putBundle(ProxyActivity.EXTRA_REFERRER_CTX_DATA, ctx.data.bundle);
                    data.putBundle(ProxyActivity.EXTRA_REFERRER_REQ_DATA, ctx.request.data);

                    data.putParcelable(ProxyActivity.EXTRA_TARGET, Uri.parse(loginPath));

                    URIRouters.route(ActivityHandler.PROXY_PATH)
                            .withContext(ctx.context)
                            .withCtxData(ctx.data)
                            .withReqData(data)
                            .open();
                    return;
                }
                next.handle(ctx);
            }
        };
    }
}
