package main.shenzhen.com.componentproject;

import android.content.Context;
import android.content.Intent;

import main.shenzhen.com.sharedlibrary.inter.IBackMain;

public class IServaceBack implements IBackMain {
    @Override
    public void startMain(Context context, String st) {
       context.startActivity(new Intent(context,SecondActivity.class));
    }
}
