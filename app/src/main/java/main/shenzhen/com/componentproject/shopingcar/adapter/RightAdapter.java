package main.shenzhen.com.componentproject.shopingcar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import main.shenzhen.com.componentproject.R;
import main.shenzhen.com.componentproject.shopingcar.adapter.linkagemenu.SectionedBaseAdapter;
import main.shenzhen.com.componentproject.shopingcar.bean.ProductListBean;
import main.shenzhen.com.componentproject.shopingcar.bean.ProductTypeListBean;
import main.shenzhen.com.componentproject.shopingcar.onCallBackListener;



/**
 * Created by Administrator on 2017/4/13.
 */

public class RightAdapter extends SectionedBaseAdapter {

    private List<ProductTypeListBean> productTypes ;

    private LayoutInflater mInflater;
    private Context context;


    private HolderClickListener mHolderClickListener;
    private onCallBackListener callBackListener;

    public void setCallBackListener(onCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public RightAdapter(Context context) {

        this.context = context;
        productTypes  = new ArrayList<>();
        productTypes.add(0,new ProductTypeListBean());
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int section, int position) {
        return "hh";
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        //左边有多少分类
        return 1;
    }

    @Override
    public int getCountForSection(int section) {
        //右边每一个分类有几件产品
        return 1;
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.right_adapter_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mDishesName = (TextView) convertView.findViewById(R.id.dishes_name);
            viewHolder.mSellNum = (TextView) convertView.findViewById(R.id.sell_num_mouth);
            viewHolder.mDishesPrice = (TextView) convertView.findViewById(R.id.dishes_price);
            viewHolder.mVipCharge = (TextView) convertView.findViewById(R.id.tv_vipcharge);
            viewHolder.mplatformyouhui = (TextView) convertView.findViewById(R.id.tv_platformyouhui);
            viewHolder.mPlatformLinear = (RelativeLayout) convertView.findViewById(R.id.platform_pay_linear);
            viewHolder.mMerchantLinear = (RelativeLayout) convertView.findViewById(R.id.merchant_pay_linear);
            viewHolder.mDishesImg = (ImageView) convertView.findViewById(R.id.dishes_img);
            viewHolder.mRela = (RelativeLayout) convertView.findViewById(R.id.dishes_rela);
            viewHolder.mCbReduce = (ImageView) convertView.findViewById(R.id.dishes_reduce);
            viewHolder.mCbIncrease = (ImageView) convertView.findViewById(R.id.dishes_add);
            viewHolder.mDishesNum = (TextView) convertView.findViewById(R.id.dishes_num);
            viewHolder.lineView = convertView.findViewById(R.id.right_adapter_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.mCbIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (callBackListener != null) {
                    callBackListener.updateProduct();
                }
                notifyDataSetChanged();
                if (mHolderClickListener != null) {
                    int[] start_location = new int[2];
                    //获取点击商品图片的位置
                    viewHolder.mDishesNum.getLocationInWindow(start_location);
                    //复制一个新的商品图标
                    Drawable drawable = context.getResources().getDrawable(R.drawable.adddetail);
                    mHolderClickListener.onHolderClick(drawable, start_location);
                }
            }
        });




        return convertView;
    }

    /**
     * 右侧title的设置
     */
    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflater.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        //右侧title设值
        ((TextView) layout.findViewById(R.id.textItem)).setText(productTypes.get(section).getTypename());
        return layout;
    }


    class ViewHolder {
        RelativeLayout mRela;
        ImageView mDishesImg;
        TextView mDishesName, mSellNum, mDishesPrice;
        TextView mDishesNum;
        ImageView mCbReduce;
        ImageView mCbIncrease;
        TextView mVipCharge;
        TextView mplatformyouhui;
        RelativeLayout mPlatformLinear;
        RelativeLayout mMerchantLinear;
        View lineView;
    }

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
    }

    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }
}
