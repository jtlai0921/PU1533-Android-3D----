package com.bn.box;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import com.bn.zxl.GameView;
public class Box2DUtil {

	//�إߤ��
	public static Rec creatRec(World world,float x,float y,float halfweight,float halfHeight,
			boolean isStatic ,GameView gameview,int textureId1
			
	)
	{
		PolygonDef recshap=new PolygonDef();//�إߦh���
		recshap.setAsBox(halfweight, halfHeight);//���w�Ϊ����x��
		if(isStatic)//�O�_�R�A
		{
			recshap.density=0;
		}
		else
		{
			recshap.density=1.0f;//�K��
		}
		recshap.friction=0.3f;//����
		recshap.restitution=0f;//�ϼu���v
		
		BodyDef bodydef=new BodyDef();//�إ�bodydef
		Vec2 v=new Vec2(x,y);
		bodydef.position.set(v);//���w���߮y��
		
		Body body=world.createBody(bodydef);//�b�@�ɤ��إ�body
		body.createShape(recshap);//���wshape
		body.setMassFromShapes();//�p���q
		
		return new Rec(body,gameview,2*halfweight,2*halfHeight,textureId1);//�Ǧ^MyRec
	}
	public static ChiLun createChiLun(World world,float x,float y,float radius,float deepth,boolean isStatic,GameView gameview)
	{
		CircleDef ballshapdef=new CircleDef();//�إ߶��
		if(isStatic)//�O�_�R�A
		{
			ballshapdef.density=0;//�K��0
		}
		else
		{
			ballshapdef.density=1.0f;//�K��
		}
		
		ballshapdef.friction=0.1f;//�����O
		ballshapdef.restitution=0;//�ϳg���v
		ballshapdef.radius=radius;//�b�|
		
		BodyDef bodydef=new BodyDef();//�_�l��body
		Vec2 v=new Vec2(x,y);//��߮y��
		bodydef.position.set(v);//�[�J
		
		Body body=world.createBody(bodydef);//�b�@�ɤ��إ�body
		body.createShape(ballshapdef);//���wshape
		
		//�]�F�@�U�y����t��
		body.setMassFromShapes();//�]�w��q
		return new ChiLun(body,radius,deepth,gameview);//�Ǧ^MyBall
	}
	//�إߤ��
	public static MuTong creatMuTong(World world,float x,float y,float halfWidth,float halfHeight,
			boolean isStatic ,GameView gameview
			
	)
	{
		PolygonDef recshap=new PolygonDef();//�إߦh���
		recshap.setAsBox(halfWidth, halfHeight);//���w�Ϊ����x��
		if(isStatic)//�O�_�R�A
		{
			recshap.density=0;
		}
		else
		{
			recshap.density=1.0f;//�K��
		}
		recshap.friction=0.3f;//����
		recshap.restitution=0f;//�ϼu���v
		
		BodyDef bodydef=new BodyDef();//�إ�bodydef
		Vec2 v=new Vec2(x,y);
		bodydef.position.set(v);//���w���߮y��
		
		Body body=world.createBody(bodydef);//�b�@�ɤ��إ�body
		body.createShape(recshap);//���wshape
		body.setMassFromShapes();//�p���q
		
		return new MuTong(body,halfHeight,gameview);//�Ǧ^MyRec
	}
	//�إ�Rain
	public static Rain creatRain(World world,float x,float y,float halfweight,float halfHeight,
			boolean isStatic ,GameView gameview
			
	)
	{
		PolygonDef recshap=new PolygonDef();//�إߦh���
		recshap.setAsBox(halfweight, halfHeight);//���w�Ϊ����x��
		if(isStatic)//�O�_�R�A
		{
			recshap.density=0;
		}
		else
		{
			recshap.density=1.0f;//�K��
		}
		recshap.friction=0.3f;//����
		recshap.restitution=0f;//�ϼu���v
		
		BodyDef bodydef=new BodyDef();//�إ�bodydef
		Vec2 v=new Vec2(x,y);
		bodydef.position.set(v);//���w���߮y��
		
		Body body=world.createBody(bodydef);//�b�@�ɤ��إ�body
		body.createShape(recshap);//���wshape
		body.setMassFromShapes();//�p���q
		
		return new Rain(body,gameview);//�Ǧ^MyRec
	}
	
