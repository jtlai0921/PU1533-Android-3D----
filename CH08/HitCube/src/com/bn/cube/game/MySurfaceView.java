package com.bn.cube.game;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import com.bn.GL20Tail.DrawBuffer;
import com.bn.GL20Tail.GoThread;
import com.bn.GL20Tail.Tail;
import com.bn.GL20Tail.TailPart;
import com.bn.cube.core.LoadUtil;
import com.bn.cube.core.MatrixState;
import com.bn.cube.core.SQLiteUtil;
import com.bn.cube.view.ButtonGraph;
import com.bn.cube.view.ButtonLine;
import com.bn.cube.view.HitCubeActivity;
import com.bn.cube.view.LineThread;
import com.bn.cube.view.MyGLSurfaceView;
import com.bn.cube.view.PieceTexture;
import com.bn.cube.view.TextureRectNo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class MySurfaceView extends MyGLSurfaceView {
	private SceneRenderer mRenderer;//場景著色器
    private int tex_loadingviewId;//載入界面的ID
    private int tex_processId;//進度指示器
    private int load_step=0;//進度指示器步數
    PieceTexture  tailPieces;  
    TextureRectNo loadingView;//3D中的載入界面
    TextureRectNo processBar;//載入界面中的進度指示器
    TextureRectNo loading;//loading
	int guanDaoId;
	int dangBanId;
	int contId;
	int signx=1;//正負號
	int signy=-1;
	boolean temp=true;
	GuanDao gd;
	boolean bl=true;
	HitCubeActivity activity;
	int piece1Id;//碎片ID
	int piece2Id;
	int piece3Id;
	int piece4Id;
	int pengId;//碰撞點
	int smallBallId;
	int hengGangId;
	int dengHaoId;
	int levelId;
	int jiaId;
	int jianId;
	boolean isLoadedOk=false;
	int numberId[]=new int[11];//數字陣列
	int bigLevelNumber=1;//關數
	int lifeNumber=0;//生命值
	int lifeNumber1=0;
	int lifeNumber2=0;
	int lifeNumber3=5;
	int pieceId;//碎片索引
	//==========================
	TextureRectShaLou shalou;
	GoThread gt;
	int shalouredId;
	int shaloupuId;
	int shalouId;
	 float  garbageAlpha=1.0f;
	 
	float yDown=0;//碎片動畫的碎片 位移
	float zDown=0f;
	float sumY;
	TextureRect rect;//擋板
	TextureRect rectBig;//擋板
	float xAngle;//攝影機繞x軸旋轉的角度
	float yAngle;//攝影機繞y軸旋轉的角度
	float cx;//攝影機的x位置
	float cy;//攝影機的y位置
	float cz=-8;//攝影機的z位置
	float tx=0;//攝影機目的點的x位置
	float ty=0;//攝影機目的點的y位置
	float tz=-2f;//攝影機目的點的z位置
	float bx=0f;
	float by=0f;
	
	CubeThread cubeThread;
	pieceDownThread pdThread;
	Wall wall;
	GuanDaoThread guanThread;
	WallThread wallThread;
	float []piecePosition=new float[270];
	public BallThread ballThread;
	LineThread lineThread;
	ButtonThread buttonThread;
	CubeInfo cubeInfo;
	Circle circle;
	Circle circle1;
	
	HitCubeActivity activitya;
	
	int ballIds[]=new int[5];
	int ballId;
	
	
	int processBeijing;
	int loadId;
	
	int burstIds[]=new int[11];
	int burstId;
	TextureRectNo denghao;
	TextureRectNo pieces;
	TextureRectNo pause;
	TextureRectNo peng;                         
	TextureRectNo smallball;
	TextureRectNo number;
	TextureRectNo level;
	TextureRectNo burst;
	TextureRectNo stop;
	TextureRectDefen defen;
	
	int pauseId;
	TextureRectNo scorebord;
	int scorebordId;
	TextureRectNo ball;
	
	float ratio;
	
	TextureRectNo piece1;   
	TextureRectNo piece2;  
	TextureRectNo piece3;  
	TextureRectNo piece4;  
	int pCount=40;
	float []pieceSpeed1=new float[pCount*3];
	float []pieceSpeed2=new float[pCount*3];
	float []pieceSpeed3=new float[pCount*3];
	float []pieceSpeed4=new float[pCount*3];
	
	
	int bCount=20;
	TextureRectNo baiTiao;
	float []baiTiaos=new float[bCount*3];
	float []baiTiaoAngles=new float[bCount*2];
	int baiTiaoId;
	
	ArrayList<float[]> lightAl=new ArrayList<float[]>();
	TextureRectLight light;
	int lightId;
	
	//=========================================
	Line line;
	LoadedObjectVertexNormal lovo[]=new LoadedObjectVertexNormal[6];
	
	//======================================================
	TextureRect board;
	
	ButtonGraph bgExit;
	ButtonGraph bgQuit;
	ButtonGraph bgGoon;
	ButtonGraph winbgQuit;
	ButtonGraph bgNext;
	
	int exitId;//離開游戲按鈕
	int quitId;//離開按鈕
	int goonId;//繼續按鈕
	int boardId;
	int tailTex;
	
	TextureRectNo counter;
	TextureRectNo exit;
	TextureRectNo buttonRect;
	TextureRectNo scorenum;
	TextureRectNo winQuit;
	TextureRectNo score;
	TextureRectNo bonus;
	TextureRectNo sec;
	int scoreId;
	int boardScore[]=new int[9];//游戲界面計分

	
	int winTotalScore[]=new int[9];

	
	int winTimeScore[]=new int[9];

	
	int winLivesScore[]=new int[9];
	
	int counterIds[]=new int[4];
	int pursueId;
	int secId;
	int timeBonusId;
	int livesBonusId;
	int nextId;
	int tonguanId;
	int tailPieceTex;
	ButtonLine buttonLine[]=new ButtonLine[3];
	
	public MySurfaceView(Context context) {
		super(context);
		this.activity=(HitCubeActivity) context;
		 this.setEGLContextClientVersion(2); //設定使用OPENGL ES2.0
	     mRenderer = new SceneRenderer();	//建立場景著色器
	     setRenderer(mRenderer);				//設定著色器		        
	     setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設定著色模式為主動著色 
	}
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
        float y = e.getY();
        
        switch (e.getAction()){
        case MotionEvent.ACTION_MOVE:          	 
        	if(Constant.dangban_flag&&!Constant.win_flag&&!Constant.pause_flag&&y<=Constant.Game_Board_Top)
        	{
            	bx=(x-Constant.screenWidth/2)/(Constant.screenWidth/(2*Constant.UnitSize))+Constant.ball_X/2.5f;
              	by=-(y-Constant.screenHigth/2)/(Constant.screenWidth/(2*Constant.UnitSize))+Constant.ball_Y/10;
              	
        	}
        	break;
         case MotionEvent.ACTION_DOWN:

           	 if(!Constant.win_flag&&!Constant.pause_flag&&x>=Constant.game_Pause_Left&&x<=Constant.game_Pause_Right&&y>=Constant.game_Pause_Top&&y<=Constant.game_Pause_Bottom)
           	 {
               com.bn.cube.view.Constant.line_frag=true;
               Constant.button_flag=true;
               buttonThread=new ButtonThread(MySurfaceView.this); 
               lineThread=new LineThread(buttonLine);
               buttonThread.start();
               lineThread.start();
           		 Constant.dangban_flag=false;
           		 Constant.move_flag=!Constant.move_flag;
           		 Constant.pause_flag=!Constant.pause_flag;
           	 }else if(Constant.win_flag)
           	 { 
           		 Constant.dangban_flag=false;
           	 }
           	 else{
           		Constant.dangban_flag=true;
           	 }
        	 if(Constant.dangban_flag&&!Constant.win_flag&&!Constant.pause_flag&&y<=Constant.Game_Board_Top)
        	 {
             	bx=(x-Constant.screenWidth/2)/(Constant.screenWidth/(2*Constant.UnitSize));
              	by=-(y-Constant.screenHigth/2)/(Constant.screenWidth/(2*Constant.UnitSize));
        	 }
        	if(!Constant.pause_flag&&!Constant.win_flag&&x>=Constant.game_Shalou_Left&&x<=Constant.game_Shalou_Right&&y>=Constant.game_Shalou_Top&&y<=Constant.game_Shalou_Bottom)
       	 	{
       		 if(Constant.shalouCount==0)
       		 {
       			 shalouId=shalouredId;
       			 
       		 }
       		 if(!Constant.shalouFlag)
       		 {
       			 Constant.shalouFlag=true;
       			 Constant.threadTime=50;
       			 new ShaLouThread(MySurfaceView.this).start();
       		 }
       	 	}
           	 if(Constant.pause_flag&&x>=Constant.Pause_Exit_Left&&x<=Constant.Pause_Exit_Right&&y>=Constant.Pause_Exit_Top&&y<=Constant.Pause_Exit_Buttom)
           	 {
           		com.bn.cube.view.Constant.line_frag=false;
                 Constant.button_flag=false; 
           		 System.exit(0);
                 
           	 }else if(Constant.pause_flag&&x>=Constant.Pause_Go_Left&&x<=Constant.Pause_Go_Right&&y>=Constant.Pause_Go_Top&&y<=Constant.Pause_Go_Buttom)
           	 {
           		com.bn.cube.view.Constant.line_frag=false;
                 Constant.button_flag=false;
           		 Constant.move_flag=!Constant.move_flag;
           		 Constant.pause_flag=!Constant.pause_flag;
           	 }else if(Constant.pause_flag&&x>=Constant.Pause_Quit_Left&&x<=Constant.Pause_Quit_Right&&y>=Constant.Pause_Quit_Top&&y<=Constant.Pause_Quit_Buttom)
           	 {
           		activity.hd.sendEmptyMessage(0);
           	 }
           	 if(Constant.win_flag&&x>=Constant.Win_Quit_Left&&x<=Constant.Win_Quit_Right&&y>=Constant.Win_Quit_Top&&y<=Constant.Win_Quit_Buttom)
           	 {
           		activity.hd.sendEmptyMessage(0);
           		 
           	 }
           	 //=====================================================================================================
           	 else if(Constant.level<=6&&Constant.win_flag&&x>=Constant.Win_Next_Left&&x<=Constant.Win_Next_Right&&y>=Constant.Win_Next_Top&&y<=Constant.Win_Next_Buttom)
           	 {
//           		System.exit(0);
                 Constant.cube_flag=false; 
                 Constant.guan_flag=false;
                 Constant.wall_flag=false;
                 com.bn.cube.view.Constant.line_frag=false;
                 Constant.button_flag=false;
          	   Constant.AndScore_flag=false;
          	   Constant.win_flag=false;
          	   Constant.ball_X=0;
          	   Constant.ball_Y=0;
          	   Constant.ball_Z=-8;
          	  
          	   Constant.scoreNumberbase=0;
          	   Constant.second=500;
          	   Constant.sumBoardScore=Constant.sumTotalScore;
          	   Constant.dangban_flag=false;
          	   Constant.move_flag=false;
          	   Constant.ball_flag=false;
          	   Constant.level++;
          	   activity.hd.sendEmptyMessage(3);          	 
           	 }else if(Constant.level==7&&Constant.win_flag&&x>=Constant.Win_Next_Left&&x<=Constant.Win_Next_Right&&y>=Constant.Win_Next_Top&&y<=Constant.Win_Next_Buttom)
           	 {
                 Constant.cube_flag=false; 
                 Constant.guan_flag=false;
                 Constant.wall_flag=false;
                 com.bn.cube.view.Constant.line_frag=false;
                 Constant.button_flag=false;
          	   Constant.AndScore_flag=false;
          	   Constant.win_flag=false;
          	   Constant.ball_X=0;
          	   Constant.ball_Y=0;
          	   Constant.ball_Z=-8;
          	  
          	   Constant.scoreNumberbase=0;
          	   Constant.second=500;
          	   Constant.sumBoardScore=Constant.sumTotalScore;
          	   Constant.dangban_flag=false;
          	   Constant.move_flag=false;
          	   Constant.ball_flag=false;
//          	   Constant.level++;
          	   activity.hd.sendEmptyMessage(0);
           	 }
           //=====================================================================================================
        	 break;
        }
        Constant.curr_X=bx;
        Constant.curr_Y=by;
        return true;
	}
	
	
	public DrawBuffer texRect;//紋理矩形
	Tail tail;
	private class SceneRenderer implements GLSurfaceView.Renderer {

		
    	
		private boolean isFirstFrame=true;
		public void onDrawFrame(GL10 gl) {
			//清除深度緩沖與彩色緩沖
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            
            if(!isLoadedOk)//如過沒有載入完成
            {
            	 drawOrthLoadingView();            	 
            }
            else
            {
            	drawGameView();
            }
           
		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {	
			//設定視窗大小及位置  
			gl.glViewport((int)Constant.sxgame,(int)Constant.sygame,(int)(1024*Constant.ratio), (int)(720*Constant.ratio));
			ratio=(float)1024/720; 		 
			 
		}
		
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
			//設定螢幕背景色RGBA
            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);  
              
            //開啟深度檢驗
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //開啟背面剪裁   
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //起始化變換矩陣
            MatrixState.setInitStack();
            Constant.curr_X=0;
            Constant.curr_Y=0;
            isFirstFrame=true;
            isLoadedOk=false;
            Constant.shengliFlag=true;
            load_step=0;//載入資源的步數
            //建立載入界面的紋理矩形
            loadingView=new TextureRectNo(1, 1f, MySurfaceView.this);
            tex_loadingviewId=initTexture("beijing.png");
            processBar=new TextureRectNo(1f, 0.1f,MySurfaceView.this);
            tex_processId=initTexture("process.png"); 
            loading=new TextureRectNo(0.8f,0.1f,MySurfaceView.this);
            loadId=initTexture("load.png");
            processBeijing=initTexture("processbeijing.png");  
		}		
		
	
		public void drawGameView()
		{
            MatrixState.setProjectFrustum(-ratio*0.7f, ratio*0.7f, -1f*0.7f, 1f*0.7f, 1f, 100f);
            MatrixState.setCamera(Constant.ball_X/2.5f,Constant.ball_Y/10-0.4f, 1f,Constant.ball_X/2.5f,Constant.ball_Y/10-0.4f, -2f, 0, 1, 0);
            
            MatrixState.pushMatrix();
            gd.drawSelf(guanDaoId);
            MatrixState.popMatrix();
          
            drawCube();
            drawCircle();         

            if(Constant.cubePieceDrawFlag)
            {
            	drawCubePieces();
            }
            
            if(Constant.baiTiaoDrawFlag)
            {
            	drawbaiTiao();
            }
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -20f);
            wall.drawSelf();
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate(Constant.ball_X, Constant.ball_Y, Constant.ball_Z);
            ball.drawSelf(ballId);
            GLES20.glDisable(GLES20.GL_BLEND); 
            GLES20.glEnable(GLES20.GL_DEPTH_TEST); 
            MatrixState.popMatrix();            
                       
            GLES20.glDisable(GLES20.GL_CULL_FACE);
            MatrixState.pushMatrix();
            tail.drawSelf(tailTex);
            MatrixState.popMatrix();
            
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            
            float disToCenter=0.2f;
            for(float i=0.0f;gt.currTp!=null&&i<gt.currTp.length;i+=0.4)
            { 
            	disToCenter=0.1f;

            	  MatrixState.pushMatrix();  
            	  MatrixState.translate(Constant.ball_X+disToCenter, Constant.ball_Y+disToCenter, Constant.ball_Z);
            	  MatrixState.translate(-i*gt.currTp.selfVector[0], -i*gt.currTp.selfVector[1], -i*gt.currTp.selfVector[2]); 
            	  tailPieces.drawSelf(tailPieceTex,TailPart.normalBuffer);
                  MatrixState.popMatrix(); 
                  
                  MatrixState.pushMatrix();  
            	  MatrixState.translate(Constant.ball_X-disToCenter, Constant.ball_Y-disToCenter, Constant.ball_Z); 
                  MatrixState.translate(-i*gt.currTp.selfVector[0], -i*gt.currTp.selfVector[1], -i*gt.currTp.selfVector[2]); 
           	      tailPieces.drawSelf(tailPieceTex,TailPart.normalBuffer);
                  MatrixState.popMatrix();
                  
                  MatrixState.pushMatrix();  
                  MatrixState.translate(Constant.ball_X+disToCenter, Constant.ball_Y-disToCenter, Constant.ball_Z); 
                  MatrixState.translate(-i*gt.currTp.selfVector[0], -i*gt.currTp.selfVector[1], -i*gt.currTp.selfVector[2]); 
              	  tailPieces.drawSelf(tailPieceTex,TailPart.normalBuffer);
                  MatrixState.popMatrix();
                  
                  MatrixState.pushMatrix();  
            	  MatrixState.translate(Constant.ball_X-disToCenter, Constant.ball_Y+disToCenter, Constant.ball_Z);
              	  MatrixState.translate(-i*gt.currTp.selfVector[0], -i*gt.currTp.selfVector[1], -i*gt.currTp.selfVector[2]); 
              	  tailPieces.drawSelf(tailPieceTex,TailPart.normalBuffer);
                  MatrixState.popMatrix();
            } 
            
            
        	
            for(int j=0;j<gt.garbage.size();j++)
            { 
            	TailPart tempTpgt=gt.garbage.get(j);

            	for(float i=0.0f;tempTpgt!=null&&i<tempTpgt.length;i+=0.4)
                {
            		   
            		  disToCenter=0.1f;
                	  MatrixState.pushMatrix();
                	  MatrixState.translate(tempTpgt.endPoint[0]+disToCenter,tempTpgt.endPoint[1]+disToCenter, tempTpgt.endPoint[2]);
                	  MatrixState.translate(-i*tempTpgt.selfVector[0], -i*tempTpgt.selfVector[1], -i*tempTpgt.selfVector[2]); 
                	 
                	  tailPieces.drawSelf(tailPieceTex,tempTpgt.getAlphaBuffer());//garbageAlpha
                      MatrixState.popMatrix();
                      
                      MatrixState.pushMatrix();  
                	  MatrixState.translate(tempTpgt.endPoint[0]-disToCenter,tempTpgt.endPoint[1]-disToCenter,tempTpgt.endPoint[2]); 
                      MatrixState.translate(-i*tempTpgt.selfVector[0], -i*tempTpgt.selfVector[1], -i*tempTpgt.selfVector[2]); 
               	      tailPieces.drawSelf(tailPieceTex,tempTpgt.getAlphaBuffer());
                      MatrixState.popMatrix();
                      
                      MatrixState.pushMatrix();  
                      MatrixState.translate(tempTpgt.endPoint[0]+disToCenter, tempTpgt.endPoint[1]-disToCenter, tempTpgt.endPoint[2]); 
                      MatrixState.translate(-i*tempTpgt.selfVector[0], -i*tempTpgt.selfVector[1], -i*tempTpgt.selfVector[2]); 
                  	  tailPieces.drawSelf(tailPieceTex,tempTpgt.getAlphaBuffer());
                      MatrixState.popMatrix();
                      
                      MatrixState.pushMatrix();  
                	  MatrixState.translate(tempTpgt.endPoint[0]-disToCenter, tempTpgt.endPoint[1]+disToCenter, tempTpgt.endPoint[2]);
                  	  MatrixState.translate(-i*tempTpgt.selfVector[0], -i*tempTpgt.selfVector[1], -i*tempTpgt.selfVector[2]); 
                  	  tailPieces.drawSelf(tailPieceTex,tempTpgt.getAlphaBuffer());
                      MatrixState.popMatrix();
                }
            }
            GLES20.glDisable(GLES20.GL_BLEND); 
             GLES20.glEnable(GLES20.GL_DEPTH_TEST); 
            
            
            
            if(Constant.pieces_flag)
            { 
            	drawPieces();
            }
          
           if(Constant.drawdaojuFlag)
           {
        	   drawDaoJu();
           }
           
             
            if(Constant.dangBanBigFlag)
            {
            	 MatrixState.pushMatrix();
                 GLES20.glEnable(GLES20.GL_BLEND);
                 GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                 MatrixState.translate(0, 0, -4.5f);
                 rectBig.drawSelf(dangBanId);
                 GLES20.glDisable(GLES20.GL_BLEND);
                 MatrixState.popMatrix();      
            }else
            {
            	 MatrixState.pushMatrix();
                 GLES20.glEnable(GLES20.GL_BLEND);
                 GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                 MatrixState.translate(bx, by-0.4f, -4.5f);
                 rect.drawSelf(dangBanId);
                 GLES20.glDisable(GLES20.GL_BLEND);
                 MatrixState.popMatrix();             
            }
            drawLight();           
            
            drawOrth();
            
        
            if(Constant.pause_flag)
            {
                drawPause();
            }
            if(Constant.scoreNumberbase>=CubeInfo.cubeNum)            
            {
               int currLevel=0;
         	   Constant.AndScore_flag=true;
         	   Constant.win_flag=true;
         	   Constant.dangban_flag=false;
         	   Constant.move_flag=false;
         	   currLevel=SQLiteUtil.query();//從資料庫中取出對應的資料
          	   if(Constant.level>=currLevel&&temp)
          	   {
          		   SQLiteUtil.update(Constant.level+1);
          		   temp=false;
          	   } 
          	   if(Constant.shengliFlag)
          	   {
          		   if(activity.yinXiao)
          		   {
                		 activity.playSound(4, 0); 
          		   } 
          		Constant.shengliFlag=false;
          	   }
          	                 
          	   drawWin();
               
                
            }
            if(Constant.counter_flag)
            {
         	   drawCounter();
            } 
            if(Constant.deFenDrawFlag)
            {
                drawDeFen();
            }
		}
		public void drawDeFen()
		{
			if(Constant.deFenJiaFlag)
			{
				Constant.deFenId=jiaId;
			}else if(Constant.deFenJianFlag)
			{
				Constant.deFenId=jianId;
			}
			MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND); 
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate(0, 0f, 0);
            defen.drawSelf(Constant.deFenId);
            GLES20.glDisable(GLES20.GL_BLEND);  
            MatrixState.popMatrix();
            if(Constant.deFenThreadFlag==false)
            {
            	  Constant.deFenThreadFlag=true;
                  new DeFenThread().start();
            }
          
		}
	    //正交投影繪制載入界面
	    public void drawOrthLoadingView(){
	        if(isFirstFrame){ //若果是第一框
	        	MatrixState.pushMatrix();
	            MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//設定正交投影
	            MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//設定攝影機
	            loadingView.drawSelf(tex_loadingviewId);
	            MatrixState.popMatrix();
	        	isFirstFrame=false;
	        } else {//這裡進行資源的載入
	        	MatrixState.pushMatrix();
	            MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//設定正交投影
	            MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//設定攝影機
	            
	            loadingView.drawSelf(tex_loadingviewId);//繪制背景圖
	            
	            MatrixState.pushMatrix();  //設定進度指示器
	            
	             MatrixState.pushMatrix();
	             GLES20.glEnable(GLES20.GL_BLEND);
	             GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
	             GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	             MatrixState.translate(0, -1+0.08f, 0f);
	             processBar.drawSelf(processBeijing);
	             GLES20.glDisable(GLES20.GL_BLEND); 
	             GLES20.glEnable(GLES20.GL_DEPTH_TEST);  
	             MatrixState.popMatrix();
	            
	             GLES20.glEnable(GLES20.GL_BLEND);
	             GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
	             GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	             MatrixState.translate(-2+2*load_step/(float)25, -1+0.09f, 0f);
	             processBar.drawSelf(tex_processId);
	             GLES20.glDisable(GLES20.GL_BLEND); 
	             GLES20.glEnable(GLES20.GL_DEPTH_TEST); 
	             MatrixState.popMatrix();
	             
	            

	            
	             MatrixState.pushMatrix();
	             GLES20.glEnable(GLES20.GL_BLEND);
	             GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
	             GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	             MatrixState.translate(0.2f, 0.5f, 0);
	             loading.drawSelf(loadId);
	             GLES20.glDisable(GLES20.GL_BLEND); 
	             GLES20.glEnable(GLES20.GL_DEPTH_TEST); 
	             MatrixState.popMatrix();

	             
	            MatrixState.popMatrix();
	            loadResource();//載入資源的方法
	            return;    
	        } 
	        }
	}
	//載入所有的資源
	public void loadResource()
	{
		switch(load_step)
		{
		case 0:
//			init_Shader();
			load_step++;
			break;
		case 1:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 2:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 3:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 4:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 5:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 6:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 7:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 8:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 9:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 10:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 11:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 12:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 13:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 14:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 15:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 16:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 17:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 18:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 19:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 20:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 21:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 22:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 23:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 24:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 25:
			init_All_Texture(load_step);
			isLoadedOk=true;//切換到一級選單
//			isMenuLevel=1;//切換到一級選單
			loadingView=null;//銷毀
			processBar=null;//銷毀
			break;
		}
	}
	//載入紋理的方法
	public void init_All_Texture(int index)
	{
		switch(index)
		{
		case 1:
          cubeInfo=new CubeInfo(MySurfaceView.this);
          CubeInfo.initCube();
          Constant.move_flag=true;
          temp=true;
          break;
		case 2:
		case 3:	
			MatrixState.setLightLocation(0, 0, 0);
			lovo[0]=LoadUtil.loadFromFile("yazi.obj",MySurfaceView.this.getResources() , MySurfaceView.this);
			break;
		case 4:  
          guanDaoId=initTexture("guandao.png");
          dangBanId=initTexture("dangban.png");                      
          contId=initTexture("cont.png");                                                   
          ballIds[0]=initTexture("ball1.png");           
          ballIds[1]=initTexture("ball2.png");            
          ballIds[2]=initTexture("ball3.png");
          ballIds[3]=initTexture("ball4.png");
          ballIds[4]=initTexture("ball5.png");     
          scoreId=initTexture("score.png");   
          break;
		case 5:
          piece1Id=initTexture("pieces1.png");
          piece2Id=initTexture("pieces4.png");
          piece3Id=initTexture("pieces3.png");
          piece4Id=initTexture("pieces2.png");
          
          counterIds[0]=initTexture("go.png");
          counterIds[1]=initTexture("num1.png");              
          counterIds[2]=initTexture("num2.png");
          counterIds[3]=initTexture("num3.png");
          
          secId=initTexture("sec.png");  
          pursueId=initTexture("pursue.png");
          timeBonusId=initTexture("timebonus.png");
          livesBonusId=initTexture("livesbonus.png");
          nextId=initTexture("next.png");
          tonguanId=initTexture("tongguan.png");
          break;
		case 6:
		case 7:	
			lovo[1]=LoadUtil.loadFromFile("tuzi.obj",MySurfaceView.this.getResources() , MySurfaceView.this);						
			break;
		case 8:  
          ballIds[0]=initTexture("ball1.png");           
          ballIds[1]=initTexture("ball2.png");            
          ballIds[2]=initTexture("ball3.png");
          ballIds[3]=initTexture("ball4.png");
          ballIds[4]=initTexture("ball5.png"); 
          burstIds[0]=initTexture("burst1.png");                     
          burstIds[1]=initTexture("burst2.png");
          burstIds[2]=initTexture("burst3.png");
          burstIds[3]=initTexture("burst4.png");
          burstIds[4]=initTexture("burst5.png");
          burstIds[5]=initTexture("burst6.png");
          burstIds[6]=initTexture("burst7.png");
          burstIds[7]=initTexture("burst8.png");
          burstIds[8]=initTexture("burst9.png");
          burstIds[9]=initTexture("burst10.png");             
          burstIds[10]=initTexture("burst11.png");
          break;
		case 9:
          exitId=initTexture("quit.png");
          quitId=initTexture("backt.png");
          goonId=initTexture("goon.png");
          boardId=initTexture("board.png");   
          smallBallId=initTexture("smallball.png");
          hengGangId=initTexture("hengxian.png");
          dengHaoId=initTexture("denghao.png");
          levelId=initTexture("level.png");
          jiaId=initTexture("plus500.png");
          jianId=initTexture("minus200.png");
          numberId[0]=initTexture("n0.png");
          numberId[1]=initTexture("n1.png");
          numberId[2]=initTexture("n2.png");
          numberId[3]=initTexture("n3.png");
          numberId[4]=initTexture("n4.png");
          numberId[5]=initTexture("n5.png");
          numberId[6]=initTexture("n6.png");
          numberId[7]=initTexture("n7.png");
          numberId[8]=initTexture("n8.png");
          numberId[9]=initTexture("n9.png");
          pengId=initTexture("peng.png");
          pauseId=initTexture("stop.png");
          scorebordId=initTexture("scoreboard.png");    
          break;
		case 10:	
		case 11:				
			lovo[2]=LoadUtil.loadFromFile("wugui.obj",MySurfaceView.this.getResources() , MySurfaceView.this);				
			break;
		case 12:  
          bgExit=new ButtonGraph(MySurfaceView.this,0.13f,new float[]{1f,0f,0f,0});
          bgQuit=new ButtonGraph(MySurfaceView.this,0.16f,new float[]{1f,1f,0f,0});
          bgGoon=new ButtonGraph(MySurfaceView.this,0.16f,new float[]{0,1f,0f,0});
          winbgQuit=new ButtonGraph(MySurfaceView.this,0.14f,new float[]{1,0f,0f,0});
          bgNext=new ButtonGraph(MySurfaceView.this,0.16f,new float[]{0,1f,0f,0});
          
          buttonLine[0]=new ButtonLine(MySurfaceView.this,0.13f,0.137f,0f,new float[]{0,0,1,0});
          buttonLine[1]=new ButtonLine(MySurfaceView.this,0.16f,0.168f,0f,new float[]{0,0,1,0});
          buttonLine[2]=new ButtonLine(MySurfaceView.this,0.14f,0.147f,0f,new float[]{0,0,1,0});
          initPosition();
          break;
		case 13:      
          rect=new TextureRect(1.5f,1f,MySurfaceView.this);
          rectBig=new TextureRect(9f,6f,MySurfaceView.this);
          board=new TextureRect(0.7f,0.7f,MySurfaceView.this);
          ball=new TextureRectNo(0.4f,0.4f,MySurfaceView.this);
          denghao=new TextureRectNo(0.03f,0.03f,MySurfaceView.this);
          pieces=new TextureRectNo(0.08f,0.08f,MySurfaceView.this);          
          pause=new TextureRectNo(0.1f,0.1f,MySurfaceView.this);
          scorebord=new TextureRectNo(1.2f,0.3f,MySurfaceView.this);                   
          smallball=new TextureRectNo(0.03f,0.03f,MySurfaceView.this);
          number=new TextureRectNo(0.014f,0.03f,MySurfaceView.this);
          level=new TextureRectNo(0.12f,0.03f,MySurfaceView.this);
          peng=new TextureRectNo(0.6f,0.3f,MySurfaceView.this);
          break;
		case 14:
		case 15:
			lovo[3]=LoadUtil.loadFromFile("xiangjiao.obj",MySurfaceView.this.getResources() , MySurfaceView.this);
			break;
		case 16:  
          burst=new TextureRectNo(0.27f,0.03f,MySurfaceView.this);
          stop=new TextureRectNo(0.13f,0.1f,MySurfaceView.this);
          exit=new TextureRectNo(0.1f,0.1f,MySurfaceView.this);
          buttonRect=new TextureRectNo(0.12f,0.09f,MySurfaceView.this);
          winQuit=new TextureRectNo(0.12f,0.09f,MySurfaceView.this);
          sec=new TextureRectNo(0.12f,0.03f,MySurfaceView.this);
          score=new TextureRectNo(0.2f,0.1f,MySurfaceView.this);
          bonus=new TextureRectNo(0.28f,0.07f,MySurfaceView.this);
          scorenum=new TextureRectNo(0.02f,0.04f,MySurfaceView.this);
          counter=new TextureRectNo(0.1f,0.1f,MySurfaceView.this); 
          defen=new TextureRectDefen(0.2f,0.1f,MySurfaceView.this);
          break;


		case 17:	
			line=new Line(MySurfaceView.this,new float[]{1,1,1,0},0.12f);
	        wall=new Wall(MySurfaceView.this,4.5f,3f,new float[]{0.8f,0,0,0});
	        //========================================================
	        piece1=new TextureRectNo(0.08f,0.08f,MySurfaceView.this);
	        piece2=new TextureRectNo(0.08f,0.08f,MySurfaceView.this);
	        piece3=new TextureRectNo(0.08f,0.08f,MySurfaceView.this);
	        piece4=new TextureRectNo(0.08f,0.08f,MySurfaceView.this);
	        circle=new Circle(MySurfaceView.this,5,new float[]{0,1,0,0});
	        circle1=new Circle(MySurfaceView.this,4,new float[]{0,0,1,0});
	        light=new TextureRectLight(2.0f,2.0f,MySurfaceView.this);
	        lightId=initTexture("light.png");
	        //================================
	        shalou=new TextureRectShaLou(0.1f,0.1f,MySurfaceView.this);
	        shaloupuId=initTexture("loudou.png");
	        shalouredId=initTexture("loudoured.png");
	        shalouId=shaloupuId;
			break;
		case 18:	
		case 19:
			lovo[4]=LoadUtil.loadFromFile("baoxiang.obj",MySurfaceView.this.getResources() , MySurfaceView.this);
			break;
		case 20:
			gd=new GuanDao(MySurfaceView.this,4.5f,3f);
			break;
		case 21:
	          initCubePieces();
	          initbaiTiao();
	          break;
		case 22:			 
	          Constant.wall_flag=true;
	          wallThread=new WallThread(wall); 
	          wallThread.start();
	          break;	
		case 23:
	          Constant.cube_flag=true;
	          Constant.guan_flag=true;
	          cubeThread=new CubeThread(CubeInfo.cube,CubeInfo.colors);
	          guanThread=new GuanDaoThread();
	          cubeThread.start();
	          guanThread.start();
	          
	        
//				break;
	          break;
		case 24:

	          new Thread(){
	            	
	            	public void  run()
	            	{
	            		int yuanId=0;
	            		int burst=0;
	            		while(Constant.mySur_flag)
	            		{
	            			burstId=burstIds[burst];
	            			ballId=ballIds[yuanId];
	            			yuanId++;
	            			yuanId=yuanId%5; 
	            			burst++;
	            			burst=burst%4;
//	            			if(Constant.ball_Z>-5)
//	            			{
//	            				Constant.pieces_flag=true;
//	            				new pieceDownThread().start();
//	            			}
	              			if(Constant.pieces_flag)
	            			{
	            				new pieceDownThread().start();
	            			}
	            			if(Constant.AndScore_flag)
	            			{
	                			if(bl)
	                			{

	                				new AndScoreThread().start();
	                			}
	                			bl=false;
	            			}
	            			try{
	            				sleep(300);
	            			}catch(Exception e)
	            			{
	            				e.printStackTrace();
	            			}
	            		}
	            	}
	            }.start();
				Constant.ball_flag=true;
				ballThread=new BallThread(MySurfaceView.this,activity);
				ballThread.start();
				tailPieceTex=initTexture("tailpiece.png"); 
				tailPieces=new PieceTexture(0.05f,0.05f,MySurfaceView.this); 

				  tailTex=initTexture("weiba3.png");
		          texRect=new DrawBuffer(MySurfaceView.this); 
		          tail=new Tail(texRect);
		          gt=new GoThread(tail,ball,MySurfaceView.this);//
		          gt.start();
	            break;
		case 25:
		} 
	}
	
	public class CubePieceThread extends Thread//碎片動畫
	{
		@Override
		public void run()
		{
			int count=0;
			
			while(Constant.cubePieceThreadFlag)
			{
				Constant.pieceX+=0.1f;
				Constant.pieceY+=0.1f;
				Constant.pieceZ+=0.2f;
				count++;
				if(count>12)
				{
					Constant.cubePieceThreadFlag=false;
					//Constant.cubePieceInitFlag=false;
					Constant.cubePieceDrawFlag=false;
					Constant.pieceX=0.0f;
					Constant.pieceY=0.0f;
					Constant.pieceZ=0.0f;
				
				}
				try
				{
					Thread.sleep(50);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
		}
	}
	public class pieceDownThread extends Thread//碎片動畫
	{
		@Override
		public void run()
		{
			pieceId=piece2Id;
			while(yDown>-3)
			{
				yDown-=0.1f;
				zDown-=0.2f;
				if(yDown<-1.6)
				{
					pieceId=piece1Id;  
				}
				try
				{
					Thread.sleep(50);
				}
				catch
				(Exception e)
				{
				}
			}
			yDown=0;
			zDown=0f;
			pieceId=piece2Id;
			Constant.pieces_flag=false;
		}
	}
	
	public class AndScoreThread extends Thread
	{
		 
		@Override
		public void run()
		{
			Constant.sumTotalScore=Constant.sumBoardScore;
			Constant.sunLivesScore=Constant.lifeNum*2500;
			Constant.sumTimeScore=(int)Constant.second*100;
			try{
				sleep(2000);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			while(Constant.AndScore_flag)
			{
				if(Constant.sumTimeScore>=100) 
				{
					Constant.sumTimeScore-=100;
					Constant.sumTotalScore+=100;
				}
				if(Constant.sunLivesScore>=100) 
				{
					Constant.sunLivesScore-=100;
					Constant.sumTotalScore+=100;
				}
				if(Constant.second<=0&&Constant.sunLivesScore<=0)
				{
					Constant.AndScore_flag=false;
				}
				try
				{
					Thread.sleep(60);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public class lightThread extends Thread
	{
		@Override
		public void run()
		{
			        int sleepTime=50;
					while(Constant.lightFlag)
					{
						Constant.uA++;
						if(Constant.uA<=30)
						{
							Constant.uAId=0;
							sleepTime=15;
						}else if(Constant.uA<=130)
						{
							Constant.uAId=1f;
							sleepTime=30;
						}else if(Constant.uA<=330)
						{
							Constant.uAId=2f;
						}
						
					
		
					if(Constant.uA>330)
					{
						Constant.lightFlag=false;
						Constant.peng_flag=false;
						//Constant.lightCount=10;
						Constant.uA=0;
						Constant.uAId=0;
					}
					try
					{ 
						sleep(sleepTime);
					}catch(Exception e)
					{ 
						e.printStackTrace();
					}
					}
			//	}
					
		}
	}
    public void drawPause()
    {
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0, 0, 0.2f);
        board.drawSelf(boardId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        MatrixState.translate(0f, 0.3f,0.3f);
        bgExit.drawSelf();
        buttonLine[0].drawSelf();
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0f, 0.3f,0.5f);
        exit.drawSelf(exitId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        MatrixState.translate(-0.3f, -0.1f, 0.3f);
        bgQuit.drawSelf();
        buttonLine[1].drawSelf();
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.3f, -0.1f, 0.5f);
        buttonRect.drawSelf(quitId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        MatrixState.translate(0.3f, -0.1f, 0.3f);
        bgGoon.drawSelf();
        buttonLine[1].drawSelf();
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0.3f, -0.1f, 0.5f);
        buttonRect.drawSelf(goonId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
    }
    public void drawCounter()
    {
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0, 0, -4f);
        counter.drawSelf(counterIds[Constant.pictureId]);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
    }
    public void drawWin()
    {
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0, 0, 0.2f);
        board.drawSelf(boardId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0, 0.5f, 0.3f);
        score.drawSelf(scoreId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        winTotalScore[8]=Constant.sumTotalScore/10000000;
        winTotalScore[7]=Constant.sumTotalScore%10000000/1000000;
        winTotalScore[6]=Constant.sumTotalScore%1000000/100000;
        winTotalScore[5]=Constant.sumTotalScore%100000/10000;
        winTotalScore[4]=Constant.sumTotalScore%10000/1000;
        winTotalScore[3]=Constant.sumTotalScore%1000/100; 
        //總分數
        for(int i=1;i<=8;i++)
        {
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate((float)(0.14f-0.04*(i-1)),0.35f, 0.3f);
            scorenum.drawSelf(numberId[winTotalScore[i]]);                 
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
        }
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.48f,0.25f, 0.3f);
        bonus.drawSelf(timeBonusId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.45f,0f, 0.3f);
        bonus.drawSelf(livesBonusId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        MatrixState.translate(-0.4f, -0.38f, 0.3f);
        winbgQuit.drawSelf();
        buttonLine[2].drawSelf();
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.4f, -0.38f, 0.5f);
        winQuit.drawSelf(quitId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        MatrixState.translate(0.45f, -0.3f, 0.3f);
        bgNext.drawSelf();
        buttonLine[1].drawSelf();
        MatrixState.popMatrix();
        
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0.45f, -0.3f, 0.5f);
        if(Constant.level==7)
        {
        	buttonRect.drawSelf(tonguanId);
        }else
        {
            buttonRect.drawSelf(nextId);
        }
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();

        //======================100============================
        //剩余秒數
        int second[]=new int[4];
        int tempSecond=(int)Constant.second;
        second[3]=tempSecond/100;
        second[2]=tempSecond%100/10;
        second[1]=tempSecond%10;
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.6f,0.12f, 0.3f);
        scorenum.drawSelf(numberId[second[3]]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.56f,0.12f, 0.3f);
        scorenum.drawSelf(numberId[second[2]]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.52f,0.12f, 0.3f);
        scorenum.drawSelf(numberId[second[1]]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        
        //100分
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.1f,0.12f, 0.3f);
        scorenum.drawSelf(numberId[0]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.14f,0.12f, 0.3f);
        scorenum.drawSelf(numberId[0]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.18f,0.12f, 0.3f);
        scorenum.drawSelf(numberId[1]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
                
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.04f,0.12f, 0.3f);
        denghao.drawSelf(dengHaoId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.28f,0.12f, 0.3f);
        denghao.drawSelf(pursueId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.38f,0.12f, 0.3f);
        sec.drawSelf(secId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        
        //時間剩余得分
        winTimeScore[8]=Constant.sumTimeScore/10000000;
        winTimeScore[7]=Constant.sumTimeScore%10000000/1000000;
        winTimeScore[6]=Constant.sumTimeScore%1000000/100000;
        winTimeScore[5]=Constant.sumTimeScore%100000/10000;
        winTimeScore[4]=Constant.sumTimeScore%10000/1000;
        winTimeScore[3]=Constant.sumTimeScore%1000/100;
        for(int i=1;i<=8;i++)
        {
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate((float)(0.3f-0.04*(i-1)),0.12f, 0.3f);
            scorenum.drawSelf(numberId[winTimeScore[i]]);                 
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
        }
        //=======================2500===========================
        //剩余生命值
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.6f,-0.12f, 0.3f);
        scorenum.drawSelf(numberId[0]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.56f,-0.12f, 0.3f);
        scorenum.drawSelf(numberId[0]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.52f,-0.12f, 0.3f);
        scorenum.drawSelf(numberId[Constant.lifeNum]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        //2500分
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.1f,-0.12f, 0.3f);
        scorenum.drawSelf(numberId[0]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.14f,-0.12f, 0.3f);
        scorenum.drawSelf(numberId[0]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.18f,-0.12f, 0.3f);
        scorenum.drawSelf(numberId[5]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.22f,-0.12f, 0.3f);
        scorenum.drawSelf(numberId[2]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.04f,-0.12f, 0.3f);
        denghao.drawSelf(dengHaoId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.28f,-0.12f, 0.3f);
        denghao.drawSelf(pursueId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        winLivesScore[8]=Constant.sunLivesScore/10000000;
        winLivesScore[7]=Constant.sunLivesScore%10000000/1000000;
        winLivesScore[6]=Constant.sunLivesScore%1000000/100000;
        winLivesScore[5]=Constant.sunLivesScore%100000/10000;
        winLivesScore[4]=Constant.sunLivesScore%10000/1000;
        winLivesScore[3]=Constant.sunLivesScore%1000/100;                
        for(int i=1;i<=8;i++)
        {
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate((float)(0.3f-0.04*(i-1)),-0.12f, 0.3f);
            scorenum.drawSelf(numberId[winLivesScore[i]]);                 
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
        }
        if(Constant.button_flag==false)
        {
        	com.bn.cube.view.Constant.line_frag=true;
            Constant.button_flag=true;
            buttonThread=new ButtonThread(MySurfaceView.this); 
            lineThread=new LineThread(buttonLine);
            buttonThread.start();
            lineThread.start();
        }

    }
	public void drawLight() 
	{
		MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(Constant.lightX, Constant.lightY, Constant.lightZ);
        light.drawSelf(lightId);
        GLES20.glDisable(GLES20.GL_BLEND); 
        MatrixState.popMatrix(); 
         
        // Constant.lightCount=10;
         if(Constant.peng_flag)
         {
        	 Constant.lightFlag=true;
        	  new lightThread().start();
         }
      
	}
	public void initPosition()
	{
		for(int i=0;i<piecePosition.length;i+=3)
		{
			signx=(Math.random()>0.5)?-1:1;
			signy=(Math.random()>0.5)?-1:1;
			double temp0=Math.random(); 
			double temp1=Math.random(); 
			double temp2=Math.random(); 
			piecePosition[i]=(float)temp0*signx*5;
			piecePosition[i+1]=(float)temp1*signy*3;
			piecePosition[i+2]=(float)temp2;
		}
	}
	//=============================================
	public void drawDaoJu()
	{
		 GLES20.glEnable(GLES20.GL_BLEND);
         GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		 MatrixState.pushMatrix();
         MatrixState.translate(Constant.daojuX, Constant.daojuY,Constant.daojuZ);
         MatrixState.scale(0.1f, 0.1f,0.1f);
         lovo[Constant.daojuId].drawSelf();
         MatrixState.popMatrix();
         GLES20.glDisable(GLES20.GL_BLEND);
	}
	public void drawPieces()
	{
		 	GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.pushMatrix();
            MatrixState.translate(0,yDown,zDown);
		for(int i=0;i<piecePosition.length;i+=3)
		{
			MatrixState.pushMatrix();
			if(Constant.ball_Y<-4)
			{
				MatrixState.translate(piecePosition[i], piecePosition[i+1], piecePosition[i+2]);
			}else
			{
				MatrixState.translate(piecePosition[i], piecePosition[i+1], piecePosition[i+2]);
			}		
			pieces.drawSelf(pieceId);
		    MatrixState.popMatrix();
		}
		  MatrixState.popMatrix();
		GLES20.glDisable(GLES20.GL_BLEND);
	
	}
	public void drawPeng()
	{
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0, 0, -5);
        peng.drawSelf(pengId);
        GLES20.glDisable(GLES20.GL_BLEND); 
        MatrixState.popMatrix();
	}
    public void drawCube()
    {
    	for(float []cubes:CubeInfo.cubes)
    	{
    		if(cubes[4]==0)
    		{
    			MatrixState.pushMatrix();
    			MatrixState.translate(cubes[0], cubes[1], cubes[2]);
    			CubeInfo.cube[(int) cubes[3]].drawSelf();
    			MatrixState.popMatrix();
    		}
    	}
    	
         
    }
    public void drawCircle()
    {
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, Constant.ball_Z);
        circle.drawSelf();
        MatrixState.popMatrix(); 

        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -5);
        circle1.drawSelf();
        MatrixState.popMatrix();
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -4.95f);
        circle1.drawSelf();
        MatrixState.popMatrix();
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -4.9f);
        circle1.drawSelf();
        MatrixState.popMatrix();
    }
    public void drawOrth()
    {
    	MatrixState.setProjectOrtho(-ratio*0.7f, ratio*0.7f, -1f*0.7f, 1f*0.7f, 1f, 100f);
   	 	MatrixState.setCamera(0f,0f,2f,0f,0.0f,-2f,0f,1.0f,0.0f); 
   	 	MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.scale(1f, 0.5f, 0);
        MatrixState.translate(0, -1.1f,0);
        scorebord.drawSelf(scorebordId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.rotate(20, -1, 0, 0);
        MatrixState.rotate(30, 0, 1, 0);
        MatrixState.translate(0.62f,-0.68f, 0.6f);
        stop.drawSelf(pauseId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();       
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.1f,-0.565f, 0.2f);
        smallball.drawSelf(smallBallId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.05f,-0.565f, 0.2f);
        denghao.drawSelf(dengHaoId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0f,-0.632f, 0.2f);
        burst.drawSelf(burstId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0f,-0.565f, 0.2f);
        number.drawSelf(numberId[lifeNumber1]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0.03f,-0.565f, 0.2f);
        number.drawSelf(numberId[lifeNumber2]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0.06f,-0.565f, 0.2f);
        number.drawSelf(numberId[Constant.lifeNum]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0.42f,-0.565f, 0.2f);
        denghao.drawSelf(hengGangId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0.22f,-0.565f, 0.2f);
        level.drawSelf(levelId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0.37f,-0.565f, 0.2f);
        number.drawSelf(numberId[bigLevelNumber]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0.47f,-0.565f, 0.2f);
        number.drawSelf(numberId[Constant.level]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();     
        //================================================================
        boardScore[8]=Constant.sumBoardScore/10000000;
        boardScore[7]=Constant.sumBoardScore%10000000/1000000;
        boardScore[6]=Constant.sumBoardScore%1000000/100000;
        boardScore[5]=Constant.sumBoardScore%100000/10000;
        boardScore[4]=Constant.sumBoardScore%10000/1000;
        boardScore[3]=Constant.sumBoardScore%1000/100;
        //=================================================================
        for(int i=1;i<=8;i++)
        {
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate((float)(-0.26f-0.03*(i-1)),-0.565f, 0.2f);
            number.drawSelf(numberId[boardScore[i]]);                 
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
        }
         
         drawShaLou();
    }

    
    //====================================
    public void drawShaLou()
    {
    	
    	  MatrixState.pushMatrix();
          GLES20.glEnable(GLES20.GL_BLEND);
          GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
          MatrixState.translate(-0.85f,-0.58f, 0.2f);
          MatrixState.rotate(20, 1, 0, 0);
          MatrixState.rotate(30, 0, 1, 0);
          MatrixState.rotate(Constant.shalouAngle, 0, 0, 1);
          shalou.drawSelf(shalouId);
          GLES20.glDisable(GLES20.GL_BLEND);
          MatrixState.popMatrix();
    }
    public void initCubePieces(){
    	
    	for(int i=0;i<pCount;i++)
    	{
    		float sx=(float) ((Math.random()-0.5f)*2f);
    		float sy=(float) ((Math.random()-0.5f)*2f);
    		float sz=(float) ((Math.random()-0.5f)*2f);
    		pieceSpeed1[3*i]=sx;
    		pieceSpeed1[3*i+1]=sy;
    		pieceSpeed1[3*i+2]=sz;
    		
    	}
    	for(int i=0;i<pCount;i++)
    	{
    		float sx=(float) ((Math.random()-0.5f)*2f);
    		float sy=(float) ((Math.random()-0.5f)*2f);
    		float sz=(float) ((Math.random()-0.5f)*2f);
    		pieceSpeed2[3*i]=sx;
      		pieceSpeed2[3*i+1]=sy;
      		pieceSpeed2[3*i+2]=sz;
    		
    	}
    	for(int i=0;i<pCount;i++)
    	{
    		float sx=(float) ((Math.random()-0.5f)*2f);
    		float sy=(float) ((Math.random()-0.5f)*2f);
    		float sz=(float) ((Math.random()-0.5f)*2f);
    		pieceSpeed3[3*i]=sx;
    		pieceSpeed3[3*i+1]=sy;
    		pieceSpeed3[3*i+2]=sz;
    		
    	}
    	for(int i=0;i<pCount;i++)
    	{
    		float sx=(float) ((Math.random()-0.5f)*2f);
    		float sy=(float) ((Math.random()-0.5f)*2f);
    		float sz=(float) ((Math.random()-0.5f)*2f);
    		pieceSpeed4[3*i]=sx;
    		pieceSpeed4[3*i+1]=sy;
    		pieceSpeed4[3*i+2]=sz;
    		
    	}    	  		    	
    }
    public void drawCubePieces(){
    	for(int i=0;i<pCount;i++)
        	{       		
        		GLES20.glEnable(GLES20.GL_BLEND);
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        		MatrixState.pushMatrix();
        		MatrixState.translate(Constant.sx+Constant.pieceX*pieceSpeed1[3*i], Constant.sy+Constant.pieceY*pieceSpeed1[3*i+1], Constant.sz+Constant.pieceZ*pieceSpeed1[3*i+2]);
        		piece1.drawSelf(piece1Id);
        		MatrixState.popMatrix();
        		GLES20.glDisable(GLES20.GL_BLEND);
        		
        		GLES20.glEnable(GLES20.GL_BLEND);
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        		MatrixState.pushMatrix();
        		MatrixState.translate(Constant.sx+Constant.pieceX*pieceSpeed2[3*i], Constant.sy+Constant.pieceY*pieceSpeed2[3*i+1], Constant.sz+Constant.pieceZ*pieceSpeed2[3*i+2]);
        		piece2.drawSelf(piece2Id);
        		MatrixState.popMatrix();
        		GLES20.glDisable(GLES20.GL_BLEND);
        		
        		GLES20.glEnable(GLES20.GL_BLEND);
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        		MatrixState.pushMatrix();
        		MatrixState.translate(Constant.sx+Constant.pieceX*pieceSpeed3[3*i], Constant.sy+Constant.pieceY*pieceSpeed3[3*i+1], Constant.sz+Constant.pieceZ*pieceSpeed3[3*i+2]);
        		piece3.drawSelf(piece3Id);
        		MatrixState.popMatrix();
        		GLES20.glDisable(GLES20.GL_BLEND);
        		
        		GLES20.glEnable(GLES20.GL_BLEND);
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        		MatrixState.pushMatrix();
        		MatrixState.translate(Constant.sx+Constant.pieceX*pieceSpeed4[3*i], Constant.sy+Constant.pieceY*pieceSpeed4[3*i+1], Constant.sz+Constant.pieceZ*pieceSpeed4[3*i+2]);
        		piece4.drawSelf(piece4Id);
        		MatrixState.popMatrix();
        		GLES20.glDisable(GLES20.GL_BLEND);
        	}
    	Constant.cubePieceThreadFlag=true;
    	new CubePieceThread().start();
    }
    public void initbaiTiao(){
    	
    	for(int i=0;i<bCount;i++)
    	{    	    		
    		float angleTempJD=(float) (Math.PI*2*Math.random());
        	float angleTempWD=(float) (Math.PI*(Math.random()-0.5f));
        	float sx=(float)(Math.cos(angleTempWD)*Math.cos(angleTempJD));
        	float sy=(float)(Math.sin(angleTempWD));
        	float sz=(float)(Math.cos(angleTempWD)*Math.sin(angleTempJD));
    		
    		baiTiaos[3*i]=sx;
    		baiTiaos[3*i+1]=sy;
    		baiTiaos[3*i+2]=sz;
 //   		baiTiaoAngles[i]=(float) (Math.random()*180);
    		baiTiaoAngles[2*i]=(float) (angleTempJD*180.0f/Math.PI);
    		baiTiaoAngles[2*i+1]=(float) (angleTempWD*180.0f/Math.PI);
    		
    	}
    }
    public void drawbaiTiao(){
    	for(int i=0;i<bCount;i++)
        	{       		
        		MatrixState.pushMatrix();        		
        		MatrixState.translate(Constant.lightX+Constant.baiTiaoX*baiTiaos[3*i], Constant.lightY+Constant.baiTiaoY*baiTiaos[3*i+1], Constant.lightZ+Constant.baiTiaoZ*baiTiaos[3*i+2]);
        		MatrixState.rotate(baiTiaoAngles[2*i], 0, 1, 0);
        		MatrixState.rotate(baiTiaoAngles[2*i+1], 0, 0, 1);
        		line.drawSelf();
        		MatrixState.popMatrix();
        	}
    	
    	if(Constant.baiTiaoThreadFlag==false)
    	{
    		Constant.baiTiaoThreadFlag=true;
        	new baiTiaoThread().start();
    	}
    	
    }
    public class baiTiaoThread extends Thread//碎片動畫
	{
		@Override
		public void run()
		{
			int count=0;
			
			while(Constant.baiTiaoThreadFlag)
			{
				if(Constant.move_flag)
				{
				Constant.baiTiaoX+=0.3f;
				Constant.baiTiaoY+=0.3f;
				Constant.baiTiaoZ-=0.3f;
				count++;
				if(count>30)
				{
					Constant.baiTiaoThreadFlag=false;
					//Constant.cubePieceInitFlag=false;
					Constant.baiTiaoDrawFlag=false;
					Constant.baiTiaoX=0.0f;
					Constant.baiTiaoY=0.0f;
					Constant.baiTiaoZ=0.0f;
				
				}
				}
				try
				{
					Thread.sleep(15);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
		}
	}

	public int initTexture(String fname)
	{
		int[] textures=new int[1];
		GLES20.glGenTextures
		(
				1,          //產生的紋理id的數量
				textures,   //紋理id的陣列
				0           //偏移量
		);    
		int textureId=textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
		
		InputStream is=null;
		try {
			is=this.getResources().getAssets().open(fname);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Bitmap bitmapTemp;
		try
		{
			bitmapTemp=BitmapFactory.decodeStream(is);
					
		}finally
		{
			try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
		}
		GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //紋理型態，在OpenGL ES中必須為GL10.GL_TEXTURE_2D
        		0, 					  //紋理的階層，0表示基本圖形層，可以瞭解為直接貼圖
        		bitmapTemp, 			  //紋理圖形
        		0					  //紋理邊框尺寸
        );
        bitmapTemp.recycle(); 		  //紋理載入成功後釋放圖片
        
        return textureId;
	}
}
