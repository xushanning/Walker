package com.xu.walker.ui.fragment.personal;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xu.walker.R;
import com.xu.walker.base.BaseFragment;
import com.xu.walker.ui.activity.history.HistoryActivity;
import com.xu.walker.ui.activity.login.LoginActivity;
import com.xu.walker.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


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
    @BindView(R.id.cl_login)
    ConstraintLayout clLogin;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.img_history)
    ImageView imgHistory;


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
        gdInfo.setStroke(2, ContextCompat.getColor(getContext(), R.color.colorWhite));
        gdInfo.setColor(ContextCompat.getColor(getContext(), R.color.colorTransparent));
        gdInfo.setCornerRadius(8);

        GradientDrawable gdGrade = (GradientDrawable) tvGrade.getBackground();
        gdGrade.setStroke(2, ContextCompat.getColor(getContext(), R.color.fg_personal_grade_bg));
        gdGrade.setColor(ContextCompat.getColor(getContext(), R.color.fg_personal_grade_bg));
        gdGrade.setCornerRadius(40);

        GradientDrawable gdAccount = (GradientDrawable) tvAccount.getBackground();
        gdAccount.setStroke(2, ContextCompat.getColor(getContext(), R.color.fg_personal_grade_bg));
        gdAccount.setColor(ContextCompat.getColor(getContext(), R.color.fg_personal_grade_bg));
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


    @OnClick({R.id.tv_history, R.id.img_history, R.id.cl_login})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_history:
                intent = new Intent(getContext(), HistoryActivity.class);
                break;
            case R.id.img_history:
                intent = new Intent(getContext(), HistoryActivity.class);
                break;
            case R.id.cl_login:
                intent = new Intent(getActivity(), LoginActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
