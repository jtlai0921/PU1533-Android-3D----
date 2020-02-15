package com.cw.view;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.view.SurfaceView;

public class InPutStreamTobyte {
		public static byte[] readStream(SurfaceView mv,int drawableId){
			InputStream is =  mv.getResources().openRawResource(drawableId);
	        byte[] bytes = new byte[1024];  
	        int leng;  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        try{
		         while((leng=is.read(bytes)) != -1)
		        {  
		            baos.write(bytes, 0, leng);  
		        } 
	        }
	        catch(Exception e)
	        {
	        } 
	        return baos.toByteArray();  
	    }
}
