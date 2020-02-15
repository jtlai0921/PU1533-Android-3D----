package lwz.com.tank.activity;

import lwz.com.tank.util.ScreenScaleResult;
import lwz.com.tank.util.ScreenScaleUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Constant {
	public static final float[][]lowWallmapdata=
		{
			{-6.5f,4.5f,0.0f,13.0f,0.5f,0.3f},{-6.5f,4.0f,0.0f,0.5f,8.0f,0.3f},{-6.5f,-4.0f,0.0f,13.0f,0.5f,0.3f},{6.0f,4.0f,0.0f,0.5f,8.0f,0.3f},//外面圍牆
			{-4.5f,2.5f,0.0f,2.5f,0.4f,0.2f},{2.0f,2.5f,0.0f,2.5f,0.4f,0.2f},
			{-4.0f,0.2f,0.0f,8.0f,0.4f,0.2f},{-0.2f,2.0f,0.0f,0.4f,4.0f,0.2f},
			{-4.5f,-2.1f,0.0f,2.5f,0.4f,0.2f},{2.0f,-2.1f,0.0f,2.5f,0.4f,0.2f}
		};
	public static final float[][]middleWallmapdata=
		{
			{-6.4f,4.4f,0.3f,12.8f,0.3f,0.3f},{-6.4f,4.1f,0.3f,0.3f,8.2f,0.3f},{-6.4f,-4.1f,0.3f,12.8f,0.3f,0.3f},{6.1f,4.1f,0.3f,0.3f,8.2f,0.3f},//外面圍牆
			{-4.4f,2.4f,0.2f,2.3f,0.2f,0.2f},{2.1f,2.4f,0.2f,2.3f,0.2f,0.2f},
			{-3.9f,0.1f,0.2f,7.8f,0.2f,0.2f},{-0.1f,1.9f,0.2f,0.2f,3.8f,0.2f},
			{-4.4f,-2.2f,0.2f,2.3f,0.2f,0.2f},{2.1f,-2.2f,0.2f,2.3f,0.2f,0.2f}
		};
	public static final float[][]highWallmapdata=
		{
			{-6.4f,4.4f,0.6f,0.3f,0.3f,0.2f},{-5.0f,4.4f,0.6f,0.4f,0.3f,0.2f},{-3.4f,4.4f,0.6f,0.4f,0.3f,0.2f},{-1.8f,4.4f,0.6f,0.4f,0.3f,0.2f},{-0.2f,4.4f,0.6f,0.4f,0.3f,0.2f},{1.4f,4.4f,0.6f,0.4f,0.3f,0.2f},{3.0f,4.4f,0.6f,0.4f,0.3f,0.2f},{4.6f,4.4f,0.6f,0.4f,0.3f,0.2f},{6.1f,4.4f,0.6f,0.3f,0.3f,0.2f},//上面
			{-6.4f,3.4f,0.6f,0.3f,0.4f,0.2f},{-6.4f,1.8f,0.6f,0.3f,0.4f,0.2f},{-6.4f,0.2f,0.6f,0.3f,0.4f,0.2f},{-6.4f,-1.4f,0.6f,0.3f,0.4f,0.2f},{-6.4f,-3.0f,0.6f,0.3f,0.4f,0.2f},//左側
			{-6.4f,-4.1f,0.6f,0.3f,0.3f,0.2f},{-5.0f,-4.1f,0.6f,0.4f,0.3f,0.2f},{-3.4f,-4.1f,0.6f,0.4f,0.3f,0.2f},{-1.8f,-4.1f,0.6f,0.4f,0.3f,0.2f},{-0.2f,-4.1f,0.6f,0.4f,0.3f,0.2f},{1.4f,-4.1f,0.6f,0.4f,0.3f,0.2f},{3.0f,-4.1f,0.6f,0.4f,0.3f,0.2f},{4.6f,-4.1f,0.6f,0.4f,0.3f,0.2f},{6.1f,-4.1f,0.6f,0.3f,0.3f,0.2f},//下面
			{6.0f,3.4f,0.6f,0.3f,0.4f,0.2f},{6.0f,1.8f,0.6f,0.3f,0.4f,0.2f},{6.0f,0.2f,0.6f,0.3f,0.4f,0.2f},{6.0f,-1.4f,0.6f,0.3f,0.4f,0.2f},{6.0f,-3.0f,0.6f,0.3f,0.4f,0.2f},//右側
			{-4.4f,2.4f,0.4f,0.2f,0.2f,0.2f},{-3.4f,2.4f,0.4f,0.3f,0.2f,0.2f},{-2.3f,2.4f,0.4f,0.2f,0.2f,0.2f},
			{2.1f,2.4f,0.4f,0.2f,0.2f,0.2f},{3.1f,2.4f,0.4f,0.3f,0.2f,0.2f},{4.2f,2.4f,0.4f,0.2f,0.2f,0.2f},
			{-3.9f,0.1f,0.4f,0.2f,0.2f,0.2f},{-2.7f,0.1f,0.4f,0.3f,0.2f,0.2f},{-1.4f,0.1f,0.4f,0.3f,0.2f,0.2f},{-0.1f,0.1f,0.4f,0.2f,0.2f,0.5f},{1.1f,0.1f,0.4f,0.3f,0.2f,0.2f},{2.4f,0.1f,0.4f,0.3f,0.2f,0.2f},{3.7f,0.1f,0.4f,0.2f,0.2f,0.2f},
			{-0.1f,1.9f,0.4f,0.2f,0.2f,0.2f},{-0.1f,1.0f,0.4f,0.2f,0.2f,0.2f},{-0.1f,-0.8f,0.4f,0.2f,0.2f,0.2f},{-0.1f,-1.7f,0.4f,0.2f,0.2f,0.2f},
			{-4.4f,-2.2f,0.4f,0.2f,0.2f,0.2f},{-3.4f,-2.2f,0.4f,0.3f,0.2f,0.2f},{-2.3f,-2.2f,0.4f,0.2f,0.2f,0.2f},
			{2.1f,-2.2f,0.4f,0.2f,0.2f,0.2f},{3.1f,-2.2f,0.4f,0.3f,0.2f,0.2f},{4.2f,-2.2f,0.4f,0.2f,0.2f,0.2f}
		};
	
	public static float SCREEN_WIDTH_STANDARD=960;  //螢幕標准寬度	
	public static float SCREEN_HEIGHT_STANDARD=540;  //螢幕標准高度
	public static float SCREEN_WIDTH;  //螢幕寬度	
	public static float SCREEN_HEIGHT;  //螢幕高度
	public static float RATIO=SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//螢幕長寬比

	//自適應螢幕工具類別物件
	public static ScreenScaleResult ssr;
	public static boolean XSFLAG=false;//執行scaleCL()標志位
	public static void scaleCL()
	{
		if(XSFLAG) return;        
        ssr=ScreenScaleUtil.calScale(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        XSFLAG=true;
	}
	
	//public static final int Wall_UNIT_SIZE=55;	
	public static final int Wall_UNIT_SIZE=40;	
	public static final float UNIT_SIZE = 4;
	
	//由Service中的Handler傳送的訊息型態
    public static final int MSG_READ = 2;
    public static final int MSG_DEVICE_NAME = 4;
	// 從Service中的Handler發來的主鍵名
    public static final String DEVICE_NAME = "device_name"; 
	    
    public static float TIME_SPAN=0.1f;//炮彈生存時間增長值
    public static float BULLET_V=50f;//炮出現膛總速度
    //坦克每框搬移的步徑
//    public static float MOVE_SPAN=2.5f;
    public static float MOVE_SPAN=4f;
    //縮放比例
    public static float ratio_width=1;
    public static float ratio_height=1;
    //縮放紋理
	public static Bitmap scaleToFit(Bitmap bm,float width_Ratio,float height_Ratio)
	{		
    	int width = bm.getWidth(); 							//圖片寬度
    	int height = bm.getHeight();							//圖片高度
    	Matrix matrix = new Matrix(); 
    	matrix.postScale((float)width_Ratio, (float)height_Ratio);				//圖片等比例拉遠為原來的fblRatio倍
    	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);//宣告點陣圖        	
    	return bmResult;									//傳回被縮放的圖片
    }
//	public static long[] COLLISION_SOUND_PATTERN={0l,280};//第一個參數為執行震動方法後多長時間開始震動
//	//第二個參數為震動持續時間，兩個參數必須為long型態
	public static long[] COLLISION_SOUND_PATTERN={0l,0};//第一個參數為執行震動方法後多長時間開始震動
	//第二個參數為震動持續時間，兩個參數必須為long型態
		
		
		
		
}
