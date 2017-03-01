package com.github.webee.uriopener.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.webee.uriopener.core.Request;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.webee.uriopener.core.Request.EXTRA_PATH_PARAMS;
import static com.github.webee.uriopener.core.Request.EXTRA_QUERY_PARAMS;

public class DumpRequestActivity extends AppCompatActivity {
    @BindView(R.id.info)
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dump_request);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Uri uri = intent.getData();
        Bundle pathParams = intent.getBundleExtra(Request.EXTRA_PATH_PARAMS);
        Bundle queryParams = intent.getBundleExtra(Request.EXTRA_QUERY_PARAMS);

        info.setText(getClass().getName() + "\n");

        info.append("\n");
        info.append(String.format("uri => %s\n", uri.toString()));

        // path params
        if (pathParams != null) {
            info.append("\n");
            info.append("Path Parameters:\n");
            for (String key : pathParams.keySet()) {
                Object val = pathParams.get(key);
                info.append(String.format("path: %s -> %s(%s)\n", key, val, val.getClass().getName()));
            }
        }

        // query params
        if (queryParams != null) {
            info.append("\n");
            info.append("Query Parameters:\n");
            for (String key : queryParams.keySet()) {
                Object val = queryParams.get(key);
                info.append(String.format("query: %s -> %s(%s)\n", key, val, val.getClass().getName()));
            }
        }

        // extras
        Bundle extras = new Bundle();
        extras.putAll(intent.getExtras());
        extras.remove(EXTRA_PATH_PARAMS);
        extras.remove(EXTRA_QUERY_PARAMS);

        if (extras.size() > 0) {
            info.append("\n");
            info.append("Extras:\n");
            for (String key : extras.keySet()) {
                Object val = extras.get(key);
                info.append(String.format("extra: %s -> %s(%s)\n", key, val, val.getClass().getName()));
            }
        }
    }
}
