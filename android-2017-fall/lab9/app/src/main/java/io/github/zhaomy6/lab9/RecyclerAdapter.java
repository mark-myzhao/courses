package io.github.zhaomy6.lab9;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private LinkedList<String[]> list;
    private LayoutInflater mInflater;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, String[] item);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public RecyclerAdapter(Context context, LinkedList<String[]> items) {
        super();
        this.list = items;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.forecast_item_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.Date = (TextView)view.findViewById(R.id.f_date);
        holder.Weather_description =(TextView)view.findViewById(R.id.f_weather);
        holder.Temperature = (TextView)view.findViewById(R.id.f_min_max_tem);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.Date.setText(this.list.get(i)[0]);
        viewHolder.Weather_description.setText(this.list.get(i)[3]);
        String min = this.list.get(i)[1].split(" ")[1];
        String max = this.list.get(i)[2].split(" ")[1];
        viewHolder.Temperature.setText(" " + min + "/" + max + " ");
        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView, i, list.get(i));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else
            return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Date;
        TextView Weather_description;
        TextView Temperature;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
