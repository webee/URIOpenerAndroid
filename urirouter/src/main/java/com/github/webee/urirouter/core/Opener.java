package com.github.webee.urirouter.core;

import android.net.Uri;
import android.os.Bundle;

/**
 * Created by webee on 17/2/22.
 */

public interface Opener {
    boolean open(android.content.Context context, Uri uri, Route route, Data ctxData, Bundle reqData);
}
