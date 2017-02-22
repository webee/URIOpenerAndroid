package com.github.webee.urirouter.handlers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import com.github.webee.urirouter.core.Data;
import com.github.webee.urirouter.core.URIRouters;

import java.util.HashSet;
import java.util.Set;

public class RouterActivity extends Activity {
    public static final Set<String> WEB_SCHEMES = new HashSet<>();
    private static Data ctxData;

    static {
        WEB_SCHEMES.add("http");
        WEB_SCHEMES.add("https");
    }

    public static void setCtxData(Data data) {
        ctxData = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            Bundle extras = intent.getExtras();

            if (!URIRouters.open(this, uri, ctxData, extras)) {
                if (WEB_SCHEMES.contains(uri.getScheme())) {
                    openUriWithBrowser(uri, extras);
                }
            }
        }
        finish();
    }

    private void openUriWithBrowser(Uri uri, Bundle extras) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (extras != null) {
            intent.putExtras(extras);
        }
        ResolveInfo defaultResolution = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // If this app is the default app for entry URLs, use the browser instead
        if (defaultResolution.activityInfo.packageName.equals(getPackageName())) {
            Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"));
            ResolveInfo browseResolution = getPackageManager().resolveActivity(browseIntent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            intent.setComponent(new ComponentName(
                    browseResolution.activityInfo.applicationInfo.packageName,
                    browseResolution.activityInfo.name));

        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
