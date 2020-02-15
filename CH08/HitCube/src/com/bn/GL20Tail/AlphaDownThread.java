package com.bn.GL20Tail;

public class AlphaDownThread extends Thread
{
	TailPart tpTochage;
	GoThread gt;
	public AlphaDownThread(TailPart tpTochage,GoThread gt)
	{
	this.tpTochage=tpTochage;
	this.gt=gt;
	}
		public void run()
		{  
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  
			while(tpTochage.alpha>=-0.01)
			{
				
				if(tpTochage.alpha==0.0f)
				{
					gt.garbage.remove(tpTochage);
				} 
				if(tpTochage.alpha>=0.6)
				{
					tpTochage.alpha-=0.2f; 
				}else{
					tpTochage.alpha-=0.05f; 
				}
				
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}  
			} 
		}
}
