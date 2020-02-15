
package com.bn.GL20Tail;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TailPart 
{
	public float[] startPoint;
	public  float[] endPoint;
    float alpha=0.82f;
    public float angle=0;
    public float[] normal={0,0,0};
    public float[] selfVector={0,0,0};
    public float length=0;
    
	float   data[]={1,1,1,1,1,1}; 
	public static FloatBuffer normalBuffer;
	
	
    public TailPart(float[] startPoint,float[] endPoint,float alpha)
    {
    	this.startPoint=startPoint;
    	this.endPoint=endPoint;
    	this.alpha=alpha; 
    	
   	 ByteBuffer abb = ByteBuffer.allocateDirect(data.length*4);
        abb.order(ByteOrder.nativeOrder());//設定位元組順序
        normalBuffer = abb.asFloatBuffer();//轉為Float型緩沖
        normalBuffer.put(data);//向緩沖區中放入頂點著色資料
        normalBuffer.position(0);//設定緩沖區起始位置 
    }
     
    public FloatBuffer getAlphaBuffer()
    { 
    	FloatBuffer mAlphaBuffer;
    	float  alphaData[]=new float[6]; 
		alphaData[0]=alpha-0.2f;
		alphaData[1]=alpha-0.2f;
		alphaData[2]=alpha-0.2f;
		alphaData[3]=alpha-0.2f;
		alphaData[4]=alpha-0.2f;
		alphaData[5]=alpha-0.2f;
		
		 ByteBuffer abb = ByteBuffer.allocateDirect(alphaData.length*4);
	     abb.order(ByteOrder.nativeOrder());//設定位元組順序
	     mAlphaBuffer = abb.asFloatBuffer();//轉為Float型緩沖
	     mAlphaBuffer.put(alphaData);//向緩沖區中放入頂點著色資料
	     mAlphaBuffer.position(0);//設定緩沖區起始位置 
		 return mAlphaBuffer; 
    }
    
    
    
    
}
