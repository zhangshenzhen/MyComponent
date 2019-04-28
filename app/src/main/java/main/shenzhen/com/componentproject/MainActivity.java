package main.shenzhen.com.componentproject;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import main.shenzhen.com.sharedlibrary.ServiceFactory;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private Button btnSign, btnLogin, btnShowUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        btnLogin = findViewById(R.id.login);
        btnSign = findViewById(R.id.sign);
        btnShowUserInfo = findViewById(R.id.fragment);

        btnSign.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnShowUserInfo.setOnClickListener(this);
    }

    private void sign() {
       // ServiceFactory.getInstance().setSignService(new SignService());
        ServiceFactory.getInstance().getSignService().launchSign(MainActivity.this, "");
    }

    private void login() {
        ServiceFactory.getInstance().getLoginService().launchLogin(MainActivity.this, "");
    }


    private void showUserInfo() {
        // v-4包的FragmentManager,注意是否是在Activity中使用，
        // 因为Activity中并没有此方法的定义，必须是继承FragmentActivity或者AppCompatActivity，然后使用

        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        ServiceFactory.getInstance().getLoginService().newUserInfoFragment(fragmentManager, R.id.llFragmentContainer, bundle);

    }
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.login:
                    login();
                    break;
                case R.id.sign:
                    sign();
                    break;
                case R.id.fragment:
                    showUserInfo();
                    break;
                default:
                    break;
            }
        }


    }
