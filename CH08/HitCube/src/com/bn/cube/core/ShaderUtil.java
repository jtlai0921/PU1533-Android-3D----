package com.bn.cube.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

//載入頂點Shader與片元Shader的工具類別
public class ShaderUtil 
{
   //載入制定shader的方法
   public static int loadShader
   (
		 int shaderType, //shader的型態  GLES20.GL_VERTEX_SHADER   GLES20.GL_FRAGMENT_SHADER
		 String source   //shader的指令稿字串
   ) 
   {
	    //建立一個新shader
        int shader = GLES20.glCreateShader(shaderType);
        //若建立成功則載入shader
        if (shader != 0) 
        {
        	//載入shader的源程式碼
            GLES20.glShaderSource(shader, source);
            //編譯shader
            GLES20.glCompileShader(shader);
            //存放編譯成功shader數量的陣列
            int[] compiled = new int[1];
            //取得Shader的編譯情況
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) 
            {//若編譯失敗則顯示錯誤日志並移除此shader
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;      
            }  
        }
        return shader;
    }
    
   //建立shader程式的方法
   public static int createProgram(String vertexSource, String fragmentSource) 
   {
	    //載入頂點著色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) 
        {
            return 0;
        }
        
        //載入片元著色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) 
        {
            return 0;
        }

        //建立程式
        int program = GLES20.glCreateProgram();
        //若程式建立成功則向程式中加入頂點著色器與片元著色器
        if (program != 0) 
        {
        	//向程式中加入頂點著色器
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            //向程式中加入片元著色器
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            //連結程式
            GLES20.glLinkProgram(program);
            //存放連結成功program數量的陣列
            int[] linkStatus = new int[1];
            //取得program的連結情況
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            //若連結失敗則顯示出錯並移除程式
            if (linkStatus[0] != GLES20.GL_TRUE) 
            {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }
    
   //檢查每一步動作是否有錯誤的方法 
   public static void checkGlError(String op) 
   {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) 
        {
            Log.e("ES20_ERROR", op + ": glError " + error);
            //throw new RuntimeException(op + ": glError " + error);
        }
   }
   
   //從sh指令稿中載入shader內容的方法
   public static String loadFromAssetsFile(String fname,Resources r)
   {
   	String result=null;    	
   	try
   	{
   		InputStream in=r.getAssets().open(fname);
			int ch=0;
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    while((ch=in.read())!=-1)
		    {
		      	baos.write(ch);
		    }      
		    byte[] buff=baos.toByteArray();
		    baos.close();
		    in.close();
   		result=new String(buff,"UTF-8"); 
   		result=result.replaceAll("\\r\\n","\n");
   	}
   	catch(Exception e)
   	{
   		e.printStackTrace();
   	}    	
   	return result;
   }
}
