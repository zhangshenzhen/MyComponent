package com.shenzhen.mysign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import main.shenzhen.com.sharedlibrary.ServiceFactory;


public class SignActivity extends AppCompatActivity {
   /*
   * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        findViewById(R.id.tvback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //module2  跳转到module1
            ServiceFactory.getInstance().getLoginService().launchLogin(SignActivity.this,"");
            }
        });
    }
}
