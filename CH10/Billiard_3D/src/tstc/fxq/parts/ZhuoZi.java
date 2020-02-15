package tstc.fxq.parts;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import static tstc.fxq.constants.Constant.*;
public class ZhuoZi {

	ZhuoZiDiTable tableBottom;//��l��
	ZhuoMian tableRect;//�ୱ
	ZhuoZiDi tableRoundUD;//��l�W�U��t
	ZhuoZiDi tableRoundLR;//��l���k��t
	Circle circle;//�y�}
	
	TextureRectWithLight tableRoundRect;//��l���תO

	ZhuoBianOne middlePart;//�y�x���y�}����
	ZhuoBianTwo roundPart;//�y�x�|�Ө�
	public ZhuoZi(MySurfaceView mv)
	{
		tableBottom=new ZhuoZiDiTable(mv, BOTTOM_HIGHT,BOTTOM_LENGTH,BOTTOM_WIDE);//�إ߮ੳ
		tableRoundUD=new ZhuoZiDi
		(
				mv, 
				UP_DOWN_HIGHT,UP_DOWN_LENGTH,EDGE_BIG,
				new float[]//new float[vCount*2];//�إ߯��z�}�C
				{
					0,0,0,1,1,0,1,0,0,1,1,1,//����
					0.215f,0.75f,0.215f,0,0.5f,0.75f,0.5f,0.75f,0.215f,0,0.5f,0,//�᭱
					
					0f,0.77f,0.77f,1f,1f,0.77f,1f,0.77f,0.77f,1f,1f,1f,//�e��
					
					0.215f,0.75f,0.215f,0,0.5f,0.75f,0.5f,0.75f,0.215f,0,0.5f,0,//�U��
					0.215f,0.75f,0.215f,0,0.5f,0.75f,0.5f,0.75f,0.215f,0,0.5f,0,//����
					0.215f,0.75f,0.215f,0,0.5f,0.75f,0.5f,0.75f,0.215f,0,0.5f,0//�k��
				}		
		);//�إ߮�l�W�U�]�e��^��t
		tableRoundLR=new ZhuoZiDi
		(
				mv, 
				UP_DOWN_HIGHT,EDGE_BIG,LEFT_RIGHT_LENGTH,
				new float[]
		          {
						0,0,0,1,1,0,1,0,0,1,1,1,//����
						0.75f,0.422f,0.75f,0.156f,0,0.422f,0,0.422f,0.75f,0.156f,0,0.156f,//�᭱
						0.75f,0.422f,0.75f,0.156f,0,0.422f,0,0.422f,0.75f,0.156f,0,0.156f,//�e��
						0.75f,0.422f,0.75f,0.156f,0,0.422f,0,0.422f,0.75f,0.156f,0,0.156f,//�U��
						0.75f,0.422f,0.75f,0.156f,0,0.422f,0,0.422f,0.75f,0.156f,0,0.156f,//����
						
						0.77f,1f,//3
						0.77f,0f,//1
						1f,1f,//4

						1f,1f,//4
						0.77f,0f,//1
						1f,0f,//2
		          }
				
		);//�إ߮�l���k��t
		circle=new Circle(mv, CIRCLE_R,CIRCLE_R);//�إ߲y�}
		
		tableRect=new ZhuoMian//�إ߮ୱ
		(
				mv, 
				TABLE_AREA_LENGTH,TABLE_AREA_WIDTH
				
		);
		
		tableRoundRect=new TextureRectWithLight//��l���תO
		(
				mv, 
				EDGE,UP_DOWN_HIGHT,0,
				new float[]
		          {
						0,0,0,1,1,1,
						1,1,1,0,0,0
		          }
		);
		middlePart=new ZhuoBianOne(mv, 1);//�y�x���y�}����
		roundPart=new ZhuoBianTwo(mv, 1);//�y�x�|�Ө�
	}
	
