package com.shenzhen.mysign;

import android.app.Application;

import main.shenzhen.com.sharedlibrary.inter.IComponentApplication;
import main.shenzhen.com.sharedlibrary.ServiceFactory;

public class SignApplication extends Application implements IComponentApplication {
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize(this);
    }


    @Override
    public void initialize(Application app) {
        application = app;
        //注册调用Signcomponent监听对象
        ServiceFactory.getInstance().setSignService(new SignService());
    }
}
