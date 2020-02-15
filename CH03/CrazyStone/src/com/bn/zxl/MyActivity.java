package com.bn.zxl;

import com.bn.util.Constant;
import com.bn.util.ScreenScaleUtil;
import com.bn.util.SharedPreferencesUtil;
import com.bn.util.SoundUtil;

import static com.bn.util.Constant.*;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MyActivity extends Activity 
{
	WhichView curr;//�ثe���ɭ�
	WelcomeView wv;//�w��ɭ�
	SelectView sv;//�����ɭ�
	HelpView hv;//���U�ɭ�
	GameView gv;//�����ɭ�
	DrawCurrView dcv;
	MainMenuView mmv;
	
	private int keyCount;
	
	SoundUtil soundutil;
	SharedPreferencesUtil sharedUtil;
	//�T������
    public Handler hd=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 0:			//�hwelcomeview	
					gotoWelcomView();
					break;
				case 1:			//�hmainmenuview
					gotoMainMenuView();
					break;			
				case 2:			//�hselectview
					gotoSelectView();
					break;
				case 3:			//gameview
					gotoGameView();
					break;
				case 4:			//helpview
					gotoHelpView();
					break;
			}
		}
	};
	
	public void gotoWelcomView()
	{
		
		wv=new WelcomeView(MyActivity.this);
		setContentView(wv);
		curr=WhichView.WELCOME_VIEW;

	}
	
	public void gotoMainMenuView() {
		
		if (dcv==null) 
		{
			dcv=new DrawCurrView(this);
		}
		if (mmv==null) 
		{
			mmv=new MainMenuView(this);
		}
		dcv.currView=mmv;
		if (curr!=WhichView.SELECT_VIEW&&curr!=WhichView.HELP_VIEW) 
		{
			setContentView(dcv);
		}
		
		curr=WhichView.MAIN_MENU_VIEW;
	}
	
	public void gotoSelectView()
	{
		
		if (dcv==null) 
		{
			dcv=new DrawCurrView(this);
		}
		if (sv==null) 
		{
			sv=new SelectView(this);
		}
		if (curr==WhichView.GAME_VIEW) 
		{
			setContentView(dcv);
		}
		dcv.currView=sv;
		curr=WhichView.SELECT_VIEW;
	}
	
	public void gotoHelpView()
	{
		if (dcv==null) 
		{
			dcv=new DrawCurrView(this);
		}
		if (hv==null) 
		{
			hv=new HelpView(this);
		}
		dcv.currView=hv;
		curr=WhichView.HELP_VIEW;
	}
    //�i�J�����ɭ�
    public void gotoGameView()
    {
    	//�}�Ұ����
    	//Constant.DRAW_THREAD_FLAG=true;
    	dcv.flag=false;
    	//if (gv==null) 
    	{
			gv=new GameView(this);
		}
        setContentView(gv);
        curr=WhichView.GAME_VIEW; 
    }
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //�]�w�����ù�
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,   
        WindowManager.LayoutParams. FLAG_FULLSCREEN); 
        //�]�w�����
    	setVolumeControlStream(AudioManager.STREAM_MUSIC);//���ı���
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//���o�ù��ؤo
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);  

        if(dm.widthPixels>dm.heightPixels)
        {
        	 Constant.SCREEN_WIDTH=dm.widthPixels;
        	 Constant.SCREEN_HEIGHT=dm.heightPixels;
        }
        else
        {
        	Constant.SCREEN_WIDTH=dm.heightPixels;
        	Constant.SCREEN_HEIGHT=dm.widthPixels;
        }
        soundutil=new SoundUtil(MyActivity.this);
        ssr=ScreenScaleUtil.calScale(SCREEN_WIDTH, SCREEN_HEIGHT);
		sharedUtil=new SharedPreferencesUtil(MyActivity.this);
        initPlayerPrefers(sharedUtil);
        new Thread(){
        	@Override
        	public void run(){
        		init2DPic(getResources());//�_�l��2D�K��
                Constant.LOAD_ACTIVITY=true;
        	}
        }.start();
        hd.sendEmptyMessage(0);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {     
    	switch (keyCode) 
    	{     
    		case 4:
    		
        		if(curr==WhichView.GAME_VIEW)
        		{
        			DRAW_THREAD_FLAG=false;
        			gotoSelectView();
        		}
        		else if(curr==WhichView.MAIN_MENU_VIEW)
        		{
        			if (keyCount==0) 
        			{
        				keyCount++;
        				Toast.makeText(getApplicationContext(), "�A���@�U�Ǧ^�����}�����I", Toast.LENGTH_SHORT).show();
					}else 
					{
						DRAW_THREAD_FLAG=false;
						System.exit(0);
					}
        			new Thread()
        			{
        				public void run() 
        				{
        					try 
        					{
								Thread.sleep(3000);
								keyCount=0;
							} 
        					catch (Exception e) 
							{
								e.printStackTrace();
							}
        				}
        			}.start();
        			
        		}
        		else if (curr==WhichView.SELECT_VIEW) 
        		{
        			gotoMainMenuView();
				}
        		else if (curr==WhichView.HELP_VIEW) 
        		{
        			gotoMainMenuView();
				}
    			return true;
    		default:         
    				break;     
    	}     
    	return super.onKeyDown(keyCode, event); 
    } 

    @Override
	protected void onPause() {
		super.onPause();
		if (soundutil.mp!=null) 
		{
			try {
				soundutil.mp.pause();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
    @Override
	protected void onResume() {
		super.onResume();
		if (soundutil.mp!=null&&getBackgroundMusicStatus()) 
		{
			soundutil.mp.start();
		}
	}
}
enum WhichView{ WELCOME_VIEW,
	GAME_VIEW,
	MAIN_MENU_VIEW,
	SELECT_VIEW,
	GOTO_NEXT_VIEW,
	HELP_VIEW
}