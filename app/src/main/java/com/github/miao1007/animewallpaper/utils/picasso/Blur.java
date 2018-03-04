package com.github.miao1007.animewallpaper.utils.picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

// Code borrowed from Nicolas Pomepuy
// https://github.com/PomepuyN/BlurEffectForAndroidDesign
public class Blur {

    public static Bitmap apply(Context context, Bitmap sentBitmap) {
        return apply(context, sentBitmap, 10);
    }

    @SuppressLint("NewApi")
    public static Bitmap apply(Context context, Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        Canvas canvas= new Canvas(bitmap);
        canvas.drawColor(Color.argb(0x3f,0x00,0x00,0x00));
        return bitmap;

    }

}
