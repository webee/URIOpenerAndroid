package com.github.webee.urirouter.core;

import android.net.Uri;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by webee on 17/2/17.
 */

public class Router {
    public static final String SEGMENT_SEP = "/";
    public static final String PARAM_SEGMENT_PREFIX = ":";
    public static final String ANY_URI_PREFIX = "*";

    // 自治，不受上级中间件的影响
    private boolean autonomy = false;

    private List<Middleware> middlewares = new LinkedList<>();
    private Handler handler;
    private Map<String, Node> staticNodes = new HashMap<>();
    private ParamNode paramNode;
    private Node anyNode;

    public Router() {
    }

    public Router(boolean autonomy) {
        this.autonomy = autonomy;
    }

    public Route find(Uri uri) {
        return find(uri.getPath());
    }

    public Route find(String path) {
        PathSegments pathSegments = new PathSegments(path);
        return find(pathSegments.segments, pathSegments.isRoot);
    }

    public Route find(List<String> segments, boolean isRoot) {
        Route route = null;
        int size = segments.size();
        if (size == 0 && isRoot) {
            route = Route.create(handler);
        } else if (size > 0) {
            String segment = segments.get(0);
            // 1. static
            Node node = staticNodes.get(segment);
            if (node != null) {
                route = node.find(segments.subList(1, size), isRoot);
            } else if (paramNode != null) {
                // 2. param
                route = paramNode.find(segment, segments.subList(1, size), isRoot);
            }
        }

        if (route == null && anyNode != null) {
            // any
            route = anyNode.getRoute();
        }

        if (route != null) {
            route.applyMiddlewares(middlewares);
            if (autonomy) {
                // 由于是自治，路由到此生成结束
                route.setFinalized();
            }
        }

        return route;
    }

    public Router use(Middleware ...middlewares) {
        this.middlewares.addAll(Arrays.asList(middlewares));
        return this;
    }

    private Handler applyMiddlewares(Handler handler, List<Middleware> middlewares) {
        Handler res = handler;
        for (int i = middlewares.size() - 1; i >= 0; i--) {
            res = middlewares.get(i).process(res);
        }
        return res;
    }

    public Router add(Uri uri, Handler handler, Middleware ...middlewares) {
        return add(uri.getPath(), handler, middlewares);
    }

    public Router add(String path, Handler handler, Middleware ...middlewares) {
        PathSegments pathSegments = new PathSegments(path);
        return add(pathSegments.segments, pathSegments.isRoot, handler, middlewares);
    }

    private Router add(List<String> segments, boolean isRoot, Handler handler, Middleware ...middlewares) {
        handler = applyMiddlewares(handler, Arrays.asList(middlewares));
        int size = segments.size();
        if (size == 0 && isRoot) {
            // root: '/'
            this.handler = handler;
        } else if (size > 0) {
            String segment = segments.get(0);
            Node node = getNode(segment, size);
            if (size == 1 && !isRoot) {
                node.setHandler(handler);
            } else {
                Router router = node.router;
                if (router == null) {
                    router = new Router();
                    node.mount(router);
                }
                router.add(segments.subList(1, size), isRoot, handler);
            }
        }
        return this;
    }

    private Node getNode(String segment, int size) {
        Node node;
        if (size == 1 && segment.equals(ANY_URI_PREFIX)) {
            if (anyNode == null) {
                anyNode = new Node(segment);
            }

            node = anyNode;
        } else if (segment.startsWith(PARAM_SEGMENT_PREFIX)) {
            String name = segment.substring(1);

            if (paramNode == null) {
                paramNode = new ParamNode(name);
            }
            if (!paramNode.name.equals(name)) {
                throw new RuntimeException(String.format("%s: %s, %s", "path param names conflict", paramNode.name, name));
            }

            node = paramNode;
        } else {
            node = staticNodes.get(segment);
            if (node == null) {
                node = new Node(segment);
                staticNodes.put(segment, node);
            }
        }
        return node;
    }

    /**
     * create a autonomy[true/false] router and mount it at path.
     * @param path the path to mount.
     * @param autonomy whether the router should be autonomy.
     * @return the new created router.
     */
    public Router mount(String path, boolean autonomy) {
        Router router = new Router(autonomy);
        mount(path, router);
        return router;
    }

    /**
     * create a non-autonomy router and mount it at path.
     * @param path the path to mount.
     * @return the new created router.
     */
    public Router mount(String path) {
        return mount(path, false);
    }

    /**
     * mount a router at path.
     * @param path the path to mount at.
     * @param router the router to mount
     */
    public void mount(String path, Router router) {
        PathSegments pathSegments = new PathSegments(path);
        mount(pathSegments.segments, router);
    }

    private void mount(List<String> segments, Router router) {
        int size = segments.size();
        if (size < 1) {
            throw new RuntimeException("mount point must not be root");
        }

        String segment = segments.get(0);
        Node node = getNode(segment, 0);
        if (size == 1) {
            node.mount(router);
        } else {
            Router sr = node.router;
            if (sr == null) {
                sr = new Router();
                node.mount(sr);
            }
            sr.mount(segments.subList(1, size), router);
        }
    }

    class PathSegments {
        final List<String> segments;
        final boolean isRoot;

        PathSegments(List<String> segments, boolean isRoot) {
            this.segments = segments;
            this.isRoot = isRoot;
        }

        PathSegments(String path) {
            this(Uri.parse(":///" + path).getPathSegments(), path.endsWith(SEGMENT_SEP));
        }
    }

    class Node {
        final String name;
        Handler handler;
        Router router;

        public Node(String name) {
            this.name = name;
        }

        void setHandler(Handler handler) {
            this.handler = handler;
        }

        void mount(Router router) {
            this.router = router;
        }

        Route getRoute() {
            return Route.create(handler);
        }

        Route find(List<String> segments, boolean isRoot) {
            if (segments.size() == 0 && !isRoot) {
                return getRoute();
            }
            return router.find(segments, isRoot);
        }
    }

    class ParamNode extends Node {
        public ParamNode(String name) {
            super(name);
        }

        Param getParam(String segment) {
            return new Param(name, segment);
        }

        Route getRoute(String segment) {
            return Route.create(handler, getParam(segment));
        }

        Route find(String segment, List<String> segments, boolean isRoot) {
            if (segments.size() == 0 && !isRoot) {
                return getRoute(segment);
            }
            return Route.create(router.find(segments, isRoot), getParam(segment));
        }
    }
}
