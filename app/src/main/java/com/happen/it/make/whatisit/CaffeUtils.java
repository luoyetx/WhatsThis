package com.happen.it.make.whatisit;

import android.graphics.Bitmap;

import com.luoyetx.minicaffe.Blob;
import com.luoyetx.minicaffe.Net;


import java.nio.ByteBuffer;

/**
 * Created by leliana on 11/6/15.
 */
public class CaffeUtils {
    private static boolean libLoaded = false;
    private CaffeUtils() {}

    public static String identifyImage(final Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byte[] bytes = byteBuffer.array();
        float[] colors = new float[bytes.length / 4 * 3];

        float mean_b = 0;
        float mean_g = 0;
        float mean_r = 0;
        for (int i = 0; i < bytes.length; i += 4) {
            int j = i / 4;
            // caffe use bgr
            colors[0 * 224 * 224 + j] = (float)(((int)(bytes[i + 2])) & 0xFF) - mean_r;
            colors[1 * 224 * 224 + j] = (float)(((int)(bytes[i + 1])) & 0xFF) - mean_g;
            colors[2 * 224 * 224 + j] = (float)(((int)(bytes[i + 0])) & 0xFF) - mean_b;
        }

        Net net = WhatsApplication.getCaffeNet();
        Blob data = net.getBlob("data");
        data.num = 1;
        data.height = 224;
        data.width = 224;
        data.channels = 3;
        data.data = colors;
        data.syncToC();
        net.forward();
        Blob output = net.getBlob("prob");
        final float[] result = output.data;

        int index = 0;
        for (int i = 0; i < result.length; ++i) {
            if (result[index] < result[i]) index = i;
        }
        String tag = WhatsApplication.getName(index);
        String [] arr = tag.split(" ", 2);
        return arr[1];
    }
}
