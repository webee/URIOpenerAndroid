package com.github.webee.urirouter.middlewares;

import com.github.webee.urirouter.core.Data;

/**
 * Created by webee on 17/2/23.
 */

public interface CtxDataProcessor {
    Data process(Data data);
}
