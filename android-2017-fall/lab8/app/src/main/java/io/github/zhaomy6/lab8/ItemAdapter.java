package io.github.zhaomy6.lab8;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

public class ItemAdapter extends BaseAdapter {
    private LinkedList<Item> list;
    private Context context;

    public ItemAdapter(LinkedList<Item> l, Context c) {
        this.list = l;
        this.context = c;
    }

    @Override
    public int getCount() {
        if (this.list == null) return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) return null;
        else return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_layout, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.ln);
        TextView birthday = (TextView) convertView.findViewById(R.id.lb);
        TextView gift = (TextView) convertView.findViewById(R.id.lg);

        Item item = this.list.get(position);
        name.setText(item.getName());
//        Log.d("Item", item.getName());
        birthday.setText(item.getBirthday());
        gift.setText(item.getGift());

        return convertView;
    }

    public LinkedList<Item> getList() {
        return this.list;
    }
}
