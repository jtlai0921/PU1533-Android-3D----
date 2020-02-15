package com.bn.ball.wt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import com.bn.ball.WuTiDrawer;

public class LiFangTi extends WuTiDrawer{
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
	int vCount;
	int lftId;
	
	public LiFangTi(int lftId)
	{
		final float UNIT_SIZE=1.0f;
    	this.lftId=lftId;
    	vCount=36;
	   	 float[] vertices=new float[]
	   	 {
	   			-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,0.8f*UNIT_SIZE, //0
				-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//1
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//2
				
				-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,
				0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//3
				
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//2
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//6
				0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//3
				
				0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//3
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//6
				0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//7
				
				0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//7
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//6
				-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//4
				
				-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//4
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//6
				-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//5
				
				-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//4
				-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//5
				-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//1
				
				-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//4
				-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//1
				-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,0.8f*UNIT_SIZE, //0

				-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//4
				-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,0.8f*UNIT_SIZE, //0
				0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//7
				
				0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//7
				-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,0.8f*UNIT_SIZE, //0
				0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//3
				
				-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//1
				-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//5
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//6
				
				-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE,//1
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,//6
				0.8f*UNIT_SIZE,-0.8f*UNIT_SIZE,0.8f*UNIT_SIZE
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
	            0.007f,0.007f,//0
	            0.005f,0.668f,//1
	            0.166f,0.668f,//2
	                               				
	            0.007f,0.007f,
	            0.166f,0.668f,//2
	            0.166f,0.007f,//3
	                               				
	            0.166f,0.668f,//2
	            0.33f,0.664f,//6
	            0.166f,0.007f,//3
	                               				
	            0.166f,0.005f,//3
	            0.33f,0.664f,//6
	            0.33f,0.005f,//7
	                               				
	            0.33f,0.008f,//7
	            0.33f,0.664f,//6
	            0.495f,0.008f,//4
	                               				
	            0.495f,0.008f,//4
	            0.33f,0.664f,//6
	            0.495f,0.664f,//5
	                               				
	            0.495f,0.005f,//4
	            0.495f,0.664f,//5
	            0.661f,0.66f,
	                               				
	            0.495f,0.005f,//4
	            0.661f,0.66f,
	            0.661f,0.005f,
	                               				
	            0.661f,0.008f,//4
	            0.661f,0.66f,
	            0.827f,0.008f,
	                               				
	            0.827f,0.008f,
	            0.661f,0.66f,
	            0.827f,0.66f,
	                               				
	            0.827f,0.007f,
	            0.827f,0.66f,
	            0.993f,0.668f,
	                               				
	            0.827f,0.007f,
	            0.993f,0.668f,
	            0.993f,0.007f
	                                   		
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, lftId);		
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		
        		0, 			 			
        		vCount				
        );
        
     //   gl.glPushMatrix();
        
    }
	

}
