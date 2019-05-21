package com.shenzhen.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_PERMISSION_LOCATION = 100;
    private TextView tvDevices;

    //定义全局变量
    private ListView lvdevice;
    private BluetoothAdapter bluetoothAdapter;
    private List<String> bluetoothdevice = new ArrayList<>();
    //private final UUID my_uuid = UUID.fromString("abcd1234-ab12-ab12-abcdef123456");
    private BluetoothSocket bluetoothSocket;
    private OutputStream os;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDevices = (TextView) findViewById(R.id.tv_devices);
        // handler.sendEmptyMessageDelayed(1,2000);

        requestPromession();

        lvdevice = findViewById(R.id.list);

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //弹出对话框 提示用户授权打开蓝牙
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
        }
    };


    private void requestPromession() {

        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            List<String> permissionDeniedList = new ArrayList<>();
            for (String permission : permissions) {
                int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                   openBuletooth();
                } else {
                    permissionDeniedList.add(permission);
                    openBuletooth();
                }
            }
            if (!permissionDeniedList.isEmpty()) {
                String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
                ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
            }

        } else {
            openBuletooth();
        }

    }

    private void openBuletooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.enable();//打开蓝牙

        //获取已经配对的蓝牙设备
        Set<BluetoothDevice> pairdevices = bluetoothAdapter.getBondedDevices();
        if (pairdevices.size() > 0) {
            for (BluetoothDevice device : pairdevices) {
                bluetoothdevice.add(device.getName() + " : " + device.getAddress());
                tvDevices.append("已经配对的蓝牙设备：" + device.getName() + " : " + device.getAddress() + "\n");
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void onClick(View v) {
        tvDevices.setText("");
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();

        //发送广播扫描周围蓝牙设备；
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        filter.setPriority(Integer.MAX_VALUE);//设置优先级
//        this.registerReceiver(receiver,filter);
    }

    public final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDING) {
                    tvDevices.append(" \n" + device.getName() + " : " + device.getAddress() + "\n");
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(context, "已经搜索完成", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        openBuletooth();
    }


}
