package lwz.com.tank.activity;

import static lwz.com.tank.activity.Constant.*;
import lwz.com.tank.bluetooth.MyDeviceListActivity;
import lwz.com.tank.bluetooth.MyService;
import lwz.com.tank.game.GameData;
import lwz.com.tank.game.MySurfaceView;
import lwz.com.tank.game.OtherSurfaceView;
import lwz.com.tank.view.BlueView;
import lwz.com.tank.view.HelpView;
import lwz.com.tank.view.MainView;
import lwz.com.tank.view.SettingView;
import lwz.com.tank.view.SoundUtil;
import lwz.com.tank.view.WaitView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private String connectedNameStr = null;// �w�s�u���˸m�W��
	private BluetoothAdapter btAdapter = null;// �����Ūޤ����d
	private MyService myService = null;// Service�Ѧ�
	public Vibrator vibrator;//�_��
	
	private MySurfaceView mGLSurfaceView;//�ŧiMySurfaceView
    private OtherSurfaceView oGLSurfaceView;//�ŧiOtherSurfaceView
    public static int GLSurfaceViewglag=0;//���D�ɭ��ЧӦ�
    static boolean search = false;
    
    int countKey=0;
	WhichView curr;
	//�D�ɭ�
	MainView mainview;
	//�]�w�ɭ�
	SettingView settingview;
	//�Ūެɭ�
	BlueView blueview;
	//���U�ɭ�
	HelpView helpview;
	WaitView waitview;
	//�ù������׻P�e��
    public static float screenWidth;
	public static float screenHeight;
	//�ù��������Y���
	public static float scaleratiox=1;
	public static float scaleratioY=1;
	//�I�����ּЧӦ�
	public boolean playbackmusic=false;
	public boolean playsound=false;
	//�ŧi���Ĥu�����O
	public SoundUtil soundutil;
	//�_�l�ƨt���x�s�PŪ���u��
	public SharedPreferences sp;
	public SharedPreferences.Editor editor;
	
	public static float intoflag;
	//�qø����
	public GameData gdFollowDraw=new GameData();
	public boolean followanmiStart;
	public int followanmiIndex;
	public float followanmiX;
	public float followanmiY;
	public float followanmiZ;
	public int followtankeflag;
	
	int tempDataCount=-1;
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        //�]�w���ù�
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		//�]�w���
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//���o�ù��ؤo
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); 
        //���o�ù����ѪR��
        screenWidth=dm.widthPixels;			
		screenHeight=dm.heightPixels;
		//�P�Э㹳�����Y���
		scaleratiox=screenWidth/960;
        scaleratioY=screenHeight/540;
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
        scaleCL();//�۾A���ù���k
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);//�_�ʪ���k
        
        //�_�l�ƭ��ı���u�����O
        soundutil=new SoundUtil(this);
        //�_�l�ƭ���
        soundutil.initSounds(); 
        //�_�l�Ƥ@��tank�t���x�s�ɮ�
        sp=getSharedPreferences("tank",0);
        //Ū���T��
        editor=sp.edit();
        playbackmusic=sp.getBoolean("playback", false);
        playsound=sp.getBoolean("playeffect", false);
        if(playbackmusic)
        {
        	soundutil.play_bg_sound();  
        }
        hd.sendEmptyMessage(0);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(this, "�L�Ū޸˸m�A�L�k�������", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
	}
	//�ŧiHandler
		public Handler hd=new Handler()
		{
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
				case 0:
					gotoMainView();
					break;
				case 1:
					gotoSettingView();
					break;
				case 3:
					gotoBlueView();
					break;
				case 4:
					dopair();
					break;
				case 5:
					discovery();
					break;
				case 6:
					gotoHelpView();
					break;
				case 7:
					gotoSurfaceView();
					break;
				case 8:
					gotoOtherSurfaceView();
					break;
				case 9:
					gotoWaitView();
					break;
				}
			}		
		};
		public void gotoWaitView()
		{
			curr = WhichView.Wait_view;
			waitview = new WaitView(this);
			waitview.requestFocus();
			setContentView(waitview);
		}
		
		public void dopair()
		{
			Intent serverIntent = new Intent(this, MyDeviceListActivity.class);
			startActivityForResult(serverIntent, 1);//1�P��konActivityResult����1�۹���
		}

		public void gotoOtherSurfaceView()
		{
			curr=WhichView.OtherSurfaceView;
			oGLSurfaceView = new OtherSurfaceView(this);
			oGLSurfaceView.requestFocus();
			if (myService == null)
				myService = new MyService(this, mHandler);
	        setContentView(oGLSurfaceView);
	        GLSurfaceViewglag=2;
	        oGLSurfaceView.setFocusableInTouchMode(true);//�]�w���iĲ��   
		}
		
		public void gotoSurfaceView()
		{
			curr=WhichView.MySurfaceView;
			mGLSurfaceView = new MySurfaceView(this);
	        mGLSurfaceView.requestFocus();
	        if (myService == null)
				myService = new MyService(this, mHandler);
	        setContentView(mGLSurfaceView);
	        GLSurfaceViewglag=1;
	        mGLSurfaceView.setFocusableInTouchMode(true);//�]�w���iĲ��
		}
		
		public void gotoHelpView() {
			curr=WhichView.HELP_VIEW;
			helpview = new HelpView(this);
			helpview.requestFocus();
			setContentView(helpview);
		}
		
		public void discovery() {
			if (btAdapter.getScanMode() !=
		            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
		            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		            startActivity(discoverableIntent);
		        }
			if (myService == null)
				myService = new MyService(this, mHandler);
		}
		
		public void gotoBlueView() {
			curr = WhichView.BLUE_VIEW;
			blueview = new BlueView(this);
			blueview.requestFocus();
			setContentView(blueview);
			
		}
		
		public void gotoMainView()
		{
			curr=WhichView.MAIN_MENU_VIEW;
			mainview=new MainView(this);
		    mainview.requestFocus();
		    setContentView(mainview);
		}
		
		public void gotoSettingView()
		{
			curr=WhichView.Setting_VIEW;
			settingview=new SettingView(this);
			settingview.requestFocus();
		    setContentView(settingview);
		}
	    //���s�w�q�˸m�Ǧ^�䪺��k
	    @Override
	    public boolean onKeyDown(int keyCode, KeyEvent e)
	    {
	    	switch(keyCode)
	    	{
	    	case KeyEvent.KEYCODE_HOME:
	    		System.exit(0);
	    		break;

	    	case 4:
	    			if(curr==WhichView.MAIN_MENU_VIEW)
	    			{
	    				if(countKey==0)
	    	        	{
	    	        		countKey++;
	    	        		Toast.makeText(this, "�A���@�����}���� ", Toast.LENGTH_LONG).show();
	    	        		new Thread()
	    	        		{
	    	        			@Override 
	    	        			public void run()
	    	        			{
	    	        				try
	    	        				{
	    	        					Thread.sleep(3000);
	    	        					countKey=0;
	    	        				}
	    	        				catch(Exception e){}
	    	        			}
	    	        		}.start();
	    	        	}
	    	        	else
	    	        	{
	    	        		System.exit(0);
	    	        	}
	    			}
	    			if(curr==WhichView.Setting_VIEW)
	    			{
	    				hd.sendEmptyMessage(0);
	    				SettingView.cdraw=false;
	    				MainView.cdraw=true;
	    			}
	    			if(curr==WhichView.MySurfaceView)
	    			{
	    				hd.sendEmptyMessage(0);
	    				MainView.cdraw=true;
	    			}
	    			if(curr==WhichView.OtherSurfaceView)
	    			{
	    				hd.sendEmptyMessage(0);
	    				MainView.cdraw=true;
	    			}
	    			if(curr==WhichView.HELP_VIEW)
	    			{
	    				hd.sendEmptyMessage(0);
	    				HelpView.cdraw=false;
	    				MainView.cdraw=true;
	    			}
	    			if(curr==WhichView.BLUE_VIEW)
	    			{
	    				hd.sendEmptyMessage(0);
	    				BlueView.cdraw=false;
	    				MainView.cdraw=true;
	    			}
	    			if(curr==WhichView.Wait_view)
	    			{
	    				hd.sendEmptyMessage(3);
	    				WaitView.cdraw=false;
	    				BlueView.cdraw=true;
	    			}
	    			return true;
	    		default:
	    			break;
	    	}
	    	return super.onKeyDown(keyCode, e);
	    }
		
	    @Override 
	    protected void onPause()
	    {
	    	super.onPause();
	    }
	    @Override
		public synchronized void onResume() {
			super.onResume();		
			if (myService != null) {// �إߨö}��Service
				// �Y�GService���Ū��A
				if (myService.getState() == MyService.STATE_NONE) {
					myService.start();// �}��Service
				}
			}
		}
		@Override
		public void onDestroy() {
			super.onDestroy();
			if (myService != null) {// ����Service
				myService.stop();
			}
		}
		// �ǰe�T������k
		public void sendMessage(String message) {
			// ���ˬd�O�_�w�g�s�u��˸m
			if (myService.getState() != MyService.STATE_CONNECTED) {
//				Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
				return;
			}
			if (message.length() > 0) {// �Y�G�T�������ŦA�ǰe�T��
//				byte[] send = message.getBytes();// ���o�ǰe�T�����줸�հ}�C�A����e
				myService.write(message);
			}
		}
		// �B�z�qService�o�Ӫ��T����Handler
		private final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.MSG_READ:
