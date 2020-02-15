package com.bn.gjxq.manager;
import com.bn.gjxq.game.*;

public class ChessRuleUtil 
{   
	public static boolean chessRule(int[][] qzwz,int[] fromWz,int[] toWz)
	{
		//�X�F��P�C���d��
		if ((fromWz[0] > 7 || fromWz[0] < 0) && (fromWz[1] > 7 || fromWz[1] < 0) && (toWz[0] > 7 || toWz[0]< 0)
				&& (toWz[1] > 7 || toWz[1] < 0)) {
			return false;
		}
		//�_�l����C�S���Ѥl
		if (qzwz[fromWz[0]][fromWz[1]] ==-1)
			return false;
		
		if(//�Y�G�Ӧ�m�O�ۤv�誺�A���򪽱����i�U
				qzwz[toWz[0]][toWz[1]]!=-1
				&&((qzwz[toWz[0]][toWz[1]]>=0&&qzwz[toWz[0]][toWz[1]]<6
				&&qzwz[fromWz[0]][fromWz[1]]>=0&&qzwz[fromWz[0]][fromWz[1]]<6)
				||(qzwz[toWz[0]][toWz[1]]>=6&&qzwz[toWz[0]][toWz[1]]<12
				&&qzwz[fromWz[0]][fromWz[1]]>=6&&qzwz[fromWz[0]][fromWz[1]]<12))
		)
		{
			return false;
		}
		
		switch (qzwz[fromWz[0]][fromWz[1]]) {
		case GameData.bbing://�էL
			//�׵ۨ��Y�l
			if (((toWz[0] - fromWz[0]) == -1) && (Math.abs(fromWz[1] - toWz[1]) == 1)
					&& (qzwz[toWz[0]][toWz[1]] > 5)) {//�j��5�����O�Y���O��誺�l
				return true;
			} else if (((toWz[0] - fromWz[0]) == -1) && (toWz[1] - fromWz[1] == 0) 
					&& qzwz[toWz[0]][toWz[1]] == -1) {//�V�e���@�Ӯ�l
				return true;
			} else if (((toWz[0] - fromWz[0]) == -2) && (fromWz[0] == 6) && (toWz[1] == fromWz[1])
					&& qzwz[toWz[0]][toWz[1]] == -1) {//�V�e����Ӯ�l�AfromWz[0] == 6���w�F�䥲���O�Ĥ@�B�~�i�H����Ӯ�l
				return true;
			}
			break;
		case GameData.bma://�հ�
		case GameData.hma://�°�
			if (Math.abs(fromWz[0] - toWz[0]) == 2 && (Math.abs(fromWz[1] - toWz[1]) == 1)) {
				return true;
			} else if (Math.abs(fromWz[0] - toWz[0]) == 1 && (Math.abs(fromWz[1] - toWz[1]) == 2)) {
				return true;
			}
			break;
		case GameData.bche://�ը�
		case GameData.hche://�¨�
			if (fromWz[0] == toWz[0]) {//��ۨ��l
				int i, j;
				if (fromWz[1] < toWz[1]) {//�V�k��
					i = fromWz[1] + 1;
					j = toWz[1];
				} else {//�V����
					i = toWz[1] + 1;
					j = fromWz[1];
				}
				for (; i < j; i++) {
					if (qzwz[fromWz[0]][i] != -1) {
						return false;
					}
				}
				return true;
			} else if (toWz[1] == fromWz[1]) {//�ݵۨ��l
				int i, j;
				if (fromWz[0] < toWz[0]) {//�V�W��
					i = fromWz[0] + 1;
					j = toWz[0];
				} else {//�V�U��
					i = toWz[0] + 1;
					j = fromWz[0];
				}
				for (; i < j; i++) {
					if (qzwz[i][fromWz[1]] != -1) {
						return false;
					}
				}
				return true;
			}
		case GameData.bxiang://�նH
		case GameData.hxiang://�¶H
			if ((Math.abs(fromWz[0] - toWz[0])) == Math.abs((fromWz[1] - toWz[1]))) {
				if (((fromWz[0] - toWz[0]) * (fromWz[1] - toWz[1]) > 0)) {//��
					int i, j, k;
					if (fromWz[0] > toWz[0]) {
						i = toWz[0] + 1;
						j = toWz[1] + 1;
						k = fromWz[0];
					} else {
						i = fromWz[0] + 1;
						j = fromWz[1] + 1;
						k = toWz[0];
					}
					for (; i < k; i++, j++) {
						if (qzwz[i][j] != -1) {
							return false;
						}
					}
					return true;
				} else if (((fromWz[0] - toWz[0]) * (fromWz[1] - toWz[1]) < 0)) {//�J
					int i, j, k;
					if (fromWz[0] > toWz[0]) {
						i = toWz[0] + 1;
						j = toWz[1] - 1;
						k = fromWz[0];
					} else {
						i = fromWz[0] + 1;
						j = fromWz[1] - 1;
						k = toWz[0];
					}
					for (; i < k; i++, j--) {
						if (qzwz[i][j] != -1) {
							return false;
						}
					}
					return true;
				}
			}
			break;
		case GameData.bhou://�ի�
		case GameData.hhou://�«�
			if ((Math.abs(fromWz[0] - toWz[0])) == Math.abs((fromWz[1] - toWz[1]))) {
				if (((fromWz[0] - toWz[0]) * (fromWz[1] - toWz[1]) > 0)) {//��
					int i, j, k;
					if (fromWz[0] > toWz[0]) {
						i = toWz[0] + 1;
						j = toWz[1] + 1;
						k = fromWz[0];
					} else {
						i = fromWz[0] + 1;
						j = fromWz[1]+ 1;
						k = toWz[0];
					}
					for (; i < k; i++, j++) {
						if (qzwz[i][j] != -1) {
							return false;
						}
					}
					return true;
				} else if (((fromWz[0] - toWz[0]) * (fromWz[1]- toWz[1]) < 0)) {//�J
					int i, j, k;
					if (fromWz[0] > toWz[0]) {
						i = toWz[0] + 1;
						j = toWz[1] - 1;
						k = fromWz[0];
					} else {
						i = fromWz[0] + 1;
						j = fromWz[1] - 1;
						k = toWz[0];
					}
					for (; i < k; i++, j--) {
						if (qzwz[i][j] != -1) {
							return false;
						}
					}
					return true;
				}
			}
			if (fromWz[0] == toWz[0]) {//���
				int i, j;
				if (fromWz[1] < toWz[1]) {//�V�k
					i = fromWz[1] + 1;
					j = toWz[1];
				} else {//�V��
					i = toWz[1] + 1;
					j = fromWz[1];
				}
				for (; i < j; i++) {
					if (qzwz[fromWz[0]][i] != -1) {
						return false;
					}
				}
				return true;
			} else if (toWz[1] == fromWz[1]) {//�ݵ�
				int i, j;
				if (fromWz[0] < toWz[0]) {//�V�U
					i = fromWz[0] + 1;
					j = toWz[0];
				} else {//�V�W
					i = toWz[0] + 1;
					j = fromWz[0];
				}
				for (; i < j; i++) {
					if (qzwz[i][fromWz[1]] != -1) {
						return false;
					}
				}
				return true;
			}
			break;
		case GameData.bwang://�դ�
		case GameData.hwang://�¤�
			if ((Math.abs(fromWz[0] - toWz[0]) < 2) && (Math.abs(fromWz[1] - toWz[1]) < 2)) {
				return true;
			}
			break;
		case GameData.hbing://�§L
			//�׵ۨ��Y�l
			if (((toWz[0] - fromWz[0]) == 1) && (Math.abs(fromWz[1] - toWz[1]) == 1)
					&& (qzwz[toWz[0]][toWz[1]] >=0&&qzwz[toWz[0]][toWz[1]]<6)) {//�p��6�N��Y���O��誺�l
				return true;
			} else if (((toWz[0] - fromWz[0]) == 1) && (toWz[1] - fromWz[1] == 0) 
					&& qzwz[toWz[0]][toWz[1]] == -1) {//�V�e���@�Ӯ�l
				return true;
			} else if (((toWz[0] - fromWz[0]) == 2) && (fromWz[0] == 1) && (toWz[1] == fromWz[1])
					&& qzwz[toWz[0]][toWz[1]] == -1) {//�V�e����Ӯ�l�AfromWz[0] == 1���w�F�䥲���O�Ĥ@�����~�i�H����Ӯ�l
				return true;
			}
			break;
		}
		return false;
	}
	
	public static Finish isFinish(int[][] qzwz)//�P�_�Y�a�O�_Ĺ�F
	{
		boolean black=false;//�¤�S��
		boolean white=false;//�դ�S��
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				if(qzwz[i][j]!=-1)
				{
					if(qzwz[i][j]==4)//�Y�G�ثe���դ�
					{
						black=true;
						
					}
					else if(qzwz[i][j]==10)//�Y�G�ثe���¤�
					{
						white=true;
					}
				}
			}
		}		
		if(!black)//�Y�G�O�¤��F
		{
			return Finish.BLACK_WIN;//�Ǧ^�դ�Ĺ
		}
		else if(!white)//�Y�G�O�դ��F
		{
			return Finish.WHITE_WIN;//�Ǧ^�¤�Ĺ
		}
		else
		{
			return Finish.NO_FINISH;//�_�h�Ǧ^�S����Ĺ
		}
	}
}
