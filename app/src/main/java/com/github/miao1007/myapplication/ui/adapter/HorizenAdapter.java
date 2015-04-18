package com.github.miao1007.myapplication.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by leon on 11/24/14.
 */
public class HorizenAdapter extends RecyclerView.Adapter<HorizenAdapter.MyViewHolder> {

    List<String> strings;
    onTabClickListener listener;

    public void setOnTabClickListener(onTabClickListener listener) {
        this.listener = listener;
    }

    public HorizenAdapter(List<String> strings) {
        this.strings = strings;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(android.R.id.title);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.browser_link_context_header,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textView.setText(strings.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTabClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public interface onTabClickListener{
        public void onTabClick(int position);
    }
}
