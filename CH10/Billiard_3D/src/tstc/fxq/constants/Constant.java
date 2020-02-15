package tstc.fxq.constants;

import java.util.ArrayList;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.parts.BallKongZhi;
import tstc.fxq.threads.MoveCameraThread;
import tstc.fxq.utils.PZJCUtil;

public class Constant
{
	public static int SCREEN_WIDTH;//螢幕的寬度
	public static int SCREEN_HEIGHT;//螢幕的高度
	public static float RATIO;//螢幕長寬比
	
	public static boolean pauseFlag=false;//是否暫停程式
 
	public final static float TABLE_UNIT_SIZE=0.5f;//桌底單位長度
	public final static float TABLE_UNIT_HIGHT=0.5f;//桌底單位高度
	
	public static boolean MQJDSYYJBF=false;//母球進洞音效已經播放

	public final static float BOTTOM_HIGHT=2.0f;//桌子底高度
	public final static float BOTTOM_LENGTH=36.0f;//桌子底長度
	public final static float BOTTOM_WIDE=24.0f;//桌子底寬度
	
	public static final float ANGLE_SPAN=12f;//將球進行單位切分的角度
	public static final float UNIT_SIZE=0.8f;//球單位尺寸
	//球搬移每一步的類比時間間隔
	public static final float TIME_SPAN=0.04f; 
	public static final float BALL_SCALE=0.8f;//球縮放值
	public static final float BALL_R=UNIT_SIZE*BALL_SCALE;//球的半徑
	public static final float BALL_R_POWER_TWO=BALL_R*BALL_R;//球半徑的平方
	public static final float V_TENUATION=0.987f;//速度衰減系數
	public static final float V_THRESHOLD=0.10f;//速度設定值，小於此設定值的速度認為為0
	public static final int   THREAD_SLEEP=20;//執行緒休眠時間
	
	public static final float MIDDLE=4.42f*BALL_R*0.9f;//中洞距離
	public static final float EDGE_SMALL=2.5f*BALL_R;//底洞橫縱距離（小區塊）
	public static final float EDGE_BIG=3*BALL_R;//地洞橫縱距離（大區塊）
	public static final float EDGE=EDGE_SMALL+EDGE_BIG;//地洞縱橫距離,大區塊+小區塊
	
	public static final float UP_DOWN_LENGTH=BOTTOM_LENGTH/2-EDGE-MIDDLE/2;//上下邊緣長度。
	public static final float LEFT_RIGHT_LENGTH=BOTTOM_WIDE-2*EDGE;//左右邊緣長度
	public static final float UP_DOWN_HIGHT=2.0f;//邊緣高度。
	public static final float TABLE_AREA_LENGTH=BOTTOM_LENGTH-2*EDGE_BIG;//桌面長度
	public static final float TABLE_AREA_WIDTH=BOTTOM_WIDE-2*EDGE_BIG;//桌面寬度 
	public static final float CIRCLE_R=3.0f*BALL_R;//球洞的大小
	public static float vBall;//記錄擊球的力道大小,方向
	public static boolean cueFlag=true;//重繪球桿的標志位
	public static boolean overFlag=false;//打球結束的標志位
	public static boolean hitFlag=false;//控制是否打球  
	public static boolean hitSound=false;//控制球球碰撞的音效
	  
	public static int score=0;//記錄得分	  
	
	public static final float GOT_SCORE_DISTANCE=EDGE_BIG-BALL_R*0.05f;//球心與外邊緣的距離，用於判斷是否進球
	public static final float CUE_ROTATE_DEGREE_SPAN=0.25f;//球桿每次轉動的角度
	
	public static final float TEXTURE_RECT_Y=-2;//桌面Y座標
	public static final float DELTA=-1f+0.5f;//小間距
	public static final float DELTA_BALL=0.5f;//球小間距
	public static final float WALL_SIZE=70.0f*TABLE_UNIT_SIZE;//牆面大小
	public static final float BALL_Y_ADJUST=0.20f;//由於影子不和球台面重合，而在球台面上方，故球要有一個y方向的位置調整值
	public static final float BALL_Y=TEXTURE_RECT_Y+DELTA_BALL+BALL_Y_ADJUST;//起始球的y位置
	public static float CUE_Y_ADJUST=0.0f;//球桿動畫調整值
	public static boolean CUE_DH_FLAG=false;//球桿動畫播放中標志位
	public static final float V_BALL_MAX = 20;//球的最大速度
	
