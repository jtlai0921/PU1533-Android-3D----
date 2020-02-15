package tstc.fxq.utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import android.opengl.GLES20;

/*
 * 3D�����������s���O
 * 
 * �`�N�G
 * �إ߸Ӫ���ɥ����bConstant.SCREEN_WIDTH�MConstant.SCREEN_HEIGHT�T�w����C
 * 
 * �b�I�s�����O��drawSelf��k���e�A�n�N��v�Ҧ��]�������v�A�ó]�w�۾�����m�G
 * MatrixState.setProjectOrtho(-Constant.RATIO, Constant.RATIO, -1, 1, 1, 2);
 * MatrixState.setCamera(0,0,1f,0f,0f,0f,0f,1.0f,0.0f);
 * ø����s����A�A�٭��Ӫ���v�Ҧ��M�۾���m
 */
public class VirtualButton 
{	
	int mProgram;//�ۭq�ۦ�ް������id
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id
    int muIsDown;//���s�O�_�Q���U���ݩʰѦ�id 
	
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R 
    int vCount=0;
    
    //������s�������ܼ�
    float sEnd;//���s�k�U����s�Bt��
    float tEnd;
	private int isDown = 0;//���s�O�_�Q���U���ЧӦ�A0��ܨS�����U�A1��ܫ��U
	int upTexId;//���U�M��_�����s�Ϥ�id

    //������Ĳ���d��
    float addedTouchScaleX;//������x��VĲ���d��
    float addedTouchScaleY;//������Y��VĲ���d��
    
	//3D�@�ɤ����ܼ�
	float x;//���s�b3D�@�ɤ�����m
	float y;
    float width;//���s�b3D�@�ɤ����e�שM���׭�
    float height;
	//2D�@�ɤ����ܼ�
	float x2D;//�����ù��������W����m�y��
	float y2D;
    float width2D;//���s�b�ù������e�שM���׭�
    float height2D;
    //2D��3D�@�ɤ��ؤo����ҡG3D = 2D*ratio2DTo3D
    float ratio2DTo3D;
    
    public VirtualButton(
    		MySurfaceView mv,
    		float x2D, float y2D, //�������s�����ù��������W����m�y��
    		float width2D, float height2D, //���s�b�ù������e�שM���׭�
    		float addedTouchScaleX, float addedTouchScaleY,//������Ĳ���d��
    		float sEnd, float tEnd, //���s�k�U����s�Bt�ȡC���W����s�Bt�Ȭ�0�C�ҥH���z�Ϥ����s���b���W��
    		int upTexId //��_�����s�Ϥ�id
    )
    {
    	this.x2D = x2D;
    	this.y2D = y2D;
    	this.width2D = width2D;
    	this.height2D = height2D;
    	this.addedTouchScaleX = addedTouchScaleX;
    	this.addedTouchScaleY = addedTouchScaleY;
    	this.sEnd = sEnd;
    	this.tEnd = tEnd;
    	this.upTexId = upTexId;
    	
    	//�N���s����m�B�ؤo�ഫ��3D�@�ɤ�����m�M�ؤo
    	changePositionAndScaleFrom2DTo3D();
    	//�_�l�Ƴ��I�y�лP�ۦ���
    	initVertexData();
    	//�_�l��shader        
    	intShader(mv);
    }

