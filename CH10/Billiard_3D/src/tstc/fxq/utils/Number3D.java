package tstc.fxq.utils;

import tstc.fxq.main.MySurfaceView;

/*
 * �Ʀr���O
 * 
 * �ѩ�3D��������O�n�۾A���ù��A
 * �b��������ɷh����m����K�A
 * �]���Ʀr���O�����C�Ӧ�m�����@�ӻ���O
 */
public class Number3D {
	MySurfaceView mv;
	private String numberStr = "";//�Ʀr
	private final int maxDecimal = 10;//���p���Ʀr�̤j���
	Board[] numberBoards = new Board[maxDecimal];//�Ʀr���P��m������O�}�C
	float[] ss = new float[maxDecimal + 1];//�Ʀr��s���z�y�а}�C
	float tEnd;//�Ʀr�Ϥ��k�U����t��
	public Number3D
	(
			MySurfaceView mv, 
			float x, float y,//�Ʀr�����W�I�y��
			float numberWidth, float numberHeight,//�C�ӼƦr�����e
    		float sEnd, float tEnd, //�Ʀr�Ϥ��k�U����s�Bt��
			int numberTexId
	){
		this.mv = mv;
		this.tEnd = tEnd;
		//�Ϥ�s�y�Ъ��B�i
		float sSpan = sEnd/10.0f;
		
		//�إ߼Ʀr0-9������O		
		for(int i=0; i<numberBoards.length; i++){
			//���P��m������O�}�C
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
		//�C�ӼƦr��s���z�y��
		for(int i=0; i<=10; i++){
			ss[i] = sSpan*i;
		}
	}
	
	//�W�[�Ʀr����k
	public void setNumber(String number){
		this.numberStr = number;
	}
	
	//�e�Ʀr����O����k
	public void drawSelf(){
		//�`��ø��Ʀr����O
		for(int i=0; i<numberStr.length(); i++)
		{
			//��o�C�@��Ʀr
			char c = numberStr.charAt(i);
			int n = c-'0';
			//ø�����������O
			numberBoards[i].drawSelf(ss[n], 0, ss[n+1], tEnd);
		}
	}
}