	public void drawSelf(int tableBottomId,int tableRectId,int tableRoundUDId,
						int tableRoundLRId,int circleId,int poolPartOneId,int poolPartTwoId,int poolPartTwoJiaoId,int poolPartOneMId, int greenId)
	{
		//l.glTranslatef(0, 0, -10);
		MatrixState.pushMatrix();
		MatrixState.translate(0, BOTTOM_Y, 0);
		tableBottom.drawSelf(tableBottomId);//ø��y�ੳ
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();
		MatrixState.translate(0, TEXTURE_RECT_Y, 0);
		tableRect.drawSelf(tableRectId);//ø��ୱ
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//��l�W�U��t�A���W
		MatrixState.translate(UP_DOWN_LENGTH/2+MIDDLE/2, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE_BIG/2);
		tableRoundUD.drawSelf(tableRoundUDId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//��l�W�U��t�A�k�W
		MatrixState.translate(-UP_DOWN_LENGTH/2-MIDDLE/2, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE_BIG/2);
		tableRoundUD.drawSelf(tableRoundUDId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//��l���W�U��t�A���U
		MatrixState.translate(UP_DOWN_LENGTH/2+MIDDLE/2, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE_BIG/2);
		MatrixState.rotate(180, 0, 1, 0);
		tableRoundUD.drawSelf(tableRoundUDId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//��l�W�U��t�A�k�U
		MatrixState.translate(-UP_DOWN_LENGTH/2-MIDDLE/2, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE_BIG/2);
		MatrixState.rotate(180, 0, 1, 0);
		tableRoundUD.drawSelf(tableRoundUDId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//��l����t
		MatrixState.translate(-BOTTOM_LENGTH/2+EDGE_BIG/2, TEXTURE_RECT_Y, 0);
		tableRoundLR.drawSelf(tableRoundLRId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//��l�k��t
		MatrixState.translate(BOTTOM_LENGTH/2-EDGE_BIG/2, TEXTURE_RECT_Y, 0);
		MatrixState.rotate(180, 0, 1, 0);
		tableRoundLR.drawSelf(tableRoundLRId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(0, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE_BIG/2);
		middlePart.drawSelf( poolPartOneId,poolPartTwoId,poolPartOneMId, greenId);//�y�x���y�}����  �W��
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();
		MatrixState.translate(0, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE_BIG/2);
		MatrixState.rotate(180, 0, 1, 0);
		middlePart.drawSelf(poolPartOneId,poolPartTwoId,poolPartOneMId, greenId);//�y�x���y�}���� �U��
		MatrixState.popMatrix();
		
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(BOTTOM_LENGTH/2-EDGE/2, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE/2);
		roundPart.drawSelf( poolPartOneId,poolPartTwoId,poolPartTwoJiaoId);//�y�x�k�W��
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(BOTTOM_LENGTH/2-EDGE/2, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE/2);
		MatrixState.rotate(-90, 0, 1, 0);
		roundPart.drawSelf( poolPartOneId,poolPartTwoId,poolPartTwoJiaoId);//�y�x�k�U��
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(-BOTTOM_LENGTH/2+EDGE/2, TEXTURE_RECT_Y, -BOTTOM_WIDE/2+EDGE/2);
		MatrixState.rotate(90, 0, 1, 0);
		roundPart.drawSelf( poolPartOneId,poolPartTwoId,poolPartTwoJiaoId);//�y�x���W��
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix(); 
		MatrixState.translate(-BOTTOM_LENGTH/2+EDGE/2, TEXTURE_RECT_Y, BOTTOM_WIDE/2-EDGE/2);
		MatrixState.rotate(180, 0, 1, 0);
		roundPart.drawSelf( poolPartOneId,poolPartTwoId,poolPartTwoJiaoId);//�y�x���U��
		MatrixState.popMatrix();
		
		
		MatrixState.pushMatrix();//���U�}9
		MatrixState.translate(0, TEXTURE_RECT_Y+DELTA, BOTTOM_WIDE/2-CIRCLE_R/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//���W�}
		MatrixState.translate(0, TEXTURE_RECT_Y+DELTA, -BOTTOM_WIDE/2+CIRCLE_R/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//�k���U�}
		MatrixState.translate(TABLE_AREA_LENGTH/2, TEXTURE_RECT_Y+DELTA, TABLE_AREA_WIDTH/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//�k���W�}
		MatrixState.translate(TABLE_AREA_LENGTH/2, TEXTURE_RECT_Y+DELTA, -TABLE_AREA_WIDTH/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//�����U�}
		MatrixState.translate(-TABLE_AREA_LENGTH/2, TEXTURE_RECT_Y+DELTA, TABLE_AREA_WIDTH/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();//�����W�}
		MatrixState.translate(-TABLE_AREA_LENGTH/2, TEXTURE_RECT_Y+DELTA, -TABLE_AREA_WIDTH/2);
		circle.drawSelf(circleId);
		MatrixState.popMatrix();
	}
}
