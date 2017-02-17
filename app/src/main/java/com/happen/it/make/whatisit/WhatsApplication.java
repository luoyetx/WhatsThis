package com.happen.it.make.whatisit;

import android.app.Application;
import android.content.Context;

import com.luoyetx.minicaffe.Net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leliana on 8/5/15.
 */
public class WhatsApplication extends Application{
    private static Net net;
    public static Net getCaffeNet() {return net;}
    private static List<String> dict;
    public static String getName(int i) {
        if (i >= dict.size()) {
            return "Shit";
        }
        return dict.get(i);
    }
    @Override
    public void onCreate() {
        super.onCreate();

        final byte[] netprototxt = readRawFile(this, R.raw.resnet10net);
        final byte[] netcaffemodel = readRawFile(this, R.raw.resnet10model);

        net = new Net(netprototxt, netcaffemodel);
        dict = readRawTextFile(this, R.raw.synset);
    }

    public static byte[] readRawFile(Context ctx, int resId)
    {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        int size = 0;
        byte[] buffer = new byte[1024];
        try (InputStream ins = ctx.getResources().openRawResource(resId)) {
            while((size=ins.read(buffer,0,1024))>=0){
                outputStream.write(buffer,0,size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public static List<String> readRawTextFile(Context ctx, int resId)
    {
        List<String> result = new ArrayList<>();
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        try {
            while (( line = buffreader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            return null;
        }
        return result;
    }
}
