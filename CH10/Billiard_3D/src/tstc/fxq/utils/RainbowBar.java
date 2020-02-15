package tstc.fxq.utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import android.opengl.GLES20;

/*
 * �m�i��
 */
public class RainbowBar 
{	
	//�O�D�����̤j�O�D������n��
    public static final int N_MAX = ColorUtil.result.length;
    //�O�D���̤j��
	public static final float MAX_STRENGTH = Constant.V_BALL_MAX;
	
    MySurfaceView mv;//MySurfaceView���O���Ѧ�
    
	int mProgram;//�ۭq�ۦ�ް������id
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maColorHandle; //���I�m���ݩʰѦ�id
	
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mColorBuffer;//���I�ۦ��ƽw�R
    
    int currN=N_MAX;//�ثen��
    private float currStrength;//�ثe�O�D��

    //������Ĳ���d��
    float addedTouchScaleX;//������x��VĲ���d��
    float addedTouchScaleY;//������Y��VĲ���d��
    
    //3D�@�ɤ����ܼ�
	float x;//����O�b3D�@�ɤ�����m
	float y;
    float width;//����O�b3D�@�ɤ����e�שM���׭�
    float height;
    
    float perHeight;//�C�ӱm�i��������
    float gapSize;//�m�i���C��ӱm�i���������_�ؤؤo
    
    float gapRatio;//�_�إe�����:gapRatio == gapSize / (perHeight + gapSize)
	//2D�@�ɤ����ܼ�
	float x2D;//�����ù��������W����m�y��
	float y2D;
    float width2D;//����O�b�ù������e�שM���׭�
    float height2D;
    
    float perHeight2D;//�C�ӱm�i���b�ù���������
    float gapSize2D;//�m�i���C��ӱm�i�������b�ù������_�ؤؤo
    //2D��3D�@�ɤ��ؤo����ҡG3D = 2D*ratio2DTo3D
    float ratio2DTo3D;
    
    public RainbowBar(
    		MySurfaceView mv,
    		float x2D, float y2D, //��������O�����ù��������W����m�y��
    		float width2D, float height2D, //����O�b�ù������e�שM���׭�
    		float addedTouchScaleX, float addedTouchScaleY,//������Ĳ���d��
    		float gapRatio//�_�إe�����
    )
    {
    	this.x2D = x2D;
    	this.y2D = y2D;
    	this.width2D = width2D;
    	this.height2D = height2D;
    	this.addedTouchScaleX = addedTouchScaleX;
    	this.addedTouchScaleY = addedTouchScaleY;
    	this.gapRatio = gapRatio;

    	//�p��2D�@�ɨC�ӱm�i�������שM�m�i���������j
    	calPerSize();
    	//�N����O����m�B�ؤo�ഫ��3D�@�ɤ�����m�M�ؤo
    	changePositionAndScaleFrom2DTo3D();
    	//�_�l�Ƴ��I�y�лP�ۦ���
    	initVertexData();
    	//�_�l��shader        
    	intShader(mv);
    }
    
    //�p��C�ӱm�i�������שM�m�i���������j����k
    public void calPerSize(){
    	/*
    	 * �z�L��{���s�ըD��g�Mh
    	 * g*(n-1)+h*n=H
    	 * g/(h+g)=r
    	 */
    	//���_�ؤؤo
		gapSize2D = height2D / ((N_MAX - 1) + (1/gapRatio - 1) * N_MAX);
		//�ѨC������
		perHeight2D = gapSize2D * (1/gapRatio - 1);
    }
    
