package com.bn.ball;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Ground {
	
	private FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
    private FloatBuffer mTextureBuffer;//���I���z��ƽw�R
    int vCount;
    int texId;
    public Ground(int texId)
    {
    	final float UNIT_SIZE=1.0f;
    	this.texId=texId;
    	vCount=6;
    	float[] vertices=new float[]
    	{    			
        		-3*UNIT_SIZE,0,-Constant.GROUND_L*UNIT_SIZE,
        		-3*UNIT_SIZE,0,0*UNIT_SIZE,
        		3*UNIT_SIZE,0,0*UNIT_SIZE,
        		
        		-3*UNIT_SIZE,0,-Constant.GROUND_L*UNIT_SIZE,
        		3*UNIT_SIZE,0,0*UNIT_SIZE,
        		3*UNIT_SIZE,0,-Constant.GROUND_L*UNIT_SIZE
    	};    	
        //�إ߳��I�y�и�ƽw�R    	
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
  
        //���z �y�и�ư_�l��
//        vCount=(int)(vertices.length/3);
        float[] textures=new float[]
        {
        		0,0,            	
            	0,1,            	
            	1,1,
            	
            	0,0,
            	1,1,            	
            	1,0 
        };
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());
        mTextureBuffer = tbb.asFloatBuffer();
        mTextureBuffer.put(textures);
        mTextureBuffer.position(0);   
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);		
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		
        		0, 			 			
        		vCount				
        );
    }
}
