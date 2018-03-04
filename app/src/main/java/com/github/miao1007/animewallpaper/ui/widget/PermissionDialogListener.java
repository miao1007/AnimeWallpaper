package com.github.miao1007.animewallpaper.ui.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.karumi.dexter.listener.single.BasePermissionListener;

/**
 * Created by leon on 2018/3/4.
 */

public class PermissionDialogListener extends BaseMultiplePermissionsListener {

  Dialog dialog;

  public PermissionDialogListener(Dialog dialog) {
    this.dialog = dialog;
  }

  @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
    super.onPermissionsChecked(report);
    if (!report.areAllPermissionsGranted()) {
      showDialog();
    }
  }

  private void showDialog() {
    dialog.show();
  }
}
