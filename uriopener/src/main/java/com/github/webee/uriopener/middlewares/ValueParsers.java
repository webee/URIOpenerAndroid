package com.github.webee.uriopener.middlewares;

import android.os.Bundle;

import java.util.Map;

/**
 * Created by webee on 17/2/21.
 */

public class ValueParsers {
    public static void parse(Bundle data, String name, String typeSeq, String value) {
        parse(data, name, typeSeq, value, null);
    }

    public static void parse(Bundle data, String name, String typeSeq, String value, Map<String, ValueParser> parsers) {
        parse(data, name, null, typeSeq, value, parsers);
    }

    public static void parse(Bundle data, String name, String nameSuffix, String typeSeq, String value, Map<String, ValueParser> parsers) {
        String[] parts = name.split(typeSeq, 2);
        String finalName = parts[0];
        if (nameSuffix != null) {
            finalName = finalName + nameSuffix;
        }
        if (parts.length < 2) {
            data.putString(finalName, value);
        } else {
            String t = parts[1];
            try {
                ValueParser parser = parsers != null ? parsers.get(t) : null;
                if (parser != null) {
                    parser.parse(data, finalName, value);
                } else {
                    switch (t) {
                        case "int":
                            data.putInt(finalName, Integer.parseInt(value));
                            break;
                        case "long":
                            data.putLong(finalName, Long.parseLong(value));
                            break;
                        case "double":
                            data.putDouble(finalName, Double.parseDouble(value));
                            break;
                        case "bool":
                            data.putBoolean(finalName, Boolean.parseBoolean(value));
                            break;
                        case "str":
                            data.putString(finalName, value);
                            break;
                        default:
                            data.putString(finalName, value);
                    }
                }
            } catch (Exception e) {
                data.putString(finalName, value);
            }
        }
    }
}
