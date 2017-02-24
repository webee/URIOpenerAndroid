package com.github.webee.uriopener.handlers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.webee.uriopener.core.Data;
import com.github.webee.uriopener.core.URIOpeners;

public class ArbitrationProxyActivity extends Activity {
    public static final String EXTRA_TARGET = "target";
    public static final String EXTRA_TARGET_CTX_DATA = "target_ctx_data";
    public static final String EXTRA_TARGET_REQ_DATA = "target_req_data";

    public static final String EXTRA_ARBITRATOR = "arbitrator";
    public static final String EXTRA_ARBITRATOR_CTX_DATA = "arbitrator_ctx_data";
    public static final String EXTRA_ARBITRATOR_REQ_DATA = "arbitrator_req_data";

    private static final int ARBITRATOR_REQ_CODE = 319;
    private static final int TARGET_REQ_CODE = 320;

    // target
    private Uri target;
    private Data targetCtxData;
    private Bundle targetReqData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        target = intent.getParcelableExtra(EXTRA_TARGET);
        targetCtxData = new Data(intent.getBundleExtra(EXTRA_TARGET_CTX_DATA));
        targetReqData = intent.getBundleExtra(EXTRA_TARGET_REQ_DATA);

        Uri arbitrator = intent.getParcelableExtra(EXTRA_ARBITRATOR);
        Data arbitratorCtxData = new Data(intent.getBundleExtra(EXTRA_ARBITRATOR_CTX_DATA));
        Bundle arbitratorReqData = intent.getBundleExtra(EXTRA_ARBITRATOR_REQ_DATA);
        URIOpeners.route(arbitrator)
                .withContext(this)
                .withCtxData(arbitratorCtxData)
                .withCtxData(ActivityHandler.ctxData().withRequestCode(ARBITRATOR_REQ_CODE).build())
                .withReqData(arbitratorReqData)
                .open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ARBITRATOR_REQ_CODE) {
            if (ActivityHandler.isRequestForResult(targetCtxData)) {
                if (resultCode == RESULT_OK) {
                    URIOpeners.route(target)
                            .withContext(this)
                            .withCtxData(targetCtxData)
                            .withCtxData(ActivityHandler.ctxData().withRequestCode(TARGET_REQ_CODE).build())
                            .withReqData(targetReqData)
                            .open();
                } else {
                    setResult(resultCode, data);
                    finish();
                }
            } else {
                if (resultCode == RESULT_OK) {
                    URIOpeners.open(this, target, targetCtxData, targetReqData);
                }
                finish();
            }
        } else if (requestCode == TARGET_REQ_CODE) {
            setResult(resultCode, data);
            finish();
        }
    }
}
