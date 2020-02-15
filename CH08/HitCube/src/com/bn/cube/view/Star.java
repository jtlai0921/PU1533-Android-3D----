package com.bn.cube.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class Star {
	
		public FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	    public int vCount=0;//�P�P�ƶq
	    public float scale;//�P�P�ؤo  
	    String mVertexShader;//���I�ۦ⾹    	 
	    String mFragmentShader;//�����ۦ⾹
	    int mProgram;//�ۭq�ۦ�ް������id 
	    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id   
	    int maPositionHandle; //���I��m�ݩʰѦ�id  
	    int uPointSizeHandle;//���I�ؤo�ѼưѦ�
	    public float width;
	    public float height;
	    public Star(float width,float height,MyGLSurfaceView mv){  
	    	this.width=width;
	    	this.height=height;
	    	initVertexData(width,height);
	    	intShader(mv);
	    }
	    public void initVertexData(float width,float height){ //�_�l�Ƴ��I�y�Ъ���k    	  	
	    	//���I�y�и�ƪ��_�l��       
	    	vCount=(int) (Math.random()*30f);
	        float vertices[]=new float[vCount*3];
	        for(int i=0;i<vCount;i++){ 
	        	//�H�����ͨC�ӬP�P��xyz�y��
	        	double tempX=(Math.random()-0.5f);
	        	double tempY=(Math.random()-0.5f);
	        	vertices[3*i]=(float) (width*tempX*2);
	        	vertices[3*i+1]=(float) (height*tempY*2);
	        	vertices[3*i+2]=0f;        
	        }
	    	scale=(float) (Math.random()*10f);
	        //�إ߳��I�y�и�ƽw�R
	        ByteBuffer vbb = ByteBuffer.allocateDirect(90*4);
	        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
	        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
	        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
	      
	    }
	    public void intShader(MyGLSurfaceView mv){    //�_�l�Ƶۦ⾹
	    	//���J���I�ۦ⾹�����O�Z���e       
	        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_xk.sh", mv.getResources());
	        ShaderUtil.checkGlError("==ss==");   
	        //���J�����ۦ⾹�����O�Z���e
	        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_xk.sh", mv.getResources());  
	        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
	        ShaderUtil.checkGlError("==ss==");      
	        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
	        //���o�{�������I��m�ݩʰѦ�id  
	        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");        
	        //���o�{�����`�ܴ��x�}�Ѧ�id
	        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
	        //���o���I�ؤo�ѼưѦ�
	        uPointSizeHandle = GLES20.glGetUniformLocation(mProgram, "uPointSize"); 
	    }
	    public void drawSelf(){  
	 
	   	    GLES20.glUseProgram(mProgram); //��w�ϥάY�M�ۦ⾹�{��
	        //�N�̲��ܴ��x�}�ǤJ�ۦ⾹�{��
	        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);  
	        GLES20.glUniform1f(uPointSizeHandle, scale);  //�N���I�ؤo�ǤJ�ۦ⾹�{��
	        GLES20.glVertexAttribPointer( //���e�����w���I��m���    
	        		maPositionHandle,   
	        		3, 
	        		GLES20.GL_FLOAT, 
	        		false,
	                3*4, 
	                mVertexBuffer   
	        );   
	        //�e�\���I��m��ư}�C
	        GLES20.glEnableVertexAttribArray(maPositionHandle);         
	        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vCount); //ø��P�P�I    
	}


}
