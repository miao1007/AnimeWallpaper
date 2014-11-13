package com.github.miao1007.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by leon on 11/12/14.
 */
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.MyViewHolder> {

    private Context context;

    public static class   MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            textView = (TextView)itemView.findViewById(R.id.textView_title);
        }
    }

    public SampleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sample_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Picasso.with(context)
                .load("http://i.imgur.com/DvpvklR.png")
                .into(myViewHolder.imageView);
        myViewHolder.textView.setText("position"  + i);
    }

    @Override
    public int getItemCount() {
        return 20;
    }
}
