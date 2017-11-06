package com.xu.walker.ui.activity.history;

import android.os.Bundle;

import com.xu.walker.R;
import com.xu.walker.base.BaseActivity;

/**
 * @author xu
 */

public class HistoryActivity extends BaseActivity<HistoryContract.IHistoryPresenter> implements HistoryContract.IHistoryView {


    @Override
    public void showError(String msg) {

    }

    @Override
    public void useNightMode(boolean isNight) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initOthers() {

    }
}
