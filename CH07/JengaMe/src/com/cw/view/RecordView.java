package com.cw.view;


import java.util.Vector;

import com.cw.game.Constant;
import com.cw.game.JengaMeActivity;
import com.cw.game.R;
import com.cw.game.R.drawable;
import com.cw.util.SQLiteUtil;
import com.cw.util.SYSUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RecordView extends SurfaceView implements SurfaceHolder.Callback
{
JengaMeActivity activity;
	Bitmap beijingtupian;
	Bitmap okbutton;
	Bitmap okbuttondown;
	Bitmap backbutton;
	Bitmap backbuttondown;
	Bitmap	backoff;
	Bitmap	backon;
	Bitmap sceneoff;
	Bitmap sceneon;
	Bitmap recordback;
	Bitmap maohao;
	Bitmap line;
	Bitmap bumberpic[]=new Bitmap[10] ;
	static boolean  backup=true;;
	static boolean okup=true;

    Vector<Vector<String>> vector;//存放結果集的向量
	static boolean yesflaga=false;
	static boolean yesflagb=false;
	 SurfaceHolder holder;
	 static  float scalX;
	 static  float scalY;
	 
	 public static boolean candraw=true;;
     public RecordView(JengaMeActivity activity)
     {
         super(activity);
         holder = this.getHolder();//取得holder  
         holder.addCallback(this);
		this.activity=activity;
		this.getHolder().addCallback(this);//設定生命周期回調接口的實現者	
		scalX=JengaMeActivity.screenWidth;
		scalY=JengaMeActivity.screenHeight;
		initBitmap();//起始化點陣圖
		
		candraw=true;
		String sql_select="select grade,time from paihangbang  order by grade desc limit 0,6;";
    	vector=SQLiteUtil.query(sql_select);//從資料庫中取出對應的資料
     }

     public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)
     {
     }
     public void surfaceCreated(SurfaceHolder holder)
     {
    	 yesflagb=activity.sp.getBoolean("playeffect", false);
    	 yesflaga=activity.sp.getBoolean("playback", false);
         new Thread(new MyThread()).start();
    
     }
     public void surfaceDestroyed(SurfaceHolder holder)
     {
     }
 	public boolean onTouchEvent(MotionEvent e)
 	{
 		int currentNUm=e.getAction();
 		float x=e.getX();
 		float y=e.getY();
 		switch(currentNUm)
 		{
 		case MotionEvent.ACTION_DOWN:
				//開始游戲
				if(x>30*JengaMeActivity.scaleratiox&&x<30*JengaMeActivity.scaleratiox+okbutton.getWidth()&&y>720*JengaMeActivity.scaleratioY&&y<720*JengaMeActivity.scaleratioY+okbutton.getHeight())
				{
					okup=false;
				}
				if(x>(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth()&&x<540*JengaMeActivity.scaleratiox&&y>720*JengaMeActivity.scaleratioY&&y<720*JengaMeActivity.scaleratioY+okbutton.getHeight())
				{
					backup=false;
				}
				break;
			case MotionEvent.ACTION_UP:	
				okup=true;
	    	    backup=true;
				if(x>30*JengaMeActivity.scaleratiox&&x<30*JengaMeActivity.scaleratiox+okbutton.getWidth()&&y>720*JengaMeActivity.scaleratioY&&y<720*JengaMeActivity.scaleratioY+okbutton.getHeight())
				{
					candraw=false;
					MenuView.cdraw=true;
					activity.hd.sendEmptyMessage(1);
					activity.editor.putBoolean("playback",yesflaga);
					activity.editor.putBoolean("playeffect",yesflagb);
					activity.editor.commit();
					activity.playBackMusic=activity.sp.getBoolean("playback", false);
					if(activity.playBackMusic)
			        {
						activity.soundutil.play_bg_sound();  
			        }else
			        {
			        	try
			        	{
			        		activity.soundutil.stop_bg_sound();
			        	}
			        	catch(Exception exception)
			        	{
			        		System.out.println(""+exception);
			        	}
			        }
				}
			if(x>(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth()&&x<(540-30)*JengaMeActivity.scaleratiox&&y>720*JengaMeActivity.scaleratioY&&y<720*JengaMeActivity.scaleratioY+backbutton.getHeight())
				{
					candraw=false;
					MenuView.cdraw=true;
					activity.hd.sendEmptyMessage(1);
				}
			break;
 		}
 		return true;
 	}
 	 Bitmap dstbmp;
 	Bitmap recobmp;
 	public void initBitmap()
 	{
 		beijingtupian=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.background2,0, Constant.background2.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);
 		recordback=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.recordback,0, Constant.recordback.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);
 		
 		
 		okbutton=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.ok), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		backbutton=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.back), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		okbuttondown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.okdown), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		backbuttondown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.backdown), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		backoff=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.backmusicoff), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		backon=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.backmusicon), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		sceneoff=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.scenemusicoff), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		sceneon=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.scenemusicon), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	 
 		bumberpic[0]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d0), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		bumberpic[1]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d1), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		bumberpic[2]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d2), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		bumberpic[3]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d3), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		bumberpic[4]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d4), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		bumberpic[5]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d5), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		bumberpic[6]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d6), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		bumberpic[7]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d7), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		bumberpic[8]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d8), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		bumberpic[9]=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.d9), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		maohao=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.maohao0), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		line=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.line), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 	}
     class MyThread implements Runnable
     {
         public void run()
         {
        	  Matrix matrix=new Matrix();
              int width = beijingtupian.getWidth();//取得資源點陣圖的寬
              int height = beijingtupian.getHeight();//取得資源點陣圖的高
                 float w = scalX/beijingtupian.getWidth();
                 float h = scalY/beijingtupian.getHeight();
              matrix.postScale(w, h);//取得縮放比例
              Rect rect = new Rect (0,0,width,height); 
              RectF rectf = new RectF(0,0,width*w,height*h); 
         while(candraw)
           {
        		 Canvas canvas = holder.lockCanvas(null);//取得畫布  
                 Paint mPaint = new Paint();
                 canvas.save();
                 canvas.drawBitmap(beijingtupian,rect,rectf,null); 
                 canvas.translate(0, 140);
                 canvas.drawBitmap(recordback,rect,rectf,null); 
               for(int i=0;i<vector.size();i++)
               {
            	   int score =Integer.parseInt(vector.get(i).get(0));
            	   String time=vector.get(i).get(1);
            	   String[] tt=time.split("-");
            	   int year=Integer.parseInt(tt[0]);
            	   int month=Integer.parseInt(tt[1]);
            	   int day=Integer.parseInt(tt[2]);
            	   int hour=Integer.parseInt(tt[3]);
            	   int minute=Integer.parseInt(tt[4]);
            	  //年 
            	   canvas.drawBitmap(bumberpic[year/10%10],40*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   canvas.drawBitmap(bumberpic[year%10],60*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	 
            	   canvas.drawBitmap(line,80*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   
            	   //月
            	   canvas.drawBitmap(bumberpic[month/10],100*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   canvas.drawBitmap(bumberpic[month%10],120*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   
            	   canvas.drawBitmap(line,140*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   
            	   //日
            	   canvas.drawBitmap(bumberpic[day/10],160*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   canvas.drawBitmap(bumberpic[day%10],180*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	  
            	   // 時
            	   canvas.drawBitmap(bumberpic[hour/10],220*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   canvas.drawBitmap(bumberpic[hour%10],240*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
              
            	   canvas.drawBitmap(maohao,260*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   
            	   //分
            	   canvas.drawBitmap(bumberpic[minute/10],280*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   canvas.drawBitmap(bumberpic[minute%10],300*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
              
            	   
            	   //得分 
            	   canvas.drawBitmap(bumberpic[score/10],380*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
            	   canvas.drawBitmap(bumberpic[score%10],400*JengaMeActivity.scaleratiox,(120+i*60)*JengaMeActivity.scaleratioY,mPaint); 
               }
                 canvas.restore();
                 if(okup)
                 {
                	 canvas.drawBitmap(okbutton,30*JengaMeActivity.scaleratiox,720*JengaMeActivity.scaleratioY, mPaint); 
                 }else
                 {
                	 canvas.drawBitmap(okbuttondown,30*JengaMeActivity.scaleratiox,720*JengaMeActivity.scaleratioY, mPaint); 
                 }
                 if(backup)
                 {
                	 canvas.drawBitmap(backbutton,(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth(),720*JengaMeActivity.scaleratioY, mPaint); 
                 }else
                 {
                	 canvas.drawBitmap(backbuttondown,(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth(),720*JengaMeActivity.scaleratioY, mPaint); 
                 }
                 try
                 {
                	holder.unlockCanvasAndPost(canvas);//解鎖畫布，傳送畫好的圖形  
                 }catch(Exception e)
                 {                	 
                 }
          }
        beijingtupian.recycle();
  		okbutton.recycle();
  		backbutton.recycle();
  		okbuttondown.recycle();
  		backbuttondown.recycle();
  		backoff.recycle();
  		backon.recycle();
  		sceneoff.recycle();
  		sceneon.recycle();
  		recordback.recycle();
  		bumberpic[0].recycle();
  		bumberpic[1].recycle();
  		bumberpic[2].recycle();
  		bumberpic[3].recycle();
  		bumberpic[4].recycle();
  		bumberpic[5].recycle();
  		bumberpic[6].recycle();
  		bumberpic[7].recycle();
  		bumberpic[8].recycle();
  		bumberpic[9].recycle();
  		maohao.recycle();
  		line.recycle();
       }
     }
}
