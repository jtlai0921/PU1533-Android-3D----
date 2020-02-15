package com.bn.ball;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.bn.ball.cube.CubeThread;
import com.bn.ball.util.SQLiteUtil;


public class BallThread extends Thread{
  
	Quaternion quaternionTotal = Quaternion.getIdentityQuaternion();
	List<WuTiForControl> zawList; //��ê���M��
	MySurfaceView mv;
	RadioBallActivity activity;
    DaoJuThread dt;
	public BallThread(List<WuTiForControl> zawList,MySurfaceView mv,GL10 gl,RadioBallActivity activity){
		this.zawList=zawList;
		this.mv=mv;
		this.activity=activity;
	}
    	public void run()
    	{
    		while(Constant.flag)
    		{
    			Constant.BALL_Z+=Constant.vz*Constant.vk;
    			Constant.sumLoadScore=(int) (-Constant.BALL_Z/5*20);
    			if(Constant.BALL_Z<-500)
    			{
    				SQLiteUtil.insertTime(Constant.sumBoardScore);
    				Constant.winFlag=true;
    				Constant.flag=false;
    			}
        	 	if(Constant.cUpY>0.1f&&Constant.BALL_X>-1.8)
            	{
            		Constant.BALL_X-=0.02f;
            	}else if(Constant.cUpY<-0.1f&&Constant.BALL_X<1.8)
            	{
            		Constant.BALL_X+=0.02f;
            	} 
    			if(Constant.cUpY>0.1)
    			{
    				Constant.vx=-0.02f;
    			}else if(Constant.cUpY<-0.1) 
    			{
    				Constant.vx=0.02f;
    			}else
    			{
    				Constant.vx=0;
    				
    			}
    				//�p���`�t��
    				float vTotal=(float)Math.sqrt(Constant.vx*Constant.vx+Constant.vz*Constant.vz*Constant.vk*Constant.vk);
    				//�p��y���B�B�ʪ��Z��
    				float distance=(float)vTotal*Constant.TIME_SPAN;			
    				//�p�⦹�B���ʮ�¶���઺�b���V�q
    				float axisXTemp=Constant.vz;
    				float axisYTemp=0;//�]���y�b�ୱ�W�B�ʱ���b�����ୱ�A�]���S��Y���q
    				float axisZTemp=-Constant.vx;
    				//����b�V�q�T���s��
    				Vector3f tmpAxis = new Vector3f(axisXTemp, axisYTemp, axisZTemp);
    				//�ھڹB�ʪ��Z���p��X�y�ݭn���ʪ�����
    				float tmpAngrad = distance/(Constant.BALL_R);
    				//�إ��{�ɥ|����
    				Quaternion tmpQuaternion = new Quaternion();
    				//�z�L����b�M���ਤ�׳]�w�|���ƪ���
    				tmpQuaternion.setToRotateAboutAxis(tmpAxis, tmpAngrad);
    				//�N�{�ɥ|���Ƥe������|����
    				quaternionTotal = quaternionTotal.cross(tmpQuaternion);
    				//���o�ثe�������b
    				Vector3f axis = quaternionTotal.getRotationAxis();
    				//���o�ثe������ਤ
    				float angrad = quaternionTotal.getRotationAngle();
    				//�N�ثe���A������������ਤ�׻P�b��ưO���i�����ܼ�
    				Constant.currAxisX = axis.x;
    				Constant.currAxisY = axis.y;
    				Constant.currAxisZ = axis.z; 
    				Constant.angleCurr = (float) Math.toDegrees(angrad);  	
//    				System.out.println("======================"+Constant.vk+"++++++++++++"+Constant.vz);
    				pengZhuang();
    				
    				if(Constant.vz*Constant.vk>-0.05&&!Constant.bolipzFlag)
					{
    					SQLiteUtil.insertTime(Constant.sumBoardScore);
						Constant.vz=0;
						Constant.loseFlag=true;
						Constant.flag=false;
						
//						System.out.println("======================ZZZZZZZZZZZZZZZZZZZ");
					}
    			try
                {
              	  Thread.sleep(20);//��10ms�A��ø
                }
                catch(Exception e)
                {
              	  e.printStackTrace();
                }     
    		}
    	}
    	
