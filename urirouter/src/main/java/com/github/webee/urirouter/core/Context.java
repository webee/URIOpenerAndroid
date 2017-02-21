package com.github.webee.urirouter.core;

import android.os.Bundle;

/**
 * Created by webee on 17/2/17.
 */

public class Context {
    public android.content.Context context;
    public Bundle data;
    public Request request;
    public Response response;

    public Context(android.content.Context context, Request request, Response response) {
        this.context = context;
        this.data = new Bundle();
        this.request = request;
        this.response = response;
    }
}
