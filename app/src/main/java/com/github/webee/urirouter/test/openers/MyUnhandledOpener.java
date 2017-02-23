package com.github.webee.urirouter.test.openers;

import android.widget.Toast;

import com.github.webee.urirouter.core.OpenContext;
import com.github.webee.urirouter.core.Opener;

/**
 * Created by webee on 17/2/22.
 */

public class MyUnhandledOpener implements Opener {
    @Override
    public boolean open(OpenContext ctx) {
        Toast.makeText(ctx.context, String.format("unhandled uri: [%s]", ctx.uri.toString()), Toast.LENGTH_SHORT).show();
        return true;
    }
}
