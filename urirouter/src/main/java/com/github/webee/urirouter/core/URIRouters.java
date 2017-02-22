package com.github.webee.urirouter.core;

import android.net.Uri;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by webee on 17/2/17.
 */

public final class URIRouters {
    public static final List<Opener> openers = new LinkedList<>();
    public static final Router root = new Router();

    static {
        registerOpener(new DefaultOpener());
    }

    public static void registerOpener(Opener ...newOpeners) {
        for (int i = newOpeners.length - 1; i >= 0; i--) {
            openers.add(0, newOpeners[i]);
        }
    }

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
        return open(context, uri, root.find(uri.getPath()), ctxData, reqData);
    }

    public static boolean open(android.content.Context context, Route route, Data ctxData, Bundle reqData) {
        return open(context, null, route, ctxData, reqData);
    }

    public static boolean open(android.content.Context context, Uri uri, Route route, Data ctxData, Bundle reqData) {
        for (Opener opener : openers) {
            if (opener.open(context, uri, route, ctxData, reqData)) {
                return true;
            }
        }
        return false;
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
            return URIRouters.open(context, uri, route, ctxData, reqData);
        }
    }
}
