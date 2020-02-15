package lwz.com.tank.game;

import static lwz.com.tank.activity.Constant.*;
import static lwz.com.tank.game.TempBullet.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import lwz.com.tank.activity.BulletTextureByVertex;
import lwz.com.tank.activity.Floor;
import lwz.com.tank.activity.HighWall;
import lwz.com.tank.activity.LowWall;
import lwz.com.tank.activity.MainActivity;
import lwz.com.tank.activity.MiddleWall;
import lwz.com.tank.activity.R;
import lwz.com.tank.activity.TextureRect;
import lwz.com.tank.tail.DrawBuffer;
import lwz.com.tank.tail.Tail;
import lwz.com.tank.util.LoadUtil;
import lwz.com.tank.util.LoadedObjectVertexNormalTexture;
import lwz.com.tank.util.PicLoadUtil;
import lwz.com.tank.view.MainView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class OtherSurfaceView extends GLSurfaceView
{
	MainActivity activity;
	public TempBulletGoThread tempbgt;//定時搬移炮彈的執行緒
	private SceneRenderer mRenderer;//場景著色器
	
	public int floorTexId;//地板紋理ID
	public int wallTexId;//牆紋理ID
	public int tanklifeId;//坦克生命紋理
	public int tanklifeId_1;//坦克生命紋理
	public int yaoganTextureId1;//搖桿1
	public int yaoganTextureId2;//搖桿2
	public int tankId1;//坦克紋理ID
	public int tankId2;//坦克紋理ID
	public int endId;//結束紋理
	int[] exploTextureId=new int[33];//爆炸紋理群組
	public int bulletId;//子彈紋理ID
	public int tailId;//子彈尾巴紋理ID
	 
	//坦克2的起始位置
	public static float Tank2_x=0;
	public static float Tank2_y=100;
	//搖桿中心點像素座標及半徑
	public  float INIT_x=(115+ssr.lucX)*ssr.ratio;
	public  float INIT_y=(425+ssr.lucY)*ssr.ratio;
	public  float INIT_R=100*(SCREEN_WIDTH-ssr.lucX*ssr.ratio*2)/SCREEN_WIDTH_STANDARD;
	
    public Object aqLock=new Object();	//動作佇列鎖
	public Queue<Action> aq=new LinkedList<Action>();//動作佇列
	DoOtherActionThread doat;					//執行動作執行緒參考
	float touchflag;//觸控事件標志位
	
	//存放點的HashMap
	HashMap<Integer,BNPoint> hm=new HashMap<Integer,BNPoint>();
	//建立一個BNPoint
	BNPoint bp;
	static int countl=0;
	//主資料
	GameData gdMain=new GameData();
	//主繪制資料
	GameData gdMainDraw=new GameData();
	//從繪制資料
	GameData gdFollowDraw=new GameData();
	
	 public  ArrayList<TempBullet> tempbulletAl=new ArrayList<TempBullet>();//炮彈清單
	 DrawBuffer texRect;
	
	 public int mainbulletflag=0;//主子彈標志位
	 public int followbulletflag=0;//從子彈標志位
	 public  int bulletCount=0;//子彈數量
	 
	public OtherSurfaceView(MainActivity activity) {
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);
		mRenderer=new SceneRenderer();
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		doat=new DoOtherActionThread(OtherSurfaceView.this);//建立執行動作執行緒
		doat.start();//啟動執行緒
		ActionThread at=new ActionThread();//建立佇列動作控制執行緒
		at.start();//啟動執行緒
		tempbgt=new TempBulletGoThread(tempbulletAl,this);//建立定時搬移炮彈的執行緒
		tempbgt.start();//啟動此執行緒
	}
	//觸摸事件回調方法
		@Override
		public boolean onTouchEvent(MotionEvent e)
		{
			//取得觸控的動作編號
			int action=e.getAction()&MotionEvent.ACTION_MASK;
			//取得主、輔點id（down時主輔點id皆正確，up時輔點id正確，主點id要查詢Map中剩下的一個點的id）
			int id=(e.getAction()&MotionEvent.ACTION_POINTER_ID_MASK)>>>MotionEvent.ACTION_POINTER_ID_SHIFT;	
			switch(action)									//>>>的意思是無號右移
			{
				case MotionEvent.ACTION_DOWN: //主點down 	
					if(activity.followtankeflag<3&&TempBullet.tankeflag1<3)	
					{
						//向Map中記錄一個新點
						hm.put(id, new BNPoint(e.getX(id),e.getY(id),countl++));
					}
				break;
				case MotionEvent.ACTION_POINTER_DOWN: //輔點down	
//					if(TempBullet.tankeflag2<3&&TempBullet.tankeflag1<3)	
					if(activity.followtankeflag<3&&TempBullet.tankeflag1<3)	
					{
						if(id<e.getPointerCount()-1)
						{
							//將編號往後順（相當於給點排序）
							HashMap<Integer,BNPoint> hmTemp=new HashMap<Integer,BNPoint>();
							Set<Integer> ks=hm.keySet();
							for(int i=0;i<ks.size();i++)
							{
								if(i<id)
								{
									hmTemp.put(i, hm.get(i));
								}
								else
								{
									hmTemp.put(i+1, hm.get(i));
								}
							}
							hm=hmTemp;					
						}
						//向Map中記錄一個新點
						hm.put(id, new BNPoint(e.getX(id),e.getY(id),countl++));
					}
				break;
				case MotionEvent.ACTION_MOVE: //主/輔點move  
					if(activity.followtankeflag<3&&TempBullet.tankeflag1<3)	
					{
						//不論主/輔點Move都更新其位置
						Set<Integer> ks=hm.keySet();
						for(int i=0;i<ks.size();i++)
						{
							if(hm.get(i)!=null)
							{
								hm.get(i).setLocation(e.getX(i), e.getY(i));	
							}
						}
					}
				break;
				case MotionEvent.ACTION_POINTER_UP: //輔點up	
					if(activity.followtankeflag<3&&TempBullet.tankeflag1<3)	
					{
						if(hm.get(id)!=null)
						{
							if(hm.get(id).gettouchflag()==2)
			        		{
								if(bulletCount<=3)
								{
									bulletCount++;
									Action acTemp = new Action
									(
				    					ActionType.FIRE_ACTION,		//動作型態
										new float[]{hm.get(id).getx(),hm.get(id).gety()}			//動作資料
									);
				    				synchronized(aqLock)			//鎖上動作佇列
									{
										aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
									}
								}
			        		}
							if(hm.get(id).gettouchflag()==1)
							{
								Action acTemp = new Action
		    					(
		        					ActionType.MOVE_ACTION,	//動作型態
		    						new float[]{INIT_x,INIT_y}			//動作資料
		    					);
		        				synchronized(aqLock)			//鎖上動作佇列
		    					{
		    						aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
		    					}
							}
							//從Map中移除對應id的輔點
							hm.remove(id);
							//將編號往前順，不空著
							HashMap<Integer,BNPoint> hmTemp=new HashMap<Integer,BNPoint>();
							Set<Integer> ks=hm.keySet();
							for(int i=0;i<ks.size();i++)
							{
								if(i>id)
								{
									hmTemp.put(i-1, hm.get(i));
								}
								else
								{
									hmTemp.put(i, hm.get(i));
								}
							}
							hm=hmTemp;
						}
					}
				break;
				case MotionEvent.ACTION_UP: //主點up
					if(activity.followtankeflag<3&&TempBullet.tankeflag1<3)	
					{
						if(hm.get(id)!=null)
						{
							if(hm.get(id).gettouchflag()==2)
			        		{
								if(bulletCount<=2)
								{
									bulletCount++;
									Action acTemp = new Action
									(
				    					ActionType.FIRE_ACTION,		//動作型態
										new float[]{hm.get(id).getx(),hm.get(id).gety()}			//動作資料
									);
				    				synchronized(aqLock)			//鎖上動作佇列
									{
										aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
									}
								}
			        		}
							if(hm.get(id).gettouchflag()==1)
							{
								Action acTemp = new Action
		    					(
		        					ActionType.MOVE_ACTION,	//動作型態
		    						new float[]{INIT_x,INIT_y}			//動作資料
		    					);
		        				synchronized(aqLock)			//鎖上動作佇列
		    					{
		    						aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
		    					}
							}
							//在本套用中主點UP則只需要清理Map即可，在其他一些套用中需要動作的
							//則取出Map中唯一剩下的點動作即可
							hm.clear();
							countl=0;
						}
					}
					else
					{
						if(e.getX()>(180+ssr.lucX)*ssr.ratio&&e.getX()<(500+ssr.lucX)*ssr.ratio&&e.getY()>(150+ssr.lucY)*ssr.ratio&&e.getY()<(340+ssr.lucY)*ssr.ratio)
						{
							MainView.cdraw=true;
	        				activity.hd.sendEmptyMessage(0);
	        				TempBullet.tankeflag1=0;
	        				activity.followtankeflag=0;
	        				bulletCount=0;
	        				DoOtherActionThread.bx=0;
	        				DoOtherActionThread.by=0;
	        				DoOtherActionThread.scale_x=0;
	        				DoOtherActionThread.scale_y=0;
	        				aq.clear();
	        				synchronized(gdMain.dataLock) 	//將主資料鎖上
	        				{
	        					gdMain.followtkwz[0]=0;
	        					gdMain.followtkwz[1]=100;
	    						gdMain.followtkwz[2]=0;
	        					gdMain.followtkwz[3]=0;
	    						gdMain.followtkwz[4]=0;
	    						gdMain.followtkfp[0]=0;
	        					synchronized(gdMainDraw.dataLock) //將繪制資料鎖上
	        					{
	        						gdMainDraw.followtkwz[0]=0;
	        						gdMainDraw.followtkwz[1]=100;
	        						gdMainDraw.followtkwz[2]=0;
	        						gdMainDraw.followtkwz[3]=0;
	        						gdMainDraw.followtkwz[4]=0;
	        						gdMainDraw.followtkfp[0]=0;
	        					}
	        				}
	        				synchronized(activity.gdFollowDraw.dataLock)	//鎖上繪制資料
	        				{
	        					activity.gdFollowDraw.maintkwz[0]=0;
	        					activity.gdFollowDraw.maintkwz[1]=-100;
	        					activity.gdFollowDraw.maintkwz[2]=0;
	        					activity.gdFollowDraw.maintkwz[3]=0;
	        					activity.gdFollowDraw.maintkwz[4]=0;
	        					activity.gdFollowDraw.maintkfp[0]=0;
	        				}
	        				TempBullet.tempanmiStart=false;
	        				activity.followanmiStart=false;
							hm.clear();
							tempbulletAl.clear();
							gdMainDraw.followTailAl.clear();
							gdMain.followTailAl.clear();
						}
						if(e.getX()>(580+ssr.lucX)*ssr.ratio&&e.getX()<(800+ssr.lucX)*ssr.ratio&&e.getY()>(150+ssr.lucY)*ssr.ratio&&e.getY()<(340+ssr.lucY)*ssr.ratio)
	        			{
							TempBullet.tankeflag1=0;
							activity.followtankeflag=0;
	        				DoOtherActionThread.bx=0;
	        				DoOtherActionThread.by=0;
	        				DoOtherActionThread.scale_x=0;
	        				DoOtherActionThread.scale_y=0;
	        				aq.clear();
	        				synchronized(gdMain.dataLock) 	//將主資料鎖上
	        				{
	        					gdMain.followtkwz[0]=0;
	        					gdMain.followtkwz[1]=100;
	    						gdMain.followtkwz[2]=0;
	        					gdMain.followtkwz[3]=0;
	    						gdMain.followtkwz[4]=0;
	    						gdMain.followtkfp[0]=0;
	        					synchronized(gdMainDraw.dataLock) //將繪制資料鎖上
	        					{
	        						gdMainDraw.followtkwz[0]=0;
	        						gdMainDraw.followtkwz[1]=100;
	        						gdMainDraw.followtkwz[2]=0;
	        						gdMainDraw.followtkwz[3]=0;
	        						gdMainDraw.followtkwz[4]=0;
	        						gdMainDraw.followtkfp[0]=0;
	        					}
	        				}
	        				synchronized(activity.gdFollowDraw.dataLock)	//鎖上繪制資料
	        				{
	        					activity.gdFollowDraw.maintkwz[0]=0;
	        					activity.gdFollowDraw.maintkwz[1]=-100;
	        					activity.gdFollowDraw.maintkwz[2]=0;
	        					activity.gdFollowDraw.maintkwz[3]=0;
	        					activity.gdFollowDraw.maintkwz[4]=0;
	        					activity.gdFollowDraw.maintkfp[0]=0;
	        				}
	        				TempBullet.tempanmiStart=false;
	        				activity.followanmiStart=false;
							hm.clear();
							tempbulletAl.clear();
							gdMainDraw.followTailAl.clear();
							gdMain.followTailAl.clear();
	        			}
					}
				break;
			} 
			return true;
		}	
		public class ActionThread extends Thread
		{
			public ActionThread()
			{
			}
			@Override
			 public void run()
			 {
				while(true)
				{
					//取得map的鍵值
			        Set<Integer> ks=hm.keySet();
			        if(ks!=null)
			        {
			        	for(int i=0;i<ks.size();i++)
			        	{
			        		bp=hm.get(i);
			        		if(bp!=null)
			        		{
			        			if(bp.gettouchflag()==1)
				        		{
				        			Action acTemp = new Action
		        					(
		            					ActionType.MOVE_ACTION,		//動作型態
		        						new float[]{bp.getx(),bp.gety()}			//動作資料
		        					);
		            				synchronized(aqLock)			//鎖上動作佇列
		        					{
		        						aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
		        					}
				        		}
			        		}
			        	}
			        }
					try 
					{
						Thread.sleep(25);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
			 }
		}
		private class SceneRenderer implements GLSurfaceView.Renderer
		{
			Floor floor;//起始化地板
			LowWall lowwall;//起始化矮牆
			MiddleWall middlewall;//起始化中牆
			HighWall highwall;//起始化高牆
			TextureRect tanklife1;//坦克生命1
			TextureRect tanklife1_1;//坦克生命1
			TextureRect tanklife2;//坦克生命2
			TextureRect tanklife2_1;//坦克生命2
			TextureRect tanklife3;//坦克生命3
			TextureRect tanklife3_1;//坦克生命2
			TextureRect yaoganButton1;//搖桿按鈕1
			TextureRect yaoganButton2;//搖桿按鈕2
			BulletTextureByVertex bullet;//起始化子彈
			BulletTextureByVertex []bullet1=new BulletTextureByVertex[4];//起始化子彈
			BulletTextureByVertex followbullet[]=new BulletTextureByVertex[4];//起始化子彈
			TextureRect[] trExplo=new TextureRect[33];//爆炸動畫
			TextureRect end;//結束界面
			
			//從特殊的obj檔案中載入物件
			LoadedObjectVertexNormalTexture lovn1;
			LoadedObjectVertexNormalTexture lovn2;
			
			LoadedObjectVertexNormalTexture lovn3;
			LoadedObjectVertexNormalTexture lovn4;

			@Override
			public void onDrawFrame(GL10 gl) {
				//清除彩色快取
	        	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	        	//設定目前矩陣為模式矩陣
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            //設定目前矩陣為單位矩陣
	            gl.glLoadIdentity();  
	            
	            GLU.gluLookAt
	            (
	            	gl, 
	            	0,0,200f,
	            	0,0,0f,
	            	0,1,0	
	            ); 
	            
	            float maintkwzTemp[]=new float[5];
	            synchronized(gdMainDraw.dataLock)	//鎖上繪制資料
	            {
	            	maintkwzTemp[0]=gdMainDraw.followtkwz[0];
	            	maintkwzTemp[1]=gdMainDraw.followtkwz[1];
	            	maintkwzTemp[2]=gdMainDraw.followtkwz[2];
	            	maintkwzTemp[3]=gdMainDraw.followtkwz[3];
	            	maintkwzTemp[4]=gdMainDraw.followtkwz[4];
	            }
	            float followtkwzTemp[]=new float[5];
	            synchronized(activity.gdFollowDraw.dataLock)	//鎖上繪制資料
	            {
	            	followtkwzTemp[0]=activity.gdFollowDraw.maintkwz[0];
	            	followtkwzTemp[1]=activity.gdFollowDraw.maintkwz[1];
	            	followtkwzTemp[2]=activity.gdFollowDraw.maintkwz[2];
	            	followtkwzTemp[3]=activity.gdFollowDraw.maintkwz[3];
	            	followtkwzTemp[4]=activity.gdFollowDraw.maintkwz[4];
	            }
	            
	            float maintkfpTemp[]=new float[2];
	            synchronized(gdMainDraw.dataLock)	//鎖上繪制資料
	            {
	            	maintkfpTemp[0]=gdMainDraw.followtkfp[0];
	            	maintkfpTemp[1]=gdMainDraw.followtkfp[1];
	            }
	            if(maintkfpTemp[0]==1)
	            {
	            	fire();
	            	mainbulletflag=1;
	            	gdMainDraw.followtkfp[0]=0;
	            }
	            float followtkfpTemp[]=new float[2];
	            synchronized(activity.gdFollowDraw.dataLock)	//鎖上繪制資料
	            {
	            	followtkfpTemp[0]=activity.gdFollowDraw.maintkfp[0];
	            	followtkfpTemp[1]=activity.gdFollowDraw.maintkfp[1];
	            }
	            
	            //繪制地板
	            gl.glPushMatrix();
	            gl.glTranslatef(0, 0, -95f);  
	            floor.drawSelf(gl);
	            gl.glPopMatrix();
	            
	            //繪制牆
	            gl.glPushMatrix();
	            gl.glTranslatef(0, 0f, -100f); 
	            lowwall.drawSelf(gl);
	            gl.glPopMatrix();
	            //繪制牆
	            gl.glPushMatrix();
	            gl.glTranslatef(0, 0f, -100f); 
	            middlewall.drawSelf(gl);
	            gl.glPopMatrix();
	            //繪制牆
	            gl.glPushMatrix();
	            gl.glTranslatef(0, 0f, -100f); 
	            highwall.drawSelf(gl);
	            gl.glPopMatrix();
	            
	            if(activity.followtankeflag<3)
	            {
	            	//繪制坦克1
					if(lovn1!=null)
		            {
		            	gl.glPushMatrix();
		            	gl.glTranslatef(followtkwzTemp[0], followtkwzTemp[1], -100);
		            	gl.glRotatef(90,1, 0, 0);       	          
		            	gl.glRotatef(followtkwzTemp[2],0, 1, 0);       
		            	lovn1.drawSelf(gl);
		            	gl.glPopMatrix();	
		            } 
		            //繪制炮管
		            if(lovn2!=null)
		            {
		            	gl.glPushMatrix();
		            	gl.glTranslatef(followtkwzTemp[0], followtkwzTemp[1], -84);
		            	gl.glRotatef(90,1,0, 0);
		            	gl.glRotatef(followtkfpTemp[1],0,1,0);       
		            	lovn2.drawSelf(gl);
		            	gl.glPopMatrix();
		            } 
	            }
	            
	            if(TempBullet.tankeflag2<3)
	            {
	            	//繪制坦克2
	 	    	   if(lovn3!=null)
	 	           {
	 	           	gl.glPushMatrix();
	 	           	gl.glTranslatef(maintkwzTemp[0], maintkwzTemp[1], -100);
	 	           	gl.glRotatef(90,1, 0, 0);       	          
	 	           	gl.glRotatef(maintkwzTemp[2],0, 1, 0);       
	 	           	lovn3.drawSelf(gl);
	 	           	gl.glPopMatrix();	
	 	           } 
	 	           if(lovn4!=null)
	 	           {
	 	           	gl.glPushMatrix();
	 	           	gl.glTranslatef(maintkwzTemp[0], maintkwzTemp[1], -84);       
	 	           	gl.glRotatef(90,1,0, 0);
	 	           	gl.glRotatef(maintkfpTemp[1],0,1,0);       
	 	           	lovn4.drawSelf(gl);
	 	           	gl.glPopMatrix();
	 	           }
	            }
	            
	            float mainbulletTemp[]=new float[4];
	            synchronized(gdMainDraw.dataLock)	//鎖上繪制資料
	            {
	            	if(gdMainDraw.followbulletAl!=null)
	            	{
	            		for(int i=0;i<gdMainDraw.followbulletAl.size();i++)
	                	{
	            			mainbulletTemp=gdMainDraw.followbulletAl.get(i);
	            			if(mainbulletTemp!=null)
	            			{
	            				gl.glPushMatrix();//保護現場
		    	    			gl.glTranslatef(mainbulletTemp[0], mainbulletTemp[1], mainbulletTemp[2]);//搬移到指定位置
		    	    			gl.glRotatef(-90, 1, 0, 0);
		    	    			gl.glRotatef(mainbulletTemp[3], 0, 1, 0);
		    	    			//若爆炸動畫沒有開始，標準繪制炮彈
		    	    			bullet1[i].drawSelf(gl);
		    	    			gl.glPopMatrix();
	            			}
	                	}
	            	}
	            	
	            }
	            
	            float followbulletTemp[]=new float[4];
	            synchronized(activity.gdFollowDraw.dataLock)	//鎖上繪制資料
	            {
	            	if(activity.gdFollowDraw.mainbulletAl!=null)
	            	{
	            		for(int i=0;i<activity.gdFollowDraw.mainbulletAl.size();i++)
	            		{
	            			followbulletTemp=activity.gdFollowDraw.mainbulletAl.get(i);
	            			if(followbulletTemp!=null)
	            			{
	            				gl.glPushMatrix();//保護現場
		    	    			gl.glTranslatef(followbulletTemp[0], followbulletTemp[1], followbulletTemp[2]);//搬移到指定位置
		    	    			gl.glRotatef(-90, 1, 0, 0);
		    	    			gl.glRotatef(followbulletTemp[3], 0, 1, 0);
		    	    			//若爆炸動畫沒有開始，標準繪制炮彈
		    	    			followbullet[i].drawSelf(gl);
		    	    			gl.glPopMatrix();
	            			}
	            		}
	            		if(activity.gdFollowDraw.mainbulletAl.size()==1)
	            		{
	            			activity.gdFollowDraw.mainbulletAl.remove(0);
	            		}
	            	}
	            }
	           
	            gl.glEnable(GL10.GL_BLEND);//開啟混合
	            gl.glDisable(GL10.GL_DEPTH_TEST); //開啟深度檢驗
	            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);//設定源混合因子與目的混合因子

	            //繪制主方尾巴
	            float TempData[][] = new float[9][3];
		        synchronized(gdMainDraw.dataLock)	//鎖上繪制資料
		        {
		        	if(gdMainDraw.followTailAl!=null)
			       	{
			       		for(int i=0;i<gdMainDraw.followTailAl.size();i++)
			       		{
			       			TempData=gdMainDraw.followTailAl.get(i);
			       			Tail tail = new Tail(texRect,TempData);
			       			gl.glPushMatrix();  
			               	gl.glTranslatef(0, 0, -84);
			               	tail.drawSelf(tailId, gl);
			           		gl.glPopMatrix();
			       		}
			       	}
		        }
		       	//繪制從方尾巴
		        float followTempData[][]=new float[9][3];
		        synchronized(activity.gdFollowDraw.dataLock)	//鎖上繪制資料
		        {
		        	if(activity.gdFollowDraw.mainTailAl!=null)
		        	{
		        		for(int i=0;i<activity.gdFollowDraw.mainTailAl.size();i++)
		        		{
		        			followTempData=activity.gdFollowDraw.mainTailAl.get(i);
		        			Tail followtail=new Tail(texRect,followTempData);
		        			gl.glPushMatrix();  
			               	gl.glTranslatef(0, 0, -84);
			               	followtail.drawSelf(tailId, gl);
			           		gl.glPopMatrix();
		        		}
		        		if(activity.gdFollowDraw.mainTailAl.size()==1)
		        		{
		        			activity.gdFollowDraw.mainTailAl.remove(0);
		        		}
		        	}
		        }
	            
	            
	            gl.glDisable(GL10.GL_BLEND); //關閉混合
	            gl.glEnable(GL10.GL_DEPTH_TEST); //關閉深度檢驗
	            
	            gl.glEnable(GL10.GL_BLEND);//開啟混合
	            gl.glDisable(GL10.GL_DEPTH_TEST); //開啟深度檢驗
	            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);//設定源混合因子與目的混合因子
	            
	            if(tempanmiStart)
	            {
	   			 if(tempanmiIndex<trExplo.length-1)
	   		    	{//動畫沒有播放完動畫換框
	   				tempanmiIndex=tempanmiIndex+1;
	   		    	}
	   		 	}  
	            //繪制爆炸紋理
	            if(tempanmiStart)
		    	{//動畫沒有播放完動畫換框
	            	gl.glPushMatrix();//保護現場
					gl.glTranslatef(tempanmiX,tempanmiY , -84);//搬移到指定位置
					trExplo[tempanmiIndex].drawSelf(gl);//繪制爆炸動畫目前框
					gl.glPopMatrix();
		    	}
	            
	            if(activity.followanmiStart)
	            {
	            	if(activity.followanmiIndex<trExplo.length-1)
	            	{
	            		activity.followanmiIndex=activity.followanmiIndex+1;
	            	}
	            }
	            if(activity.followanmiStart)
	            {
	            	gl.glPushMatrix();//保護現場
					gl.glTranslatef(activity.followanmiX ,activity.followanmiY, -84);//搬移到指定位置
					trExplo[activity.followanmiIndex].drawSelf(gl);//繪制爆炸動畫目前框
					gl.glPopMatrix();
	            }
	            
	            //繪制坦克生命圖示
	            gl.glPushMatrix();            
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            gl.glLoadIdentity(); 
	            gl.glPushMatrix();
	            gl.glTranslatef(-3.5f, 3f, -5.5f); 
	            if(activity.followtankeflag<1)
	            {
		            tanklife1.drawSelf(gl);
	            }
	            gl.glPopMatrix();
	            gl.glPopMatrix();
	            
	            gl.glPushMatrix();            
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            gl.glLoadIdentity(); 
	            gl.glPushMatrix();
	            gl.glTranslatef(-4.5f, 3f, -5.5f); 
	            if(activity.followtankeflag<2)
	            {
		            tanklife2.drawSelf(gl);
	            }
	            gl.glPopMatrix();
	            gl.glPopMatrix();
	            
	            gl.glPushMatrix();
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            gl.glLoadIdentity(); 
	            gl.glPushMatrix();
	            gl.glTranslatef(-5.5f, 3f, -5.5f); 
	            if(activity.followtankeflag<3)
	            {
		            tanklife3.drawSelf(gl);
	            }
	            gl.glPopMatrix();
	            gl.glPopMatrix();
	            
	            //繪制坦克生命圖示
	            gl.glPushMatrix();            
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            gl.glLoadIdentity(); 
	            gl.glPushMatrix();
	            gl.glTranslatef(3.5f, 3f, -5.5f); 
	            if(TempBullet.tankeflag1<1)
	            {
	            	 tanklife1_1.drawSelf(gl);
	            }
	            gl.glPopMatrix();
	            gl.glPopMatrix();
	            
	            gl.glPushMatrix();            
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            gl.glLoadIdentity(); 
	            gl.glPushMatrix();
	            gl.glTranslatef(4.5f, 3f, -5.5f); 
	            if(TempBullet.tankeflag1<2)
	            {
	            	 tanklife2_1.drawSelf(gl);
	            }
	            gl.glPopMatrix();
	            gl.glPopMatrix();
	            
	            gl.glPushMatrix();
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            gl.glLoadIdentity(); 
	            gl.glPushMatrix();
	            gl.glTranslatef(5.5f, 3f, -5.5f); 
	            if(TempBullet.tankeflag1<3)
	            {
	            	 tanklife3_1.drawSelf(gl);
	            }
	            gl.glPopMatrix();
	            gl.glPopMatrix();
	            
	          //繪制搖桿中心圓片
	            gl.glPushMatrix();            
	            //設定目前矩陣為模式矩陣
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            //設定目前矩陣為單位矩陣 
	            gl.glLoadIdentity(); 
	            gl.glPushMatrix(); 
	            gl.glTranslatef(-4.58f+maintkwzTemp[3]*0.5f, -1.95f+maintkwzTemp[4]*0.5f, -5f); // paid
	            yaoganButton2.drawSelf(gl);
	            gl.glPopMatrix();
	            gl.glPopMatrix();
	            
	            //繪制搖桿背景
	            gl.glPushMatrix();            
	            //設定目前矩陣為模式矩陣
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            //設定目前矩陣為單位矩陣
	            gl.glLoadIdentity(); 
	            gl.glPushMatrix(); 
	            gl.glTranslatef(-2.3f, -1f, -2.5f);  // paid
	            yaoganButton1.drawSelf(gl);
	            gl.glPopMatrix();
	            gl.glPopMatrix();
	            
	            gl.glPushMatrix();            
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            gl.glLoadIdentity(); 
	            if(activity.followtankeflag>=3||TempBullet.tankeflag1>=3)	
				{
					gl.glPushMatrix();
	               	gl.glTranslatef(0,0,-2);       
	               	end.drawSelf(gl);
	               	gl.glPopMatrix();
				}
	            gl.glPopMatrix();
	            
	            gl.glDisable(GL10.GL_BLEND); //關閉混合
	            gl.glEnable(GL10.GL_DEPTH_TEST); //關閉深度檢驗
			}

			@Override
			public void onSurfaceChanged(GL10 gl, int width, int height) {
				//設定視窗大小及位置 
				gl.glViewport(ssr.lucX,ssr.lucY, (int)(SCREEN_WIDTH_STANDARD*ssr.ratio), (int)(SCREEN_HEIGHT_STANDARD*ssr.ratio));
	        	//設定目前矩陣為投影矩陣
	            gl.glMatrixMode(GL10.GL_PROJECTION);
	            //設定目前矩陣為單位矩陣
	            gl.glLoadIdentity();
	            //計算透視投影的比例
	            float ratio = RATIO;
	            //呼叫此方法計算產生透視投影矩陣
	            gl.glFrustumf(-ratio, ratio, -1, 1, 1.5f, 400);
				
			}

			@Override
			public void onSurfaceCreated(GL10 gl, EGLConfig config) {
				//關閉抗抖動 
	        	gl.glDisable(GL10.GL_DITHER);
	        	//設定特定Hint專案的模式，這裡為設定為使用快速模式
	            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);
	            //設定螢幕背景色黑色RGBA
	            gl.glClearColor(0,0,0,0);
	            //開啟背面剪裁
	            gl.glEnable(GL10.GL_CULL_FACE);
	            //開啟混合   
	            gl.glEnable(GL10.GL_BLEND); 
	            //設定源混合因子與目的混合因子
	            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	            //設定著色模型為平順著色   
	            gl.glShadeModel(GL10.GL_SMOOTH);//GL10.GL_SMOOTH  GL10.GL_FLAT
	            //啟用深度測試
	            gl.glEnable(GL10.GL_DEPTH_TEST);
	            //起始化燈光
	    		initLight(gl);
	            //起始化材質
	            initMaterial(gl);
	            //容許光源       
	            gl.glEnable(GL10.GL_LIGHTING);
	            //設定光線方向
//	            float[] positionParamsGreen={-80,80,80,1};//最後的0表示是定位光
//	            float[] positionParamsGreen={0,0,90,1};//最後的0表示是定位光
	            float[] directionParams={0,0,1,0};//最後的0表示是定位光
	            //平行光
	            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, directionParams,0); 
				
	            
	            texRect=new DrawBuffer(OtherSurfaceView.this); 
	            
	            //起始化地板紋理
	            floorTexId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "floor1.png"));
	            //起始化牆紋理
	            wallTexId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "wall.png"));
	            //起始化坦克1紋理
	            tankId1=initTexture(gl,PicLoadUtil.loadBM(getResources(), "mini.png")); 
	            //起始化坦克2紋理
	            tankId2=initTexture(gl,PicLoadUtil.loadBM(getResources(), "mini1.png"));  
	            //起始化搖桿紋理
	            yaoganTextureId1=initTexture(gl,PicLoadUtil.loadBM(getResources(), "yaogan1.png"));
	            yaoganTextureId2=initTexture(gl,PicLoadUtil.loadBM(getResources(), "yaogan2.png"));
	            //坦克生命紋理
	            tanklifeId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "tanklife.png")); 
	            tanklifeId_1=initTexture(gl,PicLoadUtil.loadBM(getResources(), "tanklife1.png")); 
	            //起始化子彈紋理
	            bulletId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "wall.png"));  
	            //起始化尾巴紋理
	            tailId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "weiba.png"));
	            //起始化爆炸紋理
	            exploTextureId[0]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode1.png"));  
	            exploTextureId[1]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode2.png"));  
	            exploTextureId[2]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode3.png"));  
	            exploTextureId[3]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode4.png"));  
	            exploTextureId[4]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode5.png"));  
	            exploTextureId[5]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode6.png")); 
	            exploTextureId[6]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode7.png"));  
	            exploTextureId[7]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode8.png"));  
	            exploTextureId[8]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode9.png"));  
	            exploTextureId[9]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode10.png"));  
	            exploTextureId[10]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode11.png"));  
	            exploTextureId[11]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode12.png")); 
	            exploTextureId[12]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode13.png"));  
	            exploTextureId[13]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode14.png"));  
	            exploTextureId[14]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode15.png"));  
	            exploTextureId[15]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode16.png"));  
	            exploTextureId[16]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode17.png"));  
	            exploTextureId[17]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode18.png")); 
	            exploTextureId[18]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode19.png"));  
	            exploTextureId[19]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode20.png"));  
	            exploTextureId[20]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode21.png"));  
	            exploTextureId[21]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode22.png"));  
	            exploTextureId[22]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode23.png"));  
	            exploTextureId[23]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode24.png")); 
	            exploTextureId[24]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode25.png"));  
	            exploTextureId[25]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode26.png"));  
	            exploTextureId[26]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode27.png"));  
	            exploTextureId[27]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode28.png"));  
	            exploTextureId[28]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode29.png"));  
	            exploTextureId[29]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode30.png")); 
	            exploTextureId[30]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode31.png"));  
	            exploTextureId[31]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode32.png"));  
	            exploTextureId[32]=initTexture(gl,PicLoadUtil.loadBM(getResources(), "explode33.png"));  
	            //結束紋理
	            endId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "end.png")); 
	           
	            //繪制地板
	            floor=new Floor(-3f,2f,6f,4f,0, floorTexId);
	            //繪制矮牆
	            lowwall=new LowWall(wallTexId);
	            //繪制中牆
	            middlewall=new MiddleWall(wallTexId);
	            //繪制高牆
	            highwall=new HighWall(wallTexId);
	           //繪制子彈模型
	            bullet=new BulletTextureByVertex(3,9,18,bulletId);
	            bullet1[0]=new BulletTextureByVertex(3,9,18,bulletId);
	            bullet1[1]=new BulletTextureByVertex(3,9,18,bulletId);
	            bullet1[2]=new BulletTextureByVertex(3,9,18,bulletId);
	            bullet1[3]=new BulletTextureByVertex(3,9,18,bulletId);
	            followbullet[0]=new BulletTextureByVertex(3,9,18,bulletId);
	            followbullet[1]=new BulletTextureByVertex(3,9,18,bulletId);
	            followbullet[2]=new BulletTextureByVertex(3,9,18,bulletId);
	            followbullet[3]=new BulletTextureByVertex(3,9,18,bulletId);
	            
	           //坦克炮身
	            lovn1=LoadUtil.loadFromFileVertexOnly
	                    (
	                   		 "tank_body.obj", 
	                   		OtherSurfaceView.this.getResources(),
	                   		tankId1
	                    );
	            //坦克炮管
	            lovn2=LoadUtil.loadFromFileVertexOnly
	                    (
	                    	"tank_berral1.obj", 
	                    	OtherSurfaceView.this.getResources(),
	                    	tankId1
	                    );
	            //坦克炮身
	            lovn3=LoadUtil.loadFromFileVertexOnly
	                    (
	                   		 "tank_body.obj", 
	                   		OtherSurfaceView.this.getResources(),
	                   		tankId2
	                    );
	            //坦克炮管
	            lovn4=LoadUtil.loadFromFileVertexOnly
	                    (
	                    	"tank_berral1.obj", 
	                    	OtherSurfaceView.this.getResources(),
	                    	tankId2
	                    );
	            //建立搖桿
	            yaoganButton1=new TextureRect(yaoganTextureId1,0.12f,0.12f,1,1);
	            yaoganButton2=new TextureRect(yaoganTextureId2,0.12f,0.12f,1,1);
	            //建立坦克生命圖示
	            tanklife1=new TextureRect(tanklifeId,0.12f,0.12f,1,1);
	            tanklife2=new TextureRect(tanklifeId,0.12f,0.12f,1,1);
	            tanklife3=new TextureRect(tanklifeId,0.12f,0.12f,1,1);
	            tanklife1_1=new TextureRect(tanklifeId_1,0.12f,0.12f,1,1);
	            tanklife2_1=new TextureRect(tanklifeId_1,0.12f,0.12f,1,1);
	            tanklife3_1=new TextureRect(tanklifeId_1,0.12f,0.12f,1,1);
	            end=new TextureRect(endId,0.7f,0.7f,1,1);
	            //建立爆炸動畫群組成框
	            trExplo[0]=new TextureRect(exploTextureId[0],6f,6f,1,1);
	            trExplo[1]=new TextureRect(exploTextureId[1],6f,6f,1,1);
	            trExplo[2]=new TextureRect(exploTextureId[2],6f,6f,1,1);
	            trExplo[3]=new TextureRect(exploTextureId[3],6f,6f,1,1);
	            trExplo[4]=new TextureRect(exploTextureId[4],6f,6f,1,1);
	            trExplo[5]=new TextureRect(exploTextureId[5],6f,6f,1,1);
	            trExplo[6]=new TextureRect(exploTextureId[6],6f,6f,1,1);
	            trExplo[7]=new TextureRect(exploTextureId[7],6f,6f,1,1);
	            trExplo[8]=new TextureRect(exploTextureId[8],6f,6f,1,1);
	            trExplo[9]=new TextureRect(exploTextureId[9],6f,6f,1,1);
	            trExplo[10]=new TextureRect(exploTextureId[10],6f,6f,1,1);
	            trExplo[11]=new TextureRect(exploTextureId[11],6f,6f,1,1);
	            trExplo[12]=new TextureRect(exploTextureId[12],6f,6f,1,1);
	            trExplo[13]=new TextureRect(exploTextureId[13],6f,6f,1,1);
	            trExplo[14]=new TextureRect(exploTextureId[14],6f,6f,1,1);
	            trExplo[15]=new TextureRect(exploTextureId[15],6f,6f,1,1);
	            trExplo[16]=new TextureRect(exploTextureId[16],6f,6f,1,1);
	            trExplo[17]=new TextureRect(exploTextureId[17],6f,6f,1,1);
	            trExplo[18]=new TextureRect(exploTextureId[18],6f,6f,1,1);
	            trExplo[19]=new TextureRect(exploTextureId[19],6f,6f,1,1);
	            trExplo[20]=new TextureRect(exploTextureId[20],6f,6f,1,1);
	            trExplo[21]=new TextureRect(exploTextureId[21],6f,6f,1,1);
	            trExplo[22]=new TextureRect(exploTextureId[22],6f,6f,1,1);
	            trExplo[23]=new TextureRect(exploTextureId[23],6f,6f,1,1);
	            trExplo[24]=new TextureRect(exploTextureId[24],6f,6f,1,1);
	            trExplo[25]=new TextureRect(exploTextureId[25],6f,6f,1,1);
	            trExplo[26]=new TextureRect(exploTextureId[26],6f,6f,1,1);
	            trExplo[27]=new TextureRect(exploTextureId[27],6f,6f,1,1);
	            trExplo[28]=new TextureRect(exploTextureId[28],6f,6f,1,1);
	            trExplo[29]=new TextureRect(exploTextureId[29],6f,6f,1,1);
	            trExplo[30]=new TextureRect(exploTextureId[30],6f,6f,1,1);
	            trExplo[31]=new TextureRect(exploTextureId[31],6f,6f,1,1);
	            trExplo[32]=new TextureRect(exploTextureId[32],6f,6f,1,1);
			
			}
		}
		private void initLight(GL10 gl)
		{
	        gl.glEnable(GL10.GL_LIGHT0);//開啟0號燈  
	        
	        //環境光設定
	        float[] ambientParams={0.2f,0.2f,0.2f,1.0f};//光參數 RGBA
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams,0);            

	        //散射光設定
	        float[] diffuseParams={1f,1f,1f,1.0f};//光參數 RGBA
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams,0); 
	        
	        //反射光設定
	        float[] specularParams={0.1f,0.1f,0.1f,1.0f};//光參數 RGBA
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams,0);     
		}
		
		private void initMaterial(GL10 gl)
		{//材質為白色時什麼彩色的光源在上面就將表現出什麼彩色
	        //環境光為白色材質
	        float ambientMaterial[] = {0.4f, 0.4f, 0.4f, 1.0f};
	        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientMaterial,0);
	        //散射光為白色材質
	        float diffuseMaterial[] = {1f, 1f, 1f, 1.0f};
	        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterial,0);
	        //高光材質為白色
	        float specularMaterial[] = {0.5f, 0.5f, 0.5f, 1.0f};
	        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specularMaterial,0);
	        //高光反射區域,數越大反白區域越小越暗
	        float shininessMaterial[] = {0.5f};
	        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, shininessMaterial,0);
		}
		public int initTexture(GL10 gl,Bitmap bitmap)//textureId
		{
			//產生紋理ID
			int textureID;
			int[] textures = new int[1];
			gl.glGenTextures
			(
					1,          //產生的紋理id的數量
					textures,   //紋理id的陣列
					0           //偏移量
			);    
			textureID=textures[0];    
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
	        
	        //實際載入紋理
	        GLUtils.texImage2D
	        (
	        		GL10.GL_TEXTURE_2D,   //紋理型態，在OpenGL ES中必須為GL10.GL_TEXTURE_2D
	        		0, 					  //紋理的階層，0表示基本圖形層，可以瞭解為直接貼圖
	        		bitmap, 			  //紋理圖形
	        		0					  //紋理邊框尺寸
	        );
	        return textureID;
		}
		public void fire()
		 {
			 float startAngle;
			 float startX;
			 float startY;
			 float startZ;
			 synchronized(gdMainDraw.dataLock)	//鎖上繪制資料
			 {
				 startAngle=-gdMainDraw.followtkfp[1];
				 startX=gdMainDraw.followtkwz[0]+(float) (10*Math.sin(Math.toRadians(startAngle)));
				 startY=gdMainDraw.followtkwz[1]+(float) (10*Math.cos(Math.toRadians(startAngle)));
			 }
			 startZ=-84;
			 
			 float vx=(float) (BULLET_V*Math.sin(Math.toRadians(startAngle)));
			 float vy=(float) (BULLET_V*Math.cos(Math.toRadians(startAngle)));
			 float vz=0;
			 TempBullet tbl= new TempBullet(mRenderer.bullet,startX,startY,startZ,vx,vy,vz,mRenderer.trExplo,startAngle,this,mainbulletflag);
			 tempbulletAl.add(
					 tbl
					 );
		 }
}
