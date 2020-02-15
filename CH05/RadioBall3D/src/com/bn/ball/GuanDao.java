package com.bn.ball;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;


public class GuanDao {
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R
    int vCount=0;
    
    MySurfaceView mv;
    public GuanDao(MySurfaceView mv,float height)
    {    
    	this.mv=mv;
    	//�_�l�Ƴ��I�y�и�ƪ���k
    	initVertexData(height);
    	//�_�l�Ƶۦ⾹�{������k
    }
    
    //�_�l�Ƴ��I�y�и�ƪ���k
    public void initVertexData(float height)
    {
    	//���I�y�и�ƪ��_�l��================begin============================
        ArrayList<Float> alVertix=new ArrayList<Float>();
        ArrayList<Float> alTexCoor=new ArrayList<Float>();
       
        int t=0;
        for(float l=0;l>-Constant.GUANDAO_L;l--)
        {
        	 int s=0;
//        	
        	 for(float angle=-40;angle<220;angle+=Constant.angleSpan)
        	 {
        		 float angrad=(float) Math.toRadians(angle);//�ثe���� 
     			 float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//�U�@����
        		 float x1=(float) (height*Math.cos(angrad));   
        		 float y1=(float) (height*Math.sin(angrad));
        		 float z1=l;
        		 
        		 float x2=(float) (height*Math.cos(angradNext));
        		 float y2=(float) (height*Math.sin(angradNext));
        		 float z2=l;
        		 
        		 float x3=(float) (height*Math.cos(angradNext));
        		 float y3=(float) (height*Math.sin(angradNext));
        		 float z3=l-1;
        		 
        		 float x4=(float) (height*Math.cos(angrad));
        		 float y4=(float) (height*Math.sin(angrad));
        		 float z4=l-1;
        		 
        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.077f*(s));alTexCoor.add(0.05f*t);
        		
        		 alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		 alTexCoor.add(0.077f*(s+1));alTexCoor.add(0.05f*t);
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.077f*(s+1));alTexCoor.add(0.05f*(t+1));
        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.077f*(s));alTexCoor.add(0.05f*t);
        		
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.077f*(s+1));alTexCoor.add(0.05f*(t+1));
        		 alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		 alTexCoor.add(0.077f*(s));alTexCoor.add(0.05f*(t+1));
        		 s++;
        	 }      	
        	 t++;
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
    public void drawSelf(GL10 gl)
    { 
    	
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);           	
		gl.glVertexPointer
		(
				3, 
				GL10.GL_FLOAT,
				0,
				mVertexBuffer
	   );
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoorBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D,mv.textureID);
			

			gl.glDrawArrays
			(
					GL10.GL_TRIANGLES,
					0, 
					vCount
			);
    }
}
