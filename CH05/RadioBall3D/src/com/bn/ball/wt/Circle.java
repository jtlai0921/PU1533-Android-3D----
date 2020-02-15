package com.bn.ball.wt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
public class Circle {
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R
	int YuanId;
    int vCount=0; 
    float yAngle=0;//¶y�b���઺����
    
    public Circle(float scale,float r,int n,int yuanId)
    {
    	this.YuanId=yuanId;
    	//�I�s�_�l�Ƴ��I��ƪ�initVertexData��k
    	initVertexData(scale,r,n);
    }
    public void initVertexData(
    		float scale,	//�j�p
    		float r,		//�b�|
    		int n)		//����������
    {
    	r=r*scale;
		float angdegSpan=360.0f/n;	//�������׼�
		vCount=3*n;//���I�ӼơA�@��n�ӤT���ΡA�C�ӤT���γ����T�ӳ��I
		
		float[] vertices=new float[vCount*3];//�y�и��
		float[] textures=new float[vCount*2];//���I���zS�BT�y�ЭȰ}�C
		//�y�и�ư_�l��
		int count=0;
		int stCount=0;
		for(float angdeg=0;Math.ceil(angdeg)<360;angdeg+=angdegSpan)
		{
			double angrad=Math.toRadians(angdeg);//�ثe����
			double angradNext=Math.toRadians(angdeg+angdegSpan);//�U�@����
			//�����I
			vertices[count++]=0;//���I�y��
			vertices[count++]=0; 
			vertices[count++]=0;
			
			textures[stCount++]=0.5f;//st�y��
			textures[stCount++]=0.5f;
			//�ثe�I
			vertices[count++]=(float) (-r*Math.sin(angrad));//���I�y��
			vertices[count++]=(float) (r*Math.cos(angrad));
			vertices[count++]=0;
			
			textures[stCount++]=(float) (0.5f-0.5f*Math.sin(angrad));//st�y��
			textures[stCount++]=(float) (0.5f-0.5f*Math.cos(angrad));
			//�U�@�I
			vertices[count++]=(float) (-r*Math.sin(angradNext));//���I�y��
			vertices[count++]=(float) (r*Math.cos(angradNext));
			vertices[count++]=0;
			
			textures[stCount++]=(float) (0.5f-0.5f*Math.sin(angradNext));//st�y��
			textures[stCount++]=(float) (0.5f-0.5f*Math.cos(angradNext));
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//�إ߳��I�y�и�ƽw�R
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն��Ǭ������ʧ@�t�ζ���
        mVertexBuffer = vbb.asFloatBuffer();//�ରfloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        //���z�y�и�ư_�l��
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
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, YuanId);
    	
    	gl.glDrawArrays
    	(
    			GL10.GL_TRIANGLES, 
    			0, 
    			vCount
    	);
    }
}
