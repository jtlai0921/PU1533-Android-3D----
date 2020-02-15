package com.bn.ball;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Ground {
	
	private FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
    private FloatBuffer mTextureBuffer;//頂點紋理資料緩沖
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
        //建立頂點座標資料緩沖    	
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
  
        //紋理 座標資料起始化
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
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點座標陣列
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        
		//為畫筆指定頂點座標資料
        gl.glVertexPointer
        (
        		3,				
        		GL10.GL_FLOAT,
        		0, 				
        		mVertexBuffer	
        );    
        //開啟紋理
        gl.glEnable(GL10.GL_TEXTURE_2D);   
        //容許使用紋理ST座標緩沖
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //為畫筆指定紋理ST座標緩沖
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        //綁定目前紋理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);		
        //繪制圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		
        		0, 			 			
        		vCount				
        );
    }
}
