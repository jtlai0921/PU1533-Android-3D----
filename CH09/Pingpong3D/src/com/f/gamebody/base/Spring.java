package com.f.gamebody.base;

import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;

import com.f.util.MatrixState;

import android.opengl.GLES20;
public class Spring {
	
	private int mProgram;
	private int muMVPMatrixHandle;
	private int maPositionHandle; 
	private int maTexCoorHandle; 


	public Spring() {
		initShader();
	}

	private void initShader() {
		mProgram = program[8];
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	
	}

	public void drawSelf(int texId)
	{
		GLES20.glUseProgram(mProgram);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,MatrixState.getFinalMatrix(), 0);
		//gVCount[5],gVertexBuffer[5],gTexCoorBuffer[2]
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,false, 3 * 4,vertexBuffer[1][5]);
		GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,false, 2 * 4, texCoorBuffer[1][2]);
	   
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount[1][5]);
	}
}
