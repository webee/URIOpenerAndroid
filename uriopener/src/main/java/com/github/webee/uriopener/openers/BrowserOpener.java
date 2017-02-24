package com.github.webee.uriopener.openers;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.github.webee.uriopener.core.OpenContext;
import com.github.webee.uriopener.core.Opener;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by webee on 17/2/22.
 */

public class BrowserOpener implements Opener {
    public static final Set<String> WEB_SCHEMES = new HashSet<>();

    static {
        WEB_SCHEMES.add("http");
        WEB_SCHEMES.add("https");
    }

    @Override
    public boolean open(OpenContext ctx) {
        if (WEB_SCHEMES.contains(ctx.uri.getScheme())) {
            Intent intent = new Intent(Intent.ACTION_VIEW, ctx.uri);
            if (ctx.reqData != null) {
                intent.putExtras(ctx.reqData);
            }
            ResolveInfo defaultResolution = ctx.context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

            String defaultPackageName = defaultResolution.activityInfo.packageName;
            // If this app is the default app for entry URLs, use the browser instead
            if (defaultPackageName.equals(ctx.context.getPackageName()) || defaultPackageName.equals("android")) {
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"));
                ResolveInfo browseResolution = ctx.context.getPackageManager().resolveActivity(browseIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                intent.setComponent(new ComponentName(
                        browseResolution.activityInfo.applicationInfo.packageName,
                        browseResolution.activityInfo.name));

            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.context.startActivity(intent);
            return true;
        }
        return false;
    }
}
