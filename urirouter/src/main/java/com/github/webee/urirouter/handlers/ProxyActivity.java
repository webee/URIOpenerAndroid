package com.github.webee.urirouter.handlers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.webee.urirouter.core.Data;
import com.github.webee.urirouter.core.URIRouters;

public class ProxyActivity extends Activity {
    public static final String EXTRA_REFERRER = "referrer";
    public static final String EXTRA_REFERRER_CTX_DATA = "referrer_ctx_data";
    public static final String EXTRA_REFERRER_REQ_DATA = "referrer_req_data";

    public static final String EXTRA_TARGET = "target";
    public static final String EXTRA_TARGET_CTX_DATA = "target_ctx_data";
    public static final String EXTRA_TARGET_REQ_DATA = "target_req_data";

    private static final int TARGET_REQ_CODE = 319;
    private static final int REFERRER_REQ_CODE = 320;

    // referrer
    private Uri referrer;
    private Data referrerCtxData;
    private Bundle referrerReqData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        referrer = intent.getParcelableExtra(EXTRA_REFERRER);
        referrerCtxData = new Data(intent.getBundleExtra(EXTRA_REFERRER_CTX_DATA));
        referrerReqData = intent.getBundleExtra(EXTRA_REFERRER_REQ_DATA);

        Uri target = intent.getParcelableExtra(EXTRA_TARGET);
        Data targetCtxData = new Data(intent.getBundleExtra(EXTRA_TARGET_CTX_DATA));
        Bundle targetReqData = intent.getBundleExtra(EXTRA_TARGET_REQ_DATA);
        URIRouters.route(target)
                .withContext(this)
                .withCtxData(targetCtxData)
                .withCtxData(ActivityHandler.ctxData().withRequestCode(TARGET_REQ_CODE).build())
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
                            .withCtxData(ActivityHandler.ctxData().withRequestCode(REFERRER_REQ_CODE).build())
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
