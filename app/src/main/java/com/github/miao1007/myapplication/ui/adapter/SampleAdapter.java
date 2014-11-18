package com.github.miao1007.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.internal.widget.TintImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.miao1007.myapplication.DetailedActivity;
import com.github.miao1007.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by leon on 11/12/14.
 */
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.MyViewHolder> {

    private Context context;
    private List<String> urls;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TintImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = (TintImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView_title);
        }
    }

    public SampleAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sample, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final String url = urls.get(i);
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.lorempixel)
                .into(myViewHolder.imageView);
        myViewHolder.textView.setText("position" + i);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("URL", url);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }


}