//					byte[] readBuf = (byte[]) msg.obj;
//					// �إ߭n�ǰe���T�����r��
//					String readMessage = new String(readBuf, 0, msg.arg1);
//					byte[] readBuf = (byte[]) msg.obj;
					// �إ߭n�ǰe���T�����r��
					String readMessage = msg.obj.toString();
//					System.out.println(readMessage);
					String[] ss=readMessage.split("<#>");
					if(ss.length==1)
					{
						intoflag=Float.valueOf(ss[0]);
						if(intoflag==1)
						{
							hd.sendEmptyMessage(7);
							WaitView.cdraw=false;
						}
					}
					
					if(ss.length==3)
					{
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.maintkwz[0]=Float.valueOf(ss[0]);
								gdFollowDraw.maintkwz[1]=Float.valueOf(ss[1]);
								gdFollowDraw.maintkwz[2]=Float.valueOf(ss[2]);
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followtkwz[0]=Float.valueOf(ss[0]);
								gdFollowDraw.followtkwz[1]=Float.valueOf(ss[1]);
								gdFollowDraw.followtkwz[2]=Float.valueOf(ss[2]);
							}
						}
					}
					if(ss.length==2)
					{
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.maintkfp[0]=Float.valueOf(ss[0]);
								gdFollowDraw.maintkfp[1]=Float.valueOf(ss[1]);
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followtkfp[0]=Float.valueOf(ss[0]);
								gdFollowDraw.followtkfp[1]=Float.valueOf(ss[1]);
							}
						}
					}
					if(ss.length==4)
					{
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.mainbulletAl.clear();
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followbulletAl.clear();
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
							}
						}
					}
					if(ss.length==6)
					{
						synchronized(gdFollowDraw.dataLock) //�Nø������W
						{
							followanmiStart=Boolean.valueOf(ss[0]);
							followanmiIndex=Integer.valueOf(ss[1]);
							followanmiX=Float.valueOf(ss[2]);
							followanmiY=Float.valueOf(ss[3]);
							followanmiZ=Float.valueOf(ss[4]);
							followtankeflag=Integer.valueOf(ss[5]);
						}
					}
					if(ss.length==8)
					{
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.mainbulletAl.clear();
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[4]),Float.valueOf(ss[5]),Float.valueOf(ss[6]),Float.valueOf(ss[7])});
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followbulletAl.clear();
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[4]),Float.valueOf(ss[5]),Float.valueOf(ss[6]),Float.valueOf(ss[7])});
							}
						}
					}
					if(ss.length==12)
					{
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.mainbulletAl.clear();
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[4]),Float.valueOf(ss[5]),Float.valueOf(ss[6]),Float.valueOf(ss[7])});
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[8]),Float.valueOf(ss[9]),Float.valueOf(ss[10]),Float.valueOf(ss[11])});
							
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followbulletAl.clear();
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[4]),Float.valueOf(ss[5]),Float.valueOf(ss[6]),Float.valueOf(ss[7])});
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[8]),Float.valueOf(ss[9]),Float.valueOf(ss[10]),Float.valueOf(ss[11])});
							}
						}
					}
					if(ss.length==16)
					{
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.mainbulletAl.clear();
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[4]),Float.valueOf(ss[5]),Float.valueOf(ss[6]),Float.valueOf(ss[7])});
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[8]),Float.valueOf(ss[9]),Float.valueOf(ss[10]),Float.valueOf(ss[11])});
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[12]),Float.valueOf(ss[13]),Float.valueOf(ss[14]),Float.valueOf(ss[15])});
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followbulletAl.clear();
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[4]),Float.valueOf(ss[5]),Float.valueOf(ss[6]),Float.valueOf(ss[7])});
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[8]),Float.valueOf(ss[9]),Float.valueOf(ss[10]),Float.valueOf(ss[11])});
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[12]),Float.valueOf(ss[13]),Float.valueOf(ss[14]),Float.valueOf(ss[15])});
							}
						}
					}
					if(ss.length==27)
					{
						float followtempData[][]=new float[9][3];
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.mainTailAl.clear();
								tempDataCount=-1;
								for(int i=0;i<9;i++)
								{
									for(int j=0;j<3;j++)
									{
										tempDataCount++;
										followtempData[i][j]=Float.valueOf(ss[tempDataCount]);
									}
								}
								gdFollowDraw.mainTailAl.add(followtempData);
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followTailAl.clear();
								tempDataCount=-1;
								for(int i=0;i<9;i++)
								{
									for(int j=0;j<3;j++)
									{
										tempDataCount++;
										followtempData[i][j]=Float.valueOf(ss[tempDataCount]);
									}
								}
								gdFollowDraw.followTailAl.add(followtempData);
							}
						}
					}	
					if(ss.length==54)
					{
						float followtempData[][][]=new float[2][9][3];
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.mainTailAl.clear();
								tempDataCount=-1;
								for(int i=0;i<2;i++)
								{
									for(int j=0;j<9;j++)
									{
										for(int k=0;k<3;k++)
										{
											tempDataCount++;
											followtempData[i][j][k]=Float.valueOf(ss[tempDataCount]);
										}
									}
									gdFollowDraw.mainTailAl.add(followtempData[i]);
								}
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followTailAl.clear();
								tempDataCount=-1;
								for(int i=0;i<2;i++)
								{
									for(int j=0;j<9;j++)
									{
										for(int k=0;k<3;k++)
										{
											tempDataCount++;
											followtempData[i][j][k]=Float.valueOf(ss[tempDataCount]);
										}
									}
									gdFollowDraw.followTailAl.add(followtempData[i]);
								}
							}
						}
					}
					if(ss.length==81)
					{
						float followtempData[][][]=new float[3][9][3];
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.mainTailAl.clear();
								tempDataCount=-1;
								for(int i=0;i<3;i++)
								{
									for(int j=0;j<9;j++)
									{
										for(int k=0;k<3;k++)
										{
											tempDataCount++;
											followtempData[i][j][k]=Float.valueOf(ss[tempDataCount]);
										}
									}
									gdFollowDraw.mainTailAl.add(followtempData[i]);
								}
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followTailAl.clear();
								tempDataCount=-1;
								for(int i=0;i<3;i++)
								{
									for(int j=0;j<9;j++)
									{
										for(int k=0;k<3;k++)
										{
											tempDataCount++;
											followtempData[i][j][k]=Float.valueOf(ss[tempDataCount]);
										}
									}
									gdFollowDraw.followTailAl.add(followtempData[i]);
								}
							}
						}
					}
					if(ss.length==108)
					{
						float followtempData[][][]=new float[4][9][3];
						if(GLSurfaceViewglag==2)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.mainTailAl.clear();
								tempDataCount=-1;
								for(int i=0;i<4;i++)
								{
									for(int j=0;j<9;j++)
									{
										for(int k=0;k<3;k++)
										{
											tempDataCount++;
											followtempData[i][j][k]=Float.valueOf(ss[tempDataCount]);
										}
									}
									gdFollowDraw.mainTailAl.add(followtempData[i]);
								}
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //�Nø������W
							{
								gdFollowDraw.followTailAl.clear();
								tempDataCount=-1;
								for(int i=0;i<4;i++)
								{
									for(int j=0;j<9;j++)
									{
										for(int k=0;k<3;k++)
										{
											tempDataCount++;
											followtempData[i][j][k]=Float.valueOf(ss[tempDataCount]);
										}
									}
									gdFollowDraw.followTailAl.add(followtempData[i]);
								}
							}
						}
					}
					break;
					case Constant.MSG_DEVICE_NAME:
					// ���o�w�s�u���˸m�W�١A�åX�{���ܰT��
					connectedNameStr = msg.getData().getString(
							Constant.DEVICE_NAME);
					Toast.makeText(getApplicationContext(),
							"�w�s�u�� " + connectedNameStr, Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		};
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (requestCode) {//requestCode�лx�q����Activity���D���Activity �MstartActivityForResult����requestCode�۹��� 
			case 1: //1�N��s�u�˸m       //resultCode��ܶǦ^�Ȫ��A �ѤlActivity�z�L��setResult()��k�Ǧ^       data�]�A�F�Ǧ^���
				// �Y�G�˸m�M��Activity�Ǧ^�@�ӳs�u���˸m
				if (resultCode == Activity.RESULT_OK) {
					// ���o�˸m��MAC��}
					String address = data.getExtras().getString(
							MyDeviceListActivity.EXTRA_DEVICE_ADDR);
					// ���oBLuetoothDevice����
					BluetoothDevice device = btAdapter.getRemoteDevice(address);
					myService.connect(device);// �s�u�Ӹ˸m
				}
				break;
			}
		}
		public boolean onPrepareOptionsMenu(Button button3){
			// �Ұʸ˸m�M��Activity
			Intent serverIntent = new Intent(this, MyDeviceListActivity.class);
			startActivityForResult(serverIntent, 1);//1�P��konActivityResult����1�۹���
			return true;
		} 
}
//�C�|�U�Ӭɭ�
enum WhichView{
	GAME_VIEW,
	MAIN_MENU_VIEW,
	Setting_VIEW,
	End_VIEW,
	BLUE_VIEW,
	HELP_VIEW,
	MySurfaceView,
	OtherSurfaceView,
	Wait_view
	}
