package com.bn.gjxq.game;

import java.util.Queue;

import android.os.Bundle;
import android.os.Message;

import com.bn.gjxq.Constant;
import com.bn.gjxq.SoundUtil;
import com.bn.gjxq.manager.ChessRuleUtil;
import com.bn.gjxq.manager.Finish;
import com.bn.gjxq.manager.IntersectantUtil;
import com.bn.gjxq.manager.MatrixUtil;
import com.bn.gjxq.manager.RobotAutoUtil;
import com.bn.gjxq.manager.Vector3f;

//����ʧ@��C�������
public class DoActionThread extends Thread
{
    public boolean workFlag=true;		//������O�_�`���u�@�ЧӦ�
    GameSurfaceView gsv;				//����View�Ѧ�
    Queue<Action> aq;					//�ʧ@��C
    
    public DoActionThread(GameSurfaceView gsv)
    {
    	this.gsv=gsv;		//����View
    	this.aq=gsv.aq;		//�ʧ@��C
    }
    
    @Override
    public void run()
    {
    	while(workFlag)
    	{    		
    		Action ac=null;					//�ʧ@�Ѧ�
    		synchronized(gsv.aqLock)		//�ʧ@��C��
    		{
    			ac=aq.poll();				//�q�ʧ@��C�����X�@�Ӱʧ@�A�Y��C���S���ޱ��ʧ@�h���Xnull
    		}    		
    		if(ac!=null)					//�Y�ޱ��ʧ@�ѦҤ��Onull�A�Y���ʧ@�ݭn����
    		{
        		switch(ac.at)				//at���ޱ��ʧ@�����A�A�ھھޱ����A���椣�P���u�@
        		{
        		   case CHANGE_CAMERA:		//������v���ʧ@
        			  synchronized(gsv.gdMain.dataLock) 	//�N�D�����W
        			  {
        				  //�N�ޱ��ʧ@��a����Ƶ����ȵ��D���
        				  gsv.gdMain.calculCamare(ac.data[0], ac.data[1]);//data�}�C�O�h����XY�Z��
        				  synchronized(gsv.gdDraw.dataLock) //�Nø������W
        				  {
        					  //�N�D��Ƶ����ȵ�ø����
        					  gsv.gdDraw.updateCameraData(gsv.gdMain);
        				  }
        			  }
        		   break;
        		   case SELECT_3D:			//�B���ʧ@
        			   synchronized(gsv.gdMain.dataLock) //��W�D�����
        			   {
        				   //�������禳�S���Ŀ諸�Ѥl
        				   int xzI=-1;
        				   int xzJ=-1;
        				   float disMin=Float.MAX_VALUE;//�Z�����̤p��
        				   
        				   //�N�B���ʧ@����ƶǵ��u���k�p��XAB���I�b�@�ɮy�Шt������m
        				   Vector3f[] AB=IntersectantUtil.calculateABPosition
							(
								ac.data[0], //�ޱ��ʧ@������ơA�]���UĲ���I��xy�y�С^
								ac.data[1], 
								new float[]
								{
									gsv.gdMain.cx,//��v����m
									gsv.gdMain.cy,
									gsv.gdMain.cz,
									gsv.gdMain.tx,//�[���I��m
									gsv.gdMain.ty,
									gsv.gdMain.tz,
									gsv.gdMain.ux,//up�V�q
									gsv.gdMain.uy,
									gsv.gdMain.uz
								}
							);
        				   
        				   //�`���ѺФW��64���
        				   for(int i=0;i<8;i++)
        				   {
        						for(int j=0;j<8;j++)
        						{
        							int qzbh=gsv.gdMain.qzwz[i][j];//�Ѥl�s��
        							int qzxzzt=gsv.gdMain.qzxz[i][j];//�Ѥl�Ŀ窱�A
        							if(qzbh==-1)continue;//�Y�Ѥl�s���O-1�]�Ѥl���A���S��-1���A�^
        							//�Ѥl�bX�PZ�b�W�������q
        							float xOffset=GameStaticData.XOffsetQZ[i][j];
        							float zOffset=GameStaticData.ZOffsetQZ[i][j];
        							int hbqkTemp=(qzbh>5)?1:0;//�P�_�Ѥl�����A�A�p��5���դl  
        							MatrixUtil.setInitStack();//�_�l�ƪ�����ܴ��x�}currMatrix
        							
        							//�Y�Ѥl�Ŀ窱�A��1�]�Ŀ�^�h�uY�b�W��0.6�ӳ����סA�S�Ŀ�hY��V���h��
        							MatrixUtil.translate(xOffset, 0.6f+((qzxzzt==1)?0.6f:0.0f), zOffset);
        							//�ھ�hbqkTemp�Ѥl���A¶��Y�b����S������
        							MatrixUtil.rotate(GameStaticData.ANGLE[hbqkTemp], 0, 1, 0);
        							
        							//�D�X�b����y�Шt����v���P�B���g�u�M�Ѥl�]�򲰤��ӭ����I���̵u�Z��
        							float disTemp=IntersectantUtil.calObjectDisMin
        							(
        								new Vector3f(gsv.gdMain.cx,gsv.gdMain.cy,gsv.gdMain.cz),//��v����m
        								AB[0], //A�I
        								AB[1], //B�I
        								MatrixUtil.currMatrix,//����ثe�ܴ��x�} 
        								gsv.qizi[qzbh%6].aabb//�Ѥl���]��
        							);
        							
        							//�Y�D�X���Z��disTemp�p��Z�����̤p��disMin
        							if(disTemp<disMin)
        							{
        								disMin=disTemp;
        								xzI=i;//�N���Ī���m�����ȵ��Ŀ諸IJ
        		        				xzJ=j;
        							}
        						}
        				   }
        				   
        				   //���Ѥl�Ŀ�]�Y�Ŀ諸��m�O���Ī��^
        				   if(xzI!=-1&&xzJ!=-1)
        				   {        					  
        					   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)
            						{
        								if(i==xzI&&j==xzJ&&gsv.gdMain.qzxz[i][j]==0)//�Y�G�Ŀ��m���Ŀ窱�A�O���Ŀ窱�A
        								{		
        									if(gsv.gdMain.status==0)
        									{//�Y�ثe�S���Ŀ�Ѥl�A�h�ˬd�Ŀ�Ѥl�O�_������m��
        									 //�Y�ŦX����A�h�N�B���쪺�Ѥl�]�w���Ŀ諸�Ѥl
        									 //�ó]�w�ЧӦ�
        										if(gsv.gdMain.currColor==gsv.gdMain.getColor(i, j))
        										{//�Ŀ�Ѥl������m��
        											gsv.gdMain.qzxz[i][j]=1;//�ȴѤl�Ŀאּ1�]�Ŀ�^
            										gsv.gdMain.status=1;//���ܥثe���A�s�������Ѥl�Ŀ�
            										//����Ѥl�Ŀﭵ��
            										if(Constant.IS_YINXIAO)
            										{
            											SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            										}
        										}
        										else
        										{
        											sendMsgForToast("�ФĿ�v��m�⪺�Ѥl�I");
        										}
        									}
        									else
        									{//�Y�ثe�w�g���Ŀ�Ѥl�h�n�ݳo���Ŀ諸�Ѥl��_�Q�Y���A
        									   //�Y��Y���A�h���樫�l
        									   int toI=i;//�n���l����m
         									   int toJ=j;
         									   int fromI=0;//�_�l�Ѥl��m
         									   int fromJ=0;
         									   for(int a=0;a<8;a++)
         	                				   {
         	                						for(int b=0;b<8;b++)//�`���Ѻ�
         	                						{
         	                						   //�Y�Ѥl�Ŀאּ1�]�Ŀ窱�A�^�A�h��s�_�l�Ѥl����m
         	                						   if(gsv.gdMain.qzxz[a][b]==1)
         	                						   {
         	                							   fromI=a;
                     									   fromJ=b;
         	                						   }
         	                						}
         	                				   }
         									   boolean canFlag=ChessRuleUtil.chessRule//�I�s���ѳW�h���O�A�P�_�O�_������l
         									   (
         										   gsv.gdMain.qzwz, //�Ѥl��m
         										   new int[]{fromI,fromJ}, //�_�l�Ѥl��m
         										   new int[]{toI,toJ}//���l��m
         									   );
         									   if(canFlag)
           									   {//�Y�ਫ�l���樫�l�]�Y�l�^
         										   //�N�X�o��m���Ѥl�Ŀ窱�A�]�w��0�A��ܥ��Ŀ�
           										   gsv.gdMain.qzxz[fromI][fromJ]=0;
           										   //�N�ثe�Ѥl���b�Ŀ諸�ت���m
           										   gsv.gdMain.qzwz[toI][toJ]=gsv.gdMain.qzwz[fromI][fromJ];
           										   //�N�Ѥl��m�m���L�Ѥl
           										   gsv.gdMain.qzwz[fromI][fromJ]=GameData.no_qz;
           										   gsv.gdMain.status=0;//�m�ثe���A�s����0�]�L�Ѥl�Ŀ�^
           										   
           										   //����Y�l����
           										   if(Constant.IS_YINXIAO)
           										   {
           											   SoundUtil.playSounds(SoundUtil.CHIZI, 0, gsv.activity);
           										   }
           										   
           										   //���U�Ѥ�
        										   gsv.gdMain.currColor=(gsv.gdMain.currColor+1)%2;
        										   //Ū���O�_����Ĺ
        										   Finish result=ChessRuleUtil.isFinish(gsv.gdMain.qzwz);
        										   switch(result)
        										   {
        										   case BLACK_WIN://�¤�Ĺ�F�A���D�쵲���ɭ��]�P�_�O�HĹ�٬O����Ĺ�^
      										    	 this.workFlag=false;
      										    	 String tempshow;
      										    	 if(this.gsv.gdMain.humanColor==0)//�Y�G�H�O�¤�
      										    	 {
      										    		 tempshow="���ߧA�A�AĹ�F�I";
      										    		 sendMsgForDialog(tempshow);
      										    	 }
      										    	 else if(this.gsv.gdMain.humanColor==1)//�Y�G�H�O�դ�
      										    	 {
      										    		 tempshow="���n�N��A�A��F�I";
      										    		 sendMsgForDialog(tempshow);
      										    	 }
      										      break;
      										      case WHITE_WIN://�դ�Ĺ�F�A���D�쵲���ɭ��]�P�_�O�HĹ�٬O����Ĺ�^
      										    	 this.workFlag=false;
      										    	 if(this.gsv.gdMain.humanColor==0)//�Y�G�H�O�¤�
      										    	 {
      										    		 tempshow="���n�N��A�A��F�I";
      										    		 sendMsgForDialog(tempshow);
      										    	 }
      										    	 else if(this.gsv.gdMain.humanColor==1)//�Y�G�H�O�դ�
      										    	 {
      										    		 tempshow="���ߧA�A�AĹ�F�I";
      										    		 sendMsgForDialog(tempshow);
      										    	 }
          										  break;
      										      case NO_FINISH://�S�������A�h�������l
      										         this.jqzz();
      										      break;          										  
        										   }
           									   }
           									   else
           									   {//�Y���ਫ�l�h�󴫤Ŀ�Ѥl
	           										if(gsv.gdMain.currColor==gsv.gdMain.getColor(i, j))//�Ŀ�Ѥl������m��
	        										{
	           											gsv.gdMain.qzxz[i][j]=1;//�Ӧ�m���Ѥl�B��Ŀ窱�A
	         										    gsv.gdMain.status=1;//�m�ثe���A�s����1�]���Ѥl�Ŀ�^
	         										    //����Ѥl�Ŀ諸����
	         										    if(Constant.IS_YINXIAO)
	         										    {
	         										    	SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
	         										    }
	        										}
	        										else//�Ŀ�Ѥl���O����m��
	        										{
	        											sendMsgForToast("���B�H�ϳW�h�A���i���I");
	        										}
           									   }
        									}
        								}
        								else if(i==xzI&&j==xzJ&&gsv.gdMain.qzxz[i][j]==1)//�Y�G�ӤĿ��m���Ѥl�Ŀ窱�A���Ŀ窱�A�h
        								{	//��s�D��Ƥ��ӴѤl��m���Ŀ窱�A�����Ŀ窱�A
        									gsv.gdMain.qzxz[i][j]=0;
        									gsv.gdMain.status=0;//�m�ثe���A�s����0�]�L�Ѥl�Ŀ�^
        								}
        							}
            				   }
        					   
        					   //�`���ѺФW�Ҧ�����m,�N�������Ŀ諸�Ѥl��m�]�w�����Ŀ�
        					   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)
            						{
            						   if(!(i==xzI&&j==xzJ))//�Y�G�ثe��m���O�Ŀ��m��I�BJ
            						   {
            							   gsv.gdMain.qzxz[i][j]=0;//�N�ӴѤl��m���Ŀ窱�A�����Ŀ窱�A
            						   }
            						}
            				   }
        				   }
        				   
        				   //�Y�S���Ŀ諸�Ѥl�A�A�ݦ��S���Ŀ諸��l
        				   if(xzI==-1||xzJ==-1)
        				   {
            				   xzI=-1;
            				   xzJ=-1;
            				   disMin=Float.MAX_VALUE;
            				   
            				   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)//�`���ѺФW�Ҧ�����m
            						{     
            							//�ѺФ����X�PZ�b�������q  
            							float xOffset=GameStaticData.XOffset[i][j];
            							float zOffset=GameStaticData.ZOffset[i][j];
            							
            							//�_�l�ƪ�����ܴ��x�}currMatrix
            							MatrixUtil.setInitStack();
            							MatrixUtil.translate(xOffset, 0, zOffset);
            							
            							//�D�X����y�Шt����v���P�ѺФ���]�򲰩MAB���I�g�u���I���̤p�Z��
            							float disTemp=IntersectantUtil.calObjectDisMin
            							(
            								new Vector3f(gsv.gdMain.cx,gsv.gdMain.cy,gsv.gdMain.cz),//��v���y��
            								AB[0], //A�I
            								AB[1], //B�I
            								MatrixUtil.currMatrix, //����ثe�ܴ��x�}
            								gsv.block.aabb//�ѺФ�����]��
            							);
            							
            							if(disTemp<disMin)
            							{//�Y�GdisTemp�p��disMin�h��sdisMin�ç�s�Ŀ�IJ���ثe�`���쪺i�Bj
            								disMin=disTemp;
            								xzI=i;
            		        				xzJ=j;
            							}
            						}
            				   }
            				   
