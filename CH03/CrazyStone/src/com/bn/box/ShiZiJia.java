package com.bn.box;


import static com.bn.util.Constant.Height;
import static com.bn.util.Constant.RATIO;
import static com.bn.util.Constant.Width;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.dynamics.Body;

import com.bn.util.Constant;
import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class ShiZiJia extends MyBody
{
	float width;//寬度
	float height;//高度
	int id;//紋理id
	boolean isStatic;//是否靜態
	private FloatBuffer   mVertexBuffer1;//頂點座標資料緩沖
	private FloatBuffer   mVertexBuffer2;//頂點座標資料緩沖
	private FloatBuffer   mVertexBuffer3;//頂點座標資料緩沖
    private FloatBuffer   mTextureBuffer;//頂點紋理資料緩沖
    int vCount=0;//頂點數量    
    
	public ShiZiJia(Body body, GameView gameview,float width,float height,boolean isStatic) {
		super(body, gameview);
		this.height=height;//取得高度
		this.width=width;//取得寬度
		this.isStatic=isStatic;//是否靜態
		 vCount=6;//頂點的數量    	
        float [][]vertices=new float[3][18];//定點陣列
        vertices=vertices(width,height);//取得定點陣列
       
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb1 = ByteBuffer.allocateDirect(vertices[0].length*4);
        vbb1.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer1 = vbb1.asFloatBuffer();//轉為int型緩沖
        mVertexBuffer1.put(vertices[0]);//向緩沖區中放入頂點座標資料
        mVertexBuffer1.position(0);//設定緩沖區起始位置
        
        ByteBuffer vbb2 = ByteBuffer.allocateDirect(vertices[0].length*4);
        vbb2.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer2 = vbb2.asFloatBuffer();//轉為int型緩沖
        mVertexBuffer2.put(vertices[1]);//向緩沖區中放入頂點座標資料
        mVertexBuffer2.position(0);//設定緩沖區起始位置
        
        ByteBuffer vbb3 = ByteBuffer.allocateDirect(vertices[0].length*4);
        vbb3.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer3 = vbb3.asFloatBuffer();//轉為int型緩沖
        mVertexBuffer3.put(vertices[2]);//向緩沖區中放入頂點座標資料
        mVertexBuffer3.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點座標資料的起始化================end============================
        
        //頂點紋理資料的起始化================begin============================
        float textureCoors[]=//頂點紋理S、T座標值陣列
	    {
        	0,0,//1
        	0,1,
        	1f,0,
        	1,0,
        	0,1,
        	1,1,
	    };                
        
        //建立頂點紋理資料緩沖
        //textureCoors.length×4是因為一個float型整數四個位元組
        ByteBuffer cbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTextureBuffer = cbb.asFloatBuffer();//轉為int型緩沖
        mTextureBuffer.put(textureCoors);//向緩沖區中放入頂點著色資料
        mTextureBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點紋理資料的起始化================end============================
	}
	
	public void drawSelf(GL10 gl)
	{  
	    gl.glPushMatrix();
	    
	    float p[]=From2DTo3DUtil.point3D(body.getPosition().x,body.getPosition().y);
	    float z=0;
	    gl.glTranslatef(p[0],p[1] , z);
        gl.glRotatef((float) (-body.getAngle()*180/Math.PI), 0, 0, 1);

	    //頂點座標==========begin=============================================
	    //容許使用頂點陣列
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//為畫筆指定頂點座標資料
	    gl.glVertexPointer
	    (
	    		3,				//每個頂點的座標數量為3  xyz 
	    		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
	    		0, 				//連續頂點座標資料之間的間隔
	    		mVertexBuffer1	//頂點座標資料
	    );
	    //頂點座標==========end===============================================
	    
	    //紋理===========begin================================================
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
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);   
	    //紋理===========end==================================================
	    //繪制圖形
	    gl.glDrawArrays
	    (
	    		GL10.GL_TRIANGLES, 
	    		0, 
	    		vCount
	    );
	    gl.glRotatef(180, 0, 1, 0);
	    gl.glDrawArrays
	    (
	    		GL10.GL_TRIANGLES, 
	    		0, 
	    		vCount
	    );
	    //上面的圖繪制完成
	    
	    //開始繪制前側面
	    gl.glVertexPointer
	    (
	    		3,				//每個頂點的座標數量為3  xyz 
	    		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
	    		0, 				//連續頂點座標資料之間的間隔
	    		mVertexBuffer2	//頂點座標資料
	    );
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone2);
	    gl.glDrawArrays
	    (
	    		GL10.GL_TRIANGLES, 
	    		0, 
	    		vCount
	    );
	    gl.glRotatef(180, 0, 0, 1);
	    gl.glDrawArrays
	    (
	    		GL10.GL_TRIANGLES, 
	    		0, 
	    		vCount
	    );
	    
	    gl.glVertexPointer
	    (
	    		3,				//每個頂點的座標數量為3  xyz 
	    		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
	    		0, 				//連續頂點座標資料之間的間隔
	    		mVertexBuffer3	//頂點座標資料
	    );
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone2);
	    gl.glDrawArrays
	    (
	    		GL10.GL_TRIANGLES, 
	    		0, 
	    		vCount
	    );
	    gl.glRotatef(180, 0, 0, 1);
	    gl.glDrawArrays
	    (
	    		GL10.GL_TRIANGLES, 
	    		0, 
	    		vCount
	    );
	    
	    gl.glPopMatrix();
		    gl.glPushMatrix();
		    float p1[]=From2DTo3DUtil.point3D(body.getPosition().x,body.getPosition().y);
		    float z1=0;
		    gl.glTranslatef(p1[0],p1[1] , z1);
	        gl.glRotatef((float) (-body.getAngle()*180/Math.PI+90), 0, 0, 1);

		    //頂點座標==========begin=============================================
		    //容許使用頂點陣列
		    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			//為畫筆指定頂點座標資料
		    gl.glVertexPointer
		    (
		    		3,				//每個頂點的座標數量為3  xyz 
		    		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
		    		0, 				//連續頂點座標資料之間的間隔
		    		mVertexBuffer1	//頂點座標資料
		    );
		    //頂點座標==========end===============================================
		    
		    //紋理===========begin================================================
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
		    gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);   
		    //紋理===========end==================================================
		    //繪制圖形
		    gl.glDrawArrays
		    (
		    		GL10.GL_TRIANGLES, 
		    		0, 
		    		vCount
		    );
		    gl.glRotatef(180, 0, 1, 0);
		    gl.glDrawArrays
		    (
		    		GL10.GL_TRIANGLES, 
		    		0, 
		    		vCount
		    );
		    //上面的圖繪制完成
		    
		    //開始繪制前側面
		    gl.glVertexPointer
		    (
		    		3,				//每個頂點的座標數量為3  xyz 
		    		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
		    		0, 				//連續頂點座標資料之間的間隔
		    		mVertexBuffer2	//頂點座標資料
		    );
		    gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone2);
		    gl.glDrawArrays
		    (
		    		GL10.GL_TRIANGLES, 
		    		0, 
		    		vCount
		    );
		    gl.glRotatef(180, 0, 0, 1);
		    gl.glDrawArrays
		    (
		    		GL10.GL_TRIANGLES, 
		    		0, 
		    		vCount
		    );
		    
		    
		    gl.glVertexPointer
		    (
		    		3,				//每個頂點的座標數量為3  xyz 
		    		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
		    		0, 				//連續頂點座標資料之間的間隔
		    		mVertexBuffer3	//頂點座標資料
		    );
		    gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone2);
		    gl.glDrawArrays
		    (
		    		GL10.GL_TRIANGLES, 
		    		0, 
		    		vCount
		    );
		    gl.glRotatef(180, 0, 0, 1);
		    gl.glDrawArrays
		    (
		    		GL10.GL_TRIANGLES, 
		    		0, 
		    		vCount
		    );
		    
		    gl.glPopMatrix();
	    
	}

	public void doAction()
	{
		
	}

	public static float[][] vertices(float width,float height)
	{
		float[][] vertices=new float[3][18];
		float halfwidth=width/2;
		float halfheight=height/2;
		vertices[0][0]=(-halfwidth)/(Width/2) *RATIO ;
		vertices[0][1]=(halfheight)/(Height/2) ;
		vertices[0][2]=Constant.Z_R/2;
		
		vertices[0][3]=(-halfwidth)/(Width/2)*RATIO  ;
		vertices[0][4]=(-halfheight)/(Height/2) ;
		vertices[0][5]=Constant.Z_R/2;
		
		vertices[0][6]=(halfwidth)/(Width/2) *RATIO ;
		vertices[0][7]=halfheight/(Height/2);
		vertices[0][8]=Constant.Z_R/2;
		
		vertices[0][9]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[0][10]=halfheight/(Height/2) ;
		vertices[0][11]=Constant.Z_R/2;
		
		vertices[0][12]=(-halfwidth)/(Width/2)*RATIO  ;
		vertices[0][13]=(-halfheight)/(Height/2);
		vertices[0][14]=Constant.Z_R/2;
		
		vertices[0][15]=(halfwidth)/(Width/2) *RATIO ;
		vertices[0][16]=(-halfheight)/(Height/2) ;
		vertices[0][17]=Constant.Z_R/2;
		
		
		vertices[1][0]=(-halfwidth)/(Width/2)*RATIO  ;
		vertices[1][1]=(-halfheight)/(Height/2) ;
		vertices[1][2]=Constant.Z_R/2;
		
		vertices[1][3]=(-halfwidth)/(Width/2) *RATIO ;
		vertices[1][4]=(-halfheight)/(Height/2) ;
		vertices[1][5]=-Constant.Z_R/2;
		
		vertices[1][6]=(halfwidth)/(Width/2) *RATIO ;
		vertices[1][7]=-halfheight/(Height/2);
		vertices[1][8]=Constant.Z_R/2;
		
		vertices[1][9]=(halfwidth)/(Width/2) *RATIO ;
		vertices[1][10]=-halfheight/(Height/2) ;
		vertices[1][11]=Constant.Z_R/2;
		
		vertices[1][12]=(-halfwidth)/(Width/2)*RATIO  ;
		vertices[1][13]=(-halfheight)/(Height/2) ;
		vertices[1][14]=-Constant.Z_R/2;
		
		vertices[1][15]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[1][16]=(-halfheight)/(Height/2) ;
		vertices[1][17]=-Constant.Z_R/2;
		
		
		vertices[2][0]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[2][1]=(-halfheight)/(Height/2) ;
		vertices[2][2]=Constant.Z_R/2;
		
		vertices[2][3]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[2][4]=(-halfheight)/(Height/2) ;
		vertices[2][5]=-Constant.Z_R/2;
		
		vertices[2][6]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[2][7]=halfheight/(Height/2);
		vertices[2][8]=Constant.Z_R/2;
		
		vertices[2][9]=(halfwidth)/(Width/2) *RATIO ;
		vertices[2][10]=halfheight/(Height/2) ;
		vertices[2][11]=Constant.Z_R;
		
		vertices[2][12]=(halfwidth)/(Width/2) *RATIO ;
		vertices[2][13]=(-halfheight)/(Height/2) ;
		vertices[2][14]=-Constant.Z_R/2;
		
		vertices[2][15]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[2][16]=(halfheight)/(Height/2) ;
		vertices[2][17]=-Constant.Z_R/2;
		return vertices;
	}
    
}