	//�إߤp�y
	public static Ball createBall(World world,float x,float y,float radius,boolean isStatic,int textureId ,GameView gameview)
	{
		CircleDef ballshapdef=new CircleDef();//�إ߶��
		if(isStatic)//�O�_�R�A
		{
			ballshapdef.density=0;
		}
		else
		{
			ballshapdef.density=0.8f;//�K��
		}
		
		ballshapdef.friction=0.1f;//�����O
		ballshapdef.restitution=0;//�ϳg���v
		ballshapdef.radius=radius;//�b�|
		
		BodyDef bodydef=new BodyDef();//�_�l��body
		Vec2 v=new Vec2(x,y);//��߮y��
		bodydef.position.set(v);//�[�J
		
		Body body=world.createBody(bodydef);//�b�@�ɤ��إ�Body
		body.createShape(ballshapdef);//���wshape
		
		body.setMassFromShapes();//�]�w��q
		return new Ball(body,gameview,isStatic,textureId,radius);//�Ǧ^MyBall
	}
	public static ShiZiJia creatShizijia//�غc��
	(
		float x,float y,//���߮y��
		float width,//�@�Ӯ��������e
		float height,
		boolean isStatic,//�O�_�R�A
		World world,//�@��
		GameView gameview
	)
	{
		PolygonDef shape=new PolygonDef();//�إߦh���
		shape.setAsBox(width, height);//�h��ά��x��
		if(isStatic)//�O�_�R�A
		{
			shape.density=0;//�K��
		}
		else
		{
			shape.density=1.0f;//�K��
		}
		shape.friction=0;//����
		shape.restitution=0f;//�ϼu���v
		BodyDef bodyDef=new BodyDef();
		bodyDef.position.set(x,y);//���w�����m
		Body bodyTemp=world.createBody(bodyDef);//�b�@�ɤ��إ�Body
		bodyTemp.createShape(shape);//���wshape

			PolygonDef shape1=new PolygonDef();//�إ߲ĤG�Ӧh���
			shape1.setAsBox(height, width);//���鬰�x��
			if(isStatic)//�O�_���R�A
			{
				shape1.density=0;//�K��
			}
			else
			{
				shape1.density=1.0f;//�K��
			}
			shape1.friction=0;//����
			shape1.restitution=0f;//�ϼu���v
			bodyTemp.createShape(shape1);//�������[�J�ĤG�ӧΪ�

		bodyTemp.setMassFromShapes();//�p���q
		return new ShiZiJia(bodyTemp,gameview,width*2,height*2,isStatic);
	}
	//�إߦh��Ϊ���(�m��)
	public static Txing creatTxing//�غc��
	(  
		float x,//x�y��
		float y,//y�y��
	    float[][][] points,//�I�ǦC
        boolean isStatic,//�O�_���R�
        World world,//�@��
        GameView gameview,
        int id
    )
	{    
		//�إ߭���y�z����   
		BodyDef bodyDef = new BodyDef();   
		//�]�w��m
		bodyDef.position.set(x, y);  
		//�b�@�ɤ��إ߭���
		Body bodyTemp= world.createBody(bodyDef);
		//�إߦh��δy�z����
		PolygonDef shape = new PolygonDef();   
		//�]�w�K��
		if(isStatic)
		{
			shape.density = 0;
		}   
		else
		{
			shape.density = 1f;
		}   
		//�]�w�����t��
		shape.friction = 0.3f;   
		//�]�w��q�l���v�]�ϼu�^
		shape.restitution = 0f;   
		float point[][]=points[0];
		for(float[] fa:point)
		{
			shape.addVertex(new Vec2(fa[0],fa[1]));
		}
		//���w����Ϊ�
		bodyTemp.createShape(shape);   
		
		PolygonDef shape1 = new PolygonDef();   
		//�]�w�K��
		if(isStatic)
		{
			shape1.density = 0;
		}   
		else
		{
			shape1.density = 1f;
		}   
		//�]�w�����t��
		shape1.friction = 0.3f;   
		//�]�w��q�l���v�]�ϼu�^
		shape1.restitution = 0f;   
		float point1[][]=points[1];
		for(float[] fa:point1)
		{
			shape1.addVertex(new Vec2(fa[0],fa[1]));
		}
		//���w����Ϊ�
		bodyTemp.createShape(shape1); 
		
		bodyTemp.setMassFromShapes(); 
//		float width=point[2][0]-point[0][0];
//		float height=point[2][1]-point[0][1];
		return new Txing(bodyTemp,gameview);
	}   
}
