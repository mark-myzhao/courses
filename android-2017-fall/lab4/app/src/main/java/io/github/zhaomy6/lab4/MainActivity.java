package io.github.zhaomy6.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.main_layout);

        Button b1 = (Button) findViewById(R.id.main_button1);
        Button b2 = (Button) findViewById(R.id.main_button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toStaticAct = new Intent(MainActivity.this, StaticActivity.class);
                startActivity(toStaticAct);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDynamicAct = new Intent(MainActivity.this, DynamicActivity.class);
                startActivity(toDynamicAct);
            }
        });
    }
}
