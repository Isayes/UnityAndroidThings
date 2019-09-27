package io.github.isayes.okdroid.base;

import android.app.Application;

import java.util.ArrayList;

/**
 * Author: HF
 * Date:   2016-05-12
 * Description:
 */

public class CustomApplication extends Application {

    public static ArrayList<String> mTestNullPointer;
    // 我们把 -1 模拟表示被强杀
    public static int mAppStatus = -1;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
