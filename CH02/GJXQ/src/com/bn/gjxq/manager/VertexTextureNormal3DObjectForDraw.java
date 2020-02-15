package com.bn.gjxq.manager;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//���J�᪺����X�X��a���I�B�k�V�q�B���z�T��
public class VertexTextureNormal3DObjectForDraw 
{
	public FloatBuffer mVertexBuffer; //���I�y�и�ƽw�R
	public FloatBuffer mTexBuffer; //���I���z�y�нw�R
	public FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R
	public int vCount = 0; //���I��
	public AABB3 aabb;//�]��
	
      
    public VertexTextureNormal3DObjectForDraw(FloatBuffer mVertexBuffer,FloatBuffer mTexBuffer,FloatBuffer mNormalBuffer,int vCount,AABB3 aabb) 
    {    	
    	this.mVertexBuffer=mVertexBuffer;
		this.mTexBuffer=mTexBuffer;  
		this.mNormalBuffer=mNormalBuffer;
		this.vCount=vCount;
		this.aabb=aabb;
    }

    public void drawSelf(GL10 gl,int texId)
    {      
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�e�\�ϥγ��I�}�C
		gl.glEnable(GL10.GL_TEXTURE_2D); //�}�ү��z
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); //�e�\�ϥί��zST�y�нw�R
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);//�ҥγ��I�k�V�q�}�C
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);	//���e�����w���I�y�и��  
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);  //���e�����w���zST�y�нw�R		
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);//���e�����w���I�k�V�q���
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);   //�j�w�ثe���z
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount); //ø��ϧ�
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//�����ϥγ��I�}�C
		gl.glDisableClientState(GL10.GL_TEXTURE_2D); //�������z
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�����ϥί��zST�y�а}�C
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);//�������I�k�V�q�}�C
    }
}
