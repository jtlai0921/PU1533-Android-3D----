
package com.bn.cube.game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class Circle{

	int mProgram;//自訂著色管執行緒序id 
    int muMVPMatrixHandle;//總變換矩陣參考id  
    int maPositionHandle; //頂點位置屬性參考id   
    int  maColorHandle;
    String mVertexShader;//頂點著色器    	 
    String mFragmentShader;//片元著色器
	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mColorBuffer;//頂點座標資料緩沖
	
    int vCount=0; 
    float lineWidth=2;
    public Circle(MySurfaceView mv,float lineWidth,float []color)
    {
    	this.lineWidth=lineWidth;
    	initVertexData(color);
    	initShader(mv);
    }
    public void initVertexData(float []color)
    {
    	//頂點座標資料的起始化================begin============================    	
    	float width=4.4f;
    	float height=2.9f;
    	ArrayList<Float> alfVertix=new ArrayList<Float>();
    	float x1=width;
    	float y1=height;
    	float z1=0;
    	
    	float x2=-width;
    	float y2=height;
    	float z2=0; 
    	
    	float x3=-width;
    	float y3=-height;
    	float z3=0;
    	
    	float x4=width;
    	float y4=-height;
    	float z4=0;
		alfVertix.add(x1);alfVertix.add(y1);alfVertix.add(z1);
		alfVertix.add(x2);alfVertix.add(y2);alfVertix.add(z2);
		alfVertix.add(x3);alfVertix.add(y3);alfVertix.add(z3);
		alfVertix.add(x4);alfVertix.add(y4);alfVertix.add(z4);
		for(float angle=90;angle<270;angle+=Constant.angleSpan)
		{
   		 	float angrad=(float) Math.toRadians(angle);//目前弧度 
			float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//下一弧度
   		 	float X1=(float) (-width+height*Math.cos(angrad));   
   		 	float Y1=(float) (height*Math.sin(angrad));
   		 	float Z1=0;
   		 
   		 	float X2=(float) (-width+height*Math.cos(angradNext));
   		 	float Y2=(float) (height*Math.sin(angradNext));
   		 	float Z2=0;
			alfVertix.add(X1);alfVertix.add(Y1);alfVertix.add(Z1);
			alfVertix.add(X2);alfVertix.add(Y2);alfVertix.add(Z2);
		}
		for(float angle=-90;angle<90;angle+=Constant.angleSpan)
		{
   		 	float angrad=(float) Math.toRadians(angle);//目前弧度 
			float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//下一弧度
   		 	float X1=(float) (width+height*Math.cos(angrad));   
   		 	float Y1=(float) (height*Math.sin(angrad));
   		 	float Z1=0;
   		 
   		 	float X2=(float) (width+height*Math.cos(angradNext));
   		 	float Y2=(float) (height*Math.sin(angradNext));
   		 	float Z2=0;
			alfVertix.add(X1);alfVertix.add(Y1);alfVertix.add(Z1);
			alfVertix.add(X2);alfVertix.add(Y2);alfVertix.add(Z2);
		}
 
    	 vCount=alfVertix.size()/3;
    	//將alVertix中的座標值轉存到一個float陣列中
         float vertices[]=new float[vCount*3];
     	for(int i=0;i<alfVertix.size();i++)
     	{
     		vertices[i]=alfVertix.get(i);
     	}	
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為int型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
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
    //起始化shader
    public void initShader(MySurfaceView mv)
    {
    	//載入頂點著色器的指令稿內容       
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_cube.sh", mv.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //載入片元著色器的指令稿內容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_cube.sh", mv.getResources());  
        //基於頂點著色器與片元著色器建立程式
        ShaderUtil.checkGlError("==ss==");      
        mProgram =ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition"); 
         maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");   
    }
    
    public void drawSelf() 
    {        
    	 //制定使用某套shader程式
    	 GLES20.glUseProgram(mProgram);
         //將最終變換矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);  
         //為畫筆指定頂點位置資料    
         GLES20.glVertexAttribPointer        
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4, 
                mVertexBuffer   
         );       
         GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false,
 				4 * 4, mColorBuffer);
        
         //容許頂點位置資料陣列
         GLES20.glEnableVertexAttribArray(maPositionHandle);     
         GLES20.glEnableVertexAttribArray(maColorHandle);
         GLES20.glLineWidth(lineWidth);//設定線的寬度
         
         //繪制線形
         GLES20.glDrawArrays(GLES20.GL_LINES, 0, vCount); 
    }
    
   
}
