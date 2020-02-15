package com.f.mainbody;

import android.opengl.GLES20;

import com.f.util.MatrixState;
import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;

public class MainFloor 
{
	private int mProgram;//�ۭq�ۦ�޽u�ۦ⾹�{��id  
	private int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�
	private int muMMatrixHandle;//��m�B�����ܴ��x�}
	private int maPositionHandle; //���I��m�ݩʰѦ�  
	private int maNormalHandle; //���I�k�V�q�ݩʰѦ�  
	private int maLightLocationHandle;//������m�ݩʰѦ�  
	private int maCameraHandle; //��v����m�ݩʰѦ� 
	private int maTexCoorHandle; //���I���z�y���ݩʰѦ�  
	private int uLightTexHandle;//�դѯ��z�ݩʰѦ�id  
	private int uShadowTexHandle;//�©]���z�ݩʰѦ�id
	
	public MainFloor()
	{
    	//�_�l��shader        
    	initShader();
	}
	
	public void initShader()
	{
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        mProgram = program[2];
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
        //���o�դѡB�©]��ӯ��z�Ѧ�
        uLightTexHandle=GLES20.glGetUniformLocation(mProgram, "sLightTexture");  
        uShadowTexHandle=GLES20.glGetUniformLocation(mProgram, "sShadowTexture");
	}
	
	public void drawSelf(int texLightId,int texShadowId)
	{
		//��w�ϥάY�M�ۦ⾹�{��
   	 	GLES20.glUseProgram(mProgram);
        //�N�̲��ܴ��x�}�ǤJ�ۦ⾹�{��
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        //�N��m�B�����ܴ��x�}�ǤJ�ۦ⾹�{��
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
        //�N������m�ǤJ�ۦ⾹�{��   
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.getLightPositionBuffer());
        //�N��v����m�ǤJ�ۦ⾹�{��   
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.getCameraPosBuffer());
        // �N���I��m��ƶǤJ�ۦ�޽u
        GLES20.glVertexAttribPointer  
        (
        	maPositionHandle,   
        	3, 
        	GLES20.GL_FLOAT, 
        	false,
            3*4,   
            vertexBuffer[0][9]
        );       
        //�N���I�k�V�q��ƶǤJ�ۦ�޽u
        GLES20.glVertexAttribPointer  
        (
       		maNormalHandle, 
       		3,   
       		GLES20.GL_FLOAT, 
       		false,
         	3*4,   
         	normalBuffer[0][3]
        );   
        //���e�����w���I���z�y�и��
        GLES20.glVertexAttribPointer  
        (
       		maTexCoorHandle, 
       		2,
        	GLES20.GL_FLOAT, 
        	false,
        	2*4,   
        	texCoorBuffer[0][0]
        );
        //�ҥγ��I��m�B�k�V�q�B���z�y�и��
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maNormalHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
        //�j�w���z
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texLightId);
        //�j�w���z
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texShadowId);
        GLES20.glUniform1i(uLightTexHandle, 0);
        GLES20.glUniform1i(uShadowTexHandle, 1);
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount[0][0]);
	}
}
