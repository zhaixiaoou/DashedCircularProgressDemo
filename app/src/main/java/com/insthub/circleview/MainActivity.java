package com.insthub.circleview;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mProgressValue;
    private Button      mbt;
    private DashedCircularProgress  mCircularProgress;
    private Button mBtnContinue;
    private Button mBtnInnerCircle;
    private Button  mBtnHead;
    private Button  mBtnInnerProgress;
    private TextView    mTvValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressValue = (EditText) findViewById(R.id.edit_progress);
        mbt = (Button) findViewById(R.id.button);
        mBtnContinue    = (Button) findViewById(R.id.progress_continue);
        mBtnHead        = (Button) findViewById(R.id.inner_circle_head);
        mBtnInnerCircle = (Button) findViewById(R.id.inner_circle);
        mBtnInnerProgress = (Button) findViewById(R.id.inner_circle_progress);
        mTvValue        = (TextView) findViewById(R.id.value_text);
        mbt.setOnClickListener(this);
        mBtnContinue.setOnClickListener(this);
        mBtnHead.setOnClickListener(this);
        mBtnInnerCircle.setOnClickListener(this);
        mBtnInnerProgress.setOnClickListener(this);
        mCircularProgress = (DashedCircularProgress) findViewById(R.id.progress);
        mCircularProgress.beginContinue(true);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                setValue();
                break;
            case R.id.progress_continue:
                if (mBtnContinue.getText().toString().equals(getResources().getString(R.string.progress_continue_false))){
                    mCircularProgress.beginContinue(true);
                    mBtnContinue.setText(R.string.progress_continue_true);
                }else {
                    mCircularProgress.beginContinue(false);
                    mBtnContinue.setText(R.string.progress_continue_false);
                }
                break;
            case R.id.inner_circle:
                if (mCircularProgress.isInnerCircle()){
                    mCircularProgress.setInnerCircle(false);
                    mBtnInnerCircle.setText(R.string.inner_circle_hide);
                }else{
                    mCircularProgress.setInnerCircle(true);
                    mBtnInnerCircle.setText(R.string.inner_circle_show);
                }
                break;
            case R.id.inner_circle_head:
                if (mCircularProgress.isHead()){
                    mCircularProgress.setHead(false);
                    mBtnHead.setText(R.string.head_hide);
                }else{
                    mCircularProgress.setHead(true);
                    mBtnHead.setText(R.string.head_show);
                }
                break;
            case R.id.inner_circle_progress:
                if (mCircularProgress.isInitCircleProgress()){
                    mCircularProgress.setInitCircleProgress(false);
                    mBtnInnerProgress.setText(R.string.inner_progress_hide);
                }else{
                    mCircularProgress.setInitCircleProgress(true);
                    mBtnInnerProgress.setText(R.string.inner_progress_show);
                }
                break;
        }
    }

    private void setValue() {
        String value = mProgressValue.getText().toString();
        int value_int;
        try{
            value_int = Integer.parseInt(value);
        }catch (NumberFormatException e){
            value_int = 0;
        }
        if (!TextUtils.isEmpty(value)){
            mCircularProgress.setmValue(value_int);
            if (mCircularProgress.isContinue()){
                String  preValue = mTvValue.getText().toString();
                int preValue_int = 0;
                if (preValue.contains("%")){
                    preValue = preValue.replace("%","");
                    preValue_int = Integer.parseInt(preValue);
                }
                value_int = preValue_int+value_int;
            }
            mTvValue.setText(value_int+"%");
        }else{
            if (mCircularProgress.isContinue()){
                String  preValue = mTvValue.getText().toString();
                int preValue_int = 0;
                if (preValue.contains("%")){
                    preValue = preValue.replace("%","");
                    preValue_int = Integer.parseInt(preValue);
                }
                mTvValue.setText(preValue_int+"%");
            }else{
                mTvValue.setText("0%");
            }

        }
    }
}
