package tstc.fxq.utils;

import tstc.fxq.main.MySurfaceView;

/*
 * 分數類別
 * 
 * 由於3D中的儀表板要自適應螢幕，
 * 在游戲執行時搬移位置不方便，
 * 因此分數類別中的每個位置對應一個儀表板
 */
public class Score {
	MySurfaceView mv;
	private Number3D scoreNum;
	private int score;//得分
	public Score
	(
			MySurfaceView mv, 
			float x, float y,//數字的左上點座標
			float numberWidth, float numberHeight,//每個數字的長寬
    		float sEnd, float tEnd, //數字圖片右下角的s、t值
			int numberTexId
	){
		this.mv = mv;
		scoreNum = new Number3D(
				mv, 
				x, y, 
				numberWidth, numberHeight, 
				sEnd, tEnd, 
				numberTexId
				);
		scoreNum.setNumber(this.score + "");
	}
	
	//增加得分的方法
	public void addScore(int score){
		this.score += score;
		scoreNum.setNumber(this.score + "");
	}
	
	//取得得分的方法
	public int getScore(){
		return score;
	}
	
	//畫數字儀表板的方法
	public void drawSelf(){
		scoreNum.drawSelf();
	}
}