            				   if(xzI!=-1&&xzJ!=-1)//�ѺФ�����Ŀ��m�O���Ī��]�S���Ŀ�Ѥl���O���Ŀ諸��l�^
            				   {
            					   for(int i=0;i<8;i++)
                				   {
                						for(int j=0;j<8;j++)
                						{
                						   if(i!=xzI||j!=xzJ)//�Y�G�`���쪺��m���O�ѺФ���Ŀ諸��m
                						   {				 //�h��s�D��Ƥ���l�Ŀ窱�A��0�]���Ŀ窱�A�^
                							   gsv.gdMain.gzxz[i][j]=0;
                						   }
                						   else		//�Y�G�`���쪺��m�O�ѺФ���Ŀ諸��m
                						   {		
                							   if(gsv.gdMain.qzwz[i][j]==-1)//�Y�G�Ŀ諸��l��m�W�S���Ѥl
                							   {
                								   if(gsv.gdMain.status==1)//�ثe���A�s����1�]���Ѥl�Ŀ�^
                								   {//�Y�Ŀ��l�ɦ��Ŀ諸�Ѥl�h�I�s�W�h�ݯ�_����                									   
                									   int toI=i;//��s�n�h���Ѥl��m
                									   int toJ=j;
                									   int fromI=0;//��s�_�l�I���Ѥl��m
                									   int fromJ=0;
                									   for(int a=0;a<8;a++)
                	                				   {
                	                						for(int b=0;b<8;b++)//�`���ѺСA���Ѥl���ثe��m
                	                						{
                	                						   if(gsv.gdMain.qzxz[a][b]==1)//�Ѥl���Ŀ窱�A
                	                						   {
                	                							   fromI=a;//��s�_�l�I���Ѥl��m
                            									   fromJ=b;
                	                						   }
                	                						}
                	                				   }
                									   
                									   //�I�s���ѳW�h���O�A�P�_�O�_������l
                									   boolean canFlag=ChessRuleUtil.chessRule
                									   (
                										   gsv.gdMain.qzwz, //�Ѥl��m
                										   new int[]{fromI,fromJ}, //�_�l�Ѥl��m
                										   new int[]{toI,toJ}//���l��l
                									   );
                									   if(canFlag)//�Y�ਫ�ѫh���ѡ]���l��e�\���ťզ�m�^
                									   {
                										   gsv.gdMain.qzxz[fromI][fromJ]=0;//�N�Ѥl�Ŀ�m��0�]�Y�ӳB�L�Ѥl�^
                										   //�N�ثe�Ѥl���b�Ŀ諸�ت���m
                										   gsv.gdMain.qzwz[toI][toJ]=gsv.gdMain.qzwz[fromI][fromJ];
                										   //�N�_�l���Ѥl��m�m���L�Ѥl
                										   gsv.gdMain.qzwz[fromI][fromJ]=GameData.no_qz;
                										   gsv.gdMain.status=0;//�m�ثe���A�s�����L�Ѥl�Ŀ�
                										   
                										   //���񴶳q���l������
                										   if(Constant.IS_YINXIAO)
                										   {
                											   SoundUtil.playSounds(SoundUtil.PUTONGLUOZI, 0, gsv.activity);
                										   }
                										   
                										   //���U�Ѥ�
                										   gsv.gdMain.currColor=(gsv.gdMain.currColor+1)%2;
                										   //Ū���O�_����Ĺ
                										   Finish result=ChessRuleUtil.isFinish(gsv.gdMain.qzwz);
                										   switch(result)
                										   {
                										   case BLACK_WIN://�¤�Ĺ�F�A���D�쵲���ɭ��]�P�_�O�HĹ�٬O����Ĺ�^
              										    	 this.workFlag=false;
              										    	 String tempshow;
              										    	 if(this.gsv.gdMain.humanColor==0)//�Y�G�H�O�¤�
              										    	 {
              										    		 tempshow="���ߧA�A�AĹ�F�I";
              										    		 sendMsgForDialog(tempshow);
              										    	 }
              										    	 else if(this.gsv.gdMain.humanColor==1)//�Y�G�H�O�դ�
              										    	 {
              										    		 tempshow="���n�N��A�A��F�I";
              										    		 sendMsgForDialog(tempshow);
              										    	 }
              										      break;
              										      case WHITE_WIN://�դ�Ĺ�F�A���D�쵲���ɭ��]�P�_�O�HĹ�٬O����Ĺ�^
              										    	 this.workFlag=false;
              										    	 if(this.gsv.gdMain.humanColor==0)//�Y�G�H�O�¤�
              										    	 {
              										    		 tempshow="���n�N��A�A��F�I";
              										    		 sendMsgForDialog(tempshow);
              										    	 }
              										    	 else if(this.gsv.gdMain.humanColor==1)//�Y�G�H�O�դ�
              										    	 {
              										    		 tempshow="���ߧA�A�AĹ�F�I";
              										    		 sendMsgForDialog(tempshow);
              										    	 }
                  										  break;
              										      case NO_FINISH://�S�������A�h�������l
              										         this.jqzz();
              										      break;            	        										  
                										   }
                									   }
                									   else//�Y���ਫ�ѫh��ܴ��ܰT��
                									   {
                										   gsv.gdMain.gzxz[i][j]=1;//��l�Ŀ�m��1�]�Ŀ窱�A�^                    									   
                    									   sendMsgForToast("���B�H�ϳW�h�A���i���I");
                									   }
                								   }
                								   else
                								   {                									   
                									   sendMsgForToast("�Х��Ŀ�Ѥl�I");
                								   }
                							   }
                						   }
                						}
                				   }
            				   }
        				   }
        				   else//�Y���Ŀ諸�Ѥl�h�����Ҧ���l�Ŀ�
        				   {
        					   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)
            						{
            							//��s�D��Ƥ���l�Ŀאּ0�]�Y�Ӯ�l�S���Ŀ�^
            							gsv.gdMain.gzxz[i][j]=0;
            						}
            				   }
        				   }
        				   //�N�s���D��Ƨ�s��ø���Ƥ�
        				   synchronized(gsv.gdDraw.dataLock)//��Wø������
    					   {
    						   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)
            						{
            							//�N�D���copy�iø����
            							gsv.gdDraw.qzwz[i][j]=gsv.gdMain.qzwz[i][j];//�Ѥl��m
            							gsv.gdDraw.qzxz[i][j]=gsv.gdMain.qzxz[i][j];//�Ѥl�Ŀ�
            							gsv.gdDraw.gzxz[i][j]=gsv.gdMain.gzxz[i][j];//��l�Ŀ�
            						}
            				   }
    					   }
        			   }
        		   break;
        		}
    		}
    		
    		try 
    		{
				Thread.sleep(25);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
    	}
    }
    
    //���Toast��k
    public void sendMsgForToast(String msgStr)
    {
    	Bundle bundle=new Bundle();
	    bundle.putString("msg",msgStr);
	    Message msg=new Message();
	    msg.what=Constant.COMMAND_TOAST_MSG;
	    msg.setData(bundle);
	    gsv.activity.handler.sendMessage(msg);
    }
    //���dialog��k
    public void sendMsgForDialog(String msgStr)
    {
    	Bundle bundle=new Bundle();
	    bundle.putString("msg",msgStr);
	    Message msg=new Message();
	    msg.what=Constant.COMMAND_DIALOG_MSG;
	    msg.setData(bundle);
	    gsv.activity.handler.sendMessage(msg);
    }
    
    //�����ھڥثe���p���l
    public void jqzz()
    {    	
    	new Thread()
    	{
    		public void run()
    		{
    			try 
    			{
					Thread.sleep(2000);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
    			synchronized(gsv.gdMain.dataLock)
    			{
    				int[][] qzwzTemp=RobotAutoUtil.autoGo
        	    	(
        	    		gsv.gdMain.qzwz,//�ثe�Ѻб��p
        	    		gsv.gdMain.humanColor//�H���m��
        	    	);
    				
    				switch(RobotAutoUtil.preAction)
    				{
    					case PTZZ:
    						if(Constant.IS_YINXIAO)
    						{
    							SoundUtil.playSounds(SoundUtil.PUTONGLUOZI, 0, gsv.activity);
    						}
    					break;
    					case CZ:
    						if(Constant.IS_YINXIAO)
    						{
    							SoundUtil.playSounds(SoundUtil.CHIZI, 0, gsv.activity);
    						}
    					break;
    				}
    				
    				for(int i=0;i<8;i++)
      				{
      				     for(int j=0;j<8;j++)
      					 {
      							//�N�D���copy�iø����
      							gsv.gdMain.qzwz[i][j]=qzwzTemp[i][j];//�Ѥl��m
      					 }
      				}
    				
    				//���U�Ѥ�
					gsv.gdMain.currColor=(gsv.gdMain.currColor+1)%2;
					//Ū���O�_����Ĺ
					Finish result=ChessRuleUtil.isFinish(gsv.gdMain.qzwz);
					switch(result)
				    {
				    case BLACK_WIN://�¤�Ĺ�F�A���D�쵲���ɭ��]�P�_�O�HĹ�٬O����Ĺ�^
				    	 workFlag=false;
				    	 String tempshow;
				    	 if(gsv.gdMain.humanColor==0)//�Y�G�H�O�¤�
				    	 {
				    		 tempshow="���ߧA�A�AĹ�F�I";
				    		 sendMsgForDialog(tempshow);
				    	 }
				    	 else if(gsv.gdMain.humanColor==1)//�Y�G�H�O�դ�
				    	 {
				    		 tempshow="���n�N��A�A��F�I";
				    		 sendMsgForDialog(tempshow);
				    	 }
				      break;
				      case WHITE_WIN://�դ�Ĺ�F�A���D�쵲���ɭ��]�P�_�O�HĹ�٬O����Ĺ�^
				    	 workFlag=false;
				    	 if(gsv.gdMain.humanColor==0)//�Y�G�H�O�¤�
				    	 {
				    		 tempshow="���n�N��A�A��F�I";
				    		 sendMsgForDialog(tempshow);
				    	 }
				    	 else if(gsv.gdMain.humanColor==1)//�Y�G�H�O�դ�
				    	 {
				    		 tempshow="���ߧA�A�AĹ�F�I";
				    		 sendMsgForDialog(tempshow);
				    	 }
					  break; 
				      case NO_FINISH://�S�������A�h�������l
				    	 //�N�s���D��Ƨ�s��ø���Ƥ�
	       				 synchronized(gsv.gdDraw.dataLock)//��Wø������
	   					 {
	   						 for(int i=0;i<8;i++)
	           				 {
	           				     for(int j=0;j<8;j++)
	           					 {
	           							//�N�D���copy�iø����
	           							gsv.gdDraw.qzwz[i][j]=gsv.gdMain.qzwz[i][j];//�Ѥl��m
	           							gsv.gdDraw.qzxz[i][j]=gsv.gdMain.qzxz[i][j];//�Ѥl�Ŀ�
	           							gsv.gdDraw.gzxz[i][j]=gsv.gdMain.gzxz[i][j];//��l�Ŀ�
	           					 }
	           				 }
	   					 }
					  break;   
				    }
    			}
    		}
    	}.start();
    }
}
