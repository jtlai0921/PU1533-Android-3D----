package com.bn.zxl;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.collision.AABB;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import com.bn.box.MyBody;
import com.bn.util.Constant;
import com.bn.util.From2DTo3DUtil;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable.Callback;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import static com.bn.util.Constant.*;

public class GameView extends GLSurfaceView implements Callback
{
	MyActivity activity;
	Bitmap bitmap;
	Bitmap bitmap_yuanzhu_cebi;
	Bitmap bitmap_yuanzhu_di;
	public int textureId;//紋理的名稱ID
	public int textureId_cebi;//紋理的名稱ID
	public int textureId_di;//紋理的名稱ID
	public int textureId_cloud;//紋理的名稱ID
	public int textureId_mutong;//紋理的名稱ID
	public int textureId_exit;
	public int textureId_background;
	public int textureId_restart;
	public int textureId_nextlevel;
	public int textureId_stone1;
	public int textureId_stone2;
	public int textureId_stone9;
	public int textureId_stone10;
	public int textureId_wall;
	public int textureId_wallHeng;
	public int textureId_zhadan;
	public int textureId_diban;
	public int textureId_ding;
	Paint paint;
	World world;
	AABB aabb;
	private SceneRenderer mRenderer;//場景著色器
   
	public  boolean isGamePause=false;
	public ArrayList<MyBody> reclist;
	public ArrayList<MyBody> rainlist;
	public ArrayList<MyBody> lastRain;
	public ArrayList<MyBody> BallList;
	
	public ArrayList<String> ObjectArray;
	
	float x,y;
	
	public GameView(MyActivity activity) {
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);
		DRAW_THREAD_FLAG=true;
		reclist=new ArrayList<MyBody>();
		rainlist=new ArrayList<MyBody>();
		lastRain=new ArrayList<MyBody>();
		BallList=new ArrayList<MyBody>();
		ObjectArray=new ArrayList<String>();
		isGamePause=false;
		aabb=new AABB();
		aabb.lowerBound.set(-100.0f,-100.0f);
		aabb.upperBound.set(1000.0f,1000.0f);
		boolean doSleep=true;
		world=new World(aabb,Constant.GRAVITYTEMP,doSleep);
		Constant.initBitmap(getResources());
		Constant.initObjectTexture(this);