	public static final float BOTTOM_Y=-4;//球桌地面Y位置
	
	public static final float MiniMapScale=0.05f;//球桌拉遠比例值
	
	public static final float screenRatio800x480=1.667f;
	public static final float screenRatio854x480=1.779f;
	
	//關於桌球起始位置的常數
	public static final float MAIN_BALL_INIT_ZOFFSET=8.6f;//母球起始的Z位置
	public static final float DIS_WITH_MAIN_BALL=14.0f;//一號球和母球間的距離
	public static float GAP_BETWEEN_BALLS=0.12f;//球與球之間的起始間隙
	
	//關於角度的常數
    public static final float FREE_SIGHT_DIS = 130;//自由角度時攝影機和目的點的距離
    public static final float FIRST_SIGHT_DIS = 60;//第一人稱角度時攝影機和目的點的距離
    
    public static final float FREE_SIGHT_DIS_MIN = 30;//自由角度時攝影機和目的點的最近距離
    public static final float FIRST_SIGHT_DIS_MIN = 20;//第一人稱角度時攝影機和目的點的最近距離
    
    public static final float FREE_SIGHT_DIS_MAX = 130;//自由角度時攝影機和目的點的最遠距離
    public static final float FIRST_SIGHT_DIS_MAX = 80;//第一人稱角度時攝影機和目的點的最遠距離
    
    public static final float FREE_SIGHT_DIS_SPAN = 3;//自由角度時攝影機和目的點的最近距離
    public static final float FIRST_SIGHT_DIS_SPAN = 2;//第一人稱角度時攝影機和目的點的最近距離
    
    public static final float FREE_MIN_ELEVATION = 10;//自由角度時最小仰角
    public static final float FIRST_MIN_ELEVATION = 2;///第一人稱時最小仰角
    
	public static float[][] MINI_MAP_TRASLATE=//迷你圖的位置
	{
		{//480*320
			2.3f,1.6f,-9,
		},
		{//480*854
			3.0f,1.5f,-9,
		},
		{//800*480
			2.7f,1.6f,-9,
		}
	};
	
	//2D界面自適應屏時的常數
	public static final int screenWidthTest=800;//測試機螢幕寬度
	public static final int screenHeightTest=480;//測試機螢幕高度

	public static float wRatio;//適應全螢幕的縮放比例
	public static float hRatio;
	
	//起始化常數的冪等方法
	public static boolean isInitFlag = false;//方法是否被呼叫過的標志位	
	public static void initConst(int screenWidth,int screenHeight)
	{
		//若果方法已經執行過，則不再執行
		if(isInitFlag == true){
			return;
		}
		
		SCREEN_WIDTH=screenWidth;//螢幕的尺寸
		SCREEN_HEIGHT=screenHeight;
		//適應全螢幕的縮放比例
		wRatio=screenWidth/(float)screenWidthTest;
		hRatio=screenHeight/(float)screenHeightTest;
		
		//標示方法已被執行過
		isInitFlag = true;
	}
	
	//游戲玩法，0代表8球模式，1代表9球模式
	public static int POS_INDEX = 0;
	//游戲模式
	public static GameMode mode = GameMode.countDown;
	
	//慣性執行緒工作標志位   
	public static boolean threadWork;
	//關於解除粘滯性問題的常數
	public static final int MAX_COLLISION_NUM = 3;//最大連續碰撞的次數
	public static int collisionCount;//目前連續碰撞的次數
	public static int preCollisionIdA = -1;//上次碰撞的A球的id
	public static int preCollisionIdB = -1;//上次碰撞的B球的id
	//##########################以下是規則中用到的程式碼##################################################

	
	public static long START_TIME = 0;		//上一次擊球的結束時間(未擊球的開始時間)
	//出現犯規Toast的編號
	public static final int OVERTIMETOAST = 0;	//未擊球超過60秒時，出現犯規的Toast
	public static final int REMINDPLAYER = 1;	//未擊球超過45秒時，提醒玩家
	
	public static final int MAINBALL_FLOP = 2;	//主球落洞Toast
	public static final int NO_FIGHT = 3;		//沒有與任何球發生碰撞事件犯規
	public static final int NO_FIGHT_AND_FLOP = 4;		//沒有與任何球發發生碰撞事件並且主球落洞
	public static final int NO_FIGHT_TARGET = 5;
	public static final int MAINBALLFLOP_AND_NOTFIGHTTARGET = 6;//主球進洞，未擊中目的球
	
