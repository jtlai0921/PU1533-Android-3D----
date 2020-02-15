package com.bn.ball.cube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.bn.ball.MySurfaceView;
public class Rectangular 
{
	
	private FloatBuffer mVertexBuffer;//���I�y�и�ƽw�R
	private FloatBuffer mTextureBuffer;
    int vCount=0;	
    float UNIT_SIZE=0.3f;
    int wallId;
    public Rectangular(MySurfaceView mv,int wallId)
    {
    	//�_�l�Ƴ��I�y�лP�ۦ���
    	initVertexData();
    	this.wallId=wallId;
    	
    }
    
  //�_�l�Ƴ��I�y�лP�ۦ��ƪ���k
    public void initVertexData()
    {
    	//���I�y�и�ƪ��_�l��================begin============================
        float vertices[]=new float[]
	    {
        		-UNIT_SIZE,UNIT_SIZE,0,
				-UNIT_SIZE,-UNIT_SIZE,0,
				UNIT_SIZE,-UNIT_SIZE,0,
				
				-UNIT_SIZE,UNIT_SIZE,0,
				UNIT_SIZE,-UNIT_SIZE,0,
				UNIT_SIZE,UNIT_SIZE,0
	     };
        
        vCount=vertices.length/3;
		
        //�إ߳��I�y�и�ƽw�R
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        //�_�l�Ʊm��}�C
        float textureCoors[]=new float[]
    	        {
    	            0,0,
    	            0,1,
    	            1,1,
    	                               				
    	            0,0,
    	            1,1,
    	            1,0,              		
    	          };
    	        ByteBuffer cbb = ByteBuffer.allocateDirect(textureCoors.length*4);
    	        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
    	        mTextureBuffer = cbb.asFloatBuffer();//�ରint���w�R
    	        mTextureBuffer.put(textureCoors);//�V�w�R�Ϥ���J���I�ۦ���
    	        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
    }
    
    public void drawSelf(GL10 gl)
    {        
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        
		//���e�����w���I�y�и��
        gl.glVertexPointer
        (
        		3,				
        		GL10.GL_FLOAT,
        		0, 				
        		mVertexBuffer	
        );    
        //�}�ү��z
        gl.glEnable(GL10.GL_TEXTURE_2D);   
        //�e�\�ϥί��zST�y�нw�R
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //���e�����w���zST�y�нw�R
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        //�j�w�ثe���z
        gl.glBindTexture(GL10.GL_TEXTURE_2D, wallId);		
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		
        		0, 			 			
        		vCount				
        );
    }
}
