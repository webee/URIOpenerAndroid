package com.github.webee.urirouter.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(1234);
        finish();
    }
}
