package tstc.fxq.utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import android.opengl.GLES20;

//儀表板類別
public class Board 
{	
	int mProgram;//自訂著色管執行緒序id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id
    int muIsTransparent;//儀表板是否透明的屬性參考id 
	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖 
    int vCount=0;
    
    //關於儀表板的成員變數
    float sBegin = 0;//儀表板左上角的s、t值
    float tBegin = 0;
    float sEnd;//儀表板右下角的s、t值
    float tEnd;
	int texId;//按下和抬起的儀表板圖片id
	int isTransparent;//儀表板是否透明的
    //延伸的觸控範圍
    float addedTouchScaleX;//延伸的x方向觸控範圍
    float addedTouchScaleY;//延伸的Y方向觸控範圍
	
	//3D世界中的變數
	float x;//儀表板在3D世界中的位置
	float y;
    float width;//儀表板在3D世界中的寬度和高度值
    float height;
	//2D世界中的變數
	float x2D;//對應螢幕中的左上角位置座標
	float y2D;
    float width2D;//儀表板在螢幕中的寬度和高度值
    float height2D;
    //2D到3D世界中尺寸的比例：3D = 2D*ratio2DTo3D
    float ratio2DTo3D;
    
    public Board(
    		MySurfaceView mv,
    		float x2D, float y2D, //虛擬儀表板對應螢幕中的左上角位置座標
    		float width2D, float height2D, //儀表板在螢幕中的寬度和高度值
    		float addedTouchScaleX, float addedTouchScaleY,//延伸的觸控範圍
    		float sEnd, float tEnd, //儀表板右下角的s、t值
    		int texId, //抬起的儀表板圖片id
    		int isTransparent
    )
    {
    	this.x2D = x2D;
    	this.y2D = y2D;
    	this.width2D = width2D;
    	this.height2D = height2D;
    	this.addedTouchScaleX = addedTouchScaleX;
    	this.addedTouchScaleY = addedTouchScaleY;
    	this.sEnd = sEnd;
    	this.tEnd = tEnd;
    	this.texId = texId;
    	this.isTransparent = isTransparent;
    	
    	//將儀表板的位置、尺寸轉換成3D世界中的位置和尺寸
    	changePositionAndScaleFrom2DTo3D();
    	//起始化頂點座標與著色資料
    	initVertexData();
    	//起始化shader        
    	intShader(mv);
    }
    public Board(
    		MySurfaceView mv,
    		float x2D, float y2D, //虛擬儀表板對應螢幕中的左上角位置座標
    		float width2D, float height2D, //儀表板在螢幕中的寬度和高度值
    		float addedTouchScaleX, float addedTouchScaleY,//延伸的觸控範圍
    		float sBegin, float tBegin, //儀表板左上角的s、t值
    		float sEnd, float tEnd, //儀表板右下角的s、t值
    		int texId //抬起的儀表板圖片id
    )
    {
    	this.x2D = x2D;
    	this.y2D = y2D;
    	this.width2D = width2D;
    	this.height2D = height2D;
    	this.addedTouchScaleX = addedTouchScaleX;
    	this.addedTouchScaleY = addedTouchScaleY;
    	this.sBegin = sBegin;
    	this.tBegin = tBegin;
    	this.sEnd = sEnd;
    	this.tEnd = tEnd;
    	this.texId = texId;
    	
    	//將儀表板的位置、尺寸轉換成3D世界中的位置和尺寸
    	changePositionAndScaleFrom2DTo3D();
    	//起始化頂點座標與著色資料
    	initVertexData();
    	//起始化shader        
    	intShader(mv);
    }
    //將儀表板的位置、尺寸轉換成3D世界中的位置和尺寸的方法
    public void changePositionAndScaleFrom2DTo3D(){
    	/*
    	 * 計算2D到3D世界中尺寸的比例
    	 * 
    	 * 根據正交投影矩陣中的參數值：
         * MatrixState.setProjectFrustum(-Constant.RATIO, Constant.RATIO, -1, 1, 1, 10);
         * 可算得轉換比例為：
         * ratio2DTo3D = (top+bottom)/Constant.SCREEN_HEIGHT
    	 */
    	ratio2DTo3D = 2.0f/Constant.SCREEN_HEIGHT;
    	//寬度高度等比例變換
    	width = width2D * ratio2DTo3D;
    	height = height2D * ratio2DTo3D;
    	/*
    	 * 螢幕的位置透過儀表板的左上點座標到2D和3D世界中的中心位置的距離成比例來計算
    	 * 3D世界中的中心位置在世界座標系原點
    	 * 2D世界中的中心位置在螢幕的正中央
    	 */
    	x = -(ratio2DTo3D * (Constant.SCREEN_WIDTH/2.0f - x2D) - width/2.0f);
    	y = ratio2DTo3D * (Constant.SCREEN_HEIGHT/2.0f - y2D) - height/2.0f;
    }
    //起始化頂點座標與著色資料的方法
    public void initVertexData()
    {
    	//頂點座標資料的起始化================begin============================
        vCount=6;
       
        float vertices[]=new float[]
        {
        	-width/2.0f, height/2.0f, 0,
        	-width/2.0f, -height/2.0f, 0,
        	width/2.0f, height/2.0f, 0,
        	
        	-width/2.0f, -height/2.0f, 0,
        	width/2.0f, -height/2.0f, 0,
        	width/2.0f, height/2.0f, 0,
        };
		
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點座標資料的起始化================end============================
        
        //頂點紋理座標資料的起始化================begin============================
        float texCoor[]=new float[]//紋理座標
        {
        		sBegin,tBegin, sBegin,tEnd, sEnd,tBegin,
        		sBegin,tEnd, sEnd,tEnd, sEnd,tBegin        		
        };        
        //建立頂點紋理座標資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTexCoorBuffer = cbb.asFloatBuffer();//轉為Float型緩沖
        mTexCoorBuffer.put(texCoor);//向緩沖區中放入頂點著色資料
        mTexCoorBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點紋理座標資料的起始化================end============================

    }