    //�N����O����m�B�ؤo�ഫ��3D�@�ɤ�����m�M�ؤo����k
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
        perHeight = perHeight2D * ratio2DTo3D;
        gapSize = gapSize2D * ratio2DTo3D;
    	/*
    	 * �ù�����m�z�L����O�����W�I�y�Ш�2D�M3D�@�ɤ������ߦ�m���Z������Ҩӭp��
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
    	
        List<Float>vertexList=new ArrayList<Float>();//���I�y��
        List<Float>colorList=new ArrayList<Float>();//���I�m��
        
        //���I
        for(int i=0;i<RainbowBar.N_MAX;i++)
        {
        	vertexList.add(-width/2); vertexList.add((i+1)*perHeight + i*gapSize); vertexList.add(0f);
        	vertexList.add(-width/2); vertexList.add(i*(perHeight+gapSize)); vertexList.add(0f);
        	vertexList.add(width/2); vertexList.add((i+1)*perHeight + i*gapSize); vertexList.add(0f);
        	
        	vertexList.add(-width/2); vertexList.add(i*(perHeight+gapSize)); vertexList.add(0f);
        	vertexList.add(width/2); vertexList.add(i*(perHeight+gapSize)); vertexList.add(0f);
        	vertexList.add(width/2); vertexList.add((i+1)*perHeight + i*gapSize); vertexList.add(0f);
        }
        
        //�m��
        for(int i=0;i<RainbowBar.N_MAX;i++)
        {        	
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);
        	colorList.add(ColorUtil.result[i][0]/255f*1.0f); colorList.add(ColorUtil.result[i][1]/255f*1.0f); colorList.add(ColorUtil.result[i][2]/255f*1.0f); colorList.add(1f);      	
        }

		//���I�y�нw�R
		float []vertices=new float[vertexList.size()];
		for(int i=0;i<vertexList.size();i++)
		{
			vertices[i]=vertexList.get(i);
		}
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        
        
        
        //���I�ۦ��ƪ��_�l��================begin============================
        
		float []colors=new float[colorList.size()];
		for(int i=0;i<colorList.size();i++)
		{
			colors[i]=colorList.get(i);
		}

        //�إ߳��I�ۦ��ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mColorBuffer = cbb.asFloatBuffer();//�ରFloat���w�R
        mColorBuffer.put(colors);//�V�w�R�Ϥ���J���I�ۦ���
        mColorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�ۦ��ƪ��_�l��================end============================
    }

    //�_�l��shader
    public void intShader(MySurfaceView mv)
    {
    	//���o�ۭq�ۦ�ް������id 
        mProgram = ShaderManager.getColorShader();
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I�m���ݩʰѦ�id  
        maColorHandle= GLES20.glGetAttribLocation(mProgram, "aColor");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
    
    public void drawSelf()
    {
		// �O�@�x�}
		MatrixState.pushMatrix();
		// �N����O�h����3D�@�ɤ������w��m
		MatrixState.translate(x, y - height/2, 0);

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
		// ���e�����w���I�ۦ���
		GLES20.glVertexAttribPointer
		(
				maColorHandle, 
				4, 
				GLES20.GL_FLOAT, 
				false,
				4 * 4, 
				mColorBuffer
		);
		// �e�\���I��m��ư}�C
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maColorHandle);
		// ø��T����
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, currN * 6);

		// �٭�x�}
		MatrixState.popMatrix();
	}

	//�P�_���s�O�_��Ĳ���ƥ󪺤�k
	public boolean isActionOnRainbowBar(float pressX, float pressY)
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
	
	//���ܤO�D���O�D����k
	public void changePower(float pressX, float pressY)
	{
		float tempN = (y2D + height2D - pressY) / (height2D) * N_MAX;
    	//�m�i��ø���l�ƥءA�`�N��tempN�MColorBar.N_MAX�Ȥ����p��
    	currN = Math.min(Math.round(tempN), N_MAX);
    	//����O�D�p��1
    	currN = Math.max(currN, 1);
	}
	//��o�ثe�O�D����k
	public float getCurrStrength(){
    	//�Nn�ȧ�⦨�O�D��
		this.currStrength = ((float) currN / N_MAX) * MAX_STRENGTH;
		return currStrength;
	}
}
