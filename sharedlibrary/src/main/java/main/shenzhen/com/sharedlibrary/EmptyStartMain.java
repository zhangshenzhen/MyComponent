package main.shenzhen.com.sharedlibrary;

import android.content.Context;
import android.util.Log;

import main.shenzhen.com.sharedlibrary.inter.IBackMain;

public class EmptyStartMain implements IBackMain {
    @Override
    public void startMain(Context context, String st) {
        Log.i("执行","1..startMain......");
    }
}
