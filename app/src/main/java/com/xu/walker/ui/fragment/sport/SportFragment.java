package com.xu.walker.ui.fragment.sport;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xu.walker.R;
import com.xu.walker.TestActivity;
import com.xu.walker.base.BaseFragment;
import com.xu.walker.ui.activity.sportmap.SportMapActivity;
import com.xu.walker.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class SportFragment extends BaseFragment<SportContract.ISportPresenter> implements SportContract.ISportView {
    @BindView(R.id.img_sport_photograph)
    ImageView imgPhotograph;
    @BindView(R.id.img_sport_setting)
    ImageView imgSetting;
    @BindView(R.id.tv_sport_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_sport_mileage)
    TextView tvMileage;
    @BindView(R.id.tv_sport_time)
    TextView tvTime;
    @BindView(R.id.tv_sport_average_speed)
    TextView tvAverageSpeed;
    @BindView(R.id.tv_sport_max_speed)
    TextView tvMaxSpeed;
    @BindView(R.id.tv_sport_altitude)
    TextView tvAltitude;
    @BindView(R.id.tv_sport_climb)
    TextView tvClimb;
    @BindView(R.id.bt_sport_start)
    Button btStart;
    @BindView(R.id.bt_sport_map)
    Button btMap;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_sport_switch)
    TextView tvSportSwitch;
    @BindView(R.id.img_sport_type)
    ImageView imgSportType;
    @BindView(R.id.title_view)
    View titleView;

    private static final int SPORT_TYPE_BIKE = 1;
    private static final int SPORT_TYPE_RUN = 2;
    private static final int SPORT_TYPE_FOOTER = 3;
    private static final int SPORT_TYPE_SKIING = 4;
    private static final int SPORT_TYPE_SWIMMING = 5;
    private static final int SPORT_TYPE_INDOOR = 6;
    private static final int SPORT_TYPE_FREE = 7;
    //当前选中的rb
    private int selectRadioButton = SPORT_TYPE_BIKE;
    RadioButton rbBike = null;
    RadioButton rbRun = null;
    RadioButton rbFooter = null;
    RadioButton rbSkiing = null;
    RadioButton rbSwimming = null;
    RadioButton rbIndoor = null;
    RadioButton rbFree = null;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sport;
    }


    @Override
    public void initOthers() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ViewGroup.LayoutParams params = viewStatusBar.getLayoutParams();
        params.height = getStatusBarHeight();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void useNightMode(boolean isNight) {

    }


    @OnClick({R.id.img_sport_photograph, R.id.img_sport_setting, R.id.bt_sport_start, R.id.bt_sport_map, R.id.tv_sport_switch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_sport_photograph:
                ToastUtil.toastShort(getContext(), "拍照");
                break;
            case R.id.img_sport_setting:
                ToastUtil.toastShort(getContext(), "设置");
                break;
            case R.id.bt_sport_start:
                ToastUtil.toastShort(getContext(), "开始");
                Intent intent1 = new Intent(getActivity(), TestActivity.class);
                startActivity(intent1);
                break;
            case R.id.bt_sport_map:
                Intent intent = new Intent(getActivity(), SportMapActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sport_switch:
                showPopupWindow();
                break;
        }
    }

    @Override
    public void setSpeed(float speed) {

    }

    @Override
    public void setMileage(float mileage) {

    }

    @Override
    public void setTime() {

    }

    @Override
    public void setAverageSpeed(float averageSpeed) {

    }

    @Override
    public void setMaxSpeed(float maxSpeed) {

    }

    @Override
    public void setAltitude(float altitude) {

    }

    @Override
    public void setClimb(float climb) {

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void showPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_sport_switch, null);

        final PopupWindow popupWindow = new PopupWindow(view, 800, 400);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(titleView, 40, 40);
        setBackgroundAlph(0.8f);
        //设置背景
        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.cl_sport_switch);
        GradientDrawable gdInfo = (GradientDrawable) constraintLayout.getBackground();
        gdInfo.setStroke(1, Color.WHITE);
        gdInfo.setColor(Color.WHITE);
        gdInfo.setCornerRadius(15);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlph(1.0f);
            }
        });

        final RadioButton rbBike = ButterKnife.findById(view, R.id.rb_bike);
        final RadioButton rbRun = ButterKnife.findById(view, R.id.rb_run);
        final RadioButton rbFooter = ButterKnife.findById(view, R.id.rb_footer);
        final RadioButton rbSkiing = ButterKnife.findById(view, R.id.rb_skiing);
        final RadioButton rbSwimming = ButterKnife.findById(view, R.id.rb_swimming);
        final RadioButton rbIndoor = ButterKnife.findById(view, R.id.rb_indoor);
        final RadioButton rbFree = ButterKnife.findById(view, R.id.rb_free);
        switch (selectRadioButton) {
            case SPORT_TYPE_BIKE:
                rbBike.setChecked(true);
                break;
            case SPORT_TYPE_RUN:
                rbRun.setChecked(true);
                break;
            case SPORT_TYPE_FOOTER:
                rbFooter.setChecked(true);
                break;
            case SPORT_TYPE_SKIING:
                rbSkiing.setChecked(true);
                break;
            case SPORT_TYPE_SWIMMING:
                rbSwimming.setChecked(true);
                break;
            case SPORT_TYPE_INDOOR:
                rbIndoor.setChecked(true);
                break;
            case SPORT_TYPE_FREE:
                rbFree.setChecked(true);
                break;
        }
        rbBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRadioButton = SPORT_TYPE_BIKE;
                popupWindow.dismiss();
                ToastUtil.toastShort(getContext(), getResources().getString(R.string.toast_sport_type_bike));
            }
        });

        rbRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRadioButton = SPORT_TYPE_RUN;
                popupWindow.dismiss();
                ToastUtil.toastShort(getContext(), getResources().getString(R.string.toast_sport_type_run));
            }
        });
        rbFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRadioButton = SPORT_TYPE_FOOTER;
                popupWindow.dismiss();
                ToastUtil.toastShort(getContext(), getResources().getString(R.string.toast_sport_type_foot));
            }
        });
        rbSkiing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRadioButton = SPORT_TYPE_SKIING;
                popupWindow.dismiss();
                ToastUtil.toastShort(getContext(), getResources().getString(R.string.toast_sport_type_skiing));
            }
        });
        rbSwimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRadioButton = SPORT_TYPE_SWIMMING;
                popupWindow.dismiss();
                ToastUtil.toastShort(getContext(), getResources().getString(R.string.toast_sport_type_swimming));
            }
        });
        rbIndoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRadioButton = SPORT_TYPE_INDOOR;
                popupWindow.dismiss();
                ToastUtil.toastShort(getContext(), getResources().getString(R.string.toast_sport_type_indoor));
            }
        });
        rbFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRadioButton = SPORT_TYPE_FREE;
                popupWindow.dismiss();
                ToastUtil.toastShort(getContext(), getResources().getString(R.string.toast_sport_type_free));
            }
        });

    }

    private void setBackgroundAlph(float alph) {
        //  设置其他位置灰色
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alph; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


}
