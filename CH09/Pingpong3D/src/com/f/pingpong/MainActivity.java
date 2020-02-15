package com.f.pingpong;

import static com.f.pingpong.Constant.*;
import static com.f.pingpong.Constant.DifficultyContorl.DIFFICULTY;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.f.util.SoundAndShakeUtil;
import com.f.video.FrameData;
import com.f.view.GameSurfaceView;
import com.f.view.MainMenuSurfaceView;

enum WhichView{GAME_VIEW,MAINMENU_VIEW};//列舉型態，游戲界面、主選單界面
public class MainActivity extends Activity //PingpongMAX_4\src\com\f\pingpong
{
	private AudioManager audio;//游戲中控制音量工具物件
	private WhichView curr;//記錄目前界面
	private MainMenuSurfaceView mmsv;
	private GameSurfaceView gsv;
	private int count = 0;//兩次按下傳回則離開

	public SoundAndShakeUtil soundandshakeutil;
	public Vibrator vibrator;//震動
	public SharedPreferences sharedpreferences;
	public Handler hd = new Handler()//接收訊息Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 0:
					gotoGameView();//去游戲界面
					break;
				case 1:
					gotoMainMenuView();//去主選單界面
					break;
				case 2:
					gotoPlayVideo();//
					break;
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 設定為全螢幕
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//強制橫屏
    	
    	//游戲過程中只容許調整多媒體音量，而不容許調整通話音量
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		audio=(AudioManager) getSystemService(Service.AUDIO_SERVICE);
    	
		vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);

		//獲得螢幕尺寸
    	DisplayMetrics dm=new DisplayMetrics(); 
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        if(dm.widthPixels>dm.heightPixels)
        {
        	 SCREEN_WIDTH=dm.widthPixels;
             SCREEN_HEIGHT=dm.heightPixels;   
        }
        else
        {
        	SCREEN_WIDTH=dm.heightPixels;
            SCREEN_HEIGHT=dm.widthPixels;    
        }
        
        scaleCL();//自適應螢幕方法
        
        //起始化音效資源
        soundandshakeutil=new SoundAndShakeUtil(this);
        soundandshakeutil.initSounds();
        
        //取得SharedPreferences
        sharedpreferences = this.getSharedPreferences("pp",Context.MODE_PRIVATE);
        PASS_COUNTRY = sharedpreferences.getInt("passCountry", 0);
        if(PASS_COUNTRY == 0){
        	SharedPreferences.Editor editor = sharedpreferences.edit();
        	editor.putInt("passCountry", 1);
        	editor.commit();
        	PASS_COUNTRY = sharedpreferences.getInt("passCountry", 0);
        }
        gotoMainMenuView();
    }
    
    public void gotoMainMenuView()
	{
    	mmsv = new MainMenuSurfaceView(this);
		setContentView(mmsv);//跳躍到主選單界面
    	curr=WhichView.MAINMENU_VIEW;//目前界面置為主選單界面
	}
    
    public void gotoGameView()
	{
    	resetFlags();
    	gsv = new GameSurfaceView(this);
    	setContentView(gsv);//跳躍到游戲界面
    	curr=WhichView.GAME_VIEW;
	}
    
    private void gotoPlayVideo() 
    {
    	for(int i = 0; i < FrameData.playFrameDataList.size();i++)
		{
    		FrameData.playFrameDataList.get(i).isTaskDone=false;   
		}
        for(int i = 0; i < FrameData.helpFrameDataList.size();i++)
		{
    		FrameData.helpFrameDataList.get(i).isTaskDone=false;   
		}
		resetFlags();
		DIFFICULTY = (DIFFICULTY == -1) ? 0 : DIFFICULTY;
		gsv = new GameSurfaceView(this);
    	setContentView(gsv);//跳躍到游戲界面
    	curr=WhichView.GAME_VIEW;
		IS_PLAY_VIDEO = true;
	}
    @Override
	public boolean onKeyDown(int keyCode,KeyEvent e)//重新定義鍵按下方法
	{
		 switch (keyCode) 
		 {
		 case KeyEvent.KEYCODE_VOLUME_UP://按下音量鍵+時增大音量
		 	{
		 		audio.adjustStreamVolume
				(             
						AudioManager.STREAM_MUSIC,             
						AudioManager.ADJUST_RAISE,
						AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI
				);
				break;
		 	}		
		 	case KeyEvent.KEYCODE_VOLUME_DOWN://按下音量鍵-時減小音量
		 	{
		 		audio.adjustStreamVolume
				(             
						AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_LOWER,
						AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI
				);
		 		break;
		 	}
		 case KeyEvent.KEYCODE_BACK://傳回鍵
			 if(curr == WhichView.MAINMENU_VIEW)
			 {
				 if(count == 0)
				 {
					 Toast.makeText(this, "再次按下傳回鍵離開", Toast.LENGTH_SHORT).show();
					 count++;
				 }
				 else if(count == 1)
				 {
					 System.exit(0);
				 }
				 
				 new Thread()
				 {
					 public void run()
					 {
						 try 
						 {
							Thread.sleep(2000);
						 } 
						 catch (InterruptedException e) 
						 {
							e.printStackTrace();
						 }
						 count = 0;
					 }
				 }.start();
			 }
			 break;
		 }
		 return true;
	}
    @Override 
    protected void onResume() 
    {
        super.onResume();
        if(curr == WhichView.GAME_VIEW)
        {
        	if(IS_PLAY_VIDEO)
        	{
        		GameSurfaceView.state = 9;
        		if(IS_HELP)
        		{
        			soundandshakeutil.palyBgSound();
        		}
        	}
        	else
        	{
        		GameSurfaceView.state = 2;
        	}
        }
        else if(curr == WhichView.MAINMENU_VIEW && soundandshakeutil !=null && MainMenuSurfaceView.state != 0)
        {
        	soundandshakeutil.palyBgSound();
        }
    }

    @Override
    protected void onPause() 
    {
        super.onPause();
        if(curr == WhichView.GAME_VIEW)  
        {
        	if(IS_PLAY_VIDEO)
        	{
        		if(gsv != null && gsv.pvt != null)
        		{
        			gsv.pvt.pauseTime = System.nanoTime();
        		}
        		HELP_PAUSE = true;
        		if(IS_HELP)
        		{
        			soundandshakeutil.stopBgSound();
        		}
        	}
        	else
        	{
        		PAUSE = true;
        	}
        }
        else if(curr == WhichView.MAINMENU_VIEW && soundandshakeutil !=null)
        {
        	soundandshakeutil.stopBgSound();
        }
    }
}
