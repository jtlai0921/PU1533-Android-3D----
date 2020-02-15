package tstc.fxq.utils;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;

/*
 * 倒計時類別
 * 
 * 由於3D中的儀表板要自適應螢幕，
 * 在游戲執行時搬移位置不方便，
 * 因此時間類別中的每個位置對應一個儀表板
 */
public class Timer {
	MySurfaceView mv;
	private final int totalSecond=12*60;//總秒數
	private int leftSecond = totalSecond;//時間
	private Number3D minuteNum;//分鍾數字
	private Number3D secondNum;//秒鍾數字
	private Board breakBoard;//分隔符
	public Timer
	(
			MySurfaceView mv, 
			float x, float y,//數字的左上點座標
			float numberWidth, float numberHeight,//每個數字的長寬
    		float sEnd, float tEnd, //數字圖片右下角的s、t值
			int numberTexId,
			int breakTexId
	){
		this.mv = mv;
		minuteNum = new Number3D(
				mv, 
				x, y, 
				numberWidth, numberHeight, 
				sEnd, tEnd, 
				numberTexId
				);
		secondNum = new Number3D(
				mv, 
				x + 3*numberWidth, y, 
				numberWidth, numberHeight, 
				sEnd, tEnd, 
				numberTexId
				);
		breakBoard = new Board(
				mv, 
				x + 2*numberWidth, y, 
				numberWidth, numberHeight, 
				0, 0, 
				sEnd, tEnd, 
				breakTexId, 
				0
				);
		//根據剩余秒數，計算並設定目前分鍾數和秒數
		setTime();
	}
	
	//減少時間的方法
	public void subtractTime(int second)
	{
		if(this.leftSecond>0)
		{
			this.leftSecond -= second;
			//根據剩余秒數，計算並設定目前分鍾數和秒數
			setTime();
		}
		else//若果時間為0，結束游戲
		{
			//顯示時間結束的交談視窗
			mv.bgt.sendHandlerMessage(Constant.TIME_UP);
			//結束游戲
			mv.overGame();
		}
	}
	//根據剩余秒數，計算並設定目前分鍾數和秒數的方法
	private void setTime(){		
		//取得分鍾和秒鍾的方法
		int second = this.leftSecond%60;
		int minute = this.leftSecond/60;	
		String secondStr = second + "";
		String minuteStr = minute + "";
		if(second<10){//確保至少有兩位
			secondStr="0" + second;
		}

		if(minute<10){//確保至少有兩位
			minuteStr="0" + minute;
		}
		//設定分鍾和秒鍾
		secondNum.setNumber(secondStr);
		minuteNum.setNumber(minuteStr);
	}
	
	//取得剩余時間的方法
	public int getLeftSecond() {
		return leftSecond;
	}
	
	//繪制時間的方法
	public void drawSelf()
	{
		//繪制分鍾和秒鍾
		secondNum.drawSelf();
		minuteNum.drawSelf();
		//繪制分隔符
		breakBoard.drawSelf();
	}
}
