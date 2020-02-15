package com.f.gamebody;

import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import android.opengl.GLES20;

import com.f.util.MatrixState;
public class GameBall {

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
	private int xRangeHandle;//陰影獲得的x範圍 的一半值 參考
	private int zRangeHandle;//陰影獲得的z範圍 的一半值 參考
	private int alphaHandle;//透明度參數

	/**	目前速度，位置*/
	public Vector3f speed = new Vector3f();;
	public Vector3f position = new Vector3f();
	/** 起始速度，位置，速度*/
	public Vector3f speed_start = new Vector3f();
	public Vector3f position_start = new Vector3f();
	public Vector3f timeLive = new Vector3f();
	
	
	public int side = 0;

	public List<Vector3f> lList;//位置(Location)清單
	public GameBall()
	{
		if(IS_SHOOT_MAN){
			shootBall(0.0f);
		}else{
			shootBallAI(0.0f);
		}
		lList = new ArrayList<Vector3f>();
		initShader();
	}
  	public void addPosition(Vector3f pos)
	{
		if(lList.size()>3)lList.remove(0);
		lList.add(pos);
	}
	public void shootBall(float x)
	{
		speed.set(0.0f, 0.0f, 0.0f);				   /** 起始速度*/
		timeLive.set(0.0f, 0.0f, 0.0f); 
		position.set(x, 0.7f, TABLE_Z_MAX - 0.2f);     /** 球的起始位置*/
		speed_start.set(speed);
		position_start.set(position);
	}
	
	public void shootBallAI(float speedX)
	{
		speed.set(speedX, -0.7f, 1.1f);				   /** 起始速度*/
		timeLive.set(0.0f, 0.0f, 0.0f); 
		position.set(0, 0.7f, TABLE_Z_MIN + 0.8f);     /** 球的起始位置*/
		speed_start.set(speed);
		position_start.set(position);
		IS_SHOOTING = false;
	}
	
	public void runWithRacket(float x)
	{
		timeLive.x = 0.0f;
		position.x = x;
	}

	private void initShader() {
		
		mProgram = program[11];
		
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");  
		uMMatrixHandle =  GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		uCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
		muLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //取得程式中是否繪制陰影屬性參考
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
        xRangeHandle=GLES20.glGetUniformLocation(mProgram, "x_Range"); 
        zRangeHandle=GLES20.glGetUniformLocation(mProgram, "z_Range"); 
        alphaHandle =GLES20.glGetUniformLocation(mProgram, "alpha");
		//取得程式中投影、攝影機群組合矩陣參考
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
	}

	/**
	 * 0 是圓球 1是陰影
	 */
	public void drawSelf(int texid,int isShadow,Vector3f posNow,float alpha){
		
		MatrixState.pushMatrix();
		GLES20.glUseProgram(mProgram);
		//搬移球
		MatrixState.translate(posNow.x, posNow.y, posNow.z);
		MatrixState.scale(BALL_MBALL,BALL_MBALL, BALL_MBALL);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,MatrixState.getFinalMatrix(), 0);
		GLES20.glUniformMatrix4fv(uMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
		
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,false, 3 * 4, vertexBuffer[0][6]);
	    GLES20.glVertexAttribPointer(maTexCoorHandle,2,GLES20.GL_FLOAT,false,2*4, texCoorBuffer[0][1]); 
	    GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false, 3*4, normalBuffer[0][0]);
	    	
	    GLES20.glUniform3fv(uCameraHandle, 1, MatrixState.getCameraPosBuffer());
		GLES20.glUniform3fv(muLightLocationHandle, 1, MatrixState.getLightPositionBuffer());
		//將投影、攝影機群組合矩陣傳入著色器程式
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0);  
        //將是否繪制陰影屬性傳入著色器程式 
        //GLES20.glUniform1i(muIsShadow, isShadow);  
        GLES20.glUniform1f(muIsShadow, isShadow);  
        GLES20.glUniform1f(xRangeHandle, TABLE_WIDHT/2);  
        GLES20.glUniform1f(zRangeHandle, TABLE_LENGTH/2);  
        GLES20.glUniform1f(alphaHandle, alpha);
	    
	    GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
		GLES20.glEnableVertexAttribArray(maNormalHandle);
		 //綁定紋理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texid);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0,vCount[0][1]);
		MatrixState.popMatrix();
	}
}
