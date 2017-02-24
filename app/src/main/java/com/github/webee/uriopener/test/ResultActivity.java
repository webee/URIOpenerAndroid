package com.github.webee.uriopener.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends AppCompatActivity {
    @BindView(R.id.resultView)
    EditText resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.finishBtn)
    public void doFinish() {
        setResult(Integer.parseInt(resultView.getText().toString()));
        finish();
    }
}