    //起始化shader
    public void intShader(MySurfaceView mv)
    {
    	//取得自訂著色管執行緒序id 
        mProgram = ShaderManager.getTexBoardShader();
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理座標屬性參考id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
        //取得程式中儀表板是否透明的屬性參考id
        muIsTransparent=GLES20.glGetUniformLocation(mProgram, "isTransparent"); 
    }
    
    public void drawSelf()
    {
    	//保護矩陣
    	MatrixState.pushMatrix();

		// 將儀表板搬移到3D世界中的指定位置
		MatrixState.translate(x, y, 0);

		// 制定使用某套shader程式
		GLES20.glUseProgram(mProgram);
		// 將最終變換矩陣傳入shader程式
		GLES20.glUniformMatrix4fv
		(
				muMVPMatrixHandle, 
				1, 
				false,
				MatrixState.getFinalMatrix(), 
				0
		);
		// 為畫筆指定頂點位置資料
		GLES20.glVertexAttribPointer
		(
				maPositionHandle, 
				3, 
				GLES20.GL_FLOAT,
				false, 
				3 * 4, 
				mVertexBuffer
		);
		// 為畫筆指定頂點紋理座標資料
		GLES20.glVertexAttribPointer
		(
				maTexCoorHandle, 
				2, 
				GLES20.GL_FLOAT,
				false, 
				2 * 4, 
				mTexCoorBuffer
		);
		// 容許頂點位置資料陣列
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		// 綁定紋理
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		
		//傳遞被按下的圖片id
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
		//將儀表板是否透明的屬性傳入shader程式 
        GLES20.glUniform1i(muIsTransparent, isTransparent);
		// 繪制三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		
		//還原矩陣
        MatrixState.popMatrix(); 
	}
    //重新起始化紋理座標的方法
    public void initTexCoor(float sBegin, float tBegin,float sEnd,float tEnd)
    {
        //頂點紋理座標資料的起始化================begin============================
        float texCoor[]=new float[]//紋理座標
        {
        		sBegin,tBegin, sBegin,tEnd, sEnd,tBegin,
        		sBegin,tEnd, sEnd,tEnd, sEnd,tBegin        		
        };        
        //建立頂點紋理座標資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTexCoorBuffer = cbb.asFloatBuffer();//轉為Float型緩沖
        mTexCoorBuffer.put(texCoor);//向緩沖區中放入頂點著色資料
        mTexCoorBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點紋理座標資料的起始化================end============================
        
    }
    //改變紋理座標，繪制移表板的方法
    public void drawSelf(float sBegin, float tBegin,float sEnd,float tEnd)
    {
    	//重新起始化紋理座標資料
    	initTexCoor(sBegin, tBegin, sEnd, tEnd);    	
    	//保護矩陣
    	MatrixState.pushMatrix();
		// 將儀表板搬移到3D世界中的指定位置
		MatrixState.translate(x, y, 0);

		// 制定使用某套shader程式
		GLES20.glUseProgram(mProgram);
		// 將最終變換矩陣傳入shader程式
		GLES20.glUniformMatrix4fv
		(
				muMVPMatrixHandle, 
				1, 
				false,
				MatrixState.getFinalMatrix(), 
				0
		);
		// 為畫筆指定頂點位置資料
		GLES20.glVertexAttribPointer
		(
				maPositionHandle, 
				3, 
				GLES20.GL_FLOAT,
				false, 
				3 * 4, 
				mVertexBuffer
		);
		// 為畫筆指定頂點紋理座標資料
		GLES20.glVertexAttribPointer
		(
				maTexCoorHandle, 
				2, 
				GLES20.GL_FLOAT,
				false, 
				2 * 4, 
				mTexCoorBuffer
		);
		// 容許頂點位置資料陣列
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		// 綁定紋理
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		
		//傳遞被按下的圖片id
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

		// 繪制三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		
		//還原矩陣
        MatrixState.popMatrix(); 
	}
	//判斷儀表板上是否有觸控事件的方法
	public boolean isActionOnBoard(float pressX, float pressY)
	{
		if(
				pressX > x2D - addedTouchScaleX &&
				pressX < x2D + width2D + addedTouchScaleX && 
				pressY > y2D - addedTouchScaleY &&
				pressY < y2D + height2D + addedTouchScaleY
		)
		{
			return true;			
		}
		return false;
	}
}
