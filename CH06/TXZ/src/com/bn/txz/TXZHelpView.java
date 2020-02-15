package com.bn.txz;

import static com.bn.txz.Constant.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.txz.manager.PicDataManager;
import com.bn.txz.manager.VertexDataManager;
import com.bn.txz.manager.VertexTexture3DObjectForDraw;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class TXZHelpView extends GLSurfaceView
{
	TXZActivity activity;		//Activity參考
	private SceneRenderer mRenderer;//場景著色器   
//	static int idKey=0;//背景id的key
	boolean isLoadedOk=false;//是否載入完成標志位
	boolean inLoadView=true;//是否在載入界面標志位
	private int load_step=0;//進度指示器步數
	VertexTexture3DObjectForDraw laodBack;//載入界面背景圖
	VertexTexture3DObjectForDraw processBar;//載入 界面中的進度指示器矩形
	VertexTexture3DObjectForDraw loading;//載入界面中文字矩形
	public TXZHelpView(TXZActivity activity) 
	{
		super(activity);
		this.activity=activity;
		mRenderer = new SceneRenderer();	//建立場景著色器
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設定著色模式為主動著色   
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(x<(Constant.Help_pre_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
					&&x>(Constant.Help_pre_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
					&&y<(Constant.Help_pre_b+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio
					&&y>(Constant.Help_pre_t+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{//按下左邊按鈕
				if(idKey==0)
				{
					activity.gotoMenuView();
					idKey=0;
				}
				else{
					idKey=idKey-1;
				}
			}
			if(x<(Constant.Help_next_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
					&&x>(Constant.Help_next_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
					&&y<(Constant.Help_next_b+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio
					&&y>(Constant.Help_next_t+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{//按下右邊按鈕
				if(idKey==9)
				{
					activity.gotoMenuView();
					idKey=0;
				}
				else{
					idKey=idKey+1;
				}
			}
			break;
		}
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer 
	{
		VertexTexture3DObjectForDraw helpback;
		VertexTexture3DObjectForDraw button;
		
		int helpId[]=new int[10];
		int prepageId;
		int nextpageId;
		
		int loadBackId;//載入界面背景矩形紋理
		int processBeijing;//載入界面進度指示器矩形背景
		int tex_processId;//進度指示器
		int loadId;
		
		private boolean isFirstFrame=true;
		@Override
		public void onDrawFrame(GL10 gl) {
			//清楚深度快取與彩色快取
			gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
			if(!isLoadedOk)
			{
				inLoadView=true;
				drawLoadingView(gl);
			}
			else
			{
				inLoadView=false;
				drawHelpView(gl);
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
			
			loadBackId=initTexture(gl, PicDataManager.picDataArray[12]);//載入界面背景紋理id
			processBeijing=initTexture(gl, PicDataManager.picDataArray[13]);//載入界面進度指示器背景紋理id
			tex_processId=initTexture(gl, PicDataManager.picDataArray[14]);//載入界面進度指示器紋理id
			loadId=initTexture(gl, PicDataManager.picDataArray[15]);//載入界面背景紋理id
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

		public void drawLoadingView(GL10 gl)
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
		//建立幫助界面需要的紋理和矩形
		public void loadResource(GL10 gl)
		{
			switch(load_step)
			{
			case 0:
				load_step++;
				break;
			case 1:
				//紋理id
				helpId[0]=initTexture(gl,PicDataManager.picDataArray[5]);
				helpId[1]=initTexture(gl,PicDataManager.picDataArray[6]);
				load_step++;
				break;
			case 2:
				helpId[2]=initTexture(gl,PicDataManager.picDataArray[7]);
				helpId[3]=initTexture(gl,PicDataManager.picDataArray[8]);
				load_step++;
				break;
			case 3:
				helpId[4]=initTexture(gl,PicDataManager.picDataArray[41]);
				helpId[5]=initTexture(gl,PicDataManager.picDataArray[42]);
				load_step++;
				break;
			case 4:
				helpId[6]=initTexture(gl,PicDataManager.picDataArray[59]);
				helpId[7]=initTexture(gl,PicDataManager.picDataArray[60]);
				load_step++;
				break;
			case 5:
				helpId[8]=initTexture(gl,PicDataManager.picDataArray[61]);
				helpId[9]=initTexture(gl,PicDataManager.picDataArray[62]);
				load_step++;
				break;
			case 6:
				prepageId=initTexture(gl,PicDataManager.picDataArray[63]);
				nextpageId=initTexture(gl,PicDataManager.picDataArray[64]);
				load_step++;
				break;
			case 7:
				helpback=new VertexTexture3DObjectForDraw//輸贏界面按鈕
				(
						VertexDataManager.vertexPositionArray[22],//房子的頂點座標資料
						VertexDataManager.vertexTextrueArray[22], //房間紋理座標
						VertexDataManager.vCount[22] //頂點數
				);
				load_step++;
				break;
			case 8:
				button=new VertexTexture3DObjectForDraw//輸贏界面按鈕
				(
						VertexDataManager.vertexPositionArray[31],//房子的頂點座標資料
						VertexDataManager.vertexTextrueArray[31], //房間紋理座標
						VertexDataManager.vCount[31] //頂點數
				);
				isLoadedOk=true;//切換到一級選單
				laodBack=null;//銷毀
				processBar=null;//銷毀
				break;
			}
		}
		public void drawHelpView(GL10 gl)
		{
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
	        
	        helpback.drawSelf(gl, helpId[idKey]);
			gl.glEnable(GL10.GL_BLEND);//開啟混合
			 //設定源混合因子與目的混合因子
		    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		    
			gl.glPushMatrix();
			gl.glTranslatef(-1.3f, -0.7f, 0.1f);
			button.drawSelf(gl, prepageId);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(1.3f, -0.7f, 0.1f);
			button.drawSelf(gl, nextpageId);
			gl.glPopMatrix();
			gl.glDisable(GL10.GL_BLEND);//關閉混合
		}
	}
}
