package com.bn.GL20Tail;
 


import java.nio.FloatBuffer;  

import com.bn.cube.core.MatrixState;
import com.bn.cube.game.MySurfaceView;

import android.opengl.GLES20;

//���z�T����
public class DrawBuffer 
{	
	int mProgram;//�ۭq�ۦ�ް������id
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id  
    int maAlphaHandle;
    String mVertexShader;//���I�ۦ⾹    	 
    String mFragmentShader;//�����ۦ⾹
    
    public DrawBuffer(MySurfaceView mv)
    {
    	//�_�l�Ƶۦ⾹        
    	initShader(mv);
    }  

    //�_�l�Ƶۦ⾹
    public void initShader(MySurfaceView mv)
    {
    	//���J���I�ۦ⾹�����O�Z���e
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //���J�����ۦ⾹�����O�Z���e
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        maAlphaHandle=GLES20.glGetAttribLocation(mProgram, "aAlpha");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
    
    public void drawSelf(int texId,FloatBuffer   mVertexBuffer,FloatBuffer mTexCoorBuffer,FloatBuffer mAlphaBuffer,int vCount)
    {        
    	 //��w�ϥάY�Mshader�{��
    	 GLES20.glUseProgram(mProgram);  

         //�N�̲��ܴ��x�}�ǤJshader�{��
    	 MatrixState.copyMVMatrix();
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //���e�����w���I��m���
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //���e�����w���I���z�y�и��
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );   
         
         GLES20.glVertexAttribPointer  
         (
        		maAlphaHandle, 
         		1, 
         		GLES20.GL_FLOAT, 
         		false,
                1*4,   
                mAlphaBuffer
         );   
         
         //�e�\���I��m��ư}�C
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         GLES20.glEnableVertexAttribArray(maAlphaHandle);  
         
         //�j�w���z
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //ø��z�x��
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}
