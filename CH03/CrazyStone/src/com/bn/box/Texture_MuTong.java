package com.bn.box;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class Texture_MuTong 
{
	private FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
    private FloatBuffer mTextureBuffer;//頂點紋理資料緩沖

    int vCount=0;//頂點數量
    float r;//圓柱半徑
    float length;//圓柱長度
    float aspan;//切分角度
    float lspan;//切分長度  
    GameView gameview;
    
	public Texture_MuTong(GameView gameview,float radius,float height)
	{
		this.gameview=gameview;
		this.r=From2DTo3DUtil.k2d_3d(radius);
    	this.length=From2DTo3DUtil.k2d_3d(height);
    	this.aspan=15;
    	this.lspan=From2DTo3DUtil.k2d_3d(15);
    	
    	//取得切分整圖的紋理陣列
    	float[] texCoorArray= 
         generateTexCoor
    	(
    			 (int)(360/aspan), //紋理圖切分的列數
    			 (int)(length/lspan)  //紋理圖切分的行數
    	);
        int tc=0;//紋理陣列計數器
        int ts=texCoorArray.length;//紋理陣列長度
    	
    	ArrayList<Float> alVertix=new ArrayList<Float>();//存放頂點座標的ArrayList
    	ArrayList<Float> alTexture=new ArrayList<Float>();//存放紋理座標的ArrayList
    	
        for(float tempY=length/2;tempY>-length/2;tempY=tempY-lspan)//垂直方向lspan長度一份
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-aspan)//水平方向angleSpan度一份
        	{
        		//直印水平各到一個角度後計算對應的此點在球面上的四邊形頂點座標
        		//並建構兩個群組成四邊形的三角形
        		
        		
        		float x1=(float)(r*Math.cos(Math.toRadians(hAngle)));
        		float y1=tempY;
        		float z1=(float)(r*Math.sin(Math.toRadians(hAngle)));
        		
        		float x2=(float)(r*Math.cos(Math.toRadians(hAngle)));
        		float y2=tempY-lspan;
        		float z2=(float)(r*Math.sin(Math.toRadians(hAngle)));
        		
        		float x3=(float)(r*Math.cos(Math.toRadians(hAngle-aspan)));
        		float y3=tempY-lspan;
        		float z3=(float)(r*Math.sin(Math.toRadians(hAngle-aspan)));
        		
        		float x4=(float)(r*Math.cos(Math.toRadians(hAngle-aspan)));
        		float y4=tempY;
        		float z4=(float)(r*Math.sin(Math.toRadians(hAngle-aspan))); 
        		
        		//建構第一三角形
        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);        		
        		//建構第二三角形
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3); 
        		
        		//第一三角形3個頂點的6個紋理座標
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		//第二三角形3個頂點的6個紋理座標
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);       		
        	}
        } 	
        vCount=alVertix.size()/3;//頂點的數量為座標值數量的1/3，因為一個頂點有3個座標
    	
        //將alVertix中的座標值轉存到一個int陣列中
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
        float textureCoors[]=new float[alTexture.size()];//頂點紋理值陣列
        for(int i=0;i<alTexture.size();i++) 
        {
        	textureCoors[i]=alTexture.get(i);
        }
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTextureBuffer = tbb.asFloatBuffer();//轉為int型緩沖
        mTextureBuffer.put(textureCoors);//向緩沖區中放入頂點著色資料
        mTextureBuffer.position(0);//設定緩沖區起始位置
        
	}
	public void drawSelf(GL10 gl,float angle,float x,float y,int id)
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
        
        gl.glPushMatrix();
        //繪制圖形
        float p[]=From2DTo3DUtil.point3D(x,y);
       
        gl.glTranslatef(p[0], p[1], -From2DTo3DUtil.k2d_3d(15f));
        gl.glRotatef((float) (-angle*180/Math.PI), 0, 0, 1);

        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//以三角形模式填充
        		0, 			 			//開始點編號
        		vCount					//頂點數量
        );        
        gl.glPopMatrix();
    }
	
	public float[] generateTexCoor(int bw,int bh)
    {
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
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s+sizew;
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
