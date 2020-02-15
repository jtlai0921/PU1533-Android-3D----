package com.bn.gjxq.manager;

import com.bn.gjxq.game.GameData;

public class RobotAutoUtil   
{
	public static int next_[][] = new int[2][2];
	public static int DEPTH = 3;
	public static RobotActionEnum preAction;
	
	//黑色棋子使用
	private final static int rook_[][] = //車
	{ 
		{ 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 }
	};
	private final static int knight_[][] = 
	{ 
		{ 1, 2 }, { 2, 1 }, { 1, -2 }, { -2, 1 },//馬
	 	{ -1, 2 }, { 2, -1 }, { -1, -2 }, { -2, -1 } 
	};
	private final static int bishop_[][] =
	{ 
		{ 1, 1 }, { -1, 1 }, { 1, -1 },{ -1, -1 }//象
	};
	private final static int queen_[][] = 
	{ 
		{ 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 },//後
	 	{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 }
	};
	private final static int king_[][] = 
	{ 
		{ 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 },//王
	 	{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 }
	};
	
	//白色棋子使用
	private final static int rook_1[][] = //車
	{ 
		{ 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 }
	};
	private final static int knight_1[][] = 
	{ 
		{ -1, -2 }, { -2,- 1 }, { -1, 2 }, { 2, -1 },//馬
	 	{ 1, -2 }, { -2, 1 }, { 1, 2 }, { 2, 1 } 
	};
	private final static int bishop_1[][] =
	{ 
		{ 1, 1 }, { -1, 1 }, { 1, -1 },{ -1, -1 }//象
	};
	private final static int queen_1[][] = 
	{ 
		{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 },//後
	 	{ 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }
	};
	private final static int king_1[][] = 
	{ 
		{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 },//王
	 	{ 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }
	};
	public static int chessbe[][]=new int[8][8];
	public static int colorl;
	
	static int isCheck=0;
	 	
     public static int[][] autoGo(int[][] qzwz,int color)
     {
    	 for(int i=0;i<8;i++)
    	 {
    		 for(int j=0;j<8;j++)
    		 {
    			 chessbe[i][j]=qzwz[i][j];
    		 }
    	 }
    	 
    	 
    	 colorl=color;
	 	 alpha(32767, 0);//進行搜尋
	 	 moveResult(next_[0][0], next_[0][1], next_[1][0],next_[1][1]);
	 	 
    	 return chessbe;
     }
     
 	public static void move(int x, int y, int nx, int ny) {//搬移方法
 		chessbe[nx][ny] = chessbe[x][y];
 		chessbe[x][y] = -1;
 	}
 	
 	public static void moveResult(int x, int y, int nx, int ny) {
 		chessbe[nx][ny] = chessbe[x][y];
 		chessbe[x][y] = -1;
	}

 	public static void record(int x, int y, int nx, int ny) {//記錄方法
 		next_[0][0] = x;
 		next_[0][1] = y;
 		next_[1][0] = nx;
 		next_[1][1] = ny;
 	}

 	public static boolean valid(int x, int y) 
 	{//是否是有效的
 		if (x < 0 || x > 7 || y < 0 || y > 7)
 		{
 			return false;
 		}
 		else
 		{
 			return true;
 		}
 	}
 	
