package com.f.mainbody;

import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;

import javax.vecmath.Vector3f;

import android.opengl.GLES20;

import com.f.util.MatrixState;

public class MainBall 
{
	private int mProgram;
	private int maTexCoorHandle;
	private int maPositionHandle;
	private int muMVPMatrixHandle;
	private int maNormalHandle;
	private int muLightLocationHandle;
	private int uCameraHandle;
	private int uMMatrixHandle;
	private int muProjCameraMatrixHandle;//投影、攝影機群組合矩陣參考
	private int muIsShadow;//是否繪制陰影屬性參考
	
	public Vector3f timeLive = null;
	public Vector3f speed = null;
	public Vector3f speed_start = null;
	public Vector3f position = null;
	public Vector3f positoin_start = null;
	
	public boolean isMove = false;
	public float time_unit = 0.1f;
	
	public MainBall()
	{
		this.isMove = true;
		speed = new Vector3f(0.0f,0.0f,0.0f);
		timeLive = new Vector3f();
		position = new Vector3f(0.0f,3.0f,1.0f);
		speed_start = new Vector3f(speed);
		positoin_start = new Vector3f(position);
		initShader();
	}
	
	private void initShader() 
	{
		mProgram = program[0];
		
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");  
		uMMatrixHandle =  GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		uCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
		muLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //取得程式中是否繪制陰影屬性參考
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow");
        //取得程式中投影、攝影機群組合矩陣參考
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix");
	}
	
	public void drawSelf(int texid,int isShadow)
	{
		MatrixState.pushMatrix();
		//搬移球
		MatrixState.translate(position.x, position.y, position.z);
		GLES20.glUseProgram(mProgram);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,MatrixState.getFinalMatrix(), 0);
		GLES20.glUniformMatrix4fv(uMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,false, 3 * 4, vertexBuffer[0][6]);
	    GLES20.glVertexAttribPointer(maTexCoorHandle,2,GLES20.GL_FLOAT,false,2*4,texCoorBuffer[0][1]); 
	    GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false, 3*4, normalBuffer[0][0]);
	    GLES20.glUniform3fv(uCameraHandle, 1, MatrixState.getCameraPosBuffer());
		GLES20.glUniform3fv(muLightLocationHandle, 1, MatrixState.getLightPositionBuffer());
		//將投影、攝影機群組合矩陣傳入著色器程式
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0);  
        //將是否繪制陰影屬性傳入著色器程式 
        GLES20.glUniform1i(muIsShadow, isShadow);  
        GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
		GLES20.glEnableVertexAttribArray(maNormalHandle);
		//綁定紋理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texid);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount[0][1]);
		
		MatrixState.popMatrix();
	}
}
