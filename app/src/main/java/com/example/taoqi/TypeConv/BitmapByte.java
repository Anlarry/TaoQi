package com.example.taoqi.TypeConv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class BitmapByte {
    public static byte[] toByte(Bitmap bitmap ) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        try {
            baos.flush();
            baos.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Bitmap tmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
        return baos.toByteArray();
    }
}
