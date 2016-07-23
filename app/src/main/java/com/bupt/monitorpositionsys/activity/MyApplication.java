package com.bupt.monitorpositionsys.activity;

import org.litepal.LitePalApplication;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyApplication extends LitePalApplication{
	private String serialDev="";
	private int serialBrt =0;
	private int serialFlg =0;
	
	private SerialPort mSerialPort = null;

	public SerialPort getSerialPort() throws SecurityException, IOException,
			InvalidParameterException {
		if (mSerialPort == null) {
			/* Open the serial port */
			//mSerialPort = new SerialPort(new File("/dev/ttyUSB4"), 9600, 0);
			mSerialPort = new SerialPort(new File(serialDev), serialBrt, serialFlg);
		}
		return mSerialPort;
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
	
	public void setDev(String tty){
		serialDev = tty;
	}
	
	public String getDev(){
		return serialDev;
	}
	
	public void setBrt(int tty){
		serialBrt = tty;
	}
	
	public int getBrt(){
		return serialBrt;
	}
	
	public void setFlg(int tty){
		serialFlg = tty;
	}
	
	// java 合并两个byte数组
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	public String getTime(){
		Date time = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		return dateFormat.format(time).toString();
	}
	/**
	 * 将长度为2的byte数组转换为16位int
	 * 
	 * @param res
	 *            byte[]
	 * @return int
	 * */
	public static int byte2int(byte[] res) {

		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
		return targets;
	}
}
