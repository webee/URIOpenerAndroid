# URIRouterAndroid
[![Release](https://img.shields.io/github/release/webee/URIRouterAndroid.svg?label=maven version)](https://jitpack.io/#webee/URIRouterAndroid)

android路由组件

[https://jitpack.io/#webee/URIRouterAndroid](https://jitpack.io/#webee/URIRouterAndroid)

To install the library add:

   ```gradle
   repositories {
        jcenter()
        maven { url "https://jitpack.io" }
   }
   dependencies {
         compile 'com.github.webee:URIRouterAndroid:v0.3.2'
   }
   ```

## 全局配置
```java
// Application.onCreate
    URIRouters.init(this);
    // 设置openers
    URIRouters.insertOpener(new LogOpener(),
            // FIXME:
            // 为了在app内部使用URIRouters打开任意链接
            // 不匹配的将使用ACTION_VIEW打开
            new SchemeHostFilterOpener("",
                    "hyperwood:///", "http://hyperwood.com", "https://hyperwood.com")
            );
    // 无法打开的web链接使用浏览器打开
    URIRouters.appendOpener(new BrowserOpener());
    // 无法处理的请求
    URIRouters.appendOpener(new MyUnhandledOpener());

    // 设置路由和路由中间件
    Router root = URIRouters.root;
    root.use(new LogMiddleware(),
            new PathParamsMiddleware(),
            new QueryParamsMiddleware(),
            new HandleCtxMiddleware(new Handler() {
                @Override
                public void handle(Context ctx) {
                    ctx.setData(ActivityHandler.ctxData(ctx.data).withFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).build());
                }
            }));

    root.add("/", ActivityHandler.create(MainActivity.class));
    root.add("/login/", ActivityHandler.create(LoginActivity.class));
    root.add("/xxxx/*", ActivityHandler.create(NotImplementedActivity.class));

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
    Router testRouter = root.mount("/test", true);
    testRouter.add("/", ActivityHandler.create(TestActivity.class));
    testRouter.add("/result/", ActivityHandler.create(ResultActivity.class), loginMiddleware);
    testRouter.add("/hello", new Handler() {
        @Override
        public void handle(Context ctx) {
            Toast.makeText(ctx.context, "Hello", Toast.LENGTH_SHORT).show();
        }
    });
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
```

## URI Scheme入口配置
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
```
