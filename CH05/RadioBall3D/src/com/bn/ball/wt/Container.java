package com.bn.ball.wt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.bn.ball.WuTiDrawer;

public class Container extends WuTiDrawer{
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
	int vCount;
	int containerId;
	
	public Container(int containerId)
	{
		final float UNIT_SIZE=1.0f;
    	this.containerId=containerId;
    	vCount=36;
	   	 float[] vertices=new float[]
	   	 {
	   			-1*UNIT_SIZE,1*UNIT_SIZE,1.5f*UNIT_SIZE, //0
				-1*UNIT_SIZE,-1*UNIT_SIZE,1.5f*UNIT_SIZE,//1
				1*UNIT_SIZE,-1*UNIT_SIZE,1.5f*UNIT_SIZE,//2
				
				-1*UNIT_SIZE,1*UNIT_SIZE,1.5f*UNIT_SIZE,//0
				1*UNIT_SIZE,-1*UNIT_SIZE,1.5f*UNIT_SIZE,//2
				1*UNIT_SIZE,1*UNIT_SIZE,1.5f*UNIT_SIZE,//3
				
				1*UNIT_SIZE,-1*UNIT_SIZE,1.5f*UNIT_SIZE,//2
				1*UNIT_SIZE,-1*UNIT_SIZE,-1.5f*UNIT_SIZE,//6
				1*UNIT_SIZE,1*UNIT_SIZE,1.5f*UNIT_SIZE,//3
				
				1*UNIT_SIZE,1*UNIT_SIZE,1.5f*UNIT_SIZE,//3
				1*UNIT_SIZE,-1*UNIT_SIZE,-1.5f*UNIT_SIZE,//6
				1*UNIT_SIZE,1*UNIT_SIZE,-1.5f*UNIT_SIZE,//7
				
				1*UNIT_SIZE,1*UNIT_SIZE,-1.5f*UNIT_SIZE,//7
				1*UNIT_SIZE,-1*UNIT_SIZE,-1.5f*UNIT_SIZE,//6
				-1*UNIT_SIZE,1*UNIT_SIZE,-1.5f*UNIT_SIZE,//4
				
				-1*UNIT_SIZE,1*UNIT_SIZE,-1.5f*UNIT_SIZE,//4
				1*UNIT_SIZE,-1*UNIT_SIZE,-1.5f*UNIT_SIZE,//6
				-1*UNIT_SIZE,-1*UNIT_SIZE,-1.5f*UNIT_SIZE,//5
				
				-1*UNIT_SIZE,1*UNIT_SIZE,-1.5f*UNIT_SIZE,//4
				-1*UNIT_SIZE,-1*UNIT_SIZE,-1.5f*UNIT_SIZE,//5
				-1*UNIT_SIZE,-1*UNIT_SIZE,1.5f*UNIT_SIZE,//1
				
				-1*UNIT_SIZE,1*UNIT_SIZE,-1.5f*UNIT_SIZE,//4
				-1*UNIT_SIZE,-1*UNIT_SIZE,1.5f*UNIT_SIZE,//1
				-1*UNIT_SIZE,1*UNIT_SIZE,1.5f*UNIT_SIZE, //0

				-1*UNIT_SIZE,1*UNIT_SIZE,-1.5f*UNIT_SIZE,//4
				-1*UNIT_SIZE,1*UNIT_SIZE,1.5f*UNIT_SIZE, //0
				1*UNIT_SIZE,1*UNIT_SIZE,-1.5f*UNIT_SIZE,//7
				
				1*UNIT_SIZE,1*UNIT_SIZE,-1.5f*UNIT_SIZE,//7
				-1*UNIT_SIZE,1*UNIT_SIZE,1.5f*UNIT_SIZE, //0
				1*UNIT_SIZE,1*UNIT_SIZE,1.5f*UNIT_SIZE,//3
				
				-1*UNIT_SIZE,-1*UNIT_SIZE,1.5f*UNIT_SIZE,//1
				-1*UNIT_SIZE,-1*UNIT_SIZE,-1.5f*UNIT_SIZE,//5
				1*UNIT_SIZE,-1*UNIT_SIZE,-1.5f*UNIT_SIZE,//6
				
				-1*UNIT_SIZE,-1*UNIT_SIZE,1.5f*UNIT_SIZE,//1
				1*UNIT_SIZE,-1*UNIT_SIZE,-1.5f*UNIT_SIZE,//6
				1*UNIT_SIZE,-1*UNIT_SIZE,1.5f*UNIT_SIZE//2
	   	 };
	   //�إ߳��I�y�и�ƽw�R
	        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
	        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
	        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m     
	        
	        float textureCoors[]=new float[]
	        {       
	        		0.031f,0.031f,
	        		0.031f,0.49f,
		            0.492f,0.49f,
		            
		            0.031f,0.031f,
	        		0.492f,0.49f,
	        		0.492f,0.031f,//�k
	        		
	        		0.517f,0.031f,
	        		0.517f,0.471f,
	        		0.956f,0.471f,
	        		
	        		0.517f,0.031f,
	        		0.956f,0.471f,
		            0.956f,0.031f,//�e                               				
      				
	        		0.517f,0.031f,
	        		0.517f,0.471f,
	        		0.956f,0.471f,
	        		
	        		0.517f,0.031f,
	        		0.956f,0.471f,
		            0.956f,0.031f,//��
		                               				
		            0.031f,0.031f,
	        		0.031f,0.49f,
		            0.492f,0.49f,
		            
		            0.031f,0.031f,
	        		0.492f,0.49f,
	        		0.492f,0.031f,//��
		                               				
	        		0.517f,0.031f,
	        		0.517f,0.471f,
	        		0.956f,0.471f,
	        		
	        		0.517f,0.031f,
	        		0.956f,0.471f,
		            0.956f,0.031f,//�W
		                              		            
		            0.517f,0.031f,
	        		0.517f,0.471f,
	        		0.956f,0.471f,
	        		
	        		0.517f,0.031f,
	        		0.956f,0.471f,
		            0.956f,0.031f //�U                               		
	          };
	        ByteBuffer cbb = ByteBuffer.allocateDirect(textureCoors.length*4);
	        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        mTextureBuffer = cbb.asFloatBuffer();//�ରint���w�R
	        mTextureBuffer.put(textureCoors);//�V�w�R�Ϥ���J���I�ۦ���
	        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
	}
	@Override
	public void drawSelf(GL10 gl)
    {        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
//        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, containerId);		
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		
        		0, 			 			
        		vCount				
        );
    }
	
	
}
