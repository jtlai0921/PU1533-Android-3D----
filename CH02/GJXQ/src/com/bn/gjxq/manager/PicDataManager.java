package com.bn.gjxq.manager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;

//���z�ϸ�ƺ޲z��
public class PicDataManager 
{
	public static boolean isLoaded=false;
	public static byte[][] picDataArray=null;
	public static String[] texNameArray=
	{
			"black.jpg",//�¦�W���Ϥ�    			0
			"blackchess.jpg",//�¦�Ѥl���z		1
			"bx.jpg",//�Ŀ�¦�W���Ϥ�			2
			"room.jpg",//�ж�					3
			"white.png",//�զ�W���Ϥ�			4
			"whitechess.jpg",//�զ�Ѥl���z		5
			"wx.jpg",//�Ŀ�զ�W���Ϥ�			6
			"qipan.png",//�p�ѺЯ��z                                  7
			"heise.png",//�p�Ѻиs�զ�������z              8
			"baise.png",//�p�Ѻиs�զ�������z                 9
			"b0.png",//�§L						10
			"b1.png",//�«�						11
			"b2.png",//�¤�						12
			"b3.png",//�¶H						13
			"b4.png",//�¨�						14
			"b5.png",//�°�						15
			"w0.png",//�էL						16
			"w1.png",//�ի�						17
			"w2.png",//�դ�						18
			"w3.png",//�նH						19
			"w4.png",//�ը�						20
			"w5.png",//�հ�						21
			"xiaojiantou.png"//�p�b�Y                              22
	};
	
	//���J�Ϥ���ƶi�O���骺��k
	public static void loadPicData(Context context)
	{
		if(isLoaded)return;
		
		picDataArray=new byte[texNameArray.length][];
		
		for(int i=0;i<texNameArray.length;i++)
		{
			picDataArray[i]=loadFromAssets(context,texNameArray[i]);
		}
		
		isLoaded=true;
	}
	
	//�qAssets�����J�@�T���z
	public static byte[] loadFromAssets(Context context,String picName)
	{
		byte[] result=null;
		
		try
    	{
    		InputStream in=context.getResources().getAssets().open(picName);
			int ch=0;
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    while((ch=in.read())!=-1)
		    {
		      	baos.write(ch);
		    }      
		    result=baos.toByteArray();
		    baos.close();
		    in.close();    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}    	
		
		return result;
	}
}
