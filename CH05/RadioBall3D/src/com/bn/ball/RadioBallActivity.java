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
	public Handler hd;//�T�����
	WelcomeView welV;//�w��L���ʵe�ɭ�
	MenuView menuV;//���ɭ����Ѧ�	
	HistoryView hisView;
	SoundView soundView;
	HelpView helpView;
	ViewForDraw vfd;
	MySurfaceView gameView;
	 SoundPool shengyinchi;//���Ħ�
	 HashMap<Integer,Integer> soundId;//����id 
	 public MediaPlayer beijingyinyue; //�I�����ּ���
	 
	 public  SharedPreferences sp;
	 public boolean yinXiao=true;//�������ļЧӦ�
	 
	 
	private MySurfaceView mGLSurfaceView;
	//SensorManager����Ѧ�
	public SensorManager mySensorManager;	
	public Sensor myGravity; 	//�P�������A	
	
	//�}�o��{�FSensorEventListener���f���P������ť��
	private SensorEventListener mySensorListener = new SensorEventListener(){
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
		public void onSensorChanged(SensorEvent event) {
			//�p��X���O�b�ù��W����v��V
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
        mGLSurfaceView.requestFocus();//���o�J�I
        mGLSurfaceView.setFocusableInTouchMode(true);
		//��oSensorManager����
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);	        
        myGravity=mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.SCREEN_WIDTH=dm.widthPixels;			//dm.widthPixels    ���o�ù������ѪR��
        Constant.SCREEN_HEIGHT=dm.heightPixels;
        sp= this.getSharedPreferences("sysz", Context.MODE_PRIVATE);
		 yinXiao=sp.getBoolean("youxiyinxiao", true);
		 Constant.ScaleSR();
		 SQLiteUtil.initDatabase();//�إ߸�Ʈw
			chuangJianSound();//�إ߭I������
			playBeiJingYinYue();//����I������
        hd=new Handler()
        {
			@Override
			public void handleMessage(Message msg)
			{
        		switch(msg.what)
        		{
	        		case 0://�h�w��ɭ�
	        			gotoWelcomeView();
	        		break;
	        		case 1://�h�D���ɭ�
	        			gotoMenuView();
	        		break;
	        		case 2://�h�]�w�ɭ�
	        			gotoSoundView();
	        		break;
	        		case 3://�h���v�ɭ�
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
    	if(keyCode!=4)                                 //�Y�G���O�Ǧ^��h�����B�z
    	{
    		return false; 
    	}
       	if(keyCode==4)
    	{
    		if(curr==WhichView.MENU_VIEW)           //���}����
    		{
    			System.exit(0);
    		}else if(curr==WhichView.SOUND_VIEW)    //���}����
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
        mySensorManager.registerListener(			//�n����ť��
				mySensorListener, 					//��ť������
				myGravity,							//�P�������A
				SensorManager.SENSOR_DELAY_UI		//�P�����ƥ�ǻ����W��
				);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener(mySensorListener);	//�����n����ť��
   
    }     
    
    public void chuangJianSound()   //�إ߭��Ħ�����k
    {
    	shengyinchi=new SoundPool(   //�إ߭��Ħ�
    			6,					 //�P�ɼ���y���̤j�ƶq
    			AudioManager.STREAM_MUSIC,  //�y�����A
        		100
    			);
    	soundId=new HashMap<Integer,Integer>();//�إ�hashmap
    	soundId.put(1, shengyinchi.load(this, R.raw.crash, 1));//���J���Ĩ÷s�W��hashmap��
    	soundId.put(2, shengyinchi.load(this, R.raw.crash2, 1));
    }
    public void playSound(int sound,int loop)//���񭵮Ī���k
    {
    	//�o��AudioManager�רҪ���
    		AudioManager amg=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    		//��o�ثe���q
        	float streamVolumeCurrent=amg.getStreamVolume(AudioManager.STREAM_MUSIC);
        	//��o�̤j���q
        	float streamVolumeMax=amg.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        	float volume=streamVolumeCurrent/streamVolumeMax;
        	//���񭵮�  
        	shengyinchi.play(soundId.get(sound), volume, volume, 1, loop, 1f);  	    	
    }
    public void playBeiJingYinYue(){
    	
    	beijingyinyue=MediaPlayer.create(this,R.raw.backsound);
    	beijingyinyue.setLooping(true);
    	beijingyinyue.start();
    }
}



