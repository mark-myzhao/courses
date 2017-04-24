package io.github.zhaomy6.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;

public class StaticActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        //  Hide Action Bar
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        super.onCreate(saveInstanceState);
        setContentView(R.layout.static_layout);

        LinkedList<StaticListItem> l = new LinkedList<>();
        int[] imgResources = new int[]{
                R.mipmap.apple, R.mipmap.banana, R.mipmap.cherry,
                R.mipmap.coco, R.mipmap.kiwi, R.mipmap.orange,
                R.mipmap.pear, R.mipmap.strawberry, R.mipmap.watermelon
        };
        String[] imgNames = new String[]{
                "Apple", "Banana", "Cherry",
                "Coco", "Kiwi", "Orange",
                "Pear", "Strawberry", "Watermelon"
        };
        for (int i = 0; i < 9; ++i) {
            l.add(new StaticListItem(imgResources[i], imgNames[i]));
        }
        StaticListAdapter sa = new StaticListAdapter(this, l);
        ListView lv = (ListView) findViewById(R.id.static_list_view);
        lv.setAdapter(sa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("StaticFilter");
                StaticListAdapter a = (StaticListAdapter) parent.getAdapter();
                String name = a.getList().get(position).getContentText();
                int imgId = a.getList().get(position).getImgResId();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putInt("imgId", imgId);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        });
    }
}