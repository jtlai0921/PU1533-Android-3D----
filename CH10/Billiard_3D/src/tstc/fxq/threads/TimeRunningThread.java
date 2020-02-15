package tstc.fxq.threads;

import tstc.fxq.main.MySurfaceView;

/**
 * 
 *�t�d�˭p�ɪ������
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
	            	Thread.sleep(sleepSpan);//�ίv���w�@���
	            }
	            catch(Exception e){
	            	e.printStackTrace();//�C�L����|�T��
	            }
	            mv.timer.subtractTime(1);
		}
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
