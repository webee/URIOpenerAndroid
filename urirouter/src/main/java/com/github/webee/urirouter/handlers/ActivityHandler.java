package com.github.webee.urirouter.handlers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.github.webee.urirouter.core.Context;
import com.github.webee.urirouter.core.Handler;
import com.github.webee.urirouter.core.Param;
import com.github.webee.urirouter.core.Request;
import com.github.webee.urirouter.core.Route;

import java.util.HashMap;
import java.util.Map;

import static com.github.webee.urirouter.core.Request.EXTRA_URI;

/**
 * Created by webee on 17/2/17.
 */

public class ActivityHandler implements Handler {
    public static final String DATA_OPTIONS = ActivityHandler.class.getName() + ".options";
    public static final String DATA_REQUEST_CODE = ActivityHandler.class.getName() + ".request_code";
    private static final Map<Class<? extends Activity>, Handler> handlers = new HashMap<>();
    private final Class<? extends Activity> cls;

    public static Handler create(Class<? extends Activity> cls) {
        Handler handler = handlers.get(cls);
        if (handler == null) {
            handler = new ActivityHandler(cls);
            handlers.put(cls, handler);
        }
        return handler;
    }

    public static Route route(Class<? extends Activity> cls, Param ...pathParams) {
        return Route.create(create(cls), pathParams);
    }

    private ActivityHandler(Class<? extends Activity> cls) {
        this.cls = cls;
    }

    public static Bundle getRequestData(Bundle options) {
        Bundle data = new Bundle();
        data.putBundle(DATA_OPTIONS, options);
        return data;
    }

    public static Bundle getRequestForResultData(int requestCode, Bundle options) {
        Bundle data = new Bundle();
        data.putInt(DATA_REQUEST_CODE, requestCode);
        data.putBundle(DATA_OPTIONS, options);
        return data;
    }

    @Override
    public void handle(Context ctx) {
        android.content.Context context = ctx.context;
        Intent intent = new Intent(context, cls);

        Bundle options = ctx.data.getBundle(DATA_OPTIONS);
        Request request = ctx.request;

        intent.putExtra(EXTRA_URI, request.uri);
        intent.putExtras(request.data);
        if (ctx.data.containsKey(DATA_REQUEST_CODE)) {
            // request for result.
            int requestCode = ctx.data.getInt(DATA_REQUEST_CODE);

            ((Activity)context).startActivityForResult(intent, requestCode, options);
        } else {
            context.startActivity(intent, options);
        }
    }

    public static boolean isRequestForResult(Bundle ctxData) {
        return ctxData.containsKey(DATA_REQUEST_CODE);
    }
}
