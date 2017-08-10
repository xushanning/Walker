package com.xu.walker.ui.fragment.personal;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xu.walker.R;
import com.xu.walker.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class PersonalFragment extends BaseFragment<PersonalContract.IPersonalPresenter> {
    @BindView(R.id.img_pg_personal_setting)
    ImageView imgSetting;
    @BindView(R.id.tv_pg_personal_edit_info)
    TextView tvEditInfo;
    @BindView(R.id.tv_pg_personal_grade)
    TextView tvGrade;
    @BindView(R.id.tv_pg_personal_account)
    TextView tvAccount;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_personal;
    }


    @Override
    public void initOthers() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        GradientDrawable gdInfo = (GradientDrawable) tvEditInfo.getBackground();
        gdInfo.setStroke(2, getResources().getColor(R.color.colorWhite));
        gdInfo.setColor(getResources().getColor(R.color.colorTransparent));
        gdInfo.setCornerRadius(8);

        GradientDrawable gdGrade = (GradientDrawable) tvGrade.getBackground();
        gdGrade.setStroke(2, getResources().getColor(R.color.fg_personal_grade_bg));
        gdGrade.setColor(getResources().getColor(R.color.fg_personal_grade_bg));
        gdGrade.setCornerRadius(40);

        GradientDrawable gdAccount = (GradientDrawable) tvAccount.getBackground();
        gdAccount.setStroke(2, getResources().getColor(R.color.fg_personal_grade_bg));
        gdAccount.setColor(getResources().getColor(R.color.fg_personal_grade_bg));
        gdAccount.setCornerRadius(40);
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


}
