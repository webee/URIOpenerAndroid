package com.github.webee.urirouter.handlers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.webee.urirouter.core.URIRouters;

public class ProxyActivity extends Activity {
    public static final String EXTRA_REFERRER = ProxyActivity.class.getName() + ".referrer";
    public static final String EXTRA_REFERRER_CTX_DATA = ProxyActivity.class.getName() + ".referrer_ctx_data";
    public static final String EXTRA_REFERRER_REQ_DATA = ProxyActivity.class.getName() + ".referrer_req_data";

    public static final String EXTRA_TARGET = ProxyActivity.class.getName() + ".target";
    public static final String EXTRA_TARGET_CTX_DATA = ProxyActivity.class.getName() + ".target_ctx_data";
    public static final String EXTRA_TARGET_REQ_DATA = ProxyActivity.class.getName() + ".target_req_data";

    private static final int TARGET_REQ_CODE = 319;
    private static final int REFERRER_REQ_CODE = 320;

    // referrer
    private Uri referrer;
    private Bundle referrerCtxData;
    private Bundle referrerReqData;

    // target
    private Uri target;
    private Bundle targetCtxData;
    private Bundle targetReqData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        referrer = intent.getParcelableExtra(EXTRA_REFERRER);
        referrerCtxData = intent.getBundleExtra(EXTRA_REFERRER_CTX_DATA);
        referrerReqData = intent.getBundleExtra(EXTRA_REFERRER_REQ_DATA);

        target = intent.getParcelableExtra(EXTRA_TARGET);
        targetCtxData = intent.getBundleExtra(EXTRA_TARGET_CTX_DATA);
        targetReqData = intent.getBundleExtra(EXTRA_TARGET_REQ_DATA);
        URIRouters.route(target)
                .withContext(this)
                .withCtxData(targetCtxData)
                .withCtxData(ActivityHandler.getRequestForResultData(TARGET_REQ_CODE, null))
                .withReqData(targetReqData)
                .open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TARGET_REQ_CODE) {
            if (ActivityHandler.isRequestForResult(referrerCtxData)) {
                if (resultCode == RESULT_OK) {
                    URIRouters.route(referrer)
                            .withContext(this)
                            .withCtxData(referrerCtxData)
                            .withCtxData(ActivityHandler.getRequestForResultData(REFERRER_REQ_CODE, null))
                            .withReqData(referrerReqData)
                            .open();
                } else {
                    setResult(resultCode, data);
                    finish();
                }
            } else {
                if (resultCode == RESULT_OK) {
                    URIRouters.open(this, referrer, referrerCtxData, referrerReqData);
                }
                finish();
            }
        } else if (requestCode == REFERRER_REQ_CODE) {
            setResult(resultCode, data);
            finish();
        }
    }
}
