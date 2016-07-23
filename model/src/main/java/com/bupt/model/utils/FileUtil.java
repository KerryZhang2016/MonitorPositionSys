package com.bupt.model.utils;

import android.os.Environment;

/**
 * Created by Kerry on 15/11/13.
 *
 * 文件管理工具类
 */
public class FileUtil {

    /**
     * 判断sd卡是否可用
     * */
    public static boolean hasSdcard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
