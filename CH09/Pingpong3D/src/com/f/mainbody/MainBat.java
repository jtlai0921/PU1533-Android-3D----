package com.f.mainbody;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.f.util.MatrixState;
import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;

public class MainBat 
{
	private int mProgram;//�ۭq�ۦ�޽u�ۦ⾹�{��id  
	private int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�
	private int muMMatrixHandle;//��m�B�����ܴ��x�}
	private int maPositionHandle; //���I��m�ݩʰѦ�  
	private int maNormalHandle; //���I�k�V�q�ݩʰѦ�  
	private int maLightLocationHandle;//������m�ݩʰѦ�  
	private int maCameraHandle; //��v����m�ݩʰѦ� 
	private int maTexCoorHandle; //���I���z�y���ݩʰѦ�  
	
	public MainBat()
	{
    	//�_�l��shader        
    	initShader();
	}
	
	
	public void initShader()
	{
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        mProgram = program[1];
        //���o�{�������I��m�ݩʰѦ�  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I�m���ݩʰѦ�  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //���o�{�����`�ܴ��x�}�Ѧ�
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //���o��m�B�����ܴ��x�}�Ѧ�
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
        //���o�{����������m�Ѧ�
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //���o�{�������I���z�y���ݩʰѦ�  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor"); 
        //���o�{������v����m�Ѧ�
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
