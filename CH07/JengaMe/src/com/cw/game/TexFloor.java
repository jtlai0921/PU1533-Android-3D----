package com.cw.game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.cw.util.MatrixState;

import javax.vecmath.Vector3f;

public class TexFloor {
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
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R  
	FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R
	private FloatBuffer   mTextureBuffer;//���I�ۦ��ƽw�R
	int uTexHandle;//�~�[���z�ݩʰѦ�id
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id 
    int vCount;
    float yOffset;
    
    public TexFloor(int mProgram,final float UNIT_SIZE, float yOffset,CollisionShape groundShape,DiscreteDynamicsWorld dynamicsWorld)
    {
    	this.mProgram=mProgram;
    	this.yOffset=yOffset;
		//�إ߭��骺�_�l�ܴ�����
		Transform groundTransform = new Transform();
		groundTransform.setIdentity();
		groundTransform.origin.set(new Vector3f(0.f, yOffset, 0.f));		
		Vector3f localInertia = new Vector3f(0, 0, 0);//�D��		
		//�إ߭��骺�B�ʪ��A����
		DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
		//�إ߭���T������
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(0, myMotionState, groundShape, localInertia);
		//�إ߭���
		RigidBody body = new RigidBody(rbInfo);
		//�]�w�ϼu�t��
		body.setRestitution(0.4f);
		//�]�w�����t��
		body.setFriction(0.8f);
		//�N����[�J�i����@��
		dynamicsWorld.addRigidBody(body);
		initVertexData(UNIT_SIZE);
		initShader(mProgram);
    }
    public TexFloor(int mProgram,final float UNIT_SIZE)
    {
    	this.mProgram=mProgram;
		initVertexData(UNIT_SIZE);
		initShader(mProgram);
    }
    public void initVertexData(final float UNIT_SIZE){
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=6;
        float vertices[]=new float[]
        {        	
        	1*UNIT_SIZE,yOffset,1*UNIT_SIZE,
        	-1*UNIT_SIZE,yOffset,-1*UNIT_SIZE,
        	-1*UNIT_SIZE,yOffset,1*UNIT_SIZE,
        	
        	1*UNIT_SIZE,yOffset,1*UNIT_SIZE,
        	1*UNIT_SIZE,yOffset,-1*UNIT_SIZE,
        	-1*UNIT_SIZE,yOffset,-1*UNIT_SIZE,        	
        };
		
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        //���I���z��ƪ��_�l��================begin============================
        float textures[]=new float[]
        {
        	UNIT_SIZE/2,UNIT_SIZE/2,  0,0,  0,UNIT_SIZE/2,
        	UNIT_SIZE/2,UNIT_SIZE/2,  UNIT_SIZE/2,0,  0,0
        };

        //�إ߳��I���z��ƽw�R
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTextureBuffer= tbb.asFloatBuffer();//�ରFloat���w�R
        mTextureBuffer.put(textures);//�V�w�R�Ϥ���J���I�ۦ���
        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I���z��ƪ��_�l��================end============================
        //���I�k�V�q���
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
    public void initShader(int mProgram)
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
                mTextureBuffer
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
         //�e�\���I��m��ư}�C
         GLES20.glEnableVertexAttribArray(maPositionHandle);
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);
         GLES20.glEnableVertexAttribArray(maNormalHandle);  
         //�j�w���z
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         //ø��T����
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
