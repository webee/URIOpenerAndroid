package com.github.webee.urirouter.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.github.webee.urirouter.core.Data;
import com.github.webee.urirouter.core.URIRouters;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_NEXT = LoginActivity.class.getName() + ".next";
    public static final String EXTRA_NEXT_CTX_DATA = LoginActivity.class.getName() + ".next_ctx_data";
    public static final String EXTRA_NEXT_REQ_DATA = LoginActivity.class.getName() + ".next_req_data";

    public static final String KEY_IS_LOGIN = "is_login";

    private Uri next;
    private Data nextCtxData;
    private Bundle nextReqData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        next = intent.getParcelableExtra(EXTRA_NEXT);
        nextCtxData = new Data(intent.getBundleExtra(EXTRA_NEXT_CTX_DATA));
        nextReqData = intent.getBundleExtra(EXTRA_NEXT_REQ_DATA);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean(KEY_IS_LOGIN, false)) {
            hasLogin();
            return;
        }
    }

    @OnClick(R.id.login)
    public void login() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_IS_LOGIN, true);
        editor.apply();

        hasLogin();
    }

    private void hasLogin() {
        if (next != null) {
            URIRouters.open(this, next, nextCtxData, nextReqData);
        } else {
            setResult(RESULT_OK);
        }
        finish();
    }
}
