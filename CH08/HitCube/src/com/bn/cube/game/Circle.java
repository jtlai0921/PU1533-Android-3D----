
package com.bn.cube.game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class Circle{

	int mProgram;//�ۭq�ۦ�ް������id 
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id  
    int maPositionHandle; //���I��m�ݩʰѦ�id   
    int  maColorHandle;
    String mVertexShader;//���I�ۦ⾹    	 
    String mFragmentShader;//�����ۦ⾹
	
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mColorBuffer;//���I�y�и�ƽw�R
	
    int vCount=0; 
    float lineWidth=2;
    public Circle(MySurfaceView mv,float lineWidth,float []color)
    {
    	this.lineWidth=lineWidth;
    	initVertexData(color);
    	initShader(mv);
    }
    public void initVertexData(float []color)
    {
    	//���I�y�и�ƪ��_�l��================begin============================    	
    	float width=4.4f;
    	float height=2.9f;
    	ArrayList<Float> alfVertix=new ArrayList<Float>();
    	float x1=width;
    	float y1=height;
    	float z1=0;
    	
    	float x2=-width;
    	float y2=height;
    	float z2=0; 
    	
    	float x3=-width;
    	float y3=-height;
    	float z3=0;
    	
    	float x4=width;
    	float y4=-height;
    	float z4=0;
		alfVertix.add(x1);alfVertix.add(y1);alfVertix.add(z1);
		alfVertix.add(x2);alfVertix.add(y2);alfVertix.add(z2);
		alfVertix.add(x3);alfVertix.add(y3);alfVertix.add(z3);
		alfVertix.add(x4);alfVertix.add(y4);alfVertix.add(z4);
		for(float angle=90;angle<270;angle+=Constant.angleSpan)
		{
   		 	float angrad=(float) Math.toRadians(angle);//�ثe���� 
			float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//�U�@����
   		 	float X1=(float) (-width+height*Math.cos(angrad));   
   		 	float Y1=(float) (height*Math.sin(angrad));
   		 	float Z1=0;
   		 
   		 	float X2=(float) (-width+height*Math.cos(angradNext));
   		 	float Y2=(float) (height*Math.sin(angradNext));
   		 	float Z2=0;
			alfVertix.add(X1);alfVertix.add(Y1);alfVertix.add(Z1);
			alfVertix.add(X2);alfVertix.add(Y2);alfVertix.add(Z2);
		}
		for(float angle=-90;angle<90;angle+=Constant.angleSpan)
		{
   		 	float angrad=(float) Math.toRadians(angle);//�ثe���� 
			float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//�U�@����
   		 	float X1=(float) (width+height*Math.cos(angrad));   
   		 	float Y1=(float) (height*Math.sin(angrad));
   		 	float Z1=0;
   		 
   		 	float X2=(float) (width+height*Math.cos(angradNext));
   		 	float Y2=(float) (height*Math.sin(angradNext));
   		 	float Z2=0;
			alfVertix.add(X1);alfVertix.add(Y1);alfVertix.add(Z1);
			alfVertix.add(X2);alfVertix.add(Y2);alfVertix.add(Z2);
		}
 
    	 vCount=alfVertix.size()/3;
    	//�NalVertix�����y�Э���s��@��float�}�C��
         float vertices[]=new float[vCount*3];
     	for(int i=0;i<alfVertix.size();i++)
     	{
     		vertices[i]=alfVertix.get(i);
     	}	
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
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
    //�_�l��shader
    public void initShader(MySurfaceView mv)
    {
    	//���J���I�ۦ⾹�����O�Z���e       
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_cube.sh", mv.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //���J�����ۦ⾹�����O�Z���e
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_cube.sh", mv.getResources());  
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        ShaderUtil.checkGlError("==ss==");      
        mProgram =ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition"); 
         maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");   
    }
    
    public void drawSelf() 
    {        
    	 //��w�ϥάY�Mshader�{��
    	 GLES20.glUseProgram(mProgram);
         //�N�̲��ܴ��x�}�ǤJshader�{��
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
         GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false,
 				4 * 4, mColorBuffer);
        
         //�e�\���I��m��ư}�C
         GLES20.glEnableVertexAttribArray(maPositionHandle);     
         GLES20.glEnableVertexAttribArray(maColorHandle);
         GLES20.glLineWidth(lineWidth);//�]�w�u���e��
         
         //ø��u��
         GLES20.glDrawArrays(GLES20.GL_LINES, 0, vCount); 
    }
    
   
}
