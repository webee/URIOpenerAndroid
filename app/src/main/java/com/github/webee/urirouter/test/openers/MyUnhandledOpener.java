package com.github.webee.urirouter.test.openers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.github.webee.urirouter.core.Data;
import com.github.webee.urirouter.core.Opener;

/**
 * Created by webee on 17/2/22.
 */

public class MyUnhandledOpener implements Opener {
    @Override
    public boolean open(Context context, Uri uri, Data ctxData, Bundle reqData) {
        Toast.makeText(context, String.format("unhandled uri: [%s]", uri.toString()), Toast.LENGTH_SHORT).show();
        return true;
    }
}
