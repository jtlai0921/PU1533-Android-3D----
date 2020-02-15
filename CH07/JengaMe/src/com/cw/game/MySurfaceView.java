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
	SceneRenderer mRenderer;//場景著色器
	//int currLayer=-1;
	//static int currindex=0;
	//=============拾取============
	//光源距離Y軸的距離
    final float LIGHT_Y=70;
    public  float ratio;
    float mPreviousX;//上次的觸控位置X座標
    float mPreviousY;
    float preNanoTime;
    private int load_step=0;	
	boolean isLoadedOk=false;
    boolean simulateFlag=true;
    boolean timefalg=true;
    //是否搬移的標志位
    boolean isMoveFlag=false;
    float cx=0;	//攝影機位置x
    float cy=1;  
    float cz=6;   //攝影機位置z
    float tx=0;   //攝影機目的點x
    float ty=0;   //攝影機目的點z
    float tz=-10;   //攝影機目的點z
    int dx=0;
    int dy=1;
    int dz=0;
    //儲存抬起手後攝影機轉動的角度
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
	DiscreteDynamicsWorld dynamicsWorld;//世界物件
	CollisionShape planeShape;//共用的平面形狀
	BoxShape boxShape1;
	BoxShape boxShape2;
	JengaMeActivity activity;
	static int maxLayer=18;//游戲的最終得分 			································		 預留		 ····························
	static int index3=53;
	boolean touchpoint=false;//顯現觸控點
	float currX=-12;
	float currY=-12;
	static int timecount=0;
	float tpx=0;
	float tpy=0;
	float tpz=0;
	Vector3f localInertia = new Vector3f(0, 0, 0);//慣性向量
	CollisionShape colShape;
	float x;
	float y;
	int xt=0;
	int yt=0;
	int i=0;
	float Radius=Constant.EYE_Z;
    long previousTime;//上次按下的時間
    float xAngle=0;
    float xAngleV=0;
    float delatY=0;
    float yAngle=90;
	boolean camerarun=true;
	boolean scalsable=false;
	static boolean changeSelectState=true;
	int touchCase=-1;
	HashMap<Integer,BNPoint> hm=new HashMap<Integer,BNPoint>();
	float distance=0;//主輔點距離
	int tmpIndex=-1;
	int xAfh=1;
	float heightY=0;
	boolean playmusic=true;
	int[] cubeTextureId=new int[2];//箱子面紋理
	int []numberTexture=new int[10];
	int timetex;
	int scoretex;
	int maohaotex;
	int floorTextureId;//地面紋理
	int front;
	int le;
	int back;
	int ri;
	int touc;
	int processBeijing;
	int processId;
	TexFloor floor;//紋理矩形1
	TextureRect sky;//紋理矩形1
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
        //起始化實體世界
        initWorld();        
        mRenderer = new SceneRenderer();	//建立場景著色器
        setRenderer(mRenderer);				//設定著色器		
        playmusic= activity.sp.getBoolean("playeffect", false);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設定著色模式為主動著色   
    }
	//起始化實體世界的方法
	public void initWorld()
	{
		//建立碰撞檢驗組態訊息物件
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();		
		//建立碰撞檢驗算法分配者物件，其功能為掃描所有的碰撞檢驗對，並確定適用的檢驗策略對應的算法
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);		
		//設定整個實體世界的邊界訊息
		Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
		Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
		int maxProxies = 1024;
		//建立碰撞檢驗粗測階段的加速算法物件
		AxisSweep3 overlappingPairCache =new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		//建立推動約束解決者物件
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		//建立實體世界物件
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration);
		//設定重力加速度
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
		
		float x = event.getX();//取得觸控點X座標
    	float y = event.getY();
    	float delx= x-mPreviousX;//計算X向觸控位移 
    	float dely=y-mPreviousY;
        long currTime=System.currentTimeMillis();//取得目前時間戳
        long timeSpan=(currTime-previousTime)/7;//計算兩次觸控事件之間的時間差
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
				//計算觸控點距離並換算為縮放系數&&!isMoveFlag
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
    				x, //觸控點X座標
    				y, //觸控點Y座標
    				JengaMeActivity.screenWidth, //螢幕寬度
    				JengaMeActivity.screenHeight, //螢幕長度
    				left, //角度left、top值
    				top,
    				near, //角度near、far值
    				far
    			);
    			//射線AB
    			MyVector3f start = new MyVector3f(AB[0], AB[1], AB[2]);//起點
    			MyVector3f end = new MyVector3f(AB[3], AB[4], AB[5]);//終點
    			MyVector3f dir = end.minus(start);//長度和方向
        		
        		float minTime=1;//記錄清單中所有物體與AB相交的最短時間
        		for(int i=0;i<cubegroup.size();i++){//檢查清單中的物體 
        			AABB3 box = cubegroup.get(i).getCurrBox(); //獲得物體AABB包圍盒   
    				float t = box.rayIntersect(start, dir, null);//計算相交時間
        			if (t <= minTime) {
    					minTime = t;//記錄最小值
    					tmpIndex = i;//記錄本次觸摸物體索引&&cubegroup.get(i).touchable
    				}
        		}
        		//為了解決當被觸控物體位於頂端時的 事件
        		if(!changeSelectState)//&&若果狀態位於不可改變狀態的條件下  此時肯定有被勾選的物體～～
        		{
        			if(tmpIndex!=-1&&tmpIndex==checkedIndex&&cubegroup.get(checkedIndex).upflag==1)//若果本次觸摸到了已經勾選的位於頂端的物體
        			{
        				touchCase=0;
        				MyCube lovo = cubegroup.get(checkedIndex);
        				lovo.touchable=true;
        				Transform startTransform = new Transform();//建立剛體的起始變換物件
						startTransform.setIdentity();//變換起始化
						startTransform.origin.set(new Vector3f(tpx, tpy, tpz));//設定起始的位置
						//建立剛體的運動狀態物件
						DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
						//建立剛體訊息物件
						if(lovo.yAngle==90)
						{
							colShape=boxShape1;
						}else
						{
							colShape=boxShape2;
						}
						RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo
								(1, myMotionState, colShape, localInertia);//··················缸體大小待定據位置而定
						RigidBody body1;//對應的剛體物件
						body1 = new RigidBody(rbInfo);//建立剛體
						body1.setRestitution(0.000f);//設定反彈系數
						body1.setFriction(0.3f);//設定摩擦系數
						if(lovo.body!=null)//確保舊的剛提示被銷毀的
						{
							dynamicsWorld.removeRigidBody(lovo.body);
							
						}
						dynamicsWorld.addRigidBody(body1);//將剛體加入進實體世界
						lovo.body=body1;
        			    camerarun=false;
        			    cubegroup.get(tmpIndex).isPicked=true;
    					cubegroup.get(tmpIndex).body.activate();
        				cubegroup.get(tmpIndex).addPickedConstraint();
        			}
        			else//當有物體位於頂端時 不觸摸物體就可旋轉相機
        			{
        				touchCase=1;
        				camerarun=true;
        			}
        		}
        		else//在狀態可變更的情況下 的動作 程式碼 在拖拉木塊的情況 
        		{
        		 if(tmpIndex==-1)//若果本次沒有摸到物體  就可以轉動攝影機
	        		{
	        			if(checkedIndex==-1)
	        			{
	        				touchCase =6;
	        				camerarun=true;		//若果兩次觸摸都是 沒有勾選物體 就僅僅轉攝影機
	        			}else 					
	        			{	//若果第一次觸摸勾選了物體  但是本次沒有摸中物體 則將被勾選的物體設為空 checkedindex=-1
	        				camerarun=true;
	        				touchCase=2;
	        			}
	        		}else//本次觸摸 摸到了木塊
	        		{
		        		if(checkedIndex==-1)//當沒有物體被勾選時 就 將目前的觸控狀態更新 要求沒有發生滑動
		        		{
		        				touchCase=3;
		        				camerarun=true;
		        		}
		        		else 
		        		{
		        			if(tmpIndex!=checkedIndex)//若果目前點擊的木塊和勾選木塊不一樣 就更換被勾選的
		        			{
		        					touchCase=4;
		        					camerarun=true;
 
			        		}else//若果 目前點住的和以被勾選的是同一個  就啟動 此物體 並對其進行動作變換
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
							int fh=1;//將y軸的變化量轉到z軸時應該只附加絕對值 ，符號由原來的Z軸確定
							if(Math.round(dir1.z)!=0)
							{
								fh=Math.round(dir1.z)/Math.abs(Math.round(dir1.z));
							}
							if(cz>=0.707*Radius)//二分之根號2的半徑長 根據相機的位置據頂拖拉方向的不同
							{//前面
								dir1.set(dir1.x*vFactor,0,-fh*dir1.y*vFactor);
							}
							else if(cz<=-0.707*Radius)
							{//後面
								dir1.set(dir1.x*vFactor,0,fh*dir1.y*vFactor);
							}
							else if(cx>-0.707*Radius)
							{//右面
								dir1.set(-fh*dir1.y*vFactor,0,-xAfh*dir1.x*vFactor);
							}else if(cx<=-0.707*Radius)
							{//左面 
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
					//攝影機的活動程式碼·
					if((camerarun||checkedIndex==-1))
					{//若果兩次的 觸控不再一點 那麼完全可以搬移相機  若果 兩者相等 但是都為-1 那麼 也可以觸控
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
			            			   cx=0;//,//人眼位置的XEYE_X
					 					cy=(maxLayer+1);//,//人眼位置的YRadius*(float)Math.cos(xAngle)/3
					 					cz= 0;//,  //人眼位置的z
					 					tx= 0;//, 	//人眼光看的點X
					 					ty= 0;//, //人眼光看的點Y
					 					tz= 0;//,//人眼光看的點Z
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
					 			tx= Constant.TARGET_X;	//人眼光看的點X
					 			tz= Constant.TARGET_Z;//人眼光看的點Z
					 		}
					 		if(yt==1)
					 		{
				 				cy= Constant.EYE_Y+delatY;//人眼位置的YRadius*(float)Math.cos(xAngle)/3
				 				ty= Constant.EYE_Y+delatY;//人眼光看的點Y
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
				case 0://若果本次觸摸到了已經勾選的位於頂端的物體
					break;
				case 1://當有物體位於頂端時 不觸摸物體就可旋轉相機
					break;
				case 2://若果第一次觸摸勾選了物體  但是本次沒有摸中物體 則將被勾選的物體設為空 checkedindex=-1
					if(!isMoveFlag&&cubegroup.get(checkedIndex).upflag==0)//若果本次沒有搬移
					{
						cubegroup.get(checkedIndex).isPicked=false;//標示為未被勾選
	    				cubegroup.get(checkedIndex).removePickedConstraint();
	    				checkedIndex=-1;
					}
					break;
				case 3://當沒有物體被勾選時 就 將目前的觸控狀態更新 要求沒有發生滑動
					if(!isMoveFlag)//若果沒有搬移
					{
						checkedIndex=tmpIndex;
			       		cubegroup.get(checkedIndex).isPicked=true;//標示為被勾選
					}
					break;
				case 4://若果目前點擊的木塊和勾選木塊不一樣 就更換被勾選的
					if(!isMoveFlag)
					{
						cubegroup.get(checkedIndex).removePickedConstraint();
	    				cubegroup.get(checkedIndex).isPicked=false;
	    				checkedIndex=tmpIndex;
	    				cubegroup.get(checkedIndex).isPicked=true;
					}
					break;
				case 5://若果 目前點住的和以被勾選的是同一個  就啟動 此物體 並對其進行動作變換
					cubegroup.get(checkedIndex).isPicked=true;
					cubegroup.get(checkedIndex).body.activate();
    				cubegroup.get(checkedIndex).addPickedConstraint();
    				break;
				}
				if(checkedIndex!=-1&&(touchCase==0)){//touchCase==5|||touchCase==7此處為對位於頂端的物體進行觸控後的處理
					MyCube lovo = cubegroup.get(checkedIndex);
					if(lovo.upflag==1)//&&tmpIndex!=-1&&tmpIndex==checkedIndex被勾選的物體 在頂端lovo.upflag==1
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
				    			MySurfaceView.index3=lovo.index;//記錄目前的木塊朝向  以便反轉下一輪木塊
				    		}
						}else//滑動並且位置處於有效區域內
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
					    			MySurfaceView.index3=lovo.index;//記錄目前的木塊朝向  以便反轉下一輪木塊
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
		   mPreviousX = x;//記錄觸控筆位置
		   mPreviousY=y;
	       previousTime=currTime;//記錄此次時間戳
		return true;
	}
	
	class SceneRenderer implements GLSurfaceView.Renderer 
    {
		private boolean isFirstFrame=true;
        public void onDrawFrame(GL10 gl) {
        	//清除深度緩沖與彩色緩沖
          GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
         if(!isLoadedOk)//如過沒有載入完成
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
        	MatrixState.setCamera( //圍繞固定軸的某一圓環軌跡進行旋轉
        			  		cx,cy,cz,tx,
         			   		ty,tz,dx,dy,dz);
            MatrixState.copyMVMatrix();
            
            //繪制地板
            MatrixState.pushMatrix();
            floor.drawSelf( floorTextureId);
            
            MatrixState.popMatrix();
            gl.glDisable(GL10.GL_DEPTH_TEST); 
		    gl.glBlendFunc( GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA); //以下是為了啟動混合功能 GL10.GL_ONE_MINUS_SRC_ALPHA
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
            //繪制箱子
            synchronized(cubegroup)
			{
	            for(MyCube tc:cubegroup)
	            {
	            	MatrixState.pushMatrix();
			    	tc.drawSelf(MySurfaceView.this,cubeTextureId,0); //畫影子,0
	                MatrixState.popMatrix(); 
	            }
			}
            MatrixState.pushMatrix();
            MatrixState.translate(0,12,-50);
            sky.drawSelf(front);
            MatrixState.popMatrix();
            
            //繪制天空盒前面
            MatrixState.pushMatrix();
            MatrixState.translate(0,12, 50);
            MatrixState.rotate(180, 0, 1, 0);
            sky.drawSelf(back);
            MatrixState.popMatrix(); 
            //繪制天空盒左面
            MatrixState.pushMatrix();
            MatrixState.translate(-50, 12, 0);
            MatrixState.rotate(90, 0, 1, 0);
            sky.drawSelf(ri);
            MatrixState.popMatrix(); 
            //繪制天空盒右面
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
	            gl.glBlendFunc( GL10.GL_ONE,GL10.GL_ONE_MINUS_SRC_ALPHA); //以下是為了啟動混合功能
	            gl.glEnable(GL10.GL_BLEND);
		        MatrixState.pushMatrix();
		        MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//設定正交投影
		        MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//設定攝影機
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
            //設定視窗大小及位置 
        	GLES20.glViewport(0, 0, width, height);
            //計算透視投影的比例  
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
        	//設定螢幕背景色RGBA
            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);             
            //開啟深度檢驗
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //開啟背面剪裁   
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //起始化變換矩陣
            MatrixState.setInitStack(); 
            MatrixState.copyMVMatrix();
            MatrixState.setLightLocation(16,30, 12); 
            ShaderManager.loadCodeFromFile(activity.getResources());
            ShaderManager.compileShader();
            load_step=0;//載入資源的步數
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
            		if(MyCube.countD>3)//當有三個木塊的y方向偏移超過0.2就離開程式。··································預留·························
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
        //正交投影繪制載入界面
        public void drawOrthLoadingView(){
            if(isFirstFrame){ //若果是第一框
            	MatrixState.pushMatrix();
	            MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//設定正交投影
	            MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//設定攝影機
	            MatrixState.copyMVMatrix();
	            loadingView.drawSelf(processBeijing);
	            MatrixState.popMatrix();
            	isFirstFrame=false;
            }
            else 
            {//這裡進行資源的載入
            	 GLES20.glEnable(GLES20.GL_BLEND);
                 GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
                 GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            	MatrixState.pushMatrix();
	            MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//設定正交投影
	            MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//設定攝影機
	            MatrixState.copyMVMatrix(); 
	            MatrixState.pushMatrix();  //設定進度指示器
	            MatrixState.translate(-2+2*load_step/(float)8, 0f, -0.001f);
	            processBar.drawSelf(processId);
	            MatrixState.popMatrix();
	            loadingView.drawSelf(processBeijing); //繪制背景圖
	            MatrixState.popMatrix();
	            
	             GLES20.glDisable(GLES20.GL_BLEND); 
	             GLES20.glEnable(GLES20.GL_DEPTH_TEST); 
	            loadResource(); //載入資源的方法
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
  			isLoadedOk=true;//切換到一級選單
  			loadingView=null;//銷毀
  			processBar=null;//銷毀
  			break;
  		}
  	}
	  public void showToast() {
      	handler.post(new Runnable() {
      	   public void run() {
         			   cx= Constant.TARGET_X+Radius*(float)Math.sin(xAngle);//,   //人眼位置的XEYE_X
         			   cy=Constant.EYE_Y+backtoorigion;//,//人眼位置的YRadius*(float)Math.cos(xAngle)/3
         			   cz=Constant.TARGET_Z+Radius*(float)Math.cos(xAngle);//,   //人眼位置的z
         			   tx=0;//, 	//人眼光看的點X
         			   ty=Constant.EYE_Y+delatY;//, //人眼光看的點Y
         			   tz=0;//人眼光看的點Z
         			   camerarun=false;
      	   }
      	  });
      	 }
	  public void init_All_Texture(int index)
      {
      	switch(index)
      	{
      	case 1: 
      		cx= Constant.TARGET_X+Radius*(float)Math.sin(xAngle);//,   //人眼位置的XEYE_X
     			cy= Constant.EYE_Y+delatY;//,//人眼位置的YRadius*(float)Math.cos(xAngle)/3
     			cz=  Constant.TARGET_Z+Radius*(float)Math.cos(xAngle);//,  //人眼位置的z
     			tx= Constant.TARGET_X;//, 	//人眼光看的點X
     			ty= Constant.EYE_Y+delatY;//, //人眼光看的點Y
     			tz= Constant.TARGET_Z;//,//人眼光看的點Z
                 MatrixState.setCamera( //圍繞固定軸的某一圓環軌跡進行旋轉
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
      	  float xStart=-1f;//x座標起始值Constant.TARGET_Z+Radius*(float)Math.cos(xAngle)
          float yStart=-1/3f;//y座標起始值
          float zStart=-1f;//z座標起始值
          for(int j=0;j<18;j++)//18
          	{
          		if(j%2!=0)
          		{ 
          			yAngle=90;
          			for(int k=0;k<3;k++)//3
              		{
              			MyCube tcTemp=new MyCube       //建立紋理立方體
              			(
              					MySurfaceView.this,		//MySurfaceView的參考
                  				Constant.UNIT_SIZE,		//尺寸
                  				boxShape1,
                  				dynamicsWorld,			//實體世界
                  				1,						//剛體質量
                  				xStart+k*(0f+2*Constant.UNIT_SIZE),//起始x座標
                  				yStart+j*(0.00f+2*Constant.UNIT_SIZE/2),//起始y座標
                  				zStart+(2*Constant.UNIT_SIZE), //起始z座標        
                  				yAngle,
                  				ShaderManager.getTextureShaderProgram(),//著色器程式參考
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
              			MyCube tcTemp=new MyCube       //建立紋理立方體
              			(
              					MySurfaceView.this,		//MySurfaceView的參考
                  				Constant.UNIT_SIZE,		//尺寸
                  				boxShape2,
                  				dynamicsWorld,			//實體世界
                  				1,						//剛體質量		
                  				xStart+(2)*Constant.UNIT_SIZE,//起始x座標
                  				yStart+j*(0.00f+2*Constant.UNIT_SIZE/2), //起始y座標        
                  				zStart+k*(0f+2*Constant.UNIT_SIZE),//起始z座標
                  				yAngle,
                  				ShaderManager.getTextureShaderProgram(),//著色器程式參考
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
		//產生紋理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //產生的紋理id的數量
				textures,   //紋理id的陣列
				0           //偏移量
		); 
		
		   //透過輸入流載入圖片===============begin===================
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
        //透過輸入流載入圖片===============end=====================  
        
        //實際載入紋理
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //紋理型態，在OpenGL ES中必須為GL10.GL_TEXTURE_2D
        		0, 					  //紋理的階層，0表示基本圖形層，可以瞭解為直接貼圖
        		bitmapTmp, 			  //紋理圖形
        		0					  //紋理邊框尺寸
        );
        bitmapTmp.recycle(); 		  //紋理載入成功後釋放圖片
        return textureId;
	}
	  public int initTextureRepeat(int drawableId)//textureId
	  {
		//產生紋理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //產生的紋理id的數量
				textures,   //紋理id的陣列
				0           //偏移量
		);
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        
        //透過輸入流載入圖片===============begin===================
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
        //透過輸入流載入圖片===============end=====================  
        
        //實際載入紋理
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //紋理型態，在OpenGL ES中必須為GL20.GL_TEXTURE_2D
        		0, 					  //紋理的階層，0表示基本圖形層，可以瞭解為直接貼圖
        		bitmapTmp, 			  //紋理圖形
        		0					  //紋理邊框尺寸
        );
        bitmapTmp.recycle(); 		  //紋理載入成功後釋放圖片
        return textureId;
	}
	  //透過常數類別中的圖片的位元組陣列  得到紋理id
	  public int iniTexture( byte []textureData){//textureId
			//產生紋理ID
			int[] textures = new int[1];
			GLES20.glGenTextures
			(
					1,          //產生的紋理id的數量
					textures,   //紋理id的陣列
					0           //偏移量
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
	        //實際載入紋理
	        GLUtils.texImage2D
	        (
	        		GLES20.GL_TEXTURE_2D,   //紋理型態，在OpenGL ES中必須為GL10.GL_TEXTURE_2D
	        		0, 					  //紋理的階層，0表示基本圖形層，可以瞭解為直接貼圖
	        		bitmapTmp, 			  //紋理圖形
	        		0					  //紋理邊框尺寸
	        );
	        bitmapTmp.recycle(); 		  //紋理載入成功後釋放圖片
	        return textureId;
		}
}
