package com.bn.cube.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class Line {
	
	int mProgram;//自訂著色管線著色器程式id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maColorHandle; //頂點紋理座標屬性參考id 
    
    FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mColorBuffer;//頂點紋理座標資料緩沖
    
    int vCount;//頂點個數
    
    float UNIT_SIZE=1.0f;
	public Line(MySurfaceView mv,float []color,float width)
	{
		initVertexData(color,width);
		initShader(mv);
	}
	public void initVertexData(float []color,float width)
	{
		vCount=2;
		float  vertices[]=new float[]{
				-UNIT_SIZE*width,0,0,
				UNIT_SIZE*width,0,0
		};
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.clear();
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        float []colors=new float[vCount*4];
        for(int i=0;i<vCount;i++)
        {
        	colors[4*i]=color[0];
        	colors[4*i+1]=color[1];
        	colors[4*i+2]=color[2];
        	colors[4*i+3]=color[3];
        }
        			
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());// 設定位元組順序
		mColorBuffer = cbb.asFloatBuffer();// 轉為Float型緩沖
		mColorBuffer.put(colors);// 向緩沖區中放入頂點著色資料
		mColorBuffer.position(0);// 設定緩沖區起始位置
		
	}
	public void initShader(MySurfaceView mv){
		
		String  mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_cube.sh", mv.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //載入片元著色器的指令稿內容
        String  mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_cube.sh", mv.getResources());  
        //基於頂點著色器與片元著色器建立程式
        ShaderUtil.checkGlError("==ss==");      
        mProgram =ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理座標屬性參考id  
        maColorHandle= GLES20.glGetAttribLocation(mProgram, "aColor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
	}
	public void drawSelf(){
		
		 //制定使用某套shader程式
	   	 GLES20.glUseProgram(mProgram);        
	   	 //起始化變換矩陣
	     //將最終變換矩陣傳入shader程式
	     GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
	        //傳入頂點位置資料
	     GLES20.glVertexAttribPointer  
	        (
	        		maPositionHandle,   
	        		3, 
	        		GLES20.GL_FLOAT, 
	        		false,
	               3*4,   
	               mVertexBuffer
	        );       
	        //傳入頂點紋理座標資料
	        GLES20.glVertexAttribPointer  
	        (
	       		maColorHandle, 
	        		4, 
	        		GLES20.GL_FLOAT, 
	        		false,
	                4*4,   
	               mColorBuffer
	        );   
	        //容許頂點位置資料陣列
	        GLES20.glEnableVertexAttribArray(maPositionHandle);  
	        GLES20.glEnableVertexAttribArray(maColorHandle);  
	        GLES20.glLineWidth(1f);
	        //繪制紋理矩形
	        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vCount); 
		
	}

}
