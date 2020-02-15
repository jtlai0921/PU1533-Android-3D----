package com.bn.box;

import static com.bn.util.Constant.Height;
import static com.bn.util.Constant.RATIO;
import static com.bn.util.Constant.Width;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


import com.bn.util.Constant;
import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class TxingPicture 
{
	float width;
	float height;
	private FloatBuffer   mVertexBuffer1;//頂點座標資料緩沖
	private FloatBuffer   mVertexBuffer2;//頂點座標資料緩沖
	private FloatBuffer   mVertexBuffer3;//頂點座標資料緩沖
	private FloatBuffer   mVertexBuffer4;//頂點座標資料緩沖
	private FloatBuffer   mVertexBuffer5;//頂點座標資料緩沖
	private FloatBuffer   mVertexBuffer6;//頂點座標資料緩沖
	private FloatBuffer   mVertexBuffer7;//頂點座標資料緩沖
	private FloatBuffer   mVertexBuffer8;//頂點座標資料緩沖
    private FloatBuffer   mTextureBuffer;//頂點紋理資料緩沖
    int vCount=0;//頂點數量    
	public TxingPicture(float width,float height) {
		this.height=height;
		this.width=width;
		 vCount=6;//頂點的數量    	
	        float [][]vertices=new float[8][18];
	        vertices=vertices(width,height);
	        
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
	        
	        ByteBuffer vbb4 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb4.order(ByteOrder.nativeOrder());//設定位元組順序
	        mVertexBuffer4 = vbb4.asFloatBuffer();//轉為int型緩沖
	        mVertexBuffer4.put(vertices[3]);//向緩沖區中放入頂點座標資料
	        mVertexBuffer4.position(0);//設定緩沖區起始位置
	        
	        ByteBuffer vbb5 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb5.order(ByteOrder.nativeOrder());//設定位元組順序
	        mVertexBuffer5 = vbb5.asFloatBuffer();//轉為int型緩沖
	        mVertexBuffer5.put(vertices[4]);//向緩沖區中放入頂點座標資料
	        mVertexBuffer5.position(0);//4設定緩沖區起始位置
	        
	        ByteBuffer vbb6 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb6.order(ByteOrder.nativeOrder());//設定位元組順序
	        mVertexBuffer6 = vbb6.asFloatBuffer();//轉為int型緩沖
	        mVertexBuffer6.put(vertices[5]);//向緩沖區中放入頂點座標資料
	        mVertexBuffer6.position(0);//設定緩沖區起始位置
	        
	        ByteBuffer vbb7 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb7.order(ByteOrder.nativeOrder());//設定位元組順序
	        mVertexBuffer7 = vbb7.asFloatBuffer();//轉為int型緩沖
	        mVertexBuffer7.put(vertices[6]);//向緩沖區中放入頂點座標資料
	        mVertexBuffer7.position(0);//設定緩沖區起始位置
	        
	        ByteBuffer vbb8 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb8.order(ByteOrder.nativeOrder());//設定位元組順序
	        mVertexBuffer8 = vbb8.asFloatBuffer();//轉為int型緩沖
	        mVertexBuffer8.put(vertices[7]);//向緩沖區中放入頂點座標資料
	        mVertexBuffer8.position(0);//設定緩沖區起始位置
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
	
	public void drawSelf(GL10 gl,float x,float y,float angle,GameView gameview)
    {  
        gl.glPushMatrix();
        float p[]=From2DTo3DUtil.point3D(x,y);
        float z=0;
        gl.glTranslatef(p[0],p[1] , z);
        gl.glRotatef(angle, 0, 0, 1);
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone10);   
//	        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId);   
        //紋理===========end==================================================
        //繪制圖形
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
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
        		mVertexBuffer4	//頂點座標資料
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
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
        		mVertexBuffer6	//頂點座標資料
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone10);
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
        		mVertexBuffer7	//頂點座標資料
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
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
        		mVertexBuffer8	//頂點座標資料
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 
        		0, 
        		vCount
        );
        gl.glPopMatrix();
        
    }
	
	public static float[][] vertices(float width,float height)
	{
		float[][] vertices=new float[8][18];

		vertices[0][0]=0 ;
		vertices[0][1]=0 ;
		vertices[0][2]=Constant.Z_R/3;
		
		vertices[0][3]=0 ;
		vertices[0][4]=(-height)/(Height/2) ;
		vertices[0][5]=Constant.Z_R/3;
		
		vertices[0][6]=(width)/(Width/2) *RATIO ;
		vertices[0][7]=0;
		vertices[0][8]=Constant.Z_R/3;
		
		vertices[0][9]=(width)/(Width/2)*RATIO  ;
		vertices[0][10]=0 ;
		vertices[0][11]=Constant.Z_R/3;
		
		vertices[0][12]=0 ;
		vertices[0][13]=(-height)/(Height/2);
		vertices[0][14]=Constant.Z_R/3;
		
		vertices[0][15]=(width)/(Width/2) *RATIO ;
		vertices[0][16]=(-height)/(Height/2) ;
		vertices[0][17]=Constant.Z_R/3;
		
		
		vertices[1][0]=0 ;
		vertices[1][1]=0 ;
		vertices[1][2]=-Constant.Z_R/3;
		
		vertices[1][3]=0 ;
		vertices[1][4]=0 ;
		vertices[1][5]=Constant.Z_R/3;
		
		vertices[1][6]=(width)/(Width/2) *RATIO ;
		vertices[1][7]=0;
		vertices[1][8]=-Constant.Z_R/3;
		
		vertices[1][9]=(width)/(Width/2) *RATIO ;
		vertices[1][10]=0 ;
		vertices[1][11]=-Constant.Z_R/3;
		
		vertices[1][12]=0  ;
		vertices[1][13]=0 ;
		vertices[1][14]=Constant.Z_R/3;
		
		vertices[1][15]=(width)/(Width/2)*RATIO  ;
		vertices[1][16]=0 ;
		vertices[1][17]=Constant.Z_R/3;
		
		
		vertices[2][0]=(width)/(Width/2)*RATIO  ;
		vertices[2][1]=(-height)/(Height/2) ;
		vertices[2][2]=Constant.Z_R/3;
		
		vertices[2][3]=(width)/(Width/2)*RATIO  ;
		vertices[2][4]=(-height)/(Height/2) ;
		vertices[2][5]=-Constant.Z_R/3;
		
		vertices[2][6]=(width)/(Width/2)*RATIO  ;
		vertices[2][7]=0;
		vertices[2][8]=Constant.Z_R/3;
		
		vertices[2][9]=(width)/(Width/2) *RATIO ;
		vertices[2][10]=0 ;
		vertices[2][11]=Constant.Z_R/3;
		
		vertices[2][12]=(width)/(Width/2) *RATIO ;
		vertices[2][13]=(-height)/(Height/2) ;
		vertices[2][14]=-Constant.Z_R/3;
		
		vertices[2][15]=(width)/(Width/2)*RATIO  ;
		vertices[2][16]=0 ;
		vertices[2][17]=-Constant.Z_R/3;
		
		
		vertices[3][0]=0  ;
		vertices[3][1]=0 ;
		vertices[3][2]=Constant.Z_R/3;
		
		vertices[3][3]=0 ;
		vertices[3][4]=0 ;
		vertices[3][5]=-Constant.Z_R/3;
		
		vertices[3][6]=0 ;
		vertices[3][7]=(-height)/(Height/2);
		vertices[3][8]=Constant.Z_R/3;
		
		vertices[3][9]=0  ;
		vertices[3][10]=(-height)/(Height/2) ;
		vertices[3][11]=Constant.Z_R/3;
		
		vertices[3][12]=0 ;
		vertices[3][13]=0;
		vertices[3][14]=-Constant.Z_R/3;
		
		vertices[3][15]=0 ;
		vertices[3][16]=(-height)/(Height/2) ;
		vertices[3][17]=-Constant.Z_R/3;
		
		
		vertices[4][0]=0  ;
		vertices[4][1]=(-height)/(Height/2) ;
		vertices[4][2]=Constant.Z_R/3;
		
		vertices[4][3]=0 ;
		vertices[4][4]=(-height)/(Height/2) ;
		vertices[4][5]=-Constant.Z_R/3;
		
		vertices[4][6]=(width)/(Width/2) *RATIO ;
		vertices[4][7]=-height/(Height/2);
		vertices[4][8]=Constant.Z_R/3;
		
		vertices[4][9]=(width)/(Width/2) *RATIO ;
		vertices[4][10]=-height/(Height/2) ;
		vertices[4][11]=Constant.Z_R/3;
		
		vertices[4][12]=0 ;
		vertices[4][13]=(-height)/(Height/2) ;
		vertices[4][14]=-Constant.Z_R/3;
		
		vertices[4][15]=(width)/(Width/2)*RATIO  ;
		vertices[4][16]=(-height)/(Height/2) ;
		vertices[4][17]=-Constant.Z_R/3;
		
		
		vertices[5][0]=(width/3)/(Width/2)*RATIO  ;
		vertices[5][1]=0 ;
		vertices[5][2]=Constant.Z_R/3;
		
		vertices[5][3]=(width/3)/(Width/2)*RATIO  ;
		vertices[5][4]=(-width)/(Height/2) ;
		vertices[5][5]=Constant.Z_R/3;
		
		vertices[5][6]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[5][7]=0;
		vertices[5][8]=Constant.Z_R/3;
		
		vertices[5][9]=(width*2/3)/(Width/2) *RATIO ;
		vertices[5][10]=0 ;
		vertices[5][11]=Constant.Z_R/3;
		
		vertices[5][12]=(width/3)/(Width/2) *RATIO ;
		vertices[5][13]=(-width)/(Height/2) ;
		vertices[5][14]=Constant.Z_R/3;
		
		vertices[5][15]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[5][16]=(-width)/(Height/2) ;
		vertices[5][17]=Constant.Z_R/3;
		
		
		vertices[6][0]=(width/3)/(Width/2)*RATIO  ;
		vertices[6][1]=0 ;
		vertices[6][2]=-Constant.Z_R/3;
		
		vertices[6][3]=(width/3)/(Width/2)*RATIO  ;
		vertices[6][4]=(-width)/(Height/2) ;
		vertices[6][5]=-Constant.Z_R/3;
		
		vertices[6][6]=(width/3)/(Width/2)*RATIO  ;
		vertices[6][7]=0;
		vertices[6][8]=Constant.Z_R/3;
		
		vertices[6][9]=(width/3)/(Width/2) *RATIO ;
		vertices[6][10]=0 ;
		vertices[6][11]=Constant.Z_R/3;
		
		vertices[6][12]=(width/3)/(Width/2) *RATIO ;
		vertices[6][13]=(-width)/(Height/2) ;
		vertices[6][14]=-Constant.Z_R/3;
		
		vertices[6][15]=(width/3)/(Width/2)*RATIO  ;
		vertices[6][16]=(-width)/(Height/2) ;
		vertices[6][17]=Constant.Z_R/3;
		
		vertices[7][0]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[7][1]=0 ;
		vertices[7][2]=Constant.Z_R/3;
		
		vertices[7][3]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[7][4]=(-width)/(Height/2) ;
		vertices[7][5]=Constant.Z_R/3;
		
		vertices[7][6]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[7][7]=0;
		vertices[7][8]=-Constant.Z_R/3;
		
		vertices[7][9]=(width*2/3)/(Width/2) *RATIO ;
		vertices[7][10]=0 ;
		vertices[7][11]=-Constant.Z_R/3;
		
		vertices[7][12]=(width*2/3)/(Width/2) *RATIO ;
		vertices[7][13]=(-width)/(Height/2) ;
		vertices[7][14]=Constant.Z_R/3;
		
		vertices[7][15]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[7][16]=(-width)/(Height/2) ;
		vertices[7][17]=-Constant.Z_R/3;
		
		return vertices;
	}

}
