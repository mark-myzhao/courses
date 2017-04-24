package io.github.zhaomy6.lab9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

public class MyAdapter extends BaseAdapter {
    private Context context = null;
    //  list[0] = suggestion; list[1] = detail
    private LinkedList<String[]> list = null;

    public MyAdapter(Context c, LinkedList<String[]> info) {
        this.context = c;
        this.list = info;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        else
            return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null)
            return null;
        else
            return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, null);
        }
        TextView suggestView = (TextView) view.findViewById(R.id.item_suggestion);
        TextView detailView = (TextView) view.findViewById(R.id.item_detail);
        suggestView.setText(this.list.get(position)[0]);
        detailView.setText(this.list.get(position)[2]);
        return view;
    }
}
