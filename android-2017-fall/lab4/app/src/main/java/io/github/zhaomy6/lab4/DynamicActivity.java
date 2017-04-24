package io.github.zhaomy6.lab4;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DynamicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle curState) {
        //  Hide Action Bar
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        super.onCreate(curState);
        setContentView(R.layout.dynamic_layout);

        final DynamicReceiver dr = new DynamicReceiver();
        final EditText et = (EditText) findViewById(R.id.dynamic_text);
        final Button btn1 = (Button) findViewById(R.id.dynamic_button1);  //  register
        final Button btn2 = (Button) findViewById(R.id.dynamic_button2);  //  unregister
        btn1.setTag(0);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) btn1.getTag() == 0) {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("DynamicFilter");
                    registerReceiver(dr, intentFilter);
                    btn1.setTag(1);
                    btn1.setText("动态注销");
                } else if ((int) btn1.getTag() == 1) {
                    unregisterReceiver(dr);
                    btn1.setTag(0);
                    btn1.setText("动态注册");
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("DynamicFilter");
                Bundle bundle = new Bundle();
                bundle.putString("str", et.getText().toString());
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        });
    }
}