    //�N���s����m�B�ؤo�ഫ��3D�@�ɤ�����m�M�ؤo����k
    public void changePositionAndScaleFrom2DTo3D(){
    	/*
    	 * �p��2D��3D�@�ɤ��ؤo�����
    	 * 
    	 * �ھڥ����v�x�}�����ѼƭȡG
         * MatrixState.setProjectFrustum(-Constant.RATIO, Constant.RATIO, -1, 1, 1, 10);
         * �i��o�ഫ��Ҭ��G
         * ratio2DTo3D = (top+bottom)/Constant.SCREEN_HEIGHT
    	 */
    	ratio2DTo3D = 2.0f/Constant.SCREEN_HEIGHT;
    	//�e�װ��׵�����ܴ�
    	width = width2D * ratio2DTo3D;
    	height = height2D * ratio2DTo3D;
    	/*
    	 * �ù�����m�z�L���s�����W�I�y�Ш�2D�M3D�@�ɤ������ߦ�m���Z������Ҩӭp��
    	 * 3D�@�ɤ������ߦ�m�b�@�ɮy�Шt���I
    	 * 2D�@�ɤ������ߦ�m�b�ù���������
    	 */
    	x = -(ratio2DTo3D * (Constant.SCREEN_WIDTH/2.0f - x2D) - width/2.0f);
    	y = ratio2DTo3D * (Constant.SCREEN_HEIGHT/2.0f - y2D) - height/2.0f;
    }
    //�_�l�Ƴ��I�y�лP�ۦ��ƪ���k
    public void initVertexData()
    {
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=6;
       
        float vertices[]=new float[]
        {
        	-width/2.0f, height/2.0f, 0,
        	-width/2.0f, -height/2.0f, 0,
        	width/2.0f, height/2.0f, 0,
        	
        	-width/2.0f, -height/2.0f, 0,
        	width/2.0f, -height/2.0f, 0,
        	width/2.0f, height/2.0f, 0,
        };
		
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        
        //���I���z�y�и�ƪ��_�l��================begin============================
        float texCoor[]=new float[]//���z�y��
        {
        		0,0, 0,tEnd, sEnd,0,
        		0,tEnd, sEnd,tEnd, sEnd,0        		
        };        
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = cbb.asFloatBuffer();//�ରFloat���w�R
        mTexCoorBuffer.put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I���z�y�и�ƪ��_�l��================end============================

    }

    //�_�l��shader
    public void intShader(MySurfaceView mv)
    {
    	//���o�ۭq�ۦ�ް������id 
        mProgram = ShaderManager.getTexBtnShader();
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
        
        //���o�{�������s�O�_�Q���U���ݩʰѦ�id
        muIsDown=GLES20.glGetUniformLocation(mProgram, "isDown"); 
    }
    
    public void drawSelf()
    {
    	//�O�@�x�}
    	MatrixState.pushMatrix();

		// �N���s�h����3D�@�ɤ������w��m
		MatrixState.translate(x, y, 0);

		// ��w�ϥάY�Mshader�{��
		GLES20.glUseProgram(mProgram);
		// �N�̲��ܴ��x�}�ǤJshader�{��
		GLES20.glUniformMatrix4fv
		(
				muMVPMatrixHandle, 
				1, 
				false,
				MatrixState.getFinalMatrix(), 
				0
		);
		// ���e�����w���I��m���
		GLES20.glVertexAttribPointer
		(
				maPositionHandle, 
				3, 
				GLES20.GL_FLOAT,
				false, 
				3 * 4, 
				mVertexBuffer
		);
		// ���e�����w���I���z�y�и��
		GLES20.glVertexAttribPointer
		(
				maTexCoorHandle, 
				2, 
				GLES20.GL_FLOAT,
				false, 
				2 * 4, 
				mTexCoorBuffer
		);
		// �e�\���I��m��ư}�C
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		// �j�w���z
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		
		//�ǻ��Q���U���Ϥ�id
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, upTexId);
        //�N���s�O�_�Q���U�ݩʶǤJshader�{�� 
        GLES20.glUniform1i(muIsDown, isDown);

		// ø��T����
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		
		//�٭�x�}
        MatrixState.popMatrix(); 
	}
    //���U���s����k
	public void pressDown()
	{
		isDown = 1;
	}
	//�Q�}���s����k
	public void releaseUp()
	{
		isDown = 0;
	}
	//�P�_���s�O�_�Q���U����k
	public boolean isBtnPressedDown() {
		return (isDown == 1);
	}
	//�P�_���s�O�_��Ĳ���ƥ󪺤�k
	public boolean isActionOnButton(float pressX, float pressY)
	{
		if(
				pressX > x2D - addedTouchScaleX &&
				pressX < x2D + width2D + addedTouchScaleX && 
				pressY > y2D - addedTouchScaleY &&
				pressY < y2D + height2D + addedTouchScaleY
		)
		{
			return true;			
		}
		return false;
	}
}
