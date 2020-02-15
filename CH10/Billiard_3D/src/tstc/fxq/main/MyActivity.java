package tstc.fxq.main;
import static tstc.fxq.constants.Constant.pauseFlag;
import static tstc.fxq.constants.Constant.screenRatio800x480;
import static tstc.fxq.constants.Constant.screenRatio854x480;

import java.util.HashMap;

import tstc.fxq.constants.Constant;
import tstc.fxq.constants.WhatMessage;
import tstc.fxq.utils.DBUtil;
import tstc.fxq.utils.DateUtil;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class MyActivity extends Activity { 
	
	
	public final int EXIT_SYSTEM9 = 1;	//9球犯規離開軟體交談視窗
	public final int EXIT_SYSTEM8 = 2;	//8球犯規離開軟體交談視窗
	public final int EXIT_WIN = 3;	//贏得比賽的交談視窗
	public final int EXIT_TIME_UP = 4;	//時間到交談視窗
	public final int EXIT_BREAK_RECORD = 5;	//時間到交談視窗
	final int DIALOG_ONE = 6;
	final int DIALOG_TWO = 7;
	
	//目前界面的標示
	int currView;
	//各界面的參考 
	WelcomeView welcomeView;//歡迎界面
	MainMenuView mainMenuView;//主界面
	MySurfaceView gameView;   //游戲界面
	SoundControlView soundControlView;//音效控制界面
	HelpView helpView;//幫助界面
	AboutView aboutView;//關於界面
	ChoiceView choiceView;//選取玩法界面
	ModeView modeView;//模式選取界面
	HighScoreView highScoreView;//排行榜界面

	CheckVersionDialog cvDialog;
	AndroidVersionDialog avDialog;
	//關於資料庫的變數
    int currScore;//游戲結束後的得分
	int highestScore;
    //音樂播放器
	MediaPlayer mMediaPlayer;	
	SoundPool soundPool;//音樂池
	HashMap<Integer,Integer> soundPoolMap;//音效池中音效ID與自訂音效ID的Map
	
	private boolean backGroundMusicOn=false;//背景音樂是否開啟的標志
	private boolean soundOn=true;//音效是否開啟的標志
	//關於螢幕尺寸的資源
	static float screenHeight;//螢幕高度
	static float screenWidth;//螢幕寬度
	static float screenPictureWidth=480;
	static float screenRatio;//螢幕長寬比
	static int screenId;//螢幕Id
	//處理各個SurfaceView傳送的訊息
    Handler myHandler = new Handler(){
        public void handleMessage(Message msg) {
        	switch(msg.what)
        	{
        	case WhatMessage.MAIN_MENU_VIEW:
        		gotoMainMenuView();
        		break;
        	case WhatMessage.GAME_VIEW:
        		gotoGameView();
        		break;
        	case WhatMessage.SOUND_CONTORL_VIEW:
        		gotoSoundControlView();
        		break;
        	case WhatMessage.HIGH_SCORE_VIEW:
        		gotoHighScoreView();
        		break;
        	case WhatMessage.WELLCOME_VIEW:
        		gotoWellcomeView();
        		break;
        	case WhatMessage.HELP_VIEW:
        		gotoHelpView();
        		break;
        	case WhatMessage.ABOUT_VIEW:
        		gotoAboutView();
        		break;
        	case WhatMessage.CHOICE_VIEW:
        		gotoChoiceView();
        		break;
        	case WhatMessage.OVER_GAME:
        		goToOverView();
        		break;
    		case WhatMessage.MODE_VIEW:
    			gotoModeView();
    		break;
        	}
        }
	};
	
	//顯示toast和dialoag的handler
	public Handler hd=new Handler()
    {
    	@Override
    	public void handleMessage(Message msg)
    	{
    		//呼叫父類別處理
    		super.handleMessage(msg);
    		//根據訊息what編號的不同，執行不同的業務邏輯
    		switch(msg.what)
    		{
    		   //將訊息中的內容分析出來顯示在Toast中
    		   case Constant.OVERTIMETOAST :
    			   //顯示Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "犯規：擊球逾時！", 
               		  Toast.LENGTH_SHORT
               	   ).show();
    		   break;
    		   
    		   //15秒後未擊球，將按擊球逾時犯規處理
    		   case Constant.REMINDPLAYER :
    			   //顯示Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "提示：15秒內未擊球，將擊球逾時犯規！", 
               		  Toast.LENGTH_SHORT
               	   ).show();
    		   break;
    		   
    		   //將訊息中的內容分析出來顯示在Toast中
    		   case Constant.MAINBALL_FLOP :
    			   //顯示Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "犯規：主球落袋！", 
               		  Toast.LENGTH_SHORT
               	   ).show();
    		   break;
      		   case Constant.NO_FIGHT :
    			   //顯示Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "犯規：主球未與任何球發生碰撞！", 
               		  Toast.LENGTH_SHORT
               	   ).show();
    		   break;
      		 case Constant.NO_FIGHT_AND_FLOP :
  			   //顯示Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "犯規：主球進洞！\n犯規：主球未與任何球發生碰撞！", 
               		  Toast.LENGTH_SHORT
               	   ).show();
        		   break;
      		 case Constant.MAINBALLFLOP_AND_NOTFIGHTTARGET :
    			   //顯示Toast
	        		   Toast.makeText
	               	   (
	               			MyActivity.this, 
	               		  "犯規：主球進洞！\n犯規：擊打的不是目的球！", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
	        		   break;
      		 case Constant.NO_FIGHT_TARGET :
    			   //顯示Toast
	        		   Toast.makeText
	               	   (
	               			MyActivity.this, 
	               		  "犯規：擊打的不是目的球！", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
	        		   break;
      		 case Constant.EXIT_SYSTEM_LOSE9 :
      			 
      			MyActivity.this.showDialog(EXIT_SYSTEM9);//若果9號球進洞
      			
        		   break;
      		 case Constant.EXIT_SYSTEM_LOSE8 :
      			MyActivity.this.showDialog(EXIT_SYSTEM8);//若果9號球進洞
	        		   break;
      		 case Constant.EXIT_SYSTEM_WIN :          			
      			MyActivity.this.showDialog(EXIT_WIN);//若果9號球進洞          			
        		   break;
      		 case Constant.TIME_UP :          			
      			MyActivity.this.showDialog(EXIT_TIME_UP);//若果9號球進洞          			
        		   break;
      		 case Constant.BREAK_RECORD :          			
      			MyActivity.this.showDialog(EXIT_BREAK_RECORD);//若果9號球進洞          			
        		   break;
    		}
    	}
    };        		
	
	
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //設定為橫屏
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //設定全螢幕
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        					WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //游戲過程中只容許調整多媒體音量，而不容許調整通話音量
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //+++++++++++++++++++取得螢幕的解析度,判定螢幕比例begin+++++++++++++++++++++++++++++++++++++
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        screenHeight=dm.heightPixels;
        screenWidth=dm.widthPixels;
        
        float screenHeightTemp=screenHeight;//記錄系統傳回的螢幕解析度。
        float screenWidthTemp=screenWidth;
        
        if(screenHeightTemp>screenWidthTemp) //指定螢幕的寬和高。
        {
        	screenWidth=screenHeightTemp;
        	screenHeight=screenWidthTemp;
        } 
        screenRatio=screenWidth/screenHeight;//取得螢幕的長寬比
        
        if(Math.abs(screenRatio-screenRatio800x480)<0.01f)
        {
        	screenId=2;
        }
        else if(Math.abs(screenRatio-screenRatio854x480)<0.01f)
        {
        	screenId=1;
       	}
        else
        {
        	screenId=0;
        }
        
        //起始化常數
        Constant.initConst(dm.widthPixels, dm.heightPixels);  
         //起始化音效
        initBackGroundSound();
        initSoundPool();
      //+++++++++++++++++++取得螢幕的解析度end+++++++++++++++++++++++++++++++++++++
        
		gotoWellcomeView();//去歡迎界面   
   }
	
	

	@Override
	public Dialog onCreateDialog(int id)
	{
		Dialog dialog=null;
		switch(id)
		{
		case EXIT_SYSTEM9://離開軟體交談視窗
		case EXIT_SYSTEM8://離開軟體交談視窗
		case EXIT_WIN://離開軟體交談視窗
		case EXIT_TIME_UP://離開軟體交談視窗
		case EXIT_BREAK_RECORD://離開軟體交談視窗
			Builder b=new AlertDialog.Builder(this); 
			b.setItems(null, null);
			b.setCancelable(false);//點擊傳回鍵不可以取消
			dialog=b.create();
			break;
		case DIALOG_ONE:
    		cvDialog=new CheckVersionDialog(this);
		dialog=cvDialog;
    		break;
    	case DIALOG_TWO:
    		avDialog=new AndroidVersionDialog(this);
    		dialog=avDialog;
    		break;
		}
		return dialog;
	}
    //每次出現交談視窗時被回調以動態更新交談視窗內容的方法
    @Override
    public void onPrepareDialog(int id, final Dialog dialog)
    {
    	//出現交談視窗前，停止必要資源
		if(gameView != null){
			gameView.stopAllThreads();
			//停止背景音樂
	        if(mMediaPlayer.isPlaying()){
	        	mMediaPlayer.stop();
	        }
		}
    	//若不是等待交談視窗則傳回
    	switch(id)
    	{
 	   case EXIT_SYSTEM9://離開交談視窗
 		   initDialog(dialog,R.string.lose9);//9球進洞犯規的dialog 		   
		   break;
 	   case EXIT_SYSTEM8://離開交談視窗
 		   initDialog(dialog,R.string.lose8);////8球進洞犯規的dialog
		   break;
 	   case EXIT_WIN://離開交談視窗
 		  initDialog(dialog,R.string.win);//贏得比賽的交談視窗
		   break;
 	   case EXIT_TIME_UP://離開交談視窗
 		  initDialog(dialog,R.string.time_up);//贏得比賽的交談視窗
		   break;
 	  case EXIT_BREAK_RECORD://離開交談視窗
 		  initDialog(dialog,R.string.break_record);//贏得比賽的交談視窗
		   break;

	  case DIALOG_ONE:
		   Button bok=(Button)cvDialog.findViewById(R.id.ok_button);
		   bok.setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						System.exit(0);
					}	
				}
		   );
	  break;
	  case DIALOG_TWO:
		  Button ok=(Button)avDialog.findViewById(R.id.ok);
		   ok.setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						System.exit(0);
					}	
				}
		   );
		   break;
    	}
    }
    
    public void initDialog(final Dialog dialog,int StringId)
    {
    	//交談視窗對應的總垂直方向LinearLayout
	   	LinearLayout lle=new LinearLayout(MyActivity.this);
		lle.setOrientation(LinearLayout.VERTICAL);		//設定朝向	
		lle.setGravity(Gravity.CENTER_HORIZONTAL);
		lle.setBackgroundResource(R.drawable.dialog);
		
		//標題行的水平LinearLayout
		LinearLayout llt1=new LinearLayout(MyActivity.this);
		llt1.setOrientation(LinearLayout.HORIZONTAL);		//設定朝向	
		llt1.setGravity(Gravity.LEFT);//置中
		llt1.setLayoutParams(new ViewGroup.LayoutParams(350, LayoutParams.WRAP_CONTENT));
		
		//標題行的文字
		TextView title1=new TextView(MyActivity.this);
		title1.setText(R.string.game_over);
		title1.setTextSize(15);//設定字型大小
		title1.setTextColor(MyActivity .this.getResources().getColor(R.color.black));//設定字型彩色
		llt1.addView(title1);
		
		//將標題行新增到總LinearLayout
		lle.addView(llt1);
		
		
		//標題行的水平LinearLayout
		LinearLayout llt2=new LinearLayout(MyActivity.this);
		llt2.setOrientation(LinearLayout.HORIZONTAL);		//設定朝向	
		llt2.setGravity(Gravity.CENTER);//置中
		llt2.setLayoutParams(new ViewGroup.LayoutParams(350, LayoutParams.WRAP_CONTENT));
		
		//標題行的文字
		TextView title2=new TextView(MyActivity.this);
		title2.setText(StringId);
		title2.setTextSize(15);//設定字型大小
		title2.setTextColor(MyActivity .this.getResources().getColor(R.color.black));//設定字型彩色
		llt2.addView(title2);
		
		//將標題行新增到總LinearLayout
		lle.addView(llt2);
		

		LinearLayout lleb=new LinearLayout(MyActivity.this);
		lleb.setOrientation(LinearLayout.HORIZONTAL);//水平方向
		lleb.setLayoutParams(new ViewGroup.LayoutParams(350, LayoutParams.WRAP_CONTENT));
		lleb.setGravity(Gravity.CENTER);
		
		
		
		final ImageButton iok = new ImageButton(MyActivity.this);
		iok.setImageResource(R.drawable.again);
		iok.setMinimumHeight(20);
		iok.setMinimumWidth(150);
		iok.setBackgroundColor(0);
		iok.setOnTouchListener(
			new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) {
						iok.setAlpha(80);
		            }else if(event.getAction() == MotionEvent.ACTION_UP) {
		            	iok.setAlpha(255);
		            	//取消交談視窗
						dialog.cancel();
						//傳送去游戲界面的訊息
		    			sendMessage(WhatMessage.GAME_VIEW);
		            }  
					return false;
				}
			}	
		);
		
		
		lleb.addView(iok);
		
		final ImageButton icancel = new ImageButton(MyActivity.this);
		icancel.setImageResource(R.drawable.exit);
		icancel.setMinimumHeight(20);
		icancel.setMinimumWidth(150);
		icancel.setBackgroundColor(0);
		icancel.setOnTouchListener(
			new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) {
						icancel.setAlpha(80);
						
		            }else if(event.getAction() == MotionEvent.ACTION_UP) {
		            	icancel.setAlpha(255);
		            	System.exit(0);	
						dialog.cancel();//取消交談視窗
		            }  
					return false;
				}
			}	
		);
		lleb.addView(icancel);
   	    lle.addView(lleb);
   	    dialog.setContentView(lle);
    }
	
    public void initBackGroundSound() 
    {
    	mMediaPlayer=MediaPlayer.create(this,R.raw.backsound);//建立背景音樂
    	mMediaPlayer.setLooping(true);//設定為循環
	}
    public void initSoundPool(){
    	//音樂池 
    	soundPool=new SoundPool(10,AudioManager.STREAM_MUSIC,100);
    	soundPoolMap=new HashMap<Integer,Integer>();
    	//開局的音樂
    	soundPoolMap.put(1,soundPool.load(this,R.raw.start,1));
    	//球球碰撞的音效
    	soundPoolMap.put(2, soundPool.load(this,R.raw.hit,1));
    	//球壁碰撞,球進洞的音效
    	soundPoolMap.put(3,soundPool.load(this,R.raw.ballin,1));
    }
	public void playSound(int sound, int loop, float ratio) 
    {
	    if(pauseFlag){return;}
		AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;  
	    //控制音效的大小
	    volume *= ratio;
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 0.5f);
    }
	@Override
    public void onResume()
    {
    	super.onResume();//呼叫父類別方法
    	pauseFlag=false;    	
    }
	@Override
    public void onPause()
    {
		pauseFlag=true;		
    	super.onPause();
    } 
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent e)//重新定義鍵按下方法
	{
			switch(keyCode)
	    	{
	    	    case 4://傳回鍵
			    	switch(currView)
			    	{
			    	case WhatMessage.WELLCOME_VIEW:
			    	break;
			    	case WhatMessage.MAIN_MENU_VIEW:
			    		System.exit(0);
			    	break;
			    	case WhatMessage.MODE_VIEW:
			    		gotoChoiceView();
			    	break;
			    	case WhatMessage.HIGH_SCORE_VIEW:
			    		gotoModeView();
			    	break;
			    	case WhatMessage.GAME_VIEW:
			    		//按傳回鍵離開時，要確保所有的執行緒已經停止
			    		//資源載入完成後才可以點擊傳回鍵
			    		if(gameView.hasLoadOk)
			    		{
			    			if(gameView != null){
			    				gameView.stopAllThreads();
			    				//停止背景音樂
			    				if(mMediaPlayer.isPlaying()){
			    					mMediaPlayer.stop();
			    				}
			    			}
			    			gotoModeView();
			    		}
	
			    	break;
			    	case WhatMessage.SOUND_CONTORL_VIEW:
			    	case WhatMessage.HELP_VIEW:
			    	case WhatMessage.ABOUT_VIEW:
			    	case WhatMessage.CHOICE_VIEW:
			    		gotoMainMenuView();
			    	break;
			    	}
			    return true;	    	
	    	}
		return false;
    }
    //向Handler傳送訊息的方法
    public void sendMessage(int what)
    {
    	Message msg1 = myHandler.obtainMessage(what); 
    	myHandler.sendMessage(msg1);
    }
    
    
    public boolean isBackGroundMusicOn() {
		return backGroundMusicOn;
	}
	public void setBackGroundMusicOn(boolean backGroundMusicOn) {
		this.backGroundMusicOn = backGroundMusicOn;
	}
	
	public boolean isSoundOn() {
		return soundOn;
	}
	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}

    //去歡迎界面的方法
    private void gotoWellcomeView()
    {
    	if(welcomeView==null)
    	{
    		 welcomeView = new WelcomeView(this);
    	}
    	this.setContentView(welcomeView);
    	currView=WhatMessage.WELLCOME_VIEW;
    } 
    
    //去主選單界面的方法
    private void gotoMainMenuView()
    {
    	//若果是動畫播放完畢後進入的主選單界面，先判斷版本是否正確
    	if(currView==WhatMessage.WELLCOME_VIEW && !judgeVersion()){
    		return;
    	}
    	//若果游戲正在進行中離開了游戲，先停止游戲中所有執行緒
    	if(gameView!=null){
    		gameView.stopAllThreads();

            //停止背景音樂
            if(mMediaPlayer.isPlaying()){
            	mMediaPlayer.stop();
            }
            //將gameView參考清空，以便再次進入游戲時重新建立該物件
    		gameView = null;
    	}
    	if(mainMenuView==null)
    	{
    		mainMenuView = new MainMenuView(this);
    	}
    	this.setContentView(mainMenuView);
    	currView=WhatMessage.MAIN_MENU_VIEW;
    }
    
    //去設定音效界面的方法
    private void gotoSoundControlView()
    {
    	if(soundControlView==null)
    	{
    		soundControlView = new SoundControlView(this);
    	}
    	this.setContentView(soundControlView);
    	currView=WhatMessage.SOUND_CONTORL_VIEW;
    }
    //去游戲界面
    private void gotoGameView()
    {
    	gameView = new MySurfaceView(this);
    	this.setContentView(gameView);
    	currView = WhatMessage.GAME_VIEW;
    }
    //去幫助界面的方法
    private void gotoHelpView()
    {
    	if(helpView==null)
    	{
    		helpView = new HelpView(this);
    	}
    	this.setContentView(helpView);
    	currView=WhatMessage.HELP_VIEW;
    }
    //去關於界面的方法
    private void gotoAboutView()
    {
    	if(aboutView==null)
    	{
    		aboutView = new AboutView(this);
    	}
    	this.setContentView(aboutView);
    	currView=WhatMessage.ABOUT_VIEW;
    }
    //去選項界面的方法
    private void gotoChoiceView()
    {
    	if(choiceView==null)
    	{
    		choiceView=new ChoiceView(this);
    	}
    	this.setContentView(choiceView);
    	currView=WhatMessage.CHOICE_VIEW;
    } 
    //去選項界面的方法
    private void gotoModeView()
    {
    	if(modeView==null)
    	{
    		modeView=new ModeView(this);
    	}
    	this.setContentView(modeView);
    	currView=WhatMessage.MODE_VIEW;
    }
    //去積分榜界面的方法
    private void gotoHighScoreView()
    {
    	if(highScoreView==null)
    	{
    		highScoreView = new HighScoreView(this);
    	}
    	this.setContentView(highScoreView);
    	currView=WhatMessage.HIGH_SCORE_VIEW;
    }
    
    //將得分和時間插入資料庫，並跳躍到對應的結束界面
    private void goToOverView()
    {
    	//取得最高分
    	highestScore = DBUtil.getHighestScore(Constant.POS_INDEX);
    	//獲得目前分數和日期並插入資料庫
    	DBUtil.insert(Constant.POS_INDEX, currScore, DateUtil.getCurrentDate());
    	
    	//若果目前得分大於積分榜中最高分，進入勝利的界面
		if(currScore>highestScore){
			//顯示離開程式的dialog
			this.gameView.bgt.sendHandlerMessage(Constant.BREAK_RECORD);
			
		}
		//若果目前得分不大於積分榜中最高分
		else if(currScore > 0){
			//顯示贏得此局的交談視窗
			this.gameView.bgt.sendHandlerMessage(Constant.EXIT_SYSTEM_WIN);
		}
    	
    }
    
    //判斷版本的方法，若果版本不正確傳回false，否則傳回true
    boolean judgeVersion(){
	    //判斷目前系統所支援的最高opengles版本是不是大於2
	    if(this.getGLVersion() < 2)  
	    {
	    	this.showDialog(DIALOG_ONE);
	    	return false;
	    }
    	//判斷目前Android版本是不是低於2.2
	    else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
	    {
	    	this.showDialog(DIALOG_TWO);
	    	return false;
	    }
	    return true;
    }
    //取得OPENGLES所支援的最高版本
    public int getGLVersion() 
    {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        int majorVersion=info.reqGlEsVersion;
        majorVersion=majorVersion>>>16;
        return majorVersion;
    }
}