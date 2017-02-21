package com.github.webee.urirouter.middlewares;

import android.os.Bundle;

/**
 * Created by webee on 17/2/21.
 */

public class ValueParser {
    public static void parse(Bundle data, String name, String typeSeq, String value) {
        parse(data, name, null, typeSeq, value);
    }

    public static void parse(Bundle data, String name, String nameSuffix, String typeSeq, String value) {
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
            } catch (Exception e) {
                data.putString(finalName, value);
            }
        }
    }
}