	public static final int EXIT_SYSTEM_LOSE9 = 7;//離開才程式交談視窗
	public static final int EXIT_SYSTEM_LOSE8 = 8;//離開才程式交談視窗
	public static final int EXIT_SYSTEM_WIN = 9;//離開才程式交談視窗
	public static final int TIME_UP = 10;//時間到
	public static final int BREAK_RECORD = 11;//時間到
	
	
	public static boolean IS_FIGHT=false;	//在一次擊球過程中，是否與任何球發生碰撞的標志位
	public static boolean MAINBALL_FLOPFLAG=false;	//主球是否進洞的標志位，若未碰到其他球，主球進洞後，則不顯示“未與其他球碰撞的Toast”
	public static boolean MAINBALL_FIRST_FIGHT = false;//在一次擊球過程中，主球與其他球第一次碰撞的標志位
	public static boolean FIRST_FIGHT_ERROR = false; //第一次於主球碰撞的球不是最小球的標志位
	
	
	
	//將白球還原到起始位置的方法
	public static void recoverWhiteBall(
			MySurfaceView mv,
			ArrayList<BallKongZhi> ballAl 	//所有桌球的清單
			)
	{
		ballAl.get(0).zOffset=MAIN_BALL_INIT_ZOFFSET;
		if(ballAl.size()>=2)
		{
			//動態搜尋可以的X位置
			for(int ik=0;ik<11;ik++)
			{									
				boolean zjTemp=false;
				ballAl.get(0).xOffset=ik*BALL_R;										
				for(int jk=1;jk<ballAl.size();jk++)
				{
					zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
					if(zjTemp==true)
					{
						break;
					}
				}										
				if(zjTemp==false)
				{
					break;
				}
				
				zjTemp=false;
				ballAl.get(0).xOffset=-ik*BALL_R;										
				for(int jk=1;jk<ballAl.size();jk++)
				{
					zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
					if(zjTemp==true)
					{
						break;
					}
				}	
				if(zjTemp==false)
				{
					break;
				}
			}
		}
		ballAl.get(0).yOffset=1;
		MQJDSYYJBF=false;
		mv.cue.setAngle(0);
		//重繪母球
		MySurfaceView.miniBall=true;
		
		//*******************播放攝影機運動的動畫效果************************
		
		
		//記錄擊球後白球的位置
		MoveCameraThread.xTo = ballAl.get(0).xOffset;
		MoveCameraThread.zTo = ballAl.get(0).zOffset;
		
		if(mv.currSight == Sight.first){
			//若果目前攝影機執行緒為空，或是執行緒已經死亡，再開啟搬移攝影機的執行緒
			if(
					MoveCameraThread.currThread == null || 
					!MoveCameraThread.currThread.isAlive()
				)
			{
				//所有球停止後，開啟搬移攝影機的執行緒
				MoveCameraThread.currThread = new MoveCameraThread(mv);	
				MoveCameraThread.currThread.start();
			}
		}					//若果是自由角度，直接顯示球桿
		else{
			//顯示球桿的標志位	
			
		}

	}
	//將白球還原到起始位置的方法
	public static void recoverWhiteBallNoCam(
			MySurfaceView mv,
			ArrayList<BallKongZhi> ballAl 	//所有桌球的清單
			)
	{
		ballAl.get(0).zOffset=MAIN_BALL_INIT_ZOFFSET;
		if(ballAl.size()>=2)
		{
			//動態搜尋可以的X位置
			for(int ik=0;ik<11;ik++)
			{									
				boolean zjTemp=false;
				ballAl.get(0).xOffset=ik*BALL_R;										
				for(int jk=1;jk<ballAl.size();jk++)
				{
					zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
					if(zjTemp==true)
					{
						break;
					}
				}										
				if(zjTemp==false)
				{
					break;
				}
				
				zjTemp=false;
				ballAl.get(0).xOffset=-ik*BALL_R;										
				for(int jk=1;jk<ballAl.size();jk++)
				{
					zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
					if(zjTemp==true)
					{
						break;
					}
				}	
				if(zjTemp==false)
				{
					break;
				}
			}
		}
		ballAl.get(0).yOffset=1;
		MQJDSYYJBF=false;
		mv.cue.setAngle(0);
		//重繪母球
		MySurfaceView.miniBall=true;

	}
	//***************************逾時犯規end**************************************
}