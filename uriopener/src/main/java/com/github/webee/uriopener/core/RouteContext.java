package com.github.webee.uriopener.core;

/**
 * Created by webee on 17/2/17.
 */

public class RouteContext {
    public android.content.Context context;
    public Data data;
    public Request request;
    public Response response;

    public RouteContext(android.content.Context context, Request request, Response response) {
        this.context = context;
        this.data = new Data();
        this.request = request;
        this.response = response;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Context{" +
                "context=" + context +
                ", data=" + data +
                ", request=" + request +
                ", response=" + response +
                '}';
    }
}
