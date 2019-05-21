package com.shenzhen.laoutrmanager;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler.sendEmptyMessageDelayed(1,3000);
     }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<Integer> list = getItems();
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv);
            recyclerView.addItemDecoration(new SpaceItemDecoration(10));
            recyclerView.setLayoutManager(new TestLayoutManager()); //布局管理器
            recyclerView.setAdapter(new Adapter(MainActivity.this,list));
        }
    };


    private List<Integer> getItems() {
        List<Integer> showItems = new ArrayList<>();
        showItems.add(R.mipmap.ic_launcher);
        showItems.add(R.mipmap.ic_launcher_round);
        showItems.add(R.mipmap.gril);
        showItems.add(R.mipmap.ic_launcher);
        showItems.add(R.mipmap.ic_launcher_round);
        showItems.add(R.mipmap.gril);
        showItems.add(R.mipmap.ic_launcher);
        showItems.add(R.mipmap.ic_launcher_round);
        showItems.add(R.mipmap.gril);
        showItems.add(R.mipmap.ic_launcher);
        showItems.add(R.mipmap.ic_launcher_round);
        showItems.add(R.mipmap.gril);
        return showItems;
    }
}
