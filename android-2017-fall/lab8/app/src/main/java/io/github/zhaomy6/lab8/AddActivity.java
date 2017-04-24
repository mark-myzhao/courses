package io.github.zhaomy6.lab8;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;

public class AddActivity extends Activity
        implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);

        Button btn = (Button) findViewById(R.id.add_layout_btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
//        intent.putExtras()
        EditText et1 = (EditText) findViewById(R.id.add_row1_a);
        EditText et2 = (EditText) findViewById(R.id.add_row2_a);
        EditText et3 = (EditText) findViewById(R.id.add_row3_a);
        String name = et1.getText().toString();
        String birth = et2.getText().toString();
        String gift = et3.getText().toString();
        boolean empty = name.length() * birth.length() * gift.length() == 0;
        boolean duplicated = isDuplicated(name);
        if (!duplicated && !empty) {
            intent.putExtra("name", name);
            intent.putExtra("birth", birth);
            intent.putExtra("gift", gift);
            setResult(0, intent);
            finish();
        } else if (duplicated) {
            Toast.makeText(this, R.string.duplicated_name, Toast.LENGTH_SHORT).show();
        } else if (empty) {
            Toast.makeText(this, R.string.empty_input, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isDuplicated(String name) {
        DBHelper dbHelper = new DBHelper(this, "Lab8DB", null, 1);
        String[] names = {name};
        LinkedList<Item> list = dbHelper.queryDBItem(names);
        return !list.isEmpty();
    }
}
