package com.xu.walker.ui.fragment.sport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.xu.walker.R;
import com.xu.walker.base.BaseFragment;
import com.xu.walker.utils.ToastUtil;

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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sport;
    }

    @Override
    public int getScreenMode() {
        return DYEING_MODE;
    }

    @Override
    public void initOthers() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

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


    @OnClick({R.id.img_sport_photograph, R.id.img_sport_setting, R.id.bt_sport_start, R.id.bt_sport_map})
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
                break;
            case R.id.bt_sport_map:
                ToastUtil.toastShort(getContext(), "地图");
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
}
