package com.bn.ball;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

import static com.bn.ball.Constant.*;


//用於繪制的球
public class Ball {   
	private FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
    private FloatBuffer mTextureBuffer;//頂點紋理資料緩沖

    int vCount=0;//頂點數量
    float scale;//尺寸
    int textureID;
    
    public Ball(float scale,int textureID) 
    {
    	this.textureID=textureID;
    	this.scale=scale;    	
    	
    	//取得切分整圖的紋理陣列
    	float[] texCoorArray= 
         generateTexCoor
    	 (
    			 (int)(360/ANGLE_SPAN), //紋理圖切分的列數
    			 (int)(180/ANGLE_SPAN)  //紋理圖切分的行數
    	 );
    	
    	ArrayList<Float> alVertix=new ArrayList<Float>();//存放頂點座標的ArrayList
    	
        for(float vAngle=90;vAngle>-90;vAngle=vAngle-ANGLE_SPAN)//垂直方向angleSpan度一份
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-ANGLE_SPAN)//水平方向angleSpan度一份
        	{
        		//直印水平各到一個角度後計算對應的此點在球面上的四邊形頂點座標
        		//並建構兩個群組成四邊形的三角形
        		
        		//0-----1
        		//|  \  |
        		//3-----2
        		
        		double r=scale*UNIT_SIZE;
        		//小四邊形的第0個頂點  
        		float x0=(float)(r*Math.cos(Math.toRadians(vAngle))*Math.cos(Math.toRadians(hAngle)));
        		float z0=(float)(r*Math.cos(Math.toRadians(vAngle))*Math.sin(Math.toRadians(hAngle)));
        		float y0=(float)(r*Math.sin(Math.toRadians(vAngle)));
        		
        		//小四邊形的第1個頂點        		
        		float x1=(float)(r*Math.cos(Math.toRadians(vAngle))*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float z1=(float)(r*Math.cos(Math.toRadians(vAngle))*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float y1=(float)(r*Math.sin(Math.toRadians(vAngle)));
        		
        		//小四邊形的第2個頂點        		
        		float x2=(float)(r*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN))*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float z2=(float)(r*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN))*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float y2=(float)(r*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
        		
        		//小四邊形的第3個頂點        		
        		float x3=(float)(r*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN))*Math.cos(Math.toRadians(hAngle)));
        		float z3=(float)(r*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN))*Math.sin(Math.toRadians(hAngle)));
        		float y3=(float)(r*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
        		
        		//建構第一三角形0-2-1
        		alVertix.add(x0);alVertix.add(y0);alVertix.add(z0);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);          		
        		      		
        		//建構第二三角形0-3-2        		
        		alVertix.add(x0);alVertix.add(y0);alVertix.add(z0);
        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);        		
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2); 
        	}
        } 	
        
        vCount=alVertix.size()/3;//頂點的數量為座標值數量的1/3，因為一個頂點有3個座標
    	
        //將alVertix中的座標值轉存到一個float陣列中
        float vertices[]=new float[vCount*3];
    	for(int i=0;i<alVertix.size();i++)
    	{
    		vertices[i]=alVertix.get(i);
    	}
        
        //建立繪制頂點資料緩沖
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為int型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置     
                
        //建立紋理座標緩沖        
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoorArray.length*4);
        tbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTextureBuffer = tbb.asFloatBuffer();//轉為int型緩沖
        mTextureBuffer.put(texCoorArray);//向緩沖區中放入頂點著色資料
        mTextureBuffer.position(0);//設定緩沖區起始位置
    }

    public void drawSelf(GL10 gl)
    {
        //容許使用頂點陣列
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//為畫筆指定頂點座標資料
        gl.glVertexPointer
        (
        		3,				//每個頂點的座標數量為3  xyz 
        		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
        		0, 				//連續頂點座標資料之間的間隔
        		mVertexBuffer	//頂點座標資料
        );        
		
        //開啟紋理
        gl.glEnable(GL10.GL_TEXTURE_2D);   
        //容許使用紋理ST座標緩沖
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //為畫筆指定紋理ST座標緩沖
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        //綁定目前紋理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
        //繪制圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//以三角形模式填充
        		0, 			 			//開始點編號
        		vCount					//頂點數量
        );        
    }
    
    //自動切分紋理產生紋理陣列的方法
    public float[] generateTexCoor(int bw,int bh)
    {
    	//0-----1
		//|  \  |
		//3-----2    	
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=1.0f/bw;//列數
    	float sizeh=1.0f/bh;//行數
    	int c=0;
    	for(int i=0;i<bh;i++)
    	{
    		for(int j=0;j<bw;j++)
    		{
    			//每行列一個矩形，由兩個三角形構成，共六個點，12個紋理座標
    			float s=j*sizew;
    			float t=i*sizeh;
    			
    			result[c++]=s;
    			result[c++]=t;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;    			
    		}
    	}
    	return result;
    }
    
}
