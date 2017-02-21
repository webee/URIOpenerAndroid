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
import android.widget.TextView;
import android.widget.Toast;

import com.github.webee.urirouter.core.URIRouters;
import com.github.webee.urirouter.handlers.ActivityHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup links = (ViewGroup) findViewById(R.id.links);
        for (int i = 0; i < links.getChildCount(); i++) {
            final View view = links.getChildAt(i);
            if (view instanceof TextView) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(((TextView) view).getText().toString());
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                });
            }
        }
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
                URIRouters.open(this, "/test/result/", ActivityHandler.getRequestForResultData(1, null), null);
                return true;
            case R.id.toast:
                URIRouters.open(this, "/test/toast");
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
