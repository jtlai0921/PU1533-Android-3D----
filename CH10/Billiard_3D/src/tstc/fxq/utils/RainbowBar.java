package tstc.fxq.utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import android.opengl.GLES20;

/*
 * 彩虹條
 */
public class RainbowBar 
{	
	//力道條的最大力道對應的n值
    public static final int N_MAX = ColorUtil.result.length;
    //力道的最大值
	public static final float MAX_STRENGTH = Constant.V_BALL_MAX;
	
    MySurfaceView mv;//MySurfaceView類別的參考
    
	int mProgram;//自訂著色管執行緒序id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maColorHandle; //頂點彩色屬性參考id
	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mColorBuffer;//頂點著色資料緩沖
    
    int currN=N_MAX;//目前n值
    private float currStrength;//目前力道值

    //延伸的觸控範圍
    float addedTouchScaleX;//延伸的x方向觸控範圍
    float addedTouchScaleY;//延伸的Y方向觸控範圍
    
    //3D世界中的變數
	float x;//儀表板在3D世界中的位置
	float y;
    float width;//儀表板在3D世界中的寬度和高度值
    float height;
    
    float perHeight;//每個彩虹塊的高度
    float gapSize;//彩虹條每兩個彩虹塊之間的縫隙尺寸
    
    float gapRatio;//縫隙占的比例:gapRatio == gapSize / (perHeight + gapSize)
	//2D世界中的變數
	float x2D;//對應螢幕中的左上角位置座標
	float y2D;
    float width2D;//儀表板在螢幕中的寬度和高度值
    float height2D;
    
    float perHeight2D;//每個彩虹塊在螢幕中的高度
    float gapSize2D;//彩虹條每兩個彩虹塊之間在螢幕中的縫隙尺寸
    //2D到3D世界中尺寸的比例：3D = 2D*ratio2DTo3D
    float ratio2DTo3D;
    
    public RainbowBar(
    		MySurfaceView mv,
    		float x2D, float y2D, //虛擬儀表板對應螢幕中的左上角位置座標
    		float width2D, float height2D, //儀表板在螢幕中的寬度和高度值
    		float addedTouchScaleX, float addedTouchScaleY,//延伸的觸控範圍
    		float gapRatio//縫隙占的比例
    )
    {
    	this.x2D = x2D;
    	this.y2D = y2D;
    	this.width2D = width2D;
    	this.height2D = height2D;
    	this.addedTouchScaleX = addedTouchScaleX;
    	this.addedTouchScaleY = addedTouchScaleY;
    	this.gapRatio = gapRatio;

    	//計算2D世界每個彩虹塊的高度和彩虹塊間的間隔
    	calPerSize();
    	//將儀表板的位置、尺寸轉換成3D世界中的位置和尺寸
    	changePositionAndScaleFrom2DTo3D();
    	//起始化頂點座標與著色資料
    	initVertexData();
    	//起始化shader        
    	intShader(mv);
    }
    
    //計算每個彩虹塊的高度和彩虹塊間的間隔的方法
    public void calPerSize(){
    	/*
    	 * 透過方程式群組求解g和h
    	 * g*(n-1)+h*n=H
    	 * g/(h+g)=r
    	 */
    	//解縫隙尺寸
		gapSize2D = height2D / ((N_MAX - 1) + (1/gapRatio - 1) * N_MAX);
		//解每塊高度
		perHeight2D = gapSize2D * (1/gapRatio - 1);
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
        perHeight = perHeight2D * ratio2DTo3D;
        gapSize = gapSize2D * ratio2DTo3D;
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
    	
        List<Float>vertexList=new ArrayList<Float>();//頂點座標
        List<Float>colorList=new ArrayList<Float>();//頂點彩色
        
        //頂點
        for(int i=0;i<RainbowBar.N_MAX;i++)
        {
        	vertexList.add(-width/2); vertexList.add((i+1)*perHeight + i*gapSize); vertexList.add(0f);
        	vertexList.add(-width/2); vertexList.add(i*(perHeight+gapSize)); vertexList.add(0f);
        	vertexList.add(width/2); vertexList.add((i+1)*perHeight + i*gapSize); vertexList.add(0f);
        	
        	vertexList.add(-width/2); vertexList.add(i*(perHeight+gapSize)); vertexList.add(0f);
        	vertexList.add(width/2); vertexList.add(i*(perHeight+gapSize)); vertexList.add(0f);
        	vertexList.add(width/2); vertexList.add((i+1)*perHeight + i*gapSize); vertexList.add(0f);
        }
        
        //彩色
        for(int i=0;i<RainbowBar.N_MAX;i++)
        {        	
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);      	
        }

		//頂點座標緩沖
		float []vertices=new float[vertexList.size()];
		for(int i=0;i<vertexList.size();i++)
		{
			vertices[i]=vertexList.get(i);
		}
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點座標資料的起始化================end============================
        
        
        
        //頂點著色資料的起始化================begin============================
        
		float []colors=new float[colorList.size()];
		for(int i=0;i<colorList.size();i++)
		{
			colors[i]=colorList.get(i);
		}

        //建立頂點著色資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mColorBuffer = cbb.asFloatBuffer();//轉為Float型緩沖
        mColorBuffer.put(colors);//向緩沖區中放入頂點著色資料
        mColorBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點著色資料的起始化================end============================
    }

    //起始化shader
    public void intShader(MySurfaceView mv)
    {
    	//取得自訂著色管執行緒序id 
        mProgram = ShaderManager.getColorShader();
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點彩色屬性參考id  
        maColorHandle= GLES20.glGetAttribLocation(mProgram, "aColor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
    
    public void drawSelf()
    {
		// 保護矩陣
		MatrixState.pushMatrix();
		// 將儀表板搬移到3D世界中的指定位置
		MatrixState.translate(x, y - height/2, 0);

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
		// 為畫筆指定頂點著色資料
		GLES20.glVertexAttribPointer
		(
				maColorHandle, 
				4, 
				GLES20.GL_FLOAT, 
				false,
				4 * 4, 
				mColorBuffer
		);
		// 容許頂點位置資料陣列
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maColorHandle);
		// 繪制三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, currN * 6);

		// 還原矩陣
		MatrixState.popMatrix();
	}

	//判斷按鈕是否有觸控事件的方法
	public boolean isActionOnRainbowBar(float pressX, float pressY)
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
	
	//改變力道條力道的方法
	public void changePower(float pressX, float pressY)
	{
		float tempN = (y2D + height2D - pressY) / (height2D) * N_MAX;
    	//彩虹條繪制的格子數目，注意取tempN和ColorBar.N_MAX值中較小的
    	currN = Math.min(Math.round(tempN), N_MAX);
    	//防止力道小於1
    	currN = Math.max(currN, 1);
	}
	//獲得目前力道的方法
	public float getCurrStrength(){
    	//將n值折算成力道值
		this.currStrength = ((float) currN / N_MAX) * MAX_STRENGTH;
		return currStrength;
	}
}
