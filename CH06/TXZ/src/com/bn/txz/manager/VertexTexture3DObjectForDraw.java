package com.bn.txz.manager;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;
import com.bn.txz.Constant;

//只有頂點與紋理座標的物體
public class VertexTexture3DObjectForDraw
{
	public FloatBuffer mVertexBuffer; //頂點座標資料緩沖
	public FloatBuffer mTexBuffer; //頂點紋理座標緩沖
	public int vCount = 0; //頂點數
	
	//vertices頂點  texCoor紋理ST座標
	public VertexTexture3DObjectForDraw(FloatBuffer mVertexBuffer,FloatBuffer mTexBuffer,int vCount) 
	{
		this.mVertexBuffer=mVertexBuffer;
		this.mTexBuffer=mTexBuffer;  
		this.vCount=vCount;
	}

	public void drawSelf(GL10 gl, int texId) {

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//容許使用頂點陣列
		gl.glEnable(GL10.GL_TEXTURE_2D); //開啟紋理
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); //容許使用紋理ST座標緩沖
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);	//為畫筆指定頂點座標資料  
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);  //為畫筆指定紋理ST座標緩沖
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);   //綁定目前紋理
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount); //繪制圖形
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//關閉使用頂點陣列
		gl.glDisableClientState(GL10.GL_TEXTURE_2D); //關閉紋理
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//關閉使用紋理ST座標緩沖
	}
	
	int xOffset;//x平移量
    int zOffset;//z平移量
    float scale;//星星尺寸
    IntBuffer mColorBuffer;//頂點著色資料
	public VertexTexture3DObjectForDraw(int xOffset,int zOffset,float scale,
			FloatBuffer mVertexBuffer,IntBuffer mColorBuffer,int vCount)
	{
		this.xOffset=xOffset;
		this.zOffset=zOffset;
		this.scale=scale;
		this.mVertexBuffer=mVertexBuffer;
		this.mColorBuffer=mColorBuffer;
		this.vCount=vCount;
	}
	public void drawSelf(GL10 gl)
    {        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點座標陣列
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);//啟用頂點彩色陣列
        
        gl.glPointSize(scale);//設定星星尺寸
        gl.glTranslatef(xOffset*Constant.UNIT_SIZE, 0, 0);//x向偏移
        gl.glTranslatef(0, 0, zOffset*Constant.UNIT_SIZE);//y向偏移
        
		//為畫筆指定頂點座標資料
        gl.glVertexPointer
        (
        		3,				//每個頂點的座標數量為3  xyz 
        		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
        		0, 				//連續頂點座標資料之間的間隔
        		mVertexBuffer	//頂點座標資料
        );        
        
        //為畫筆指定頂點著色資料
        gl.glColorPointer
        (
        		4, 				//設定彩色的群組成成分，必須為4—RGBA
        		GL10.GL_FIXED, 	//頂點彩色值的型態為 GL_FIXED
        		0, 				//連續頂點著色資料之間的間隔
        		mColorBuffer	//頂點著色資料
        );
		
        //繪制 點
        gl.glDrawArrays
        (
        		GL10.GL_POINTS, 		//以點模式填充
        		0, 			 			//開始點編號
        		vCount					//頂點的數量
        );
        
        gl.glPointSize(1);//還原像素尺寸
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//關閉使用頂點陣列
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);//關閉使用彩色陣列
    }
}
