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
	private String connectedNameStr = null;// 已連線的裝置名稱
	private BluetoothAdapter btAdapter = null;// 本機藍芽介面卡
	private MyService myService = null;// Service參考
	public Vibrator vibrator;//震動
	
	private MySurfaceView mGLSurfaceView;//宣告MySurfaceView
    private OtherSurfaceView oGLSurfaceView;//宣告OtherSurfaceView
    public static int GLSurfaceViewglag=0;//跳躍界面標志位
    static boolean search = false;
    
    int countKey=0;
	WhichView curr;
	//主界面
	MainView mainview;
	//設定界面
	SettingView settingview;
	//藍芽界面
	BlueView blueview;
	//幫助界面
	HelpView helpview;
	WaitView waitview;
	//螢幕的長度與寬度
    public static float screenWidth;
	public static float screenHeight;
	//螢幕像素的縮放比
	public static float scaleratiox=1;
	public static float scaleratioY=1;
	//背景音樂標志位
	public boolean playbackmusic=false;
	public boolean playsound=false;
	//宣告音效工具類別
	public SoundUtil soundutil;
	//起始化系統儲存與讀取工具
	public SharedPreferences sp;
	public SharedPreferences.Editor editor;
	
	public static float intoflag;
	//從繪制資料
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
        //設定全螢幕
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		//設定橫屏
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//取得螢幕尺寸
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); 
        //取得螢幕的解析度
        screenWidth=dm.widthPixels;			
		screenHeight=dm.heightPixels;
		//與標准像素的縮放比
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
        scaleCL();//自適應螢幕方法
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);//震動的方法
        
        //起始化音效控制工具類別
        soundutil=new SoundUtil(this);
        //起始化音效
        soundutil.initSounds(); 
        //起始化一個tank系統儲存檔案
        sp=getSharedPreferences("tank",0);
        //讀取訊息
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
            Toast.makeText(this, "無藍芽裝置，無法執行游戲", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
	}
	//宣告Handler
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
			startActivityForResult(serverIntent, 1);//1與方法onActivityResult中的1相對應
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
	        oGLSurfaceView.setFocusableInTouchMode(true);//設定為可觸控   
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
	        mGLSurfaceView.setFocusableInTouchMode(true);//設定為可觸控
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
	    //重新定義裝置傳回鍵的方法
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
	    	        		Toast.makeText(this, "再按一次離開游戲 ", Toast.LENGTH_LONG).show();
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
			if (myService != null) {// 建立並開啟Service
				// 若果Service為空狀態
				if (myService.getState() == MyService.STATE_NONE) {
					myService.start();// 開啟Service
				}
			}
		}
		@Override
		public void onDestroy() {
			super.onDestroy();
			if (myService != null) {// 停止Service
				myService.stop();
			}
		}
		// 傳送訊息的方法
		public void sendMessage(String message) {
			// 先檢查是否已經連線到裝置
			if (myService.getState() != MyService.STATE_CONNECTED) {
//				Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
				return;
			}
			if (message.length() > 0) {// 若果訊息不為空再傳送訊息
//				byte[] send = message.getBytes();// 取得傳送訊息的位元組陣列，平行送
				myService.write(message);
			}
		}
		// 處理從Service發來的訊息的Handler
		private final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.MSG_READ:
//					byte[] readBuf = (byte[]) msg.obj;
//					// 建立要傳送的訊息的字串
//					String readMessage = new String(readBuf, 0, msg.arg1);
//					byte[] readBuf = (byte[]) msg.obj;
					// 建立要傳送的訊息的字串
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
							{
								gdFollowDraw.maintkwz[0]=Float.valueOf(ss[0]);
								gdFollowDraw.maintkwz[1]=Float.valueOf(ss[1]);
								gdFollowDraw.maintkwz[2]=Float.valueOf(ss[2]);
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
							{
								gdFollowDraw.maintkfp[0]=Float.valueOf(ss[0]);
								gdFollowDraw.maintkfp[1]=Float.valueOf(ss[1]);
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
							{
								gdFollowDraw.mainbulletAl.clear();
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
							{
								gdFollowDraw.followbulletAl.clear();
								gdFollowDraw.followbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
							}
						}
					}
					if(ss.length==6)
					{
						synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
							{
								gdFollowDraw.mainbulletAl.clear();
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[4]),Float.valueOf(ss[5]),Float.valueOf(ss[6]),Float.valueOf(ss[7])});
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
							{
								gdFollowDraw.mainbulletAl.clear();
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[0]),Float.valueOf(ss[1]),Float.valueOf(ss[2]),Float.valueOf(ss[3])});
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[4]),Float.valueOf(ss[5]),Float.valueOf(ss[6]),Float.valueOf(ss[7])});
								gdFollowDraw.mainbulletAl.add(new float[]{Float.valueOf(ss[8]),Float.valueOf(ss[9]),Float.valueOf(ss[10]),Float.valueOf(ss[11])});
							
							}
						}
						if(GLSurfaceViewglag==1)
						{
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
							synchronized(gdFollowDraw.dataLock) //將繪制資料鎖上
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
					// 取得已連線的裝置名稱，並出現提示訊息
					connectedNameStr = msg.getData().getString(
							Constant.DEVICE_NAME);
					Toast.makeText(getApplicationContext(),
							"已連線到 " + connectedNameStr, Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		};
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (requestCode) {//requestCode標誌從哪個Activity跳躍到該Activity 和startActivityForResult中的requestCode相對應 
			case 1: //1代表連線裝置       //resultCode表示傳回值狀態 由子Activity透過其setResult()方法傳回       data包括了傳回資料
				// 若果裝置清單Activity傳回一個連線的裝置
				if (resultCode == Activity.RESULT_OK) {
					// 取得裝置的MAC位址
					String address = data.getExtras().getString(
							MyDeviceListActivity.EXTRA_DEVICE_ADDR);
					// 取得BLuetoothDevice物件
					BluetoothDevice device = btAdapter.getRemoteDevice(address);
					myService.connect(device);// 連線該裝置
				}
				break;
			}
		}
		public boolean onPrepareOptionsMenu(Button button3){
			// 啟動裝置清單Activity
			Intent serverIntent = new Intent(this, MyDeviceListActivity.class);
			startActivityForResult(serverIntent, 1);//1與方法onActivityResult中的1相對應
			return true;
		} 
}
//列舉各個界面
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
