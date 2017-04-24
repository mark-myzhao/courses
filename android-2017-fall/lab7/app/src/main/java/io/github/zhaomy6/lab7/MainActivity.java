package io.github.zhaomy6.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sp = getSharedPreferences("Lab7Password", MODE_PRIVATE);
        Log.d("MainActivity", sp == null ? "0" : "1");
        boolean registered = sp.getBoolean("registered", false);
        if (registered) {
            //  之前已经注册过，只显示一个密码输入框
            EditText et = (EditText) findViewById(R.id.password);
            et.setHint("Password");
            EditText confirm = (EditText) findViewById(R.id.confirm_password);
            confirm.setVisibility(View.INVISIBLE);
        }

        Button okBtn = (Button) findViewById(R.id.ok);
        Button clearBtn = (Button) findViewById(R.id.clear);
        okBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean registered = false;
        EditText pw1 = (EditText) findViewById(R.id.password);
        EditText pw2 = (EditText) findViewById(R.id.confirm_password);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        if (sp != null) {
            registered = sp.getBoolean("registered", false);
        }
        switch (v.getId()) {
            case R.id.ok:
                if (!registered) {
                    //  未注册，进行密码设置
                    if (pw1.getText().toString().length() * pw2.getText().toString().length() == 0) {
                        Toast.makeText(this, "Password cannot be Empty", Toast.LENGTH_SHORT).show();
                    } else if (pw1.getText().toString().equals(pw2.getText().toString())) {
                        if (sp != null) {
                            Log.d("MainActivity", "sp != null");
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("registered", true);
                            editor.putString("Password", pw1.getText().toString());
                            editor.apply();
                            //  跳转界面
                            startActivity(intent);
                        }
                    } else {
                        //  弹出错误信息提示
                        Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //  已注册，检测密码是否正确
                    if (sp.getString("Password", "").equals(pw1.getText().toString())) {
                        //  密码正确
                        //  跳转界面
                        startActivity(intent);
                    } else {
                        //  密码错误，弹出提示信息
                        Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.clear:
                pw1.setText("");
                pw2.setText("");
                break;
        }
    }
}
