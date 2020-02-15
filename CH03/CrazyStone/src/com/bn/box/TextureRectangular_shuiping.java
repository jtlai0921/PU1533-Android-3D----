package com.bn.box;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import com.bn.util.From2DTo3DUtil;

public class TextureRectangular_shuiping 
{
	private FloatBuffer mVertexBuffer;//頂點座標資料緩沖
    private FloatBuffer mTextureBuffer;//頂點紋理資料緩沖
    int vCount;
    
    public TextureRectangular_shuiping(float width,float height)
    {
    	float vertices[]=From2DTo3DUtil.Vertices_heng(width,height);
    	this.vCount=vertices.length/3;
    	
    	ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
    	vbb.order(ByteOrder.nativeOrder());//設定位元組順序
    	mVertexBuffer = vbb.asFloatBuffer();//轉為int型緩沖
    	mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        float textureCoors[]=
    	{
    		0,0,
    		0,1,
    		1,0,
    		1,0,
    		0,1,
    		1,1
    	};
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);//建立頂點紋理資料緩沖
        tbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTextureBuffer= tbb.asFloatBuffer();//轉為Float型緩沖
        mTextureBuffer.put(textureCoors);//向緩沖區中放入頂點著色資料
        mTextureBuffer.position(0);//設定緩沖區起始位置
    }
    
    public void drawSelf(GL10 gl,float angle,int textureId,float[] point)
    {
    	gl.glPushMatrix();	
    	gl.glTranslatef(point[0], point[1], -0.05f);
        gl.glRotatef((float) (-angle*180/Math.PI), 0, 0, 1);

    	
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點座標陣列
    	
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
}
