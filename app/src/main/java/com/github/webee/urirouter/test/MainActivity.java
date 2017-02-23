package com.github.webee.urirouter.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.webee.urirouter.core.URIRouters;
import com.github.webee.urirouter.handlers.ActivityHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.uriView)
    EditText uriView;

    private String[] pathes = new String[]{
            "https://t.xchat.engdd.com/www/links.html",
            "https://hyperwood.com/a/b/",
            "https://hyperwood.com/test/?name=webee&ageTint=28",
            "hyperwood:///test/",
            "https://hyperwood.com/user/webee/28/?name=vivian&name=xiaoee&ageTint=27",
            "hyperwood:///user/webee/28/",
            "https://hyperwood.com/user/xxx/",
            "hyperwood:///user/xxx/",
            "hyperwood:///todo/a/",
            "hyperwood:///todo/a/b/",
            "/test/hello",
            "/test/result/?__ACTIVITY_REQUEST_CODE=2",
            "/test/params/webee/4321/true/?tallTdouble=1.75",
            "/login/",
            "/xxx/test",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ViewGroup links = (ViewGroup) findViewById(R.id.links);
        for (String path : pathes) {
            TextView tv = new TextView(this);
            tv.setBackgroundResource(R.drawable.text_link_selector_bg);
            tv.setText(path);
            links.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(((TextView) v).getText().toString());
                    URIRouters.open(MainActivity.this, uri);
                    // OR:
                    /*
                    if (uri.getScheme() == null || uri.getScheme().equals("")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                    */
                }
            });
        }
    }

    @OnClick(R.id.openBtn)
    public void open() {
        String path = uriView.getText().toString();
        URIRouters.open(this, path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.test_result:
                URIRouters.open(this, "/test/result/",
                        ActivityHandler.ctxData()
                                .withRequestCode(1)
                                .build());
                return true;
            case R.id.testHello:
                URIRouters.open(this, "/test/hello");
                return true;
            case R.id.switch_user:
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(LoginActivity.KEY_IS_LOGIN);
                editor.apply();

                Bundle reqData = new Bundle();
                reqData.putParcelable(LoginActivity.EXTRA_NEXT, Uri.parse("/"));
                URIRouters.open(this, "/login/", null, reqData);
                //finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, String.format("requestCode: %d, resultCode: %d", requestCode, resultCode), Toast.LENGTH_SHORT).show();
    }
}
