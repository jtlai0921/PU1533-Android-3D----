package com.bn.cube.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class Star {
	
		public FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	    public int vCount=0;//星星數量
	    public float scale;//星星尺寸  
	    String mVertexShader;//頂點著色器    	 
	    String mFragmentShader;//片元著色器
	    int mProgram;//自訂著色管執行緒序id 
	    int muMVPMatrixHandle;//總變換矩陣參考id   
	    int maPositionHandle; //頂點位置屬性參考id  
	    int uPointSizeHandle;//頂點尺寸參數參考
	    public float width;
	    public float height;
	    public Star(float width,float height,MyGLSurfaceView mv){  
	    	this.width=width;
	    	this.height=height;
	    	initVertexData(width,height);
	    	intShader(mv);
	    }
	    public void initVertexData(float width,float height){ //起始化頂點座標的方法    	  	
	    	//頂點座標資料的起始化       
	    	vCount=(int) (Math.random()*30f);
	        float vertices[]=new float[vCount*3];
	        for(int i=0;i<vCount;i++){ 
	        	//隨機產生每個星星的xyz座標
	        	double tempX=(Math.random()-0.5f);
	        	double tempY=(Math.random()-0.5f);
	        	vertices[3*i]=(float) (width*tempX*2);
	        	vertices[3*i+1]=(float) (height*tempY*2);
	        	vertices[3*i+2]=0f;        
	        }
	    	scale=(float) (Math.random()*10f);
	        //建立頂點座標資料緩沖
	        ByteBuffer vbb = ByteBuffer.allocateDirect(90*4);
	        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
	        mVertexBuffer = vbb.asFloatBuffer();//轉為int型緩沖
	        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
	        mVertexBuffer.position(0);//設定緩沖區起始位置
	      
	    }
	    public void intShader(MyGLSurfaceView mv){    //起始化著色器
	    	//載入頂點著色器的指令稿內容       
	        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_xk.sh", mv.getResources());
	        ShaderUtil.checkGlError("==ss==");   
	        //載入片元著色器的指令稿內容
	        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_xk.sh", mv.getResources());  
	        //基於頂點著色器與片元著色器建立程式
	        ShaderUtil.checkGlError("==ss==");      
	        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
	        //取得程式中頂點位置屬性參考id  
	        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");        
	        //取得程式中總變換矩陣參考id
	        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
	        //取得頂點尺寸參數參考
	        uPointSizeHandle = GLES20.glGetUniformLocation(mProgram, "uPointSize"); 
	    }
	    public void drawSelf(){  
	 
	   	    GLES20.glUseProgram(mProgram); //制定使用某套著色器程式
	        //將最終變換矩陣傳入著色器程式
	        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);  
	        GLES20.glUniform1f(uPointSizeHandle, scale);  //將頂點尺寸傳入著色器程式
	        GLES20.glVertexAttribPointer( //為畫筆指定頂點位置資料    
	        		maPositionHandle,   
	        		3, 
	        		GLES20.GL_FLOAT, 
	        		false,
	                3*4, 
	                mVertexBuffer   
	        );   
	        //容許頂點位置資料陣列
	        GLES20.glEnableVertexAttribArray(maPositionHandle);         
	        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vCount); //繪制星星點    
	}


}
