package com.shenzhen.mylogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import main.shenzhen.com.sharedlibrary.inter.ILoginService;

public class LoginService implements ILoginService {
    @Override
    public void launchLogin(Context ctx, String targetClass) {
        ctx.startActivity(new Intent(ctx,LoginActivity.class));
        Log.i("执行","LoginService......");
    }

    @Override
    public Fragment newUserInfoFragment(FragmentManager fragmentManager, int viewId, Bundle bundle) {
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(bundle); fragmentManager.beginTransaction().replace(viewId, fragment).commit();
        return fragment;
    }
}
