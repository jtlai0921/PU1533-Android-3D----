package com.bn.cube.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class Water {
	int mProgram;//�ۭq�ۦ�޽u�ۦ⾹�{��id
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int muMMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id 
	
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R
    int vCount=0;
    
    MyGLSurfaceView mv;
    public Water(MyGLSurfaceView mv,float width,float height)
    {    
    	this.mv=mv;
    	//�_�l�Ƴ��I�y�и�ƪ���k
    	initVertexData(width,height);
    	//�_�l�Ƶۦ⾹�{������k
    	initShader(mv);
    }
    
    //�_�l�Ƴ��I�y�и�ƪ���k
    public void initVertexData(float width,float height)
    {
    	//���I�y�и�ƪ��_�l��================begin============================
        ArrayList<Float> alVertix=new ArrayList<Float>();
        ArrayList<Float> alTexCoor=new ArrayList<Float>();
        float sspan=1/(2*width);
        int s=0;
        for(float w=-width;w<=width;w++)
        {
        	float x1=w;float y1=height;float z1=0;
        	float x2=w;float y2=-height;float z2=0;
        	alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        	alTexCoor.add(s*sspan);alTexCoor.add(0f);
        	alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        	alTexCoor.add(s*sspan);alTexCoor.add(1f);
        	s++;
        }
        
        vCount=alVertix.size()/3;
        float vertices[]=new float[vCount*3];
     	for(int i=0;i<alVertix.size();i++) 
     	{
     		vertices[i]=alVertix.get(i);
     	}	
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        float texCoor[]=new float[vCount*2];//���I���z�}�C
        for(int i=0;i<alTexCoor.size();i++)
     	{
        	texCoor[i]=alTexCoor.get(i);
     	}	
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = cbb.asFloatBuffer();//�ରFloat���w�R
        mTexCoorBuffer.put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
    }

	//�_�l�Ƶۦ⾹�{������k
    public void initShader(MyGLSurfaceView mv)
    {
    	//���J���I�ۦ⾹�����O�Z���e       
      String  mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_water.sh", mv.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //���J�����ۦ⾹�����O�Z���e
      String  mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_water.sh", mv.getResources());  
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        ShaderUtil.checkGlError("==ss==");       
        mProgram =ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");    
    }
    
    public void drawSelf(int texId)
    { 
    	 //��w�ϥάY�Mshader�{��
    	 GLES20.glUseProgram(mProgram);        
    	 //�_�l���ܴ��x�}
         //�N�̲��ܴ��x�}�ǤJshader�{��
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0); 
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
         GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount); 
    }
}

