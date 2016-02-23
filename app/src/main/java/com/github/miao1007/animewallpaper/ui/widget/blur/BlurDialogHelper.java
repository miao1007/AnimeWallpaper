//package com.github.miao1007.animewallpaper.ui.widget.blur;
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import com.github.miao1007.animewallpaper.R;
//import com.github.miao1007.animewallpaper.utils.StatusBarUtils;
//
///**
// * Created by leon on 2/23/16.
// */
//public class BlurDialogHelper {
//
//  Dialog dialog;
//
//  public Dialog create(Dialog dialog, BlurDrawable drawable) {
//
//    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//      @Override public void onCancel(DialogInterface dialog) {
//        drawable.setCornerRadius(0);
//        drawable.setDrawOffset(0, 0);
//      }
//    });
//    dialog.getWindow().getDecorView().post(new Runnable() {
//      @Override public void run() {
//        drawable.setCornerRadius(getResources().getDimension(R.dimen.internal_searchbar_radius));
//        drawable.setDrawOffset(
//            (getWindow().getDecorView().getWidth() - dialog.getWindow().getDecorView().getWidth())
//                / 2,
//            (getWindow().getDecorView().getHeight() - dialog.getWindow().getDecorView().getHeight()
//                + StatusBarUtils.getStatusBarOffsetPx(getApplicationContext())) / 2);
//        dialog.getWindow().setBackgroundDrawable(drawable);
//      }
//    });
//    return dialog;
//  }
//}
