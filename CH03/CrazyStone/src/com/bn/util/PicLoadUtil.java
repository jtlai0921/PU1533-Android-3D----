package com.bn.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//處理圖片
public class PicLoadUtil 
{
	public static Bitmap loadBM(Resources res,String fname)//透過IO得到了圖片的bitmap
    {
    	Bitmap result=null;    	
    	try
    	{
    		InputStream in=res.getAssets().open(fname);
			int ch=0;
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    while((ch=in.read())!=-1)
		    {
		      	baos.write(ch);
		    }      
		    byte[] buff=baos.toByteArray();
		    baos.close();
		    in.close();
		    result=BitmapFactory.decodeByteArray(buff, 0, buff.length);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}    	
    	return result;
    }
}
