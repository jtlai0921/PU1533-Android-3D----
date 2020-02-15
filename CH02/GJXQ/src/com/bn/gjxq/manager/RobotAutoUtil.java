package com.bn.gjxq.manager;

import com.bn.gjxq.game.GameData;

public class RobotAutoUtil   
{
	public static int next_[][] = new int[2][2];
	public static int DEPTH = 3;
	public static RobotActionEnum preAction;
	
	//�¦�Ѥl�ϥ�
	private final static int rook_[][] = //��
	{ 
		{ 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 }
	};
	private final static int knight_[][] = 
	{ 
		{ 1, 2 }, { 2, 1 }, { 1, -2 }, { -2, 1 },//��
	 	{ -1, 2 }, { 2, -1 }, { -1, -2 }, { -2, -1 } 
	};
	private final static int bishop_[][] =
	{ 
		{ 1, 1 }, { -1, 1 }, { 1, -1 },{ -1, -1 }//�H
	};
	private final static int queen_[][] = 
	{ 
		{ 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 },//��
	 	{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 }
	};
	private final static int king_[][] = 
	{ 
		{ 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 },//��
	 	{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 }
	};
	
	//�զ�Ѥl�ϥ�
	private final static int rook_1[][] = //��
	{ 
		{ 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 }
	};
	private final static int knight_1[][] = 
	{ 
		{ -1, -2 }, { -2,- 1 }, { -1, 2 }, { 2, -1 },//��
	 	{ 1, -2 }, { -2, 1 }, { 1, 2 }, { 2, 1 } 
	};
	private final static int bishop_1[][] =
	{ 
		{ 1, 1 }, { -1, 1 }, { 1, -1 },{ -1, -1 }//�H
	};
	private final static int queen_1[][] = 
	{ 
		{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 },//��
	 	{ 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }
	};
	private final static int king_1[][] = 
	{ 
		{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 },//��
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
	 	 alpha(32767, 0);//�i��j�M
	 	 moveResult(next_[0][0], next_[0][1], next_[1][0],next_[1][1]);
	 	 
    	 return chessbe;
     }
     
 	public static void move(int x, int y, int nx, int ny) {//�h����k
 		chessbe[nx][ny] = chessbe[x][y];
 		chessbe[x][y] = -1;
 	}
 	
 	public static void moveResult(int x, int y, int nx, int ny) {
 		chessbe[nx][ny] = chessbe[x][y];
 		chessbe[x][y] = -1;
	}

 	public static void record(int x, int y, int nx, int ny) {//�O����k
 		next_[0][0] = x;
 		next_[0][1] = y;
 		next_[1][0] = nx;
 		next_[1][1] = ny;
 	}

 	public static boolean valid(int x, int y) 
 	{//�O�_�O���Ī�
 		if (x < 0 || x > 7 || y < 0 || y > 7)
 		{
 			return false;
 		}
 		else
 		{
 			return true;
 		}
 	}
 	
	//�������¤l
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
	
	//�������դl
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

