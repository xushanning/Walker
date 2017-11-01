package com.xu.walker.ui.activity.login;

import android.widget.TextView;

import com.xu.walker.base.IBasePresenter;
import com.xu.walker.base.IBaseView;

import org.w3c.dom.Text;

/**
 * Created by xusn10 on 2017/8/11.
 *
 * @author xu
 */

public class LoginContract {
    interface ILoginView extends IBaseView {
        /**
         * 获取loginText
         *
         * @return textView
         */
        TextView getUserNameText();

        /**
         * 获取密码的text
         *
         * @return textView
         */
        TextView getPassWordText();

        /**
         * 获取登陆text
         *
         * @return 登陆text
         */
        TextView getLoginText();
    }

    interface ILoginPresenter extends IBasePresenter<ILoginView> {
        /**
         * 执行登陆
         */
        void doLogin();
    }
}
