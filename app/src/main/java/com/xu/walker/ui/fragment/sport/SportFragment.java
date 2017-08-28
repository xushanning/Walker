package com.xu.walker.ui.fragment.sport;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xu.walker.R;
import com.xu.walker.base.BaseFragment;
import com.xu.walker.service.MainService;
import com.xu.walker.ui.activity.main.MainActivity;
import com.xu.walker.ui.activity.sportmap.SportMapActivity;
import com.xu.walker.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class SportFragment extends BaseFragment<SportContract.ISportPresenter> implements SportContract.ISportView, EasyPermissions.PermissionCallbacks {
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

    public static final int SPORT_TYPE_BIKE = 1;
    public static final int SPORT_TYPE_RUN = 2;
    public static final int SPORT_TYPE_FOOTER = 3;
    public static final int SPORT_TYPE_SKIING = 4;
    public static final int SPORT_TYPE_SWIMMING = 5;
    public static final int SPORT_TYPE_INDOOR = 6;
    public static final int SPORT_TYPE_FREE = 7;
    //当前选中的rb
    private int selectRadioButton = SPORT_TYPE_BIKE;

    private MainService.MyBinder myBinder;

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
        //Logger.d("初始化presenter");
        mPresenter = new SportPresenter();
        mPresenter.start();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void useNightMode(boolean isNight) {

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (MainService.MyBinder) iBinder;
            myBinder.startSport();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


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
                startSport();
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

    //@AfterPermissionGranted( Manifest.permission.ACCESS_FINE_LOCATION)
    private void startSport() {
//        if (EasyPermissions.hasPermissions(this, perms)) {
//
//        } else {
//            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
//                    RC_CAMERA_AND_LOCATION, perms);
//        }
        Intent bindIntent = new Intent(getContext(), MainService.class);
        getContext().bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
        btStart.setBackgroundColor(Color.parseColor("#E84E40"));
        btStart.setText(getResources().getString(R.string.fg_sport_end_sport));
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
                setTypeSelectAction(SPORT_TYPE_BIKE, popupWindow, getResources().getString(R.string.fg_sport_begin_riding), getResources().getString(R.string.toast_sport_type_bike), R.mipmap.sports_fg_bike_select);
            }
        });

        rbRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_RUN, popupWindow, getResources().getString(R.string.fg_sport_begin_running), getResources().getString(R.string.toast_sport_type_run), R.mipmap.sports_fg_run_select);
            }
        });
        rbFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_FOOTER, popupWindow, getResources().getString(R.string.fg_sport_begin_footer), getResources().getString(R.string.toast_sport_type_foot), R.mipmap.sports_fg_footer_select);
            }
        });
        rbSkiing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_SKIING, popupWindow, getResources().getString(R.string.fg_sport_begin_skiing), getResources().getString(R.string.toast_sport_type_skiing), R.mipmap.sports_fg_skiing_select);
            }
        });
        rbSwimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_SWIMMING, popupWindow, getResources().getString(R.string.fg_sport_begin_swimming), getResources().getString(R.string.toast_sport_type_swimming), R.mipmap.sports_fg_swimming_select);
            }
        });
        rbIndoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_INDOOR, popupWindow, getResources().getString(R.string.fg_sport_begin_indoor), getResources().getString(R.string.toast_sport_type_indoor), R.mipmap.sports_fg_indoor_select);
            }
        });
        rbFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_FREE, popupWindow, getResources().getString(R.string.fg_sport_begin_free), getResources().getString(R.string.toast_sport_type_free), R.mipmap.sports_fg_free_select);
            }
        });

    }

    private void setBackgroundAlph(float alph) {
        //  设置其他位置灰色
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alph;
        getActivity().getWindow().setAttributes(lp);
    }

    //1.选择的type，2、pop 3、开始按钮的text 4、吐司string
    private void setTypeSelectAction(int setSelectRB, PopupWindow popupWindow, String btStartText, String toastString, int drawable) {
        selectRadioButton = setSelectRB;
        //设置主activity的导航图片
        ((MainActivity) getActivity()).setNavigationImg(setSelectRB);
        //设置选择类型的图片
        imgSportType.setImageDrawable(ContextCompat.getDrawable(getContext(), drawable));
        popupWindow.dismiss();
        btStart.setText(btStartText);
        ToastUtil.toastShort(getContext(), toastString);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribeRxBus();
        if (connection != null) {
            getContext().unbindService(connection);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {

    }
}
