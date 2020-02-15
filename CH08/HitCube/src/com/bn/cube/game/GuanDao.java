package com.bn.cube.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class GuanDao {
	int mProgram;//自訂著色管線著色器程式id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int muMMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id 
    int muCurrZLHHandle;
	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖
    int vCount=0;
    
    MySurfaceView mv;
    public GuanDao(MySurfaceView mv,float width,float height)
    {    
    	this.mv=mv;
    	//起始化頂點座標資料的方法
    	initVertexData(width,height);
    	//起始化著色器程式的方法
    	initShader(mv);
    }
    
    //起始化頂點座標資料的方法
    public void initVertexData(float width,float height)
    {
    	//頂點座標資料的起始化================begin============================
        ArrayList<Float> alVertix=new ArrayList<Float>();
        ArrayList<Float> alTexCoor=new ArrayList<Float>();
       
        int t=0;
        for(float l=0;l>-Constant.length;l--)
        {
        	 int s=0;
        	 for(float w=width;w>-width;w--)
        	 {
        		 float x1=w;
        		 float y1=height;
        		 float z1=l;
        		 
        		 float x2=w-1;
        		 float y2=height;
        		 float z2=l;
        		 
        		 float x3=w-1;
        		 float y3=height;
        		 float z3=l-1;
        		 
        		 float x4=w;
        		 float y4=height;
        		 float z4=l-1;
        		 
        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.027137f*s);alTexCoor.add(0.033f*t);
        		 alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		 alTexCoor.add(0.027137f*(s+1));alTexCoor.add(0.033f*t);
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.027137f*(s+1));alTexCoor.add(0.033f*(t+1));
        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.027137f*s);alTexCoor.add(0.033f*t);
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.027137f*(s+1));alTexCoor.add(0.033f*(t+1));
        		 alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		 alTexCoor.add(0.027137f*s);alTexCoor.add(0.033f*(t+1));
        		 s++;
        	 }
