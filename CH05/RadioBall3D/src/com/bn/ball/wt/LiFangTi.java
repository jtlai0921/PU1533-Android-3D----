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
	   //建立頂點座標資料緩沖
	        //vertices.length*4是因為一個整數四個位元組
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
	        mVertexBuffer = vbb.asFloatBuffer();//轉為int型緩沖
	        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
	        mVertexBuffer.position(0);//設定緩沖區起始位置     
	        
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
	        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
	        mTextureBuffer = cbb.asFloatBuffer();//轉為int型緩沖
	        mTextureBuffer.put(textureCoors);//向緩沖區中放入頂點著色資料
	        mTextureBuffer.position(0);//設定緩沖區起始位置
	}
	@Override
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, lftId);		
        //繪制圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		
        		0, 			 			
        		vCount				
        );
        
     //   gl.glPushMatrix();
        
    }
	

}
