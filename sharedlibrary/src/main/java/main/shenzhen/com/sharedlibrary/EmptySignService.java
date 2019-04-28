package main.shenzhen.com.sharedlibrary;

import android.content.Context;
import android.util.Log;

import main.shenzhen.com.sharedlibrary.inter.ISignService;

public class EmptySignService implements ISignService {

    @Override
    public void launchSign(Context context, String userid) {
        Log.i("执行","1..SignService......");
    }
}
