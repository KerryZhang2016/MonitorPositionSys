package com.bupt.model.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtil
{
    // 用户token
	public static final String USER_TOKEN = "token";
    // 码表设备地址
    public static final String ADDRESS_MABIAO = "ADDRESS_MABIAO";
    // 踏频设备地址
    public static final String ADDRESS_TAPIN = "ADDRESS_TAPIN";
    // 按键设备地址
    public static final String ADDRESS_THUMB = "ADDRESS_THUMB";

    // 用户当前队伍ID
    public static final String COVERSATION_ID = "conversationId";

    // 当前用户最高分：username_maxscore
	public static final String MAX_SCORE = "maxScore";
	// 骑行模式：标准模式、省电模式
	public static final String CYCLING_MODE = "cyclingMode";

	// 时间轴自动下载数据时间记录
    public static final String SYN_TIME = "synTime";
    // 是否有新的记录
    public static final String NEW_RECORD = "newRecord";
    // 骑行里程
    public static final String CYCLING_TOTALMILE = "totalMile";
    // 骑行时长
    public static final String CYCLING_TOTALTIME = "totalTime";
    // 骑行次数
    public static final String CYCLING_RECORDNUM = "recordNum";
    // 当前记录状态
    public static final String RECORD_STATE = "recordState";

    // 用户当前金币数
    public static final String COIN = "coin";
    // 用户最后一次金币改变
    public static final String COIN_CHANGE = "coinChange";
    // 用户当前钻石数
    public static final String DIAMOND = "diamond";
    // 用户最后一次钻石改变
    public static final String DIAMOND_CHANGE = "diamondChange";

    // 用户say hi次数
    public static final String HI_NUM = "hiNum";

	// 商城的状态
    public static final String SHOP_STATUS = "shop_status";
    // 商城的获取日期
    public static final String SHOP_DATE = "shop_date";
    // 商城的版本号
    public static final String SHOP_VERSION = "shop_version";

    /**
	 * 保存在手机里面的文件名
	 */
	public static final String FILE_NAME = "mtt";

	/** 
	 * 保存字符串数据
	 * */
	public static void save(Context context, String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences("mtt", Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.apply();
	}
	
	/** 
	 * 获取字符串数据
	 * */
	public static String getValue(Context context, String key, String defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("mtt", Context.MODE_PRIVATE);

		return sharedPreferences.getString(key, defaultValue);
	}
	
	/** 
	 * 保存int数据
	 * */
	public static void saveInt(Context context, String key, int value) {
		SharedPreferences.Editor editor = context.getSharedPreferences("mtt", Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.apply();
	}
	
	/** 
	 * 获取int数据
	 * */
	public static int getIntValue(Context context, String key, int defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("mtt", Context.MODE_PRIVATE);

		return sharedPreferences.getInt(key, defaultValue);
	}
	
	/** 
	 * 保存float数据
	 * */
	public static void saveFloat(Context context, String key, float value) {
		SharedPreferences.Editor editor = context.getSharedPreferences("mtt", Context.MODE_PRIVATE).edit();
		editor.putFloat(key, value);
		editor.apply();
	}
	
	/** 
	 * 获取float数据
	 * */
	public static float getFloatValue(Context context, String key, float defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("mtt", Context.MODE_PRIVATE);

		return sharedPreferences.getFloat(key, defaultValue);
	}
	
	/** 
	 * 保存boolean数据
	 * */
	public static void saveBoolean(Context context, String key, boolean value) {
		SharedPreferences.Editor editor = context.getSharedPreferences("mtt", Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.apply();
	}
	
	/** 
	 * 获取boolean数据
	 * */
	public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("mtt", Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public static String getStringValue(Context context, String key, String defaultValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences("mtt", Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, defaultValue);
	}

	public static void saveString(Context context, String key, String value){
		SharedPreferences.Editor editor = context.getSharedPreferences("mtt", Context.MODE_PRIVATE).edit();
		editor.putString(key,value);
		editor.apply();
	}

	/**
	 * 保存long数据
	 * */
	public static void saveLong(Context context, String key, long value) {
		SharedPreferences.Editor editor = context.getSharedPreferences("mtt", Context.MODE_PRIVATE).edit();
		editor.putLong(key, value);
		editor.apply();
	}

	/**
	 * 获取long数据
	 * */
	public static long getLongValue(Context context, String key, long defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("mtt", Context.MODE_PRIVATE);

		return sharedPreferences.getLong(key, defaultValue);
	}

	/**
	 * 移除某个key值已经对应的值
	 */
	public static void remove(Context context, String key)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 */
	public static void clear(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 */
	public static boolean contains(Context context, String key)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 */
	public static Map<String, ?> getAll(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 * 
	 */
	private static class SharedPreferencesCompat
	{
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod()
		{
			try
			{
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException ignored)
			{
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 */
		public static void apply(SharedPreferences.Editor editor)
		{
			try
			{
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ignored) {
                ignored.printStackTrace();
			}
            editor.commit();
		}
	}

    /**
     * 用户注销时清空信息
     * */
    public static void clearUserDatas(Context context){
        SPUtil.save(context, ADDRESS_MABIAO, null);
        SPUtil.save(context, ADDRESS_TAPIN, null);
        SPUtil.save(context, ADDRESS_THUMB, null);

        // 记录已更新
        SPUtil.saveBoolean(context,NEW_RECORD, true);
        // 清除所在队伍ID
        SPUtil.save(context,COVERSATION_ID, null);

        // 清除用户的token
        SPUtil.save(context,USER_TOKEN, "");
        // 清除用户的最高分记录
        SPUtil.saveInt(context,MAX_SCORE,0);
        // 清除用户的金币和钻石记录
        SPUtil.saveInt(context,COIN,0);
        SPUtil.saveInt(context,COIN_CHANGE,0);
        SPUtil.saveInt(context,DIAMOND,0);
        SPUtil.saveInt(context,DIAMOND_CHANGE,0);
        // 清除用户say hi次数
        SPUtil.saveInt(context,HI_NUM,0);
    }

}
