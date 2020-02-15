package com.bn.gjxq.game;

import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.gjxq.Constant;
import com.bn.gjxq.GJXQActivity;
import com.bn.gjxq.manager.PicDataManager;
import com.bn.gjxq.manager.VertexDataManager;
import com.bn.gjxq.manager.VertexTexture3DObjectForDraw;
import com.bn.gjxq.manager.VertexTextureNormal3DObjectForDraw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import static com.bn.gjxq.Constant.*;

public class GameSurfaceView extends GLSurfaceView{
	
	GJXQActivity activity;
	
	float x,y;					//觸控點的X與Y座標
	
	boolean isDrawSmallQP=true;  //是否繪制小棋碟標志位
	float mPreviousX,mPreviousY;
	float startX,startY;
	boolean isMove=false;
	final float MOVE_THOLD=30;	//觸控時判斷是否是move動作

	public SceneRenderer mRenderer; 
	
	public Object aqLock=new Object();	//動作佇列鎖
	public Queue<Action> aq=new LinkedList<Action>();//動作佇列
	DoActionThread dat;					//執行動作執行緒參考
	
	//主資料
	GameData gdMain=new GameData();
	//繪制資料
	GameData gdDraw=new GameData();
	
	VertexTexture3DObjectForDraw room;		//房間
	VertexTexture3DObjectForDraw block;		//棋碟方塊
	VertexTextureNormal3DObjectForDraw [] qizi=new VertexTextureNormal3DObjectForDraw [6];	//棋子
	VertexTexture3DObjectForDraw smallBoard;//小棋碟
	VertexTexture3DObjectForDraw smallBoardBlock;//小棋碟群組成方塊
	VertexTexture3DObjectForDraw smallChess;//小棋子
	VertexTexture3DObjectForDraw smallJianTou;//小箭頭
	
	public GameSurfaceView(GJXQActivity activity) 
	{		
		super(activity);
		this.activity=activity;
		this.gdMain.humanColor=Constant.SELECTED_COLOR;
		this.gdMain.currColor=this.gdMain.humanColor;//目前彩色為人的彩色
		mRenderer = new SceneRenderer();
		setRenderer(mRenderer);		//設定著色器
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);	//設定著色模式為主動著色
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		//若目前不是人走棋，觸控無效
		synchronized(gdMain.dataLock)
		{
			if(gdMain.currColor!=gdMain.humanColor)
			{
				return false;
			}
		}
		
