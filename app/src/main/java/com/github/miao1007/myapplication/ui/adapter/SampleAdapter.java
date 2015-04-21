package com.github.miao1007.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.ui.activity.DetailedActivity;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by leon on 11/12/14.
 */
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.MyViewHolder> {

  private List<ImageResult> imageResults;

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.iv_card_preview) ImageView imageView;
    @InjectView(R.id.tv_card_author) TextView textView_author;
    @InjectView(R.id.tv_card_tags) TextView textView_tag;

    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }

  public SampleAdapter(List<ImageResult> imageResults) {
    this.imageResults = imageResults;
  }

  @Override public MyViewHolder onCreateViewHolder(ViewGroup viewGroup,  int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.list_item_sample, viewGroup, false);
    return new MyViewHolder(view);
  }

  @Override public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
    final Context context = myViewHolder.itemView.getContext();
    final String url = imageResults.get(i).getPreviewUrl();
    Drawable drawable = myViewHolder.imageView.getDrawable();
    Picasso.with(context).load(url)
        //.placeholder(R.drawable.lorempixel)
        .into(myViewHolder.imageView);
    myViewHolder.textView_author.setText(imageResults.get(i).getAuthor());
    myViewHolder.textView_tag.setText(imageResults.get(i).getTags());
    myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(context, DetailedActivity.class);
        intent.putExtra(DetailedActivity.EXTRA_IMAGE, imageResults.get(i));
        context.startActivity(intent);
      }
    });
  }


  @Override public int getItemCount() {
    return imageResults.size();
  }
}
