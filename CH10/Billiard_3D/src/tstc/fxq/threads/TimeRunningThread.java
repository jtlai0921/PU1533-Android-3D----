package tstc.fxq.threads;

import tstc.fxq.main.MySurfaceView;

/**
 * 
 *負責倒計時的執行緒
 *
 */
public class TimeRunningThread extends Thread {
	MySurfaceView mv;
	private boolean flag=true;
	private int sleepSpan=1000;		
	public TimeRunningThread(MySurfaceView gameView)
	{
		this.mv=gameView;
	}
	@Override
	public void run()
	{
		while(flag)
		{			
			 try{
	            	Thread.sleep(sleepSpan);//睡眠指定毫秒數
	            }
	            catch(Exception e){
	            	e.printStackTrace();//列印堆堆疊訊息
	            }
	            mv.timer.subtractTime(1);
		}
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
