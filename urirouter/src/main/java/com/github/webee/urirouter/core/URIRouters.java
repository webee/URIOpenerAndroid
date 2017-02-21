package com.github.webee.urirouter.core;

import android.net.Uri;
import android.os.Bundle;

/**
 * Created by webee on 17/2/17.
 */

public final class URIRouters {
    public static final Router root = new Router();

    public static boolean open(android.content.Context context, String path) {
        return open(context, Uri.parse(path));
    }

    public static boolean open(android.content.Context context, Uri uri) {
        return open(context, uri, null, null);
    }

    public static boolean open(android.content.Context context, String path, Data ctxData) {
        return open(context, Uri.parse(path), ctxData, null);
    }

    public static boolean open(android.content.Context context, String path, Data ctxData, Bundle reqData) {
        return open(context, Uri.parse(path), ctxData, reqData);
    }

    public static boolean open(android.content.Context context, Uri uri, Data ctxData, Bundle reqData) {
        Route route = root.find(uri.getPath());
        if (route != null) {
            Request request = new Request(uri, route.pathParams);
            if (reqData != null) {
                request.data.putAll(reqData);
            }

            Context ctx = new Context(context, request, null);
            if (ctxData != null) {
                ctx.data.putAll(ctxData);
            }
            route.handler.handle(ctx);
            return true;
        }
        return false;
    }

    public static boolean open(android.content.Context context, Route route, Data ctxData, Bundle reqData) {
        Request request = new Request(route.pathParams);
        if (reqData != null) {
            request.data.putAll(reqData);
        }

        Context ctx = new Context(context, request, null);
        if (ctxData != null) {
            ctx.data.putAll(ctxData);
        }
        route.handler.handle(ctx);
        return true;
    }

    public static Builder route(String path) {
        return new Builder(Uri.parse(path));
    }

    public static Builder route(Uri uri) {
        return new Builder(uri);
    }

    public static Builder route(Route route) {
        return new Builder(route);
    }

    public static class Builder {
        private android.content.Context context;
        private Uri uri;
        private Route route;
        private Data ctxData;
        private Bundle reqData;

        Builder(Uri uri) {
            this.uri = uri;
        }

        Builder(Route route) {
            this.route = route;
        }

        public Builder withContext(android.content.Context context) {
            this.context = context;
            return this;
        }

        public Builder withCtxData(Data ctxData) {
            if (ctxData != null) {
                if (this.ctxData == null) {
                    this.ctxData = new Data();
                }
                this.ctxData.putAll(ctxData);
            }
            return this;
        }

        public Builder withReqData(Bundle reqData) {
            if (reqData != null) {
                if (this.reqData == null) {
                    this.reqData = new Bundle();
                }
                this.reqData.putAll(reqData);
            }
            return this;
        }

        public boolean open() {
            if (route != null) {
                return URIRouters.open(context, route, ctxData, reqData);
            }
            return URIRouters.open(context, uri, ctxData, reqData);
        }
    }
}
