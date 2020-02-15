package com.bn.gjxq.manager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;

//紋理圖資料管理者
public class PicDataManager 
{
	public static boolean isLoaded=false;
	public static byte[][] picDataArray=null;
	public static String[] texNameArray=
	{
			"black.jpg",//黑色上面圖片    			0
			"blackchess.jpg",//黑色棋子紋理		1
			"bx.jpg",//勾選黑色上面圖片			2
			"room.jpg",//房間					3
			"white.png",//白色上面圖片			4
			"whitechess.jpg",//白色棋子紋理		5
			"wx.jpg",//勾選白色上面圖片			6
			"qipan.png",//小棋碟紋理                                  7
			"heise.png",//小棋碟群組成方塊紋理              8
			"baise.png",//小棋碟群組成方塊紋理                 9
			"b0.png",//黑兵						10
			"b1.png",//黑後						11
			"b2.png",//黑王						12
			"b3.png",//黑象						13
			"b4.png",//黑車						14
			"b5.png",//黑馬						15
			"w0.png",//白兵						16
			"w1.png",//白後						17
			"w2.png",//白王						18
			"w3.png",//白象						19
			"w4.png",//白車						20
			"w5.png",//白馬						21
			"xiaojiantou.png"//小箭頭                              22
	};
	
	//載入圖片資料進記憶體的方法
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
	
	//從Assets中載入一幅紋理
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
