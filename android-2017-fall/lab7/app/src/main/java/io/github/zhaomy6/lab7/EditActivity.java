package io.github.zhaomy6.lab7;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class EditActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        Button save = (Button) findViewById(R.id.save_btn);
        Button load = (Button) findViewById(R.id.load_btn);
        Button clear = (Button) findViewById(R.id.clear_btn);
        save.setOnClickListener(this);
        load.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText text = (EditText) findViewById(R.id.content_text);
        switch (v.getId()) {
            case R.id.save_btn:
                try (FileOutputStream fos = openFileOutput("Lab7", MODE_PRIVATE)) {
                    fos.write(text.getText().toString().getBytes());
                    Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
                    fos.close();
                } catch (Exception e) {
                    Log.d("EditActivity", "IO Exception");
                    e.printStackTrace();
                }
                break;
            case R.id.load_btn:
                try (FileInputStream fis = openFileInput("Lab7")) {
                    byte[] content = new byte[fis.available()];
                    fis.read(content);
                    text.setText(new String(content));
                    Toast.makeText(this, "Load Successfully", Toast.LENGTH_SHORT).show();
                    fis.close();
                } catch (Exception e) {
                    Log.d("EditActivity", "IO Exception");
                    Toast.makeText(this, "Fail to Load the File", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.clear_btn:
                text.setText("");
                break;
        }
    }
}
