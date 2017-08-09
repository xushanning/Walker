package com.xu.walker.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xusn10 on 2017/1/9.
 */
public class ToastUtil {
    public static void toastLong(Context context, String strText) {

        Toast.makeText(context, strText, Toast.LENGTH_LONG).show();

    }

    public static void toastShort(Context context, String strText) {

        Toast.makeText(context, strText, Toast.LENGTH_SHORT).show();

    }
}
