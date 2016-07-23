package com.bupt.model.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kerry on 15/11/3.
 *
 * 图片处理工具
 */
public class BitmapUtil {

    /**
     * Bitmap转换成byte[]
     * */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[]转换成Bitmap
     * */
    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     *  以最省内存的方式读取本地资源的图片
     */
    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new  BitmapFactory.Options();
        opt.inPreferredConfig =  Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //  获取资源图片
        InputStream is =  context.getResources().openRawResource(resId);
        return  BitmapFactory.decodeStream(is, null, opt);
    }

    //---------------------------用户头像处理---------------------------------

    /**
     * 保存用户头像到SD卡的MTT文件下
     * */
    public static void compressUsericonToFile(Bitmap bmp,String picName){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        String path= Environment.getExternalStorageDirectory().getPath()+"/MTT/"+picName;
        File mFile=new File(path);
        if(mFile.exists()){
            // 删除之前的头像
            mFile.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(createMTTFile(picName));
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取用户头像
     * */
    public static Bitmap readUsericon(String filename){
        String path = Environment.getExternalStorageDirectory().getPath() + "/MTT/" + filename;
        File mFile = new File(path);
        if(mFile.exists()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFile(path,options);
        }else {
            return null;
        }
    }

    /**
     * 清除用户头像
     * */
    public static void deleteUsericon(){
        String path = Environment.getExternalStorageDirectory().getPath() + "/MTT/usericon.jpg";
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }

    /**
     * 在SD卡下创建MTT文件夹
     * */
    private static File createMTTFile(String picName){
        //如果想要指定路径，则可以这样写
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/MTT");
        if(!file.exists()){
            file.mkdir();
        }
        File f = new File(Environment.getExternalStorageDirectory().getPath()+"/MTT/"+picName);
        if (f.exists()) {
            f.delete();
        }else {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }
}
