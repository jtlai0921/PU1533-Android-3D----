package com.bn.gjxq.manager;
import com.bn.gjxq.game.*;

public class ChessRuleUtil 
{   
	public static boolean chessRule(int[][] qzwz,int[] fromWz,int[] toWz)
	{
		//出了行與列的範圍
		if ((fromWz[0] > 7 || fromWz[0] < 0) && (fromWz[1] > 7 || fromWz[1] < 0) && (toWz[0] > 7 || toWz[0]< 0)
				&& (toWz[1] > 7 || toWz[1] < 0)) {
			return false;
		}
		//起始的行列沒有棋子
		if (qzwz[fromWz[0]][fromWz[1]] ==-1)
			return false;
		
		if(//若果該位置是自己方的，那麼直接不可下
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
		case GameData.bbing://白兵
			//斜著走吃子
			if (((toWz[0] - fromWz[0]) == -1) && (Math.abs(fromWz[1] - toWz[1]) == 1)
					&& (qzwz[toWz[0]][toWz[1]] > 5)) {//大於5指的是吃的是對方的子
				return true;
			} else if (((toWz[0] - fromWz[0]) == -1) && (toWz[1] - fromWz[1] == 0) 
					&& qzwz[toWz[0]][toWz[1]] == -1) {//向前走一個格子
				return true;
			} else if (((toWz[0] - fromWz[0]) == -2) && (fromWz[0] == 6) && (toWz[1] == fromWz[1])
					&& qzwz[toWz[0]][toWz[1]] == -1) {//向前走兩個格子，fromWz[0] == 6限定了其必須是第一步才可以走兩個格子
				return true;
			}
			break;
		case GameData.bma://白馬
		case GameData.hma://黑馬
			if (Math.abs(fromWz[0] - toWz[0]) == 2 && (Math.abs(fromWz[1] - toWz[1]) == 1)) {
				return true;
			} else if (Math.abs(fromWz[0] - toWz[0]) == 1 && (Math.abs(fromWz[1] - toWz[1]) == 2)) {
				return true;
			}
			break;
		case GameData.bche://白車
		case GameData.hche://黑車
			if (fromWz[0] == toWz[0]) {//橫著走子
				int i, j;
				if (fromWz[1] < toWz[1]) {//向右走
					i = fromWz[1] + 1;
					j = toWz[1];
				} else {//向左走
					i = toWz[1] + 1;
					j = fromWz[1];
				}
				for (; i < j; i++) {
					if (qzwz[fromWz[0]][i] != -1) {
						return false;
					}
				}
				return true;
			} else if (toWz[1] == fromWz[1]) {//豎著走子
				int i, j;
				if (fromWz[0] < toWz[0]) {//向上走
					i = fromWz[0] + 1;
					j = toWz[0];
				} else {//向下走
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
		case GameData.bxiang://白象
		case GameData.hxiang://黑象
			if ((Math.abs(fromWz[0] - toWz[0])) == Math.abs((fromWz[1] - toWz[1]))) {
				if (((fromWz[0] - toWz[0]) * (fromWz[1] - toWz[1]) > 0)) {//捺
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
				} else if (((fromWz[0] - toWz[0]) * (fromWz[1] - toWz[1]) < 0)) {//撇
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
		case GameData.bhou://白後
		case GameData.hhou://黑後
			if ((Math.abs(fromWz[0] - toWz[0])) == Math.abs((fromWz[1] - toWz[1]))) {
				if (((fromWz[0] - toWz[0]) * (fromWz[1] - toWz[1]) > 0)) {//捺
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
				} else if (((fromWz[0] - toWz[0]) * (fromWz[1]- toWz[1]) < 0)) {//撇
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
			if (fromWz[0] == toWz[0]) {//橫著
				int i, j;
				if (fromWz[1] < toWz[1]) {//向右
					i = fromWz[1] + 1;
					j = toWz[1];
				} else {//向左
					i = toWz[1] + 1;
					j = fromWz[1];
				}
				for (; i < j; i++) {
					if (qzwz[fromWz[0]][i] != -1) {
						return false;
					}
				}
				return true;
			} else if (toWz[1] == fromWz[1]) {//豎著
				int i, j;
				if (fromWz[0] < toWz[0]) {//向下
					i = fromWz[0] + 1;
					j = toWz[0];
				} else {//向上
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
		case GameData.bwang://白王
		case GameData.hwang://黑王
			if ((Math.abs(fromWz[0] - toWz[0]) < 2) && (Math.abs(fromWz[1] - toWz[1]) < 2)) {
				return true;
			}
			break;
		case GameData.hbing://黑兵
			//斜著走吃子
			if (((toWz[0] - fromWz[0]) == 1) && (Math.abs(fromWz[1] - toWz[1]) == 1)
					&& (qzwz[toWz[0]][toWz[1]] >=0&&qzwz[toWz[0]][toWz[1]]<6)) {//小於6代表吃的是對方的子
				return true;
			} else if (((toWz[0] - fromWz[0]) == 1) && (toWz[1] - fromWz[1] == 0) 
					&& qzwz[toWz[0]][toWz[1]] == -1) {//向前走一個格子
				return true;
			} else if (((toWz[0] - fromWz[0]) == 2) && (fromWz[0] == 1) && (toWz[1] == fromWz[1])
					&& qzwz[toWz[0]][toWz[1]] == -1) {//向前走兩個格子，fromWz[0] == 1限定了其必須是第一次走才可以走兩個格子
				return true;
			}
			break;
		}
		return false;
	}
	
	public static Finish isFinish(int[][] qzwz)//判斷某家是否贏了
	{
		boolean black=false;//黑方沒輸
		boolean white=false;//白方沒輸
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				if(qzwz[i][j]!=-1)
				{
					if(qzwz[i][j]==4)//若果目前有白王
					{
						black=true;
						
					}
					else if(qzwz[i][j]==10)//若果目前有黑王
					{
						white=true;
					}
				}
			}
		}		
		if(!black)//若果是黑方輸了
		{
			return Finish.BLACK_WIN;//傳回白方贏
		}
		else if(!white)//若果是白方輸了
		{
			return Finish.WHITE_WIN;//傳回黑方贏
		}
		else
		{
			return Finish.NO_FINISH;//否則傳回沒有輸贏
		}
	}
}
