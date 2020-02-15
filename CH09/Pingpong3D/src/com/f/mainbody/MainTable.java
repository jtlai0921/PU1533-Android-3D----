package com.f.mainbody;

import javax.microedition.khronos.opengles.GL10;

import com.f.util.MatrixState;
import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;

import android.opengl.GLES20;

public class MainTable 
{
	private int mProgram;//自訂著色管線著色器程式id  
	private int muMVPMatrixHandle;//總變換矩陣參考
	private int muMMatrixHandle;//位置、旋轉變換矩陣
	private int maPositionHandle; //頂點位置屬性參考  
	private int maNormalHandle; //頂點法向量屬性參考  
	private int maLightLocationHandle;//光源位置屬性參考  
	private int maCameraHandle; //攝影機位置屬性參考 
	private int maTexCoorHandle; //頂點紋理座標屬性參考  
	private int muProjCameraMatrixHandle;//投影、攝影機群組合矩陣參考
	private int muIsShadow;//是否繪制陰影屬性參考
	
	public MainTable()
	{
    	//起始化shader
    	initShader();
	}
	
	public void initShader()
	{
        //基於頂點著色器與片元著色器建立程式
        mProgram = program[4];
        //取得程式中頂點位置屬性參考  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點彩色屬性參考  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //取得程式中總變換矩陣參考
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //取得位置、旋轉變換矩陣參考
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
        //取得程式中光源位置參考
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //取得程式中頂點紋理座標屬性參考  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor"); 
        //取得程式中攝影機位置參考
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        //取得程式中是否繪制陰影屬性參考
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow");
        //取得程式中投影、攝影機群組合矩陣參考
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix");
	}
	
	public void drawSelf(int texId,int isShadow)
	{
		//制定使用某套著色器程式
   	 	GLES20.glUseProgram(mProgram);
        //將最終變換矩陣傳入著色器程式
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        //將位置、旋轉變換矩陣傳入著色器程式
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
        //將光源位置傳入著色器程式   
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.getLightPositionBuffer());
        //將攝影機位置傳入著色器程式   
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.getCameraPosBuffer());
        GLES20.glUniform1i(muIsShadow, isShadow);
        //將投影、攝影機群組合矩陣傳入著色器程式
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0);
        // 將頂點位置資料傳入著色管線
        GLES20.glVertexAttribPointer  
        (
        	maPositionHandle,   
        	3, 
        	GLES20.GL_FLOAT, 
        	false,
            3*4,   
            vertexBuffer[0][8]
        );       
        //將頂點法向量資料傳入著色管線
        GLES20.glVertexAttribPointer  
        (
       		maNormalHandle, 
       		3,   
       		GLES20.GL_FLOAT, 
       		false,
         	3*4,   
         	normalBuffer[0][2]
        );   
        //為畫筆指定頂點紋理座標資料
        GLES20.glVertexAttribPointer  
        (
       		maTexCoorHandle, 
       		2,
        	GLES20.GL_FLOAT, 
        	false,
        	2*4,   
        	texCoorBuffer[0][3]
        );
        //啟用頂點位置、法向量、紋理座標資料
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maNormalHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
        //綁定紋理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        //繪制載入的物體
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexlength[0][1], GL10.GL_UNSIGNED_SHORT, indexBuffer[0][1]);
	}
}
