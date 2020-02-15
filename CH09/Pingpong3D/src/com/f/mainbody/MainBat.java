package com.f.mainbody;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.f.util.MatrixState;
import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;

public class MainBat 
{
	private int mProgram;//自訂著色管線著色器程式id  
	private int muMVPMatrixHandle;//總變換矩陣參考
	private int muMMatrixHandle;//位置、旋轉變換矩陣
	private int maPositionHandle; //頂點位置屬性參考  
	private int maNormalHandle; //頂點法向量屬性參考  
	private int maLightLocationHandle;//光源位置屬性參考  
	private int maCameraHandle; //攝影機位置屬性參考 
	private int maTexCoorHandle; //頂點紋理座標屬性參考  
	
	public MainBat()
	{
    	//起始化shader        
    	initShader();
	}
	
	
	public void initShader()
	{
        //基於頂點著色器與片元著色器建立程式
        mProgram = program[1];
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
	}
	
	public void drawSelf(int texId)
	{
   	 	GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.getLightPositionBuffer());
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.getCameraPosBuffer());
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,false, 3 * 4, vertexBuffer[0][7]);
		GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false,	3 * 4, normalBuffer[0][1]);
		GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,false, 2 * 4, texCoorBuffer[0][2]);
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maNormalHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexlength[0][0], GL10.GL_UNSIGNED_SHORT, indexBuffer[0][0]);
	}
}
