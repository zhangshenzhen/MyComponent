package main.shenzhen.com.componentproject.shopingcar;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.shenzhen.com.componentproject.R;
import main.shenzhen.com.componentproject.shopingcar.adapter.RightAdapter;
import main.shenzhen.com.componentproject.shopingcar.adapter.linkagemenu.PinnedHeaderListView;
import main.shenzhen.com.componentproject.shopingcar.bean.ProductListBean;
import main.shenzhen.com.componentproject.shopingcar.bean.ProductTypeListBean;

public class ShopingCarAnimationActivity extends AppCompatActivity implements View.OnClickListener ,onCallBackListener{
    private LinearLayout back;
    private TextView title;
    private boolean isScroll = true;
    private ListView mainList;
    private PinnedHeaderListView moreList;

    private Map<String, String> params = new HashMap<>();


    private RightAdapter rightAdapter;   //右侧适配器

    private List<ProductTypeListBean> productTypeslist = new ArrayList<>();

    private boolean isClean = false;          // 是否完成清理
    private int number = 0;                    // 正在执行的动画数量
    private FrameLayout animation_viewGroup;
    private TextView settlement;               //点好了
    private ImageView shopping_cart;          //“惠”logo
    private TextView shopping;
    private List<ProductListBean> productList;

    private TextView shoppingNum;             //购物车件数
    private int shopNum;

    private TextView shoppingPrise;           //购物车价格
    private String totalPrice;

    private String pkmuser;

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 用来清除动画后留下的垃圾
                    try {
                        animation_viewGroup.removeAllViews();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isClean = false;
                    break;
                default:
                    break;
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shoping_car);

        beforeInitView();
    }

    private void beforeInitView() {
        productList = new ArrayList<>();
        shopping = (TextView) findViewById(R.id.shopping);
        back = (LinearLayout) findViewById(R.id.shopping_back);
        title = (TextView) findViewById(R.id.menu_shopname);
        mainList = (ListView) findViewById(R.id.classify_mainlist);
        moreList = (PinnedHeaderListView) findViewById(R.id.classify_morelist);
        settlement = (TextView) findViewById(R.id.settlement);
        shopping_cart = (ImageView) findViewById(R.id.shopping_cart);
        shoppingNum = (TextView) findViewById(R.id.shoppingNum);
        shoppingPrise = (TextView) findViewById(R.id.shoppingPrice);
        animation_viewGroup = createAnimLayout();


        settlement.setOnClickListener(this);
        back.setOnClickListener(this);
        next2();
    }


    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private FrameLayout createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.shopping_back:
                finish();
                break;

        }
    }

    private void next2() {

        rightAdapter = new RightAdapter(this);
        moreList.setAdapter(rightAdapter);
        //动画
        rightAdapter.SetOnSetHolderClickListener(new RightAdapter.HolderClickListener() {
            @Override
            public void onHolderClick(Drawable drawable, int[] start_location) {
                doAnim(drawable, start_location);
            }

        });

    }

    private void doAnim(Drawable drawable, int[] start_location) {
        if (!isClean) {
            setAnim(drawable, start_location);
        } else {
            try {
                animation_viewGroup.removeAllViews();
                isClean = false;
                setAnim(drawable, start_location);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isClean = true;
            }
        }
    }

    /**
     * 动画效果设置
     *
     * @param drawable       将要加入购物车的商品
     * @param start_location 起始位置
     */
    @SuppressLint("NewApi")
    private void setAnim(Drawable drawable, int[] start_location) {
        Animation mScaleAnimation = new ScaleAnimation(1.2f, 0.6f, 1.2f, 0.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mScaleAnimation.setFillAfter(true);

        final ImageView iview = new ImageView(this);
        iview.setImageDrawable(drawable);
        final View view = addViewToAnimLayout(animation_viewGroup, iview,
                start_location);


        view.setAlpha(0.6f);

        int[] end_location = new int[2];
        settlement.getLocationInWindow(end_location);

        // 计算位移
        int endX = 0 - start_location[0] + 100;// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);


        Animation mRotateAnimation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF,
                0.3f);
        mRotateAnimation.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(mRotateAnimation);
        set.addAnimation(mScaleAnimation);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);// 动画的执行时间
        view.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                number++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                number--;
                if (number == 0) {
                    isClean = true;
                    myHandler.sendEmptyMessage(0);
                }

                ObjectAnimator.ofFloat(shopping_cart, "translationY", 0, 4, -2, 0).setDuration(400).start();
                ObjectAnimator.ofFloat(shoppingNum, "translationY", 0, 4, -2, 0).setDuration(400).start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
    }

    /**
     * @param vg       动画运行的层 这里是frameLayout
     * @param view     要运行动画的View
     * @param location 动画的起始位置
     * @return
     * @deprecated 将要执行动画的view 添加到动画层
     */
    private View addViewToAnimLayout(ViewGroup vg, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);

        return view;
    }


    @Override
    public void updateProduct(/*ProductListBean product, String type*/) {

        setPrise();
    }

    /**
     * 更新购物车价格
     */
    public void setPrise() {
        double sum = 0;
        shopNum = 0;
        for (ProductListBean pro : productList) {
            sum = sum(sum, mul((double) pro.getNumber(), Double.parseDouble(pro.getProductPrice())));
            shopNum = shopNum + pro.getNumber();
        }

        if (shopNum > 0) {
            settlement.setBackgroundColor(Color.parseColor("#f94c64"));
            shopping_cart.setBackgroundResource(getResources().getIdentifier("hui", "drawable", "com.bjypt.vipcard"));

        } else {
            settlement.setBackgroundColor(Color.parseColor("#B0B0B0"));
            shopping_cart.setBackgroundResource(getResources().getIdentifier("menu_hui_gray", "drawable", "com.bjypt.vipcard"));
        }

        if (shopNum > 0) {
            shoppingNum.setVisibility(View.VISIBLE);
        } else {
            shoppingNum.setVisibility(View.GONE);
        }
        if (sum > 0) {
            shoppingPrise.setVisibility(View.VISIBLE);
            shopping.setVisibility(View.VISIBLE);
        } else {
            shoppingPrise.setVisibility(View.GONE);
            shopping.setVisibility(View.GONE);
        }
        totalPrice = (new DecimalFormat("0.00")).format(sum);
        shoppingPrise.setText("¥" + " " + (new DecimalFormat("0.00")).format(sum));
        shoppingNum.setText(String.valueOf(shopNum));
    }


    public double sum(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }
    public  double mul(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }


}
