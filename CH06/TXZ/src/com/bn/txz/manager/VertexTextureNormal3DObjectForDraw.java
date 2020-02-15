package com.bn.txz.manager;

import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//���J�᪺����X�X��a���I�B�k�V�q�B���z�T��
public class VertexTextureNormal3DObjectForDraw 
{
	public FloatBuffer mVertexBuffer; //���I�y�и�ƽw�R
	public FloatBuffer mTexBuffer; //���I���z�y�нw�R
	public FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R
	public int vCount = 0; //���I��
	int texId;
	
    public VertexTextureNormal3DObjectForDraw(FloatBuffer mVertexBuffer,FloatBuffer mTexBuffer,FloatBuffer mNormalBuffer,int vCount,int texId) 
    {    	
    	this.mVertexBuffer=mVertexBuffer;
		this.mTexBuffer=mTexBuffer;  
		this.mNormalBuffer=mNormalBuffer;
		this.vCount=vCount;
		this.texId=texId;
    }
    
    public void drawSelf(GL10 gl)
    {        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);//�ҥγ��I�k�V�q�}�C     
        gl.glEnable(GL10.GL_TEXTURE_2D);    //�}�ү��z      
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�e�\�ϥί��zST�y�нw�R

        
		//���e�����w���I�y�и��
        gl.glVertexPointer
        (
        		3,				//�C�ӳ��I���y�мƶq��3  xyz 
        		GL10.GL_FLOAT,	//���I�y�ЭȪ����A�� GL_FIXED
        		0, 				//�s���I�y�и�Ƥ��������j
        		mVertexBuffer	//���I�y�и��
        ); 
        
        //���e�����w���I�k�V�q���
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        
        //���e�����w���zST�y�нw�R
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        //�j�w�ثe���z
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//�H�T���μҦ���R
        		0, 			 			//�}�l�I�s��
        		vCount					//���I���ƶq
        );        
        
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);//�T�γ��I�k�V�q�}�C
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�e�\�ϥί��zST�y�нw�R
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
    
    public VertexTextureNormal3DObjectForDraw(FloatBuffer mVertexBuffer,FloatBuffer mTexBuffer,FloatBuffer mNormalBuffer,int vCount) 
    {    	
    	this.mVertexBuffer=mVertexBuffer;
		this.mTexBuffer=mTexBuffer;  
		this.mNormalBuffer=mNormalBuffer;
		this.vCount=vCount;
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
