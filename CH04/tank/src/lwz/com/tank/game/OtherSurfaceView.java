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
	public TempBulletGoThread tempbgt;//�w�ɷh�����u�������
	private SceneRenderer mRenderer;//�����ۦ⾹
	
	public int floorTexId;//�a�O���zID
	public int wallTexId;//�𯾲zID
	public int tanklifeId;//�Z�J�ͩR���z
	public int tanklifeId_1;//�Z�J�ͩR���z
	public int yaoganTextureId1;//�n��1
	public int yaoganTextureId2;//�n��2
	public int tankId1;//�Z�J���zID
	public int tankId2;//�Z�J���zID
	public int endId;//�������z
	int[] exploTextureId=new int[33];//�z�����z�s��
	public int bulletId;//�l�u���zID
	public int tailId;//�l�u���گ��zID
	 
	//�Z�J2���_�l��m
	public static float Tank2_x=0;
	public static float Tank2_y=100;
	//�n�줤���I�����y�ФΥb�|
	public  float INIT_x=(115+ssr.lucX)*ssr.ratio;
	public  float INIT_y=(425+ssr.lucY)*ssr.ratio;
	public  float INIT_R=100*(SCREEN_WIDTH-ssr.lucX*ssr.ratio*2)/SCREEN_WIDTH_STANDARD;
	
    public Object aqLock=new Object();	//�ʧ@��C��
	public Queue<Action> aq=new LinkedList<Action>();//�ʧ@��C
	DoOtherActionThread doat;					//����ʧ@������Ѧ�
	float touchflag;//Ĳ���ƥ�ЧӦ�
	
	//�s���I��HashMap
	HashMap<Integer,BNPoint> hm=new HashMap<Integer,BNPoint>();
	//�إߤ@��BNPoint
	BNPoint bp;
	static int countl=0;
	//�D���
	GameData gdMain=new GameData();
	//�Dø����
	GameData gdMainDraw=new GameData();
	//�qø����
	GameData gdFollowDraw=new GameData();
	
	 public  ArrayList<TempBullet> tempbulletAl=new ArrayList<TempBullet>();//���u�M��
	 DrawBuffer texRect;
	
	 public int mainbulletflag=0;//�D�l�u�ЧӦ�
	 public int followbulletflag=0;//�q�l�u�ЧӦ�
	 public  int bulletCount=0;//�l�u�ƶq
	 
	public OtherSurfaceView(MainActivity activity) {
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);
		mRenderer=new SceneRenderer();
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		doat=new DoOtherActionThread(OtherSurfaceView.this);//�إ߰���ʧ@�����
		doat.start();//�Ұʰ����
		ActionThread at=new ActionThread();//�إߦ�C�ʧ@��������
		at.start();//�Ұʰ����
		tempbgt=new TempBulletGoThread(tempbulletAl,this);//�إߩw�ɷh�����u�������
		tempbgt.start();//�Ұʦ������
	}
	//Ĳ�N�ƥ�^�դ�k
		@Override
		public boolean onTouchEvent(MotionEvent e)
		{
			//���oĲ�����ʧ@�s��
			int action=e.getAction()&MotionEvent.ACTION_MASK;
			//���o�D�B���Iid�]down�ɥD���Iid�ҥ��T�Aup�ɻ��Iid���T�A�D�Iid�n�d��Map���ѤU���@���I��id�^
			int id=(e.getAction()&MotionEvent.ACTION_POINTER_ID_MASK)>>>MotionEvent.ACTION_POINTER_ID_SHIFT;	
			switch(action)									//>>>���N��O�L���k��
			{
				case MotionEvent.ACTION_DOWN: //�D�Idown 	
					if(activity.followtankeflag<3&&TempBullet.tankeflag1<3)	
					{
						//�VMap���O���@�ӷs�I
						hm.put(id, new BNPoint(e.getX(id),e.getY(id),countl++));
					}
				break;
				case MotionEvent.ACTION_POINTER_DOWN: //���Idown	
//					if(TempBullet.tankeflag2<3&&TempBullet.tankeflag1<3)	
					if(activity.followtankeflag<3&&TempBullet.tankeflag1<3)	
					{
						if(id<e.getPointerCount()-1)
						{
							//�N�s�����ᶶ�]�۷���I�Ƨǡ^
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
						//�VMap���O���@�ӷs�I
						hm.put(id, new BNPoint(e.getX(id),e.getY(id),countl++));
					}
				break;
				case MotionEvent.ACTION_MOVE: //�D/���Imove  
					if(activity.followtankeflag<3&&TempBullet.tankeflag1<3)	
					{
						//���ץD/���IMove����s���m
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
				case MotionEvent.ACTION_POINTER_UP: //���Iup	
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
				    					ActionType.FIRE_ACTION,		//�ʧ@���A
										new float[]{hm.get(id).getx(),hm.get(id).gety()}			//�ʧ@���
									);
				    				synchronized(aqLock)			//��W�ʧ@��C
									{
										aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
									}
								}
			        		}
							if(hm.get(id).gettouchflag()==1)
							{
								Action acTemp = new Action
		    					(
		        					ActionType.MOVE_ACTION,	//�ʧ@���A
		    						new float[]{INIT_x,INIT_y}			//�ʧ@���
		    					);
		        				synchronized(aqLock)			//��W�ʧ@��C
		    					{
		    						aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
		    					}
							}
							//�qMap����������id�����I
							hm.remove(id);
							//�N�s�����e���A���ŵ�
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
				case MotionEvent.ACTION_UP: //�D�Iup
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
				    					ActionType.FIRE_ACTION,		//�ʧ@���A
										new float[]{hm.get(id).getx(),hm.get(id).gety()}			//�ʧ@���
									);
				    				synchronized(aqLock)			//��W�ʧ@��C
									{
										aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
									}
								}
			        		}
							if(hm.get(id).gettouchflag()==1)
							{
								Action acTemp = new Action
		    					(
		        					ActionType.MOVE_ACTION,	//�ʧ@���A
		    						new float[]{INIT_x,INIT_y}			//�ʧ@���
		    					);
		        				synchronized(aqLock)			//��W�ʧ@��C
		    					{
		    						aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
		    					}
							}
							//�b���M�Τ��D�IUP�h�u�ݭn�M�zMap�Y�i�A�b��L�@�ǮM�Τ��ݭn�ʧ@��
							//�h���XMap���ߤ@�ѤU���I�ʧ@�Y�i
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
	        				synchronized(gdMain.dataLock) 	//�N�D�����W
	        				{
	        					gdMain.followtkwz[0]=0;
	        					gdMain.followtkwz[1]=100;
	    						gdMain.followtkwz[2]=0;
	        					gdMain.followtkwz[3]=0;
	    						gdMain.followtkwz[4]=0;
	    						gdMain.followtkfp[0]=0;
	        					synchronized(gdMainDraw.dataLock) //�Nø������W
	        					{
	        						gdMainDraw.followtkwz[0]=0;
	        						gdMainDraw.followtkwz[1]=100;
	        						gdMainDraw.followtkwz[2]=0;
	        						gdMainDraw.followtkwz[3]=0;
	        						gdMainDraw.followtkwz[4]=0;
	        						gdMainDraw.followtkfp[0]=0;
	        					}
	        				}
	        				synchronized(activity.gdFollowDraw.dataLock)	//��Wø����
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
	        				synchronized(gdMain.dataLock) 	//�N�D�����W
	        				{
	        					gdMain.followtkwz[0]=0;
	        					gdMain.followtkwz[1]=100;
	    						gdMain.followtkwz[2]=0;
	        					gdMain.followtkwz[3]=0;
	    						gdMain.followtkwz[4]=0;
	    						gdMain.followtkfp[0]=0;
	        					synchronized(gdMainDraw.dataLock) //�Nø������W
	        					{
	        						gdMainDraw.followtkwz[0]=0;
	        						gdMainDraw.followtkwz[1]=100;
	        						gdMainDraw.followtkwz[2]=0;
	        						gdMainDraw.followtkwz[3]=0;
	        						gdMainDraw.followtkwz[4]=0;
	        						gdMainDraw.followtkfp[0]=0;
	        					}
	        				}
	        				synchronized(activity.gdFollowDraw.dataLock)	//��Wø����
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
					//���omap�����
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
		            					ActionType.MOVE_ACTION,		//�ʧ@���A
		        						new float[]{bp.getx(),bp.gety()}			//�ʧ@���
		        					);
		            				synchronized(aqLock)			//��W�ʧ@��C
		        					{
		        						aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
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
			Floor floor;//�_�l�Ʀa�O
			LowWall lowwall;//�_�l�ƸG��
			MiddleWall middlewall;//�_�l�Ƥ���
			HighWall highwall;//�_�l�ư���
			TextureRect tanklife1;//�Z�J�ͩR1
			TextureRect tanklife1_1;//�Z�J�ͩR1
			TextureRect tanklife2;//�Z�J�ͩR2
			TextureRect tanklife2_1;//�Z�J�ͩR2
			TextureRect tanklife3;//�Z�J�ͩR3
			TextureRect tanklife3_1;//�Z�J�ͩR2
			TextureRect yaoganButton1;//�n����s1
			TextureRect yaoganButton2;//�n����s2
			BulletTextureByVertex bullet;//�_�l�Ƥl�u
			BulletTextureByVertex []bullet1=new BulletTextureByVertex[4];//�_�l�Ƥl�u
			BulletTextureByVertex followbullet[]=new BulletTextureByVertex[4];//�_�l�Ƥl�u
			TextureRect[] trExplo=new TextureRect[33];//�z���ʵe
			TextureRect end;//�����ɭ�
			
			//�q�S��obj�ɮפ����J����
			LoadedObjectVertexNormalTexture lovn1;
			LoadedObjectVertexNormalTexture lovn2;
			
			LoadedObjectVertexNormalTexture lovn3;
			LoadedObjectVertexNormalTexture lovn4;

			@Override
			public void onDrawFrame(GL10 gl) {
				//�M���m��֨�
	        	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	        	//�]�w�ثe�x�}���Ҧ��x�}
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            //�]�w�ثe�x�}�����x�}
	            gl.glLoadIdentity();  
	            
	            GLU.gluLookAt
	            (
	            	gl, 
	            	0,0,200f,
	            	0,0,0f,
	            	0,1,0	
	            ); 
	            
	            float maintkwzTemp[]=new float[5];
	            synchronized(gdMainDraw.dataLock)	//��Wø����
	            {
	            	maintkwzTemp[0]=gdMainDraw.followtkwz[0];
	            	maintkwzTemp[1]=gdMainDraw.followtkwz[1];
	            	maintkwzTemp[2]=gdMainDraw.followtkwz[2];
	            	maintkwzTemp[3]=gdMainDraw.followtkwz[3];
	            	maintkwzTemp[4]=gdMainDraw.followtkwz[4];
	            }
	            float followtkwzTemp[]=new float[5];
	            synchronized(activity.gdFollowDraw.dataLock)	//��Wø����
	            {
	            	followtkwzTemp[0]=activity.gdFollowDraw.maintkwz[0];
	            	followtkwzTemp[1]=activity.gdFollowDraw.maintkwz[1];
	            	followtkwzTemp[2]=activity.gdFollowDraw.maintkwz[2];
	            	followtkwzTemp[3]=activity.gdFollowDraw.maintkwz[3];
	            	followtkwzTemp[4]=activity.gdFollowDraw.maintkwz[4];
	            }
	            
	            float maintkfpTemp[]=new float[2];
	            synchronized(gdMainDraw.dataLock)	//��Wø����
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
	            synchronized(activity.gdFollowDraw.dataLock)	//��Wø����
	            {
	            	followtkfpTemp[0]=activity.gdFollowDraw.maintkfp[0];
	            	followtkfpTemp[1]=activity.gdFollowDraw.maintkfp[1];
	            }
	            
	            //ø��a�O
	            gl.glPushMatrix();
	            gl.glTranslatef(0, 0, -95f);  
	            floor.drawSelf(gl);
	            gl.glPopMatrix();
	            
	            //ø����
	            gl.glPushMatrix();
	            gl.glTranslatef(0, 0f, -100f); 
	            lowwall.drawSelf(gl);
	            gl.glPopMatrix();
	            //ø����
	            gl.glPushMatrix();
	            gl.glTranslatef(0, 0f, -100f); 
	            middlewall.drawSelf(gl);
	            gl.glPopMatrix();
	            //ø����
	            gl.glPushMatrix();
	            gl.glTranslatef(0, 0f, -100f); 
	            highwall.drawSelf(gl);
	            gl.glPopMatrix();
	            
	            if(activity.followtankeflag<3)
	            {
	            	//ø��Z�J1
					if(lovn1!=null)
		            {
		            	gl.glPushMatrix();
		            	gl.glTranslatef(followtkwzTemp[0], followtkwzTemp[1], -100);
		            	gl.glRotatef(90,1, 0, 0);       	          
		            	gl.glRotatef(followtkwzTemp[2],0, 1, 0);       
		            	lovn1.drawSelf(gl);
		            	gl.glPopMatrix();	
		            } 
		            //ø���
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
	            	//ø��Z�J2
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
	            synchronized(gdMainDraw.dataLock)	//��Wø����
	            {
	            	if(gdMainDraw.followbulletAl!=null)
	            	{
	            		for(int i=0;i<gdMainDraw.followbulletAl.size();i++)
	                	{
	            			mainbulletTemp=gdMainDraw.followbulletAl.get(i);
	            			if(mainbulletTemp!=null)
	            			{
	            				gl.glPushMatrix();//�O�@�{��
		    	    			gl.glTranslatef(mainbulletTemp[0], mainbulletTemp[1], mainbulletTemp[2]);//�h������w��m
		    	    			gl.glRotatef(-90, 1, 0, 0);
		    	    			gl.glRotatef(mainbulletTemp[3], 0, 1, 0);
		    	    			//�Y�z���ʵe�S���}�l�A�з�ø��u
		    	    			bullet1[i].drawSelf(gl);
		    	    			gl.glPopMatrix();
	            			}
	                	}
	            	}
	            	
	            }
	            
	            float followbulletTemp[]=new float[4];
	            synchronized(activity.gdFollowDraw.dataLock)	//��Wø����
	            {
	            	if(activity.gdFollowDraw.mainbulletAl!=null)
	            	{
	            		for(int i=0;i<activity.gdFollowDraw.mainbulletAl.size();i++)
	            		{
	            			followbulletTemp=activity.gdFollowDraw.mainbulletAl.get(i);
	            			if(followbulletTemp!=null)
	            			{
	            				gl.glPushMatrix();//�O�@�{��
		    	    			gl.glTranslatef(followbulletTemp[0], followbulletTemp[1], followbulletTemp[2]);//�h������w��m
		    	    			gl.glRotatef(-90, 1, 0, 0);
		    	    			gl.glRotatef(followbulletTemp[3], 0, 1, 0);
		    	    			//�Y�z���ʵe�S���}�l�A�з�ø��u
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
	           
	            gl.glEnable(GL10.GL_BLEND);//�}�ҲV�X
	            gl.glDisable(GL10.GL_DEPTH_TEST); //�}�Ҳ`������
	            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);//�]�w���V�X�]�l�P�ت��V�X�]�l

	            //ø��D�����
	            float TempData[][] = new float[9][3];
		        synchronized(gdMainDraw.dataLock)	//��Wø����
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
		       	//ø��q�����
		        float followTempData[][]=new float[9][3];
		        synchronized(activity.gdFollowDraw.dataLock)	//��Wø����
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
	            
	            
	            gl.glDisable(GL10.GL_BLEND); //�����V�X
	            gl.glEnable(GL10.GL_DEPTH_TEST); //�����`������
	            
	            gl.glEnable(GL10.GL_BLEND);//�}�ҲV�X
	            gl.glDisable(GL10.GL_DEPTH_TEST); //�}�Ҳ`������
	            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);//�]�w���V�X�]�l�P�ت��V�X�]�l
	            
	            if(tempanmiStart)
	            {
	   			 if(tempanmiIndex<trExplo.length-1)
	   		    	{//�ʵe�S�����񧹰ʵe����
	   				tempanmiIndex=tempanmiIndex+1;
	   		    	}
	   		 	}  
	            //ø���z�����z
	            if(tempanmiStart)
		    	{//�ʵe�S�����񧹰ʵe����
	            	gl.glPushMatrix();//�O�@�{��
					gl.glTranslatef(tempanmiX,tempanmiY , -84);//�h������w��m
					trExplo[tempanmiIndex].drawSelf(gl);//ø���z���ʵe�ثe��
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
	            	gl.glPushMatrix();//�O�@�{��
					gl.glTranslatef(activity.followanmiX ,activity.followanmiY, -84);//�h������w��m
					trExplo[activity.followanmiIndex].drawSelf(gl);//ø���z���ʵe�ثe��
					gl.glPopMatrix();
	            }
	            
	            //ø��Z�J�ͩR�ϥ�
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
	            
	            //ø��Z�J�ͩR�ϥ�
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
	            
	          //ø��n�줤�߶��
	            gl.glPushMatrix();            
	            //�]�w�ثe�x�}���Ҧ��x�}
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            //�]�w�ثe�x�}�����x�} 
	            gl.glLoadIdentity(); 
	            gl.glPushMatrix(); 
	            gl.glTranslatef(-4.58f+maintkwzTemp[3]*0.5f, -1.95f+maintkwzTemp[4]*0.5f, -5f); // paid
	            yaoganButton2.drawSelf(gl);
	            gl.glPopMatrix();
	            gl.glPopMatrix();
	            
	            //ø��n��I��
	            gl.glPushMatrix();            
	            //�]�w�ثe�x�}���Ҧ��x�}
	            gl.glMatrixMode(GL10.GL_MODELVIEW);
	            //�]�w�ثe�x�}�����x�}
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
	            
	            gl.glDisable(GL10.GL_BLEND); //�����V�X
	            gl.glEnable(GL10.GL_DEPTH_TEST); //�����`������
			}

			@Override
			public void onSurfaceChanged(GL10 gl, int width, int height) {
				//�]�w�����j�p�Φ�m 
				gl.glViewport(ssr.lucX,ssr.lucY, (int)(SCREEN_WIDTH_STANDARD*ssr.ratio), (int)(SCREEN_HEIGHT_STANDARD*ssr.ratio));
	        	//�]�w�ثe�x�}����v�x�}
	            gl.glMatrixMode(GL10.GL_PROJECTION);
	            //�]�w�ثe�x�}�����x�}
	            gl.glLoadIdentity();
	            //�p��z����v�����
	            float ratio = RATIO;
	            //�I�s����k�p�ⲣ�ͳz����v�x�}
	            gl.glFrustumf(-ratio, ratio, -1, 1, 1.5f, 400);
				
			}

			@Override
			public void onSurfaceCreated(GL10 gl, EGLConfig config) {
				//�����ܧݰ� 
	        	gl.glDisable(GL10.GL_DITHER);
	        	//�]�w�S�wHint�M�ת��Ҧ��A�o�̬��]�w���ϥΧֳt�Ҧ�
	            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);
	            //�]�w�ù��I����¦�RGBA
	            gl.glClearColor(0,0,0,0);
	            //�}�ҭI���ŵ�
	            gl.glEnable(GL10.GL_CULL_FACE);
	            //�}�ҲV�X   
	            gl.glEnable(GL10.GL_BLEND); 
	            //�]�w���V�X�]�l�P�ت��V�X�]�l
	            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	            //�]�w�ۦ�ҫ��������ۦ�   
	            gl.glShadeModel(GL10.GL_SMOOTH);//GL10.GL_SMOOTH  GL10.GL_FLAT
	            //�ҥβ`�״���
	            gl.glEnable(GL10.GL_DEPTH_TEST);
	            //�_�l�ƿO��
	    		initLight(gl);
	            //�_�l�Ƨ���
	            initMaterial(gl);
	            //�e�\����       
	            gl.glEnable(GL10.GL_LIGHTING);
	            //�]�w���u��V
//	            float[] positionParamsGreen={-80,80,80,1};//�̫᪺0��ܬO�w���
//	            float[] positionParamsGreen={0,0,90,1};//�̫᪺0��ܬO�w���
	            float[] directionParams={0,0,1,0};//�̫᪺0��ܬO�w���
	            //�����
	            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, directionParams,0); 
				
	            
	            texRect=new DrawBuffer(OtherSurfaceView.this); 
	            
	            //�_�l�Ʀa�O���z
	            floorTexId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "floor1.png"));
	            //�_�l���𯾲z
	            wallTexId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "wall.png"));
	            //�_�l�ƩZ�J1���z
	            tankId1=initTexture(gl,PicLoadUtil.loadBM(getResources(), "mini.png")); 
	            //�_�l�ƩZ�J2���z
	            tankId2=initTexture(gl,PicLoadUtil.loadBM(getResources(), "mini1.png"));  
	            //�_�l�Ʒn�쯾�z
	            yaoganTextureId1=initTexture(gl,PicLoadUtil.loadBM(getResources(), "yaogan1.png"));
	            yaoganTextureId2=initTexture(gl,PicLoadUtil.loadBM(getResources(), "yaogan2.png"));
	            //�Z�J�ͩR���z
	            tanklifeId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "tanklife.png")); 
	            tanklifeId_1=initTexture(gl,PicLoadUtil.loadBM(getResources(), "tanklife1.png")); 
	            //�_�l�Ƥl�u���z
	            bulletId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "wall.png"));  
	            //�_�l�Ƨ��گ��z
	            tailId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "weiba.png"));
	            //�_�l���z�����z
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
	            //�������z
	            endId=initTexture(gl,PicLoadUtil.loadBM(getResources(), "end.png")); 
	           
	            //ø��a�O
	            floor=new Floor(-3f,2f,6f,4f,0, floorTexId);
	            //ø��G��
	            lowwall=new LowWall(wallTexId);
	            //ø���
	            middlewall=new MiddleWall(wallTexId);
	            //ø���
	            highwall=new HighWall(wallTexId);
	           //ø��l�u�ҫ�
	            bullet=new BulletTextureByVertex(3,9,18,bulletId);
	            bullet1[0]=new BulletTextureByVertex(3,9,18,bulletId);
	            bullet1[1]=new BulletTextureByVertex(3,9,18,bulletId);
	            bullet1[2]=new BulletTextureByVertex(3,9,18,bulletId);
	            bullet1[3]=new BulletTextureByVertex(3,9,18,bulletId);
	            followbullet[0]=new BulletTextureByVertex(3,9,18,bulletId);
	            followbullet[1]=new BulletTextureByVertex(3,9,18,bulletId);
	            followbullet[2]=new BulletTextureByVertex(3,9,18,bulletId);
	            followbullet[3]=new BulletTextureByVertex(3,9,18,bulletId);
	            
	           //�Z�J����
	            lovn1=LoadUtil.loadFromFileVertexOnly
	                    (
	                   		 "tank_body.obj", 
	                   		OtherSurfaceView.this.getResources(),
	                   		tankId1
	                    );
	            //�Z�J����
	            lovn2=LoadUtil.loadFromFileVertexOnly
	                    (
	                    	"tank_berral1.obj", 
	                    	OtherSurfaceView.this.getResources(),
	                    	tankId1
	                    );
	            //�Z�J����
	            lovn3=LoadUtil.loadFromFileVertexOnly
	                    (
	                   		 "tank_body.obj", 
	                   		OtherSurfaceView.this.getResources(),
	                   		tankId2
	                    );
	            //�Z�J����
	            lovn4=LoadUtil.loadFromFileVertexOnly
	                    (
	                    	"tank_berral1.obj", 
	                    	OtherSurfaceView.this.getResources(),
	                    	tankId2
	                    );
	            //�إ߷n��
	            yaoganButton1=new TextureRect(yaoganTextureId1,0.12f,0.12f,1,1);
	            yaoganButton2=new TextureRect(yaoganTextureId2,0.12f,0.12f,1,1);
	            //�إߩZ�J�ͩR�ϥ�
	            tanklife1=new TextureRect(tanklifeId,0.12f,0.12f,1,1);
	            tanklife2=new TextureRect(tanklifeId,0.12f,0.12f,1,1);
	            tanklife3=new TextureRect(tanklifeId,0.12f,0.12f,1,1);
	            tanklife1_1=new TextureRect(tanklifeId_1,0.12f,0.12f,1,1);
	            tanklife2_1=new TextureRect(tanklifeId_1,0.12f,0.12f,1,1);
	            tanklife3_1=new TextureRect(tanklifeId_1,0.12f,0.12f,1,1);
	            end=new TextureRect(endId,0.7f,0.7f,1,1);
	            //�إ��z���ʵe�s�զ���
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
	        gl.glEnable(GL10.GL_LIGHT0);//�}��0���O  
	        
	        //���ҥ��]�w
	        float[] ambientParams={0.2f,0.2f,0.2f,1.0f};//���Ѽ� RGBA
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams,0);            

	        //���g���]�w
	        float[] diffuseParams={1f,1f,1f,1.0f};//���Ѽ� RGBA
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams,0); 
	        
	        //�Ϯg���]�w
	        float[] specularParams={0.1f,0.1f,0.1f,1.0f};//���Ѽ� RGBA
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams,0);     
		}
		
		private void initMaterial(GL10 gl)
		{//���謰�զ�ɤ���m�⪺�����b�W���N�N��{�X����m��
	        //���ҥ����զ����
	        float ambientMaterial[] = {0.4f, 0.4f, 0.4f, 1.0f};
	        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientMaterial,0);
	        //���g�����զ����
	        float diffuseMaterial[] = {1f, 1f, 1f, 1.0f};
	        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterial,0);
	        //�������謰�զ�
	        float specularMaterial[] = {0.5f, 0.5f, 0.5f, 1.0f};
	        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specularMaterial,0);
	        //�����Ϯg�ϰ�,�ƶV�j�ϥհϰ�V�p�V�t
	        float shininessMaterial[] = {0.5f};
	        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, shininessMaterial,0);
		}
		public int initTexture(GL10 gl,Bitmap bitmap)//textureId
		{
			//���ͯ��zID
			int textureID;
			int[] textures = new int[1];
			gl.glGenTextures
			(
					1,          //���ͪ����zid���ƶq
					textures,   //���zid���}�C
					0           //�����q
			);    
			textureID=textures[0];    
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
	        
	        //��ڸ��J���z
	        GLUtils.texImage2D
	        (
	        		GL10.GL_TEXTURE_2D,   //���z���A�A�bOpenGL ES��������GL10.GL_TEXTURE_2D
	        		0, 					  //���z�����h�A0��ܰ򥻹ϧμh�A�i�H�A�Ѭ������K��
	        		bitmap, 			  //���z�ϧ�
	        		0					  //���z��ؤؤo
	        );
	        return textureID;
		}
		public void fire()
		 {
			 float startAngle;
			 float startX;
			 float startY;
			 float startZ;
			 synchronized(gdMainDraw.dataLock)	//��Wø����
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
