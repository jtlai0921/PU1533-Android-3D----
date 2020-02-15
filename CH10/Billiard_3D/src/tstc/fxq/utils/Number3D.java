package tstc.fxq.utils;

import tstc.fxq.main.MySurfaceView;

/*
 * 數字類別
 * 
 * 由於3D中的儀表板要自適應螢幕，
 * 在游戲執行時搬移位置不方便，
 * 因此數字類別中的每個位置對應一個儀表板
 */
public class Number3D {
	MySurfaceView mv;
	private String numberStr = "";//數字
	private final int maxDecimal = 10;//估計的數字最大位數
	Board[] numberBoards = new Board[maxDecimal];//數字不同位置的儀表板陣列
	float[] ss = new float[maxDecimal + 1];//數字的s紋理座標陣列
	float tEnd;//數字圖片右下角的t值
	public Number3D
	(
			MySurfaceView mv, 
			float x, float y,//數字的左上點座標
			float numberWidth, float numberHeight,//每個數字的長寬
    		float sEnd, float tEnd, //數字圖片右下角的s、t值
			int numberTexId
	){
		this.mv = mv;
		this.tEnd = tEnd;
		//圖片s座標的步進
		float sSpan = sEnd/10.0f;
		
		//建立數字0-9的儀表板		
		for(int i=0; i<numberBoards.length; i++){
			//不同位置的儀表板陣列
			numberBoards[i] = new Board(
					mv,
					x + i*numberWidth, y,
					numberWidth, numberHeight,
					0, 0,
					sSpan*i, 0,
					sSpan*(i+1), tEnd,
					numberTexId
	        );
		}
		//每個數字的s紋理座標
		for(int i=0; i<=10; i++){
			ss[i] = sSpan*i;
		}
	}
	
	//增加數字的方法
	public void setNumber(String number){
		this.numberStr = number;
	}
	
	//畫數字儀表板的方法
	public void drawSelf(){
		//循環繪制數字儀表板
		for(int i=0; i<numberStr.length(); i++)
		{
			//獲得每一位數字
			char c = numberStr.charAt(i);
			int n = c-'0';
			//繪制對應的儀表板
			numberBoards[i].drawSelf(ss[n], 0, ss[n+1], tEnd);
		}
	}
}
