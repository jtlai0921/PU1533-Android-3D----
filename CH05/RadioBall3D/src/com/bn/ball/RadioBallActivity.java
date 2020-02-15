package com.bn.ball;

import java.util.HashMap;

import com.bn.ball.jiemian.Constant;
import com.bn.ball.jiemian.HelpView;
import com.bn.ball.jiemian.HistoryView;
import com.bn.ball.jiemian.MenuView;
import com.bn.ball.jiemian.SoundView;
import com.bn.ball.jiemian.ViewForDraw;
import com.bn.ball.jiemian.WelcomeView;
import com.bn.ball.util.SQLiteUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
enum WhichView
{
	WELCOME_VIEW,MENU_VIEW,SOUND_VIEW,HISTORY_VIEW,HELP_VIEW,GAME_VIEW
};
public class RadioBallActivity extends Activity {	
	static WhichView curr;
	MenuView menuView;
	public Handler hd;//訊息控制器
	WelcomeView welV;//歡迎過場動畫界面
	MenuView menuV;//選單界面的參考	
	HistoryView hisView;
	SoundView soundView;
	HelpView helpView;
	ViewForDraw vfd;
	MySurfaceView gameView;
	 SoundPool shengyinchi;//音效池
	 HashMap<Integer,Integer> soundId;//音效id 
	 public MediaPlayer beijingyinyue; //背景音樂播放器
	 
	 public  SharedPreferences sp;
	 public boolean yinXiao=true;//游戲音效標志位
	 
	 
	private MySurfaceView mGLSurfaceView;
	//SensorManager物件參考
	public SensorManager mySensorManager;	
	public Sensor myGravity; 	//感知器型態	
	
	//開發實現了SensorEventListener接口的感知器監聽器
	private SensorEventListener mySensorListener = new SensorEventListener(){
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
		public void onSensorChanged(SensorEvent event) {
			//計算出重力在螢幕上的投影方向
			float[] values=event.values;
			float length=(float) Math.sqrt(values[0]*values[0]+values[1]*values[1]+values[2]*values[2]);
			if(com.bn.ball.Constant.flag)
			{
				com.bn.ball.Constant.cUpY=-values[1]/length;
			}
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mGLSurfaceView = new MySurfaceView(this);
        mGLSurfaceView.requestFocus();//取得焦點
        mGLSurfaceView.setFocusableInTouchMode(true);
		//獲得SensorManager物件
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);	        
        myGravity=mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.SCREEN_WIDTH=dm.widthPixels;			//dm.widthPixels    取得螢幕水平解析度
        Constant.SCREEN_HEIGHT=dm.heightPixels;
        sp= this.getSharedPreferences("sysz", Context.MODE_PRIVATE);
		 yinXiao=sp.getBoolean("youxiyinxiao", true);
		 Constant.ScaleSR();
		 SQLiteUtil.initDatabase();//建立資料庫
			chuangJianSound();//建立背景音樂
			playBeiJingYinYue();//播放背景音樂
        hd=new Handler()
        {
			@Override
			public void handleMessage(Message msg)
			{
        		switch(msg.what)
        		{
	        		case 0://去歡迎界面
	        			gotoWelcomeView();
	        		break;
	        		case 1://去主選單界面
	        			gotoMenuView();
	        		break;
	        		case 2://去設定界面
	        			gotoSoundView();
	        		break;
	        		case 3://去歷史界面
	        			gotoHistoryView();
	        		break;	
	        		case 4:
	        			gotoHelpView();
	        		break;	
	        		case 5:
	        			gotoGameView();
	        			break;
        		}
			}
        };
        hd.sendEmptyMessage(0);    
    }
    public void gotoGameView(){
    	vfd.flag=false;
    	com.bn.ball.Constant.flag=false;
    	com.bn.ball.Constant.bolipzFlag=false;
    	com.bn.ball.Constant.winFlag=false;
    	com.bn.ball.Constant.loseFlag=false;
    	com.bn.ball.Constant.daojuFlag=false;
    	com.bn.ball.Constant.sumBoardScore=0;
    	com.bn.ball.Constant.sumEfectScore=0;
    	com.bn.ball.Constant.sumLoadScore=0;
    	com.bn.ball.Constant.gameOver=true;
    	com.bn.ball.Constant.vk=1;
    	com.bn.ball.Constant.vz=-0.2f;
    	com.bn.ball.Constant.pengCount=1.2f;
    	 yinXiao=sp.getBoolean("youxiyinxiao", true);
    	gameView=new MySurfaceView(RadioBallActivity.this);
    	setContentView(gameView);  
    	curr=WhichView.GAME_VIEW;

    }
    public void gotoSoundView()
    {
    	soundView=new SoundView(RadioBallActivity.this);
    	vfd.curr=soundView;
    	curr=WhichView.SOUND_VIEW;
    }
    public void gotoHistoryView()
    {
    	hisView=new HistoryView(RadioBallActivity.this);
    	vfd.curr=hisView;
    	curr=WhichView.HISTORY_VIEW;
    }
    public void gotoHelpView()
    {
    	helpView=new HelpView(RadioBallActivity.this);
    	vfd.curr=helpView;
    	curr=WhichView.HELP_VIEW;
    }
	public  void gotoMenuView() 
	{
    	com.bn.ball.Constant.flag=false;
    	com.bn.ball.Constant.bolipzFlag=false;
    	if(vfd==null)
    	{    		
    		vfd=new ViewForDraw(this);    		   		
    	}
    	if(!vfd.flag)
    	{
    		vfd.initThread();
    		setContentView(vfd);  
    	}
    	menuV=new MenuView(RadioBallActivity.this);
    	vfd.curr=menuV;
		curr=WhichView.MENU_VIEW;
	}
    
