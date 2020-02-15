package tstc.fxq.utils;

import tstc.fxq.main.MySurfaceView;

/*
 * �������O
 * 
 * �ѩ�3D��������O�n�۾A���ù��A
 * �b��������ɷh����m����K�A
 * �]���������O�����C�Ӧ�m�����@�ӻ���O
 */
public class Score {
	MySurfaceView mv;
	private Number3D scoreNum;
	private int score;//�o��
	public Score
	(
			MySurfaceView mv, 
			float x, float y,//�Ʀr�����W�I�y��
			float numberWidth, float numberHeight,//�C�ӼƦr�����e
    		float sEnd, float tEnd, //�Ʀr�Ϥ��k�U����s�Bt��
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
	
	//�W�[�o������k
	public void addScore(int score){
		this.score += score;
		scoreNum.setNumber(this.score + "");
	}
	
	//���o�o������k
	public int getScore(){
		return score;
	}
	
	//�e�Ʀr����O����k
	public void drawSelf(){
		scoreNum.drawSelf();
	}
}
