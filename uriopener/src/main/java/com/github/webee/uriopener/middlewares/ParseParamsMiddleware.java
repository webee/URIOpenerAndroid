package com.github.webee.uriopener.middlewares;

import com.github.webee.uriopener.core.Middleware;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by webee on 17/2/22.
 */

public abstract class ParseParamsMiddleware implements Middleware {
    String TYPE_SEP_REGEX = "@";
    Map<String, ValueParser> parsers;

    public ParseParamsMiddleware() {
    }

    public ParseParamsMiddleware(String typeSep) {
        TYPE_SEP_REGEX = typeSep;
    }

    public ParseParamsMiddleware addParser(String name, ValueParser parser) {
        if (parsers == null) {
            parsers = new HashMap<>();
        }
        parsers.put(name, parser);
        return this;
    }

    public ParseParamsMiddleware addParsers(Map<String, ValueParser> moreParsers) {
        if (parsers == null) {
            parsers = new HashMap<>();
        }
        parsers.putAll(moreParsers);
        return this;
    }
}
