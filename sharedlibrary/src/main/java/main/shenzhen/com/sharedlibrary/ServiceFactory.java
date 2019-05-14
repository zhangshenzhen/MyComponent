package main.shenzhen.com.sharedlibrary;

import main.shenzhen.com.sharedlibrary.inter.IBackMain;
import main.shenzhen.com.sharedlibrary.inter.ILoginService;
import main.shenzhen.com.sharedlibrary.inter.ISignService;

public class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();
    private ILoginService mLoginService;
    private ISignService mSignService;
    private IBackMain mIBackMain;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public ILoginService getLoginService() {
        if (mLoginService == null) {
            mLoginService = new EmptyLoginService();
        }
        return mLoginService;
    }

    public void setLoginService(ILoginService mLoginService) {
        this.mLoginService = mLoginService;
    }

    public ISignService getSignService() {
        if (mSignService == null) {
            mSignService = new EmptySignService();
        }
        return mSignService;
    }

    public void setSignService(ISignService mSignService) {
        this.mSignService = mSignService;
    }


    public IBackMain getmIBackMain() {
        if (mIBackMain==null){
            mIBackMain = new EmptyStartMain();
        }
        return mIBackMain;
    }

    public void setmIBackMain(IBackMain mIBackMain) {

        this.mIBackMain = mIBackMain;
    }

}
