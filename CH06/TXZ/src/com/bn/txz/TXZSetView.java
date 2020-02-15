package com.bn.txz;

import static com.bn.txz.Constant.bottom;
import static com.bn.txz.Constant.far;
import static com.bn.txz.Constant.near;
import static com.bn.txz.Constant.ratio;
import static com.bn.txz.Constant.top;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.bn.txz.game.GameData;
import com.bn.txz.game.Robot;
import com.bn.txz.manager.PicDataManager;
import com.bn.txz.manager.VertexDataManager;
import com.bn.txz.manager.VertexTexture3DObjectForDraw;
import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class TXZSetView extends GLSurfaceView{

	TXZActivity activity;		//Activity參考
	private SceneRenderer mRenderer;//場景著色器      
	
	public GameData gdMain=new GameData();
    GameData gdDraw=new GameData();
    GameData gdTemp=new GameData();  
    
    boolean flagn=true;
    float anglet=0;
	float anglex=25;
	boolean flagx=false;
	boolean color=false;
    
	public TXZSetView(Context context) {
		super(context);
		this.activity=(TXZActivity)context;
		mRenderer = new SceneRenderer();	//建立場景著色器
	    setRenderer(mRenderer);				//設定著色器		        
	    setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設定著色模式為主動著色   
	}
	
	public boolean onTouchEvent(MotionEvent e)
	{
		int x=(int)e.getX();
		int y=(int)e.getY();
		switch(e.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(x>=(Constant.Set_kai_1_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Set_kai_1_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Set_kai_1_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Set_kai_1_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{
				Constant.IS_BEIJINGYINYUE=!Constant.IS_BEIJINGYINYUE;
				if(Constant.IS_BEIJINGYINYUE)
				{
					activity.playBeiJingYinYue();
				}
				else
				{
					activity.stopBeiJingYinYue();
				}
			}
			else if(x>=(Constant.Set_kai_2_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Set_kai_2_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Set_kai_2_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Set_kai_2_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{
				Constant.IS_YINXIAO=!Constant.IS_YINXIAO;
			}
			else if(x>=(Constant.Set_back_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Set_back_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Set_back_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Set_back_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{
				activity.gotoMenuView();
				Constant.SET_IS_WHILE=false;
				Constant.set_flag=false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		
		return true;
	}
	
private class SceneRenderer implements GLSurfaceView.Renderer {
	VertexTexture3DObjectForDraw button;//按鈕
	int headTexId;//頁首紋理ID
	int armTexId;//其他部位紋理ID
	int bgmusic;
	int bgyouxi;
	int on;
	int off;
	int back;
	
	int bgmusicl;
	int bgyouxil;
	int onl;
	int offl;
	int backl;
	
	float yAngle=0;
	
	//從obj檔案中載入的3D物體的參考
	VertexTextureNormal3DObjectForDraw[] lovntArray=new VertexTextureNormal3DObjectForDraw[6];	
	VertexTexture3DObjectForDraw bigCelestial;//星空
	VertexTexture3DObjectForDraw smallCelestial;//星空
	Robot robot;
	SetDoActionThread dat;	
	
	public void onDrawFrame(GL10 gl) {
		//清楚深度快取與彩色快取
		gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_PROJECTION);//設定目前矩陣為投影矩陣
		gl.glLoadIdentity();//設定目前矩陣為單位矩陣
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);  //呼叫此方法計算產生透視投影矩陣
		gl.glMatrixMode(GL10.GL_MODELVIEW);//設定目前矩陣為模式矩陣
		gl.glLoadIdentity();//設定目前矩陣為單位矩陣	
		
		GLU.gluLookAt//可能變形的角度——大角度
        (
        		gl, 
        		0f,   //人眼位置的X
        		5f, 	//人眼位置的Y
        		5f,   //人眼位置的Z
        		0, 	//人眼光看的點X
        		5f,   //人眼光看的點Y
        		0,   //人眼光看的點Z
        		0, 
        		1, 
        		0
        );   
		
        //將繪制資料覆制進臨時資料
		synchronized(gdDraw.dataLock)
		{
			gdDraw.copyTo(gdTemp);
		} 
		
		gl.glPushMatrix();
		gl.glRotatef(yAngle, 0, 1, 0);
		bigCelestial.drawSelf(gl);//繪制星空
		smallCelestial.drawSelf(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(-5f, 0, -2.5f);
        //繪制物體
        robot.drawSelfSet(gl);
        gl.glPopMatrix();
        
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
	        
         if(color)
         {
        	 gl.glPushMatrix();
    		 gl.glTranslatef(0.6f, 0.6f, 0.1f);	
    		 gl.glRotatef(-anglet, 0, 1, 0);
     		 gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, bgmusic);
    		 gl.glPopMatrix();
    		
    		 if(Constant.IS_BEIJINGYINYUE)
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.6f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, off);
    			 gl.glPopMatrix();
    		 }
    		 else
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.6f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, on);
    			 gl.glPopMatrix();
    		 }
    		 
    		 gl.glPushMatrix();
    		 gl.glTranslatef(0.6f, 0.2f, 0.1f);	
    		 gl.glRotatef(-anglet, 0, 1, 0);
    	 		gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, bgyouxi);
    		 gl.glPopMatrix();
    		 
    		 if(Constant.IS_YINXIAO)
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.2f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, off);
    			 gl.glPopMatrix();
    		 }
    		 else
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.2f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, on);
    			 gl.glPopMatrix();
    		 }
    		 
    		
    		
    		 gl.glPushMatrix();
    		 gl.glTranslatef(1.2f, -0.5f, 0.1f);
    		 gl.glRotatef(-anglet, 0, 1, 0);
    	 		gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, back);
    		 gl.glPopMatrix();
         }
         else
         {
        	 gl.glPushMatrix();
    		 gl.glTranslatef(0.6f, 0.6f, 0.1f);	
    		 gl.glRotatef(-anglet, 0, 1, 0);
     		 gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, bgmusicl);
    		 gl.glPopMatrix();
    		
    		 if(Constant.IS_BEIJINGYINYUE)
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.6f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, offl);
    			 gl.glPopMatrix();
    		 }
    		 else
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.6f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, onl);
    			 gl.glPopMatrix();
    		 }
    		 
    		 gl.glPushMatrix();
    		 gl.glTranslatef(0.6f, 0.2f, 0.1f);	
    		 gl.glRotatef(-anglet, 0, 1, 0);
    	 		gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, bgyouxil);
    		 gl.glPopMatrix();
    		 
    		 if(Constant.IS_YINXIAO)
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.2f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, offl);
    			 gl.glPopMatrix();
    		 }
    		 else
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.2f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, onl);
    			 gl.glPopMatrix();
    		 }
    		 
    		
    		
    		 gl.glPushMatrix();
    		 gl.glTranslatef(1.2f, -0.5f, 0.1f);
    		 gl.glRotatef(-anglet, 0, 1, 0);
    	 		gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, backl);
    		 gl.glPopMatrix();
         }
	}
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
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
			
			Constant.set_flag=true;
			Constant.SET_IS_WHILE=true;
            
            //紋理的起始化
            armTexId=initTexture(gl, PicDataManager.picDataArray[4]);//機器人其他部分紋理id
			lovntArray[0]=new VertexTextureNormal3DObjectForDraw//身體
			(
					VertexDataManager.vertexPositionArray[17],
					VertexDataManager.vertexTextrueArray[17],
					VertexDataManager.vertexNormalArray[17],
					VertexDataManager.vCount[17],
					armTexId
			);
			headTexId=initTexture(gl, PicDataManager.picDataArray[3]);//機器人頭紋理id
			lovntArray[1]=new VertexTextureNormal3DObjectForDraw//頭
			(
					VertexDataManager.vertexPositionArray[16],
					VertexDataManager.vertexTextrueArray[16],
					VertexDataManager.vertexNormalArray[16],
					VertexDataManager.vCount[16],
					headTexId
			);
			lovntArray[2]=new VertexTextureNormal3DObjectForDraw//左臂上
			(
					VertexDataManager.vertexPositionArray[18],
					VertexDataManager.vertexTextrueArray[18],
					VertexDataManager.vertexNormalArray[18],
					VertexDataManager.vCount[18],
					armTexId
			);
			
			lovntArray[3]=new VertexTextureNormal3DObjectForDraw//左臂下
			(
					VertexDataManager.vertexPositionArray[19],
					VertexDataManager.vertexTextrueArray[19],
					VertexDataManager.vertexNormalArray[19],
					VertexDataManager.vCount[19],
					armTexId
			);
			
			lovntArray[4]=new VertexTextureNormal3DObjectForDraw//右臂上
			(
					VertexDataManager.vertexPositionArray[20],
					VertexDataManager.vertexTextrueArray[20],
					VertexDataManager.vertexNormalArray[20],
					VertexDataManager.vCount[20],
					armTexId
			);
			
			lovntArray[5]=new VertexTextureNormal3DObjectForDraw//右臂下
			(
					VertexDataManager.vertexPositionArray[21],
					VertexDataManager.vertexTextrueArray[21],
					VertexDataManager.vertexNormalArray[21],
					VertexDataManager.vCount[21],  
					armTexId
			);
			robot=new Robot(lovntArray,TXZSetView.this);  
            dat=new SetDoActionThread(robot,TXZSetView.this);       
            dat.start();
            
            back=initTexture(gl,PicDataManager.picDataArray[29]);
            bgyouxi=initTexture(gl,PicDataManager.picDataArray[38]);//開始游戲紋理id
            bgmusic=initTexture(gl,PicDataManager.picDataArray[37]);//開始游戲紋理id
            on=initTexture(gl,PicDataManager.picDataArray[39]);//設定紋理id
            off=initTexture(gl, PicDataManager.picDataArray[43]);//載入界面背景紋理id
            backl=initTexture(gl,PicDataManager.picDataArray[54]);
            bgyouxil=initTexture(gl,PicDataManager.picDataArray[51]);//開始游戲紋理id
            bgmusicl=initTexture(gl,PicDataManager.picDataArray[50]);//開始游戲紋理id
            onl=initTexture(gl,PicDataManager.picDataArray[52]);//設定紋理id
            offl=initTexture(gl, PicDataManager.picDataArray[53]);//載入界面背景紋理id
            button=new VertexTexture3DObjectForDraw//輸贏界面按鈕
			(
					VertexDataManager.vertexPositionArray[15],//房子的頂點座標資料
					VertexDataManager.vertexTextrueArray[15], //房間紋理座標
					VertexDataManager.vCount[15] //頂點數
			);
            
            new Thread() 
            {
            	public void run() 
            	{
            		while(Constant.set_flag)
            		{
            			if(flagn)
            			{            				
            				anglet+=1f;
            				if(anglet>=25)
            				{	
            					flagn=false; 
            				}
            			}else{
            				anglet-=1f;
            				if(anglet<=-25)
            				{
            					flagn=true;
            				}            				
            			}
            			if(flagx)
            			{
            				anglex+=0.5f;
            				if(anglex>=12.5f)
            				{
            					flagx=false;
            				}	
            			}else {
            				anglex-=0.5f;
            				if(anglex<-12.5f)
            				{
            					flagx=true;
            				}
            			}
            			
            			try {
							Thread.sleep(90);
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
            		while(Constant.set_flag)
            		{
            			color=!color;
            			try
            			{
            				Thread.sleep(500);
            			}
            			catch(Exception e)
            			{
            				e.printStackTrace();
            			}
            		}
            	}
            }.start();
            
            bigCelestial=new VertexTexture3DObjectForDraw
            (
            		0,0,3,
            		VertexDataManager.vertexPositionArray[24],
            		VertexDataManager.vertexColorArray[24],
            		VertexDataManager.vCount[24]
            );
            smallCelestial=new VertexTexture3DObjectForDraw
            (
            		0,0,1.5f,
            		VertexDataManager.vertexPositionArray[25],
            		VertexDataManager.vertexColorArray[25],
            		VertexDataManager.vCount[25]
            );
            new Thread()
            {
            	public void run()
            	{
            		while(Constant.set_flag)
            		{
            			yAngle+=0.5f;
            			if(yAngle>=360)
            			{
            				yAngle=0;
            			}
            			try
            			{
            				Thread.sleep(100);
            			}
            			catch(InterruptedException e)
            			{
            				e.printStackTrace();
            			}
            		}
            	}
            }.start();
		}
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
