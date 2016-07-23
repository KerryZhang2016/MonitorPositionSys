package com.bupt.monitorpositionsys.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.bupt.monitorpositionsys.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public abstract class SerialActivity extends Activity {

	protected MyApplication mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	protected ReadThread mReadThread;

	class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			byte[] buffer = new byte[1024];

			while (!isInterrupted()) {
				int size = 0;
				try {

					if (mInputStream == null)
						return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						DataReceived(buffer, size);
					}

				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	private int haveReced = 0;
	private byte[] reced = new byte[16*1024];
	private void DataReceived(byte[] buffer, int size) {
		// TODO Auto-generated method stub
		System.arraycopy(buffer, 0, reced, haveReced, size);
		haveReced = haveReced + size;
		/*
		String flg = new String(buffer, size-2, 2);
		if(!flg.equals("\r\n")){
			return ;
		}
		*/
		onDataReceived(reced, haveReced);
		haveReced = 0;
	}
	
	protected abstract void onDataReceived(final byte[] buffer, final int size);

	void sendMessage(String msg, int localICaddr){
		
		/*int dataType, type, mode, command, address, messageLength;
		dataType = type = mode = command = address = messageLength = 0;
		dataType=1;
		String strMessage = msg;
		byte[] message = null;
		byte[] messagegb2312 = new byte[512];

		try {
			message = strMessage.getBytes("gb2312");
			messageLength = message.length;
			int j = 0;
			for (int i = 0; i < messageLength; i++) {
				if (message[i] > 0) {
					messagegb2312[i + j] = (byte) 0xA3;
					j++;
					messagegb2312[i + j] = (byte) (message[i] + 0x80);

				} else {
					messagegb2312[i + j] = message[i];
				}
			}
			messageLength = messageLength + j;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		
		try {
			int l = msg.length();
			byte[] dataPacket = new byte[l/2];
			//byte[] bConmmand=Msg.getBytes("UTF8");
			int n = 0;
			for (int i = 0; i < l/2;i++ ) {
			    String byteString = msg.substring(n, n+2);
			    byte tb =  (byte)(Integer.parseInt(byteString,16));
				dataPacket[i] =tb;
				n+=2;
				
			}
			/**
			dataPacket[0]=(byte)0x01;
			dataPacket[1]=(byte)0x04;
			dataPacket[2]=(byte)0x00;
	        dataPacket[3]=(byte)0x00;
	        dataPacket[4]=(byte)0x00;
	        dataPacket[5]=(byte)0x01;
	        dataPacket[6]=(byte)0x31;
	        dataPacket[7]=(byte)0xCA;
	        */
	         
			if (mOutputStream != null)
				mOutputStream.write(dataPacket, 0, l);
			//sendDataPacket(dataType, type, mode, command, localICaddr, address, messageLength * 8, messagegb2312);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void sendDataPacket(int dataType, int type, int mode, int command, int localICaddr, int address, int messageLength,
			byte[] message) throws Exception {
		// 字节长度
		final int orderLength = 5;
		final int dataLength = 2;
		final int localAddressLength = 3;
		final int posAddressLength = 3;
		final int messageTypeLength = 1;
		final int messageContentLength = 210;
		final int checksumLength = 1;
		final int maxTotalLength = 228;

		byte[] dataPacket = new byte[maxTotalLength];
		String string = "$TXSQ";
		byte[] bConmmand = string.getBytes("UTF8");
		for (int n = 0; n < bConmmand.length; n++) {
			dataPacket[n] = bConmmand[n];
		}
		int i=0;
		
		switch(dataType){
		case 0:  //文本
			// 地址
			i = orderLength + dataLength;

			dataPacket[i + 2] = (byte) (0xff & localICaddr);
			dataPacket[i + 1] = (byte) ((0xff00 & localICaddr) >> 8);
			dataPacket[i] = (byte) ((0xff0000 & localICaddr) >> 16);

			// dataType 0代表报文通信 1代表查询通信
			i = orderLength + dataLength + localAddressLength;
			dataPacket[i] = 0x00;
			dataPacket[i] = (byte) (dataPacket[i] | 0x40);
			if (type == 0)// 特快通信
			{

			} else if (type == 1) {// 普通通信
				dataPacket[i] = (byte) (dataPacket[i] | 0x04);
			}
			if (mode == 0)// 传输方式汉字
			{

			} else if (mode == 1) {// 传输方式代码
				dataPacket[i] = (byte) (dataPacket[i] | 0x02);
			}
			if (command == 0)// 通信
			{

			} else if (command == 1) {// 口令识别
				dataPacket[i] = (byte) (dataPacket[i] | 0x01);
			}

			i = orderLength + dataLength + localAddressLength + messageTypeLength;
			dataPacket[i + 2] = (byte) (0xff & address);
			dataPacket[i + 1] = (byte) ((0xff00 & address) >> 8);
			dataPacket[i] = (byte) ((0xff0000 & address) >> 16);

			i = orderLength + dataLength + localAddressLength + messageTypeLength + posAddressLength;
			dataPacket[i + 1] = (byte) (0xff & messageLength);
			dataPacket[i] = (byte) ((0xff00 & messageLength) >> 8);// 报文长度

			// 长度
			i = orderLength;
			int length = maxTotalLength - (messageContentLength - messageLength / 8);
			dataPacket[i + 1] = (byte) (0xff & length);
			dataPacket[i] = (byte) ((0xff00 & length) >> 8);

			i = maxTotalLength - checksumLength - messageContentLength;
			for (int n = 0; n < messageLength / 8; n++) {
				dataPacket[i + n] = message[n];
			}

			//strMessage=rTest(message,messageLength);

			i = maxTotalLength - checksumLength - messageContentLength + messageLength / 8;
			byte bCheckCode = dataPacket[0];
			for (int n = 1; n < i; n++) {
				bCheckCode = (byte) (bCheckCode ^ dataPacket[n]);
			}
			dataPacket[i] = bCheckCode;
			if (mOutputStream != null)
				mOutputStream.write(dataPacket, 0, i + 1);
			break;
		case 1:  //十六进制
			
			byte[] MessageBuffer = new byte[4096 * 1024];
			int j = 0;
			for (int n = 0; n < messageLength / 8; n = n + 2) {
				if (message[n] == (byte) 0xA3) {
					MessageBuffer[j] = (byte) (message[n + 1] - 128);
					j++;
				} else {
					MessageBuffer[j] = message[n];
					MessageBuffer[j + 1] = message[n + 1];
					j = j + 2;
				}
			}
			String strMessage = new String(MessageBuffer, 0, j, "gb2312");
			bConmmand=strMessage.getBytes("UTF8");
			for (int n = 0; n < bConmmand.length; n++) {
				dataPacket[n] = bConmmand[n];
			}
			i=bConmmand.length-1;

			if (mOutputStream != null)
				mOutputStream.write(dataPacket, 0, i + 1);
			break;
		default:
			break;
		}
	}
	
	public int Serial_Start(String Dev, int Brt){
		try {
			mApplication.setDev(Dev);
			mApplication.setBrt(Brt);
			//mApplication.setFlg(Flg);
			mSerialPort = mApplication.getSerialPort();
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
		} catch (SecurityException e) {
			DisplayError(R.string.error_security);
			return 1;
		} catch (IOException e) {
			DisplayError(R.string.error_unknown);
			return 2;
		} catch (InvalidParameterException e) {
			DisplayError(R.string.error_configuration);
			return 3;
		}
		return 0;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (MyApplication) getApplication();
		//Serial_Start("/dev/ttyUSB4",115200);
	}
	
	private void DisplayError(int resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//SerialActivity.this.finish();
			}
		});
		b.show();
	}
	
	@Override
	protected void onDestroy() {
		if (mReadThread != null)
			mReadThread.interrupt();
		mApplication.closeSerialPort();
		mSerialPort = null;
		super.onDestroy();
	}
}
