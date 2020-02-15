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
	
	
	public final int EXIT_SYSTEM9 = 1;	//9�y�ǳW���}�n���͵���
	public final int EXIT_SYSTEM8 = 2;	//8�y�ǳW���}�n���͵���
	public final int EXIT_WIN = 3;	//Ĺ�o���ɪ���͵���
	public final int EXIT_TIME_UP = 4;	//�ɶ����͵���
	public final int EXIT_BREAK_RECORD = 5;	//�ɶ����͵���
	final int DIALOG_ONE = 6;
	final int DIALOG_TWO = 7;
	
	//�ثe�ɭ����Х�
	int currView;
	//�U�ɭ����Ѧ� 
	WelcomeView welcomeView;//�w��ɭ�
	MainMenuView mainMenuView;//�D�ɭ�
	MySurfaceView gameView;   //�����ɭ�
	SoundControlView soundControlView;//���ı���ɭ�
	HelpView helpView;//���U�ɭ�
	AboutView aboutView;//����ɭ�
	ChoiceView choiceView;//������k�ɭ�
	ModeView modeView;//�Ҧ�����ɭ�
	HighScoreView highScoreView;//�Ʀ�]�ɭ�

	CheckVersionDialog cvDialog;
	AndroidVersionDialog avDialog;
	//�����Ʈw���ܼ�
    int currScore;//���������᪺�o��
	int highestScore;
    //���ּ���
	MediaPlayer mMediaPlayer;	
	SoundPool soundPool;//���֦�
	HashMap<Integer,Integer> soundPoolMap;//���Ħ�������ID�P�ۭq����ID��Map
	
	private boolean backGroundMusicOn=false;//�I�����֬O�_�}�Ҫ��Ч�
	private boolean soundOn=true;//���ĬO�_�}�Ҫ��Ч�
	//����ù��ؤo���귽
	static float screenHeight;//�ù�����
	static float screenWidth;//�ù��e��
	static float screenPictureWidth=480;
	static float screenRatio;//�ù����e��
	static int screenId;//�ù�Id
	//�B�z�U��SurfaceView�ǰe���T��
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
	
	//���toast�Mdialoag��handler
	public Handler hd=new Handler()
    {
    	@Override
    	public void handleMessage(Message msg)
    	{
    		//�I�s�����O�B�z
    		super.handleMessage(msg);
    		//�ھڰT��what�s�������P�A���椣�P���~���޿�
    		switch(msg.what)
    		{
    		   //�N�T���������e���R�X����ܦbToast��
    		   case Constant.OVERTIMETOAST :
    			   //���Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "�ǳW�G���y�O�ɡI", 
               		  Toast.LENGTH_SHORT
               	   ).show();
    		   break;
    		   
    		   //15��᥼���y�A�N�����y�O�ɥǳW�B�z
    		   case Constant.REMINDPLAYER :
    			   //���Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "���ܡG15�������y�A�N���y�O�ɥǳW�I", 
               		  Toast.LENGTH_SHORT
               	   ).show();
    		   break;
    		   
    		   //�N�T���������e���R�X����ܦbToast��
    		   case Constant.MAINBALL_FLOP :
    			   //���Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "�ǳW�G�D�y���U�I", 
               		  Toast.LENGTH_SHORT
               	   ).show();
    		   break;
      		   case Constant.NO_FIGHT :
    			   //���Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "�ǳW�G�D�y���P����y�o�͸I���I", 
               		  Toast.LENGTH_SHORT
               	   ).show();
    		   break;
      		 case Constant.NO_FIGHT_AND_FLOP :
  			   //���Toast
        		   Toast.makeText
               	   (
               			MyActivity.this, 
               		  "�ǳW�G�D�y�i�}�I\n�ǳW�G�D�y���P����y�o�͸I���I", 
               		  Toast.LENGTH_SHORT
               	   ).show();
        		   break;
      		 case Constant.MAINBALLFLOP_AND_NOTFIGHTTARGET :
    			   //���Toast
	        		   Toast.makeText
	               	   (
	               			MyActivity.this, 
	               		  "�ǳW�G�D�y�i�}�I\n�ǳW�G���������O�ت��y�I", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
	        		   break;
      		 case Constant.NO_FIGHT_TARGET :
    			   //���Toast
	        		   Toast.makeText
	               	   (
	               			MyActivity.this, 
	               		  "�ǳW�G���������O�ت��y�I", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
	        		   break;
      		 case Constant.EXIT_SYSTEM_LOSE9 :
      			 
      			MyActivity.this.showDialog(EXIT_SYSTEM9);//�Y�G9���y�i�}
      			
        		   break;
      		 case Constant.EXIT_SYSTEM_LOSE8 :
      			MyActivity.this.showDialog(EXIT_SYSTEM8);//�Y�G9���y�i�}
	        		   break;
      		 case Constant.EXIT_SYSTEM_WIN :          			
      			MyActivity.this.showDialog(EXIT_WIN);//�Y�G9���y�i�}          			
        		   break;
      		 case Constant.TIME_UP :          			
      			MyActivity.this.showDialog(EXIT_TIME_UP);//�Y�G9���y�i�}          			
        		   break;
      		 case Constant.BREAK_RECORD :          			
      			MyActivity.this.showDialog(EXIT_BREAK_RECORD);//�Y�G9���y�i�}          			
        		   break;
    		}
    	}
    };        		
	
	
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //�]�w�����
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //�]�w���ù�
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        					WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //�����L�{���u�e�\�վ�h�C�魵�q�A�Ӥ��e�\�վ�q�ܭ��q
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //+++++++++++++++++++���o�ù����ѪR��,�P�w�ù����begin+++++++++++++++++++++++++++++++++++++
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        screenHeight=dm.heightPixels;
        screenWidth=dm.widthPixels;
        
        float screenHeightTemp=screenHeight;//�O���t�ζǦ^���ù��ѪR�סC
        float screenWidthTemp=screenWidth;
        
        if(screenHeightTemp>screenWidthTemp) //���w�ù����e�M���C
        {
        	screenWidth=screenHeightTemp;
        	screenHeight=screenWidthTemp;
        } 
        screenRatio=screenWidth/screenHeight;//���o�ù������e��
        
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
        
        //�_�l�Ʊ`��
        Constant.initConst(dm.widthPixels, dm.heightPixels);  
         //�_�l�ƭ���
        initBackGroundSound();
        initSoundPool();
      //+++++++++++++++++++���o�ù����ѪR��end+++++++++++++++++++++++++++++++++++++
        
		gotoWellcomeView();//�h�w��ɭ�   
   }
	
	

	@Override
	public Dialog onCreateDialog(int id)
	{
		Dialog dialog=null;
		switch(id)
		{
		case EXIT_SYSTEM9://���}�n���͵���
		case EXIT_SYSTEM8://���}�n���͵���
		case EXIT_WIN://���}�n���͵���
		case EXIT_TIME_UP://���}�n���͵���
		case EXIT_BREAK_RECORD://���}�n���͵���
			Builder b=new AlertDialog.Builder(this); 
			b.setItems(null, null);
			b.setCancelable(false);//�I���Ǧ^�䤣�i�H����
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
    //�C���X�{��͵����ɳQ�^�եH�ʺA��s��͵������e����k
    @Override
    public void onPrepareDialog(int id, final Dialog dialog)
    {
    	//�X�{��͵����e�A����n�귽
		if(gameView != null){
			gameView.stopAllThreads();
			//����I������
	        if(mMediaPlayer.isPlaying()){
	        	mMediaPlayer.stop();
	        }
		}
    	//�Y���O���ݥ�͵����h�Ǧ^
    	switch(id)
    	{
 	   case EXIT_SYSTEM9://���}��͵���
 		   initDialog(dialog,R.string.lose9);//9�y�i�}�ǳW��dialog 		   
		   break;
 	   case EXIT_SYSTEM8://���}��͵���
 		   initDialog(dialog,R.string.lose8);////8�y�i�}�ǳW��dialog
		   break;
 	   case EXIT_WIN://���}��͵���
 		  initDialog(dialog,R.string.win);//Ĺ�o���ɪ���͵���
		   break;
 	   case EXIT_TIME_UP://���}��͵���
 		  initDialog(dialog,R.string.time_up);//Ĺ�o���ɪ���͵���
		   break;
 	  case EXIT_BREAK_RECORD://���}��͵���
 		  initDialog(dialog,R.string.break_record);//Ĺ�o���ɪ���͵���
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
    	//��͵����������`������VLinearLayout
	   	LinearLayout lle=new LinearLayout(MyActivity.this);
		lle.setOrientation(LinearLayout.VERTICAL);		//�]�w�¦V	
		lle.setGravity(Gravity.CENTER_HORIZONTAL);
		lle.setBackgroundResource(R.drawable.dialog);
		
		//���D�檺����LinearLayout
		LinearLayout llt1=new LinearLayout(MyActivity.this);
		llt1.setOrientation(LinearLayout.HORIZONTAL);		//�]�w�¦V	
		llt1.setGravity(Gravity.LEFT);//�m��
		llt1.setLayoutParams(new ViewGroup.LayoutParams(350, LayoutParams.WRAP_CONTENT));
		
		//���D�檺��r
		TextView title1=new TextView(MyActivity.this);
		title1.setText(R.string.game_over);
		title1.setTextSize(15);//�]�w�r���j�p
		title1.setTextColor(MyActivity .this.getResources().getColor(R.color.black));//�]�w�r���m��
		llt1.addView(title1);
		
		//�N���D��s�W���`LinearLayout
		lle.addView(llt1);
		
		
		//���D�檺����LinearLayout
		LinearLayout llt2=new LinearLayout(MyActivity.this);
		llt2.setOrientation(LinearLayout.HORIZONTAL);		//�]�w�¦V	
		llt2.setGravity(Gravity.CENTER);//�m��
		llt2.setLayoutParams(new ViewGroup.LayoutParams(350, LayoutParams.WRAP_CONTENT));
		
		//���D�檺��r
		TextView title2=new TextView(MyActivity.this);
		title2.setText(StringId);
		title2.setTextSize(15);//�]�w�r���j�p
		title2.setTextColor(MyActivity .this.getResources().getColor(R.color.black));//�]�w�r���m��
		llt2.addView(title2);
		
		//�N���D��s�W���`LinearLayout
		lle.addView(llt2);
		

		LinearLayout lleb=new LinearLayout(MyActivity.this);
		lleb.setOrientation(LinearLayout.HORIZONTAL);//������V
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
		            	//������͵���
						dialog.cancel();
						//�ǰe�h�����ɭ����T��
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
						dialog.cancel();//������͵���
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
    	mMediaPlayer=MediaPlayer.create(this,R.raw.backsound);//�إ߭I������
    	mMediaPlayer.setLooping(true);//�]�w���`��
	}
    public void initSoundPool(){
    	//���֦� 
    	soundPool=new SoundPool(10,AudioManager.STREAM_MUSIC,100);
    	soundPoolMap=new HashMap<Integer,Integer>();
    	//�}��������
    	soundPoolMap.put(1,soundPool.load(this,R.raw.start,1));
    	//�y�y�I��������
    	soundPoolMap.put(2, soundPool.load(this,R.raw.hit,1));
    	//�y���I��,�y�i�}������
    	soundPoolMap.put(3,soundPool.load(this,R.raw.ballin,1));
    }
	public void playSound(int sound, int loop, float ratio) 
    {
	    if(pauseFlag){return;}
		AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;  
	    //����Ī��j�p
	    volume *= ratio;
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 0.5f);
    }
	@Override
    public void onResume()
    {
    	super.onResume();//�I�s�����O��k
    	pauseFlag=false;    	
    }
	@Override
    public void onPause()
    {
		pauseFlag=true;		
    	super.onPause();
    } 
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent e)//���s�w�q����U��k
	{
			switch(keyCode)
	    	{
	    	    case 4://�Ǧ^��
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
			    		//���Ǧ^�����}�ɡA�n�T�O�Ҧ���������w�g����
			    		//�귽���J������~�i�H�I���Ǧ^��
			    		if(gameView.hasLoadOk)
			    		{
			    			if(gameView != null){
			    				gameView.stopAllThreads();
			    				//����I������
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
    //�VHandler�ǰe�T������k
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

    //�h�w��ɭ�����k
    private void gotoWellcomeView()
    {
    	if(welcomeView==null)
    	{
    		 welcomeView = new WelcomeView(this);
    	}
    	this.setContentView(welcomeView);
    	currView=WhatMessage.WELLCOME_VIEW;
    } 
    
    //�h�D���ɭ�����k
    private void gotoMainMenuView()
    {
    	//�Y�G�O�ʵe���񧹲���i�J���D���ɭ��A���P�_�����O�_���T
    	if(currView==WhatMessage.WELLCOME_VIEW && !judgeVersion()){
    		return;
    	}
    	//�Y�G�������b�i�椤���}�F�����A������������Ҧ������
    	if(gameView!=null){
    		gameView.stopAllThreads();

            //����I������
            if(mMediaPlayer.isPlaying()){
            	mMediaPlayer.stop();
            }
            //�NgameView�ѦҲM�šA�H�K�A���i�J�����ɭ��s�إ߸Ӫ���
    		gameView = null;
    	}
    	if(mainMenuView==null)
    	{
    		mainMenuView = new MainMenuView(this);
    	}
    	this.setContentView(mainMenuView);
    	currView=WhatMessage.MAIN_MENU_VIEW;
    }
    
    //�h�]�w���Ĭɭ�����k
    private void gotoSoundControlView()
    {
    	if(soundControlView==null)
    	{
    		soundControlView = new SoundControlView(this);
    	}
    	this.setContentView(soundControlView);
    	currView=WhatMessage.SOUND_CONTORL_VIEW;
    }
    //�h�����ɭ�
    private void gotoGameView()
    {
    	gameView = new MySurfaceView(this);
    	this.setContentView(gameView);
    	currView = WhatMessage.GAME_VIEW;
    }
    //�h���U�ɭ�����k
    private void gotoHelpView()
    {
    	if(helpView==null)
    	{
    		helpView = new HelpView(this);
    	}
    	this.setContentView(helpView);
    	currView=WhatMessage.HELP_VIEW;
    }
    //�h����ɭ�����k
    private void gotoAboutView()
    {
    	if(aboutView==null)
    	{
    		aboutView = new AboutView(this);
    	}
    	this.setContentView(aboutView);
    	currView=WhatMessage.ABOUT_VIEW;
    }
    //�h�ﶵ�ɭ�����k
    private void gotoChoiceView()
    {
    	if(choiceView==null)
    	{
    		choiceView=new ChoiceView(this);
    	}
    	this.setContentView(choiceView);
    	currView=WhatMessage.CHOICE_VIEW;
    } 
    //�h�ﶵ�ɭ�����k
    private void gotoModeView()
    {
    	if(modeView==null)
    	{
    		modeView=new ModeView(this);
    	}
    	this.setContentView(modeView);
    	currView=WhatMessage.MODE_VIEW;
    }
    //�h�n���]�ɭ�����k
    private void gotoHighScoreView()
    {
    	if(highScoreView==null)
    	{
    		highScoreView = new HighScoreView(this);
    	}
    	this.setContentView(highScoreView);
    	currView=WhatMessage.HIGH_SCORE_VIEW;
    }
    
    //�N�o���M�ɶ����J��Ʈw�A�ø��D������������ɭ�
    private void goToOverView()
    {
    	//���o�̰���
    	highestScore = DBUtil.getHighestScore(Constant.POS_INDEX);
    	//��o�ثe���ƩM����ô��J��Ʈw
    	DBUtil.insert(Constant.POS_INDEX, currScore, DateUtil.getCurrentDate());
    	
    	//�Y�G�ثe�o���j��n���]���̰����A�i�J�ӧQ���ɭ�
		if(currScore>highestScore){
			//������}�{����dialog
			this.gameView.bgt.sendHandlerMessage(Constant.BREAK_RECORD);
			
		}
		//�Y�G�ثe�o�����j��n���]���̰���
		else if(currScore > 0){
			//���Ĺ�o��������͵���
			this.gameView.bgt.sendHandlerMessage(Constant.EXIT_SYSTEM_WIN);
		}
    	
    }
    
    //�P�_��������k�A�Y�G���������T�Ǧ^false�A�_�h�Ǧ^true
    boolean judgeVersion(){
	    //�P�_�ثe�t�ΩҤ䴩���̰�opengles�����O���O�j��2
	    if(this.getGLVersion() < 2)  
	    {
	    	this.showDialog(DIALOG_ONE);
	    	return false;
	    }
    	//�P�_�ثeAndroid�����O���O�C��2.2
	    else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
	    {
	    	this.showDialog(DIALOG_TWO);
	    	return false;
	    }
	    return true;
    }
    //���oOPENGLES�Ҥ䴩���̰�����
    public int getGLVersion() 
    {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        int majorVersion=info.reqGlEsVersion;
        majorVersion=majorVersion>>>16;
        return majorVersion;
    }
}