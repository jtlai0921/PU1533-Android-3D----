package com.bn.cube.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class Wall {

	int mProgram;//自訂著色管線著色器程式id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maColorHandle; //頂點紋理座標屬性參考id 
    
    FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mColorBuffer;//頂點紋理座標資料緩沖
    
    int vCount;//頂點個數
	public Wall(MySurfaceView mv,float width,float height,float []color)
	{
		initVertexData(width,height,color);
		initShader(mv);
	}
	
	public void initVertexData(float width,float height,float []color)
	{
		  ArrayList<Float> alVertix=new ArrayList<Float>();
		  alVertix.add(width);alVertix.add(height);alVertix.add(0f);
		  alVertix.add(-width);alVertix.add(height);alVertix.add(0f);
		  alVertix.add(-width);alVertix.add(-height);alVertix.add(0f);
		  alVertix.add(width);alVertix.add(height);alVertix.add(0f);
		  alVertix.add(-width);alVertix.add(-height);alVertix.add(0f);
		  alVertix.add(width);alVertix.add(-height);alVertix.add(0f);
		  for(float angle=90;angle<270;angle+=Constant.angleSpan)
		  {
			  float angrad=(float) Math.toRadians(angle);//目前弧度 
  			  float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//下一弧度
  			  float x1=(float) (-width+height*Math.cos(angrad));   
  			  float y1=(float) (height*Math.sin(angrad));
  			  float z1=0;
   		 
  			  float x2=(float) (-width+height*Math.cos(angradNext));
  			  float y2=(float) (height*Math.sin(angradNext));
  			  float z2=0;
  			  alVertix.add(-width);alVertix.add(0f);alVertix.add(0f);
  			  alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
  			  alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
  			  
  			  
		  }
		  for(float angle=-90;angle<90;angle+=Constant.angleSpan)
     	 {
			  float angrad=(float) Math.toRadians(angle);//目前弧度
  			  float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//下一弧度
  			  float x1=(float) (width+height*Math.cos(angrad));   
			  float y1=(float) (height*Math.sin(angrad));
			  float z1=0;
 		 
			  float x2=(float) (width+height*Math.cos(angradNext));
			  float y2=(float) (height*Math.sin(angradNext));
			  float z2=0;
			  alVertix.add(width);alVertix.add(0f);alVertix.add(0f);
  			  alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
  			  alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
     	 }
		  	vCount=alVertix.size()/3;
		  	float  vertices[]=new float[vCount*3];
	        for(int i=0;i<vCount;i++)
	        {
	        	vertices[3*i]=alVertix.get(3*i);
	        	vertices[3*i+1]=alVertix.get(3*i+1);
	        	vertices[3*i+2]=alVertix.get(3*i+2);
	        }
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
    
    //繪制紋理矩形
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 

}
}
