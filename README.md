# URIOpenerAndroid
[![Release](https://img.shields.io/github/release/webee/URIOpenerAndroid.svg?label=maven version)](https://jitpack.io/#webee/URIOpenerAndroid)

URI Opener, 顾名思义就是用来打开URI的，不论是外部还是内部请求，不论是匹配intent-filter声明的还是其它任意URI， URI Opener都可以处理。
URI Opener可以作为应用的路由，使用URI对资源(Activity, Service, Action)定义，实现应用内组件之间的解耦，并可以处理应用内外的跳转。
URI Opener核心思想极其简洁，使用打开器，中间件等可以灵活地增强功能。

[https://jitpack.io/#webee/URIOpenerAndroid](https://jitpack.io/#webee/URIOpenerAndroid)

To install the library add:

   ```gradle
   repositories {
        jcenter()
        maven { url "https://jitpack.io" }
   }
   dependencies {
         compile 'com.github.webee:URIOpenerAndroid:v1.0.1'
   }
   ```

## 演示
#### 应用内打开
![image](https://raw.githubusercontent.com/webee/URIOpenerAndroid/master/docs/uri_opener1.gif)

#### 外部链接
![image](https://raw.githubusercontent.com/webee/URIOpenerAndroid/master/docs/uri_opener2.gif)

# 使用方法
## 全局配置
```java
// Application.onCreate
    // 初始化
    URIOpeners.init(this);

    // 设置openers
    URIOpeners.appendOpener(new LogOpener(),
            // FIXME:
            // 为了在app内部使用URIRouters打开任意链接
            // 除了从外部进入的请求, 其它不匹配的将使用ACTION_VIEW打开
            new SchemeHostFilterOpener("",
                    "hyperwood:///", "http://hyperwood.com", "https://hyperwood.com")
            );
    // 路由打开器
    RouteOpener routeOpener = new RouteOpener();
    URIOpeners.appendOpener(routeOpener);
    // 无法打开的web链接使用浏览器打开
    URIOpeners.appendOpener(new BrowserOpener());
    // 无法处理的请求
    URIOpeners.appendOpener(new MyUnhandledOpener());

    Router root = routeOpener.root;
    // 初始化某些处理器
    ActivityHandler.initRoutes(root);
    // 设置路由和路由中间件
    root.use(new LogMiddleware(),
            new ExtractActivityRequestCodeMiddleware(),
            new PathParamsMiddleware(),
            new QueryParamsMiddleware(),
            new ProcessCtxDataMiddleware(new CtxDataProcessor() {
                @Override
                public Data process(Data data) {
                    return ActivityHandler.ctxData(data).withFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).build();
                }
            }));

    root.add("/", ActivityHandler.create(MainActivity.class));
    root.add("/login/", ActivityHandler.create(LoginActivity.class));
    root.add("/todo/*", ActivityHandler.create(NotImplementedActivity.class));

    // user router.
    Router userRouter = root.mount("/user");
    // 权限中间件
    Middleware loginMiddleware = new LoginMiddleware("/login/", new LoginMiddleware.IsLoginChecker() {
        @Override
        public boolean check(android.content.Context context) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            return sharedPref.getBoolean(LoginActivity.KEY_IS_LOGIN, false);
        }
    });

    userRouter.use(loginMiddleware);
    userRouter.add("/:uid/:age@int/", ActivityHandler.create(UserActivity.class));
    userRouter.add("/*", ActivityHandler.create(TestActivity.class));

    // test router.
    Router testRouter = root.mount("/test");
    testRouter.add("/", ActivityHandler.create(TestActivity.class));
    testRouter.add("/result/", ActivityHandler.create(ResultActivity.class), loginMiddleware);
    testRouter.add("/params/:name/:uid@int/:adult@bool/", ActivityHandler.create(TestActivity.class),
            new FlattenParamsMiddleware("adult=>isAdult", "tall=>height"));
    testRouter.add("/hello", new Handler() {
        @Override
        public void handle(RouteContext ctx) {
            Toast.makeText(ctx.context, "Hello", Toast.LENGTH_SHORT).show();
        }
    });

    // xxx router.
    // xxx router is autonomy, which means it does not inherit middlewares.
    Router xxxRouter = root.mount("/xxx", true);
    xxxRouter.use(new LogMiddleware());
    xxxRouter.add("/test", ActivityHandler.create(TestActivity.class));
```

## 使用
```java
    // 1.
    URIRouters.open(this, "/test/result/", ActivityHandler.ctxData().withRequestCode(1).build(), null);
    
    // 2.
    URIRouters.open(this, "/test/hello");
    
    // 3.
    Bundle reqData = new Bundle();
    reqData.putParcelable(LoginActivity.EXTRA_NEXT, Uri.parse("/"));
    URIRouters.open(this, "/login/", null, reqData);
    
    // 4.
    URIRouters.route("/test/")
        .withContext(context)
        .withCtxData(data)
        .withReqData(data)
        .open();

    // 5.
    URIRouters.open("https://baidu.com");

    // 6. 来自外部的请求(浏览器链接等)
```

## 外部请求入口URI Scheme配置
```xml
  <activity android:name="com.github.webee.urirouter.core.RouterActivity">

      <!-- <intent-filter android:autoVerify="true"> -->
      <intent-filter>
          <action android:name="android.intent.action.VIEW" />

          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />

          <data android:scheme="hyperwood" />
      </intent-filter>
      <intent-filter>
          <action android:name="android.intent.action.VIEW" />

          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />

          <data
              android:host="hyperwood.com"
              android:scheme="http" />
          <data
              android:host="hyperwood.com"
              android:scheme="https" />
      </intent-filter>
```

## uri示例
```
    // 其中路径参数和queryString默认为字符串，可由PathParams和QueryParams中间件处理成具体类型

    https://hyperwood.com/test/?name=webee&ageTint=28
    hyperwood:///test/
    https://hyperwood.com/user/webee/28/?name=vivian&name=xiaoee&ageTint=27
    hyperwood:///user/webee/28/
    https://hyperwood.com/user/xxx/
    hyperwood:///user/xxx/
    hyperwood:///xxxx/a/
    hyperwood:///xxxx/a/b/
    /user/xxx/
    /xxxx/1234
```

## 其它
### 说明
看过不少app路由的实现，感觉设计都不是特别简洁，增加功能也不足够灵活。URIOpener目的在于实现一个概念清晰、简洁，通用且功能灵活的app组件化工具。
URIOpener的功能可以类比Mac OSX系统的open命令, URIOpener的基本概念就是两个，1.URI代表请求; 2.Opener是处理请求的方法
自带Opener RouteOpener负责应用内资源的维护和处理, 使用Router(文件系统)建立URI(路径)和内部资源(文件)的关系，内部资源表示成一个Handler(文件处理器)，使用Middleware来加强Handler的功能，使用Middleware来加强Handler的功能。
可以自定义其它Opener处理非应用内资源。

关于使用注解配置路由的考虑，我认为路由本身就是用来集中管理应用资源的，就是一个全局管理器，集中配置是更清晰的；
另外注解配置其实也是一种对业务代码的入侵，如果实现不是由我们控制的, 根本也没法使用注解了。

### 打开器
打开器是对URI的处理方法
URIOpeners可以注册多个打开器，按顺序尝试只到成功打开为止。
除了以下自带的打开器，也可以自定义打开器：

* LogOpener: 打印请求日志
* SchemeHostFilterOpener: 除了外部请求，其它不匹配的Scheme/Host组合使用ACTION_VIEW方式由系统处理
* RouteOpener: 路由打开器，查找URI对应的Handler并进行处理
* BrowserOpener: 无法打开的Web链接使用系统浏览器打开

### 处理器(Handler)
处理器路由打开器注册的路径的处理方法, 可以自定义。
自带的ActivityHandler可以根据Activity生成打开Activity的Handler.

### 路由器(Router)
因为Router是应用内部的URI映射，所以是不关心scheme和host的，路由匹配只使用URI的path部分。
path分为结点和段，每个结点代表一个子router, 结点和段都可能对应handler
比如: /user/:uid/profile，有3个结点和user, :uid, profile3个段
其中user, profile为静态段，:uid为参数段(一个router只能有一个参数段, /user/)
另外还有一种任意匹配段，比如: /todo/*
这样如果/todo/a/b/c这种path没有匹配上的时候会匹配上/todo/*
路由匹配优先级为：静态 > 参数 > 任意

参数段以':'作为标志，只识别字符串，为了使用带类型的参数，可以使用PathParamsMiddleware

router可以挂载上另一个router的某个结点上，比如app定义一个root路由器，组件A的路由aRouter, 组件B的路由bRouter,
```
// 挂载两个子路由器
root.mount('/a', aRouter)
root.mount('/b', bRouter)
```
除非子路由器是自治的(autonomy), 其将继承父级的中间件。


### 中间件
中间件是用来增强Handler的，可以自定义。

比如：
* PathParamsMiddleware: 解析路径参数
* QueryParamsMiddleware: 解析查询参数
* LoginMiddleware: 通用的登录检查功能

### OpenContextProcessor
在打开之前对请求上下文件进行处理, 可以自定义

比如:
* UserAppContextOpenCtxProcessor: 在没有提供context的情况使用当前Application作为context