    	public void pengZhuang(){
    	
    		for(WuTiForControl zfcTemp:zawList)//ø���ê��
            {
                if(zfcTemp.zOffset+Constant.container_Length<Constant.BALL_Z){
                	break;
                }
                if(zfcTemp.zOffset-Constant.container_Length>Constant.BALL_Z){
                	continue;
                }  
                if(zfcTemp.zOffset+Constant.cube_Length<Constant.BALL_Z){
                	break;
                }
                if(zfcTemp.zOffset-Constant.cube_Length>Constant.BALL_Z){
                	continue;
                }
                if(zfcTemp.zOffset+Constant.yuanZhu_Length<Constant.BALL_Z){
                	break;
                }
                if(zfcTemp.zOffset-Constant.yuanZhu_Length>Constant.BALL_Z){
                	continue;
                }
                if(zfcTemp.zOffset+Constant.liFangTi_Length<Constant.BALL_Z){
                	break;
                }
                if(zfcTemp.zOffset-Constant.liFangTi_Length>Constant.BALL_Z){
                	continue;
                }
                if(zfcTemp.id==0)
                {
                	if(zfcTemp.zOffset+Constant.yuanZhu_Length>=Constant.BALL_Z&&
                			zfcTemp.zOffset-Constant.yuanZhu_Length<=Constant.BALL_Z&&
                			zfcTemp.xOffset+Constant.yuanZhu_Width>=Constant.BALL_X+Constant.BALL_R&&
                			zfcTemp.xOffset-Constant.yuanZhu_Width<=Constant.BALL_X-Constant.BALL_R&&
                			-1+Constant.yuanZhu_Height>=Constant.BALL_Y-Constant.BALL_R)
                	{             		
                		if(zfcTemp.quanHao!=0)
                		{
                			Constant.vz=Constant.vz+Constant.upValue;
                    		if(activity.yinXiao){
    							activity.playSound(2, 0);
    						}
                    		System.out.println("000000000000000000000");
                		}
                	}
                		zfcTemp.quanHao=0;
                	
                	
                }else if(zfcTemp.id==1)
                {
                	if(zfcTemp.zOffset+Constant.liFangTi_Length>=Constant.BALL_Z&&
                			zfcTemp.zOffset-Constant.liFangTi_Length<=Constant.BALL_Z&&
                			zfcTemp.xOffset+Constant.liFangTi_Width>=Constant.BALL_X+Constant.BALL_R&&
                			zfcTemp.xOffset-Constant.liFangTi_Width<=Constant.BALL_X-Constant.BALL_R&&
                			-1+Constant.liFangTi_Height>=Constant.BALL_Y-Constant.BALL_R)
                	{    
                   		if(zfcTemp.quanHao!=0)
                		{
                			Constant.vz=Constant.vz-Constant.downValue;
                      		if(activity.yinXiao){
    							activity.playSound(2, 0);
    						}
                		}
                	}
                		zfcTemp.quanHao=0;
                	
                }else if(zfcTemp.id==2)
                {
                	if(zfcTemp.zOffset+Constant.container_Length>=Constant.BALL_Z&&
                			zfcTemp.zOffset-Constant.container_Length<=Constant.BALL_Z-Constant.BALL_R&&
                			zfcTemp.xOffset+Constant.container_Width>=Constant.BALL_X+Constant.BALL_R&&  
                			zfcTemp.xOffset-Constant.container_Width<=Constant.BALL_X-Constant.BALL_R&&
                			-1+Constant.container_Height>=Constant.BALL_Y-Constant.BALL_R        
                			)
                	{                         
                		if(zfcTemp.quanHao!=0)
                		{
                			Constant.vz=Constant.vz-Constant.downValue;
                     		if(activity.yinXiao){
    							activity.playSound(2, 0);
    						}
//                     		System.out.println("22222222222222222");
                		}

                    }
                		zfcTemp.quanHao=0;
                	
                }else if(zfcTemp.id==3)
                {
                	if(zfcTemp.xOffset+Constant.boLi_Length>=Constant.BALL_X+Constant.BALL_R&&  
                			zfcTemp.xOffset-Constant.boLi_Length<=Constant.BALL_X-Constant.BALL_R&&
                			-1+Constant.boLi_Height>=Constant.BALL_Y-Constant.BALL_R         
                			)
                	{
                		
                		if(zfcTemp.quanHao!=0)
                		{
                			if(!Constant.bolipzFlag)
                			{ 
                				
                				if(Constant.BALL_Z<zfcTemp.zOffset-0.2f)
                				{
            					zfcTemp.quanHao=0;
                				}
                				if(Constant.pengCount<0f)
                				{
                					Constant.pengCount=0;
                				}
                				if(activity.yinXiao){
        							activity.playSound(2, 0);
        						}
                				if(zfcTemp.quanHao!=0){
                				Constant.vk=-Constant.pengCount*Constant.vk;
                				}
                			    if(Constant.gameOver)
                			    {
                    				Constant.bolipzFlag=true;
                    				
                    				new BoLiPZThread(zfcTemp).start();
                    				Constant.gameOver=false;
                			    }
                				Constant.pengCount-=0.1f;
                			}
                		}
                    }
                }else if(zfcTemp.id==4) 
                {
                	if(zfcTemp.zOffset+Constant.cube_Length>=Constant.BALL_Z&&
                			zfcTemp.zOffset-Constant.cube_Length<=Constant.BALL_Z&&
                			zfcTemp.xOffset+Constant.cube_Width>=Constant.BALL_X+Constant.BALL_R&&  
                			zfcTemp.xOffset-Constant.cube_Width<=Constant.BALL_X-Constant.BALL_R&&
                			-1+Constant.cube_Height>=Constant.BALL_Y-Constant.BALL_R         
                			)
                	{
                		
                		if(zfcTemp.quanHao!=0)
                		{
                			zfcTemp.quanHao=0;
                			com.bn.ball.cube.Constant.THREAD_FLAG=true;
                        	mv.rotate_flag=true;
                        	new CubeThread(mv).start();
                        	if(activity.yinXiao){
    							activity.playSound(1, 0);
    						}
                        	Constant.daojuFlag=true;
                        	Constant.num=(int)(Math.random()*2);
                        	Constant.daojuZFlag=true;
                        	Constant.daoJuZ=zfcTemp.zOffset+Constant.change;
                        	if(Constant.num==0)
                        	{
                        		
                        		if(Constant.sumBoardScore>200)
                        		{
                        			Constant.sumEfectScore-=200;
                        		}else
                        		{
                        			Constant.sumBoardScore=0;
                        		}
                        		
                        	}else
                        	{
                        		Constant.sumEfectScore+=500;
                        		
                        	}
                            dt=new DaoJuThread();
                            dt.start();
                		}
                    }
                }                
            }
    	}
    }

