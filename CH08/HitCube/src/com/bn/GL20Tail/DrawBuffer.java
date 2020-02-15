package com.bn.GL20Tail;
 


import java.nio.FloatBuffer;  

import com.bn.cube.core.MatrixState;
import com.bn.cube.game.MySurfaceView;

import android.opengl.GLES20;

//紋理三角形
public class DrawBuffer 
{	
	int mProgram;//自訂著色管執行緒序id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id  
    int maAlphaHandle;
    String mVertexShader;//頂點著色器    	 
    String mFragmentShader;//片元著色器
    
    public DrawBuffer(MySurfaceView mv)
    {
    	//起始化著色器        
    	initShader(mv);
    }  

    //起始化著色器
    public void initShader(MySurfaceView mv)
    {
    	//載入頂點著色器的指令稿內容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //載入片元著色器的指令稿內容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //基於頂點著色器與片元著色器建立程式
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理座標屬性參考id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        maAlphaHandle=GLES20.glGetAttribLocation(mProgram, "aAlpha");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
    
    public void drawSelf(int texId,FloatBuffer   mVertexBuffer,FloatBuffer mTexCoorBuffer,FloatBuffer mAlphaBuffer,int vCount)
    {        
    	 //制定使用某套shader程式
    	 GLES20.glUseProgram(mProgram);  

         //將最終變換矩陣傳入shader程式
    	 MatrixState.copyMVMatrix();
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //為畫筆指定頂點位置資料
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //為畫筆指定頂點紋理座標資料
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );   
         
         GLES20.glVertexAttribPointer  
         (
        		maAlphaHandle, 
         		1, 
         		GLES20.GL_FLOAT, 
         		false,
                1*4,   
                mAlphaBuffer
         );   
         
         //容許頂點位置資料陣列
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         GLES20.glEnableVertexAttribArray(maAlphaHandle);  
         
         //綁定紋理
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //繪制紋理矩形
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}
