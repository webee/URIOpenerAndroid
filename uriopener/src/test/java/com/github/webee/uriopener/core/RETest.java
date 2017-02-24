package com.github.webee.uriopener.core;

import android.net.Uri;

import com.github.webee.uriopener.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by webee on 17/2/15.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RETest {
    @Test
    public void path_param() {
        Pattern r = Pattern.compile("^:(\\w+)(?::([a-z]{1,2})(?:\\((.*)\\))?)?$");
        //String s = ":key:s(\\w{,32})";
        String s = ":key:s(\\w{,32})";

        Matcher m = r.matcher(s);
        if (m.matches()) {
            int c = m.groupCount();
            System.out.println("s: " + m.group());
            System.out.println("name: " + m.group(1));
            if (c >= 2) {
                System.out.println("type id: " + m.group(2));
            }
            if (c >= 3) {
                System.out.println("type pattern: " + m.group(3));
            }
        }
    }

    @Test
    public void path() {
        Uri uri = Uri.parse("/a/123/123.45/xyz?a=1&b=2#fragment");
        System.out.println(uri.getHost());
        System.out.println(uri.getPath());
        System.out.println(uri.getQuery());
        System.out.println(uri.getFragment());
    }

    @Test
    public void path_parts() {
        Uri uri = Uri.parse("http://qinqinwojia.cn/a/123//123.45/xyz?a=1++2+3&b=6");
        List<String> parts = uri.getPathSegments();
        System.out.println(parts);
        System.out.println(uri.getQueryParameter("a"));
        System.out.println(uri.getQueryParameter("b"));
    }

    @Test
    public void path_parts2() {
        System.out.println(Uri.parse("").getPathSegments());
        System.out.println(Uri.parse("/").getPathSegments());
        System.out.println(Uri.parse("/a").getPathSegments());
        System.out.println(Uri.parse("/a/").getPathSegments());
        System.out.println(Uri.parse("/a/b").getPathSegments());
        System.out.println(Uri.parse(":///a///:b:i(123)/").getPathSegments());
        String s = "//a///b/c///";
        s = s.trim();
        System.out.println(Arrays.asList(s.split("/")));
        System.out.println(Uri.parse("http://a.com/a/b/*/c/?x=1#hash").getPath());
        System.out.println(Uri.parse("http://a.com/a/b/*/c/?x=1#hash").getQuery());
        System.out.println(Uri.parse("http://a.com/a/b/*/c/?x=1#hash").getFragment());
        System.out.println(Uri.parse("/a/b/*/c/?x=1#hash").getScheme());
        //System.out.println(Uri.parse("qqwj:///a/b/c/?x=1").getHost());
        //System.out.println(Uri.parse("qqwj:///a/b/c/?x=1").getPath());
    }
}
