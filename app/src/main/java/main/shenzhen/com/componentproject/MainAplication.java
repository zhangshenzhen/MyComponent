package main.shenzhen.com.componentproject;

import android.app.Application;
import android.util.Log;

import main.shenzhen.com.sharedlibrary.AppConfig;
import main.shenzhen.com.sharedlibrary.ServiceFactory;
import main.shenzhen.com.sharedlibrary.inter.IComponentApplication;

public class MainAplication extends Application implements IComponentApplication {
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
    public void initialize(Application application) {
        for (String cpnt : AppConfig.Components) {
            try {
                Class<?> clz = Class.forName(cpnt);
                Object obj = clz.newInstance();
                if (obj instanceof IComponentApplication) {
                    ((IComponentApplication) obj).initialize(this);
                }
            } catch (Exception e) {
                Log.e("initialize", e.getMessage());
            }
        }

        //注册调用主app监听对象 有库到主app
        ServiceFactory.getInstance().setmIBackMain(new IServaceBack());
    }


}
