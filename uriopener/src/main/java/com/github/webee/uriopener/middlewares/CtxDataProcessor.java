package com.github.webee.uriopener.middlewares;

import com.github.webee.uriopener.core.Data;

/**
 * Created by webee on 17/2/23.
 */

public interface CtxDataProcessor {
    Data process(Data data);
}
