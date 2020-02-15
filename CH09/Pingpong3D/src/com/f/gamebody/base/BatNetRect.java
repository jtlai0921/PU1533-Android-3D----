package com.f.gamebody.base;

import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.f.util.MatrixState;
import com.f.util.ShaderUtil;

import static com.f.pingpong.ShaderManager.*;

public class BatNetRect {

	private int vCount;
	private int mProgram;
	private int maPositionHandle;
	private int muMVPMatrixHandle;
	private int maTexCoorHandle;
	private int uMMatrixHandle;
	private int uCameraHandle;
	private int uLightLocationHandle;
	private int aNormalHandle;
	private int muIsShadow;//是否繪制陰影屬性參考  
	private int muProjCameraMatrixHandle;//投影、攝影機群組合矩陣參考
	
	private FloatBuffer mVertexBuffer  = null;
	private FloatBuffer mTexCoorBuffer = null;
	private FloatBuffer mNormalBuffer  = null;
	
	public BatNetRect(int vCount,FloatBuffer mVertexBuffer,FloatBuffer mTexCoorBuffer,FloatBuffer mNormalBuffer)
	{
		this.vCount = vCount;
		this.mVertexBuffer = mVertexBuffer;
		this.mTexCoorBuffer = mTexCoorBuffer;
		this.mNormalBuffer = mNormalBuffer;
		initShader();
	}
	
	public BatNetRect(Resources res,int vCount,FloatBuffer mVertexBuffer,FloatBuffer mTexCoorBuffer,FloatBuffer mNormalBuffer)
	{
		this.vCount = vCount;
		this.mVertexBuffer = mVertexBuffer;
		this.mTexCoorBuffer = mTexCoorBuffer;
		this.mNormalBuffer = mNormalBuffer;
		initShader(res);
	}
	
	private void initShader() 
	{
		mProgram = program[10];
		
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");  
		
		aNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		uMMatrixHandle =  GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		uCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
		uLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //取得程式中是否繪制陰影屬性參考
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
		//取得程式中投影、攝影機群組合矩陣參考
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
	}
	
	private void initShader(Resources res) 
	{
		String mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_gametable_shadow.sh",res);
		String mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_gametable_shadow.sh",res);
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");  
		
		aNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		uMMatrixHandle =  GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		uCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
		uLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //取得程式中是否繪制陰影屬性參考
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
		//取得程式中投影、攝影機群組合矩陣參考
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
	}
	
	public void drawSelf(int texid,int isShadow){
		
		GLES20.glUseProgram(mProgram);
		
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,MatrixState.getFinalMatrix(), 0);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,false, 3 * 4, mVertexBuffer);
	    GLES20.glVertexAttribPointer(maTexCoorHandle,2,GLES20.GL_FLOAT,false,2*4,mTexCoorBuffer); 
		GLES20.glVertexAttribPointer(aNormalHandle, 3, GLES20.GL_FLOAT, false, 3*4, mNormalBuffer);
		GLES20.glUniformMatrix4fv(uMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
		GLES20.glUniform3fv(uCameraHandle, 1, MatrixState.getCameraPosBuffer());
		GLES20.glUniform3fv(uLightLocationHandle, 1, MatrixState.getLightPositionBuffer());

		//將投影、攝影機群組合矩陣傳入著色器程式
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0);  
        GLES20.glUniform1i(muIsShadow, isShadow);  
        
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
		GLES20.glEnableVertexAttribArray(aNormalHandle);
		 //綁定紋理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texid);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
	}
}
