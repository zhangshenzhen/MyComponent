package com.shenzhen.photo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  ActivityCompat.OnRequestPermissionsResultCallback {

    private ImageView user_img;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 100;
    private TextView tvDevices;

    //定义全局变量

    private Uri imageUri;
    private Bitmap photo;
    private Time time_gameImg;
    private Uri photoUri;
    private String imageFileName, filepath, str_datePic;
    private static final int PHOTO_REQUEST_CAMERA = 1;   // 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;  // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;      // 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    // 存储图片的路径
    private String PATH = Environment.getExternalStorageDirectory() + "/vipcard/Camera/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_img = findViewById(R.id.user_img);
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        requestPromession();
    }
    public void takePicture(View view){
        useCamera();
    }
    public void fromPicture(View view){
        // 获取相册
        Intent intent1 = new Intent(Intent.ACTION_PICK,
                null);
        intent1.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);

        startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);

    }

    /**
     * 使用相机
     */
    private void useCamera() {
        // 设置时区
        time_gameImg = new Time("GMT+8");
        time_gameImg.setToNow();       //当前时间
        str_datePic = time_gameImg.year
                + (time_gameImg.month + 1)
                + time_gameImg.monthDay + time_gameImg.hour
                + time_gameImg.minute + time_gameImg.second
                + ".jpg";

        File f2 = new File(PATH); //创建文件夹
        if (!f2.exists()) {
            f2.mkdirs();
        }
        // 调用系统照相机
        // 拍照动作完成时图片被存储到指定路径
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PATH, str_datePic)));
        saveDataToSharePreference("str_datePic", str_datePic);
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
//        com.orhanobut.logger.Logger.e("照片机被调用");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_CAMERA){
            str_datePic = getFromSharedPreference("str_datePic");
            File picture = new File(PATH + str_datePic);
            //生成URI地址
            imageUri = Uri.fromFile(picture);
//            Logger.e(imageUri.toString());
            startPhotoZoom(getUri());              //拍照进入裁剪状态
        }

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                imageUri = data.getData();
//                Logger.e(imageUri.toString());
                startPhotoZoom(data.getData());                       //裁剪图片
            }
        }

        if (requestCode == PHOTO_REQUEST_CUT) {
            if (null != data) {

                try {
                    Bitmap bp = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                    user_img.setImageBitmap(bp);
                    //根据用户主键存储用户头像
                    //                    String imgHand = getFromSharePreference(pkregister)
//                            + System.currentTimeMillis() + ".jpg";
                    Time time = new Time("GMT+8");                          // 设置时区
                    time.setToNow();                                         // 当前时间// saveImage(bp, imgHand);
                    //发送广播，更改SlidingMenu头像
                    //                   RegisterReceiverUtils.getInstance().sendBroadcast(getActivity(), "img_hand", null);

//                    String imageUrl = ImageDownloader.Scheme.FILE.wrap(outputImage + "");
//
//                    riv_user_photo.setVisibility(View.VISIBLE);
//                    ImageLoader.getInstance().displayImage(imageUrl, riv_user_photo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }

    }
    private Uri getUri() {
        str_datePic = getFromSharedPreference("str_datePic");
        File path = new File(PATH);
//        File path = new File(Environment.getExternalStorageDirectory(), "Android/data/包名/files/header");
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, str_datePic);
        //由于一些Android 7.0以下版本的手机在剪裁保存到URI会有问题，所以根据版本处理下兼容性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(this, "com.shenzhen.photo.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 进入裁剪状态 （剪裁图片）
     *
     * @param uri 图片的路径
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        //Stirng picLocNameString = uri.getPath();
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        photoUri = Uri.fromFile(new File(PATH, "head.jpg"));
//        Logger.e(Uri.fromFile(new File(PATH, "head.jpg")).toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PATH, "head.jpg")));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // 取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    /**存储数据到SharePreference**/
    public  void saveDataToSharePreference(String key,String value){
        SharedPreferences sharedPreferences = getSharedPreferences("hyb", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getFromSharedPreference(String key){
        String string;
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("hyb",Context.MODE_PRIVATE);
            string = sharedPreferences.getString(key,"");
        }catch (Exception e){
            return "";
        }
        return string;
    }

    private void requestPromession() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,Manifest.permission.READ_EXTERNAL_STORAGE};
            List<String> permissionDeniedList = new ArrayList<>();
            for (String permission : permissions) {
                int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                } else {
                    permissionDeniedList.add(permission);
                }
            }
            if (!permissionDeniedList.isEmpty()) {
                String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
                ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //是否得到权限、、此处偷懒省略。。。
    }

}
