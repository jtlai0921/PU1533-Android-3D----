package tstc.fxq.utils;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;

/*
 * 生產自適應屏的儀表板的工廠類別
 * 
 * 呼叫該類別的靜態方法
 * 可以建立自適應屏的儀表板物件
 */
public class InstrumentBoardFactory {
	//原始手機解析度
	private static final float sSpWidth = 480;//原始垂直的寬度
	private static final float sSpHeight = 800;//原始垂直的高度
	
	private static float originalScreenWidth;//原始垂直的寬度
	private static float originalScreenHeight;//原始垂直的高度
	private static float originalScreenRatio;//原始垂直的長寬比
	
	//目的屏中按鈕的位置和尺寸
	private static float targetX;
	private static float targetY;
	private static float targetWidth;
	private static float targetHeight;
	private static float targetAddedTouchScaleX;
	private static float targetAddedTouchScaleY;
	//建立虛擬按鈕物件的靜態方法
	public static VirtualButton newButtonInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //虛擬按鈕對應螢幕中的左上角位置座標
    		float width2D, float height2D, //按鈕在螢幕中的寬度和高度值
    		float addedTouchScaleX, float addedTouchScaleY,//延伸的觸控範圍
    		float sEnd, float tEnd, //按鈕右下角的s、t值。左上角的s、t值為0。所以紋理圖中按鈕應在左上角
    		int upTexId, //按下和抬起的按鈕圖片id
    		boolean fixXYRatioFlag, //是否保持按鈕的原始長寬比
    		Gravity gravity
    ){
		//若果螢幕寬和高還沒有起始化，拋出例外
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//起始化原始屏尺寸
		initOriginalScreenScale();
		
		//改變原始按鈕的長寬比，以適應全螢幕
		if(fixXYRatioFlag == false){
			//根據原始屏中按鈕的2D座標，計算比例
			calTargetToFillScreen(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY);	
		}
		else{//保持原始按鈕長寬比，不適應全螢幕，將按鈕縮放並置中
			calTargetToFixXY(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY, gravity);
		}
		
		//建立目的屏上的虛擬按鈕物件，並傳回
		return new VirtualButton(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				targetAddedTouchScaleX, targetAddedTouchScaleY,
				sEnd, tEnd, //sEnd、tEnd
        		upTexId//upTexId
        );
	}
	//建立虛擬按鈕物件的靜態方法---2--按下時切換圖片
	public static VirtualButtonChangePic newButtonChangePicInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //虛擬按鈕對應螢幕中的左上角位置座標
    		float width2D, float height2D, //按鈕在螢幕中的寬度和高度值
    		float addedTouchScaleX, float addedTouchScaleY,//延伸的觸控範圍
    		float sEnd, float tEnd, //按鈕右下角的s、t值。左上角的s、t值為0。所以紋理圖中按鈕應在左上角
    		int upTexId, int downTexId, //按下和抬起的按鈕圖片id
    		boolean fixXYRatioFlag, //是否保持按鈕的原始長寬比
    		Gravity gravity
    ){
		//若果螢幕寬和高還沒有起始化，拋出例外
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//起始化原始屏尺寸
		initOriginalScreenScale();
		
		//改變原始按鈕的長寬比，以適應全螢幕
		if(fixXYRatioFlag == false){
			//根據原始屏中按鈕的2D座標，計算比例
			calTargetToFillScreen(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY);	
		}
		else{//保持原始按鈕長寬比，不適應全螢幕，將按鈕縮放並置中
			calTargetToFixXY(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY, gravity);
		}
		
		//建立目的屏上的虛擬按鈕物件，並傳回
		return new VirtualButtonChangePic(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				targetAddedTouchScaleX, targetAddedTouchScaleY,
				sEnd, tEnd, //sEnd、tEnd
        		upTexId, downTexId //upTexId、downTexId,
        );
	}
	//建立儀表板的靜態方法
	public static Board newBoardInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //虛擬儀表板對應螢幕中的左上角位置座標
    		float width2D, float height2D, //儀表板在螢幕中的寬度和高度值
    		float addedTouchScaleX, float addedTouchScaleY,//延伸的觸控範圍
    		float sEnd, float tEnd, //儀表板右下角的s、t值。左上角的s、t值為0
    		int texId, //儀表板圖片id
    		int isTransparent,//是否透明
    		boolean fixXYRatioFlag, //是否保持儀表板的原始長寬比
    		Gravity gravity
    ){
		//若果螢幕寬和高還沒有起始化，拋出例外
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//起始化原始屏尺寸
		initOriginalScreenScale();
		
		//改變原始儀表板的長寬比，以適應全螢幕
		if(fixXYRatioFlag == false){
			//根據原始屏中儀表板的2D座標，計算比例
			calTargetToFillScreen(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY);	
		}
		else{//保持原始儀表板長寬比，不適應全螢幕，將儀表板縮放並置中
			calTargetToFixXY(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY, gravity);
		}
		
		//建立目的屏上的虛擬儀表板物件，並傳回
		return new Board(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				targetAddedTouchScaleX, targetAddedTouchScaleY,
				sEnd, tEnd, 
        		texId,
        		isTransparent
        );
	}	

	//建立得分的靜態方法
	public static Score newScoreInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //虛擬儀表板對應螢幕中的左上角位置座標
    		float width2D, float height2D, //儀表板在螢幕中的寬度和高度值
    		float sEnd, float tEnd, //儀表板右下角的s、t值
    		int texId, //儀表板圖片id
    		boolean fixXYRatioFlag, //是否保持儀表板的原始長寬比
    		Gravity gravity
    ){
		//若果螢幕寬和高還沒有起始化，拋出例外
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//起始化原始屏尺寸
		initOriginalScreenScale();
		
		//改變原始儀表板的長寬比，以適應全螢幕
		if(fixXYRatioFlag == false){
			//根據原始屏中儀表板的2D座標，計算比例
			calTargetToFillScreen(x2D, y2D, width2D, height2D, 0, 0);	
		}
		else{//保持原始儀表板長寬比，不適應全螢幕，將儀表板縮放並置中
			calTargetToFixXY(x2D, y2D, width2D, height2D, 0, 0, gravity);
		}
		
		//建立目的屏上的虛擬儀表板物件，並傳回
		return new Score(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				sEnd, tEnd,
        		texId
        );
	}	

	//建立倒計時的靜態方法
	public static Timer newTimerInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //虛擬儀表板對應螢幕中的左上角位置座標
    		float width2D, float height2D, //儀表板在螢幕中的寬度和高度值
    		float sEnd, float tEnd, //儀表板右下角的s、t值
    		int numberTexId, //數字id
    		int breakTexId,//分隔符id
    		boolean fixXYRatioFlag, //是否保持儀表板的原始長寬比
    		Gravity gravity
    ){
		//若果螢幕寬和高還沒有起始化，拋出例外
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//起始化原始屏尺寸
		initOriginalScreenScale();
		
		//改變原始儀表板的長寬比，以適應全螢幕
		if(fixXYRatioFlag == false){
			//根據原始屏中儀表板的2D座標，計算比例
			calTargetToFillScreen(x2D, y2D, width2D, height2D, 0, 0);	
		}
		else{//保持原始儀表板長寬比，不適應全螢幕，將儀表板縮放並置中
			calTargetToFixXY(x2D, y2D, width2D, height2D, 0, 0, gravity);
		}
		
		//建立目的屏上的虛擬儀表板物件，並傳回
		return new Timer(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				sEnd, tEnd,
				numberTexId,
				breakTexId
        );
	}
	//建立彩虹條的靜態方法
	public static RainbowBar newRainbowBarInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //彩虹條對應螢幕中的左上角位置座標
    		float width2D, float height2D, //彩虹條在螢幕中的寬度和高度值
    		float addedTouchScaleX, float addedTouchScaleY,//延伸的觸控範圍
    		float gapRatio, //縫隙占的比例
    		boolean fixXYRatioFlag, //是否保持彩虹條的原始長寬比
    		Gravity gravity
    ){
		//若果螢幕寬和高還沒有起始化，拋出例外
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//起始化原始屏尺寸
		initOriginalScreenScale();
		
		//改變原始彩虹條的長寬比，以適應全螢幕
		if(fixXYRatioFlag == false){
			//根據原始屏中彩虹條的2D座標，計算比例
			calTargetToFillScreen(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY);	
		}
		else{//保持原始彩虹條長寬比，不適應全螢幕，將彩虹條縮放並置中
			calTargetToFixXY(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY, gravity);
		}
		
		//建立目的屏上的彩虹條物件，並傳回
		return new RainbowBar(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				targetAddedTouchScaleX, targetAddedTouchScaleY,
				gapRatio
        );
	}
	
	//起始化原始屏尺寸的方法
	private static void initOriginalScreenScale(){
		//螢幕為垂直，起始化垂直資料
		if(Constant.SCREEN_WIDTH < Constant.SCREEN_HEIGHT){
			originalScreenWidth = sSpWidth;
			originalScreenHeight = sSpHeight;
		}
		else{//螢幕為橫屏，起始化橫屏資料
			originalScreenWidth = sSpHeight;
			originalScreenHeight = sSpWidth;
		}
		//計算原始屏的長寬比
		originalScreenRatio = originalScreenWidth/originalScreenHeight;
	}
	
	//計算目的按鈕位置和尺寸，伸展變形，以適應全螢幕的方法
	private static void calTargetToFillScreen
	(
			float x2D, float y2D,
			float width2D, float height2D,
			float addedTouchScaleX2D, float addedTouchScaleY2D
	)
	{
		//根據原始屏和目的屏的尺寸，計算縮放比例
		float xRatio = Constant.SCREEN_WIDTH/originalScreenWidth; //x方向縮放比例
		float yRatio = Constant.SCREEN_HEIGHT/originalScreenHeight; //y方向縮放比例
		
		//根據比例計算目的屏中按鈕的位置和尺寸
		targetX = x2D * xRatio;
		targetY = y2D * yRatio;
		targetWidth = width2D * xRatio;
		targetHeight = height2D * yRatio;

		targetAddedTouchScaleX = addedTouchScaleX2D * xRatio;
		targetAddedTouchScaleY = addedTouchScaleY2D * yRatio;
	}
	
	//計算目的按鈕位置和尺寸，保持按鈕x、y比例不變，縮放置中，不適應全螢幕的方法
	private static void calTargetToFixXY
	(
			float x2D, float y2D, 
			float width2D, float height2D,
			float addedTouchScaleX2D, float addedTouchScaleY2D,
			Gravity gravity
	)
	{
		//計算目的屏的長寬比
		float targetScreenRatio = Constant.SCREEN_WIDTH /(float)Constant.SCREEN_HEIGHT;
		//備用縮放比
		float screenHightRatio = Constant.SCREEN_HEIGHT/originalScreenHeight;//螢幕高的比
		float screenWidthRatio = Constant.SCREEN_WIDTH/originalScreenWidth;//螢幕寬的比
		//實際用到的縮放比
		float ratio;
		//若目的屏的長寬比，比原始屏的長寬比大，縮放比以螢幕高的比為准
		if(targetScreenRatio > originalScreenRatio){
			//實際用到的縮放比以高為基准
			ratio = screenHightRatio;
			//原始屏等比例縮放後的寬度
			float w0 = originalScreenWidth * ratio;
			//原始屏縮放並置中後，其原點（螢幕左上角）和目的屏的原點x方向的寬度差
			float x0 = (Constant.SCREEN_WIDTH - w0)/2.0f;
			//根據比例計算目的屏中按鈕的位置和尺寸
			if(gravity == Gravity.Center){//置中
				targetX = x2D * ratio + x0;
			}
			else if(gravity == Gravity.Side){//靠邊
				//若果按鈕在螢幕的左邊，自動齊左
				if(x2D < originalScreenWidth / 2){
					targetX = x2D * ratio;
				}
				//若果按鈕在螢幕的右邊，自動齊右
				else{
					targetX = x2D * ratio + 2 * x0;
				}
			}
			targetY = y2D * ratio;
			targetWidth = width2D * ratio;
			targetHeight = height2D * ratio;
			targetAddedTouchScaleX = addedTouchScaleX2D * ratio;
			targetAddedTouchScaleY = addedTouchScaleY2D * ratio;
		}
		else{//若目的屏的長寬比，比原始屏的長寬比小或相等，縮放比以螢幕寬的比為准
			//實際用到的縮放比以寬為基准
			ratio = screenWidthRatio;
			//原始屏等比例縮放後的高度
			float h0 = originalScreenHeight * ratio;
			//原始屏縮放並置中後，其原點（螢幕左上角）和目的屏的原點y方向的高度差
			float y0 = (Constant.SCREEN_HEIGHT - h0)/2.0f;
			//根據比例計算目的屏中按鈕的位置和尺寸
			targetX = x2D * ratio ;
			if(gravity == Gravity.Center){//置中
				targetY = y2D * ratio+ y0;
			}
			else if(gravity == Gravity.Side){//靠邊
				//若果按鈕在螢幕的上邊，自動頂端對齊
				if(y2D < originalScreenHeight / 2){
					targetY = y2D * ratio;
				}
				//若果按鈕在螢幕的下邊，自動底端對齊
				else{
					targetY = y2D * ratio + 2 * y0;
				}
			}
			targetWidth = width2D * ratio;
			targetHeight = height2D * ratio;
			targetAddedTouchScaleX = addedTouchScaleX2D * ratio;
			targetAddedTouchScaleY = addedTouchScaleY2D * ratio;
		}
	}

	/*
	 * 儀表板等比例伸展自適應屏時，重力方向的列舉型態
	 */
	public static enum Gravity{
		Center,//置中
		Side//靠邊
	}
}
