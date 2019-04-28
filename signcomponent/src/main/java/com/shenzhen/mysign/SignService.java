package com.shenzhen.mysign;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import main.shenzhen.com.sharedlibrary.inter.ISignService;

public class SignService implements ISignService{
    @Override
    public void launchSign(Context context, String userid) {
        Log.i("执行","signService......");
        context.startActivity(new Intent(context,SignActivity.class));
    }


}
