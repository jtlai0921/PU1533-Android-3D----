package tstc.fxq.parts;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import static tstc.fxq.constants.Constant.*;
public class ZhuoZi {

	ZhuoZiDiTable tableBottom;//桌子底
	ZhuoMian tableRect;//桌面
	ZhuoZiDi tableRoundUD;//桌子上下邊緣
	ZhuoZiDi tableRoundLR;//桌子左右邊緣
	Circle circle;//球洞
	
	TextureRectWithLight tableRoundRect;//桌子角擋板

	ZhuoBianOne middlePart;//球台中球洞部分
	ZhuoBianTwo roundPart;//球台四個角
	public ZhuoZi(MySurfaceView mv)
	{
		tableBottom=new ZhuoZiDiTable(mv, BOTTOM_HIGHT,BOTTOM_LENGTH,BOTTOM_WIDE);//建立桌底
		tableRoundUD=new ZhuoZiDi
		(
				mv, 
				UP_DOWN_HIGHT,UP_DOWN_LENGTH,EDGE_BIG,
				new float[]//new float[vCount*2];//建立紋理陣列
				{
					0,0,0,1,1,0,1,0,0,1,1,1,//頂面
					0.215f,0.75f,0.215f,0,0.5f,0.75f,0.5f,0.75f,0.215f,0,0.5f,0,//後面
					
					0f,0.77f,0.77f,1f,1f,0.77f,1f,0.77f,0.77f,1f,1f,1f,//前面
					
					0.215f,0.75f,0.215f,0,0.5f,0.75f,0.5f,0.75f,0.215f,0,0.5f,0,//下面
					0.215f,0.75f,0.215f,0,0.5f,0.75f,0.5f,0.75f,0.215f,0,0.5f,0,//左面
					0.215f,0.75f,0.215f,0,0.5f,0.75f,0.5f,0.75f,0.215f,0,0.5f,0//右面
				}		
		);//建立桌子上下（前後）邊緣
		tableRoundLR=new ZhuoZiDi
		(
				mv, 
				UP_DOWN_HIGHT,EDGE_BIG,LEFT_RIGHT_LENGTH,
				new float[]
		          {
						0,0,0,1,1,0,1,0,0,1,1,1,//頂面
						0.75f,0.422f,0.75f,0.156f,0,0.422f,0,0.422f,0.75f,0.156f,0,0.156f,//後面
						0.75f,0.422f,0.75f,0.156f,0,0.422f,0,0.422f,0.75f,0.156f,0,0.156f,//前面
						0.75f,0.422f,0.75f,0.156f,0,0.422f,0,0.422f,0.75f,0.156f,0,0.156f,//下面
						0.75f,0.422f,0.75f,0.156f,0,0.422f,0,0.422f,0.75f,0.156f,0,0.156f,//左面
						
						0.77f,1f,//3
						0.77f,0f,//1
						1f,1f,//4

						1f,1f,//4
						0.77f,0f,//1
						1f,0f,//2
		          }
				
		);//建立桌子左右邊緣
		circle=new Circle(mv, CIRCLE_R,CIRCLE_R);//建立球洞
		
		tableRect=new ZhuoMian//建立桌面
		(
				mv, 
				TABLE_AREA_LENGTH,TABLE_AREA_WIDTH
				
		);
		
		tableRoundRect=new TextureRectWithLight//桌子角擋板
		(
				mv, 
				EDGE,UP_DOWN_HIGHT,0,
				new float[]
		          {
						0,0,0,1,1,1,
						1,1,1,0,0,0
		          }
		);
		middlePart=new ZhuoBianOne(mv, 1);//球台中球洞部分
		roundPart=new ZhuoBianTwo(mv, 1);//球台四個角
	}
	
