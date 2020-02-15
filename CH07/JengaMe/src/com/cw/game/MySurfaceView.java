package com.cw.game;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector3f;
 
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.cw.util.IntersectantUtil;
import com.cw.util.MatrixState;
import com.cw.util.SQLiteUtil;
import com.cw.view.EndMenu;


class MySurfaceView extends GLSurfaceView implements SurfaceHolder.Callback 
{
	SceneRenderer mRenderer;//�����ۦ⾹
	//int currLayer=-1;
	//static int currindex=0;
	//=============�B��============
	//�����Z��Y�b���Z��
    final float LIGHT_Y=70;
    public  float ratio;
    float mPreviousX;//�W����Ĳ����mX�y��
    float mPreviousY;
    float preNanoTime;
    private int load_step=0;	
	boolean isLoadedOk=false;
    boolean simulateFlag=true;
    boolean timefalg=true;
    //�O�_�h�����ЧӦ�
    boolean isMoveFlag=false;
    float cx=0;	//��v����mx
    float cy=1;  
    float cz=6;   //��v����mz
    float tx=0;   //��v���ت��Ix
    float ty=0;   //��v���ت��Iz
    float tz=-10;   //��v���ت��Iz
    int dx=0;
    int dy=1;
    int dz=0;
    //�x�s��_�����v����ʪ�����
    double upRadians=0;
    float left;
    float right;
	float top;
	float bottom;
	float near;
	float far;
	 ArrayList<MyCube> cubegroup=new ArrayList<MyCube>();
	static int checkedIndex=-1;
	int precheckindex=-1;
	DiscreteDynamicsWorld dynamicsWorld;//�@�ɪ���
	CollisionShape planeShape;//�@�Ϊ������Ϊ�
	BoxShape boxShape1;
	BoxShape boxShape2;
	JengaMeActivity activity;
	static int maxLayer=18;//�������̲ױo�� 			�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P		 �w�d		 �P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P
	static int index3=53;
	boolean touchpoint=false;//��{Ĳ���I
	float currX=-12;
	float currY=-12;
	static int timecount=0;
	float tpx=0;
	float tpy=0;
	float tpz=0;
	Vector3f localInertia = new Vector3f(0, 0, 0);//�D�ʦV�q
	CollisionShape colShape;
	float x;
	float y;
	int xt=0;
	int yt=0;
	int i=0;
	float Radius=Constant.EYE_Z;
    long previousTime;//�W�����U���ɶ�
    float xAngle=0;
    float xAngleV=0;
    float delatY=0;
    float yAngle=90;
	boolean camerarun=true;
	boolean scalsable=false;
	static boolean changeSelectState=true;
	int touchCase=-1;
	HashMap<Integer,BNPoint> hm=new HashMap<Integer,BNPoint>();
	float distance=0;//�D���I�Z��
	int tmpIndex=-1;
	int xAfh=1;
	float heightY=0;
	boolean playmusic=true;
	int[] cubeTextureId=new int[2];//�c�l�����z
	int []numberTexture=new int[10];
	int timetex;
	int scoretex;
	int maohaotex;
	int floorTextureId;//�a�����z
	int front;
	int le;
	int back;
	int ri;
	int touc;
	int processBeijing;
	int processId;
	TexFloor floor;//���z�x��1
	TextureRect sky;//���z�x��1
	TextureRect scorebord;
	TextureRect pic;
	TextureRect touchpp;
	TextureRect loadingView;
	TextureRect processBar;
	Handler handler = new Handler();
	int backtoorigion=6;
	public MySurfaceView(Context context) 
	{
        super(context);
        this.activity=(JengaMeActivity) context;
        this.setEGLContextClientVersion(2);
        Radius=Constant.EYE_Z;
        //�_�l�ƹ���@��
        initWorld();        
        mRenderer = new SceneRenderer();	//�إ߳����ۦ⾹
        setRenderer(mRenderer);				//�]�w�ۦ⾹		
        playmusic= activity.sp.getBoolean("playeffect", false);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//�]�w�ۦ�Ҧ����D�ʵۦ�   
    }
	//�_�l�ƹ���@�ɪ���k
	public void initWorld()
	{
		//�إ߸I������պA�T������
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();		
		//�إ߸I�������k���t�̪���A��\�ର���y�Ҧ����I�������A�ýT�w�A�Ϊ����給����������k
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);		
		//�]�w��ӹ���@�ɪ���ɰT��
		Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
		Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
		int maxProxies = 1024;
		//�إ߸I������ʴ����q���[�t��k����
		AxisSweep3 overlappingPairCache =new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		//�إ߱��ʬ����ѨM�̪���
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		//�إ߹���@�ɪ���
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration);
		//�]�w���O�[�t��
		dynamicsWorld.setGravity(new Vector3f(0, -20, 0));
		boxShape1=new BoxShape(new Vector3f(1*Constant.UNIT_SIZE,Constant.UNIT_SIZE/2,3*Constant.UNIT_SIZE));
		boxShape2=new BoxShape(new Vector3f(3*Constant.UNIT_SIZE,Constant.UNIT_SIZE/2,1*Constant.UNIT_SIZE));
		planeShape=new StaticPlaneShape(new Vector3f(0, 1, 0), 0);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		x=event.getX();
		y=event.getY();
		
		float x = event.getX();//���oĲ���IX�y��
    	float y = event.getY();
    	float delx= x-mPreviousX;//�p��X�VĲ���첾 
    	float dely=y-mPreviousY;
        long currTime=System.currentTimeMillis();//���o�ثe�ɶ��W
        long timeSpan=(currTime-previousTime)/7;//�p��⦸Ĳ���ƥ󤧶����ɶ��t
    	int action=event.getAction()&MotionEvent.ACTION_MASK;
		int id=(event.getAction()&MotionEvent.ACTION_POINTER_ID_MASK)>>>MotionEvent.ACTION_POINTER_ID_SHIFT;	
		
		switch(action)
		{
		case MotionEvent.ACTION_DOWN: 
		case MotionEvent.ACTION_POINTER_DOWN: 
			scalsable=true;
				hm.put(id, new BNPoint(event.getX(id), event.getY(id)));
			if (hm.size() == 2) {
				BNPoint bpTempA = hm.get(0);
				BNPoint bpTempB = hm.get(1);
				if(bpTempA!=null&& bpTempB!=null)
				{
					distance = BNPoint.calDistance(bpTempA, bpTempB);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE: 
			Set<Integer> ks = hm.keySet();
			for (int i : ks) {
				try
				{
					hm.get(i).setLocation(event.getX(i), event.getY(i));
				}catch(Exception e)
				{
				}
			}
			if (hm.size() == 2) {
				BNPoint bpTempA = hm.get(0);
				BNPoint bpTempB = hm.get(1);
				//�p��Ĳ���I�Z���ô��⬰�Y��t��&&!isMoveFlag
				if(bpTempA!=null&& bpTempB!=null){
				float currDis = BNPoint.calDistance(bpTempA, bpTempB);
				if ((currDis-distance>4||currDis-distance<-4)) {
					//isMoveFlag=false;
						Radius-=(currDis-distance)/30;//*2
						if(Radius>24)
			        	{
			        		Radius=24f;
			        	}
			        	if(Radius<8)
			        	{
			        		Radius=8f;
			        	}
				}
				distance = currDis;
				}
			}
			scalsable=false;
			break;
		case MotionEvent.ACTION_UP: 
			hm.clear();
			break;
		case MotionEvent.ACTION_POINTER_UP: 
			hm.remove(id);
			break;
		} 
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				currX=x;
				currY=y;
				touchpoint=true;
				mPreviousX=event.getX();
				mPreviousY=event.getY();
				preNanoTime=System.nanoTime();
				tmpIndex=-1;
			    isMoveFlag=false;
    			float[] AB=IntersectantUtil.calculateABPosition
    			(
    				x, //Ĳ���IX�y��
    				y, //Ĳ���IY�y��
    				JengaMeActivity.screenWidth, //�ù��e��
    				JengaMeActivity.screenHeight, //�ù�����
    				left, //����left�Btop��
    				top,
    				near, //����near�Bfar��
    				far
    			);
    			//�g�uAB
    			MyVector3f start = new MyVector3f(AB[0], AB[1], AB[2]);//�_�I
    			MyVector3f end = new MyVector3f(AB[3], AB[4], AB[5]);//���I
    			MyVector3f dir = end.minus(start);//���שM��V
        		
        		float minTime=1;//�O���M�椤�Ҧ�����PAB�ۥ檺�̵u�ɶ�
        		for(int i=0;i<cubegroup.size();i++){//�ˬd�M�椤������ 
        			AABB3 box = cubegroup.get(i).getCurrBox(); //��o����AABB�]��   
    				float t = box.rayIntersect(start, dir, null);//�p��ۥ�ɶ�
        			if (t <= minTime) {
    					minTime = t;//�O���̤p��
    					tmpIndex = i;//�O������Ĳ�N�������&&cubegroup.get(i).touchable
    				}
        		}
        		//���F�ѨM��QĲ�������󳻺ݮɪ� �ƥ�
        		if(!changeSelectState)//&&�Y�G���A��󤣥i���ܪ��A������U  ���ɪ֩w���Q�Ŀ諸������
        		{
        			if(tmpIndex!=-1&&tmpIndex==checkedIndex&&cubegroup.get(checkedIndex).upflag==1)//�Y�G����Ĳ�N��F�w�g�Ŀ諸��󳻺ݪ�����
        			{
        				touchCase=0;
        				MyCube lovo = cubegroup.get(checkedIndex);
        				lovo.touchable=true;
        				Transform startTransform = new Transform();//�إ߭��骺�_�l�ܴ�����
						startTransform.setIdentity();//�ܴ��_�l��
						startTransform.origin.set(new Vector3f(tpx, tpy, tpz));//�]�w�_�l����m
						//�إ߭��骺�B�ʪ��A����
						DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
						//�إ߭���T������
						if(lovo.yAngle==90)
						{
							colShape=boxShape1;
						}else
						{
							colShape=boxShape2;
						}
						RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo
								(1, myMotionState, colShape, localInertia);//�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P����j�p�ݩw�ڦ�m�өw
						RigidBody body1;//���������骫��
						body1 = new RigidBody(rbInfo);//�إ߭���
						body1.setRestitution(0.000f);//�]�w�ϼu�t��
						body1.setFriction(0.3f);//�]�w�����t��
						if(lovo.body!=null)//�T�O�ª��责�ܳQ�P����
						{
							dynamicsWorld.removeRigidBody(lovo.body);
							
						}
						dynamicsWorld.addRigidBody(body1);//�N����[�J�i����@��
						lovo.body=body1;
        			    camerarun=false;
        			    cubegroup.get(tmpIndex).isPicked=true;
    					cubegroup.get(tmpIndex).body.activate();
        				cubegroup.get(tmpIndex).addPickedConstraint();
        			}
        			else//�������󳻺ݮ� ��Ĳ�N����N�i����۾�
        			{
        				touchCase=1;
        				camerarun=true;
        			}
        		}
        		else//�b���A�i�ܧ󪺱��p�U ���ʧ@ �{���X �b��Ԥ�������p 
        		{
        		 if(tmpIndex==-1)//�Y�G�����S���N�쪫��  �N�i�H�����v��
	        		{
	        			if(checkedIndex==-1)
	        			{
	        				touchCase =6;
	        				camerarun=true;		//�Y�G�⦸Ĳ�N���O �S���Ŀ磌�� �N�ȶ�����v��
	        			}else 					
	        			{	//�Y�G�Ĥ@��Ĳ�N�Ŀ�F����  ���O�����S���N������ �h�N�Q�Ŀ諸����]���� checkedindex=-1
	        				camerarun=true;
	        				touchCase=2;
	        			}
	        		}else//����Ĳ�N �N��F���
	        		{
		        		if(checkedIndex==-1)//��S������Q�Ŀ�� �N �N�ثe��Ĳ�����A��s �n�D�S���o�ͷư�
		        		{
		        				touchCase=3;
		        				camerarun=true;
		        		}
		        		else 
		        		{
		        			if(tmpIndex!=checkedIndex)//�Y�G�ثe�I��������M�Ŀ������@�� �N�󴫳Q�Ŀ諸
		        			{
		        					touchCase=4;
		        					camerarun=true;
 
			        		}else//�Y�G �ثe�I���M�H�Q�Ŀ諸�O�P�@��  �N�Ұ� ������ �ù��i��ʧ@�ܴ�
			        		{
	        					camerarun=false;
	        					touchCase=5;
			        		}
		        		}
	        	     }
        		}
			break;
			case MotionEvent.ACTION_MOVE:
				if(x-mPreviousX>=10.0f||x-mPreviousX<=-10.0f)
				{
					isMoveFlag=true;
					xt=1;
				}
				if(y-mPreviousY>=10.0f||y-mPreviousY<=-10.0)
				{
					isMoveFlag=true;
					yt=1;
				}
				if(isMoveFlag)
				{
					if(checkedIndex!=-1&&cubegroup.get(checkedIndex).touchable&&(touchCase==5))
					{
						camerarun=false;
						MyCube lovo = cubegroup.get(checkedIndex);
						float[] nearXY=IntersectantUtil.calculateABPosition(
								x,y,
								JengaMeActivity.screenWidth,JengaMeActivity.screenHeight,
								left,top,
								near,far
								);
						float[] nearPreXY = IntersectantUtil.calculateABPosition(
								mPreviousX,mPreviousY,
								JengaMeActivity.screenWidth,JengaMeActivity.screenHeight,
								left,top,
								near,far
								);
						if(lovo.p2p!=null)
						{
						Vector3f currPivot = lovo.p2p.getPivotInB(new Vector3f());
						Vector3f  dir1 = new Vector3f(nearXY[0]-nearPreXY[0],nearXY[1]-nearPreXY[1],nearXY[2]-nearPreXY[2]);
						float vFactor =8f;
						if(lovo.layer!=maxLayer-1||lovo.layer!=maxLayer-2)
						{
							int fh=1;//�Ny�b���ܤƶq���z�b�����ӥu���[����� �A�Ÿ��ѭ�Ӫ�Z�b�T�w
							if(Math.round(dir1.z)!=0)
							{
								fh=Math.round(dir1.z)/Math.abs(Math.round(dir1.z));
							}
							if(cz>=0.707*Radius)//�G�����ڸ�2���b�|�� �ھڬ۾�����m�ڳ���Ԥ�V�����P
							{//�e��
								dir1.set(dir1.x*vFactor,0,-fh*dir1.y*vFactor);
							}
							else if(cz<=-0.707*Radius)
							{//�᭱
								dir1.set(dir1.x*vFactor,0,fh*dir1.y*vFactor);
							}
							else if(cx>-0.707*Radius)
							{//�k��
								dir1.set(-fh*dir1.y*vFactor,0,-xAfh*dir1.x*vFactor);
							}else if(cx<=-0.707*Radius)
							{//���� 
								dir1.set(fh*dir1.y*vFactor,0,xAfh*dir1.x*vFactor);
							}
						currPivot.add(dir1);
						lovo.p2p.setPivotB(currPivot);
						}
						}
					}
					if(touchCase==0)
					{
						MyCube lovo = cubegroup.get(tmpIndex);
						float[] nearXY=IntersectantUtil.calculateABPosition(
								x,y,
								JengaMeActivity.screenWidth,JengaMeActivity.screenHeight,
								left,top,
								near,far
								);
						float[] nearPreXY = IntersectantUtil.calculateABPosition(
								mPreviousX,mPreviousY,
								JengaMeActivity.screenWidth,JengaMeActivity.screenHeight,
								left,top,
								near,far
								);
						if(lovo.p2p!=null)
						{
							Vector3f currPivot = lovo.p2p.getPivotInB(new Vector3f());
							Vector3f dir1 = new Vector3f(nearXY[0]-nearPreXY[0],nearXY[1]-nearPreXY[1],nearXY[2]-nearPreXY[2]);
							float vFactor =4f;
							dir1.set(dir1.x*vFactor,dir1.y*vFactor,dir1.z*vFactor);
							heightY=dir1.y;
							currPivot.add(dir1);
							lovo.p2p.setPivotB(currPivot);
						}
					}
					//��v�������ʵ{���X�P
					if((camerarun||checkedIndex==-1))
					{//�Y�G�⦸�� Ĳ�����A�@�I ���򧹥��i�H�h���۾�  �Y�G ��̬۵� ���O����-1 ���� �]�i�HĲ��
					 	 delatY+=(dely)/60;
					  	if(delatY<0.02)
					 	{
					 		delatY=0.02f;
					 	}
			            if(timeSpan!=0)
			            {
			        			yAngle=(yAngle+360)%360;
			            		xAngleV=(int)((delx)/timeSpan);
			            		xAngle+=xAngleV/60;
			            		if(Math.toDegrees(xAngle)<=90||Math.toDegrees(xAngle)>=270)
			            		{
			            			xAfh=1;
			            		}else{
			            			xAfh=-1;
			            		}
			            }
					 	if(delatY>9)
					 	{
					 		if(!changeSelectState)
					 		{
			            			   cx=0;//,//�H����m��XEYE_X
					 					cy=(maxLayer+1);//,//�H����m��YRadius*(float)Math.cos(xAngle)/3
					 					cz= 0;//,  //�H����m��z
					 					tx= 0;//, 	//�H�����ݪ��IX
					 					ty= 0;//, //�H�����ݪ��IY
					 					tz= 0;//,//�H�����ݪ��IZ
					 					dx=0;
			                   			dy=0; 
			                   			dz=1;
					 		  MatrixState.copyMVMatrix();
					 		}
					 		delatY=9f;
					 	}
					 	else
					 	{
					 		if(xt==1)
					 		{
					 			cx= Constant.TARGET_X+Radius*(float)Math.sin(-xAngle);
					 			cz=  Constant.TARGET_Z+Radius*(float)Math.cos(-xAngle);
					 			tx= Constant.TARGET_X;	//�H�����ݪ��IX
					 			tz= Constant.TARGET_Z;//�H�����ݪ��IZ
					 		}
					 		if(yt==1)
					 		{
				 				cy= Constant.EYE_Y+delatY;//�H����m��YRadius*(float)Math.cos(xAngle)/3
				 				ty= Constant.EYE_Y+delatY;//�H�����ݪ��IY
					 		}
		 					dx=0;
                   			dy=1;
                   			dz=0;
					 		MatrixState.copyMVMatrix();
					 	}
					}
		         }
			break;
			case MotionEvent.ACTION_UP:
				touchpoint=false;
				switch(touchCase)
				{
				case 0://�Y�G����Ĳ�N��F�w�g�Ŀ諸��󳻺ݪ�����
					break;
				case 1://�������󳻺ݮ� ��Ĳ�N����N�i����۾�
					break;
				case 2://�Y�G�Ĥ@��Ĳ�N�Ŀ�F����  ���O�����S���N������ �h�N�Q�Ŀ諸����]���� checkedindex=-1
					if(!isMoveFlag&&cubegroup.get(checkedIndex).upflag==0)//�Y�G�����S���h��
					{
						cubegroup.get(checkedIndex).isPicked=false;//�Хܬ����Q�Ŀ�
	    				cubegroup.get(checkedIndex).removePickedConstraint();
	    				checkedIndex=-1;
					}
					break;
				case 3://��S������Q�Ŀ�� �N �N�ثe��Ĳ�����A��s �n�D�S���o�ͷư�
					if(!isMoveFlag)//�Y�G�S���h��
					{
						checkedIndex=tmpIndex;
			       		cubegroup.get(checkedIndex).isPicked=true;//�Хܬ��Q�Ŀ�
					}
					break;
				case 4://�Y�G�ثe�I��������M�Ŀ������@�� �N�󴫳Q�Ŀ諸
					if(!isMoveFlag)
					{
						cubegroup.get(checkedIndex).removePickedConstraint();
	    				cubegroup.get(checkedIndex).isPicked=false;
	    				checkedIndex=tmpIndex;
	    				cubegroup.get(checkedIndex).isPicked=true;
					}
					break;
				case 5://�Y�G �ثe�I���M�H�Q�Ŀ諸�O�P�@��  �N�Ұ� ������ �ù��i��ʧ@�ܴ�
					cubegroup.get(checkedIndex).isPicked=true;
					cubegroup.get(checkedIndex).body.activate();
    				cubegroup.get(checkedIndex).addPickedConstraint();
    				break;
				}
				if(checkedIndex!=-1&&(touchCase==0)){//touchCase==5|||touchCase==7���B�����󳻺ݪ�����i��Ĳ���᪺�B�z
					MyCube lovo = cubegroup.get(checkedIndex);
					if(lovo.upflag==1)//&&tmpIndex!=-1&&tmpIndex==checkedIndex�Q�Ŀ諸���� �b����lovo.upflag==1
					{
						if(!isMoveFlag)
						{
							lovo.upflag=0;
							lovo.isPicked=false;
							MySurfaceView.checkedIndex=-1;
							MyCube.flag++;
							if(MyCube.flag%3==0&&MyCube.flag!=0)
				    		{
								MySurfaceView.maxLayer++;
				    			MySurfaceView.index3=lovo.index;//�O���ثe������¦V  �H�K����U�@�����
				    		}
						}else//�ưʨåB��m�B�󦳮İϰ줺
						{
							if(lovo.p2p!=null)
							{
								lovo.removePickedConstraint();
							}
							if(lovo.trans.origin.y<=(MySurfaceView.maxLayer+4)/2-0.01)
							{
								MyCube.flag++;
								lovo.isPicked=false;
								MySurfaceView.checkedIndex=-1;
								if(MyCube.flag%3==0&&MyCube.flag!=0)
					    		{
									MySurfaceView.maxLayer++;
					    			MySurfaceView.index3=lovo.index;//�O���ثe������¦V  �H�K����U�@�����
					    		}
								lovo.upflag=0;
							}
						}
						lovo.upthere=false;
					}
				}
				isMoveFlag=false;
				touchCase=-1;
				currX=-12;
				currY=-12;
				camerarun=true;
			break;
		}
		   mPreviousX = x;//�O��Ĳ������m
		   mPreviousY=y;
	       previousTime=currTime;//�O�������ɶ��W
		return true;
	}
	
	class SceneRenderer implements GLSurfaceView.Renderer 
    {
		private boolean isFirstFrame=true;
        public void onDrawFrame(GL10 gl) {
        	//�M���`�׽w�R�P�m��w�R
          GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
         if(!isLoadedOk)//�p�L�S�����J����
          {
          	  drawOrthLoadingView();
          }
         else
          {
           	 drawGameView(gl);
          }
     	if(backtoorigion>=18)
    	{
     		
    		SQLiteUtil.insertTime(MySurfaceView.maxLayer);
			EndMenu.cdraw=true;
			simulateFlag=false;
			timefalg=false;
			MyCube.flag=0;
			MyCube.countD=0;
			MySurfaceView.timecount=0;
			activity.hd.sendEmptyMessage(3);
			MySurfaceView.changeSelectState=true;
			MySurfaceView.checkedIndex=-1;
			MySurfaceView.index3=53;
			MySurfaceView.maxLayer=18;
			Radius=Constant.EYE_Z;
			backtoorigion=6;
    	}
        }
        public void drawGameView(GL10 gl)
		{
          
        	GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        	MatrixState.pushMatrix();
        	MatrixState.setProjectFrustum(-left, right, -bottom, top, near, far);
        	MatrixState.setCamera( //��¶�T�w�b���Y�@�����y��i�����
        			  		cx,cy,cz,tx,
         			   		ty,tz,dx,dy,dz);
            MatrixState.copyMVMatrix();
            
            //ø��a�O
            MatrixState.pushMatrix();
            floor.drawSelf( floorTextureId);
            
            MatrixState.popMatrix();
            gl.glDisable(GL10.GL_DEPTH_TEST); 
		    gl.glBlendFunc( GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA); //�H�U�O���F�ҰʲV�X�\�� GL10.GL_ONE_MINUS_SRC_ALPHA
		    gl.glEnable(GL10.GL_BLEND);
            synchronized(cubegroup)
			{
	            for(MyCube tc:cubegroup)
	            {
	            	MatrixState.pushMatrix();
	            	tc.drawSelf(MySurfaceView.this,cubeTextureId,1);
	                MatrixState.popMatrix(); 
	            }
			}
            gl.glDisable(GL10.GL_BLEND); 
			gl.glEnable(GL10.GL_DEPTH_TEST);
            //ø��c�l
            synchronized(cubegroup)
			{
	            for(MyCube tc:cubegroup)
	            {
	            	MatrixState.pushMatrix();
			    	tc.drawSelf(MySurfaceView.this,cubeTextureId,0); //�e�v�l,0
	                MatrixState.popMatrix(); 
	            }
			}
            MatrixState.pushMatrix();
            MatrixState.translate(0,12,-50);
            sky.drawSelf(front);
            MatrixState.popMatrix();
            
            //ø��ѪŲ��e��
            MatrixState.pushMatrix();
            MatrixState.translate(0,12, 50);
            MatrixState.rotate(180, 0, 1, 0);
            sky.drawSelf(back);
            MatrixState.popMatrix(); 
            //ø��ѪŲ�����
            MatrixState.pushMatrix();
            MatrixState.translate(-50, 12, 0);
            MatrixState.rotate(90, 0, 1, 0);
            sky.drawSelf(ri);
            MatrixState.popMatrix(); 
            //ø��ѪŲ��k��
            MatrixState.pushMatrix();
            MatrixState.translate(50, 12, 0);
            MatrixState.rotate(-90, 0, 1, 0);
            sky.drawSelf(le);
            MatrixState.popMatrix();
            
            MatrixState.popMatrix();
            MatrixState.pushMatrix();
       
            drawScore(gl);
            MatrixState.popMatrix();
		} 
        
        public void drawScore(GL10 gl)
        {
		        int gW=maxLayer%10;
		        int sW=maxLayer/10;
		        int second=timecount%60;
		        int minute=timecount/60;
		        gl.glDisable(GL10.GL_DEPTH_TEST); 
	            gl.glBlendFunc( GL10.GL_ONE,GL10.GL_ONE_MINUS_SRC_ALPHA); //�H�U�O���F�ҰʲV�X�\��
	            gl.glEnable(GL10.GL_BLEND);
		        MatrixState.pushMatrix();
		        MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//�]�w�����v
		        MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//�]�w��v��
		        MatrixState.copyMVMatrix();
		    	if(touchpoint)
	        	{
	        		 MatrixState.pushMatrix();
	        		 MatrixState.translate(-(JengaMeActivity.scaleratiox*270-currX)/(JengaMeActivity.scaleratiox*270),(JengaMeActivity.scaleratioY*480-currY)/(480*JengaMeActivity.scaleratioY),0);
	        		 touchpp.drawSelf(touc);
	                 MatrixState.popMatrix();
	        	}
		        MatrixState.pushMatrix();
		        MatrixState.translate(-0.7f, 0.88f, 0);
		        pic.drawSelf(scoretex);
		        MatrixState.translate(0.35f, 0.02f, 0);
		        scorebord.drawSelf(numberTexture[sW]);
		        MatrixState.translate(0.1f, 0f, 0);
		        scorebord.drawSelf(numberTexture[gW]);
		        MatrixState.popMatrix();
		        MatrixState.pushMatrix();
		        MatrixState.translate(0.95f, 0.9f, 0);
		        scorebord.drawSelf(numberTexture[second%10]);
		        
		         MatrixState.translate(-0.1f, 0, 0);
		        scorebord.drawSelf(numberTexture[second/10]);
		        
		        MatrixState.translate(-0.1f, -0.02f, 0);
		        scorebord.drawSelf(maohaotex);
		        MatrixState.translate(-0.1f, 0.02f, 0);
		        scorebord.drawSelf(numberTexture[minute%10]);
		        
		        MatrixState.translate(-0.1f, 0, 0);
		        scorebord.drawSelf(numberTexture[minute/10]);
		        
		        MatrixState.translate(-0.4f,-0.02f, 0);
		        pic.drawSelf(timetex);
		        MatrixState.popMatrix();
		        MatrixState.popMatrix();
		       
		        gl.glDisable(GL10.GL_BLEND); 
		        gl.glEnable(GL10.GL_DEPTH_TEST);
        }
      
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�]�w�����j�p�Φ�m 
        	GLES20.glViewport(0, 0, width, height);
            //�p��z����v�����  
            	 ratio = (float) width/height;
                 left=right=ratio;
                 top=bottom=1;
                 near=2;
                 far=100;
                 MatrixState.setProjectFrustum(-left, right, -bottom, top, near, far);
        }
        
        public void initTextureneeded()
        {
        	   floorTextureId=initTextureRepeat(R.drawable.f6);
               front=initTexture(R.drawable.front); 
               le=initTexture(R.drawable.left);
               ri=initTexture(R.drawable.right);
               back=initTexture(R.drawable.back1); 
               
               touc=initTexture(R.drawable.touch);
               cubeTextureId[0]=initTexture(R.drawable.cube1);
               cubeTextureId[1]=initTexture(R.drawable.cube);
               numberTexture[0]=initTexture(R.drawable.d0);
               
               
               numberTexture[1]=initTexture(R.drawable.d1);
               numberTexture[2]=initTexture(R.drawable.d2);
               numberTexture[3]=initTexture(R.drawable.d3);
               numberTexture[4]=initTexture(R.drawable.d4);
               
               numberTexture[5]=initTexture(R.drawable.d5);
               numberTexture[6]=initTexture(R.drawable.d6);
               numberTexture[7]=initTexture(R.drawable.d7);
               numberTexture[8]=initTexture(R.drawable.d8);
               
               numberTexture[9]=initTexture(R.drawable.d9);
               timetex=initTexture(R.drawable.time);
               scoretex=initTexture(R.drawable.score);
               maohaotex=initTexture(R.drawable.maohao);
        }
        
        public void onSurfaceCreated(GL10 gl, EGLConfig config) { 
        	//�]�w�ù��I����RGBA
            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);             
            //�}�Ҳ`������
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //�}�ҭI���ŵ�   
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //�_�l���ܴ��x�}
            MatrixState.setInitStack(); 
            MatrixState.copyMVMatrix();
            MatrixState.setLightLocation(16,30, 12); 
            ShaderManager.loadCodeFromFile(activity.getResources());
            ShaderManager.compileShader();
            load_step=0;//���J�귽���B��
            isLoadedOk=false;
            isFirstFrame=true;
            processBeijing=initTexture(R.drawable.backg);
            processId=initTexture(R.drawable.process);
            loadingView=new TextureRect(ShaderManager.getTextureShaderProgram(),MySurfaceView.this,1f,1f,0f);
            processBar=new TextureRect(ShaderManager.getTextureShaderProgram(),MySurfaceView.this,1f,0.03f,0f);
            new  Thread()
            {
            	@Override 
            	public void run()
            	{
            		while(timefalg&&backtoorigion<18)
            		{  
            		if(MyCube.countD>3)//���T�Ӥ����y��V�����W�L0.2�N���}�{���C�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�w�d�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P�P
                    {
               		 MyCube.turnnable=true;
               		 showToast(); 
	                         try{
	                    	    	backtoorigion++;
	                    	    	Thread.sleep(100);
	                    	    }
	                    	    catch(Exception e)
	                    	    {}
                    }else
                    {
                 	   try{
	                   			MySurfaceView.timecount++;
	                   			Thread.sleep(1000);
                 	   }catch(Exception e){}
                    }
            		}
            	}
            }.start();
        
        } 
        //�����vø����J�ɭ�
        public void drawOrthLoadingView(){
            if(isFirstFrame){ //�Y�G�O�Ĥ@��
            	MatrixState.pushMatrix();
	            MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//�]�w�����v
	            MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//�]�w��v��
	            MatrixState.copyMVMatrix();
	            loadingView.drawSelf(processBeijing);
	            MatrixState.popMatrix();
            	isFirstFrame=false;
            }
            else 
            {//�o�̶i��귽�����J
            	 GLES20.glEnable(GLES20.GL_BLEND);
                 GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
                 GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            	MatrixState.pushMatrix();
	            MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//�]�w�����v
	            MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//�]�w��v��
	            MatrixState.copyMVMatrix(); 
	            MatrixState.pushMatrix();  //�]�w�i�׫��ܾ�
	            MatrixState.translate(-2+2*load_step/(float)8, 0f, -0.001f);
	            processBar.drawSelf(processId);
	            MatrixState.popMatrix();
	            loadingView.drawSelf(processBeijing); //ø��I����
	            MatrixState.popMatrix();
	            
	             GLES20.glDisable(GLES20.GL_BLEND); 
	             GLES20.glEnable(GLES20.GL_DEPTH_TEST); 
	            loadResource(); //���J�귽����k
	            return;
            }
        }
    }
	  public void loadResource()
  	{
  		switch(load_step)
  		{
  		case 0: 
  			load_step++;
  			break;
  		case 1:
  			init_All_Texture(load_step);
  			load_step++;
  			break;
  		case 2:
  			init_All_Texture(load_step);
  			load_step++;
  			break;
  		case 3:
  			init_All_Texture(load_step);
  			load_step++;
  			break;
  		case 4:
  			init_All_Texture(load_step);
  			load_step++;
  			break;
  		case 5:
  			init_All_Texture(load_step);
  			load_step++;
  			break;
  		case 6:
  			init_All_Texture(load_step);
  			load_step++;
  			break;
  		case 7:
  			init_All_Texture(load_step);
  			load_step++;
  			break;
  		case 8:
  			load_step++;
  			init_All_Texture(load_step);
  			isLoadedOk=true;//������@�ſ��
  			loadingView=null;//�P��
  			processBar=null;//�P��
  			break;
  		}
  	}
	  public void showToast() {
      	handler.post(new Runnable() {
      	   public void run() {
         			   cx= Constant.TARGET_X+Radius*(float)Math.sin(xAngle);//,   //�H����m��XEYE_X
         			   cy=Constant.EYE_Y+backtoorigion;//,//�H����m��YRadius*(float)Math.cos(xAngle)/3
         			   cz=Constant.TARGET_Z+Radius*(float)Math.cos(xAngle);//,   //�H����m��z
         			   tx=0;//, 	//�H�����ݪ��IX
         			   ty=Constant.EYE_Y+delatY;//, //�H�����ݪ��IY
         			   tz=0;//�H�����ݪ��IZ
         			   camerarun=false;
      	   }
      	  });
      	 }
	  public void init_All_Texture(int index)
      {
      	switch(index)
      	{
      	case 1: 
      		cx= Constant.TARGET_X+Radius*(float)Math.sin(xAngle);//,   //�H����m��XEYE_X
     			cy= Constant.EYE_Y+delatY;//,//�H����m��YRadius*(float)Math.cos(xAngle)/3
     			cz=  Constant.TARGET_Z+Radius*(float)Math.cos(xAngle);//,  //�H����m��z
     			tx= Constant.TARGET_X;//, 	//�H�����ݪ��IX
     			ty= Constant.EYE_Y+delatY;//, //�H�����ݪ��IY
     			tz= Constant.TARGET_Z;//,//�H�����ݪ��IZ
                 MatrixState.setCamera( //��¶�T�w�b���Y�@�����y��i�����
           			   		cx,
           			   		cy,
           			   		cz,
           			   		tx,
           			   		ty,
           			   		tz, 
                  			dx, 
                  			dy, 
                  			dz);
      		break;
      	case 2:
      		floorTextureId=initTextureRepeat(R.drawable.f6);
            front=initTexture(R.drawable.front); 
            le=initTexture(R.drawable.left);
            ri=initTexture(R.drawable.right);
            back=initTexture(R.drawable.back1);  
    		touc=initTexture(R.drawable.touch);
      	case 3:
              cubeTextureId[0]=initTexture(R.drawable.cube1);
              cubeTextureId[1]=initTexture(R.drawable.cube);
              numberTexture[0]=initTexture(R.drawable.d0);
      		   numberTexture[1]=initTexture(R.drawable.d1);
               numberTexture[2]=initTexture(R.drawable.d2);
               numberTexture[3]=initTexture(R.drawable.d3);
               numberTexture[4]=initTexture(R.drawable.d4);
      		break;
      	case 4:
      	        numberTexture[5]=initTexture(R.drawable.d5);
                numberTexture[6]=initTexture(R.drawable.d6);
                numberTexture[7]=initTexture(R.drawable.d7);
                numberTexture[8]=initTexture(R.drawable.d8); 
                numberTexture[9]=initTexture(R.drawable.d9);
                timetex=initTexture(R.drawable.time);
                scoretex=initTexture(R.drawable.score);
                maohaotex=initTexture(R.drawable.maohao);
      		break;
      	case 5:
      		 floor=new TexFloor(ShaderManager.getTextureShaderProgram(),40*Constant.UNIT_SIZE,-Constant.UNIT_SIZE,planeShape,dynamicsWorld);
                sky=new TextureRect(ShaderManager.getTextureShaderProgram(),MySurfaceView.this,50f,40f,0f);
                scorebord=new TextureRect(ShaderManager.getTextureShaderProgram(),MySurfaceView.this,0.05f,0.08f,0f);
                pic=new  TextureRect(ShaderManager.getTextureShaderProgram(),MySurfaceView.this,0.3f,0.08f,0f);
                touchpp=new TextureRect(ShaderManager.getTextureShaderProgram(),MySurfaceView.this,0.2f,0.1f,0);
      		break; 
      	case 6: 
      	  float xStart=-1f;//x�y�а_�l��Constant.TARGET_Z+Radius*(float)Math.cos(xAngle)
          float yStart=-1/3f;//y�y�а_�l��
          float zStart=-1f;//z�y�а_�l��
          for(int j=0;j<18;j++)//18
          	{
          		if(j%2!=0)
          		{ 
          			yAngle=90;
          			for(int k=0;k<3;k++)//3
              		{
              			MyCube tcTemp=new MyCube       //�إ߯��z�ߤ���
              			(
              					MySurfaceView.this,		//MySurfaceView���Ѧ�
                  				Constant.UNIT_SIZE,		//�ؤo
                  				boxShape1,
                  				dynamicsWorld,			//����@��
                  				1,						//�����q
                  				xStart+k*(0f+2*Constant.UNIT_SIZE),//�_�lx�y��
                  				yStart+j*(0.00f+2*Constant.UNIT_SIZE/2),//�_�ly�y��
                  				zStart+(2*Constant.UNIT_SIZE), //�_�lz�y��        
                  				yAngle,
                  				ShaderManager.getTextureShaderProgram(),//�ۦ⾹�{���Ѧ�
                  				j+1
                  		);
              			tcTemp.index=j*3+k;
              			cubegroup.add(tcTemp);
              			tcTemp.body.forceActivationState(RigidBody.WANTS_DEACTIVATION);
              		}
          		}else
          		{
          			yAngle=0;
          			for(int k=0;k<3;k++)
              		{
              			MyCube tcTemp=new MyCube       //�إ߯��z�ߤ���
              			(
              					MySurfaceView.this,		//MySurfaceView���Ѧ�
                  				Constant.UNIT_SIZE,		//�ؤo
                  				boxShape2,
                  				dynamicsWorld,			//����@��
                  				1,						//�����q		
                  				xStart+(2)*Constant.UNIT_SIZE,//�_�lx�y��
                  				yStart+j*(0.00f+2*Constant.UNIT_SIZE/2), //�_�ly�y��        
                  				zStart+k*(0f+2*Constant.UNIT_SIZE),//�_�lz�y��
                  				yAngle,
                  				ShaderManager.getTextureShaderProgram(),//�ۦ⾹�{���Ѧ�
              					j+1
              			);
              			tcTemp.index=j*3+k;
              			cubegroup.add(tcTemp);
              			tcTemp.body.forceActivationState(RigidBody.WANTS_DEACTIVATION);
              		}
          		}
          	}
		break;
      	case 7: 
      	  new Thread()
          {
          	public void run()
          	{
          		while(simulateFlag)
          		{            			
          			try 
          			{
              			dynamicsWorld.stepSimulation(Constant.TIME_STEP, Constant.MAX_SUB_STEPS);
							Thread.sleep(100);	
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
          		}
          	}
          }.start(); 
          break;
      	case 8: 
      	  break;
      	} 
      } 
	  public int initTexture(int drawableId){//textureId
		//���ͯ��zID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //���ͪ����zid���ƶq
				textures,   //���zid���}�C
				0           //�����q
		); 
		
		   //�z�L��J�y���J�Ϥ�===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        
        
        
        
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        
		
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //�z�L��J�y���J�Ϥ�===============end=====================  
        
        //��ڸ��J���z
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //���z���A�A�bOpenGL ES��������GL10.GL_TEXTURE_2D
        		0, 					  //���z�����h�A0��ܰ򥻹ϧμh�A�i�H�A�Ѭ������K��
        		bitmapTmp, 			  //���z�ϧ�
        		0					  //���z��ؤؤo
        );
        bitmapTmp.recycle(); 		  //���z���J���\������Ϥ�
        return textureId;
	}
	  public int initTextureRepeat(int drawableId)//textureId
	  {
		//���ͯ��zID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //���ͪ����zid���ƶq
				textures,   //���zid���}�C
				0           //�����q
		);
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        
        //�z�L��J�y���J�Ϥ�===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //�z�L��J�y���J�Ϥ�===============end=====================  
        
        //��ڸ��J���z
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //���z���A�A�bOpenGL ES��������GL20.GL_TEXTURE_2D
        		0, 					  //���z�����h�A0��ܰ򥻹ϧμh�A�i�H�A�Ѭ������K��
        		bitmapTmp, 			  //���z�ϧ�
        		0					  //���z��ؤؤo
        );
        bitmapTmp.recycle(); 		  //���z���J���\������Ϥ�
        return textureId;
	}
	  //�z�L�`�����O�����Ϥ����줸�հ}�C  �o�쯾�zid
	  public int iniTexture( byte []textureData){//textureId
			//���ͯ��zID
			int[] textures = new int[1];
			GLES20.glGenTextures
			(
					1,          //���ͪ����zid���ƶq
					textures,   //���zid���}�C
					0           //�����q
			);
			int textureId=textures[0];    
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
	        
	        Bitmap bitmapTmp;
	        try 
	        {
	        	bitmapTmp = BitmapFactory.decodeByteArray(textureData, 0, textureData.length);
	        } 
	        finally 
	        {
	        }
	        //��ڸ��J���z
	        GLUtils.texImage2D
	        (
	        		GLES20.GL_TEXTURE_2D,   //���z���A�A�bOpenGL ES��������GL10.GL_TEXTURE_2D
	        		0, 					  //���z�����h�A0��ܰ򥻹ϧμh�A�i�H�A�Ѭ������K��
	        		bitmapTmp, 			  //���z�ϧ�
	        		0					  //���z��ؤؤo
	        );
	        bitmapTmp.recycle(); 		  //���z���J���\������Ϥ�
	        return textureId;
		}
}
