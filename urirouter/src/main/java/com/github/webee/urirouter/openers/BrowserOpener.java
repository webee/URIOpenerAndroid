package com.github.webee.urirouter.openers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import com.github.webee.urirouter.core.Data;
import com.github.webee.urirouter.core.Opener;

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
    public boolean open(Context context, Uri uri, Data ctxData, Bundle reqData) {
        if (WEB_SCHEMES.contains(uri.getScheme())) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (reqData != null) {
                intent.putExtras(reqData);
            }
            ResolveInfo defaultResolution = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

            // If this app is the default app for entry URLs, use the browser instead
            if (defaultResolution.activityInfo.packageName.equals(context.getPackageName())) {
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"));
                ResolveInfo browseResolution = context.getPackageManager().resolveActivity(browseIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                intent.setComponent(new ComponentName(
                        browseResolution.activityInfo.applicationInfo.packageName,
                        browseResolution.activityInfo.name));

            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