		x=event.getX();
		y=event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
               startX=x;
               startY=y;
               isMove=false;
               if(x>=(800+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   x<=(900+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
            		   &&y>=(20+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio
            		   &&y<=(60+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//若按下小箭頭
               {
            	   isDrawSmallQP=!isDrawSmallQP;//將是否繪制小棋碟標志位置反
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
				if(isMove)//搬移標志位為true
				{
					float dx=x-mPreviousX;//X方向搬移的長度
					float dy=y-mPreviousY;//Y方向搬移的長度
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
			break;
			case MotionEvent.ACTION_UP:
				if(!isMove)//若果不是搬移
				{
					Action acTemp=new Action//拾取動作
					(
						ActionType.SELECT_3D,//動作型態
						new float[]{x,y}//動作資料
					);
					synchronized(aqLock)//鎖上動作佇列
					{
						aq.offer(acTemp);//向動作佇列的佇列尾加入動作
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
		int roomId;	//房間紋理id
		
		//格子紋理圖四幅
		//第一維索引        0---白    1----黑
		//第二維    0---未勾選     1----勾選
		int[][] blockTexId=new int[2][2];
		
		//棋子紋理圖兩幅，白色棋子紋理與黑絲棋子的紋理
		int[] qzTexId=new int[2];
		
		//小棋碟紋理圖id
		int samllBoardId;
		
		//小棋碟群組成方塊紋理圖兩幅
		int[] smallBoardBlockId=new int[2];
		int[] smallChessID=new int[12];
		
		//小箭頭紋理
		int smallJianTouId;
		
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			//將繪制資料覆制進臨時變數
			int[][] qiwzTemp=new int[8][8];	//棋子位置
			int[][] gzxzTemp=new int[8][8]; //格子勾選 
			int[][] qzxzTemp=new int[8][8]; //棋子勾選
			float[] camera=new float[9];	//相機
			synchronized(gdDraw.dataLock)	//鎖上繪制資料
			{
				for(int i=0;i<8;i++)
				{
					for(int j=0;j<8;j++)//將繪制資料棋子位置、格子勾選、棋子勾選copy進臨時變數
					{
						qiwzTemp[i][j]=gdDraw.qzwz[i][j];
						gzxzTemp[i][j]=gdDraw.gzxz[i][j];
						qzxzTemp[i][j]=gdDraw.qzxz[i][j];
					}
				}
				//將攝影機9參數copy進臨時變數
				camera[0]=gdDraw.cx;//攝影機位置
				camera[1]=gdDraw.cy;
				camera[2]=gdDraw.cz;
				camera[3]=gdDraw.tx;//觀察點位置
				camera[4]=gdDraw.ty;
				camera[5]=gdDraw.tz;
				camera[6]=gdDraw.ux;//up向量
				camera[7]=gdDraw.uy;
				camera[8]=gdDraw.uz;
			}
			
			//清楚蛇毒快取與彩色快取
			gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_PROJECTION);//設定目前矩陣為投影矩陣
			gl.glLoadIdentity();//設定目前矩陣為單位矩陣
			gl.glFrustumf(-ratio, ratio, bottom, top, near, far);//呼叫此方法計算產生透視投影矩陣
			gl.glMatrixMode(GL10.GL_MODELVIEW);//設定目前矩陣為模式矩陣
			gl.glLoadIdentity();//設定目前矩陣為單位矩陣			

			GLU.gluLookAt//設定攝影機
			(
				gl, 
				camera[0],camera[1],camera[2],//攝影機位置
				camera[3],camera[4],camera[5],//觀察點位置
				camera[6],camera[7],camera[8]//UP向量
			);

			
			//繪制房間
			room.drawSelf(gl, roomId);   
			
			//繪制棋碟方塊
			for(int i=0;i<8;i++)
			{
				for(int j=0;j<8;j++)
				{
					int status=gzxzTemp[i][j];			//勾選狀態
					int hb=GameStaticData.gzhb[i][j];	//格子編號
					float xOffset=GameStaticData.XOffset[i][j];//格子在X軸方向的偏移量
					float zOffset=GameStaticData.ZOffset[i][j];//格子在Z軸方向的偏移量
					int texIdTemp=blockTexId[hb][status];	   //棋碟方塊的紋理id
					gl.glPushMatrix();
					gl.glTranslatef(xOffset, 0, zOffset);
					block.drawSelf(gl, texIdTemp);
					gl.glPopMatrix();
				}
			}
			
			//繪制棋子
			//容許光源       
	        gl.glEnable(GL10.GL_LIGHTING);	        
			for(int i=0;i<8;i++)
			{
				for(int j=0;j<8;j++)
				{
					int qzbh=qiwzTemp[i][j];	//棋子編號
					int qzxzzt=qzxzTemp[i][j];  //棋子勾選狀態
					if(qzbh==-1)continue;		//若果棋子編號為-1即沒有棋子
					float xOffset=GameStaticData.XOffsetQZ[i][j];//棋子在X軸方向的偏移量
					float zOffset=GameStaticData.ZOffsetQZ[i][j];//棋子在Y軸方向的偏移量
					int hbqkTemp=(qzbh>5)?1:0;					 //標誌棋子是黑子還是白子
					int texIdTemp=qzTexId[hbqkTemp];//棋子紋理id
					gl.glPushMatrix();
					gl.glTranslatef(xOffset, 0.6f+((qzxzzt==1)?0.6f:0.0f), zOffset);
					gl.glRotatef(GameStaticData.ANGLE[hbqkTemp], 0, 1, 0);
					qizi[qzbh%6].drawSelf(gl, texIdTemp);//繪制棋子
					gl.glPopMatrix();
				}
			}
			//禁止光源       
	        gl.glDisable(GL10.GL_LIGHTING);

			 //開啟混合
			 gl.glEnable(GL10.GL_BLEND);
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
	         
	         //繪制小箭頭
             gl.glPushMatrix();     			 
 			 gl.glTranslatef(1.2f, 0.91f, -2);
 			 smallJianTou.drawSelf(gl, smallJianTouId);
 			 gl.glPopMatrix();
 			 
 			if(isDrawSmallQP)//若繪制小棋碟
			{
	 			 //繪制小棋碟
			     gl.glPushMatrix();				 
				 gl.glTranslatef(1.2f, 0.3f, -1);
				 smallBoard.drawSelf(gl, samllBoardId);
				 gl.glPopMatrix();
				 
				 //繪制小棋碟方塊
				 for(int i=0;i<8;i++)
				 {
					for(int j=0;j<8;j++)
					{
						int hb=GameStaticData.gzhb[i][j];	//格子編號
						int tempId=smallBoardBlockId[hb];
						gl.glPushMatrix();
						gl.glTranslatef(0.8f+j*0.115f, 0.695f-i*0.115f, -0.5f);
						smallBoardBlock.drawSelf(gl, tempId);
						gl.glPopMatrix();
					}
				 }
				 
				 for(int i=0;i<8;i++)
				 {
					for(int j=0;j<8;j++)
					{
						int qzbh=qiwzTemp[i][j];	//棋子編號，是在哪個格子繪制那個棋子
						int hb=GameStaticData.gzhb[i][j];	//格子編號，用來看繪制什麼彩色的格子
						int tempId=smallBoardBlockId[hb];
						if(qzbh==0)
						{
							tempId=smallChessID[10];
						}
						if(qzbh==1)
						{
							tempId=smallChessID[11];
						}
						if(qzbh==2)
						{
							tempId=smallChessID[9];
						}
						if(qzbh==3)
						{
							tempId=smallChessID[7];
						}
						if(qzbh==4)
						{
							tempId=smallChessID[8];
						}
						if(qzbh==5)
						{
							tempId=smallChessID[6];
						}
						if(qzbh==6)
						{
							tempId=smallChessID[4];
						}
						if(qzbh==7)
						{
							tempId=smallChessID[5];
						}
						if(qzbh==8)
						{
							tempId=smallChessID[3];
						}
						if(qzbh==9)
						{
							tempId=smallChessID[1];
						}
						if(qzbh==10)
						{
							tempId=smallChessID[3];
						}
						if(qzbh==11)
						{
							tempId=smallChessID[0];
						}
						if(qzbh==-1)
						{
							tempId=smallBoardBlockId[hb];
						}
						gl.glPushMatrix();
						gl.glTranslatef(0.8f+j*0.115f, 0.7f-i*0.115f, 0f);
						smallChess.drawSelf(gl, tempId);//繪制小棋子
						gl.glPopMatrix();
						
					}
				 }
			 }		     
	         gl.glDisable(GL10.GL_BLEND);//關閉混合
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) 
		{
			gl.glViewport(Constant.screenScaleResult.lucX, Constant.screenScaleResult.lucY, 
					(int)(GJXQActivity.screenWidthStandard*Constant.screenScaleResult.ratio), 
					(int)(GJXQActivity.screenHeightStandard*Constant.screenScaleResult.ratio));
			
			ratio=GJXQActivity.screenWidthStandard/GJXQActivity.screenHeightStandard;  
			//設定為開啟背面剪裁
    		gl.glEnable(GL10.GL_CULL_FACE);
    		
    		dat=new DoActionThread(GameSurfaceView.this);//建立執行動作執行緒
    		dat.start();//啟動執行緒
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);  		//關閉抗抖動 
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);//設定特定Hint專案的模式，這裡為設定為使用快速模式
			gl.glClearColor(0, 0, 0, 0);		//設定螢幕背景色黑色RGBA
			gl.glEnable(GL10.GL_DEPTH_TEST);	//開啟深度檢驗
			gl.glDisable(GL10.GL_CULL_FACE);	//設定為開啟背面剪裁
			gl.glShadeModel(GL10.GL_SMOOTH);	//設定著色模型為平順著色   
			
			initTexId(gl);//起始化紋理
			
			room=new VertexTexture3DObjectForDraw
			(
				VertexDataManager.vertexPositionArray[0],//房子的頂點座標資料
				VertexDataManager.vertexTextrueArray[0], //房間紋理座標
				VertexDataManager.vCount[0], //頂點數
				VertexDataManager.AABBData[0]//包圍盒
			);
			block=new VertexTexture3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[1],//棋子方塊的頂點座標資料
					VertexDataManager.vertexTextrueArray[1], //棋子方塊的紋理座標資料
					VertexDataManager.vCount[1], //頂點數
					VertexDataManager.AABBData[1]//包圍盒
			);
			qizi[0]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[2],
					VertexDataManager.vertexTextrueArray[2],
					VertexDataManager.vertexNormalArray[2],
					VertexDataManager.vCount[2],
					VertexDataManager.AABBData[2]
			);
			qizi[1]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[3],
					VertexDataManager.vertexTextrueArray[3],
					VertexDataManager.vertexNormalArray[3],
					VertexDataManager.vCount[3],
					VertexDataManager.AABBData[3]
			);
			qizi[2]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[4],
					VertexDataManager.vertexTextrueArray[4],
					VertexDataManager.vertexNormalArray[4],
					VertexDataManager.vCount[4],
					VertexDataManager.AABBData[4]
			);
			qizi[3]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[5],
					VertexDataManager.vertexTextrueArray[5],
					VertexDataManager.vertexNormalArray[5],
					VertexDataManager.vCount[5],
					VertexDataManager.AABBData[5]
			);
			qizi[4]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[6],
					VertexDataManager.vertexTextrueArray[6],
					VertexDataManager.vertexNormalArray[6],
					VertexDataManager.vCount[6],
					VertexDataManager.AABBData[6]
			);
			qizi[5]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[7],
					VertexDataManager.vertexTextrueArray[7],
					VertexDataManager.vertexNormalArray[7],
					VertexDataManager.vCount[7],
					VertexDataManager.AABBData[7]
			);
			
			//小棋碟
			smallBoard=new VertexTexture3DObjectForDraw  
			(
					VertexDataManager.vertexPositionArray[8],
					VertexDataManager.vertexTextrueArray[8],
					VertexDataManager.vCount[8],
					VertexDataManager.AABBData[8]
			);
			//小棋碟群組成方塊
			smallBoardBlock=new VertexTexture3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[9],
					VertexDataManager.vertexTextrueArray[9],
					VertexDataManager.vCount[9],
					VertexDataManager.AABBData[9]
			);
			//小棋子
			smallChess=new VertexTexture3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[10],
					VertexDataManager.vertexTextrueArray[10],
					VertexDataManager.vCount[10],
					VertexDataManager.AABBData[10]
			);
			//小箭頭
			smallJianTou=new VertexTexture3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[11],
					VertexDataManager.vertexTextrueArray[11],
					VertexDataManager.vCount[11],
					VertexDataManager.AABBData[11]
			);
			
			initLight(gl);
			initMaterial(gl);
		}
		
		public void initTexId(GL10 gl) {//起始化紋理id
			roomId = initTexture(gl, PicDataManager.picDataArray[3]);		//房間紋理id
			blockTexId[0][0]=initTexture(gl,PicDataManager.picDataArray[4]);//棋碟方塊紋理id
			blockTexId[0][1]=initTexture(gl,PicDataManager.picDataArray[6]);
			blockTexId[1][0]=initTexture(gl,PicDataManager.picDataArray[0]);
			blockTexId[1][1]=initTexture(gl,PicDataManager.picDataArray[2]);
			qzTexId[0]=initTexture(gl,PicDataManager.picDataArray[5]);		//棋子紋理id
			qzTexId[1]=initTexture(gl,PicDataManager.picDataArray[1]);
			samllBoardId=initTexture(gl,PicDataManager.picDataArray[7]);//小棋碟紋理id
			smallBoardBlockId[0]=initTexture(gl,PicDataManager.picDataArray[8]);//小棋碟群組成方塊紋理id
			smallBoardBlockId[1]=initTexture(gl,PicDataManager.picDataArray[9]);//小棋碟群組成方塊紋理id
			//小棋子的紋理id
			smallChessID[0]=initTexture(gl,PicDataManager.picDataArray[10]);//黑兵
			smallChessID[1]=initTexture(gl,PicDataManager.picDataArray[11]);//黑後
			smallChessID[2]=initTexture(gl,PicDataManager.picDataArray[12]);//黑王
			smallChessID[3]=initTexture(gl,PicDataManager.picDataArray[13]);//黑象
			smallChessID[4]=initTexture(gl,PicDataManager.picDataArray[14]);//黑車
			smallChessID[5]=initTexture(gl,PicDataManager.picDataArray[15]);//黑馬
			smallChessID[6]=initTexture(gl,PicDataManager.picDataArray[16]);//白兵	
			smallChessID[7]=initTexture(gl,PicDataManager.picDataArray[17]);//白後	
			smallChessID[8]=initTexture(gl,PicDataManager.picDataArray[18]);//白王	
			smallChessID[9]=initTexture(gl,PicDataManager.picDataArray[19]);//白象	
			smallChessID[10]=initTexture(gl,PicDataManager.picDataArray[20]);//白車	
			smallChessID[11]=initTexture(gl,PicDataManager.picDataArray[21]);//白馬	
			
			smallJianTouId=initTexture(gl,PicDataManager.picDataArray[22]);//小箭頭紋理id
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