		mRenderer = new SceneRenderer();
		this.setRenderer(mRenderer);				//設定著色器		
	    setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設定著色模式為主動著色   
	    initContactListener();
	    RESTART=true;
		PhysicsThread dt=new PhysicsThread(this);
		dt.start();
		ChangeThread ct=new ChangeThread(this);
		ct.start();
	}
	void initContactListener()
	{
		ContactListener contactlistener=new ContactListener()
		{
			@Override
			public void add(ContactPoint arg0) {
				//碰撞檢驗處理
				
				CollisionAction.doAction
				(
						
					GameView.this, 
					arg0.shape1.getBody(),
					arg0.shape2.getBody(),  
					arg0.position.x, 
					arg0.position.y,
					arg0.normal,					
					arg0.velocity
				);
				
			}
			@Override
			public void persist(ContactPoint arg0) {
			}
			@Override
			public void remove(ContactPoint arg0) {
			}
			@Override
			public void result(ContactResult arg0) {
			}
		};
		world.setContactListener(contactlistener);
	}
	
	
	public boolean onTouchEvent(MotionEvent e)
	{
		 x = e.getX();
		 y = e.getY();
		switch(e.getAction())
		{
			case MotionEvent.ACTION_UP:	
				if(ObjectArray.size()!=0)
				{
					BoxPosition_x=Touch_X;
					BoxPosition_y=Touch_Y;
					Add_Flag=true;

//					Touch_X=480;
//					Touch_Y=80;
				}

			break; 
		    case MotionEvent.ACTION_MOVE:
		    	if(ObjectArray.size()!=0)
		    	{
		    		Touch_X=x/ssr.ratio-ssr.lucX;
		    		Touch_Y=y/ssr.ratio-ssr.lucY;
		    		
		    	}
				break;
		    case MotionEvent.ACTION_DOWN:
		    	if(ObjectArray.size()!=0&&!Touch_Flag)
		    	{
					Touch_X=480;
					Touch_Y=80;
		    		Add_Flag=false;
		    		if(x>(430+ssr.lucX)*ssr.ratio&&x<(530+ssr.lucX)*ssr.ratio&&y>(30+ssr.lucY)*ssr.ratio&&y<(130+ssr.lucY)*ssr.ratio)
		    		{
		    			Touch_Flag=true;
		    		}
		    	}
				if(x>(517+ssr.lucX)*ssr.ratio&&x<(618+ssr.lucX)*ssr.ratio&&y>(190+ssr.lucY)*ssr.ratio&&y<(288+ssr.lucY)*ssr.ratio&&ObjectArray.size()==0)
				{
					if(Level_Fail_Flag)
					{
						RESTART=true;
					}
				}
				if(x>(342+ssr.lucX)*ssr.ratio&&x<(444+ssr.lucX)*ssr.ratio&&y>(194+ssr.lucY)*ssr.ratio&&y<(287+ssr.lucY)*ssr.ratio)
				{
					if(Level_Fail_Flag)
					{
						this.activity.hd.sendEmptyMessage(2);
					}
					
				}
				if(x>(434+ssr.lucX)*ssr.ratio&&x<(528+ssr.lucX)*ssr.ratio&&y>(198+ssr.lucY)*ssr.ratio&&y<(285+ssr.lucY)*ssr.ratio)
				{
					if(Level_Fail_Flag)
					{
						if(CurrentLevel!=0)
						{
							CurrentLevel=CurrentLevel-1;
						}
						else
						{
							CurrentLevel=5;
						}
						
						RESTART=true;
					}
				}
		    	break;
		}
		return true;
	}
	
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {   
		float ratio=(float)960/540;
    	public SceneRenderer()
    	{
    	}

		@Override
		public void onDrawFrame(GL10 gl) 
		{
        	gl.glOrthof(-ratio, ratio, -1, 1, 1, 100);
        	gl.glMatrixMode(GL10.GL_MODELVIEW);
        	gl.glLoadIdentity();  
        	gl.glShadeModel(GL10.GL_SMOOTH);
        	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            //啟用深度測試
            gl.glEnable(GL10.GL_DEPTH_TEST);
            GLU.gluLookAt(
                    gl, 
                    0, 2f, 10f,//眼睛
                	0, 0f, 0, //看點
                	0f, 1f, 0f//朝向
                	
                );
//            Constant.backGround.drawSelf(gl, textureId_background);
            Constant.room.drawSelf(gl);
            if(!Level_Fail_Flag)
            {
	            for(int i=0;i<reclist.size();i++)
	            {
	            	reclist.get(i).drawSelf(gl);
	            }
	            for(int i=0;i<BallList.size();i++)
	            {
	            	BallList.get(i).drawSelf(gl);
	            }
	            for(int i=0;i<rainlist.size();i++)
	            {
	            	if(rainlist.get(i)!=null)
	            	{
	            		rainlist.get(i).drawSelf(gl);
	            	}
	            }
	            for(int i=0;i<lastRain.size();i++)
	            {
	            	if (lastRain.get(i)!=null) {
						lastRain.get(i).drawSelf(gl);
					}
	            }
            }
            
            if(Touch_Flag&&ObjectArray.size()!=0&&ObjectArray.get(ObjectArray.size()-1)!=null)
            {
            	if(ObjectArray.get(ObjectArray.size()-1)=="MuTong")
            	{
            		mutongPicture.drawSelf(gl, Touch_X, Touch_Y);
            	}
            	else if(ObjectArray.get(ObjectArray.size()-1)=="Rec")
            	{
            		Constant.recPicture.drawSelf(gl, Touch_X, Touch_Y);
            	}
            	else if(ObjectArray.get(ObjectArray.size()-1)=="ChiLun")
            	{
            		chilunPicture.drawSelf(Touch_X, Touch_Y, gl);
            	}
            }
            else if(ObjectArray.size()!=0)
            {
            	if(ObjectArray.get(ObjectArray.size()-1)=="MuTong")
            	{
            		mutongPicture.drawSelf(gl, 480, 80);
            	}
            	else if(ObjectArray.get(ObjectArray.size()-1)=="Rec")
            	{
            		Constant.recPicture.drawSelf(gl, 480, 80);
            	}
            	else if(ObjectArray.get(ObjectArray.size()-1)=="ChiLun")
            	{
            		chilunPicture.drawSelf(480, 80, gl);
            	}
            	
            }
            
            for(int i=0;i<ObjectArray.size();i++)
            {
            	if(ObjectArray.get(i)=="MuTong")
            	{
            		ObjectArrayTexture.drawSelf(gl, textureId_mutong, From2DTo3DUtil.point3D(20+30+80*i, 0), 0.7f,0);
            	}
            	else if(ObjectArray.get(i)=="Rec")
            	{
            		ObjectArrayTexture.drawSelf(gl, textureId, From2DTo3DUtil.point3D(20+30+80*i, 0), 0.7f,0);

            	}
            	else if(ObjectArray.get(i)=="ChiLun")
            	{
            		ObjectArrayTexture.drawSelf(gl, textureId_di, From2DTo3DUtil.point3D(20+30+80*i, 0), 0.7f,0);

            	}
            }
            
            if(ObjectArray.size()==0)
            {
            	Constant.Cloud.drawSelf(gl, textureId_cloud, From2DTo3DUtil.point3D(Cloud_Position, 10), 0.8f,0);
            }
            
            if(Level_Fail_Flag)
            {
//            	levelexit.drawSelf(gl, textureId_exit);
            	if(LastLevel==CurrentLevel)
            	{
            		levelexit.drawSelf(gl, textureId_restart);

            	}
            	else
            	{
            		levelexit.drawSelf(gl, textureId_nextlevel);
            	}
            }
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
            //設定視窗大小及位置 
        	gl.glViewport(ssr.lucX, ssr.lucY, (int)(Width*ssr.ratio), (int)(Height*ssr.ratio));
        
        	//設定目前矩陣為投影矩陣
            gl.glMatrixMode(GL10.GL_PROJECTION);
            //設定目前矩陣為單位矩陣
            gl.glLoadIdentity();

		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) 
		{
        	//關閉抗抖動 
        	gl.glDisable(GL10.GL_DITHER);
        	//設定特定Hint專案的模式，這裡為設定為使用快速模式
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);
            //設定螢幕背景色黑色RGBA
            gl.glClearColor(0,0,0,0);
            //開啟背面剪裁
//            gl.glEnable(GL10.GL_CULL_FACE);
            //開啟混合
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            gl.glShadeModel(GL10.GL_SMOOTH);//GL10.GL_SMOOTH  GL10.GL_FLAT
            //啟用深度測試
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glDeleteTextures(1, new int []{textureId},0);
            gl.glDeleteTextures(1, new int []{textureId_cebi},0);
            gl.glDeleteTextures(1, new int []{textureId_di},0);
            gl.glDeleteTextures(1, new int []{textureId_cloud},0);
            gl.glDeleteTextures(1, new int []{textureId_mutong},0);
            gl.glDeleteTextures(1, new int []{textureId_exit},0);
            gl.glDeleteTextures(1, new int []{textureId_background},0);
            gl.glDeleteTextures(1, new int []{textureId_nextlevel},0);
            gl.glDeleteTextures(1, new int []{textureId_restart},0);
            gl.glDeleteTextures(1, new int []{textureId_stone1},0);
            gl.glDeleteTextures(1, new int []{textureId_stone2},0);
            gl.glDeleteTextures(1, new int []{textureId_stone9},0);
            gl.glDeleteTextures(1, new int []{textureId_stone10},0);
            gl.glDeleteTextures(1, new int []{textureId_wall},0);
            gl.glDeleteTextures(1, new int []{textureId_wallHeng},0);
            gl.glDeleteTextures(1, new int []{textureId_zhadan},0);
            gl.glDeleteTextures(1, new int []{textureId_diban},0);
            gl.glDeleteTextures(1, new int []{textureId_ding},0);
            
            textureId=initTexture(gl,TP_ARRAY[0]);
            textureId_cebi=initTexture(gl,TP_ARRAY[1]);
            textureId_di=initTexture(gl,TP_ARRAY[2]);
            textureId_cloud=initTexture(gl,TP_ARRAY[3]);
            textureId_mutong=initTexture(gl,TP_ARRAY[4]);
            
            textureId_exit=initTexture(gl,TP_ARRAY[5]);
            
            textureId_background=initTexture(gl,TP_ARRAY[6]);
            textureId_nextlevel=initTexture(gl,TP_ARRAY[7]);
            textureId_restart=initTexture(gl,TP_ARRAY[8]);
            textureId_stone1=initTexture(gl,TP_ARRAY[9]);
            textureId_stone2=initTexture(gl,TP_ARRAY[10]);
            textureId_stone9=initTexture(gl,TP_ARRAY[11]);
            textureId_stone10=initTexture(gl,TP_ARRAY[12]);
            textureId_wall=initTexture(gl,TP_ARRAY[13]);
            textureId_wallHeng=initTexture(gl,TP_ARRAY[14]);
            textureId_zhadan=initTexture(gl,TP_ARRAY[15]);
            textureId_diban=initTexture(gl,TP_ARRAY[16]);
            textureId_ding=initTexture(gl,TP_ARRAY[17]);
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
    }
}
