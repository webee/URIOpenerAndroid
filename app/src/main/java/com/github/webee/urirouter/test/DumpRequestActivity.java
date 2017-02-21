package com.github.webee.urirouter.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.webee.urirouter.core.Request;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DumpRequestActivity extends AppCompatActivity {
    @BindView(R.id.info)
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dump_request);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(Request.EXTRA_URI);
        Bundle pathParams = intent.getBundleExtra(Request.EXTRA_PATH_PARAMS);
        Bundle queryParams = intent.getBundleExtra(Request.EXTRA_QUERY_PARAMS);

        info.setText(getClass().getName());
        info.append("\n\n");
        info.append(String.format("uri => %s\n\n", uri.toString()));

        // path params
        if (pathParams != null) {
            for (String key : pathParams.keySet()) {
                Object val = pathParams.get(key);
                info.append(String.format("path: %s -> %s(%s)\n\n", key, val, val.getClass().getName()));
            }
        }

        // query params
        if (queryParams != null) {
            for (String key : queryParams.keySet()) {
                Object val = queryParams.get(key);
                info.append(String.format("query: %s -> %s(%s)\n\n", key, val, val.getClass().getName()));
            }
        }
    }
}