	public void drawSelf(int tableBottomId,int tableRectId,int tableRoundUDId,
						int tableRoundLRId,int circleId,int poolPartOneId,int poolPartTwoId,int poolPartTwoJiaoId,int poolPartOneMId, int greenId)
	{
		//l.glTranslatef(0, 0, -10);
		MatrixState.pushMatrix();
		MatrixState.translate(0, BOTTOM_Y, 0);
		tableBottom.drawSelf(tableBottomId);//繪制球桌底
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();
		MatrixState.translate(0, TEXTURE_RECT_Y, 0);
		tableRect.drawSelf(tableRectId);//繪制桌面
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//桌子上下邊緣，左上
		MatrixState.translate(UP_DOWN_LENGTH/2+MIDDLE/2, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE_BIG/2);
		tableRoundUD.drawSelf(tableRoundUDId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//桌子上下邊緣，右上
		MatrixState.translate(-UP_DOWN_LENGTH/2-MIDDLE/2, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE_BIG/2);
		tableRoundUD.drawSelf(tableRoundUDId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//桌子的上下邊緣，左下
		MatrixState.translate(UP_DOWN_LENGTH/2+MIDDLE/2, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE_BIG/2);
		MatrixState.rotate(180, 0, 1, 0);
		tableRoundUD.drawSelf(tableRoundUDId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//桌子上下邊緣，右下
		MatrixState.translate(-UP_DOWN_LENGTH/2-MIDDLE/2, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE_BIG/2);
		MatrixState.rotate(180, 0, 1, 0);
		tableRoundUD.drawSelf(tableRoundUDId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//桌子左邊緣
		MatrixState.translate(-BOTTOM_LENGTH/2+EDGE_BIG/2, TEXTURE_RECT_Y, 0);
		tableRoundLR.drawSelf(tableRoundLRId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//桌子右邊緣
		MatrixState.translate(BOTTOM_LENGTH/2-EDGE_BIG/2, TEXTURE_RECT_Y, 0);
		MatrixState.rotate(180, 0, 1, 0);
		tableRoundLR.drawSelf(tableRoundLRId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(0, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE_BIG/2);
		middlePart.drawSelf( poolPartOneId,poolPartTwoId,poolPartOneMId, greenId);//球台中球洞部分  上面
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();
		MatrixState.translate(0, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE_BIG/2);
		MatrixState.rotate(180, 0, 1, 0);
		middlePart.drawSelf(poolPartOneId,poolPartTwoId,poolPartOneMId, greenId);//球台中球洞部分 下面
		MatrixState.popMatrix();
		
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(BOTTOM_LENGTH/2-EDGE/2, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE/2);
		roundPart.drawSelf( poolPartOneId,poolPartTwoId,poolPartTwoJiaoId);//球台右上角
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(BOTTOM_LENGTH/2-EDGE/2, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE/2);
		MatrixState.rotate(-90, 0, 1, 0);
		roundPart.drawSelf( poolPartOneId,poolPartTwoId,poolPartTwoJiaoId);//球台右下角
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(-BOTTOM_LENGTH/2+EDGE/2, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE/2);
		MatrixState.rotate(90, 0, 1, 0);
		roundPart.drawSelf( poolPartOneId,poolPartTwoId,poolPartTwoJiaoId);//球台左上角
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(-BOTTOM_LENGTH/2+EDGE/2, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE/2);
		MatrixState.rotate(180, 0, 1, 0);
		roundPart.drawSelf( poolPartOneId,poolPartTwoId,poolPartTwoJiaoId);//球台左下角
		MatrixState.popMatrix();
		
		
		MatrixState.pushMatrix();//中下洞9
		MatrixState.translate(0, TEXTURE_RECT_Y+DELTA, BOTTOM_WIDE/2-CIRCLE_R/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//中上洞
		MatrixState.translate(0, TEXTURE_RECT_Y+DELTA, -BOTTOM_WIDE/2+CIRCLE_R/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//右側下洞
		MatrixState.translate(TABLE_AREA_LENGTH/2, TEXTURE_RECT_Y+DELTA, TABLE_AREA_WIDTH/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//右側上洞
		MatrixState.translate(TABLE_AREA_LENGTH/2, TEXTURE_RECT_Y+DELTA, -TABLE_AREA_WIDTH/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//左側下洞
		MatrixState.translate(-TABLE_AREA_LENGTH/2, TEXTURE_RECT_Y+DELTA, TABLE_AREA_WIDTH/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//左側上洞
		MatrixState.translate(-TABLE_AREA_LENGTH/2, TEXTURE_RECT_Y+DELTA, -TABLE_AREA_WIDTH/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
	}
}
