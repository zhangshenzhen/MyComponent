package main.shenzhen.com.sharedlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import main.shenzhen.com.sharedlibrary.inter.ILoginService;

public class EmptyLoginService implements ILoginService {
    @Override
    public void launchLogin(Context ctx, String targetClass) {
        Log.i("执行","1..LoginService......");
    }

    @Override
    public Fragment newUserInfoFragment(FragmentManager fragmentManager, int viewId, Bundle bundle) {
        return null;
    }
}
