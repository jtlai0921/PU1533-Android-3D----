package tstc.fxq.utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import android.opengl.GLES20;

/*
 * 3D中的虛擬按鈕類別
 * 
 * 注意：
 * 建立該物件時必須在Constant.SCREEN_WIDTH和Constant.SCREEN_HEIGHT確定之後。
 * 
 * 在呼叫該類別的drawSelf方法之前，要將投影模式設為正交投影，並設定相機的位置：
 * MatrixState.setProjectOrtho(-Constant.RATIO, Constant.RATIO, -1, 1, 1, 2);
 * MatrixState.setCamera(0,0,1f,0f,0f,0f,0f,1.0f,0.0f);
 * 繪制完按鈕之後，再還原原來的投影模式和相機位置
 */
public class VirtualButton 
{	
	int mProgram;//自訂著色管執行緒序id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id
    int muIsDown;//按鈕是否被按下的屬性參考id 
	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖 
    int vCount=0;
    
    //關於按鈕的成員變數
    float sEnd;//按鈕右下角的s、t值
    float tEnd;
	private int isDown = 0;//按鈕是否被按下的標志位，0表示沒有按下，1表示按下
	int upTexId;//按下和抬起的按鈕圖片id

    //延伸的觸控範圍
    float addedTouchScaleX;//延伸的x方向觸控範圍
    float addedTouchScaleY;//延伸的Y方向觸控範圍
    
	//3D世界中的變數
	float x;//按鈕在3D世界中的位置
	float y;
    float width;//按鈕在3D世界中的寬度和高度值
    float height;
	//2D世界中的變數
	float x2D;//對應螢幕中的左上角位置座標
	float y2D;
    float width2D;//按鈕在螢幕中的寬度和高度值
    float height2D;
    //2D到3D世界中尺寸的比例：3D = 2D*ratio2DTo3D
    float ratio2DTo3D;
    
    public VirtualButton(
    		MySurfaceView mv,
    		float x2D, float y2D, //虛擬按鈕對應螢幕中的左上角位置座標
    		float width2D, float height2D, //按鈕在螢幕中的寬度和高度值
    		float addedTouchScaleX, float addedTouchScaleY,//延伸的觸控範圍
    		float sEnd, float tEnd, //按鈕右下角的s、t值。左上角的s、t值為0。所以紋理圖中按鈕應在左上角
    		int upTexId //抬起的按鈕圖片id
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
    	this.upTexId = upTexId;
    	
    	//將按鈕的位置、尺寸轉換成3D世界中的位置和尺寸
    	changePositionAndScaleFrom2DTo3D();
    	//起始化頂點座標與著色資料
    	initVertexData();
    	//起始化shader        
    	intShader(mv);
    }

    //將按鈕的位置、尺寸轉換成3D世界中的位置和尺寸的方法
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
    	 * 螢幕的位置透過按鈕的左上點座標到2D和3D世界中的中心位置的距離成比例來計算
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
        		0,0, 0,tEnd, sEnd,0,
        		0,tEnd, sEnd,tEnd, sEnd,0        		
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
        mProgram = ShaderManager.getTexBtnShader();
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理座標屬性參考id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
        
        //取得程式中按鈕是否被按下的屬性參考id
        muIsDown=GLES20.glGetUniformLocation(mProgram, "isDown"); 
    }
    
    public void drawSelf()
    {
    	//保護矩陣
    	MatrixState.pushMatrix();

		// 將按鈕搬移到3D世界中的指定位置
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
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, upTexId);
        //將按鈕是否被按下屬性傳入shader程式 
        GLES20.glUniform1i(muIsDown, isDown);

		// 繪制三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		
		//還原矩陣
        MatrixState.popMatrix(); 
	}
    //按下按鈕的方法
	public void pressDown()
	{
		isDown = 1;
	}
	//松開按鈕的方法
	public void releaseUp()
	{
		isDown = 0;
	}
	//判斷按鈕是否被按下的方法
	public boolean isBtnPressedDown() {
		return (isDown == 1);
	}
	//判斷按鈕是否有觸控事件的方法
	public boolean isActionOnButton(float pressX, float pressY)
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
