package com.bupt.monitorpositionsys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.bupt.model.utils.HandlerUtil;
import com.bupt.monitorpositionsys.R;

public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        HandlerUtil.runOnUiThreadDelay(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Welcome.this,SkipActivity.class ));
                Welcome.this.finish();
            }
        },1500);
    }

}
