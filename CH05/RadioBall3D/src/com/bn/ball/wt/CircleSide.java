package com.bn.ball.wt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class CircleSide {
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R
	FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R
	int circleSideId;
    int vCount=0; 
    
    public CircleSide(float scale,float r,float h,int n, int circleSideId)
    {    	
    	this.circleSideId=circleSideId;
    	//�I�s�_�l�Ƴ��I��ƪ�initVertexData��k
    	initVertexData(scale,r,h,n);
    }
    
    public void initVertexData(
    		float scale,	//�j�p
    		float r,		//�b�|
    		float h,		//����
    		int n			//����������
    	)
    {
    	r=scale*r;
    	h=scale*h;
    	
		float angdegSpan=360.0f/n;
		vCount=3*n*4;//���I�ӼơA�@��3*n*4�ӤT���ΡA�C�ӤT���γ����T�ӳ��I
		//�y�и�ư_�l��
		float[] vertices=new float[vCount*3];
		float[] textures=new float[vCount*2];//���I���zS�BT�y�ЭȰ}�C
		//�y�и�ư_�l��
		int count=0;
		int stCount=0;
		for(float angdeg=0;Math.ceil(angdeg)<360;angdeg+=angdegSpan)//����
		{
			double angrad=Math.toRadians(angdeg);//�ثe����
			double angradNext=Math.toRadians(angdeg+angdegSpan);//�U�@����
			//����ثe�I---0
			vertices[count++]=(float) (-r*Math.sin(angrad));
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angrad));
			
			textures[stCount++]=(float) (angrad/(2*Math.PI));//st�y��
			textures[stCount++]=1;
			//����U�@�I---3
			vertices[count++]=(float) (-r*Math.sin(angradNext));
			vertices[count++]=h;
			vertices[count++]=(float) (-r*Math.cos(angradNext));
			
			textures[stCount++]=(float) (angradNext/(2*Math.PI));//st�y��
			textures[stCount++]=0;
			//����ثe�I---2
			vertices[count++]=(float) (-r*Math.sin(angrad));
			vertices[count++]=h;
			vertices[count++]=(float) (-r*Math.cos(angrad));
			
			textures[stCount++]=(float) (angrad/(2*Math.PI));//st�y��
			textures[stCount++]=0;
			
			//����ثe�I---0
			vertices[count++]=(float) (-r*Math.sin(angrad));
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angrad));
			
			textures[stCount++]=(float) (angrad/(2*Math.PI));//st�y��
			textures[stCount++]=1;
			//����U�@�I---1
			vertices[count++]=(float) (-r*Math.sin(angradNext));
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angradNext));
			
			textures[stCount++]=(float) (angradNext/(2*Math.PI));//st�y��
			textures[stCount++]=1;
			//����U�@�I---3
			vertices[count++]=(float) (-r*Math.sin(angradNext));
			vertices[count++]=h;
			vertices[count++]=(float) (-r*Math.cos(angradNext));
			
			textures[stCount++]=(float) (angradNext/(2*Math.PI));//st�y��
			textures[stCount++]=0;
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//�إ߳��I�y�и�ƽw�R
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն��Ǭ������ʧ@�t�ζ���
        mVertexBuffer = vbb.asFloatBuffer();//�ରfloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        //st�y�и�ư_�l��
        ByteBuffer cbb = ByteBuffer.allocateDirect(textures.length*4);//�إ߳��I���z��ƽw�R
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն��Ǭ������ʧ@�t�ζ���
        mTexCoorBuffer = cbb.asFloatBuffer();//�ରfloat���w�R
        mTexCoorBuffer.put(textures);//�V�w�R�Ϥ���J���I���z���
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
    	gl.glTexCoordPointer
    	(
    			2, 
    			GL10.GL_FLOAT, 
    			0, 
    			mTexCoorBuffer
    	);
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, circleSideId);
    	
    	gl.glDrawArrays
    	(
    			GL10.GL_TRIANGLES, 
    			0, 
    			vCount
    	);
    }
}
