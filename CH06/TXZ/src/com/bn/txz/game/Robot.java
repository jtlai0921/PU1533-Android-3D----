package com.bn.txz.game;

import javax.microedition.khronos.opengles.GL10;
import com.bn.txz.MenuBodyPart;
import com.bn.txz.SelectBodyPart;
import com.bn.txz.SetBodyPart;
import com.bn.txz.TXZMenuView;
import com.bn.txz.TXZSelectView;
import com.bn.txz.TXZSetView;
import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;
import static com.bn.txz.game.GameStaticData.*;

public class Robot 
{
	TXZGameSurfaceView msv;
	//機器人的各個元件
	BodyPart bRoot,bBody,bHead,bLeftTop,bLeftBottom,bRightTop,bRightBottom;
	public BodyPart[] bpArray;
	//根關節位置
	float[] location;
	float positionx=0;//機器人的位置
	float positionz=0;//機器人的位置
	 int m=0;//機器人的行和列
	 int n=0;
	public static boolean RobotFlag=true;
	boolean isFirst=true;
	
	public Robot(VertexTextureNormal3DObjectForDraw[] lovntArray,TXZGameSurfaceView msv)
	{
		this.msv=msv;
		//0-bRoot
        //1-bBody
        //2-bHead
        //3-bLeftTop
		//4-bLeftBottom
        //5-bRightTop
		//6-bRightBottom
        bRoot=new BodyPart(0,0,0,null,0,msv);
        bBody=new BodyPart(0.005f,0.919f,-0.006f,lovntArray[0],1,msv);
        bHead=new BodyPart(0.005f,1.089f,-0.006f,lovntArray[1],2,msv);
        bLeftTop=new BodyPart(-0.298f,0.972f,0.035f,lovntArray[2],3,msv);
        bLeftBottom=new BodyPart(-0.315f,0.723f, 0.035f,lovntArray[3],4,msv);
        bRightTop=new BodyPart(0.319f,0.972f,0.035f,lovntArray[4],5,msv);
        bRightBottom=new BodyPart(0.34f,0.723f,0.035f,lovntArray[5],6,msv);
        
        bpArray=new BodyPart[]
		{
        		bRoot,bBody,bHead,bLeftTop,bLeftBottom,bRightTop,bRightBottom
		};
        
        bRoot.addChild(bBody);
        bBody.addChild(bHead);
        bBody.addChild(bLeftTop);
        bBody.addChild(bRightTop);
        bLeftTop.addChild(bLeftBottom);
        bRightTop.addChild(bRightBottom);
	}
	
	TXZMenuView menu;
	MenuBodyPart bRootl,bBodyl,bHeadl,bLeftTopl,bLeftBottoml,bRightTopl,bRightBottoml;
	public MenuBodyPart[] bpArrayl;
	public Robot(VertexTextureNormal3DObjectForDraw[] lovntArray,TXZMenuView menu)
	{
		//0-bRoot
        //1-bBody
        //2-bHead
        //3-bLeftTop
		//4-bLeftBottom
        //5-bRightTop
		//6-bRightBottom
		this.menu=menu;
		bRootl=new MenuBodyPart(0f,0f,0f,null,0,menu);
		bBodyl=new MenuBodyPart(-0.008f,4.678f,-0.049f,lovntArray[0],1,menu);
        bHeadl=new MenuBodyPart(-0.008f,5.84f,-0.049f,lovntArray[1],2,menu);
        bLeftTopl=new MenuBodyPart(-1.739f,5.136f,-0.049f,lovntArray[2],3,menu);
        bLeftBottoml=new MenuBodyPart(-1.861f,3.734f,-0.049f,lovntArray[3],4,menu);
        bRightTopl=new MenuBodyPart(1.763f,5.136f,-0.049f,lovntArray[4],5,menu);
        bRightBottoml=new MenuBodyPart(1.891f,3.734f,-0.049f,lovntArray[5],6,menu);

        
        bpArrayl=new MenuBodyPart[]
		{
        		bRootl,bBodyl,bHeadl,bLeftTopl,bLeftBottoml,bRightTopl,bRightBottoml
		};
        
        bRootl.addChild(bBodyl);
        bBodyl.addChild(bHeadl);
        bBodyl.addChild(bLeftTopl);
        bBodyl.addChild(bRightTopl);
        bLeftTopl.addChild(bLeftBottoml);
        bRightTopl.addChild(bRightBottoml);
	}

