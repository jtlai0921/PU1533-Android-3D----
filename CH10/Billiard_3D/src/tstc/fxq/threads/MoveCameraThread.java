package tstc.fxq.threads;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;

/*
 * 搬移攝影機的執行緒類別
 */
public class MoveCameraThread extends Thread{
	MySurfaceView mv;
	private int sleepSpan=5;
	//目前搬移攝影機的執行緒
	public static MoveCameraThread currThread;
	//攝影機搬移的起始點
	public static float xFrom;
	public static float zFrom;
	//攝影機搬移的終止點
	public static float xTo;
	public static float zTo;
	
	//攝影機每次搬移的步進
	private float moveSpan = 0.05f;//實際搬移步進
	private float moveSpanX;//x方向搬移步進
	private float moveSpanZ;//z方向搬移步進
	//攝影機搬移總次數
	private int totalSteps;
	
	public MoveCameraThread
	(
			MySurfaceView mv
	)
	{
		this.mv = mv;
		
		//起點到終點的向量
		float vecX = MoveCameraThread.xTo-MoveCameraThread.xFrom;
		float vecZ = MoveCameraThread.zTo-MoveCameraThread.zFrom;
		//起點和終點的距離
		float dis = (float) Math.sqrt(vecX*vecX + vecZ*vecZ);
		//計算x、y方向上搬移步進
		moveSpanX = vecX/dis*moveSpan;
		moveSpanZ = vecZ/dis*moveSpan;
		//攝影機搬移總次數
		totalSteps = (int) (dis/moveSpan);
	}
	@Override
	public void run()
	{
		for(int i=0; i<totalSteps+1; i++)
		{
			//計算目前目的點位置，並設定攝影機的位置
			mv.setCameraPostion(xFrom + i*moveSpanX, zFrom + i*moveSpanZ);

			try{
            	Thread.sleep(sleepSpan);//睡眠指定毫秒數
            }
            catch(Exception e){
            	e.printStackTrace();//列印堆堆疊訊息
            }
		}
		
		//動畫播放完後，改變關聯標志位	
		Constant.cueFlag=true;//繪制球桿標志位
        //設定攝影機的位置到白球處
		mv.setCameraPostion(MySurfaceView.ballAl.get(0).xOffset, MySurfaceView.ballAl.get(0).zOffset);
	}
}
