package com.bupt.monitorpositionsys.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bupt.model.utils.SPUtil;
import com.bupt.model.utils.assist.Toastor;
import com.bupt.monitorpositionsys.R;

public class SkipActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);

        initUI();
    }

    private void initUI() {
        LinearLayout ll_db = (LinearLayout) findViewById(R.id.ll_skip_db);
        ll_db.setOnClickListener(this);
        LinearLayout ll_record = (LinearLayout) findViewById(R.id.ll_skip_record);
        ll_record.setOnClickListener(this);
        LinearLayout ll_phone = (LinearLayout) findViewById(R.id.ll_skip_setting);
        ll_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_skip_db:
                startActivity(new Intent(SkipActivity.this,MainActivity.class));
                break;
            case R.id.ll_skip_record:
                startActivity(new Intent(SkipActivity.this,RecordActivity.class));
                break;
            case R.id.ll_skip_setting:
                // 设置报警电话
                showDialog_Layout(SkipActivity.this);
                break;
        }
    }

    //显示基于Layout的AlertDialog
    private void showDialog_Layout(Context context) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View textEntryView = inflater.inflate(
                R.layout.dialog_phone_setting, null);
        final EditText edtInput=(EditText)textEntryView.findViewById(R.id.edt_phone);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("设置报警电话，可一键报警和来电自动接听");
        builder.setView(textEntryView);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        SPUtil.saveString(getApplicationContext(),"phone",edtInput.getText().toString());
                        new Toastor(getApplicationContext()).showToast("设置成功");
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
