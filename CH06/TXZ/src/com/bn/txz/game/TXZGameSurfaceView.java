package com.bn.txz.game;

import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static com.bn.txz.Constant.*;
import com.bn.txz.Constant;
import com.bn.txz.TXZActivity;
import com.bn.txz.manager.PicDataManager;
import com.bn.txz.manager.VertexDataManager;
import com.bn.txz.manager.VertexTexture3DObjectForDraw;
import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class TXZGameSurfaceView extends GLSurfaceView{
	
	public SceneRenderer mRenderer; 
	
	//主資料
	GameData gdMain=new GameData();
	//繪制資料
	GameData gdDraw=new GameData();
	//臨時資料
	GameData gdTemp=new GameData();
	//關卡類別的主、繪制和臨時資料
	GuanQiaData gqdMain=new GuanQiaData();
	GuanQiaData gqdDraw=new GuanQiaData();
	GuanQiaData gqdTemp=new GuanQiaData();
	
	float mPreviousX,mPreviousY;
	float startX,startY;
	boolean isMove=false;
	final float MOVE_THOLD=30;	//觸控時判斷是否是move動作
	float x,y;					//觸控點的X與Y座標
	
	public Object aqLock=new Object();	//動作佇列鎖
	public Queue<Action> aq=new LinkedList<Action>();//動作佇列
	TXZDoActionThread dat;					//執行動作執行緒參考
	
	public Object aqygLock=new Object();
	public Queue<Action> aqyg=new LinkedList<Action>();//搖桿動作佇列
	YGDoActionThread ygdat;
	
	VertexTexture3DObjectForDraw room;		//房間
	VertexTexture3DObjectForDraw sky;		//天空
	VertexTextureNormal3DObjectForDraw wall;//橋
	VertexTexture3DObjectForDraw water;		//水
	VertexTextureNormal3DObjectForDraw[] lovntArray=new VertexTextureNormal3DObjectForDraw[6];//機器人各部分
	Robot robot;
	VertexTexture3DObjectForDraw left;//向左的虛擬按鈕
	
	VertexTexture3DObjectForDraw yaogan1;//搖桿
	VertexTexture3DObjectForDraw yaogan2;
	
	VertexTexture3DObjectForDraw laodBack;//載入界面背景圖
	VertexTexture3DObjectForDraw processBar;//載入 界面中的進度指示器矩形
	VertexTexture3DObjectForDraw loading;//載入界面中文字矩形
	
	VertexTexture3DObjectForDraw bground;//贏界面背景
	VertexTexture3DObjectForDraw bgbutton;//贏界面中去下一關按鈕
	
	int num=16;
	VertexTexture3DObjectForDraw texRect[]=new VertexTexture3DObjectForDraw[num];//紋理矩形
	
	boolean waterflag=true;
	private int load_step=0;//進度指示器步數
	
	public static float currDegree=0;
	public static float currDegreeView=0;
	public static float currX;//機器人所在的位置
	public static float currY;
	public static float currZ;
	public boolean isFirst=false;//是否是第一次轉換角度的標志
	
	boolean viewFlag=false;//攝影機角度標志位
	 
	boolean isLoadedOk=false;//是否載入完成標志位
	boolean inLoadView=true;//是否在載入界面標志位
	public boolean isInAnimation=false;//是否在播放動畫標志位
	private boolean isWinOrLose=false;//是否在輸贏標志位
	public boolean isDrawWinOrLose=false;
	
	
	public boolean temp=true;
	
	static float offsetx=0;//搖桿搬移的x軸的偏移量
	static float offsety=0;//搖桿搬移的y軸的偏移量
	boolean isYaogan=false;//第一次按下的點是否在搖桿內
	static float vAngle=100;//搖桿的偏轉角度
	boolean isGo=false;//是否加入前進動作到佇列的標志
	boolean isGoFlag;//更新isGo的執行緒是否工作的標志位
	
	float skyAngle=0;
	public static boolean isSkyAngle=false;
	
	
	
	TXZActivity activity;
	public TXZGameSurfaceView(TXZActivity activity) {
		super(activity);
		this.activity=activity;
		mRenderer = new SceneRenderer();
		setRenderer(mRenderer);		//設定著色器
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);	//設定著色模式為主動著色
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if(inLoadView)
		{
			return false;
		}
		
		float YAOGAN_WAI_LEFT=(763.6f+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio;//觸控邊緣位置
		float YAOGAN_WAI_RIGHT=(938.2f+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio;
		float YAOGAN_WAI_TOP=(518.4f+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio;
		float YAOGAN_WAI_BOTTOM=(334.8f+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio;
		
		Constant.YAOGAN_CENTER_X=YAOGAN_WAI_LEFT+(YAOGAN_WAI_RIGHT-YAOGAN_WAI_LEFT)/2;//中心
		Constant.YAOGAN_CENTER_Y=YAOGAN_WAI_BOTTOM+(YAOGAN_WAI_TOP-YAOGAN_WAI_BOTTOM)/2;
		Constant.YAOGAN_R=91.8f*(screenWidth-Constant.screenScaleResult.lucX*Constant.screenScaleResult.ratio*2)/Constant.screenWidthStandard;
		
		x=event.getX();
		y=event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(isInAnimation)
				{
					return false;
				}
               startX=x;
               startY=y;
               isMove=false;
               currDegreeView=currDegree;
               isYaogan=false;
               if(Math.sqrt((x-Constant.YAOGAN_CENTER_X)*(x-Constant.YAOGAN_CENTER_X)+
            		   (y-Constant.YAOGAN_CENTER_Y)*(y-Constant.YAOGAN_CENTER_Y))<Constant.YAOGAN_R)
               {//觸控點在搖桿內
            	   if(isWinOrLose)
            	   {
            		   return false;
            	   }
            	   isYaogan=true;
               }
               if(x>=(Constant.Game_View_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   x<=(Constant.Game_View_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   y>=(Constant.Game_View_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
            		   y<=(Constant.Game_View_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//按下轉換角度虛擬按鈕
               {
            	   if(isWinOrLose)
            	   {
            		   return false;
            	   }
            	   Action acTemp=new Action			//轉換角度的動作
				   (
						ActionType.CONVERT			//動作型態
				   );
					synchronized(aqLock)			//鎖上動作佇列
					{
						aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
					}
					isFirst=!isFirst;
               }
               else if(x>=(Constant.Game_Win_First_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   x<=(Constant.Game_Win_First_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   y>=(Constant.Game_Win_First_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
            		   y<=(Constant.Game_Win_First_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio
            		   &&isWinOrLose)//在輸贏界面按下第一個按鈕
               {
        		   //傳回到選單界面
            	   if(!isWinOrLose)
            	   {
            		   return false;
            	   }
        		   activity.handler.sendEmptyMessage(Constant.COMMAND_GOTO_MENU_VIEW);
        		   isWinOrLose=false;
        		   isDrawWinOrLose=false;
               }
               else if(x>=(Constant.Game_Win_Two_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   x<=(Constant.Game_Win_Two_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   y>=(Constant.Game_Win_Two_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
            		   y<=(Constant.Game_Win_Two_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//在輸贏界面按下第二個按鈕
               {
            	   if(!isWinOrLose)
            	   {
            		   return false;
            	   }
            	   if(gdMain.winFlag)//贏界面按下下一關按鈕
            	   {
            		   //到下一關去
            		   if(GameData.level==9)
            		   {
            			   GameData.level=0;
            		   }
            		   gdMain.loseFlag=false;
            		   gdMain.winFlag=false;
            		   isWinOrLose=false;
            		   GameData.level=GameData.level+1;
            		   isDrawWinOrLose=false;
            		   activity.handler.sendEmptyMessage(Constant.COMMAND_GOTO_GAME_VIEW);
            	   }
            	   else if(gdMain.loseFlag)//輸界面按下重玩按鈕
            	   {
            		   gdMain.loseFlag=false;
            		   gdMain.winFlag=false;
            		   isWinOrLose=false;
            		   isDrawWinOrLose=false;
            		   activity.handler.sendEmptyMessage(Constant.COMMAND_GOTO_GAME_VIEW);
            	   }
               }
			break;
			case MotionEvent.ACTION_MOVE:
				float dxStart=Math.abs(x-startX);//X方向的偏移量
				float dyStart=Math.abs(y-startY);//Y方向的偏移量
				
				//若果x與y搬移的範圍大於
				if(dxStart>MOVE_THOLD||dyStart>MOVE_THOLD)
				{
					isMove=true;
				}
				if(!viewFlag&&isMove&&!isYaogan)	//搬移標志位為true,並且起始按下的點不在搖桿內
				{
					float dx=x-mPreviousX;			//X方向搬移的長度
					float dy=y-mPreviousY;			//Y方向搬移的長度
					Action acTemp=new Action		//改變攝影機的動作
					(
						ActionType.CHANGE_CAMERA,	//動作型態
						new float[]{dx,dy}			//動作資料
					);
					synchronized(aqLock)			//鎖上動作佇列
					{
						aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
					}
				}
				if(isMove&&isYaogan)				//搬移標志位為true,並且起始按下的點在搖桿內
				{
					Action acTemp=new Action		//改變搖桿的動作
					(
						ActionType.YAOGAN_MOVE,		//動作型態
						new float[]{x,y}			//動作資料
					);
					synchronized(aqygLock)			//鎖上動作佇列
					{
						aqyg.offer(acTemp);			//將動作佇列的佇列尾加入動作
					}
					if(vAngle>=-45&&vAngle<45&&isGo)
					{//前進
						Action acTempl=new Action		//機器人前進的動作
					   (
							ActionType.ROBOT_UP			//動作型態
					   );
						synchronized(aqLock)			//鎖上動作佇列
						{
							aq.offer(acTempl);			//將動作佇列的佇列尾加入動作
						}
						isGo=false;
					}
				}
			break;
			case MotionEvent.ACTION_UP:
				Action actemp=new Action
				(
					ActionType.ACTION_UP	
				);
				synchronized(aqygLock)			//鎖上動作佇列
				{
					aqyg.offer(actemp);			//將動作佇列的佇列尾加入動作
				}
				if(isYaogan)
				{
					if(vAngle>=-135&&vAngle<-45)
					{//右轉
						Action acTemp=new Action		//機器人向右轉的動作
					   (
							ActionType.ROBOT_RIGHT	//動作型態
					   );
						synchronized(aqLock)			//鎖上動作佇列
						{
							aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
						}
					}
					if((vAngle>=45&&vAngle<90)||(vAngle>=-270&&vAngle<-225))
					{//左轉
						Action acTemp=new Action		//機器人向左轉的動作
	   				   (
	   						ActionType.ROBOT_LEFT	//動作型態
	   				   );
	   					synchronized(aqLock)			//鎖上動作佇列
	   					{
	   						aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
	   					}
					}
					if(vAngle>=-225&&vAngle<-135)
					{//後轉
						Action acTemp=new Action		//機器人向後轉的動作
					   (
							ActionType.ROBOT_DOWN	//動作型態
					   );
						synchronized(aqLock)			//鎖上動作佇列
						{
							aq.offer(acTemp);			//將動作佇列的佇列尾加入動作
						}
					}
				}
			break;
		}
		mPreviousX=x;//更新上一次的位置
		mPreviousY=y;
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer
	{
		int currentFlagindex=0;//目前框編號
		private boolean isFirstFrame=true;
		int roomId;	//房間紋理id
		int headId;//頭id
		int leftId;//左臂id
		int wallId;//橋紋理id
		int waterId;//水的紋理id
		int convertId;//角度轉換虛擬按鈕紋理id
		int boxId;//箱子紋理id
		int targetId;//目的地紋理id
		int loadBackId;//載入界面背景矩形紋理
		int processBeijing;//載入界面進度指示器矩形背景
		int tex_processId;//進度指示器
		int loadId;
		int loseId;//贏界面文字矩形紋理id
		int winId;//贏界面背景紋理id
		int returnId;//贏界面下一關按鈕紋理id
		int retryId;//贏界面中傳回按鈕紋理id
		int next_GuanId;//下一關按鈕紋理id
		int yaogan1Id;//搖桿的紋理id
		int yaogan2Id;
		
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			
			//清楚深度快取與彩色快取
			gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
			
			  if(!isLoadedOk)//如過沒有載入完成
	          {
				     inLoadView=true;
	            	 drawOrthLoadingView(gl);  //繪制載入進度指示器界面          	 
	          }
			  else
			  {
				  inLoadView=false;
				  drawGameView(gl);
			  }
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) 
		{
			gl.glViewport(
					Constant.screenScaleResult.lucX, Constant.screenScaleResult.lucY, 
					(int)(Constant.screenWidthStandard*Constant.screenScaleResult.ratio), 
					(int)(Constant.screenHeightStandard*Constant.screenScaleResult.ratio)
			);
			
			Constant.ratio=Constant.screenWidthStandard/Constant.screenHeightStandard;  
			//設定為開啟背面剪裁
    		gl.glEnable(GL10.GL_CULL_FACE);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);  		//關閉抗抖動 
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);//設定特定Hint專案的模式，這裡為設定為使用快速模式
			gl.glClearColor(0, 0, 0, 0);		//設定螢幕背景色黑色RGBA
			gl.glEnable(GL10.GL_DEPTH_TEST);	//開啟深度檢驗
			gl.glDisable(GL10.GL_CULL_FACE);	//設定為開啟背面剪裁
			gl.glShadeModel(GL10.GL_SMOOTH);	//設定著色模型為平順著色   
			Constant.CURR_DIRECTION=POSITIVE_MOVETO_Z;//起始朝向為z軸正方向
			
			Robot.RobotFlag=true;
			currDegree=0;
			isWinOrLose=false;
			Constant.IS_DRAW_WIN=false;
			isSkyAngle=true;
			
			initTexId(gl);//起始化紋理
			
			laodBack=new VertexTexture3DObjectForDraw//載入界面背景矩形
			(
					VertexDataManager.vertexPositionArray[22],//載入界面背景矩形的頂點座標資料
					VertexDataManager.vertexTextrueArray[22], //載入界面背景矩形紋理座標
					VertexDataManager.vCount[22] //頂點數
			);
			
			processBar=new VertexTexture3DObjectForDraw//載入界面背景矩形
			(
					VertexDataManager.vertexPositionArray[11],//載入界面背景矩形的頂點座標資料
					VertexDataManager.vertexTextrueArray[11], //載入界面背景矩形紋理座標
					VertexDataManager.vCount[11] //頂點數
			);
			
			loading=new VertexTexture3DObjectForDraw//載入界面文字矩形
			(
					VertexDataManager.vertexPositionArray[12],//載入界面文字矩形的頂點座標資料
					VertexDataManager.vertexTextrueArray[12], //載入界面文字矩形紋理座標
					VertexDataManager.vCount[12] //頂點數
			);
			
			//啟動一個執行緒動態切換框
            new Thread()
            {
            	@Override
            	public void run()
            	{
            		while(true)
            		{
            			currentFlagindex=(currentFlagindex+1)%texRect.length;            		
	            		try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            		}
            		
            	}
            }.start();
            
            
            new Thread()
            {
            	public void run()
            	{
            		while(isSkyAngle)
            		{
            			skyAngle=(skyAngle+0.2f)%360;
            			try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            		}
            	}
            }.start();
        	initLight(gl);
			initMaterial(gl);
            
		}
		
		public void initTexId(GL10 gl) {//起始化紋理id
			loadBackId=initTexture(gl, PicDataManager.picDataArray[12]);//載入界面背景紋理id
			processBeijing=initTexture(gl, PicDataManager.picDataArray[13]);//載入界面進度指示器背景紋理id
			tex_processId=initTexture(gl, PicDataManager.picDataArray[14]);//載入界面進度指示器紋理id
			loadId=initTexture(gl, PicDataManager.picDataArray[15]);//載入界面背景紋理id

		}
		
		public int initTexture(GL10 gl, byte[] data) {//起始化紋理
			int[] textures = new int[1];
			gl.glGenTextures(1, textures, 0);
			int currTextureId = textures[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);//綁定目前紋理
			
			//使用MipMap紋理過濾
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);//最近點取樣
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);//線性紋理過濾
			 //設定紋理伸展模式為重復  
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
			Bitmap bitmapTmp=BitmapFactory.decodeByteArray(data, 0, data.length);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);
			bitmapTmp.recycle();
			return currTextureId;
		}
	
	
		//繪制進度指示器載入界面
		public void drawOrthLoadingView(GL10 gl)
		{
			if(isFirstFrame)
			{
				 gl.glPushMatrix();
				 gl.glMatrixMode(GL10.GL_PROJECTION);//設定投影矩陣
				 gl.glLoadIdentity();//設定目前矩陣為單位矩陣				
				 gl.glOrthof(-ratio, ratio, bottom, top, near, far);//呼叫此方法計算產生正交投影矩陣
				 
				 GLU.gluLookAt//設定攝影機
			     ( 
						gl, 
						0,0,10,
						0,0,0,
						0,1,0  
				 );	
				 
				 gl.glMatrixMode(GL10.GL_MODELVIEW);//設定模式矩陣
			     gl.glLoadIdentity(); //設定目前矩陣為單位矩陣
			     laodBack.drawSelf(gl, loadBackId);//繪制載入界面背景
			     gl.glPopMatrix();
			     isFirstFrame=false;
			}
			else
			{
				 gl.glPushMatrix();
				 gl.glMatrixMode(GL10.GL_PROJECTION);//設定投影矩陣
				 gl.glLoadIdentity();//設定目前矩陣為單位矩陣				
				 gl.glOrthof(-ratio, ratio, bottom, top, near, far);//呼叫此方法計算產生正交投影矩陣
				 
				 GLU.gluLookAt//設定攝影機
			     ( 
						gl, 
						0,0,10,
						0,0,0,
						0,1,0  
				 );	
				 
				 gl.glMatrixMode(GL10.GL_MODELVIEW);//設定模式矩陣
			     gl.glLoadIdentity(); //設定目前矩陣為單位矩陣
			     laodBack.drawSelf(gl, loadBackId);//繪制載入界面背景
			     
			     gl.glPushMatrix();
			     
			     gl.glPushMatrix();
			     gl.glEnable(GL10.GL_BLEND);
			     gl.glDisable(GL10.GL_DEPTH_TEST); 
			     gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			     gl.glTranslatef(0, -1+0.08f, 0f);
			     processBar.drawSelf(gl, processBeijing);
	             gl.glDisable(GL10.GL_BLEND); 
	             gl.glEnable(GL10.GL_DEPTH_TEST);  
	             gl.glPopMatrix();
	            
	             gl.glEnable(GL10.GL_BLEND);
	             gl.glDisable(GL10.GL_DEPTH_TEST); 
	             gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	             gl.glTranslatef(-3.5f+3.5f*load_step/(float)8, -1+0.09f, 0f);
	             processBar.drawSelf(gl, tex_processId);
	             gl.glDisable(GL10.GL_BLEND); 
	             gl.glEnable(GL10.GL_DEPTH_TEST); 
	             gl.glPopMatrix();
	             
	             gl.glPushMatrix();
	             gl.glEnable(GL10.GL_BLEND);
	             gl.glDisable(GL10.GL_DEPTH_TEST); 
	             gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	             gl.glTranslatef(0.2f, 0.5f, 0);
	             loading.drawSelf(gl, loadId);
	             gl.glDisable(GL10.GL_BLEND); 
	             gl.glEnable(GL10.GL_DEPTH_TEST); 
	             gl.glPopMatrix();

	            gl.glPopMatrix();
	            loadResource(gl);//載入資源的方法
	            return;    
			}
		}
		
		//載入所有的資源
		public void loadResource(GL10 gl)
		{
			switch(load_step)
			{
			case 0:
				load_step++;
				break;
			case 1:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 2:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 3:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 4:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 5:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 6:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 7:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 8:
				init_All_Resource(gl,load_step);
				isLoadedOk=true;//切換到一級選單
				laodBack=null;//銷毀
				processBar=null;//銷毀
				break;
			}
		}
		
		//載入紋理的方法
		public void init_All_Resource(GL10 gl,int index)
		{
			switch(index)
			{
			case 1:  
				PicDataManager.loadPicData(TXZGameSurfaceView.this.getContext(), index);
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
	          break;
			case 2:
				PicDataManager.loadPicData(TXZGameSurfaceView.this.getContext(), index);
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				break;
			case 3:	
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				break;
			case 4:  
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				break;
			case 5:
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				break;
			case 6:
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				 //建立紋理矩形對物件 
				 //建立紋理矩形對物件 
				double dAngle=Math.PI/12/(num/4);//每次轉15度
	            for(int i=0;i<num;i++)
	            {
	            	if(i<num/4)//第一步左轉
	            	{
	            		VertexDataManager.initSoftBoxVertexData(dAngle*i);
	            		texRect[i]=new VertexTexture3DObjectForDraw
	            		(
	            				VertexDataManager.vertexPositionArray[26],
	            				VertexDataManager.vertexTextrueArray[26],
	            				VertexDataManager.vCount[26]
	            		);
	            	}
	            	if(i<num/2&&i>=num/4)//第二步右轉回標準
	            	{
	            		VertexDataManager.initSoftBoxVertexData(dAngle*(num/4-(i-num/4)));
	            		texRect[i]=new VertexTexture3DObjectForDraw
	            		(
	            				VertexDataManager.vertexPositionArray[26],
	            				VertexDataManager.vertexTextrueArray[26],
	            				VertexDataManager.vCount[26]
	            		);
	            	}
	            	if(i<num*3/4&&i>=num/2)//第三步右轉
	            	{
	            		VertexDataManager.initSoftBoxVertexData(dAngle*(num/4-(i-num/4)));
	            		texRect[i]=new VertexTexture3DObjectForDraw
	            		(
	            				VertexDataManager.vertexPositionArray[26],
	            				VertexDataManager.vertexTextrueArray[26],
	            				VertexDataManager.vCount[26]
	            		);
	            	}
	            	if(i<num&&i>=num*3/4)//第四部左轉回標準
	            	{
	            		VertexDataManager.initSoftBoxVertexData(dAngle*((i-num/4)-num*3/4));
	            		texRect[i]=new VertexTexture3DObjectForDraw
	            		(
	            				VertexDataManager.vertexPositionArray[26],
	            				VertexDataManager.vertexTextrueArray[26],
	            				VertexDataManager.vCount[26]
	            		);
	            	}
	            }
				break;
			case 7:	
				left=new VertexTexture3DObjectForDraw//向左的虛擬按鈕
				(
						VertexDataManager.vertexPositionArray[8],//房子的頂點座標資料
						VertexDataManager.vertexTextrueArray[8], //房間紋理座標
						VertexDataManager.vCount[8] //頂點數
				);
				
				yaogan1=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[28],
						VertexDataManager.vertexTextrueArray[28],
						VertexDataManager.vCount[28]
				);
				yaogan2=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[29],
						VertexDataManager.vertexTextrueArray[29],
						VertexDataManager.vCount[29]
				);
				yaogan1Id=initTexture(gl, PicDataManager.picDataArray[57]);
				yaogan2Id=initTexture(gl, PicDataManager.picDataArray[58]);
				
				
				boxId=initTexture(gl, PicDataManager.picDataArray[10]);//箱子紋理id
				
		
				
				convertId=initTexture(gl, PicDataManager.picDataArray[9]);//角度轉換虛擬按鈕紋理id
				
				roomId = initTexture(gl, PicDataManager.picDataArray[0]);		//房間紋理id
				room=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[0],//房子的頂點座標資料
						VertexDataManager.vertexTextrueArray[0], //房間紋理座標
						VertexDataManager.vCount[0] //頂點數
				);
				
				sky=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[32],//房子的頂點座標資料
						VertexDataManager.vertexTextrueArray[32], //房間紋理座標
						VertexDataManager.vCount[32] //頂點數
				);
				
				wallId=initTexture(gl, PicDataManager.picDataArray[1]);		//群組成橋紋理id
				wall=new VertexTextureNormal3DObjectForDraw//橋
				(
	                VertexDataManager.vertexPositionArray[9],
					VertexDataManager.vertexTextrueArray[9],
					VertexDataManager.vertexNormalArray[9],
					VertexDataManager.vCount[9]
				);
				
				waterId = initTexture(gl, PicDataManager.picDataArray[2]);		//水的紋理id
				water=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[7],//房子的頂點座標資料
						VertexDataManager.vertexTextrueArray[7], //房間紋理座標
						VertexDataManager.vCount[7] //頂點數
				);
				
				loseId=initTexture(gl, PicDataManager.picDataArray[16]);		//輸背景紋理id
				winId=initTexture(gl, PicDataManager.picDataArray[17]);		//贏背景紋理id
				returnId=initTexture(gl, PicDataManager.picDataArray[19]);		//重玩紋理id
				retryId=initTexture(gl, PicDataManager.picDataArray[18]);		//傳回紋理id
				next_GuanId=initTexture(gl, PicDataManager.picDataArray[20]);		//下一關紋理id
				
				bground=new VertexTexture3DObjectForDraw//輸贏界面背景
				(
						VertexDataManager.vertexPositionArray[14],//房子的頂點座標資料
						VertexDataManager.vertexTextrueArray[14], //房間紋理座標
						VertexDataManager.vCount[14] //頂點數
				);
				
				bgbutton=new VertexTexture3DObjectForDraw//輸贏界面按鈕
				(
						VertexDataManager.vertexPositionArray[15],//房子的頂點座標資料
						VertexDataManager.vertexTextrueArray[15], //房間紋理座標
						VertexDataManager.vCount[15] //頂點數
				);
				
				break;
			case 8: 
				targetId=initTexture(gl, PicDataManager.picDataArray[11]);//目的地紋理id
				
				
				leftId=initTexture(gl, PicDataManager.picDataArray[4]);//機器人其他部分紋理id
				lovntArray[0]=new VertexTextureNormal3DObjectForDraw//身體
				(
						VertexDataManager.vertexPositionArray[2],
						VertexDataManager.vertexTextrueArray[2],
						VertexDataManager.vertexNormalArray[2],
						VertexDataManager.vCount[2],
						leftId
				);
				headId=initTexture(gl, PicDataManager.picDataArray[3]);//機器人頭紋理id
				lovntArray[1]=new VertexTextureNormal3DObjectForDraw//頭
				(
						VertexDataManager.vertexPositionArray[1],
						VertexDataManager.vertexTextrueArray[1],
						VertexDataManager.vertexNormalArray[1],
						VertexDataManager.vCount[1],
						headId
				);
				lovntArray[2]=new VertexTextureNormal3DObjectForDraw//左臂上
				(
						VertexDataManager.vertexPositionArray[3],
						VertexDataManager.vertexTextrueArray[3],
						VertexDataManager.vertexNormalArray[3],
						VertexDataManager.vCount[3],
						leftId
				);
				
				lovntArray[3]=new VertexTextureNormal3DObjectForDraw//左臂下
				(
						VertexDataManager.vertexPositionArray[4],
						VertexDataManager.vertexTextrueArray[4],
						VertexDataManager.vertexNormalArray[4],
						VertexDataManager.vCount[4],
						leftId
				);
				
				lovntArray[4]=new VertexTextureNormal3DObjectForDraw//右臂上
				(
						VertexDataManager.vertexPositionArray[5],
						VertexDataManager.vertexTextrueArray[5],
						VertexDataManager.vertexNormalArray[5],
						VertexDataManager.vCount[5],
						leftId
				);
				
				lovntArray[5]=new VertexTextureNormal3DObjectForDraw//右臂下
				(
						VertexDataManager.vertexPositionArray[6],
						VertexDataManager.vertexTextrueArray[6],
						VertexDataManager.vertexNormalArray[6],
						VertexDataManager.vCount[6],  
						leftId
				);
				robot=new Robot(lovntArray,TXZGameSurfaceView.this);  
	    		dat=new TXZDoActionThread(TXZGameSurfaceView.this);//建立執行動作執行緒
	    		dat.start();//啟動執行緒
	    		ygdat=new YGDoActionThread(TXZGameSurfaceView.this);
	    		ygdat.start();
	    		new Thread()
	    		{
	    			public void run()
	    			{
	    				isGoFlag=true;
	    				while(isGoFlag)
	    				{
	    					isGo=true;
	    					try 
	    		    		{
	    						Thread.sleep(700);
	    					} catch (InterruptedException e) 
	    					{
	    						e.printStackTrace();
	    					}
	    				}
	    			}
	    		}.start();
				break;
			} 
		}
		
		//繪制游戲 界面
		public void drawGameView(GL10 gl)
		{
			gl.glMatrixMode(GL10.GL_PROJECTION);//設定目前矩陣為投影矩陣
			gl.glLoadIdentity();//設定目前矩陣為單位矩陣
			visualAngle(gl,ratio);//呼叫此方法計算產生透視投影矩陣
			gl.glMatrixMode(GL10.GL_MODELVIEW);//設定目前矩陣為模式矩陣
			gl.glLoadIdentity();//設定目前矩陣為單位矩陣	
			
			cameraPosition(gl);//攝影機設定
			//將繪制資料覆制進臨時變數
			synchronized(gdDraw.dataLock)	//鎖上繪制資料
			{
				gdDraw.copyTo(gdTemp);
			}
			
			synchronized(gqdDraw.gqdataLock)	//鎖上繪制資料
			{
				gqdTemp.boxCount=gqdDraw.boxCount;
				for(int i=0;i<gqdTemp.boxCount;i++)
				{
					gqdTemp.cdArray[i].row=gqdDraw.cdArray[i].row;
					gqdTemp.cdArray[i].col=gqdDraw.cdArray[i].col;
				}
				for(int i=0;i<gqdDraw.MAP[GameData.level-1].length;i++)
				{
					for(int j=0;j<gqdDraw.MAP[GameData.level-1][0].length;j++)
					{
						gqdTemp.MAP[GameData.level-1][i][j]=gqdMain.MAP[GameData.level-1][i][j];
					}
				}
				
			}

			
			//================繪制倒影=============================
			gl.glDisable(GL10.GL_CULL_FACE);//關閉背面剪裁
			
			for(int i=0;i<gqdMain.MAP[GameData.level-1].length;i++)
			{
				for(int j=0;j<gqdMain.MAP[GameData.level-1][0].length;j++)
            	{
        			float xOffset=GuanQiaData.XOffset[i][j];//格子在X軸方向的偏移量
					float zOffset=GuanQiaData.ZOffset[i][j];//格子在Z軸方向的偏移量
            		if(gqdTemp.MAP[GameData.level-1][i][j]==1||gqdTemp.MAP[GameData.level-1][i][j]==2||
            				gqdTemp.MAP[GameData.level-1][i][j]==4)//若果地圖中為橋或箱子或機器人，則繪制橋
            		{
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, -0.1f, zOffset);
            			wall.drawSelf(gl,wallId);//繪制橋的倒影
            			gl.glPopMatrix();
            		}
            		if(gqdTemp.MAP[GameData.level-1][i][j]==3||gqdTemp.MAP[GameData.level-1][i][j]==6)//若果地圖中為目的地或人在目的地，則繪制目的地
            		{
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, -0.1f, zOffset);
            			wall.drawSelf(gl,targetId);//目的地倒影
            			gl.glPopMatrix();
            		}
            		if(gqdTemp.MAP[GameData.level-1][i][j]==2)//若果地圖中為箱子，則繪制箱子
            		{
            			gl.glEnable(GL10.GL_BLEND);
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, 0.9f, zOffset);
            			if(i==GuanQiaData.move_row&&j==GuanQiaData.move_col)
            			{
            				gl.glTranslatef(GuanQiaData.xoffset, 0, GuanQiaData.zoffset);
            			}
            			texRect[currentFlagindex].drawSelf(gl,boxId);//繪制目前框
            			gl.glPopMatrix();
            			gl.glDisable(GL10.GL_BLEND);
            		}
            		if(gqdTemp.MAP[GameData.level-1][i][j]==5)//若果是推好的箱子，目的地也要繪制
            		{
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, -0.1f, zOffset);
            			wall.drawSelf(gl,targetId);//目的地倒影
            			gl.glPopMatrix();
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, 0.9f, zOffset);
            			wall.drawSelf(gl,targetId);//繪制推好的箱子的倒影
            			gl.glPopMatrix();
            		}
            	}
            }
			
            gl.glPushMatrix();
			gl.glScalef(1, -1, 1);
			gl.glTranslatef(0, 0.1f, 0);
    		robot.drawSelf(gl);//繪制機器人
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glScalef(1, -1, 1);
			gl.glTranslatef(0, -0.4f, 0);
			room.drawSelf(gl, roomId);//繪制房間的倒影
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glScalef(1, -1, 1);
			gl.glTranslatef(0, -0.4f, 0);
			gl.glRotatef(skyAngle, 0, 1, 0);
			sky.drawSelf(gl, roomId);//繪制天空的倒影
			gl.glPopMatrix();
			
			gl.glEnable(GL10.GL_CULL_FACE);//開啟背面剪裁
			
            gl.glEnable(GL10.GL_BLEND); //開啟混合   
            //設定源混合因子與目的混合因子
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			water.drawSelf(gl, waterId);
			gl.glDisable(GL10.GL_BLEND);
			//================繪制倒影=============================
			
			//===============繪制實體===============================
			gl.glPushMatrix();
			//容許光源       
	        gl.glEnable(GL10.GL_LIGHTING);
    		gl.glTranslatef(0, GameStaticData.FLOOR_Y+0.8f, 0);
    		robot.drawSelf(gl);//繪制機器人
    		//禁止光源       
	        gl.glDisable(GL10.GL_LIGHTING);
            gl.glPopMatrix();
	        
            
			room.drawSelf(gl, roomId);//繪制房間
			
			gl.glPushMatrix();
			gl.glRotatef(skyAngle, 0, 1, 0);//繪制天空
			sky.drawSelf(gl, roomId);
			gl.glPopMatrix();
			
			
			for(int i=0;i<gqdMain.MAP[GameData.level-1].length;i++)
			{
				for(int j=0;j<gqdMain.MAP[GameData.level-1][0].length;j++)
            	{
        			float xOffset=GuanQiaData.XOffset[i][j];//格子在X軸方向的偏移量
					float zOffset=GuanQiaData.ZOffset[i][j];//格子在Z軸方向的偏移量
            		if(gqdTemp.MAP[GameData.level-1][i][j]==1||gqdTemp.MAP[GameData.level-1][i][j]==2||
            				gqdTemp.MAP[GameData.level-1][i][j]==4)
            		{//若果地圖中為橋或箱子或機器人，則繪制橋
            			gl.glPushMatrix();
            			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y, zOffset);
            			wall.drawSelf(gl,wallId);//繪制橋
            			gl.glPopMatrix();
            		}
            		if(gqdTemp.MAP[GameData.level-1][i][j]==3||gqdTemp.MAP[GameData.level-1][i][j]==6)//若果地圖中為目的地或人在的目的地，則繪制目的地
            		{
            			gl.glPushMatrix();
            			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y, zOffset);
            			wall.drawSelf(gl,targetId);//目的地
            			gl.glPopMatrix();
            		}
            		
            		if(gqdTemp.MAP[GameData.level-1][i][j]==5)//若果是推好的箱子，目的地也要繪制
            		{
            			gl.glPushMatrix();
            			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y, zOffset);
            			wall.drawSelf(gl,targetId);//目的地倒影
            			gl.glPopMatrix();
            			gl.glPushMatrix();
            			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y+1f, zOffset);
            			wall.drawSelf(gl,targetId);//繪制推好的箱子的倒影
            			gl.glPopMatrix();
            		}
            	}
            }
			
			for(int i=0;i<gqdTemp.boxCount;i++)//繪制箱子
			{
				float xOffset=GuanQiaData.XOffset[gqdTemp.cdArray[i].row][gqdTemp.cdArray[i].col];//格子在X軸方向的偏移量
				float zOffset=GuanQiaData.ZOffset[gqdTemp.cdArray[i].row][gqdTemp.cdArray[i].col];//格子在Z軸方向的偏移量
				gl.glEnable(GL10.GL_BLEND);
    			gl.glPushMatrix();
    			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y+1f, zOffset);
    			if(gqdTemp.cdArray[i].row==GuanQiaData.move_row&&gqdTemp.cdArray[i].col==GuanQiaData.move_col)
    			{
    				gl.glTranslatef(GuanQiaData.xoffset, 0, GuanQiaData.zoffset);
    			}
    			if(gqdTemp.cdArray[i].row!=0&&gqdTemp.cdArray[i].col!=0)
    			{
    				texRect[currentFlagindex].drawSelf(gl,boxId);//繪制目前框
    			}
    			
    			gl.glPopMatrix();
    			gl.glDisable(GL10.GL_BLEND);
			}
			//===============繪制實體===============================
			
			//===============繪制虛擬按鈕=============================
			 gl.glEnable(GL10.GL_BLEND);//開啟混合
			 //設定源混合因子與目的混合因子
		     gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			 gl.glMatrixMode(GL10.GL_PROJECTION);//設定投影矩陣
			 gl.glLoadIdentity();//設定目前矩陣為單位矩陣				
			 gl.glOrthof(-ratio, ratio, bottom, top, near, far);//呼叫此方法計算產生正交投影矩陣
			 
			 GLU.gluLookAt//設定攝影機
		     ( 
					gl, 
					0,0,10,
					0,0,0,
					0,1,0  
			 );				
		     
	         gl.glMatrixMode(GL10.GL_MODELVIEW);//設定模式矩陣
	         gl.glLoadIdentity(); //設定目前矩陣為單位矩陣	
	         
	         //中間圓
	         gl.glPushMatrix();
	         gl.glTranslatef(1.35f, -0.6f, -0.1f);
	         gl.glTranslatef(offsetx*0.17f, offsety*0.17f, 0f);
	         yaogan2.drawSelf(gl,yaogan2Id);
	         gl.glPopMatrix();
	         
	         //搖桿背景
	         gl.glPushMatrix();
	         gl.glTranslatef(1.35f, -0.6f, 0f);
	         yaogan1.drawSelf(gl, yaogan1Id);
	         gl.glPopMatrix();
	         
	         gl.glPushMatrix();
	         gl.glTranslatef(1.64f, 0.86f, 0f);
	         left.drawSelf(gl, convertId);//繪制角度轉換的虛擬按鈕
	         gl.glPopMatrix();
	         
	         gl.glDisable(GL10.GL_BLEND);//關閉混合
	       //===============繪制虛擬按鈕=============================
	         
	       //=====================判斷游戲是否結束================
	            judgeGoToLastViewOrGoToNext(gl);
	       //=====================判斷游戲是否結束================
		}
		
		//游戲結束，判斷否是進入下一關游戲
		public void judgeGoToLastViewOrGoToNext(GL10 gl)
		{
			int row=0;
	        int col=0;
	        int targetCount=0;
	        int boxWaterleft=0;
	        int boxWaterright=0;
	        int boxWatertop=0;
	        int boxWaterdown=0;
	        int boxWaterCount=0;
	        row=gqdTemp.MAP[GameData.level-1].length;
			col=gqdTemp.MAP[GameData.level-1][0].length;
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<col;j++)
				{
					if(gqdTemp.MAP[GameData.level-1][i][j]==2)//沒有推好的箱子數量
					{
						targetCount++;
					}
				}
			}
			if(targetCount==0)//游戲結束,即目的點的數量為0
			{
				int currLevel=0;
				gdMain.winFlag=true;
				gdMain.loseFlag=false;
				gdMain.gameFinish=false;
				isWinOrLose=true;
				currLevel=activity.sharedUtil.getPassNum();
	          	if(GameData.level>=currLevel&&temp)
	          	{
	          		activity.sharedUtil.putPassNum(currLevel+1);
	          		temp=false;
	          	} 
				dat.workFlag=false;
				ygdat.workFlag=false;
				isGoFlag=false;
				if(isDrawWinOrLose&&!isInAnimation)
				{
					drawWinOrLose(gl);
				}
			}
			else if(targetCount!=0)//是否沒有推合格的箱子不能推動，若不能推動則玩家輸了本關游戲
			{
				for(int i=0;i<row;i++)
				{
					for(int j=0;j<col;j++)
					{
						if(gqdTemp.MAP[GameData.level-1][i][j]==2)//目的數量
						{
							if(gqdTemp.MAP[GameData.level-1][i-1][j]==0)
							{
								boxWatertop=1;
							}
							if(gqdTemp.MAP[GameData.level-1][i+1][j]==0)
							{
								boxWaterdown=1;
							}
							if(gqdTemp.MAP[GameData.level-1][i][j-1]==0)
							{
								boxWaterleft=1;
							}
							if(gqdTemp.MAP[GameData.level-1][i][j+1]==0)
							{
								boxWaterright=1;
							}
							
							if((boxWatertop==1&&boxWaterleft==1)||(boxWaterleft==1&&boxWaterdown==1)||(boxWaterdown==1&&boxWaterright==1)||
									(boxWaterright==1&&boxWatertop==1))
							{
								boxWaterCount++;
								boxWatertop=0;
								boxWaterdown=0;
								boxWaterleft=0;
								boxWaterright=0;
								
							}
						}
					}
				}
				
//				for(int i=0;i<targetCount;i++)
//				{
//					if((boxWatertop==1&&boxWaterleft==1)||(boxWaterleft==1&&boxWaterdown==1)||(boxWaterdown==1&&boxWaterright==1)||
//							(boxWaterright==1&&boxWatertop==1))
//					{
//						boxWaterCount++;
//					}
//				}
				if(boxWaterCount==targetCount)
				{
					System.out.println(boxWaterCount+"  ========  "+targetCount);
					gdMain.winFlag=false;
					gdMain.loseFlag=true;
					gdMain.gameFinish=true;
					isWinOrLose=true;
					dat.workFlag=false;
					ygdat.workFlag=false;
					isGoFlag=false;
					Constant.IS_DRAW_WIN=true;
					if(isDrawWinOrLose&&!isInAnimation)
					{
						drawWinOrLose(gl);
					}
				}
				
			}
		}
		
		public void drawWinOrLose(GL10 gl)
		{
			
			 if(Constant.IS_DRAW_WIN)
			 {
				 gl.glEnable(GL10.GL_BLEND);//關閉混合
				 if(gdMain.loseFlag&&gdMain.gameFinish)
		         {
		             	gl.glPushMatrix();
		         		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		         		gl.glTranslatef(0f,0f, 0.1f);
		             	bground.drawSelf(gl,loseId);
		             	gl.glPopMatrix();
		         }
		         if(gdMain.winFlag)
		         {
		             	gl.glPushMatrix();
		         		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		         		gl.glTranslatef(0f,0f, 0.1f);
		             	bground.drawSelf(gl,winId);
		             	gl.glPopMatrix();
		         }
		         if(gdMain.loseFlag)
		         {
		             	gl.glPushMatrix();
		             	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		             	gl.glTranslatef(-0.45f,-0.2f, 0.15f);
		             	bgbutton.drawSelf(gl,returnId);//傳回按鈕
		             	gl.glPopMatrix();
		             	
		             	gl.glPushMatrix();
		             	gl.glTranslatef(0.45f,-0.2f, 0.15f);
		             	bgbutton.drawSelf(gl,retryId);//重玩按鈕
		             	gl.glPopMatrix();
		         }
		         if(gdMain.winFlag)
		         {
		        	 gl.glPushMatrix();
		             gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		             gl.glTranslatef(-0.45f,-0.2f, 0.15f);
		             bgbutton.drawSelf(gl,returnId);//傳回按鈕
		             gl.glPopMatrix();
		             
		             gl.glPushMatrix();
		             gl.glTranslatef(0.45f,-0.2f, 0.15f);
		             bgbutton.drawSelf(gl,next_GuanId);//下一關按鈕
		             gl.glPopMatrix();
		         }
				 gl.glDisable(GL10.GL_BLEND);//關閉混合
			 }
		}
		
		//角度變化後，第一角度和第三角度的角度大小
		public void visualAngle(GL10 gl, float ratio)
		{
			if(viewFlag)//第一角度
	    	{
				gl.glFrustumf(-ratio*0.3f, ratio*0.3f, bottom*0.3f, top*0.3f, near*0.3f, far*0.3f);
	        	
	    	}else//第三角度
	    	{
	    		//呼叫此方法計算產生透視投影矩陣
	            gl.glFrustumf(-ratio, ratio, bottom, top, near, far);//呼叫此方法計算產生透視投影矩陣
	    	}
		}
		
		//角度變化後，第一角度和第三角度，攝影機的位置
		public void cameraPosition(GL10 gl)
		{
			if(viewFlag)//第一角度，永遠更在機器人的後上方
	        {
				if(isFirst)
				{
					currX=robot.positionx;
					currZ=robot.positionz;
					isFirst=!isFirst;
				}
				
	        	  GLU.gluLookAt
		          ( 
		        		  gl,
		                  (float) (currX+1f*Math.sin(Math.toRadians(currDegreeView+180))),//攝影機的位置
		                   1.5f+2.7f,
		                  (float) (currZ+1f*Math.cos(Math.toRadians(currDegreeView+180))), //機器人的後方
		                  
		                  (float) (currX+0.5f*Math.sin(Math.toRadians(currDegreeView+180))),//攝影機看向的位置
		                  1.5f+2f, 
		                  (float) (currZ+0.5f*Math.cos(Math.toRadians(currDegreeView+180))),
		                  
		                  0,1,0
		           );
	        	  synchronized(gqdDraw.gqdataLock)//鎖繪數
		           {
					   gqdDraw.ComparableByDis(
		        			  (float) (currX+1f*Math.sin(Math.toRadians(currDegreeView+180))), 
		        			  1.5f+2.7f, 
		        			  (float) (currZ+1f*Math.cos(Math.toRadians(currDegreeView+180))),
		        			  gqdMain.MAP[GameData.level-1]);//排序
		           }
	        	
	        }else//第三角度
	        {	
	        	
	        	//將繪制資料覆制進臨時變數
				synchronized(gdDraw.dataLock)	//鎖上繪制資料
				{
					//將攝影機9參數copy進臨時變數
					gdTemp.cx=gdDraw.cx;//攝影機位置
					gdTemp.cy=gdDraw.cy;
					gdTemp.cz=gdDraw.cz;
					gdTemp.tx=gdDraw.tx;//觀察點位置
					gdTemp.ty=gdDraw.ty;
					gdTemp.tz=gdDraw.tz;
					gdTemp.ux=gdDraw.ux;//up向量
					gdTemp.uy=gdDraw.uy;
					gdTemp.uz=gdDraw.uz;
				}
	        	GLU.gluLookAt//總攝影機設定
				(
					gl, 
					gdTemp.cx,gdTemp.cy,gdTemp.cz,//攝影機位置
					gdTemp.tx,gdTemp.ty,gdTemp.tz,//觀察點位置
					gdTemp.ux,gdTemp.uy,gdTemp.uz//UP向量
				);
	        	synchronized(gqdDraw.gqdataLock)//鎖主數
		        {
					   gqdDraw.ComparableByDis(
							   gdMain.cx, 
							   gdMain.cy, 
							   gdMain.cz,
							   gqdMain.MAP[GameData.level-1]);//排序
		        }
	        }
		}
		
		private void initLight(GL10 gl)
		{
	        gl.glEnable(GL10.GL_LIGHT0);//開啟0號燈  
	        
	        //環境光設定
	        float[] ambientParams={0.4f,0.4f,0.4f,1.0f};//光參數 RGBA
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams,0);            

	        //散射光設定
	        float[] diffuseParams={1f,1f,1f,1.0f};//光參數 RGBA
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams,0); 
	        
	        //反射光設定
	        float[] specularParams={0.15f,0.15f,0.15f,1.0f};//光參數 RGBA
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams,0);
	        
	        //設定光源位置
	        float[] positionParams={10,20,20,1};//最後的1表示是定位光
	        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, positionParams,0); 
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
		
	}
}
