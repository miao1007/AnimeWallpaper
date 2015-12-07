package com.github.miao1007.myapplication.ui.frag;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.support.service.Query;
import com.github.miao1007.myapplication.support.service.konachan.ImageResult;
import com.github.miao1007.myapplication.utils.picasso.BlurTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagePreviewCard extends Fragment {

  public static final String EXTRA_IMAGE = "URL";
  public static final String TAG = ImagePreviewCard.class.getSimpleName();
  @InjectView(R.id.container) LinearLayout mLinearLayout;
  @InjectView(R.id.iv_detailed_card) ImageView mImageview;
  int defalutColor;
  Query query;
  PhotoViewAttacher attacher;
  OnImageLoadedListener mCallback;
  private ImageResult imageResult;

  public ImagePreviewCard() {
  }

  public static ImagePreviewCard newInstance(ImageResult imageResult) {
    ImagePreviewCard card = new ImagePreviewCard();
    Bundle bundle = new Bundle();
    bundle.putParcelable(EXTRA_IMAGE, imageResult);
    card.setArguments(bundle);
    return card;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    defalutColor = getResources().getColor(R.color.accent_material_light);
    imageResult = getArguments().getParcelable(EXTRA_IMAGE);
    //https://konachan.com/post.json/?limit=10&tags=rating:s%20id:199240&page=1

  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_image_detailed_card, container, false);
    ButterKnife.inject(this, view);

    return view;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Picasso.with(getActivity())
        .load(imageResult.getJpegUrl())
        .placeholder(R.drawable.lorempixel)
        //.transform(new BlurTransformation(getContext()))
        .into(mImageview);
  }

  @Override public void onDetach() {
    super.onDetach();
    ButterKnife.reset(this);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
    try {
      mCallback = (OnImageLoadedListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement OnHeadlineSelectedListener");
    }
  }

  // Container Activity must implement this interface
  public interface OnImageLoadedListener {
    void onArticleSelected(int color);
  }
}
