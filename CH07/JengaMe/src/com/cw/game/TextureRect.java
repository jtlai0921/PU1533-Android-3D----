package com.cw.game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.cw.util.MatrixState;

import android.opengl.GLES20;

//���z�x��
public class TextureRect 
{	

	int mProgram;//�ۭq�ۦ�޽u�ۦ⾹�{��id  
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�
    int muMMatrixHandle;//��m�B�����ܴ��x�}
    int maPositionHandle; //���I��m�ݩʰѦ�  
    int maNormalHandle; //���I�k�V�q�ݩʰѦ�  
    int maLightLocationHandle;//������m�ݩʰѦ�  
    int maCameraHandle; //��v����m�ݩʰѦ� 
    int muIsShadow;//�O�_ø��v�ݩʰѦ�
    int muProjCameraMatrixHandle;//��v�B��v���s�զX�x�}�Ѧ�
    String mVertexShader;//���I�ۦ⾹�{���X���O�Z    	 
    String mFragmentShader;//�����ۦ⾹�{���X���O�Z    
	FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R

	int uTexHandle;//�~�[���z�ݩʰѦ�id
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id 
    
    
    
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R
    int vCount=0;   
    float wid;
    float hei;
    float zf;
    public TextureRect(int mProgram,MySurfaceView mv,float width,float heigth,float zOffset)//50,40,0
    {    	
    	this.wid=width;
    	this.hei=heigth;
    	this.zf=zOffset;
    	//�_�l�Ƴ��I�y�лP�ۦ���
    	initVertexData();
    	//�_�l��shader        
    	initShader(mv,mProgram);
    }
    
    //�_�l�Ƴ��I�y�лP�ۦ��ƪ���k
    public void initVertexData()
    {
    	//���I�y�и�ƪ��_�l��
        vCount=6;
        float vertices[]=new float[]
        {
        	-wid,hei,zf,
        	-wid,-hei,zf,
        	wid,-hei,zf,
        	  
        	wid,-hei,zf,
        	wid,hei,zf,
        	-wid,hei,zf
        };
		
        //�إ߳��I�y�и�ƽw�R
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        //���I���z�y�и�ƪ��_�l��
        float texCoor[]=new float[]//���I�m��Ȱ}�C�A�C�ӳ��I4�Ӧ�m��RGBA
        {
        		0,0, 0,1, 1,1,
        		1,1, 1,0, 0,0        		
        };        
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = cbb.asFloatBuffer();//�ରFloat���w�R
        mTexCoorBuffer.put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        float mNormals[]=new float[]
                {
                	 0,1,0,0,1,0,0,1,0,
                	 0,1,0,0,1,0,0,1,0,
                };

                //�إ߳��I���z��ƽw�Rb
                ByteBuffer mnbb = ByteBuffer.allocateDirect(mNormals.length*4);
                mnbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
                mNormalBuffer= mnbb.asFloatBuffer();//�ରFloat���w�R
                mNormalBuffer.put(mNormals);//�V�w�R�Ϥ���J���I�ۦ���
                mNormalBuffer.position(0);//�]�w�w�R�ϰ_�l��m
    }

    //�_�l��shader
    public void initShader(MySurfaceView mv,int mProgram)
    {
            this.mProgram=mProgram;
	        maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");  
	        uTexHandle=GLES20.glGetUniformLocation(mProgram, "sTexture");
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
	        //���o�{������v����m�Ѧ�
	        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera");  
	        //���o�{�����O�_ø��v�ݩʰѦ�
	        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
	        //���o�{������v�B��v���s�զX�x�}�Ѧ�
	        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
 
    
    }
    
    public void drawSelf(int texId)
    {        
    	GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
        //�N��v����m�ǤJ�ۦ⾹�{��   
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        //�N�O�_ø��v�ݩʶǤJ�ۦ⾹�{�� 
        GLES20.glUniform1i(muIsShadow, 0);       
        //�N��v�B��v���s�զX�x�}�ǤJ�ۦ⾹�{��
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0); 
        GLES20.glUniform1i(uTexHandle, 0);
         
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //�ǿ鳻�I���z�y�и��
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
         		maNormalHandle, 
         		3,   
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         );
         //�e�\���I��m�B���z�y�и�ư}�C
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);
         GLES20.glEnableVertexAttribArray(maNormalHandle);  
         //�j�w���z
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //ø��z�x��
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}