package com.bn.txz.game;

import static com.bn.txz.Constant.YAOGAN_CENTER_X;
import static com.bn.txz.Constant.YAOGAN_CENTER_Y;
import static com.bn.txz.Constant.YAOGAN_R;
import static com.bn.txz.game.TXZGameSurfaceView.vAngle;

import java.util.Queue;

import com.bn.txz.Constant;

public class YGDoActionThread extends Thread{
	public boolean workFlag=true;		//������O�_�`���u�@�ЧӦ�
    TXZGameSurfaceView gsv;				//����View�Ѧ�
    Queue<Action> aq;
  //�n�줤�߶��I�������q
    public static float scale_x=0;
    public static float scale_y=0;
    
    public YGDoActionThread(TXZGameSurfaceView gsv)
    {
    	this.gsv=gsv;		//����View
    	this.aq=gsv.aqyg;		//�ʧ@��C
    }

	@Override
	public void run() {
		while(workFlag)
		{
			Action ac=null;					//�ʧ@�Ѧ�
    		synchronized(gsv.aqygLock)		//�ʧ@��C��
    		{
    			ac=aq.poll();				//�q�ʧ@��C�����X�@�Ӱʧ@�A�Y��C���S���ޱ��ʧ@�h���Xnull
    		}
    		if(ac!=null)
    		{
    			switch(ac.at)
    			{
    			case YAOGAN_MOVE:
     			   float x;
     			   float y;
     			   
     			   if(Math.sqrt((ac.data[0]-Constant.YAOGAN_CENTER_X)*(ac.data[0]-Constant.YAOGAN_CENTER_X)+
                 		   (ac.data[1]-Constant.YAOGAN_CENTER_Y)*(ac.data[1]-Constant.YAOGAN_CENTER_Y))<Constant.YAOGAN_R)
     			   {
     				   x=ac.data[0];
         			   y=ac.data[1];
     			   }
     			   else
     			   {
     				 //�ثe�I�y�Ш�n�줤���I���Z��
					   float tempR=(float) Math.sqrt((ac.data[0]-YAOGAN_CENTER_X)*(ac.data[0]-YAOGAN_CENTER_X)+(YAOGAN_CENTER_Y-ac.data[1])*(YAOGAN_CENTER_Y-ac.data[1]));
					   //�N�ثe�I�y�бj���ର�n��Ĳ���d����t�y��
					   x=YAOGAN_CENTER_X+(ac.data[0]-YAOGAN_CENTER_X)*YAOGAN_R/tempR;
					   y=YAOGAN_CENTER_Y+(ac.data[1]-YAOGAN_CENTER_Y)*YAOGAN_R/tempR;
     			   }
	    			 //�n�줤�߶��I��x�b�����q
					scale_x=(x-YAOGAN_CENTER_X)/YAOGAN_R;
					//�n�줤�߶��I��y�b�����q
					scale_y=(YAOGAN_CENTER_Y-y)/YAOGAN_R;
     			   TXZGameSurfaceView.offsetx=scale_x;
     			   TXZGameSurfaceView.offsety=scale_y;
     			   if(y<YAOGAN_CENTER_Y)
   				   	{
   				   		//�p��n�쪺���ਤ��
   					   	vAngle=-(float)Math.toDegrees( Math.asin((x-YAOGAN_CENTER_X)/Math.sqrt((x-YAOGAN_CENTER_X)*(x-YAOGAN_CENTER_X)+(y-YAOGAN_CENTER_Y)*(y-YAOGAN_CENTER_Y))));
   				   	}
   					if(y>YAOGAN_CENTER_Y)
   				   	{
   				   		vAngle=-180+(float)Math.toDegrees( Math.asin((x-YAOGAN_CENTER_X)/Math.sqrt((x-YAOGAN_CENTER_X)*(x-YAOGAN_CENTER_X)+(y-YAOGAN_CENTER_Y)*(y-YAOGAN_CENTER_Y))));
   				   	}
	   			break;
    			case ACTION_UP:
     			   TXZGameSurfaceView.offsetx=0;
     			   TXZGameSurfaceView.offsety=0;
     			   TXZGameSurfaceView.vAngle=100;
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
