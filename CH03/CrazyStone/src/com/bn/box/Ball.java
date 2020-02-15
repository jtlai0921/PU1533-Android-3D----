package com.bn.box;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.dynamics.Body;
import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class Ball extends MyBody{
	private FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
    private FloatBuffer   mTextureBuffer;//頂點紋理資料緩沖
    int vCount=0;//頂點數量    
	float scale;//球半徑
	boolean isStatic ;
	int id;
	public Ball(Body body,GameView gameview,boolean isStatic,int id,float radius)//建構器
	{
		super(body,gameview);
		this.body=body;
		this.id=id;
		this.isStatic=isStatic;
		scale=From2DTo3DUtil.k2d_3d(radius);//球半徑
    	final float angleSpan=11.25f;//將球進行單位切分的角度
    	//取得切分整圖的紋理陣列
    	float[] texCoorArray= 
         generateTexCoor
    	 (
    			 (int)(360/angleSpan), //紋理圖切分的列數
    			 (int)(180/angleSpan)  //紋理圖切分的行數
    	);
        int tc=0;//紋理陣列計數器
        int ts=texCoorArray.length;//紋理陣列長度
    	ArrayList<Float> alVertix=new ArrayList<Float>();//存放頂點座標的ArrayList
    	ArrayList<Float> alTexture=new ArrayList<Float>();//存放紋理座標的ArrayList
        for(float vAngle=90;vAngle>-90;vAngle=vAngle-angleSpan)//垂直方向angleSpan度一份
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-angleSpan)//水平方向angleSpan度一份
        	{
        		//直印水平各到一個角度後計算對應的此點在球面上的四邊形頂點座標
        		//並建構兩個群組成四邊形的三角形
        		//V1大木條
        		double xozLength=scale*Math.cos(Math.toRadians(vAngle));
        		float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y1=(float)(scale*Math.sin(Math.toRadians(vAngle)));
        		
        		xozLength=scale *Math.cos(Math.toRadians(vAngle-angleSpan));
        		float x2=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z2=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y2=(float)(scale *Math.sin(Math.toRadians(vAngle-angleSpan)));
        		
        		xozLength=scale *Math.cos(Math.toRadians(vAngle-angleSpan));
        		float x3=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
        		float z3=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
        		float y3=(float)(scale *Math.sin(Math.toRadians(vAngle-angleSpan)));
        		
        		xozLength=scale *Math.cos(Math.toRadians(vAngle));
        		float x4=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
        		float z4=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
        		float y4=(float)(scale *Math.sin(Math.toRadians(vAngle)));  
        		
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
        //建立頂點法向量資料緩沖
        ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length*4);
        nbb.order(ByteOrder.nativeOrder());//設定位元組順序
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
	public void drawSelf(GL10 gl)
    {
        gl.glPushMatrix();
    	float p[]=From2DTo3DUtil.point3D(body.getPosition().x,body.getPosition().y);
    	gl.glTranslatef(p[0],p[1] , 0);
        gl.glRotatef((float) (-body.getAngle()*180/Math.PI), 0, 0, 1);
        
        int textureId;
        if(isStatic)
        {
        	textureId=gameview.textureId_di;
        }
        else
        {
        	
        	if(id==1)
        	{
        		textureId=gameview.textureId_stone10;
        	}
        	else if(id==-1)
        	{
        		textureId=gameview.textureId_zhadan;
        	}
        	else
        	{
        		textureId=gameview.textureId_cebi;
        	}
        	
        }
        
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
        //容許使用紋理陣列
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //為畫筆指定紋理uv座標資料
        gl.glTexCoordPointer
        (
        		2, 					//每個頂點兩個紋理座標資料 S、T
        		GL10.GL_FLOAT, 		//資料型態
        		0, 					//連續紋理座標資料之間的間隔
        		mTextureBuffer		//紋理座標資料
        );
        //為畫筆綁定指定名稱ID紋理		
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);   
        //繪制圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 
        		0, 
        		vCount
        );
        gl.glPopMatrix();
    }
	
//自動切分紋理產生紋理陣列的方法
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

