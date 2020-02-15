package com.bn.zxl;

import static com.bn.util.Constant.Cloud_Position;

import org.jbox2d.dynamics.joints.RevoluteJointDef;

import com.bn.box.Box2DUtil;


public class Level_Data 
{
	public static void loadGameData(GameView gv,int levelNum)
	{
		if(levelNum==0)
		{
			gv.BallList.add(
	        		Box2DUtil.createBall(gv.world, 480, 440, 25, false,gv.textureId_cebi, gv)
	        );
			gv.reclist.add(
	        		Box2DUtil.creatMuTong(gv.world, 420, 420, 27f, 38f, false, gv)
			);
	        gv.reclist.add(//下面地面
	        		Box2DUtil.creatRec(gv.world, 480f, 500f, 800f, 20f, true, gv,-1)
	        );
	        
		}
		else if(levelNum==1)
		{
			gv.BallList.add(
				Box2DUtil.createBall(gv.world, 495, 470, 25, false,gv.textureId_cebi, gv)	
			);
			gv.reclist.add(
					Box2DUtil.createBall(gv.world, 460, 510, 25, true,gv.textureId_di, gv)	
				);
			gv.reclist.add(
					Box2DUtil.createBall(gv.world, 530, 510, 25, true,gv.textureId_di, gv)	
				);
			gv.reclist.add(
					Box2DUtil.createBall(gv.world, 600, 470, 25, true,gv.textureId_di, gv)	
				);
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 940f, 520f, 10f, 10f, true, gv,2)
	        );
		}
		else if(levelNum==2)
		{
			gv.BallList.add(
					Box2DUtil.createBall(gv.world, 536, 460, 25, false,gv.textureId_cebi, gv)	
				);
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 453f, 203f, 34f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 571f, 354f, 10f, 108f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 365f, 360f, 38f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 484f, 500f, 95f, 10f, true, gv,2)
	        );
			gv.reclist.add(
					Box2DUtil.creatRain(gv.world, Cloud_Position, 940, 520, 10, true, gv)
			);
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 940f, 520f, 10f, 10f, true, gv,2)
	        );
			
		}
		else if(levelNum==3)
		{
			gv.BallList.add(
					Box2DUtil.createBall(gv.world, 212, 138, 25, false,gv.textureId_cebi, gv)	
				);
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 148f, 176f, 68f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 760f, 385f, 105f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 853f, 442f, 10f, 40f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatShizijia(325, 211, 100, 10, false, gv.world, gv)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 325, 211, 5f, 5f, true, gv,2)
	        );
	        RevoluteJointDef rjd=new RevoluteJointDef();
			rjd.initialize(gv.reclist.get(3).body, gv.reclist.get(4).body, gv.reclist.get(4).body.getWorldCenter());
			gv.world.createJoint(rjd);
	        gv.reclist.add(
	        		Box2DUtil.creatShizijia(460, 388, 100, 10, false, gv.world, gv)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 460, 388, 5f, 5f, true, gv,2)
	        );
	        RevoluteJointDef rjd1=new RevoluteJointDef();
			rjd1.initialize(gv.reclist.get(5).body, gv.reclist.get(6).body, gv.reclist.get(6).body.getWorldCenter());
			rjd1.maxMotorTorque=10000000;
			rjd1.motorSpeed=-0.5f;
			rjd1.enableMotor=true;
			gv.world.createJoint(rjd1);
			
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 650, 492, 118f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 940f, 520f, 10f, 10f, true, gv,2)
	        );
		}
		else if(levelNum==4)
		{
			gv.BallList.add(
					Box2DUtil.createBall(gv.world, 319, 212, 25, false,gv.textureId_cebi, gv)	
				);
			gv.BallList.add(
					Box2DUtil.createBall(gv.world, 635, 212, 25, false,gv.textureId_cebi, gv)	
				);
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 364f, 250f, 56f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 590f, 250f, 56f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 276f, 238f, 10f, 70f, false, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 685f, 238f, 10f, 70f, false, gv,2)
	        );

	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 333f, 326f, 100f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 627f, 326f, 100f, 10f, true, gv,2)
	        );
	        RevoluteJointDef rjd2=new RevoluteJointDef();
			rjd2.initialize(gv.reclist.get(4).body, gv.reclist.get(2).body, gv.reclist.get(2).body.getWorldCenter());
			gv.world.createJoint(rjd2);
	        RevoluteJointDef rjd3=new RevoluteJointDef();
			rjd3.initialize(gv.reclist.get(5).body, gv.reclist.get(3).body, gv.reclist.get(3).body.getWorldCenter());
			gv.world.createJoint(rjd3);
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 380f, 421f, 10f, 83f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 580f, 421f, 10f, 83f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 420f, 466f, 25f, 38f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 543f, 466f, 25f, 38f, true, gv,2)
	        );
			gv.reclist.add(
					Box2DUtil.createBall(gv.world, 430, 290, 22, false,1, gv)	
				);
			gv.reclist.add(
					Box2DUtil.createBall(gv.world, 527, 290, 22, false,1, gv)	
				);
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 940f, 520f, 10f, 10f, true, gv,2)
	        );
		}
		else if(levelNum==5)
		{
			gv.BallList.add(
					Box2DUtil.createBall(gv.world, 424, 249, 25, false,gv.textureId_cebi, gv)	
				);
			gv.BallList.add(
					Box2DUtil.createBall(gv.world, 527, 249, 25, false,gv.textureId_cebi, gv)	
				);
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 267f, 125f, 20f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 267f, 225f, 20f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 683f, 125f, 20f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 683f, 225f, 20f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 313f, 326f, 20f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 638f, 326f, 20f, 10f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 424f, 346f, 10f, 70f, false, gv,2)
	        );
	        
	        
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 527f, 346f, 10f, 70f, false, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 424f, 441f, 10f, 20f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 527f, 441f, 10f, 20f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 236f, 301f, 10f, 188f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 716f, 301f, 10f, 188f, true, gv,2)
	        );
	        gv.reclist.add(
	        		Box2DUtil.creatRec(gv.world, 940f, 520f, 10f, 10f, true, gv,2)
	        );
		}
		
	}
}