    public  void gotoWelcomeView() {
		
    	welV=new WelcomeView(RadioBallActivity.this);
		setContentView(welV);
		curr=WhichView.WELCOME_VIEW;
	}
	@Override
    public boolean onKeyDown(int keyCode,KeyEvent e)
    {
    	if(keyCode!=4)                                 //若果不是傳回鍵則不做處理
    	{
    		return false; 
    	}
       	if(keyCode==4)
    	{
    		if(curr==WhichView.MENU_VIEW)           //離開游戲
    		{
    			System.exit(0);
    		}else if(curr==WhichView.SOUND_VIEW)    //離開游戲
    		{
    			hd.sendEmptyMessage(1);
    		}else if(curr==WhichView.HELP_VIEW)
    		{
    			hd.sendEmptyMessage(1);
    		}else if(curr==WhichView.HISTORY_VIEW)
    		{
    			hd.sendEmptyMessage(1);
    		}else if(curr==WhichView.GAME_VIEW)
    		{
    			hd.sendEmptyMessage(1);
    		}else if(curr==WhichView.WELCOME_VIEW)
    		{
    			System.exit(0);
    		} 
    	}	
    	return true;
    }
   
    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(			//登錄監聽器
				mySensorListener, 					//監聽器物件
				myGravity,							//感知器型態
				SensorManager.SENSOR_DELAY_UI		//感知器事件傳遞的頻度
				);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener(mySensorListener);	//取消登錄監聽器
   
    }     
    
    public void chuangJianSound()   //建立音效池的方法
    {
    	shengyinchi=new SoundPool(   //建立音效池
    			6,					 //同時播放流的最大數量
    			AudioManager.STREAM_MUSIC,  //流的型態
        		100
    			);
    	soundId=new HashMap<Integer,Integer>();//建立hashmap
    	soundId.put(1, shengyinchi.load(this, R.raw.crash, 1));//載入音效並新增到hashmap中
    	soundId.put(2, shengyinchi.load(this, R.raw.crash2, 1));
    }
    public void playSound(int sound,int loop)//播放音效的方法
    {
    	//得到AudioManager案例物件
    		AudioManager amg=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    		//獲得目前音量
        	float streamVolumeCurrent=amg.getStreamVolume(AudioManager.STREAM_MUSIC);
        	//獲得最大音量
        	float streamVolumeMax=amg.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        	float volume=streamVolumeCurrent/streamVolumeMax;
        	//播放音效  
        	shengyinchi.play(soundId.get(sound), volume, volume, 1, loop, 1f);  	    	
    }
    public void playBeiJingYinYue(){
    	
    	beijingyinyue=MediaPlayer.create(this,R.raw.backsound);
    	beijingyinyue.setLooping(true);
    	beijingyinyue.start();
    }
}



