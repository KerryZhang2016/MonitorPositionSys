package com.bupt.monitorpositionsys.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.bupt.model.utils.AppManager;
import com.bupt.model.utils.data.DataKeeper;

/**
 * Created by Kerry on 15/11/26.
 *
 * 基础Activity
 */
public class BaseActivity extends FragmentActivity{
    // 上下文实例
    public Context context;
    // 应用全局的实例
    public MyApplication application;
    // 全局的SharePreference实例
    public DataKeeper dataKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);

        context = getApplicationContext();
        application = (MyApplication) this.getApplication();
        dataKeeper = new DataKeeper(getApplicationContext(), DataKeeper.KEY_PK_HOME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }
}
