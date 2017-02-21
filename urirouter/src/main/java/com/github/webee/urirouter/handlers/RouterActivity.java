package com.github.webee.urirouter.handlers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import com.github.webee.urirouter.core.URIRouters;

import java.util.HashSet;
import java.util.Set;

public class RouterActivity extends Activity {
    public static final Set<String> WEB_SCHEMES = new HashSet<>();

    static {
        WEB_SCHEMES.add("http");
        WEB_SCHEMES.add("https");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            Bundle extras = intent.getExtras();
            if (!URIRouters.open(this, uri, null, extras)) {
                if (WEB_SCHEMES.contains(uri.getScheme())) {
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    if (extras != null) {
                        i.putExtras(extras);
                    }
                    ResolveInfo defaultResolution = getPackageManager().resolveActivity(i, PackageManager.MATCH_DEFAULT_ONLY);

                    // If this app is the default app for entry URLs, use the browser instead
                    if (defaultResolution.activityInfo.packageName.equals(getPackageName())) {
                        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"));
                        ResolveInfo browseResolution = getPackageManager().resolveActivity(browseIntent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                        i.setComponent(new ComponentName(
                                browseResolution.activityInfo.applicationInfo.packageName,
                                browseResolution.activityInfo.name));

                    }
                    startActivity(i);
                }
            }
        }
        finish();
    }
}