//        	
        	 for(float angle=90;angle<270;angle+=Constant.angleSpan)
        	 {
        		 float angrad=(float) Math.toRadians(angle);//目前弧度 
     			 float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//下一弧度
        		 float x1=(float) (-width+height*Math.cos(angrad));   
        		 float y1=(float) (height*Math.sin(angrad));
        		 float z1=l;
        		 
        		 float x2=(float) (-width+height*Math.cos(angradNext));
        		 float y2=(float) (height*Math.sin(angradNext));
        		 float z2=l;
        		 
        		 float x3=(float) (-width+height*Math.cos(angradNext));
        		 float y3=(float) (height*Math.sin(angradNext));
        		 float z3=l-1;
        		 
        		 float x4=(float) (-width+height*Math.cos(angrad));
        		 float y4=(float) (height*Math.sin(angrad));
        		 float z4=l-1;
        		 
        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.028418f*(s-9)+0.244233f);alTexCoor.add(0.033f*t);
        		
        		 alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		 alTexCoor.add(0.028418f*(s-8)+0.244233f);alTexCoor.add(0.033f*t);
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.028418f*(s-8)+0.244233f);alTexCoor.add(0.033f*(t+1));
        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.028418f*(s-9)+0.244233f);alTexCoor.add(0.033f*t);
        		
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.028418f*(s-8)+0.244233f);alTexCoor.add(0.033f*(t+1));
        		 alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		 alTexCoor.add(0.028418f*(s-9)+0.244233f);alTexCoor.add(0.033f*(t+1));
        		 s++;
        	 }
        	 for(float w=-width;w<width;w++)
        	 {
        		 float x1=w;
        		 float y1=-height;
        		 float z1=l;
        		 
        		 float x2=w+1;
        		 float y2=-height;
        		 float z2=l;
        		 
        		 float x3=w+1;
        		 float y3=-height;
        		 float z3=l-1;
        		 
        		 float x4=w;
        		 float y4=-height;
        		 float z4=l-1;
        		 
        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.027137f*(s-18)+0.5f);alTexCoor.add(0.033f*t);
        		 alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		 alTexCoor.add(0.027137f*(s-17)+0.5f);alTexCoor.add(0.033f*(t));
        		 alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		 alTexCoor.add(0.027137f*(s-18)+0.5f);alTexCoor.add(0.033f*(t+1));
        		 alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		 alTexCoor.add(0.027137f*(s-17)+0.5f);alTexCoor.add(0.033f*t);
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.027137f*(s-17)+0.5f);alTexCoor.add(0.033f*(t+1));
        		 alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		 alTexCoor.add(0.027137f*(s-18)+0.5f);alTexCoor.add(0.033f*(t+1));
        		 s++;
        	 }        	
        	 for(float angle=-90;angle<90;angle+=Constant.angleSpan)
        	 {
        		 float angrad=(float) Math.toRadians(angle);//目前弧度
     			 float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//下一弧度
        		 float x1=(float) (width+height*Math.cos(angrad));
        		 float y1=(float) (height*Math.sin(angrad));
        		 float z1=l;
        		 
        		 float x2=(float) (width+height*Math.cos(angradNext));
        		 float y2=(float) (height*Math.sin(angradNext));
        		 float z2=l;
        		 
        		 float x3=(float) (width+height*Math.cos(angradNext));
        		 float y3=(float) (height*Math.sin(angradNext));
        		 float z3=l-1;
        		 
        		 float x4=(float) (width+height*Math.cos(angrad));
        		 float y4=(float) (height*Math.sin(angrad));
        		 float z4=l-1;
        		 

        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.028418f*(s-27)+0.744233f);alTexCoor.add(0.033f*t);
        		 alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		 alTexCoor.add(0.028418f*(s-26)+0.744233f);alTexCoor.add(0.033f*(t));
        		 alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		 alTexCoor.add(0.028418f*(s-27)+0.744233f);alTexCoor.add(0.033f*(t+1));
        		 alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		 alTexCoor.add(0.028418f*(s-26)+0.744233f);alTexCoor.add(0.033f*t);
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.028418f*(s-26)+0.744233f);alTexCoor.add(0.033f*(t+1));
        		 alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		 alTexCoor.add(0.028418f*(s-27)+0.744233f);alTexCoor.add(0.033f*(t+1));
        		 s++;
        	 }
        	 t++;
        }
        vCount=alVertix.size()/3;
        float vertices[]=new float[vCount*3];
     	for(int i=0;i<alVertix.size();i++) 
     	{
     		vertices[i]=alVertix.get(i);
     	}	
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        float texCoor[]=new float[vCount*2];//頂點紋理陣列
        for(int i=0;i<alTexCoor.size();i++)
     	{
        	texCoor[i]=alTexCoor.get(i);
     	}	
        //建立頂點紋理座標資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTexCoorBuffer = cbb.asFloatBuffer();//轉為Float型緩沖
        mTexCoorBuffer.put(texCoor);//向緩沖區中放入頂點著色資料
        mTexCoorBuffer.position(0);//設定緩沖區起始位置
    }

	//起始化著色器程式的方法
    public void initShader(MySurfaceView mv)
    {
    	//載入頂點著色器的指令稿內容       
      String  mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_guan.sh", mv.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //載入片元著色器的指令稿內容
      String  mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_guan.sh", mv.getResources());  
        //基於頂點著色器與片元著色器建立程式
        ShaderUtil.checkGlError("==ss==");      
        mProgram =ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理座標屬性參考id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");  
        muCurrZLHHandle=GLES20.glGetUniformLocation(mProgram, "currZLH");  
    }
    
    public void drawSelf(int texId)
    { 
    	 //制定使用某套shader程式
    	 GLES20.glUseProgram(mProgram);        
    	 //起始化變換矩陣
         //將最終變換矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0); 
         GLES20.glUniform1f(muCurrZLHHandle, Constant.currZL);
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
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );   
         //容許頂點位置資料陣列
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         
         //綁定紋理
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //繪制紋理矩形
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}
