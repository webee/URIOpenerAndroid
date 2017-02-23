package com.github.webee.urirouter.openers;

import android.content.Intent;
import android.net.Uri;

import com.github.webee.urirouter.core.OpenContext;
import com.github.webee.urirouter.core.Opener;
import com.github.webee.urirouter.handlers.RouterActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by webee on 17/2/22.
 */

public class SchemeHostFilterOpener implements Opener {
    private Set<Uri> allowedUriSchemes = new HashSet<>();

    public SchemeHostFilterOpener(String ...paths) {
        for (String path : paths) {
            Uri uri = Uri.parse(path);
            allowedUriSchemes.add(uri);
        }
    }

    private boolean isStringEqual(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if (a != null) {
            return a.equals(b);
        }
        return false;
    }

    private boolean isAllowed(String s, String h) {
        for (Uri x : allowedUriSchemes) {
            if (isStringEqual(x.getScheme(), s) && isStringEqual(x.getHost(), h)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean open(OpenContext ctx) {
        if (RouterActivity.isFromExternal(ctx.ctxData)) {
            // 来自外部的链接肯定是可以处理的，不需要过滤
            return false;
        }

        if (!isAllowed(ctx.uri.getScheme(), ctx.uri.getHost())) {
            Intent intent = new Intent(Intent.ACTION_VIEW, ctx.uri);
            if (ctx.reqData != null) {
                intent.putExtras(ctx.reqData);
            }
            ctx.context.startActivity(intent);
            return true;
        }
        return false;
    }
}
