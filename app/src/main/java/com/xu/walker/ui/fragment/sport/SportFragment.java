package com.xu.walker.ui.fragment.sport;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.IBinder;

import com.orhanobut.logger.Logger;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

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
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by xusn10 on 2017/8/7.
 *
 * @author 许善宁
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
    TextView tvSportTime;
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
    @BindView(R.id.tv_sport_title)
    TextView tvSportTitle;
    public static final String SPORT_TYPE_BIKE = "骑行";
    public static final String SPORT_TYPE_RUN = "跑步";
    public static final String SPORT_TYPE_FOOTER = "徒步";
    public static final String SPORT_TYPE_SKIING = "滑雪";
    public static final String SPORT_TYPE_SWIMMING = "游泳";
    public static final String SPORT_TYPE_INDOOR = "室内训练";
    public static final String SPORT_TYPE_FREE = "自由行";


    /**
     * 当前选中的rb，默认的是自行车
     */
    private String sportType = SPORT_TYPE_BIKE;


    private static final int RC_LOCATION = 123;
    /**
     * 默认的定位时间间隔是3s一次
     */
    private int locationInterval = 3000;
    private static final int IS_SPORTING = 1;
    private static final int STOP_SPORTING = 0;
    /**
     * 默认停止运动
     */
    private int sportStatus = STOP_SPORTING;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_sport;
    }


    @Override
    public void initOthers() {
        mPresenter.bindService(getContext());
        mPresenter.start();
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
                //先判断运动状态
                if (sportStatus == IS_SPORTING) {
                    ToastUtil.toastShort(getContext(), getResources().getString(R.string.fg_sport_can_not_choose_type));
                } else if (sportStatus == STOP_SPORTING) {
                    showPopupWindow();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setSpeed(String speed) {
        tvSpeed.setText(speed);
    }

    @Override
    public void setMileage(String mileage) {
        tvMileage.setText(mileage);
    }

    @Override
    public void setTime(String time) {
        tvSportTime.setText(time);
    }

    @Override
    public void setAverageSpeed(float averageSpeed) {

    }

    @Override
    public void setMaxSpeed(String maxSpeed) {
        tvMaxSpeed.setText(maxSpeed);
    }

    @Override
    public void setAltitude(String altitude) {
        tvAltitude.setText(altitude);
    }

    @Override
    public void setClimb(String climb) {
        tvClimb.setText(climb);
    }

    @Override
    public void setTitle(String title) {
        tvSportTitle.setText(title);
    }

    @Override
    public void showContinueSportUI(String sportDistance, String sportTime) {
        Logger.d("运动碎片收到");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String message = "你上一次已运动" + sportDistance + "km,用时" + sportTime + ",是否继续?";
        Logger.d(message);
        AlertDialog dialog = builder.setTitle("是否继续")
                .setMessage(message)
                .setNegativeButton("继续上次", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setPositiveButton("开始新的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.reStartSports(locationInterval, sportType);
                    }
                })
                .create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#8C8C8C"));
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1DA6DD"));

    }

    @Override
    public void changeUiToBeginSport() {
        btStart.setBackgroundColor(Color.parseColor("#E84E40"));
        btStart.setText(getResources().getString(R.string.fg_sport_end_sport));
        //计时
        sportStatus = IS_SPORTING;
    }

    @Override
    public void changeUiToStopSport() {
        btStart.setBackgroundColor(Color.parseColor("#189ADB"));
        btStart.setText(getResources().getString(R.string.fg_sport_begin_riding));
        sportStatus = STOP_SPORTING;
        //初始化tv的值
        tvSpeed.setText(R.string.fg_sport_init_text_0_00);
        tvMileage.setText(R.string.fg_sport_init_text_0_00);
        tvSportTime.setText(R.string.fg_sport_init_text_00_00);
        tvAverageSpeed.setText(R.string.fg_sport_init_text_0_00);
        tvMaxSpeed.setText(R.string.fg_sport_init_text_0_00);
        tvAltitude.setText(R.string.fg_sport_init_text_0_0);
        tvClimb.setText(R.string.fg_sport_init_text_0_0);
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void startSport() {
        //有权限，直接执行
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            switch (sportStatus) {
                case IS_SPORTING:
                    //停止运动
                    mPresenter.stopSport();
                    changeUiToStopSport();
                    break;
                case STOP_SPORTING:
                    mPresenter.checkSportsFrDB(locationInterval, sportType);
                    break;
                default:
                    break;
            }

        } else {
            //申请权限
            EasyPermissions.requestPermissions(this, getString(R.string.fg_sport_sport_location_permission),
                    RC_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }

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
        switch (sportType) {
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
            default:
                break;
        }
        rbBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_BIKE, popupWindow, getResources().getString(R.string.fg_sport_begin_riding), getResources().getString(R.string.toast_sport_type_bike), R.mipmap.sports_fg_bike_select, 3000);
            }
        });

        rbRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_RUN, popupWindow, getResources().getString(R.string.fg_sport_begin_running), getResources().getString(R.string.toast_sport_type_run), R.mipmap.sports_fg_run_select, 5000);
            }
        });
        rbFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_FOOTER, popupWindow, getResources().getString(R.string.fg_sport_begin_footer), getResources().getString(R.string.toast_sport_type_foot), R.mipmap.sports_fg_footer_select, 10000);
            }
        });
        rbSkiing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_SKIING, popupWindow, getResources().getString(R.string.fg_sport_begin_skiing), getResources().getString(R.string.toast_sport_type_skiing), R.mipmap.sports_fg_skiing_select, 3000);
            }
        });
        rbSwimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_SWIMMING, popupWindow, getResources().getString(R.string.fg_sport_begin_swimming), getResources().getString(R.string.toast_sport_type_swimming), R.mipmap.sports_fg_swimming_select, 10000);
            }
        });
        rbIndoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_INDOOR, popupWindow, getResources().getString(R.string.fg_sport_begin_indoor), getResources().getString(R.string.toast_sport_type_indoor), R.mipmap.sports_fg_indoor_select, 10000);
            }
        });
        rbFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeSelectAction(SPORT_TYPE_FREE, popupWindow, getResources().getString(R.string.fg_sport_begin_free), getResources().getString(R.string.toast_sport_type_free), R.mipmap.sports_fg_free_select, 2000);
            }
        });

    }

    private void setBackgroundAlph(float alph) {
        //  设置其他位置灰色
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alph;
        getActivity().getWindow().setAttributes(lp);
    }


    /**
     * @param setSelectRB      选择的type
     * @param popupWindow      pop
     * @param btStartText      开始按钮的text
     * @param toastString      吐司string
     * @param drawable         设置图片
     * @param locationInterval 设置定位时间的间隔
     */
    private void setTypeSelectAction(String setSelectRB, PopupWindow popupWindow, String btStartText, String toastString, int drawable, int locationInterval) {

        sportType = setSelectRB;
        //设置主activity的导航图片
        ((MainActivity) getActivity()).setNavigationImg(setSelectRB);
        //设置选择类型的图片
        imgSportType.setImageDrawable(ContextCompat.getDrawable(getContext(), drawable));
        popupWindow.dismiss();
        btStart.setText(btStartText);
        ToastUtil.toastShort(getContext(), toastString);
        this.locationInterval = locationInterval;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribeRxBus();
        mPresenter.unBindService(getContext());
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
        //当用户拒绝权限的时候提示
        if (!EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
        }
    }
}
