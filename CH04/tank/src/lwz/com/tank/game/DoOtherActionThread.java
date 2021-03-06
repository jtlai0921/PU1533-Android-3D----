package lwz.com.tank.game;

import static lwz.com.tank.activity.Constant.*;
import java.util.Queue;

public class DoOtherActionThread extends Thread
{
	public boolean workFlag=true;		//執行緒是否循環工作標志位
	OtherSurfaceView osv;
	Queue<Action> aq;					//動作佇列
	
    //坦克的偏轉角度
    float vAngle=0;
    //炮管的偏移角度
    float pAngle=0;
    //坦克每框搬移的步徑
    float hd_x=0;
    float hd_y=0;
    //搖桿中心圓點的偏移量
    static float scale_x=0;
    static float scale_y=0;
    //坦克1搬移的步徑
  	static float bx=0;
  	static float by=0;
  	float tkwz_x=0;
  	float tkwz_y=0;
	//坦克2的起始位置
	public  float Tank2_x=0;
	public  float Tank2_y=100;
	//搖桿中心點像素座標及半徑
	public  float INIT_x=(115+ssr.lucX)*ssr.ratio;
	public  float INIT_y=(425+ssr.lucY)*ssr.ratio;
	public  float INIT_R=100*(SCREEN_WIDTH-ssr.lucX*ssr.ratio*2)/SCREEN_WIDTH_STANDARD;
	//坦克起始的像素座標
    float tkposition_x=480f;
    float tkposition_y=438.75f;
    
    public static float fireflag=0;
    