	//機器執黑子
	public static int Value() {
		int score = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				switch (chessbe[i][j]) {
				case GameData.hche: {
					if (j == 0 || j == 7)
						score += 50;
					score += 500;
				}
					break;
				case GameData.hma:
					score += 300;
					break;
				case GameData.hxiang:
					score += 325;
					break;
				case GameData.hhou:
					score += 900;
					break;
				case GameData.hwang:
					score += 20000;
					break;
				case GameData.hbing:
					score += 100;
					break;
				case GameData.bche:
					score -= 500;
					break;
				case GameData.bma:
					score -= 300;
					break;
				case GameData.bxiang:
					score -= 325;
					break;
				case GameData.bhou:
					score -= 900;
					break;
				case GameData.bwang:
					score -= 20000;
					break;
				case GameData.bbing:
					score -= 100;
					break;
				default:
//					score = score;
				}
			}
		}
		return score;
	}
	
	//機器執白子
	public static int Valuel() {
		int score = 0;
		for (int i = 7; i >=0; i--) {
			for (int j = 7; j >=0; j--) {
				switch (chessbe[i][j]) {
				case GameData.bche: {
					if (j == 7 || j == 0)
						score += 50;
					score += 500;
				}
					break;
				case GameData.bma:
					score += 300;
					break;
				case GameData.bxiang:
					score += 325;
					break;
				case GameData.bhou:
					score += 900;
					break;
				case GameData.bwang:
					score += 20000;
					break;
				case GameData.bbing:
					score += 100;
					break;
				case GameData.hche:
					score -= 500;
					break;
				case GameData.hma:
					score -= 300;
					break;
				case GameData.hxiang:
					score -= 325;
					break;
				case GameData.hhou:
					score -= 900;
					break;
				case GameData.hwang:
					score -= 20000;
					break;
				case GameData.hbing:
					score -= 100;
					break;
				default:
//					score = score;
				}
			}
		}
		return score;
	}

	public static int producer(int table[][], int x, int y) {//(x,y)是棋碟中白色棋子的位置
 		//table記錄的為走向下一個位置要搬移的i,j
 		int count = 0;
 		int nx = 0;
 		int ny = 0;
 		int k = 0;
 		switch (chessbe[x][y]) {//將循環到的位置的棋子+6判斷其是不是白色子
 		 case GameData.bbing://循環到的位置是白兵
 				if (x == 6) //第6行，從第0行開始算
 				{
 					//若果卒的前方兩個格子的位置沒有棋子
 					//並且前方一個格子的位置沒有棋子
 					if (chessbe[x - 2][y] ==-1
 							&& chessbe[x - 1][y] == -1) 
 					{
 						table[count][0] = -2;//記錄向前走兩步的位置
 						table[count++][1] = 0;
 						preAction=RobotActionEnum.PTZZ;
 					}
 				}
 				//若果傳入位置的左斜前方在棋碟內
 				//並且左斜前方為黑方棋子 ，並且傳入位置不在第6行
 				if (valid(x - 1, y - 1)
 						&& chessbe[x - 1][y - 1] >=6&& chessbe[x-1][y-1] <= 11 && x != 6) 
 				{
 					table[count][0] = -1;//記錄此位置（吃子）
 					table[count++][1] = -1;
 					preAction=RobotActionEnum.CZ;
 				}
 				
 				//若果傳入位置的右斜前方在棋碟內
 				//並且右斜前方為黑方棋子 ，並且傳入位置不在第6行
 				if (valid(x -1, y + 1)
 						&& chessbe[x - 1][y + 1] >=6&&chessbe[x - 1][y + 1] <=11 && x != 6)
 				{
 					table[count][0] = -1;//記錄此位置（吃子）
 					table[count++][1] = 1;
 					preAction=RobotActionEnum.CZ;
 				}
 				//若果傳入位置的前方一個位置在棋碟內
 				//並且前方一個位置無棋子 ，並且傳入位置不在第6行
 				if (valid(x - 1, y) && chessbe[x - 1][y] == -1 && x != 6) {
 					table[count][0] = -1;//記錄此位置
 					table[count++][1] = 0;
 					preAction=RobotActionEnum.PTZZ;
 				}
 			break;
 		case GameData.bche://若循環到的位置是白車
 			for (k = 0; k < 4; ++k)
 			{
 				for (int len = 1; len < 8; ++len) 
 				{
 					if (valid(nx = x + rook_1[k][0] * len, ny = y + rook_1[k][1]* len))  //若果下一個位置在棋碟內
 					{
 						//若果下一個位置無棋子或為黑方棋子
 						if (chessbe[nx][ny]==-1||(chessbe[nx][ny]>=6&&chessbe[nx][ny]<=11)) 
 						{
 							if(chessbe[nx][ny]==-1)
 							{
 								preAction=RobotActionEnum.PTZZ;
 							}
 							else
 							{
 								preAction=RobotActionEnum.CZ;
 							}
 							table[count][0] = nx - x;//記錄下一步的位置
 							table[count++][1] = ny - y;
 						}
 						if (chessbe[nx][ny] != -1)//若果要落子的位置是自己的棋子則break
 							break;
 					}
 				}
 			}
 			break;
 		case GameData.bma://循環到的位置是白馬
 			for (k = 0; k < 8; ++k) 
 			{
 				if (valid(nx = x + knight_1[k][0], ny = y + knight_1[k][1])) //若果下一個位置在棋碟內
 				{
 					//若果下一個位置無棋子或為黑方棋子
 					if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=6&&chessbe[nx][ny]<=11)) 
 					{
 						if(chessbe[nx][ny]==-1)//無棋子則是撲通落子
						{
							preAction=RobotActionEnum.PTZZ;
						}
						else//吃子
						{
							preAction=RobotActionEnum.CZ;
						}
 						table[count][0] = knight_1[k][0];//記錄下一步的位置
 						table[count++][1] = knight_1[k][1];
 					}
 				}
 			}
 			break;
 		case GameData.bxiang://循環到的位置是白象
 			for (k = 0; k < 4; ++k) 
 			{
 				for (int len = 1; len < 8; ++len) 
 				{
 					if (valid(nx = x + bishop_1[k][0] * len, ny = y+ bishop_1[k][1] * len)) //若果下一個位置在棋碟內
 					{
 						//若果下一個位置無棋子或為黑方棋子
 						if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=6&&chessbe[nx][ny]<=11)) 
 						{
 							if(chessbe[nx][ny]==-1)//無棋子則是普通落子
 							{
 								preAction=RobotActionEnum.PTZZ;
 							}
 							else//吃子
 							{
 								preAction=RobotActionEnum.CZ;
 							}
 							table[count][0] = nx - x;
 							table[count++][1] = ny - y;
 						}
 						if (chessbe[nx][ny] != -1)//若果要落子的位置上是自己放的棋子則break
 							break;
 					}
 				}
 			}
 			break;
 		case GameData.bhou://循環到的位置是白後
 			for (k = 0; k < 8; ++k) 
 			{
 				for (int len = 1; len < 8; ++len) 
 				{
 					if (valid(nx = x + queen_1[k][0] * len, ny = y+ queen_1[k][1] * len))//若果下一個位置在棋碟內 
 					{
 						//若果下一個位置無棋子或為黑方棋子
 						if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=6&&chessbe[nx][ny]<=11)) 
 						{
 							if(chessbe[nx][ny]==-1)//無棋子則是普通落子
 							{
 								preAction=RobotActionEnum.PTZZ;
 							}
 							else//吃子
 							{
 								preAction=RobotActionEnum.CZ;
 							}
 							table[count][0] = nx - x;
 							table[count++][1] = ny - y;
 						}
 						if (chessbe[nx][ny] != -1)//若果要落子的位置是自己方的棋子
 							break;
 					}
 				}
 			}
 			break;
 		case GameData.bwang://循環到的位置是白王
 			for (k = 0; k < 8; ++k)
 			{
 				if (valid(nx = x + king_1[k][0], ny = y + king_1[k][1])) //若果下一個位置在棋碟內
 				{
 					//若果下一個位置無棋子或為黑方棋子
 					if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=6&&chessbe[nx][ny]<=11))
 					{
 						if(chessbe[nx][ny]==-1)//無棋子則是普通落子
						{
							preAction=RobotActionEnum.PTZZ;
						}
						else//吃子
						{
							preAction=RobotActionEnum.CZ;
						}
 						table[count][0] = nx - x;
 						table[count++][1] = ny - y;
 					}
 				}
 			}
 			break;
 		case GameData.hbing://循環到的位置是黑兵
				if (x == 1) //第1行，從第0行開始算
				{
					//若果卒的前方兩個格子的位置沒有棋子
					//並且前方一個格子的位置沒有棋子
					if (chessbe[x + 2][y] ==-1
							&& chessbe[x + 1][y] == -1) 
					{
						preAction=RobotActionEnum.PTZZ;
						table[count][0] = 2;//記錄前方兩個格子所在的位置
						table[count++][1] = 0;
					}
				}
				//若果傳入位置的右斜前方在棋碟內
				//並且左斜前方為白方棋子 ，並且傳入位置不在第1行
				if (valid(x + 1, y + 1)
						&& chessbe[x + 1][y + 1] >=0&& chessbe[x+1][y+1] <= 5 && x != 1) 
				{
					preAction=RobotActionEnum.CZ;
					table[count][0] = 1;
					table[count++][1] = 1;
				}
				
				//若果傳入位置的左斜後方在棋碟內
				//並且右前方為白方棋子 ，並且傳入位置不在第1行
				if (valid(x + 1, y - 1)
						&& chessbe[x + 1][y - 1] >=0&&chessbe[x + 1][y - 1] <=5 && x != 1)
				{
					preAction=RobotActionEnum.CZ;
					table[count][0] = 1;
					table[count++][1] = -1;
				}
				//若果傳入位置的左方在棋碟內
				//並且前方無棋子 ，並且傳入位置不在第1行
				if (valid(x + 1, y) && chessbe[x + 1][y] == -1 && x != 1) {
					preAction=RobotActionEnum.PTZZ;
					table[count][0] = 1;
					table[count++][1] = 0;
				}
			break;

		case GameData.hche://若循環到的位置是黑車
			for (k = 0; k < 4; ++k)
			{
				for (int len = 1; len < 8; ++len) 
				{
					if (valid(nx = x + rook_[k][0] * len, ny = y + rook_[k][1]* len))  //若果下一個位置在棋碟內
					{
						//若果下一個位置無棋子或為白方棋子
						if (chessbe[nx][ny]==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5)) 
						{
							if(chessbe[nx][ny]==-1)//無棋子則是普通落子
 							{
 								preAction=RobotActionEnum.PTZZ;
 							}
 							else//吃子
 							{
 								preAction=RobotActionEnum.CZ;
 							}
							table[count][0] = nx - x;
							table[count++][1] = ny - y;
						}
						if (chessbe[nx][ny] != -1)//若果要落子的位置是自己方的位置則break
							break;
					}
				}
			}
			break;
		case GameData.hma://循環到的位置是黑馬
			for (k = 0; k < 8; ++k) 
			{
				if (valid(nx = x + knight_[k][0], ny = y + knight_[k][1])) //若果下一個位置在棋碟內
				{
					//若果下一個位置無棋子或為白方棋子
					if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5)) 
					{
						if(chessbe[nx][ny]==-1)//無棋子則是普通落子
						{
							preAction=RobotActionEnum.PTZZ;
						}
						else//吃子
						{
							preAction=RobotActionEnum.CZ;
						}
						table[count][0] = knight_[k][0];
						table[count++][1] = knight_[k][1];
					}
				}
			}
			break;
		case GameData.hxiang://循環到的位置是黑象
			for (k = 0; k < 4; ++k) 
			{
				for (int len = 1; len < 8; ++len) 
				{
					if (valid(nx = x + bishop_[k][0] * len, ny = y+ bishop_[k][1] * len)) //若果下一個位置在棋碟內
					{
						//若果下一個位置無棋子或為白方棋子
						if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5)) 
						{
							if(chessbe[nx][ny]==-1)//無棋子則是普通落子
 							{
 								preAction=RobotActionEnum.PTZZ;
 							}
 							else//吃子
 							{
 								preAction=RobotActionEnum.CZ;
 							}
							table[count][0] = nx - x;
							table[count++][1] = ny - y;
						}
						if (chessbe[nx][ny] != -1)//若果要落子的位置是自己方的棋子
							break;
					}
				}
			}
			break;
		case GameData.hhou://循環到的位置是黑後
			for (k = 0; k < 8; ++k) 
			{
				for (int len = 1; len < 8; ++len) 
				{
					if (valid(nx = x + queen_[k][0] * len, ny = y+ queen_[k][1] * len))//若果下一個位置在棋碟內 
					{
						//若果下一個位置無棋子或為白方棋子
						if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5)) 
						{
							if(chessbe[nx][ny]==-1)//無棋子則是普通落子
 							{
 								preAction=RobotActionEnum.PTZZ;//普通走子
 							}
 							else
 							{
 								preAction=RobotActionEnum.CZ;//吃子
 							}
							table[count][0] = nx - x;
							table[count++][1] = ny - y;
						}
						if (chessbe[nx][ny] != -1)
							break;
					}
				}
			}
			break;
		case GameData.hwang://循環到的位置是黑王
			for (k = 0; k < 8; ++k)
			{
				if (valid(nx = x + king_[k][0], ny = y + king_[k][1])) //若果下一個位置在棋碟內
				{
					//若果下一個位置無棋子或為白方棋子
					if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5))
					{
						if(chessbe[nx][ny]==-1)
						{
							preAction=RobotActionEnum.PTZZ;//普通走子
						}
						else
						{
							preAction=RobotActionEnum.CZ;//吃子
						}
						table[count][0] = nx - x;
						table[count++][1] = ny - y;
					}
				}
			}
			break;
 		default:
 			;
 		}
 		return count;
 	}

	public static int alpha(int beta, int depth) 
	{
		if(colorl==1)//機器執黑子
		{
			if (depth >= DEPTH)
			{
				return -Value();
			}
				
			int alpha = -32767;
			int i, j;
			int table[][] = new int[64][2];
			for (i = 0; i < 8; i++) {//循環棋碟上所有位置
				for (j = 0; j < 8; j++) {
					//若果循環到的位置無棋子或是是白色棋子則結束本次循環
					if (chessbe[i][j] ==-1||(chessbe[i][j]>=0&&chessbe[i][j]<=5))
					{
						continue;
					}
						
					int len = producer(table, i, j);
					for (int k = 0; k < len; ++k) 
					{
						int nx = i + table[k][0];
						int ny = j + table[k][1];
						int remember = chessbe[nx][ny];
						move(i, j, nx, ny);
						int value;
						if (chessbe[nx][ny] == GameData.bwang)//若落子的位置是白王
						{
							value = 32767;
						}
						else//要落子的位置不是白王
						{
							value = beta(alpha, depth + 1);
						}
							
						if (alpha < value) 
						{
							alpha = value;
							if (depth == 0)
							{
								record(i, j, nx, ny);
							}
						}
						move(nx, ny, i, j);
						chessbe[nx][ny] = remember;
						if (alpha >= beta)
						{
							return alpha;
						}
					}
				}
			}
			return alpha;
		}
		else//機器執白子
		{
			if (depth >= DEPTH)
			{
				return -Valuel();
			}
				
			int alpha = -32767;
			int i, j;
			int table[][] = new int[64][2];
			for (i = 7; i >=0; i--) //循環棋碟上所有位置
			{
				for (j = 7; j >=0; j--) 
				{
					//循環到的位置無棋子或是是黑色棋子則結束本次循環
					if (chessbe[i][j] ==-1||(chessbe[i][j]>=6&&chessbe[i][j]<=11))
					{
						continue;
					}
						
					int len = producer(table, i, j);
					for (int k = 0; k < len; ++k) 
					{
						int nx = i + table[k][0];
						int ny = j + table[k][1];
						int remember = chessbe[nx][ny];
						move(i, j, nx, ny);
						int value;
						if (chessbe[nx][ny] == GameData.hwang)
						{
							value = 32767;
						}
						else
						{
							value = beta(alpha, depth + 1);
						}
							
						if (alpha < value) 
						{
							alpha = value;
							if (depth == 0)
							{
								record(i, j, nx, ny);
							}
						}
						move(nx, ny, i, j);
						chessbe[nx][ny] = remember;
						if (alpha >= beta)
						{
							return alpha;
						}
					}
				}
			}
			return alpha;
		}
	}

	public static int beta(int alpha, int depth) 
	{
		if(colorl==1)//機器執黑子
		{
			if (depth >= DEPTH)
			{
				return Value();
			}
				
			int beta = 32767;
			int i, j;
			int table[][] = new int[64][2];
			for (i = 0; i < 8; i++) //循環棋碟上所有的位置
			{
				for (j = 0; j < 8; j++) 
				{
					//循環到的位置無棋子或是是黑色棋子則結束本次循環
					if (chessbe[i][j] ==-1||(chessbe[i][j]>=6&&chessbe[i][j]<=11))
					{
						continue;
					}
						
					int len = producer(table, i, j);
					for (int k = 0; k < len; ++k) 
					{
						int nx = i + table[k][0];
						int ny = j + table[k][1];
						int remember = chessbe[nx][ny];
						move(i, j, nx, ny);
						int value;
						if (chessbe[nx][ny] == GameData.hwang)//要落子的位置是黑王
						{
							value = -32767;
						}
						else//要落子的位置不是黑王
						{
							value = alpha(beta, depth + 1);
						}
							
						if (beta > value) 
						{
							beta = value;
						}
						move(nx, ny, i, j);
						chessbe[nx][ny] = remember;
						if (alpha >= beta)
						{
							return beta;
						}
					}
				}
			}
			return beta;
		}
		else//機器執白子
		{
			if (depth >= DEPTH)
			{
				return Valuel();
			}
				
			int beta = 32767;
			int i, j;
			int table[][] = new int[64][2];
			for (i = 7; i >=0; i--) //循環棋碟上所有的位置
			{
				for (j = 7; j >=0; j--) 
				{
					//循環到的位置無棋子或是是白色棋子則結束本次循環
					if (chessbe[i][j] ==-1||(chessbe[i][j]>=0&&chessbe[i][j]<=5))
					{
						continue;
					}
						
					int len = producer(table, i, j);
					for (int k = 0; k < len; ++k) 
					{
						int nx = i + table[k][0];
						int ny = j + table[k][1];
						int remember = chessbe[nx][ny];
						move(i, j, nx, ny);
						int value;
						if (chessbe[nx][ny] == GameData.bwang)//要落子的位置是白王
						{
							value = -32767;
						}
						else
						{
							value = alpha(beta, depth + 1);
						}
							
						if (beta > value) 
						{
							beta = value;
						}
						move(nx, ny, i, j);
						chessbe[nx][ny] = remember;
						if (alpha >= beta)
						{
							return beta;
						}
					}
				}
			}
			return beta;
		}
	}
}
