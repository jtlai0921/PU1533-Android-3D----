package tstc.fxq.main;
import static tstc.fxq.constants.Constant.BALL_SCALE;
import static tstc.fxq.constants.Constant.CUE_DH_FLAG;
import static tstc.fxq.constants.Constant.CUE_Y_ADJUST;
import static tstc.fxq.constants.Constant.MINI_MAP_TRASLATE;
import static tstc.fxq.constants.Constant.MiniMapScale;
import static tstc.fxq.constants.Constant.cueFlag;
import static tstc.fxq.constants.Constant.mode;
import static tstc.fxq.constants.Constant.pauseFlag;
import static tstc.fxq.constants.Constant.threadWork;
import static tstc.fxq.constants.Constant.vBall;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import tstc.fxq.constants.Constant;
import tstc.fxq.constants.GameMode;
import tstc.fxq.constants.Sight;
import tstc.fxq.constants.WhatMessage;
import tstc.fxq.parts.BallKongZhi;
import tstc.fxq.parts.BallDingDian;
import tstc.fxq.parts.Cue;
import tstc.fxq.parts.MiniLine;
import tstc.fxq.parts.MiniMap;
import tstc.fxq.parts.PoolMap;
import tstc.fxq.parts.ZhuoZi;
import tstc.fxq.parts.NearPoint;
import tstc.fxq.parts.Percentage;
import tstc.fxq.parts.ProgressBar;
import tstc.fxq.parts.TextureRect;
import tstc.fxq.parts.Qiang;
import tstc.fxq.threads.BallGoThread;
import tstc.fxq.threads.MoveCameraThread;
import tstc.fxq.threads.RegulationTimeThread;
import tstc.fxq.threads.ThreadKey;
import tstc.fxq.threads.TimeRunningThread;
import tstc.fxq.utils.Board;
import tstc.fxq.utils.InstrumentBoardFactory;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.RainbowBar;
import tstc.fxq.utils.Score;
import tstc.fxq.utils.ShaderManager;
import tstc.fxq.utils.Timer;
import tstc.fxq.utils.VirtualButton;
import tstc.fxq.utils.VirtualButtonChangePic;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class MySurfaceView extends GLSurfaceView{
	private float TOUCH_SCALE_FACTOR_X;	//X方向縮放比例
	private float TOUCH_SCALE_FACTOR_Y; //y方向縮放比例
	
	float mPreviousX;
	float mPreviousY;
	public MyActivity activity=(MyActivity)this.getContext();
	
	//攝影機的位置
	float cx=0;//攝影機x位置
	float cy=50;//攝影機y位置
	float cz=50;//攝影機z位置
	
	float tx=0;//目的點x位置
	float ty=0;//目的點y位置
	float tz=0;//目的點z位置
	
	//透視投影的縮放因子
    float factor = 0.38f;
    
    //站在白球的位置看攝影機時的關聯值
	public float currSightDis=Constant.FIRST_SIGHT_DIS;//攝影機和目的的距離
	float angdegElevation=10;//仰角
	public float angdegAzimuth=0;//方位角
	//目前最小仰角
	float currMinElevation = Constant.FIRST_MIN_ELEVATION;//開始為第一人稱的最小仰角
	//角度模式
	public Sight currSight = Sight.first;//開始為第一人稱角度
	
	//關於慣性的變數
    float yAngleV=0;//總場景角度變化速度
    boolean isMove=false;//觸摸事件是否為搬移的標志位
    long previousTime;//上次按下的時間
    
	// 攝影機位置追蹤母球的方法
	public void setCameraPostion(float xOffset, float zOffset) {
		/*
		 * 將白球位置設為目的
		 * 
		 * 在設計的時候，球的起始位置和桌面差90度
		 * 故繪制球時整體沿y軸旋轉了-90度
		 * 座標也要對應轉換成旋轉後的
		 */
		//若為第一人稱角度，目的點是母球，攝影機的距離較近
		if(currSight == Sight.first){
			tx = -zOffset;
			ty = Constant.BALL_Y;
			tz = xOffset;
		}
		//若為自由角度，目的點是球台中心點，攝影機的距離較遠
		else if(currSight == Sight.free){
			tx = 0;
			ty = Constant.BALL_Y;
			tz = 0;
		}
		//計算攝影機的位置
		double angradElevation = Math.toRadians(angdegElevation);// 仰角（弧度）
		double angradAzimuth = Math.toRadians(angdegAzimuth);// 方位角
		cx = (float) (tx - currSightDis * Math.cos(angradElevation)	* Math.cos(angradAzimuth));
		cy = (float) (ty + currSightDis * Math.sin(angradElevation));
		cz = (float) (tz + currSightDis * Math.cos(angradElevation) * Math.sin(angradAzimuth));
	}
	
    private SceneRenderer mRenderer;//場景著色器
    
    //游戲中的狀態值
    int size;//記錄球的個數    
    public static boolean miniBall=true;//控制繪制迷你母球
    static boolean mFlag=true;//控制是否開啟小地圖
    public static int state = -1;//記錄按下的虛擬按鈕
    static boolean moveFlag=false;//若果在點擊虛擬鍵時，手搬移了,那麼改為false，定為不是ACTION_MOVE事件。
    public static List<Integer> alBallMiniId;//迷你桌球紋理ID清單
    public static List<Integer> alBallTexId;//桌球紋理ID
    static boolean downFlag=false;//若果在move時，down事件不能使用。
    
    
    
    int currProgress;//進度指示器的進度
    //紋理id宣告
    int wel_8;	//載入界面的背景圖片 
    int wel_9;	//載入界面的背景圖片
    int process_top; //進度指示器的上層圖片
    int process_bottom;	//進度指示器的下層圖片
    int num[];		//11張數字圖片（內含百分號）
    
    
    //紋理id宣告
    int cueId;
	int ballTexId;
	public int wallId;//牆面和地板Id
	int tableBottomId;//桌底紋理ID		
	int tableAreaId;//桌面紋理ID
	int tableAreaHelpShadowId;//為球的影子服務的桌面紋理
	int tableRoundUDId;//桌子上下邊緣ID
	int tableRoundLRId;//桌子左右邊緣ID
	int circleId;//洞ID	
	public int iconId;//移標版Id
	public int numbersId;//分數Id
	int ballMiniId;//迷你桌球紋理ID
	int cueMiniId;//迷你球桿紋理ID
	int leftId;//虛擬按鍵左轉動紋理ID
	int rightId;//虛擬按鍵右轉動紋理ID
	int mId;//虛擬鍵M鍵ID
	int goId;//虛擬鍵擊球的ID
	int miniTableId;//迷你球桌紋理ID	
	int poolPartOneId;//球台中洞部分紋理ID
	int poolPartTwoId;//球台中洞部分紋理ID	
	int poolPartTwoJiaoId;//球台邊洞部分紋理ID
	int poolPartOneMId;//球台中洞部分紋理ID	
	int greenId;//球台中洞綠色部分紋理ID	
	int barId;//力道條背景
	int sightFreeId;//自由角度紋理id
	int sightFirstId;//第一人稱角度紋理id
	int nearId;//拉近圖片紋理id
	int farId;//拉遠圖片紋理id
	int breakId;//分隔符id
	
	
	//********************************進度指示器begin*****************
    TextureRect wel;//載入界面的背景圖片
	ProgressBar process;//進度指示器
	Percentage perc;//進度的百分比
	//********************************進度指示器end*****************
	
	//游戲主場景中物件的參考
    public static ArrayList<BallKongZhi> ballAl;//桌球的清單    
	public Cue cue;//球桿	
	MiniMap ballTexture;//迷你地圖上的球
	MiniMap cueMini;//迷你球桿
	MiniLine line;//線 
	PoolMap poolMap;
	Qiang wall;//牆面和地板
	BallDingDian btbv;//用於繪制的球    
	ZhuoZi drawTable;//桌子
	
    //自適應屏的所有儀表板的物件
	VirtualButton goBtn;//go按鈕
	VirtualButton rightBtn;//右按鈕
	VirtualButton leftBtn;//左按鈕
	VirtualButton mBtn;//m按鈕
	Board iconBoard;//球儀表板
	RainbowBar rainbowBar;//彩虹條
	Board barBoard;//力道條背景
	public Score score;//進球個數
	VirtualButtonChangePic sightBtn;//角度轉換按鈕
	VirtualButton nearBtn;//調節攝影機和目的點距離的按鈕
	VirtualButton farBtn;//調節攝影機和目的點距離的按鈕
	public Timer timer;//倒計時器
	//執行緒物件的參考
    public BallGoThread bgt;//桌球的運動執行緒
    static ThreadKey key;//檢驗按下虛擬鍵的執行緒
    TimeRunningThread timeRunningThread;//倒計時執行緒
    //++++++++++++++++++++++++++++++逾時犯規用的程式碼begin++++++++++++++++++++++++++++++++++
    RegulationTimeThread regTimeThread;	//每一秒判斷一次是否逾時的執行緒
    //++++++++++++++++++++++++++++++逾時犯規用的程式碼end++++++++++++++++++++++++++++++++++
    
    boolean hasLoadOk=false;//是否已經載入完成
	public MySurfaceView(Context context) {
        super(context);
        
        this.setKeepScreenOn(true);        
        
        this.setEGLContextClientVersion(2); //設定使用OPENGL ES2.0
        mRenderer = new SceneRenderer();	//建立場景著色器
        setRenderer(mRenderer);				//設定著色器		
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設定著色模式為主動著色
        
        ballAl=new ArrayList<BallKongZhi>();//桌球的清單
        alBallMiniId=new ArrayList<Integer>();//迷你桌球紋理ID清單
        alBallTexId=new ArrayList<Integer>();//桌球紋理ID
        
        //++++++++++++++++++++++++++++++逾時犯規用的程式碼begin++++++++++++++++++++++++++++++++++
        regTimeThread = new RegulationTimeThread(this,ballAl);
        //++++++++++++++++++++++++++++++逾時犯規用的程式碼end++++++++++++++++++++++++++++++++++
        
        //每次起始化後，還原靜態常數的值
        miniBall=true;//控制繪制迷你母球
        mFlag=true;//控制是否開啟小地圖
        state = -1;//記錄按下的虛擬按鈕
        moveFlag=false;//若果在點擊虛擬鍵時，手搬移了,那麼改為false，定為不是ACTION_MOVE事件。
        alBallTexId=new ArrayList<Integer>();//桌球紋理ID
        downFlag=false;//若果在move時，down事件不能使用。
        Constant.cueFlag=true;//繪制球桿標志位  
        
        //啟動一個執行緒根據目前的角速度旋轉場景
        Constant.threadWork=true;
        new Thread()
        {
        	public void run()
        	{
        		while(threadWork)
        		{ 
        			//若果球桿在顯示中
        			if(cueFlag){
            			//根據角速度計算新的場景旋轉角度
            			angdegAzimuth-=yAngleV;
            			//將角度規格化到0～360之間
            			angdegAzimuth=(angdegAzimuth+360)%360;
            			
            			if(ballAl != null && ballAl.size() != 0){
        	            	//重新設定攝影機的位置
        	 	            setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);
            			}
    	 	            
            			//若目前手指已經抬起則角速度衰減
            			if(!isMove)
            			{
            				yAngleV=yAngleV*0.8f;
            			}
            			//若角速度小於設定值則歸0
            			if(Math.abs(yAngleV)<0.05)
            			{
            				yAngleV=0;
            			}
        			}
        			
        			try 
        			{
						Thread.sleep(50);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
        		}
        	}
        }.start();
    }
	
	@Override
    public boolean onTouchEvent(MotionEvent e) {
		
		//若果正在載入，傳回true
		if(!hasLoadOk){
			return true;
		}
		
		float y = e.getY();
        float x = e.getX();

        float dx = x - mPreviousX;//計算觸控筆Y位移
    	float dy = y - mPreviousY;//計算觸控筆Y位移

        long currTime=System.currentTimeMillis();//取得目前時間戳
        long timeSpan=(currTime-previousTime)/10;//計算兩次觸控事件之間的時間差
        
        switch (e.getAction()) 
        {
        case MotionEvent.ACTION_DOWN: 
        	
        	isMove=false;
        	
        	if(CUE_DH_FLAG==true)
    		{//球桿擊打動畫播放中，不響應事件
    			break;
    		} 
        	//若果觸控事件發生在彩虹上，則改變力道
        	if(rainbowBar.isActionOnRainbowBar(x, y)){
        		rainbowBar.changePower(x, y);
        		moveFlag=false;//在虛擬按鈕位置按下，鎖定move事件
        	}
        	//若果在自由角度中點擊圖示，會切換到俯視角度
        	else if(iconBoard.isActionOnBoard(x, y) && currSight == Sight.free){
        		angdegElevation = 89.99f;
        		angdegAzimuth = 90;
        		//改成最合適的俯視距離
    			currSightDis = Constant.FREE_SIGHT_DIS;
        		//重新設定攝影機的位置
	            setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);
	            moveFlag=false;//在虛擬按鈕位置按下，鎖定move事件
        	}
    		if(downFlag==true)
        	{
	        	//若果觸控事件發生在右按鈕上
	        	if(rightBtn.isActionOnButton(x, y))
	        	{
	        		rightBtn.pressDown();//按下按鈕
	        		if(Constant.cueFlag)	//轉場結束，並且在有球桿時才能轉動球桿
	        		{
	        			state=ThreadKey.rightRotate;//表示按下右轉動按鈕
	            		moveFlag=false;//在虛擬按鈕位置按下，鎖定move事件
	        		}
	        	}
	        	//若果觸控事件發生在左按鈕上
	        	else if(leftBtn.isActionOnButton(x, y))
	        	{
	        		leftBtn.pressDown();//按下按鈕
	        		if(cueFlag)	//轉場結束，並且在有球桿時才能轉動球桿
	        		{
	        			state=ThreadKey.leftRotate;//表示按下左轉動按鈕
	            		moveFlag=false;//在虛擬按鈕位置按下，鎖定move事件
	        		}
	        	}
	        	//若果觸控事件發生在m按鈕上
	        	else if(mBtn.isActionOnButton(x, y))
	        	{
	        		mBtn.pressDown();//按下按鈕
	        		mFlag=!mFlag;//按下M鍵
	        		moveFlag=false;//在虛擬按鈕位置按下，鎖定move事件
	        	}
	        	//若果觸控事件發生拉近按鈕上，並且目前球已經停止
	        	else if(nearBtn.isActionOnButton(x, y))
	        	{
	        		if(cueFlag){
		        		nearBtn.pressDown();//按下按鈕
		        		state=ThreadKey.nearer;//表示按下拉近按鈕
	        		}
	        		moveFlag=false;//在虛擬按鈕位置按下，鎖定move事件
	        	}
	        	//若果觸控事件發生在拉遠按鈕上，並且目前球已經停止
	        	else if(farBtn.isActionOnButton(x, y))
	        	{
	        		if(cueFlag){
		        		farBtn.pressDown();//按下按鈕
		        		state=ThreadKey.farther;//表示按下拉遠按鈕
		        	}
	        		moveFlag=false;//在虛擬按鈕位置按下，鎖定move事件
	        	}
	        	//若果觸控事件發生在切換角度按鈕上
	        	else if(sightBtn.isActionOnButton(x, y))
	        	{
	        		sightBtn.pressDown();//按下按鈕
	        		//切換角度模式
	        		if(!sightBtn.isBtnPressedDown()){
	        			//改變最小仰角
	        			currMinElevation = Constant.FIRST_MIN_ELEVATION;
	        			angdegElevation = 10;
	        			//切換到第一人稱角度前，記錄擊球前母球的位置，使攝影機從此處開始運動
						MoveCameraThread.xFrom = ballAl.get(0).xOffset;
						MoveCameraThread.zFrom = ballAl.get(0).zOffset;

		        		//將目前距離限制在第一角度距離的範圍內
		        		currSightDis = Math.min(currSightDis, Constant.FIRST_SIGHT_DIS_MAX);
		        		
	        			//獲得切換前的球桿旋轉角度
	        			float tempAngle = cue.getAngle();
	        			//改變目前角度
	        			currSight = Sight.first;
	        			//球桿的旋轉角度設為切換前的角度
	        			cue.setAngle(tempAngle);
	        		}
	        		else{
	        			//改變最小仰角
	        			currMinElevation = Constant.FREE_MIN_ELEVATION;
	        			//若果切換前目前角度小於最小角度，將目前角度設為最小角度
	        			if(angdegElevation > currMinElevation){
	        				angdegElevation = currMinElevation;
	        			}
	        			//將目前距離限制在自由角度距離的範圍內
		        		currSightDis = Math.max(currSightDis, Constant.FREE_SIGHT_DIS_MIN);
		        		
	        			//獲得切換前的球桿旋轉角度
	        			float tempAngle = cue.getAngle();
	        			//改變目前角度
	        			currSight = Sight.free;
	        			//球桿的旋轉角度設為切換前的角度
	        			cue.setAngle(tempAngle);
	        		}
	        		//重新設定攝影機的位置
	        		setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);
	        		moveFlag=false;//在虛擬按鈕位置按下，鎖定move事件
	        	}
	        	//若果觸控事件發生在go按鈕上
	        	else if(goBtn.isActionOnButton(x, y))
	        	{
	        		goBtn.pressDown();//按下按鈕
	        		//擊球虛擬按鈕
		        	if(cueFlag)	
		        	{
		        		CUE_DH_FLAG=true;
		        		new Thread()
						{
							public void run()
							{
								for(int i=0;i<5;i++)
								{
									CUE_Y_ADJUST+=0.25f;
									try 
									{
										Thread.sleep(100);
									} 
									catch (InterruptedException e) 
									{
										e.printStackTrace();
									}
								}
								for(int i=0;i<5;i++)
								{
									CUE_Y_ADJUST-=0.25f;
									try 
									{
										Thread.sleep(100);
									} 
									catch (InterruptedException e) 
									{
										e.printStackTrace();
									}
								}
								CUE_DH_FLAG=false;
								
								//記錄擊球前母球的位置
								MoveCameraThread.xFrom = ballAl.get(0).xOffset;
								MoveCameraThread.zFrom = ballAl.get(0).zOffset;
								//將力道條的目前力道給予值給球的速度
								vBall=MySurfaceView.this.rainbowBar.getCurrStrength();
    			        		ballAl.get(0).vx=(float)(-1*vBall*Math.sin(Math.toRadians(cue.getAngle())));//分解母球速度
    							ballAl.get(0).vz=(float)(-1*vBall*Math.cos(Math.toRadians(cue.getAngle())));
    							cueFlag=false;//打球標志位設為false//按下擊球鍵

    							if(activity.isSoundOn()){
        							activity.playSound(1, 0, 1);//播放開球音效    	
    							}    							
							}
						}.start();
		        	}
		        	
		        	
		        	//++++++++++++++++++++++++++++++逾時犯規用的程式碼begin++++++++++++++++++++++++++++++++++
		        	Constant.START_TIME=System.currentTimeMillis();//未擊球的開始時間
		        	//++++++++++++++++++++++++++++++逾時犯規用的程式碼end++++++++++++++++++++++++++++++++++
		        	
		        	//++++++++++++++++++++沒有擊到目的球的犯規事件程式碼begin++++++++++++++++++
		        	Constant.IS_FIGHT=false;	//在一次擊球事件中，是否與其他任何球發生碰撞的標志位
		        	//++++++++++++++++++++沒有擊到目的球的犯規事件程式碼end++++++++++++++++++++
		        	
		        	Constant.MAINBALL_FIRST_FIGHT = false;
		        	
		        	
		        	
             		moveFlag=false;//在虛擬按鈕位置按下，鎖定move事件
	        	}
        	}
        break;
        case MotionEvent.ACTION_MOVE:
        	if(CUE_DH_FLAG==true)
    		{//球桿擊打動畫播放中，不響應事件
    			break;
    		}
        	//若果搬移時觸控事件沒有發生在go按鈕上
        	if(!goBtn.isActionOnButton(x, y)){
        		goBtn.releaseUp();//釋放按鈕
        	}
        	//若果搬移時觸控事件沒有發生在右按鈕上
        	if(!rightBtn.isActionOnButton(x, y)){
        		rightBtn.releaseUp();//釋放按鈕
        	}
        	//若果搬移時觸控事件沒有發生在左按鈕上
        	if(!leftBtn.isActionOnButton(x, y)){
        		leftBtn.releaseUp();//釋放按鈕
        	}
        	//若果搬移時觸控事件沒有發生在m按鈕上
        	if(!mBtn.isActionOnButton(x, y)){
        		mBtn.releaseUp();//釋放按鈕
        	}
        	//若果觸控事件發生拉近按鈕上
        	if(!nearBtn.isActionOnButton(x, y)){
        		nearBtn.releaseUp();//釋放按鈕
        	}
        	//若果觸控事件發生在拉遠按鈕上
        	if(!farBtn.isActionOnButton(x, y)){
        		farBtn.releaseUp();//釋放按鈕
        	}
        	if(rainbowBar.isActionOnRainbowBar(x, y)){
        		rainbowBar.changePower(x, y);
        	}
        	//若果觸控事件發生在角度切換按鈕上，什麼也不做
        	else if(sightBtn.isActionOnButton(x, y)){
        		//什麼也不做
        	}
        	//若果在自由角度中按下圖示，什麼也不做
        	else if(iconBoard.isActionOnBoard(x, y) && currSight == Sight.free){
        		//什麼也不做
        	}
        	//按下按鈕時，將moveFlag設為false，這樣從按鈕上搬移出來也角度也不會轉
        	else if(moveFlag==true)
        	{
        		downFlag=false;//控制down事件可不可用
	            
	            float tmpAngdegAzimuth = -dx * TOUCH_SCALE_FACTOR_X;//設定沿y軸旋轉角度
	            float tmpAngdegElevation = -dy * TOUCH_SCALE_FACTOR_Y;//設定沿x軸旋轉角度

	            //若果為第一人稱角度，且球在運動中，不容許左右抹屏
	            if(currSight == Sight.first && cueFlag == false){
	            	break;
	            }
	            
	            //是否轉動攝影機的標志位：0不轉動，1左右轉動，2上下轉動
	            int rotateDirection = 0;
	            //若果直印向搬移多，並且搬移足夠多
	            if(Math.abs(dx)<=Math.abs(dy) && Math.abs(tmpAngdegElevation) > 0.34f){	            	
	            	rotateDirection = 2;
	            }
	            //若果水平搬移多，並且搬移足夠多
	            else if(Math.abs(dx)>Math.abs(dy) && Math.abs(tmpAngdegAzimuth) > 3f){
		            rotateDirection = 1;
		            //觸控位移大於設定值則進入搬移狀態
	            	isMove=true; 	            	
	            }
	            else{
	            	isMove = false;
	            	yAngleV = 0;
	            }
	            //水平轉動
	            if(rotateDirection == 1){
	 	            if(isMove)
	 	            {//若在搬移狀態則計算角度變化速度
	 	            	if(timeSpan!=0)
	 	            	{//防止分母為0產生無窮大的速度
	 	            		yAngleV=dx * TOUCH_SCALE_FACTOR_X/timeSpan;     
	 	            	}
	 	            } 
	 	            
	            }//直印轉動
	            else if(rotateDirection == 2){
		            angdegElevation += tmpAngdegElevation;//設定沿x軸旋轉角度		            
		            //若果改變90度還要改變攝影機的頭朝向，故不到90度
		            if(angdegElevation>89.99f){
		            	angdegElevation=89.99f;
		            }
		            //限制目前仰角不能小於最小仰角
		            else if(angdegElevation<currMinElevation){
		            	angdegElevation = currMinElevation;
		            }
		            //重新設定攝影機的位置
		            setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);
	            }
        	}
			break;
        case MotionEvent.ACTION_UP:
        	//釋放按鈕
        	goBtn.releaseUp();
        	rightBtn.releaseUp();
        	leftBtn.releaseUp();
        	mBtn.releaseUp();
        	nearBtn.releaseUp();
        	farBtn.releaseUp();
        	
    		moveFlag=true; 
        	downFlag=true;//控制
        	state=-1;//-1--表示不轉動
        	isMove=false;
        break;
        }

		mPreviousY = y;//記錄觸控筆位置
	    mPreviousX = x;//記錄觸控筆位置
        previousTime=currTime;//記錄此次時間戳
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		//注解掉的為測試刷框頻率的程式碼
//		long olds;
//    	long currs;
		boolean isFirstFrame=true;//是否是繪制的第一框
		
        public void onDrawFrame(GL10 gl) {
        	//注解掉的為測試刷框頻率的程式碼
//        	currs=System.nanoTime();			
//			System.out.println(1000000000.0/(currs-olds)+"FPS");
//			olds=currs;
        	if(!hasLoadOk)
        	{
            	//清除彩色快取於深度快取
            	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            	
            	//繪制歡迎界面
            	MatrixState.pushMatrix();
                MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);
                MatrixState.setCamera(0, 0, 4, 0, 0,3, 0, 1, 0); 
                
                //若果是15球模式
                if(Constant.POS_INDEX==0)
                {
                    MatrixState.pushMatrix();
                    wel.drawSelf(wel_8);//繪制歡迎界面
                    MatrixState.popMatrix();
                	
                }//若果是9球模式
                else
                {
                    MatrixState.pushMatrix();
                    wel.drawSelf(wel_9);//繪制歡迎界面
                    MatrixState.popMatrix();
                }
                MatrixState.popMatrix();
                
                //若果是第一框
                if(isFirstFrame)
                {
                	isFirstFrame=false;
                }//若果是第二框
                else
                { 	
                	
                	if(currProgress==0)
                	{
                		//載入進度指示器的點陣圖資源
                		initProgressBarBitmap();
                		//載入進度指示器的shader
                		initProgressBarShader();
                		//建立進度指示器的物件
                		initProgressBarObject();
                		
                	}
                	
                    //繪制進度指示器
                    drawProgressBar();
                	
                	currProgress++;//進度指示器的進度怎能更加
                	
                	if(currProgress==20)
                	{
                		//載入用到的所有點陣圖資源
                		initBitmap();
                	}
                	
                	if(currProgress==40)
                	{
    					//載入所有Shader
    					ShaderManager.loadShadersFromFile(MySurfaceView.this);
    					//編譯所有的shader
    					ShaderManager.compileShaders();
                	}
                	
                	if(currProgress==60)
                	{
    					//游戲實體建立物件
    					initLoading();
                	}
                	
                	if(currProgress==80)
                	{
                        //啟動桌球的運動執行緒
                        bgt=new BallGoThread(ballAl,MySurfaceView.this);
                        bgt.start();

            			moveFlag=true;//轉場結束後螢幕可觸控
            			downFlag=true;//轉場結束後虛擬鍵可觸控
            			//轉場結束後，設為為第一角度
            			setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);

                        key=new ThreadKey(MySurfaceView.this);
                        key.start();//啟動檢驗虛擬按鍵的執行緒    
                        
                		if(Constant.mode == GameMode.countDown){
                			timeRunningThread=new TimeRunningThread(MySurfaceView.this);//建立計時執行緒
                			timeRunningThread.start();
                		}
                        //每次進入游戲界面時，將攝影機的運動的執行緒清空
                        MoveCameraThread.currThread = null;
                        
                        
                        //++++++++++++++++++++++++++++++逾時犯規用的程式碼begin++++++++++++++++++++++++++++++++++
                        regTimeThread.start();
                      //++++++++++++++++++++++++++++++逾時犯規用的程式碼end++++++++++++++++++++++++++++++++++
                       
        	            
                	}
    	            if(currProgress>=100)
    	            {
            			if(activity.isBackGroundMusicOn())//開啟背景音樂
            			{
	            	    	activity.initBackGroundSound();
            				activity.mMediaPlayer.start();
            			}
    	            	hasLoadOk=true;	//載入結束，標志位設為true
    	            }
                }
	
        	}else
        	{
        		//清除彩色快取於深度快取
            	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            	
                //呼叫此方法計算產生透視投影矩陣
                MatrixState.setProjectFrustum(-Constant.RATIO*factor, Constant.RATIO*factor, -1*factor, 1*factor, 4, 250); 
            	
            	//在旋轉攝影機前，鎖定攝影機的旋轉角度，使球桿和攝影機的旋轉角度視覺上保持一致
            	cue.lockAngdegAzimuth(angdegAzimuth);
                //設定camera位置
            	MatrixState.setCamera
                (
                		cx,   //人眼位置的X
                		cy, 	//人眼位置的Y
                		cz,   //人眼位置的Z
                		tx, 	//人眼光看的點X
                		ty,   //人眼光看的點Y
                		tz,   //人眼光看的點Z
                		0, 	//up位置
                		1, 
                		0
                );
            	
            	
            	
                //繪制場景
            	MatrixState.pushMatrix();
            	MatrixState.translate(0, -5, 0);
                wall.drawSelf(wallId);//繪制牆面和地板
                MatrixState.popMatrix();
                
                //繪制桌子
                MatrixState.pushMatrix();
                drawTable.drawSelf
                (
                		tableBottomId,tableAreaId,tableRoundUDId,
                		tableRoundLRId,circleId,poolPartOneId,poolPartTwoId,
                		poolPartTwoJiaoId,poolPartOneMId,
                		greenId
                );
                MatrixState.popMatrix();
                
                //繪制球
                MatrixState.pushMatrix();//起始矩陣入堆疊
                MatrixState.rotate(-90, 0, 1, 0); 
                size=ballAl.size();//獲得球的個數
                
            	try 
                {
            		//複製球紋理和球的清單  
            		List<Integer> alBallTexIdTmp=new ArrayList<Integer>(alBallTexId);//桌球紋理ID
            		ArrayList<BallKongZhi> ballAlTmp=new ArrayList<BallKongZhi>(ballAl);;//桌球的清單
            		
            		//開啟混合
                    GLES20.glEnable(GLES20.GL_BLEND);  
                    //設定混合因子
                    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);            		
            		//繪制所有球本影
            		for(int i=0;i<size;i++)
                    {
            			ballAlTmp.get(i).lockState();
                    	ballAlTmp.get(i).drawSelf(alBallTexIdTmp.get(i), tableAreaHelpShadowId, 1);
                    }
                    //關閉混合
                    GLES20.glDisable(GLES20.GL_BLEND);
                    
            		//繪制所有球
                	for(int i=0;i<size;i++)
                    {
                    	ballAlTmp.get(i).drawSelf(alBallTexIdTmp.get(i), tableAreaHelpShadowId, 0);
                    }
                	
                }catch(Exception e)
                {
                	e.printStackTrace();
                }
            	MatrixState.popMatrix();//還原起始矩陣
            	
            	
            	// 繪制球桿
    			if (cueFlag && ballAl != null && ballAl.size() != 0) {
    				// 禁用深度測試
    				GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    				MatrixState.pushMatrix();
    				cue.drawSelf(ballAl.get(0), cueId, cueId, tableRoundUDId);
    				MatrixState.popMatrix();
    				GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    			}
                
                //**********************開始繪制小地圖******************************************
    			MatrixState.setProjectFrustum(-Constant.RATIO, Constant.RATIO, -1, 1, 4, 250);
                MatrixState.setCamera(0,0,0,0,0,-1,0,1,0);//攝影機還原到原點
                
                //禁用深度測試
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                GLES20.glEnable(GLES20.GL_BLEND);//開啟混合   
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);//設定源混合因子與目的混合因子
                                
                if(mFlag==true)//控制是否開啟小地圖
                {
    	            MatrixState.pushMatrix();
    	            MatrixState.translate(MINI_MAP_TRASLATE[MyActivity.screenId][0], MINI_MAP_TRASLATE[MyActivity.screenId][1], MINI_MAP_TRASLATE[MyActivity.screenId][2]);
    	            
    	            MatrixState.pushMatrix();
    	            poolMap.drawSelf(miniTableId);//繪制迷你地圖
    	            MatrixState.popMatrix();
    	            
    	            if(ballAl.size() != 0)//若清單中球的數量為0，則不繪制球
    	            {
    		            try
    		            {
    		            	if(miniBall)//控制是否繪制母球
    		            	{
    		            		MatrixState.pushMatrix();
    		            		ballTexture.drawSelf
    				            (
    				            		-ballAl.get(0).zOffset*MiniMapScale,
    				            		-ballAl.get(0).xOffset*MiniMapScale,
    				            		0.1f,
    				            		alBallMiniId.get(0)
    				            );//繪制目球
    		            		MatrixState.popMatrix();
    		            	}
    		            	
    		            	for(int i=1;i<ballAl.size();i++)
    		            	{
    		            		MatrixState.pushMatrix();//繪制迷你地圖上的球
    				            ballTexture.drawSelf
    				            (
    				            		-ballAl.get(i).zOffset*MiniMapScale,
    				            		-ballAl.get(i).xOffset*MiniMapScale,
    				            		0.1f,
    				            		alBallMiniId.get(i)
    				            );//繪制其它球
    				            MatrixState.popMatrix();
    		            	}
    			            if(cueFlag)
    			            {
    			            	MatrixState.pushMatrix();
    			            	MatrixState.translate(-ballAl.get(0).zOffset*MiniMapScale, -ballAl.get(0).xOffset*MiniMapScale, 0);
    			            	MatrixState.rotate(cue.getAngle(), 0, 0, 1);
    			            	
    			            	MatrixState.pushMatrix();
    			            	cueMini.drawSelf//繪制迷你球桿
    			            	(
    			            			-0.15f, 
    			            			0, 
    			            			0.1f, 
    			            			cueMiniId
    			            	);
    			            	MatrixState.popMatrix(); 
    			            	MatrixState.popMatrix();
    			            	
    			            	MatrixState.pushMatrix();
    			            	float[] cuePoint=NearPoint.getCuePoint//取得球桿座標
    			            	(-ballAl.get(0).zOffset*MiniMapScale, -ballAl.get(0).xOffset*MiniMapScale, -cue.getAngle());
    			            	float[] tempPoint=NearPoint.GetJiaoDian//取得目的點座標
            					(
            							-ballAl.get(0).zOffset*MiniMapScale,-ballAl.get(0).xOffset*MiniMapScale,
            							cuePoint[0],cuePoint[1]
            					);
    			            	line.drawSelf//繪制線
    			            	(
    			            			new float[]
    	            			          {
    			            					-ballAl.get(0).zOffset*MiniMapScale, -ballAl.get(0).xOffset*MiniMapScale, 0.1f,
    			            					tempPoint[0],tempPoint[1],0.1f
    	            			          }
    			            			
    			            	);
    			            	
    			            	MatrixState.popMatrix();
    			            }
    		            }catch(Exception e)
    		            {
    		            	e.printStackTrace();
    		            }
    	            }
    	            MatrixState.popMatrix();
                }
                //關閉混合
                GLES20.glDisable(GLES20.GL_BLEND);
                //啟用深度測試
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                //**********************結束繪制小地圖******************************************
                
                //================繪制自適應屏的儀表板======= begin ========
                //設定正交投影矩陣
                MatrixState.setProjectOrtho(-Constant.RATIO, Constant.RATIO, -1, 1, 1, 2);
                //呼叫此方法產生攝影機9參數位置矩陣
                MatrixState.setCamera(0,0,1f,0f,0f,0f,0f,1.0f,0.0f);
                //開啟混合
                GLES20.glEnable(GLES20.GL_BLEND);
                //設定混合因子
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                //禁用深度測試
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                
                //go按鈕
                MatrixState.pushMatrix();
                goBtn.drawSelf();          
                MatrixState.popMatrix(); 
                //右按鈕
                MatrixState.pushMatrix();
                rightBtn.drawSelf();          
                MatrixState.popMatrix(); 
                //左按鈕
                MatrixState.pushMatrix();
                leftBtn.drawSelf();          
                MatrixState.popMatrix(); 
                //m按鈕
                MatrixState.pushMatrix();
                mBtn.drawSelf();          
                MatrixState.popMatrix();             
                //icon儀表板
                MatrixState.pushMatrix();
                iconBoard.drawSelf();          
                MatrixState.popMatrix();            
                //力道條背景
                MatrixState.pushMatrix();
                barBoard.drawSelf();          
                MatrixState.popMatrix();
                //彩虹條
                MatrixState.pushMatrix();
                rainbowBar.drawSelf();          
                MatrixState.popMatrix();
                //得分
                MatrixState.pushMatrix();
                score.drawSelf();          
                MatrixState.popMatrix();
                //切換角度按鈕
                MatrixState.pushMatrix();
                sightBtn.drawSelf();          
                MatrixState.popMatrix();
                //拉近按鈕
                MatrixState.pushMatrix();
                nearBtn.drawSelf();          
                MatrixState.popMatrix();
                //拉遠按鈕
                MatrixState.pushMatrix();
                farBtn.drawSelf();          
                MatrixState.popMatrix();
                //倒計時按鈕
                if(Constant.mode == GameMode.countDown){
                    MatrixState.pushMatrix();
                    timer.drawSelf();          
                    MatrixState.popMatrix();            	
                }
                
                
                //關閉混合
                GLES20.glDisable(GLES20.GL_BLEND);
                //啟用深度測試
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                //================繪制自適應屏的儀表板======= end ==========  
                
                //還原原透視投影矩陣
                MatrixState.setProjectFrustum(-Constant.RATIO*factor, Constant.RATIO*factor, -1*factor, 1*factor, 4, 250);

        	}

        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
        	
        	//螢幕的長寬給予值
            Constant.SCREEN_WIDTH = width;
        	Constant.SCREEN_HEIGHT = height;        	 
        	//計算GLSurfaceView的長寬比
            Constant.RATIO = (float) width / height;
            
        	//起始化XY方向縮放比例
        	TOUCH_SCALE_FACTOR_X = 360f/width;
        	TOUCH_SCALE_FACTOR_Y = 90f/height;
            //設定視窗大小及位置 
        	GLES20.glViewport(0, 0, width, height);           
        }
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
            //設定螢幕背景色黑色RGBA
        	GLES20.glClearColor(0,0,0,1);            
            //啟用深度測試
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    		//設定為開啟背面剪裁
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //起始化變換矩陣
            MatrixState.setInitStack();
            //起始化光源位置
            MatrixState.setLightLocation(0, 10, 0);

           
            //載入背景的點陣圖資源
            wel_8=initTexture(R.drawable.wel_8);//15球的載入界面的背景
            wel_9=initTexture(R.drawable.wel_9);//9球的載入界面的背景
            //載入，載入界面的shader
            ShaderManager.loadWelcomeSurfaceShaderFromFile(MySurfaceView.this);
            ShaderManager.compileWelcomeSurfaceShader();
            //載入界面的矩形建立物件
    		wel = new TextureRect(2f, 2f, 0.781f, 0.936f); 
        }
    }
	
	//載入，進度指示器界面的點陣圖資源
	public void initProgressBarBitmap()
	{
		//起始化圖片id
        process_top=initTexture(R.drawable.process_top);
        process_bottom=initTexture(R.drawable.process_bottom);
        //起始化數字圖片資源
        num = new int[]{
        	initTexture(R.drawable.num0),
        	initTexture(R.drawable.num1),
        	initTexture(R.drawable.num2),
        	initTexture(R.drawable.num3),
        	initTexture(R.drawable.num4),
        	initTexture(R.drawable.num5),
        	initTexture(R.drawable.num6),
        	initTexture(R.drawable.num7),
        	initTexture(R.drawable.num8),
        	initTexture(R.drawable.num9),
        	initTexture(R.drawable.num10)
        };
	}
	//載入進度指示器界面的shader
	public void initProgressBarShader()
	{
        //載入，載入界面的shader
        ShaderManager.loadProgressBarShaderFromFile(MySurfaceView.this);
        ShaderManager.compileProgressBarShader();
	}
	//為進度指示器建立物件
	public void initProgressBarObject()
	{
        //建立虛擬按鈕物件 
        process=new ProgressBar(
        		MySurfaceView.this,
        		0.06f,0.6f,
        		0.1f, 		//高度  
        		1f, 1f //sEnd、tEnd
        		);
        //數字進度		
        perc = new Percentage(
        		MySurfaceView.this,
        		0.4f,0.61f,
        		0.04f, 0.08f, 		//寬度，高度  
        		1f, 1f //sEnd、tEnd
        );  
	}
	
	public void drawProgressBar()
	{
		//開啟混合
        GLES20.glEnable(GLES20.GL_BLEND);  
        //設定混合因子
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
        //繪制底面的圖片
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0f, 2.0f);
        process.drawSelf(process_bottom,100);         
        MatrixState.popMatrix(); 
        //繪制上面的圖片
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0f, 2.1f);
        process.drawSelf(process_top,currProgress);          
        MatrixState.popMatrix();
        
		//繪制進度指示器上方的數字
		String tempStr=currProgress+"";
		for(int i=0;i<tempStr.length();i++)   
		{   
			MatrixState.pushMatrix();
			MatrixState.translate((2*perc.width)*(i+1), 0f, 2.2f);
			perc.drawSelf(num[tempStr.charAt(i)-'0']); 
			MatrixState.popMatrix(); 
		}
		
		//繪制百分號
		MatrixState.pushMatrix();
		MatrixState.translate((tempStr.length()+1)*2*perc.width, 0f, 2.2f);
		perc.drawSelf(num[10]); 
		MatrixState.popMatrix(); 
		
        //關閉混合
        GLES20.glDisable(GLES20.GL_BLEND);
	}
	
	//起始化所有的圖片資源
	public void initBitmap()
	{
        cueId=initTexture(R.drawable.cue);//起始化球桿ID            
        //起始化紋理    ,將所有球的紋理ID放入一個清單中        
        ballTexId=initTexture(R.drawable.snooker0);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker1);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker2);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker3);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker4);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker5);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker6);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker7);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker8);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker9);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker10);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker11);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker12);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker13);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker14);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker15);
        alBallTexId.add(ballTexId);
        //起始化桌子 房間的紋理ID
		wallId=initTexture(R.drawable.wall);
        tableBottomId=initTexture(R.drawable.bottom);//桌底紋理ID
        tableAreaId=initTexture(R.drawable.area);//桌面紋理ID
        tableAreaHelpShadowId=initTexture(R.drawable.area_help_shadow);//為球的影子服務的桌面紋理
        tableRoundUDId=initTexture(R.drawable.round);//桌子上下邊緣ID
        tableRoundLRId=initTexture(R.drawable.roundlr);//桌子左右邊緣ID
        circleId=initTexture(R.drawable.circle);//球洞ID
		iconId=initTexture(R.drawable.icon0);
		numbersId=initTexture(R.drawable.numbers);
		////////////////////////////////////////////////
		//起始化迷你地圖紋理ID
		ballMiniId=initTexture(R.drawable.minimap0);//母球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap1);//1號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap2);//2號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap3);//3號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap4);//4號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap5);//5號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap6);//6號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap7);//7號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap8);//8號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap9);//9號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap10);//10號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap11);//11號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap12);//12號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap13);//13號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap14);//14號球
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap15);//15號球
		alBallMiniId.add(ballMiniId);
		
		cueMiniId=initTexture(R.drawable.cue);
		miniTableId=initTexture(R.drawable.minitable);//起始化球桌紋理
		////////////////////////起始化虛擬按鈕紋理
		leftId=initTexture(R.drawable.left);
		rightId=initTexture(R.drawable.right);
		mId=initTexture(R.drawable.m);
		goId=initTexture(R.drawable.space);
		poolPartOneId=initTexture(R.drawable.texture4);
		poolPartTwoId=initTexture(R.drawable.round);
		poolPartTwoJiaoId=initTexture(R.drawable.d1);
		poolPartOneMId=initTexture(R.drawable.d2);
		greenId = initTexture(R.drawable.green);
		barId=initTexture(R.drawable.bar);
		sightFirstId = initTexture(R.drawable.sight_first);
		sightFreeId = initTexture(R.drawable.sight_free);
		nearId = initTexture(R.drawable.near);
		farId = initTexture(R.drawable.far);
		breakId = initTexture(R.drawable.breaker);
		/////////////////////////////////////////////////////
	}
	//起始化紋理
	public int initTexture(int drawableId)
	{
		//產生紋理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //產生的紋理id的數量
				textures,   //紋理id的陣列
				0           //偏移量
		);    
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		//非Mipmap紋理取樣過濾參數	
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		
		//ST方向紋理伸展模式
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);		
        
        //透過輸入流載入圖片===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);        	
        }
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        } 
        //實際載入紋理,換成這個方法後，若果圖片格式有問題，會拋出圖片格式例外，不再會誤顯示其他例外
	   	GLUtils.texImage2D
	    (
	    		GLES20.GL_TEXTURE_2D, //紋理型態
	     		0, 
	     		GLUtils.getInternalFormat(bitmapTmp), 
	     		bitmapTmp, //紋理圖形
	     		GLUtils.getType(bitmapTmp), 
	     		0 //紋理邊框尺寸
	     );   
        //自動產生Mipmap紋理
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //釋放紋理圖
        bitmapTmp.recycle();
        //傳回紋理ID
        return textureId;
	}	
	
	//建立游戲元件物件
	public void initLoading()
	{
		cue=new Cue(MySurfaceView.this,1.0f);
		
		poolMap=new PoolMap(this);//迷你桌台
		
        ballTexture=new MiniMap//繪制迷你地圖上的球
 		(MySurfaceView.this,
 			1.0f*MiniMapScale,1.0f*MiniMapScale,
 			new float[]
		          {
	    				0,0,0,1,1,1,
	    	        	1,1,1,0,0,0
		          }
 		);
        
        cueMini=new MiniMap//繪制迷你球桿
        (MySurfaceView.this,
         		4.0f*MiniMapScale,0.25f*MiniMapScale,
     			new float[]
 		          {
 	    				0,0,0,1,1,1,
 	    	        	1,1,1,0,0,0
 		          }	
        );
        
        line=new MiniLine(this);//繪制線
        
	    drawTable=new ZhuoZi(this);//建立桌子物件
	    wall=new Qiang(this);//建立牆和底面
	    //建立用於繪制的球
	    btbv=new BallDingDian(MySurfaceView.this,BALL_SCALE);
	    
	    //建立用於控制的球，並加入清單
		for(int i=0;i<BallKongZhi.AllBallsPos[Constant.POS_INDEX].length;i++)
		{
			ballAl.add(
					new BallKongZhi(
							btbv,
							BallKongZhi.AllBallsPos[Constant.POS_INDEX][i][0],
							BallKongZhi.AllBallsPos[Constant.POS_INDEX][i][1],
							this,
							i
							)
					);
		}
		
		//建立自適應屏的儀表板
        initInstrumentBoards();
	}
	//起始化儀表板的方法
	void initInstrumentBoards(){
        //go按鈕 
        goBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		0f, 430f,
        		50f, 50f,
        		30,30,
        		1f, 1f, //sEnd、tEnd
        		goId, //upTexId、downTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //右按鈕
        rightBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		470f, 430f,//x2=800-x1-r
        		50f, 50f,
        		20,20,
        		1f, 1f, //sEnd、tEnd
        		rightId, //upTexId、downTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //左按鈕
        leftBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		280f, 430f,
        		50f, 50f,
        		20,20,
        		1f, 1f, //sEnd、tEnd
        		leftId, //upTexId、downTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );        
        //m按鈕
        mBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		600f, 430f,
        		50f, 50f,
        		40,40,
        		1f, 1f, //sEnd、tEnd
        		mId, //upTexId、downTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );   
        //建立icon儀表板 
        iconBoard=InstrumentBoardFactory.newBoardInstance(
        		MySurfaceView.this,
        		10f, 12f,
        		30f, 30f,
        		40, 40,
        		1f, 1f, //sEnd、tEnd
        		iconId, //upTexId、downTexId,
        		0,//不透明
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //建立彩虹條
        rainbowBar=InstrumentBoardFactory.newRainbowBarInstance(
        		MySurfaceView.this,
        		8f, 145f,
        		22f, 190f,
        		20, 30,
        		0.114678899f,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //力道條背景
        barBoard=InstrumentBoardFactory.newBoardInstance(
        		MySurfaceView.this,
        		4f, 140f,
        		30f, 200f,
        		0, 0,
        		0.448f, 1f, //sEnd、tEnd
        		barId, //upTexId、downTexId,
        		1,//透明
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //建立得分物件
        score=InstrumentBoardFactory.newScoreInstance(
        		MySurfaceView.this,
        		50f, 13f,
        		22f, 27f,
        		1f, 1f, //sEnd、tEnd
        		numbersId, //numbersId
        		true,
        		InstrumentBoardFactory.Gravity.Side
        ); 
        //切換角度的按鈕
        sightBtn=InstrumentBoardFactory.newButtonChangePicInstance(
        		MySurfaceView.this,
        		740f, 430f,
        		50f, 50f,
        		40f, 40f,
        		1f, 1f, //sEnd、tEnd
        		sightFirstId,sightFreeId, //upTexId、downTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //拉近按鈕
        nearBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		200f, 430f,
        		50f, 50f,
        		20,20,
        		1f, 1f, //sEnd、tEnd
        		nearId, //upTexId、downTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );  
        //拉遠按鈕
        farBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		130f, 430f,
        		50f, 50f,
        		20,20,
        		1f, 1f, //sEnd、tEnd
        		farId, //upTexId、downTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //建立得分物件
        timer=InstrumentBoardFactory.newTimerInstance(
        		MySurfaceView.this,
        		350f, 13f,
        		22f, 27f,
        		1f, 1f, //sEnd、tEnd
        		numbersId, //numbersId
        		breakId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        ); 
	}
	@Override
    public void onResume()
    {
    	super.onResume();//呼叫父類別方法
    	pauseFlag = false;  
    	Constant.cueFlag=true;//繪制球桿標志位
    	//重新建立並啟動所有執行緒
    	recreateAllThreads();
    }
	@Override
	public void onPause() {
		super.onPause();
		pauseFlag = true;
		//停止所有執行緒
		stopAllThreads();
	}

	//停止所有執行緒的方法
	void stopAllThreads()
	{
		bgt.flag = false;   
		key.stateFlag = false;
		if(timeRunningThread != null){
			timeRunningThread.setFlag(false);
		}
		regTimeThread.regTimeFlag = false;
		threadWork = false;
	}
	void recreateAllThreads(){
		bgt.flag = true;   
		key.stateFlag = true;
		if(timeRunningThread != null){
			timeRunningThread.setFlag(true);
		}
		regTimeThread.regTimeFlag = true;
		
		//啟動桌球的運動執行緒
        bgt=new BallGoThread(ballAl,MySurfaceView.this);
        bgt.start();
        //啟動檢驗虛擬按鍵的執行緒   
        key=new ThreadKey(MySurfaceView.this);
        key.start();
        //建立計時執行緒
		if(Constant.mode == GameMode.countDown){
			timeRunningThread=new TimeRunningThread(MySurfaceView.this);
			timeRunningThread.start();
		}
		regTimeThread.start();
	}
	//結束游戲，並跳躍到對應界面的方法。該方法只能在游戲結束，需要記分時呼叫
	public void overGame(){
		stopAllThreads();
		//停止背景音樂
        if(activity.mMediaPlayer.isPlaying()){
        	activity.mMediaPlayer.stop();
        }
        
        //若果是倒計時模式
        if(mode == GameMode.countDown){
        	//將總得分給予值給activity中的score
        	activity.currScore = timer.getLeftSecond();
    		activity.sendMessage(WhatMessage.OVER_GAME);
        } 
        //若果是練習模式
        else{
			//顯示贏得此局的交談視窗
			this.bgt.sendHandlerMessage(Constant.EXIT_SYSTEM_WIN);
        }
	}
}

