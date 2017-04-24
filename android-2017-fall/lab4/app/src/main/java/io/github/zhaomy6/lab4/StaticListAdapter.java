package io.github.zhaomy6.lab4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class StaticListAdapter extends BaseAdapter {
    private LinkedList<StaticListItem> list;
    private Context context;

    public StaticListAdapter(Context c, LinkedList<StaticListItem> l) {
        this.list = l;
        this.context = c;
    }

    public LinkedList<StaticListItem> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.static_list_item, null);
        }

        ImageView iv = (ImageView) convertView.findViewById(R.id.list_item_image);
        TextView tv = (TextView) convertView.findViewById(R.id.list_item_text);

        iv.setImageResource(this.list.get(position).getImgResId());
        tv.setText(this.list.get(position).getContentText());

        return convertView;
    }
}
