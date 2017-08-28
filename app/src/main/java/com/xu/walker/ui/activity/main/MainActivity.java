package com.xu.walker.ui.activity.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xu.walker.R;
import com.xu.walker.base.BaseActivity;
import com.xu.walker.ui.fragment.club.ClubFragment;
import com.xu.walker.ui.fragment.discover.DiscoverFragment;
import com.xu.walker.ui.fragment.personal.PersonalFragment;
import com.xu.walker.ui.fragment.roadbook.RoadBookFragment;
import com.xu.walker.ui.fragment.sport.SportFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

//
//                                  _oo8oo_
//                                 o8888888o
//                                 88" . "88
//                                 (| -_- |)
//                                 0\  =  /0
//                               ___/'==='\___
//                             .' \\|     |// '.
//                            / \\|||  :  |||// \
//                           / _||||| -:- |||||_ \
//                          |   | \\\  -  /// |   |
//                          | \_|  ''\---/''  |_/ |
//                          \  .-\__  '-'  __/-.  /
//                        ___'. .'  /--.--\  '. .'___
//                     ."" '<  '.___\_<|>_/___.'  >' "".
//                    | | :  `- \`.:`\ _ /`:.`/ -`  : | |
//                    \  \ `-.   \_ __\ /__ _/   .-` /  /
//                =====`-.____`.___ \_____/ ___.`____.-`=====
//                                  `=---=`
//
//
//               ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//                          佛祖保佑         永无bug
//
public class MainActivity extends BaseActivity<MainContract.IMainPresenter> implements MainContract.IMainView{

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.rb_sport)
    RadioButton rbSport;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private ClubFragment mClubFragment;
    private DiscoverFragment mDiscoverFragment;
    private PersonalFragment mPersonalFragment;
    private RoadBookFragment mRoadBookFragment;
    private SportFragment mSportFragment;

    private static final int CLUB_FRAGMENT = 0;
    private static final int DISCOVER_FRAGMENT = 1;
    private static final int PERSONAL_FRAGMENT = 2;
    private static final int ROAD_BOOK_FRAGMENT = 3;
    private static final int SPORT_FRAGMENT = 4;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MainPresenter();
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initOthers() {
        mFragmentManager = this.getSupportFragmentManager();
        showFragment(SPORT_FRAGMENT);
        onClickListner();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void useNightMode(boolean isNight) {

    }

    private void showFragment(int num) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        // 显示之前先调用隐藏的方法避免显示重叠的情况
        hideFragment(mFragmentTransaction);
        switch (num) {
            case SPORT_FRAGMENT:
                if (mSportFragment != null) {
                    mFragmentTransaction.show(mSportFragment);
                } else {
                    mSportFragment = new SportFragment();
                    mFragmentTransaction.add(R.id.main_frameLayout, mSportFragment);
                }
                break;
            case PERSONAL_FRAGMENT:
                if (mPersonalFragment != null) {
                    mFragmentTransaction.show(mPersonalFragment);
                } else {
                    mPersonalFragment = new PersonalFragment();
                    mFragmentTransaction.add(R.id.main_frameLayout, mPersonalFragment);
                }
                break;
            case DISCOVER_FRAGMENT:
                if (mDiscoverFragment != null) {
                    mFragmentTransaction.show(mDiscoverFragment);
                } else {
                    mDiscoverFragment = new DiscoverFragment();
                    mFragmentTransaction.add(R.id.main_frameLayout, mDiscoverFragment);
                }
                break;
            case ROAD_BOOK_FRAGMENT:
                if (mRoadBookFragment != null) {
                    mFragmentTransaction.show(mRoadBookFragment);
                } else {
                    mRoadBookFragment = new RoadBookFragment();
                    mFragmentTransaction.add(R.id.main_frameLayout, mRoadBookFragment);
                }
                break;
            case CLUB_FRAGMENT:
                if (mClubFragment != null) {
                    mFragmentTransaction.show(mClubFragment);
                } else {
                    mClubFragment = new ClubFragment();
                    mFragmentTransaction.add(R.id.main_frameLayout, mClubFragment);
                }
                break;
        }
        mFragmentTransaction.commit();
    }

    private void onClickListner() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_sport:
                        showFragment(SPORT_FRAGMENT);
                        break;
                    case R.id.rb_personal:
                        showFragment(PERSONAL_FRAGMENT);
                        break;
                    case R.id.rb_discover:
                        showFragment(DISCOVER_FRAGMENT);
                        break;
                    case R.id.rb_roadBook:
                        showFragment(ROAD_BOOK_FRAGMENT);
                        break;
                    case R.id.rb_club:
                        showFragment(CLUB_FRAGMENT);
                        break;
                }
            }
        });
    }

    private void hideFragment(FragmentTransaction mFragmentTransaction) {
        if (mSportFragment != null) {
            mFragmentTransaction.hide(mSportFragment);
        }
        if (mPersonalFragment != null) {
            mFragmentTransaction.hide(mPersonalFragment);
        }
        if (mDiscoverFragment != null) {
            mFragmentTransaction.hide(mDiscoverFragment);
        }
        if (mRoadBookFragment != null) {
            mFragmentTransaction.hide(mRoadBookFragment);
        }
        if (mClubFragment != null) {
            mFragmentTransaction.hide(mClubFragment);
        }

    }

    //根据运动的选择，设置导航栏的图片
    public void setNavigationImg(int sportType) {
        switch (sportType) {
            case SportFragment.SPORT_TYPE_BIKE:
                rbSport.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.main_tab_bg_sport_bike), null, null);
                break;
            case SportFragment.SPORT_TYPE_RUN:
                rbSport.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.main_tab_bg_sport_run), null, null);
                break;
            case SportFragment.SPORT_TYPE_FOOTER:
                rbSport.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.main_tab_bg_sport_footer), null, null);
                break;
            case SportFragment.SPORT_TYPE_SKIING:
                rbSport.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.main_tab_bg_sport_skiing), null, null);
                break;
            case SportFragment.SPORT_TYPE_SWIMMING:
                rbSport.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.main_tab_bg_sport_swimming), null, null);
                break;
            case SportFragment.SPORT_TYPE_INDOOR:
                rbSport.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.main_tab_bg_sport_indoor), null, null);
                break;
            case SportFragment.SPORT_TYPE_FREE:
                rbSport.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.main_tab_bg_sport_free), null, null);
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //AlertDialog的选用
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定要退出吗?");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //finish();
                    System.exit(0);//彻底退出应用
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
        }
        return true;
    }

}
