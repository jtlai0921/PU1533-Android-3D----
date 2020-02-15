package com.bn.cube.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class Line {
	
	int mProgram;//�ۭq�ۦ�޽u�ۦ⾹�{��id
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maColorHandle; //���I���z�y���ݩʰѦ�id 
    
    FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mColorBuffer;//���I���z�y�и�ƽw�R
    
    int vCount;//���I�Ӽ�
    
    float UNIT_SIZE=1.0f;
	public Line(MySurfaceView mv,float []color,float width)
	{
		initVertexData(color,width);
		initShader(mv);
	}
	public void initVertexData(float []color,float width)
	{
		vCount=2;
		float  vertices[]=new float[]{
				-UNIT_SIZE*width,0,0,
				UNIT_SIZE*width,0,0
		};
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.clear();
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        float []colors=new float[vCount*4];
        for(int i=0;i<vCount;i++)
        {
        	colors[4*i]=color[0];
        	colors[4*i+1]=color[1];
        	colors[4*i+2]=color[2];
        	colors[4*i+3]=color[3];
        }
        			
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());// �]�w�줸�ն���
		mColorBuffer = cbb.asFloatBuffer();// �ରFloat���w�R
		mColorBuffer.put(colors);// �V�w�R�Ϥ���J���I�ۦ���
		mColorBuffer.position(0);// �]�w�w�R�ϰ_�l��m
		
	}
	public void initShader(MySurfaceView mv){
		
		String  mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_cube.sh", mv.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //���J�����ۦ⾹�����O�Z���e
        String  mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_cube.sh", mv.getResources());  
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        ShaderUtil.checkGlError("==ss==");      
        mProgram =ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�id  
        maColorHandle= GLES20.glGetAttribLocation(mProgram, "aColor");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
	}
	public void drawSelf(){
		
		 //��w�ϥάY�Mshader�{��
	   	 GLES20.glUseProgram(mProgram);        
	   	 //�_�l���ܴ��x�}
	     //�N�̲��ܴ��x�}�ǤJshader�{��
	     GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
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
	       		maColorHandle, 
	        		4, 
	        		GLES20.GL_FLOAT, 
	        		false,
	                4*4,   
	               mColorBuffer
	        );   
	        //�e�\���I��m��ư}�C
	        GLES20.glEnableVertexAttribArray(maPositionHandle);  
	        GLES20.glEnableVertexAttribArray(maColorHandle);  
	        GLES20.glLineWidth(1f);
	        //ø��z�x��
	        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vCount); 
		
	}

}
