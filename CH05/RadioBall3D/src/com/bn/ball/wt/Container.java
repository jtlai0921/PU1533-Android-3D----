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
	   //建立頂點座標資料緩沖
	        //vertices.length*4是因為一個整數四個位元組
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
	        mVertexBuffer = vbb.asFloatBuffer();//轉為int型緩沖
	        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
	        mVertexBuffer.position(0);//設定緩沖區起始位置     
	        
	        float textureCoors[]=new float[]
	        {       
	        		0.031f,0.031f,
	        		0.031f,0.49f,
		            0.492f,0.49f,
		            
		            0.031f,0.031f,
	        		0.492f,0.49f,
	        		0.492f,0.031f,//右
	        		
	        		0.517f,0.031f,
	        		0.517f,0.471f,
	        		0.956f,0.471f,
	        		
	        		0.517f,0.031f,
	        		0.956f,0.471f,
		            0.956f,0.031f,//前                               				
      				
	        		0.517f,0.031f,
	        		0.517f,0.471f,
	        		0.956f,0.471f,
	        		
	        		0.517f,0.031f,
	        		0.956f,0.471f,
		            0.956f,0.031f,//後
		                               				
		            0.031f,0.031f,
	        		0.031f,0.49f,
		            0.492f,0.49f,
		            
		            0.031f,0.031f,
	        		0.492f,0.49f,
	        		0.492f,0.031f,//左
		                               				
	        		0.517f,0.031f,
	        		0.517f,0.471f,
	        		0.956f,0.471f,
	        		
	        		0.517f,0.031f,
	        		0.956f,0.471f,
		            0.956f,0.031f,//上
		                              		            
		            0.517f,0.031f,
	        		0.517f,0.471f,
	        		0.956f,0.471f,
	        		
	        		0.517f,0.031f,
	        		0.956f,0.471f,
		            0.956f,0.031f //下                               		
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
//        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, containerId);		
        //繪制圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		
        		0, 			 			
        		vCount				
        );
    }
	
	
}