	public static int producer(int table[][], int x, int y) {//(x,y)�O�ѺФ��զ�Ѥl����m
 		//table�O���������V�U�@�Ӧ�m�n�h����i,j
 		int count = 0;
 		int nx = 0;
 		int ny = 0;
 		int k = 0;
 		switch (chessbe[x][y]) {//�N�`���쪺��m���Ѥl+6�P�_��O���O�զ�l
 		 case GameData.bbing://�`���쪺��m�O�էL
 				if (x == 6) //��6��A�q��0��}�l��
 				{
 					//�Y�G�򪺫e���Ӯ�l����m�S���Ѥl
 					//�åB�e��@�Ӯ�l����m�S���Ѥl
 					if (chessbe[x - 2][y] ==-1
 							&& chessbe[x - 1][y] == -1) 
 					{
 						table[count][0] = -2;//�O���V�e����B����m
 						table[count++][1] = 0;
 						preAction=RobotActionEnum.PTZZ;
 					}
 				}
 				//�Y�G�ǤJ��m�����׫e��b�ѺФ�
 				//�åB���׫e�謰�¤�Ѥl �A�åB�ǤJ��m���b��6��
 				if (valid(x - 1, y - 1)
 						&& chessbe[x - 1][y - 1] >=6&& chessbe[x-1][y-1] <= 11 && x != 6) 
 				{
 					table[count][0] = -1;//�O������m�]�Y�l�^
 					table[count++][1] = -1;
 					preAction=RobotActionEnum.CZ;
 				}
 				
 				//�Y�G�ǤJ��m���k�׫e��b�ѺФ�
 				//�åB�k�׫e�謰�¤�Ѥl �A�åB�ǤJ��m���b��6��
 				if (valid(x -1, y + 1)
 						&& chessbe[x - 1][y + 1] >=6&&chessbe[x - 1][y + 1] <=11 && x != 6)
 				{
 					table[count][0] = -1;//�O������m�]�Y�l�^
 					table[count++][1] = 1;
 					preAction=RobotActionEnum.CZ;
 				}
 				//�Y�G�ǤJ��m���e��@�Ӧ�m�b�ѺФ�
 				//�åB�e��@�Ӧ�m�L�Ѥl �A�åB�ǤJ��m���b��6��
 				if (valid(x - 1, y) && chessbe[x - 1][y] == -1 && x != 6) {
 					table[count][0] = -1;//�O������m
 					table[count++][1] = 0;
 					preAction=RobotActionEnum.PTZZ;
 				}
 			break;
 		case GameData.bche://�Y�`���쪺��m�O�ը�
 			for (k = 0; k < 4; ++k)
 			{
 				for (int len = 1; len < 8; ++len) 
 				{
 					if (valid(nx = x + rook_1[k][0] * len, ny = y + rook_1[k][1]* len))  //�Y�G�U�@�Ӧ�m�b�ѺФ�
 					{
 						//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��¤�Ѥl
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
 							table[count][0] = nx - x;//�O���U�@�B����m
 							table[count++][1] = ny - y;
 						}
 						if (chessbe[nx][ny] != -1)//�Y�G�n���l����m�O�ۤv���Ѥl�hbreak
 							break;
 					}
 				}
 			}
 			break;
 		case GameData.bma://�`���쪺��m�O�հ�
 			for (k = 0; k < 8; ++k) 
 			{
 				if (valid(nx = x + knight_1[k][0], ny = y + knight_1[k][1])) //�Y�G�U�@�Ӧ�m�b�ѺФ�
 				{
 					//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��¤�Ѥl
 					if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=6&&chessbe[nx][ny]<=11)) 
 					{
 						if(chessbe[nx][ny]==-1)//�L�Ѥl�h�O���q���l
						{
							preAction=RobotActionEnum.PTZZ;
						}
						else//�Y�l
						{
							preAction=RobotActionEnum.CZ;
						}
 						table[count][0] = knight_1[k][0];//�O���U�@�B����m
 						table[count++][1] = knight_1[k][1];
 					}
 				}
 			}
 			break;
 		case GameData.bxiang://�`���쪺��m�O�նH
 			for (k = 0; k < 4; ++k) 
 			{
 				for (int len = 1; len < 8; ++len) 
 				{
 					if (valid(nx = x + bishop_1[k][0] * len, ny = y+ bishop_1[k][1] * len)) //�Y�G�U�@�Ӧ�m�b�ѺФ�
 					{
 						//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��¤�Ѥl
 						if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=6&&chessbe[nx][ny]<=11)) 
 						{
 							if(chessbe[nx][ny]==-1)//�L�Ѥl�h�O���q���l
 							{
 								preAction=RobotActionEnum.PTZZ;
 							}
 							else//�Y�l
 							{
 								preAction=RobotActionEnum.CZ;
 							}
 							table[count][0] = nx - x;
 							table[count++][1] = ny - y;
 						}
 						if (chessbe[nx][ny] != -1)//�Y�G�n���l����m�W�O�ۤv�񪺴Ѥl�hbreak
 							break;
 					}
 				}
 			}
 			break;
 		case GameData.bhou://�`���쪺��m�O�ի�
 			for (k = 0; k < 8; ++k) 
 			{
 				for (int len = 1; len < 8; ++len) 
 				{
 					if (valid(nx = x + queen_1[k][0] * len, ny = y+ queen_1[k][1] * len))//�Y�G�U�@�Ӧ�m�b�ѺФ� 
 					{
 						//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��¤�Ѥl
 						if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=6&&chessbe[nx][ny]<=11)) 
 						{
 							if(chessbe[nx][ny]==-1)//�L�Ѥl�h�O���q���l
 							{
 								preAction=RobotActionEnum.PTZZ;
 							}
 							else//�Y�l
 							{
 								preAction=RobotActionEnum.CZ;
 							}
 							table[count][0] = nx - x;
 							table[count++][1] = ny - y;
 						}
 						if (chessbe[nx][ny] != -1)//�Y�G�n���l����m�O�ۤv�誺�Ѥl
 							break;
 					}
 				}
 			}
 			break;
 		case GameData.bwang://�`���쪺��m�O�դ�
 			for (k = 0; k < 8; ++k)
 			{
 				if (valid(nx = x + king_1[k][0], ny = y + king_1[k][1])) //�Y�G�U�@�Ӧ�m�b�ѺФ�
 				{
 					//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��¤�Ѥl
 					if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=6&&chessbe[nx][ny]<=11))
 					{
 						if(chessbe[nx][ny]==-1)//�L�Ѥl�h�O���q���l
						{
							preAction=RobotActionEnum.PTZZ;
						}
						else//�Y�l
						{
							preAction=RobotActionEnum.CZ;
						}
 						table[count][0] = nx - x;
 						table[count++][1] = ny - y;
 					}
 				}
 			}
 			break;
 		case GameData.hbing://�`���쪺��m�O�§L
				if (x == 1) //��1��A�q��0��}�l��
				{
					//�Y�G�򪺫e���Ӯ�l����m�S���Ѥl
					//�åB�e��@�Ӯ�l����m�S���Ѥl
					if (chessbe[x + 2][y] ==-1
							&& chessbe[x + 1][y] == -1) 
					{
						preAction=RobotActionEnum.PTZZ;
						table[count][0] = 2;//�O���e���Ӯ�l�Ҧb����m
						table[count++][1] = 0;
					}
				}
				//�Y�G�ǤJ��m���k�׫e��b�ѺФ�
				//�åB���׫e�謰�դ�Ѥl �A�åB�ǤJ��m���b��1��
				if (valid(x + 1, y + 1)
						&& chessbe[x + 1][y + 1] >=0&& chessbe[x+1][y+1] <= 5 && x != 1) 
				{
					preAction=RobotActionEnum.CZ;
					table[count][0] = 1;
					table[count++][1] = 1;
				}
				
				//�Y�G�ǤJ��m�����׫��b�ѺФ�
				//�åB�k�e�謰�դ�Ѥl �A�åB�ǤJ��m���b��1��
				if (valid(x + 1, y - 1)
						&& chessbe[x + 1][y - 1] >=0&&chessbe[x + 1][y - 1] <=5 && x != 1)
				{
					preAction=RobotActionEnum.CZ;
					table[count][0] = 1;
					table[count++][1] = -1;
				}
				//�Y�G�ǤJ��m������b�ѺФ�
				//�åB�e��L�Ѥl �A�åB�ǤJ��m���b��1��
				if (valid(x + 1, y) && chessbe[x + 1][y] == -1 && x != 1) {
					preAction=RobotActionEnum.PTZZ;
					table[count][0] = 1;
					table[count++][1] = 0;
				}
			break;

		case GameData.hche://�Y�`���쪺��m�O�¨�
			for (k = 0; k < 4; ++k)
			{
				for (int len = 1; len < 8; ++len) 
				{
					if (valid(nx = x + rook_[k][0] * len, ny = y + rook_[k][1]* len))  //�Y�G�U�@�Ӧ�m�b�ѺФ�
					{
						//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��դ�Ѥl
						if (chessbe[nx][ny]==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5)) 
						{
							if(chessbe[nx][ny]==-1)//�L�Ѥl�h�O���q���l
 							{
 								preAction=RobotActionEnum.PTZZ;
 							}
 							else//�Y�l
 							{
 								preAction=RobotActionEnum.CZ;
 							}
							table[count][0] = nx - x;
							table[count++][1] = ny - y;
						}
						if (chessbe[nx][ny] != -1)//�Y�G�n���l����m�O�ۤv�誺��m�hbreak
							break;
					}
				}
			}
			break;
		case GameData.hma://�`���쪺��m�O�°�
			for (k = 0; k < 8; ++k) 
			{
				if (valid(nx = x + knight_[k][0], ny = y + knight_[k][1])) //�Y�G�U�@�Ӧ�m�b�ѺФ�
				{
					//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��դ�Ѥl
					if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5)) 
					{
						if(chessbe[nx][ny]==-1)//�L�Ѥl�h�O���q���l
						{
							preAction=RobotActionEnum.PTZZ;
						}
						else//�Y�l
						{
							preAction=RobotActionEnum.CZ;
						}
						table[count][0] = knight_[k][0];
						table[count++][1] = knight_[k][1];
					}
				}
			}
			break;
		case GameData.hxiang://�`���쪺��m�O�¶H
			for (k = 0; k < 4; ++k) 
			{
				for (int len = 1; len < 8; ++len) 
				{
					if (valid(nx = x + bishop_[k][0] * len, ny = y+ bishop_[k][1] * len)) //�Y�G�U�@�Ӧ�m�b�ѺФ�
					{
						//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��դ�Ѥl
						if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5)) 
						{
							if(chessbe[nx][ny]==-1)//�L�Ѥl�h�O���q���l
 							{
 								preAction=RobotActionEnum.PTZZ;
 							}
 							else//�Y�l
 							{
 								preAction=RobotActionEnum.CZ;
 							}
							table[count][0] = nx - x;
							table[count++][1] = ny - y;
						}
						if (chessbe[nx][ny] != -1)//�Y�G�n���l����m�O�ۤv�誺�Ѥl
							break;
					}
				}
			}
			break;
		case GameData.hhou://�`���쪺��m�O�«�
			for (k = 0; k < 8; ++k) 
			{
				for (int len = 1; len < 8; ++len) 
				{
					if (valid(nx = x + queen_[k][0] * len, ny = y+ queen_[k][1] * len))//�Y�G�U�@�Ӧ�m�b�ѺФ� 
					{
						//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��դ�Ѥl
						if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5)) 
						{
							if(chessbe[nx][ny]==-1)//�L�Ѥl�h�O���q���l
 							{
 								preAction=RobotActionEnum.PTZZ;//���q���l
 							}
 							else
 							{
 								preAction=RobotActionEnum.CZ;//�Y�l
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
		case GameData.hwang://�`���쪺��m�O�¤�
			for (k = 0; k < 8; ++k)
			{
				if (valid(nx = x + king_[k][0], ny = y + king_[k][1])) //�Y�G�U�@�Ӧ�m�b�ѺФ�
				{
					//�Y�G�U�@�Ӧ�m�L�Ѥl�ά��դ�Ѥl
					if (chessbe[nx][ny] ==-1||(chessbe[nx][ny]>=0&&chessbe[nx][ny]<=5))
					{
						if(chessbe[nx][ny]==-1)
						{
							preAction=RobotActionEnum.PTZZ;//���q���l
						}
						else
						{
							preAction=RobotActionEnum.CZ;//�Y�l
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
		if(colorl==1)//�������¤l
		{
			if (depth >= DEPTH)
			{
				return -Value();
			}
				
			int alpha = -32767;
			int i, j;
			int table[][] = new int[64][2];
			for (i = 0; i < 8; i++) {//�`���ѺФW�Ҧ���m
				for (j = 0; j < 8; j++) {
					//�Y�G�`���쪺��m�L�Ѥl�άO�O�զ�Ѥl�h���������`��
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
						if (chessbe[nx][ny] == GameData.bwang)//�Y���l����m�O�դ�
						{
							value = 32767;
						}
						else//�n���l����m���O�դ�
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
		else//�������դl
		{
			if (depth >= DEPTH)
			{
				return -Valuel();
			}
				
			int alpha = -32767;
			int i, j;
			int table[][] = new int[64][2];
			for (i = 7; i >=0; i--) //�`���ѺФW�Ҧ���m
			{
				for (j = 7; j >=0; j--) 
				{
					//�`���쪺��m�L�Ѥl�άO�O�¦�Ѥl�h���������`��
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
		if(colorl==1)//�������¤l
		{
			if (depth >= DEPTH)
			{
				return Value();
			}
				
			int beta = 32767;
			int i, j;
			int table[][] = new int[64][2];
			for (i = 0; i < 8; i++) //�`���ѺФW�Ҧ�����m
			{
				for (j = 0; j < 8; j++) 
				{
					//�`���쪺��m�L�Ѥl�άO�O�¦�Ѥl�h���������`��
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
						if (chessbe[nx][ny] == GameData.hwang)//�n���l����m�O�¤�
						{
							value = -32767;
						}
						else//�n���l����m���O�¤�
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
		else//�������դl
		{
			if (depth >= DEPTH)
			{
				return Valuel();
			}
				
			int beta = 32767;
			int i, j;
			int table[][] = new int[64][2];
			for (i = 7; i >=0; i--) //�`���ѺФW�Ҧ�����m
			{
				for (j = 7; j >=0; j--) 
				{
					//�`���쪺��m�L�Ѥl�άO�O�զ�Ѥl�h���������`��
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
						if (chessbe[nx][ny] == GameData.bwang)//�n���l����m�O�դ�
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
