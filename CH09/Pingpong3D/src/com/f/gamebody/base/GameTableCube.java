package com.f.gamebody.base;

import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;

import android.opengl.GLES20;

import com.f.util.MatrixState;

public class GameTableCube {

	private int mProgram;
	private int maPositionHandle;
	private int muMVPMatrixHandle;
	private int maTexCoorHandle;
	private int uMMatrixHandle;
	private int uCameraHandle;
	private int aNormalHandle;
	private int uLightLocationHandle;
	private int muIsShadow;//�O�_ø��v�ݩʰѦ�  
	private int muProjCameraMatrixHandle;//��v�B��v���s�զX�x�}�Ѧ�
	
	public GameTableCube()
	{
		initShader();
	}
	private void initShader() {
		
		mProgram = program[10];
		
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");  
		
		aNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		uMMatrixHandle =  GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		uCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
		uLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //���o�{�����O�_ø��v�ݩʰѦ�
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
		//���o�{������v�B��v���s�զX�x�}�Ѧ�
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
	}
	public void drawSelf(int texid,int isShadow)
	{
		
		GLES20.glUseProgram(mProgram);
		//gVCount[4],gVertexBuffer[4],gTexCoorBuffer[1] ,gNormalBuffer[1]
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,MatrixState.getFinalMatrix(), 0);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,false, 3 * 4, vertexBuffer[1][4]);
	    GLES20.glVertexAttribPointer(maTexCoorHandle,2,GLES20.GL_FLOAT,false,2*4,texCoorBuffer[1][1]); 
	    GLES20.glVertexAttribPointer(aNormalHandle, 3, GLES20.GL_FLOAT, false, 3*4, normalBuffer[1][1]);
		GLES20.glUniformMatrix4fv(uMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
		GLES20.glUniform3fv(uCameraHandle, 1, MatrixState.getCameraPosBuffer());
		GLES20.glUniform3fv(uLightLocationHandle, 1, MatrixState.getLightPositionBuffer());
		
		//�N��v�B��v���s�զX�x�}�ǤJ�ۦ⾹�{��
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0);  
        GLES20.glUniform1i(muIsShadow, isShadow);  
	    
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
		GLES20.glEnableVertexAttribArray(aNormalHandle);
		//�j�w���z
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texid);
        
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount[1][4]);
	}


}
