package com.shenzhen.mylogin;

import android.app.Application;

import main.shenzhen.com.sharedlibrary.EmptyStartMain;
import main.shenzhen.com.sharedlibrary.inter.IComponentApplication;
import main.shenzhen.com.sharedlibrary.ServiceFactory;

public class LoginApplication extends Application implements IComponentApplication {
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize(this);
        initLone();
    }

    private void initLone() { //作为独立app 使用的
        ServiceFactory.getInstance().setmIBackMain(new EmptyStartMain());
    }

    @Override
    public void initialize(Application app) {
        application = app;
        //注册调用Logincomponent监听对象
        ServiceFactory.getInstance().setLoginService(new LoginService());
    }
}
