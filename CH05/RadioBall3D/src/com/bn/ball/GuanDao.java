package com.bn.ball;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;


public class GuanDao {
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖
    int vCount=0;
    
    MySurfaceView mv;
    public GuanDao(MySurfaceView mv,float height)
    {    
    	this.mv=mv;
    	//起始化頂點座標資料的方法
    	initVertexData(height);
    	//起始化著色器程式的方法
    }
    
    //起始化頂點座標資料的方法
    public void initVertexData(float height)
    {
    	//頂點座標資料的起始化================begin============================
        ArrayList<Float> alVertix=new ArrayList<Float>();
        ArrayList<Float> alTexCoor=new ArrayList<Float>();
       
        int t=0;
        for(float l=0;l>-Constant.GUANDAO_L;l--)
        {
        	 int s=0;
//        	
        	 for(float angle=-40;angle<220;angle+=Constant.angleSpan)
        	 {
        		 float angrad=(float) Math.toRadians(angle);//目前弧度 
     			 float angradNext=(float) Math.toRadians(angle+Constant.angleSpan);//下一弧度
        		 float x1=(float) (height*Math.cos(angrad));   
        		 float y1=(float) (height*Math.sin(angrad));
        		 float z1=l;
        		 
        		 float x2=(float) (height*Math.cos(angradNext));
        		 float y2=(float) (height*Math.sin(angradNext));
        		 float z2=l;
        		 
        		 float x3=(float) (height*Math.cos(angradNext));
        		 float y3=(float) (height*Math.sin(angradNext));
        		 float z3=l-1;
        		 
        		 float x4=(float) (height*Math.cos(angrad));
        		 float y4=(float) (height*Math.sin(angrad));
        		 float z4=l-1;
        		 
        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.077f*(s));alTexCoor.add(0.05f*t);
        		
        		 alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		 alTexCoor.add(0.077f*(s+1));alTexCoor.add(0.05f*t);
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.077f*(s+1));alTexCoor.add(0.05f*(t+1));
        		 alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		 alTexCoor.add(0.077f*(s));alTexCoor.add(0.05f*t);
        		
        		 alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
        		 alTexCoor.add(0.077f*(s+1));alTexCoor.add(0.05f*(t+1));
        		 alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		 alTexCoor.add(0.077f*(s));alTexCoor.add(0.05f*(t+1));
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
    public void drawSelf(GL10 gl)
    { 
    	
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);           	
		gl.glVertexPointer
		(
				3, 
				GL10.GL_FLOAT,
				0,
				mVertexBuffer
	   );
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoorBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D,mv.textureID);
			

			gl.glDrawArrays
			(
					GL10.GL_TRIANGLES,
					0, 
					vCount
			);
    }
}