    public DoOtherActionThread(OtherSurfaceView osv)
    {
    	this.osv=osv;		//游戲View
    	this.aq=osv.aq;		//動作佇列
    }
    @Override
	public void run()
	{
		 while(workFlag)
		 {
			Action ac=null;					//動作參考
    		synchronized(osv.aqLock)		//動作佇列鎖
    		{
    			ac=aq.poll();				//從動作佇列中取出一個動作，若佇列中沒有操控動作則取出null
    		} 
    		if(ac!=null)					//若操控動作參考不是null，即有動作需要執行
    		{
    			switch(ac.at)
    			{
    				case MOVE_ACTION:
    					float x;
    					float y;
    					if(Math.sqrt((ac.data[0]-INIT_x)*(ac.data[0]-INIT_x)+(ac.data[1]-INIT_y)*(ac.data[1]-INIT_y))<INIT_R)
					    {
							x=ac.data[0];
							y=ac.data[1];
					    }
					    else
					    {
						   //目前點座標到搖桿中心點的距離
						   float tempR=(float) Math.sqrt((ac.data[0]-INIT_x)*(ac.data[0]-INIT_x)+(INIT_y-ac.data[1])*(INIT_y-ac.data[1]));
						   //將目前點座標強制轉為搖桿觸控範圍的邊緣座標
						   x=INIT_x+(ac.data[0]-INIT_x)*INIT_R/tempR;
						   y=INIT_y+(ac.data[1]-INIT_y)*INIT_R/tempR;
						   
					    }
    					//搖桿中心圓點的x軸偏移量
    					scale_x=(x-INIT_x)/INIT_R;
    					//搖桿中心圓點的y軸偏移量
    					scale_y=(INIT_y-y)/INIT_R;
    					if(y<INIT_y)
    				   	{
    				   		//計算坦克的偏轉角度
    					   	vAngle=-(float)Math.toDegrees( Math.asin((x-INIT_x)/Math.sqrt((x-INIT_x)*(x-INIT_x)+(y-INIT_y)*(y-INIT_y))));
    				   	}
    					if(y>INIT_y)
    				   	{
    				   		vAngle=-180+(float)Math.toDegrees( Math.asin((x-INIT_x)/Math.sqrt((x-INIT_x)*(x-INIT_x)+(y-INIT_y)*(y-INIT_y))));
    				   	}
    					hd_x=-(float) (MOVE_SPAN*Math.sin(Math.toRadians(vAngle)));
    					hd_y=(float) (MOVE_SPAN*Math.cos(Math.toRadians(vAngle)));
    					if(y==INIT_y)
    					{
    						hd_x=0;
    						hd_y=0;
    					}
    					
    					bx=bx+hd_x;//坦克1每框變換的x座標
    					by=by+hd_y;//坦克1每框變換的y座標
    					//取得矮牆陣列的長度
    					int rows=lowWallmapdata.length;
    					//對陣列進行循環做碰撞檢驗
    					for(int i=0;i<rows;i++)
    					{	
    						if(bx>lowWallmapdata[i][0]*Wall_UNIT_SIZE-20f-Tank2_x&&bx<(lowWallmapdata[i][0]+lowWallmapdata[i][3])*Wall_UNIT_SIZE+20f-Tank2_x&&by<lowWallmapdata[i][1]*Wall_UNIT_SIZE+20f-Tank2_y&&by>(lowWallmapdata[i][1]-lowWallmapdata[i][4])*Wall_UNIT_SIZE-20f-Tank2_y)
    						{
    							if(bx-hd_x>lowWallmapdata[i][0]*Wall_UNIT_SIZE-20f-Tank2_x&&bx-hd_x<(lowWallmapdata[i][0]+lowWallmapdata[i][3])*Wall_UNIT_SIZE+20f-Tank2_x)	
    							{
									by=by-hd_y;
    							}
    							else
    							{
									bx=bx-hd_x;
    							}
    						}
    					}
    					tkwz_x=bx+Tank2_x;
    					tkwz_y=by+Tank2_y;
    					synchronized(osv.gdMain.dataLock) 	//將主資料鎖上
    					{
    						osv.gdMain.followtkwz[0]=tkwz_x;
    						osv.gdMain.followtkwz[1]=tkwz_y;
    						osv.gdMain.followtkwz[2]=vAngle;
    						osv.gdMain.followtkwz[3]=scale_x;
    						osv.gdMain.followtkwz[4]=scale_y;
    						osv.activity.sendMessage(tkwz_x+"<#>"+tkwz_y+"<#>"+vAngle+"<#>");
    						synchronized(osv.gdMainDraw.dataLock) //將繪制資料鎖上
    						{
    							osv.gdMainDraw.followtkwz[0]=tkwz_x;
    							osv.gdMainDraw.followtkwz[1]=tkwz_y;
    							osv.gdMainDraw.followtkwz[2]=vAngle;
    							osv.gdMainDraw.followtkwz[3]=scale_x;
    							osv.gdMainDraw.followtkwz[4]=scale_y;
    						}
    					}
    					break;
    				case FIRE_ACTION:
    					fireflag=1;
    					float x2=ac.data[0];
    					float y2=ac.data[1];
    					//實時計算坦克投影到螢幕上的座標，此時是攝影機不隨著坦克搬移。
				  		 tkposition_x=tkwz_x*1.5f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD+SCREEN_WIDTH/2;//起始坦克位置x
				  		 tkposition_y=SCREEN_HEIGHT/2-(tkwz_y)*1f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD;//起始坦克位置y
    					//此時是攝影機隨著坦克搬移下，實時計算坦克投影到螢幕上的座標。
//				  		 tkposition_x=(Tank2_x)*1.5f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD+SCREEN_WIDTH/2;//起始坦克位置x
//				  		 tkposition_y=SCREEN_HEIGHT/2-(Tank2_y-12.5f)*1.5f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD;//起始坦克位置y
				  		System.out.println(tkposition_x+"====================="+tkposition_y);
				  		 
				  		 if(y2>tkposition_y)
    					 {
    						//計算炮管的偏轉角度
    						 pAngle=-180+(float)Math.toDegrees( Math.asin((x2-tkposition_x)/Math.sqrt((x2-tkposition_x)*(x2-tkposition_x)+(y2-tkposition_y)*(y2-tkposition_y))));
    					 }
    					 else
    					 {
    					   //計算炮管的偏轉角度
    					   pAngle=-(float)Math.toDegrees( Math.asin((x2-tkposition_x)/Math.sqrt((x2-tkposition_x)*(x2-tkposition_x)+(y2-tkposition_y)*(y2-tkposition_y))));
    					 }
    					synchronized(osv.gdMain.dataLock) 	//將主資料鎖上
    					{
    						osv.gdMain.followtkfp[0]=fireflag;
    						osv.gdMain.followtkfp[1]=pAngle;
    						osv.activity.sendMessage(fireflag+"<#>"+pAngle+"<#>");
    						synchronized(osv.gdMainDraw.dataLock) //將繪制資料鎖上
    						{
    							osv.gdMainDraw.followtkfp[0]=fireflag;
    							osv.gdMainDraw.followtkfp[1]=pAngle;
    						}
    					}
    					break;
    			}
    		}
    		try 
    		{
				Thread.sleep(10);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		 }
	 }
    
}
