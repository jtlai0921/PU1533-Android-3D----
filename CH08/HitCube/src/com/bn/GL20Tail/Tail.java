package com.bn.GL20Tail;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Tail 
{
	final float w=0.1f;
	FloatBuffer   mVertexBufferG;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBufferG;//頂點紋理座標資料緩沖
	FloatBuffer   mAlphaBufferG;//頂點透明度資料緩沖
	
	int vCount=0;   
	DrawBuffer db;
	public List<TailPart> pl=new ArrayList<TailPart>();
	Object lock=new Object();
	
	public Tail(DrawBuffer db)
	{
		this.db=db;
	}
	
	public float[][] getPnArray(TailPart tp)
	{
		float[][] result=null;
		//原始矩形四個點座標
		float[] p0={-w,0,0,1};
		float[] p1={w,0,0,1};
		float[] p2={w,1,0,1};
		float[] p3={-w,1,0,1};
		float[] sourceV={0,1,0,1};
		
		float[] targetV=new float[3];
		targetV[0]=tp.endPoint[0]-tp.startPoint[0];
		targetV[1]=tp.endPoint[1]-tp.startPoint[1];
		targetV[2]=tp.endPoint[2]-tp.startPoint[2];
		tp.selfVector=VectorUtil.vectorNormal(targetV);
		
		//計算向量的夾角
		float angle=(float)Math.toDegrees(VectorUtil.angle(sourceV, targetV));
		tp.angle=angle;
		
		//計算向量的叉積
		float[] normal=VectorUtil.getCrossProduct
		(
			sourceV[0], sourceV[1], sourceV[2], 
			targetV[0], targetV[1], targetV[2]
		);
		normal=VectorUtil.vectorNormal(normal);
		tp.normal=normal;
		
		//計算此線段起始點與目的點的距離
		float dis=VectorUtil.mould(targetV);
		tp.length=dis;
		float[] mc=new float[16];
		Matrix.setIdentityM(mc, 0);
		Matrix.translateM(mc, 0, tp.startPoint[0], tp.startPoint[1], tp.startPoint[2]);
		Matrix.rotateM(mc, 0, angle, normal[0], normal[1], normal[2]);
		Matrix.scaleM(mc, 0, 1, dis, 1);
		
		float[] p0a=new float[4];
		float[] p1a=new float[4];
		float[] p2a=new float[4];
		float[] p3a=new float[4];
		
		Matrix.multiplyMV(p0a,0, mc,0, p0, 0);
		Matrix.multiplyMV(p1a,0, mc,0, p1, 0);
		Matrix.multiplyMV(p2a,0, mc,0, p2, 0);
		Matrix.multiplyMV(p3a,0, mc,0, p3, 0);
		
		result=new float[][]
		{
			p0a,p1a,p2a,p3a
        };
		
		return result;
	}
	
	//起始化頂點座標與著色資料的方法synchronized
     public void genVertexData()
    {
    	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
    	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖    	
    	FloatBuffer   mAlphaBuffer;//頂點透明度資料緩沖
    	
    	vCount=pl.size()*6;
    	float vertices[]=new float[vCount*3];
    	float texCoor[]=new float[vCount*2];
    	float alphaData[]=new float[vCount];
    	int count=0;
    	float[][] pArrayBefore=null;
    	float[][] pArrayAfter=null;
    	float[][] pArray=null;
    	for(int i=0;i<pl.size();i++)
    	{
    		//每個矩形頂點分佈如下
    		// 3-----2
    		// | \   |
    		// |   \ |
    		// 0-----1
    		// 卷冊繞模式0-1-3，1-2-3
    		//每個矩形的0號點接上一個矩形的3號點
    		//每個矩形的1號點接上一個矩形的2號點
    		//每個矩形的2號點接下一個矩形的1號點
    		//每個矩形的3號點接下一個矩形的0號點
    		//第一個矩形沒有上家，最後一個矩形沒有下家
    		
    		float[] p0c,p1c,p2c,p3c;//目前的
			float[] p0n=null;
    		float[] p1n=null;
			@SuppressWarnings("unused")
			float[] p2n;
			@SuppressWarnings("unused")
			float[] p3n; 
    		@SuppressWarnings("unused")
			float[] p0p=null,p1p=null,p2p=null,p3p=null;//上一個
    		float[] p0a=null;//結果
    		float[] p1a=null;
    		float[] p2a=null;
    		float[] p3a=null;
    		
    		//若果是第一段和前面的接
    		if(i==0)
    		{
    			pArray=getPnArray(pl.get(i));
        		
    		}
    		if(i<pl.size()-1)
    		{
    			pArrayAfter=getPnArray(pl.get(i+1));
    		}
    		
    		p0c=pArray[0];
    		p1c=pArray[1];
    		p2c=pArray[2];
    		p3c=pArray[3];
    		//if(pArray!=null)
    		{
    			p0n=pArrayAfter[0];
    		p1n=pArrayAfter[1];
    		p2n=pArrayAfter[2];
    		p3n=pArrayAfter[3];	
    		}
    	
    		
    		if(i!=0)
    		{
    			p0p=pArrayBefore[0];
    			p1p=pArrayBefore[1];
    			p2p=pArrayBefore[2];
    			p3p=pArrayBefore[3];
    		}
    		
    		if(i==0)
    		{
    			p0a=p0c;
        		p1a=p1c;
        		p2a=new float[]
        		{
        			(p2c[0]+p1n[0])/2.0f,
        			(p2c[1]+p1n[1])/2.0f,
        			(p2c[2]+p1n[2])/2.0f,
        		};
        		p3a=new float[]
        		{
        			(p3c[0]+p0n[0])/2.0f,
        			(p3c[1]+p0n[1])/2.0f,
        			(p3c[2]+p0n[2])/2.0f,
            	};
    		}
    		else if(i==pl.size()-1)
    		{
    			p0a=new float[]
        		{
        			(p0c[0]+p3p[0])/2.0f,
        			(p0c[1]+p3p[1])/2.0f,
        			(p0c[2]+p3p[2])/2.0f,
        		};
    			
    			p1a=new float[]
        		{
        			(p1c[0]+p2p[0])/2.0f,
        			(p1c[1]+p2p[1])/2.0f,
        			(p1c[2]+p2p[2])/2.0f,
        		};
    			
    			p2a=p2c;
        		p3a=p3c;
    		}
    		else
    		{
    			p0a=new float[]
        		{
        			(p0c[0]+p3p[0])/2.0f,
        			(p0c[1]+p3p[1])/2.0f,
        			(p0c[2]+p3p[2])/2.0f,
        		};
    			
    			p1a=new float[]
        		{
        			(p1c[0]+p2p[0])/2.0f,
        			(p1c[1]+p2p[1])/2.0f,
        			(p1c[2]+p2p[2])/2.0f,
        		};
        		p2a=new float[]
        		{
        			(p2c[0]+p1n[0])/2.0f,
        			(p2c[1]+p1n[1])/2.0f,
        			(p2c[2]+p1n[2])/2.0f,
        		};
        		p3a=new float[]
        		{
        			(p3c[0]+p0n[0])/2.0f,
        			(p3c[1]+p0n[1])/2.0f,
        			(p3c[2]+p0n[2])/2.0f,
            	};    			
    		}
    		
    		vertices[count*18+0]=p0a[0];
    		vertices[count*18+1]=p0a[1];
    		vertices[count*18+2]=p0a[2];
    		
    		vertices[count*18+3]=p1a[0];
    		vertices[count*18+4]=p1a[1];
    		vertices[count*18+5]=p1a[2];
    		
    		vertices[count*18+6]=p3a[0];
    		vertices[count*18+7]=p3a[1];
    		vertices[count*18+8]=p3a[2];
    		
    		vertices[count*18+9]=p1a[0];
    		vertices[count*18+10]=p1a[1];
    		vertices[count*18+11]=p1a[2];
    		
    		vertices[count*18+12]=p2a[0];
    		vertices[count*18+13]=p2a[1];
    		vertices[count*18+14]=p2a[2];
    		
    		vertices[count*18+15]=p3a[0];
    		vertices[count*18+16]=p3a[1];
    		vertices[count*18+17]=p3a[2];
    		
    		
    		texCoor[count*12+0]=0;
    		texCoor[count*12+1]=1;
    		texCoor[count*12+2]=1;
    		texCoor[count*12+3]=1;
    		texCoor[count*12+4]=0;
    		texCoor[count*12+5]=0;
    		
    		texCoor[count*12+6]=1;
    		texCoor[count*12+7]=1;
    		texCoor[count*12+8]=1;
    		texCoor[count*12+9]=0;
    		texCoor[count*12+10]=0;
    		texCoor[count*12+11]=0;
    		
    		
    		 
    			float alphaTemp=pl.get(i).alpha;
        		alphaData[count*6+0]=alphaTemp;
        		alphaData[count*6+1]=alphaTemp;
        		alphaData[count*6+2]=alphaTemp;
        		alphaData[count*6+3]=alphaTemp;
        		alphaData[count*6+4]=alphaTemp;
        		alphaData[count*6+5]=alphaTemp;
    		 
    		
    	
    		
    		count++;
    		
    		pArrayBefore=pArray;
    		pArray=pArrayAfter;
    	}
    	
    	//頂點座標資料的起始化================begin===============================
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點座標資料的起始化================end=================================
        
        //頂點紋理座標資料的起始化================begin============================ 
        //建立頂點紋理座標資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTexCoorBuffer = cbb.asFloatBuffer();//轉為Float型緩沖
        mTexCoorBuffer.put(texCoor);//向緩沖區中放入頂點著色資料
        mTexCoorBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點紋理座標資料的起始化================end============================
        
        
        ByteBuffer abb = ByteBuffer.allocateDirect(alphaData.length*4);
        abb.order(ByteOrder.nativeOrder());//設定位元組順序
        mAlphaBuffer = abb.asFloatBuffer();//轉為Float型緩沖
        mAlphaBuffer.put(alphaData);//向緩沖區中放入頂點著色資料
        mAlphaBuffer.position(0);//設定緩沖區起始位置
        
        synchronized(lock)
        {
        	mVertexBufferG=mVertexBuffer;//頂點座標資料緩沖
        	mTexCoorBufferG=mTexCoorBuffer;//頂點紋理座標資料緩沖
        	mAlphaBufferG=mAlphaBuffer;
        }
    }    
	
	public void drawSelf(int texId)
	{       
		FloatBuffer   mVertexBuffer=null;//頂點座標資料緩沖
    	FloatBuffer   mTexCoorBuffer=null;//頂點紋理座標資料緩沖
    	FloatBuffer   mAlphaBuffer=null;//頂點透明度資料緩沖
        synchronized(lock)
        {
        	mVertexBuffer=mVertexBufferG;//頂點座標資料緩沖
        	mTexCoorBuffer=mTexCoorBufferG;//頂點紋理座標資料緩沖
        	mAlphaBuffer=mAlphaBufferG;
        }
        if(mVertexBuffer!=null&&mTexCoorBufferG!=null)
        {
        	GLES20.glEnable(GLES20.GL_BLEND);
        	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        	db.drawSelf(texId, mVertexBuffer, mTexCoorBuffer,mAlphaBuffer, vCount);
        	GLES20.glDisable(GLES20.GL_BLEND);
        }
	}
}
