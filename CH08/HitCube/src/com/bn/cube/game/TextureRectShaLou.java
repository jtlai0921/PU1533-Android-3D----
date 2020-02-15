package com.bn.cube.game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;


import android.opengl.GLES20;

//���z�x��
public class TextureRectShaLou 
{	
	int mProgram;//�ۭq�ۦ�޽u�ۦ⾹�{��id
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id 
    int muKaiShiIdHandle;//�F�|�}�l��id
    int muRHandle;//�F�|�}�l��id
    int muShaLouCountHandle;//�F�|�p�ƾ�
	
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R
    int vCount=0;
    
    public TextureRectShaLou(float width,float height,MySurfaceView mv)
    {    	
    	//�_�l�Ƴ��I�y�и�ƪ���k
    	initVertexData(width,height);
    	//�_�l�Ƶۦ⾹�{������k
    	initShader(mv);
    }
    
    //�_�l�Ƴ��I�y�и�ƪ���k
    public void initVertexData(float width,float height)
    {
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=6;
        float vertices[]=new float[]
        {
        	-width,height,0,
        	-width,-height,0,
        	width,-height,0,
        	
        	width,-height,0,
        	width,height,0,
        	-width,height,0
        };
		
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
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
    }
    //�_�l�Ƶۦ⾹�{������k
    public void initShader(MySurfaceView mv)
    {
    	//���J���I�ۦ⾹�����O�Z���e       
      String  mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_shalou.sh", mv.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //���J�����ۦ⾹�����O�Z���e
      String  mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_shalou.sh", mv.getResources());  
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        ShaderUtil.checkGlError("==ss==");      
        mProgram =ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        muKaiShiIdHandle= GLES20.glGetUniformLocation(mProgram, "uKaiShiId"); 
        muRHandle= GLES20.glGetUniformLocation(mProgram, "uR"); 
        muShaLouCountHandle= GLES20.glGetUniformLocation(mProgram, "uShaLouCount"); 
    }
    
    public void drawSelf(int texId)
    { 
    	 //��w�ϥάY�Mshader�{��
    	 GLES20.glUseProgram(mProgram);        
    	 //�_�l���ܴ��x�}
         //�N�̲��ܴ��x�}�ǤJshader�{��
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         GLES20.glUniform1f(muKaiShiIdHandle, Constant.shalouKaiId);
         GLES20.glUniform1f(muRHandle,0.1f);
         GLES20.glUniform1f(muShaLouCountHandle,Constant.shalouCount);
         //�ǤJ���I��m���
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //�ǤJ���I���z�y�и��
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );   
         //�e�\���I��m��ư}�C
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         
         //�j�w���z
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //ø��z�x��
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}
