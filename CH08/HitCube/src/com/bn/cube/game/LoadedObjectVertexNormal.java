package com.bn.cube.game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

//���J�᪺����X�X��a���I�T���A�۰ʭp�⭱�����k�V�q
public class LoadedObjectVertexNormal
{	
	int mProgram;//�ۭq�ۦ�޽u�ۦ⾹�{��id  
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�
    int muMMatrixHandle;//��m�B�����ܴ��x�}
    int maPositionHandle; //���I��m�ݩʰѦ�  
    int maNormalHandle; //���I�k�V�q�ݩʰѦ�  
    int maLightLocationHandle;//������m�ݩʰѦ�  
    int maCameraHandle; //��v����m�ݩʰѦ� 
    int muColorIdHandle;
    int muDaoJuAHandle;
    int muDaoJuALevelHandle;
    String mVertexShader;//���I�ۦ⾹�{���X���O�Z    	 
    String mFragmentShader;//�����ۦ⾹�{���X���O�Z    
	
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R  
	FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R
    int vCount=0;  
    
    public LoadedObjectVertexNormal(MySurfaceView mv,float[] vertices,float[] normals)
    {    	
    	//�_�l�Ƴ��I�y�лP�ۦ���
    	initVertexData(vertices,normals);
    	//�_�l��shader        
    	initShader(mv);
    }
    
    //�_�l�Ƴ��I�y�лP�ۦ��ƪ���k
    public void initVertexData(float[] vertices,float[] normals)
    {
    	//���I�y�и�ƪ��_�l��================begin============================
    	vCount=vertices.length/3;   
		
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        
        //���I�k�V�q��ƪ��_�l��================begin============================  
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mNormalBuffer = cbb.asFloatBuffer();//�ରFloat���w�R
        mNormalBuffer.put(normals);//�V�w�R�Ϥ���J���I�k�V�q���
        mNormalBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�ۦ��ƪ��_�l��================end============================
    }

    //�_�l��shader
    public void initShader(MySurfaceView mv)
    {
    	//���J���I�ۦ⾹�����O�Z���e
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_daoju.sh", mv.getResources());
        //���J�����ۦ⾹�����O�Z���e
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_daoju.sh", mv.getResources());  
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
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
        muColorIdHandle=GLES20.glGetUniformLocation(mProgram, "uColorId");
        muDaoJuAHandle=GLES20.glGetUniformLocation(mProgram, "uDaoJuA");
        muDaoJuALevelHandle=GLES20.glGetUniformLocation(mProgram, "uDaoJuALevel");
    }
    
    public void drawSelf()
    {        
    	 //��w�ϥάY�M�ۦ⾹�{��
    	 GLES20.glUseProgram(mProgram);
         //�N�̲��ܴ��x�}�ǤJ�ۦ⾹�{��
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //�N��m�B�����ܴ��x�}�ǤJ�ۦ⾹�{��
         GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
         GLES20.glUniform1f(muColorIdHandle, Constant.daojuId);
         GLES20.glUniform1f(muDaoJuAHandle, Constant.daojuACount);
         GLES20.glUniform1f(muDaoJuALevelHandle, Constant.daojuAlevel);
         //�N������m�ǤJ�ۦ⾹�{��   
         GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
         //�N��v����m�ǤJ�ۦ⾹�{��   
         GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
         // �N���I��m��ƶǤJ�ۦ�޽u
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //�N���I�k�V�q��ƶǤJ�ۦ�޽u
         GLES20.glVertexAttribPointer  
         (
        		maNormalHandle, 
         		3,   
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         );   
         //�ҥγ��I��m�B�k�V�q���
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maNormalHandle);  
         //ø����J������
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}
