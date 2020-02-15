package com.f.gamebody.base;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.f.util.MatrixState;
import static com.f.pingpong.ShaderManager.*;

public class GameFloorWallRect 
{
	int vCount;
	int mProgram;//自訂著色管線著色器程式id  
    int muMVPMatrixHandle;//總變換矩陣參考
    int muMMatrixHandle;//位置、旋轉變換矩陣
    int maPositionHandle; //頂點位置屬性參考  
    int maNormalHandle; //頂點法向量屬性參考  
    int maLightLocationHandle;//光源位置屬性參考  
    int maCameraHandle; //攝影機位置屬性參考 
    int maTexCoorHandle; //頂點紋理座標屬性參考  
    int uLightTexHandle;//白天紋理屬性參考id  
    int uShadowTexHandle;//黑夜紋理屬性參考id
    String mVertexShader;//頂點著色器程式碼指令稿    	 
    String mFragmentShader;//片元著色器程式碼指令稿
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private FloatBuffer normalBuffer;
	
	public GameFloorWallRect(int vConut,FloatBuffer vertexBuffer,FloatBuffer textureBuffer,FloatBuffer normalBuffer)
	{
		this.vCount = vConut;
		this.vertexBuffer = vertexBuffer;
		this.textureBuffer = textureBuffer;
		this.normalBuffer = normalBuffer;
    	//起始化shader        
    	initShader();
	}
	public void initShader()
	{
        //基於頂點著色器與片元著色器建立程式
        mProgram = program[7];
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
        //取得白天、黑夜兩個紋理參考
        uLightTexHandle=GLES20.glGetUniformLocation(mProgram, "sLightTexture");  
        uShadowTexHandle=GLES20.glGetUniformLocation(mProgram, "sShadowTexture");
	}
	
	public void drawSelf(int texLightId,int texShadowId)
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
        // 將頂點位置資料傳入著色管線
        GLES20.glVertexAttribPointer  
        (
        	maPositionHandle,   
        	3, 
        	GLES20.GL_FLOAT, 
        	false,
            3*4,   
            vertexBuffer
        );       
        //將頂點法向量資料傳入著色管線
        GLES20.glVertexAttribPointer  
        (
       		maNormalHandle, 
       		3,   
       		GLES20.GL_FLOAT, 
       		false,
         	3*4,   
         	normalBuffer
        );   
        //為畫筆指定頂點紋理座標資料
        GLES20.glVertexAttribPointer  
        (
       		maTexCoorHandle, 
       		2,
        	GLES20.GL_FLOAT, 
        	false,
        	2*4,   
        	textureBuffer
        );
        //啟用頂點位置、法向量、紋理座標資料
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maNormalHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
        //綁定紋理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texLightId);
        //綁定紋理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texShadowId);
        GLES20.glUniform1i(uLightTexHandle, 0);
        GLES20.glUniform1i(uShadowTexHandle, 1);
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
	}
}