	TXZSetView set;
	SetBodyPart bRootSet,bBodySet,bHeadSet,bLeftTopSet,bLeftBottomSet,bRightTopSet,bRightBottomSet;
	public SetBodyPart[] bpArraySet;
	public Robot(VertexTextureNormal3DObjectForDraw[] lovntArray,TXZSetView set)
	{
		this.set=set;
		bRootSet=new SetBodyPart(0f,0f,0f,null,0,set);
		bBodySet=new SetBodyPart(-0.008f,4.657f,-0.002f,lovntArray[0],1,set);
		bHeadSet=new SetBodyPart(-0.008f,5.803f,-0.002f,lovntArray[1],2,set);
		bLeftTopSet=new SetBodyPart(-1.721f,5.114f,-0.002f,lovntArray[2],3,set);
		bLeftBottomSet=new SetBodyPart(-1.836f,3.754f,-0.002f,lovntArray[3],4,set);
		bRightTopSet=new SetBodyPart(1.732f,5.114f,-0.002f,lovntArray[4],5,set);
		bRightBottomSet=new SetBodyPart(1.856f,3.718f,-0.002f,lovntArray[5],6,set);
        
		bpArraySet=new SetBodyPart[]
		{
				bRootSet,bBodySet,bHeadSet,bLeftTopSet,bLeftBottomSet,bRightTopSet,bRightBottomSet
		};
        
		bRootSet.addChild(bBodySet);
		bBodySet.addChild(bHeadSet);
		bBodySet.addChild(bLeftTopSet);
		bBodySet.addChild(bRightTopSet);
		bLeftTopSet.addChild(bLeftBottomSet);
		bRightTopSet.addChild(bRightBottomSet);
	}
	
	TXZSelectView select;
	SelectBodyPart bRootSelect,bBodySelect,bHeadSelect,bLeftTopSelect,bLeftBottomSelect,bRightTopSelect,bRightBottomSelect;
	public SelectBodyPart[] bpArraySelect;
	public Robot(VertexTextureNormal3DObjectForDraw[] lovntArray,TXZSelectView select)
	{
		this.select=select;
		bRootSelect=new SelectBodyPart(0f,0f,0f,null,0,select);
		bBodySelect=new SelectBodyPart(-0.008f,4.657f,-0.002f,lovntArray[0],1,select);
		bHeadSelect=new SelectBodyPart(-0.008f,5.803f,-0.002f,lovntArray[1],2,select);
		bLeftTopSelect=new SelectBodyPart(-1.721f,5.114f,-0.002f,lovntArray[2],3,select);
		bLeftBottomSelect=new SelectBodyPart(-1.836f,3.754f,-0.002f,lovntArray[3],4,select);
		bRightTopSelect=new SelectBodyPart(1.732f,5.114f,-0.002f,lovntArray[4],5,select);
		bRightBottomSelect=new SelectBodyPart(1.856f,3.718f,-0.002f,lovntArray[5],6,select);
        
		bpArraySelect=new SelectBodyPart[]
		{
				bRootSelect,bBodySelect,bHeadSelect,bLeftTopSelect,bLeftBottomSelect,bRightTopSelect,bRightBottomSelect
		};
        
		bRootSelect.addChild(bBodySelect);
		bBodySelect.addChild(bHeadSelect);
		bBodySelect.addChild(bLeftTopSelect);
		bBodySelect.addChild(bRightTopSelect);
		bLeftTopSelect.addChild(bLeftBottomSelect);
		bRightTopSelect.addChild(bRightBottomSelect);
	}
	
	public void drawSelf(GL10 gl)
	{		
		//掃描晶體位置地圖的每個格子，若此格有晶體則繪制
		for(int i=0;i<msv.gqdDraw.MAP[GameData.level-1].length;i++) 
		{
			for(int j=0;j<msv.gqdDraw.MAP[GameData.level-1][0].length;j++)
			{
				if(msv.gqdDraw.MAP[GameData.level-1][i][j]==4||msv.gqdDraw.MAP[GameData.level-1][i][j]==6)
				{	
					m=i;
					n=j;
					positionx=n*UNIT_SIZE-(int)(msv.gqdDraw.MAP[GameData.level-1][0].length/2)*UNIT_SIZE+1f*UNIT_SIZE;
					positionz=m*UNIT_SIZE-(int)(msv.gqdDraw.MAP[GameData.level-1].length/2)*UNIT_SIZE+0.5f*UNIT_SIZE;
					
					gl.glPushMatrix();//保護現場
					//搬移機器人到此格對應的位置
					if(RobotFlag)
					{
						gl.glTranslatef(positionx, 
				    		0, 
				    		positionz);
						 //繪制機器人
					    bRoot.drawSelf(gl);
					}
					else if(isFirst)
					{
						 //繪制機器人
						gl.glTranslatef(positionx, 
					    		0, 
					    		positionz);
							 //繪制機器人
						    bRoot.drawSelf(gl);
					    isFirst=false;
					}
					bRoot.drawSelf(gl);
				    gl.glPopMatrix();//還原現場
				}
			}
		}
	}

	
	public void drawSelfAnother(GL10 gl)
	{		
		//從根元件開始繪制
		bRootl.drawSelf(gl);
	}
	
	public void drawSelfSet(GL10 gl)
	{		
		//從根元件開始繪制
		bRootSet.drawSelf(gl);
	}
	
	public void drawSelfSelect(GL10 gl)
	{		
		//從根元件開始繪制
		bRootSelect.drawSelf(gl);
	}
}
