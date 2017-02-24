package com.github.webee.uriopener.handlers;

import android.content.Intent;

/**
 * Created by webee on 17/2/21.
 */

public interface IntentProcessor {
    Intent process(Intent origin);
}
