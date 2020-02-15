package tstc.fxq.utils;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;

/*
 * �Ͳ��۾A���̪�����O���u�t���O
 * 
 * �I�s�����O���R�A��k
 * �i�H�إߦ۾A���̪�����O����
 */
public class InstrumentBoardFactory {
	//��l����ѪR��
	private static final float sSpWidth = 480;//��l�������e��
	private static final float sSpHeight = 800;//��l����������
	
	private static float originalScreenWidth;//��l�������e��
	private static float originalScreenHeight;//��l����������
	private static float originalScreenRatio;//��l���������e��
	
	//�ت��̤����s����m�M�ؤo
	private static float targetX;
	private static float targetY;
	private static float targetWidth;
	private static float targetHeight;
	private static float targetAddedTouchScaleX;
	private static float targetAddedTouchScaleY;
	//�إߵ������s�����R�A��k
	public static VirtualButton newButtonInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //�������s�����ù��������W����m�y��
    		float width2D, float height2D, //���s�b�ù������e�שM���׭�
    		float addedTouchScaleX, float addedTouchScaleY,//������Ĳ���d��
    		float sEnd, float tEnd, //���s�k�U����s�Bt�ȡC���W����s�Bt�Ȭ�0�C�ҥH���z�Ϥ����s���b���W��
    		int upTexId, //���U�M��_�����s�Ϥ�id
    		boolean fixXYRatioFlag, //�O�_�O�����s����l���e��
    		Gravity gravity
    ){
		//�Y�G�ù��e�M���٨S���_�l�ơA�ߥX�ҥ~
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//�_�l�ƭ�l�̤ؤo
		initOriginalScreenScale();
		
		//���ܭ�l���s�����e��A�H�A�����ù�
		if(fixXYRatioFlag == false){
			//�ھڭ�l�̤����s��2D�y�СA�p����
			calTargetToFillScreen(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY);	
		}
		else{//�O����l���s���e��A���A�����ù��A�N���s�Y��øm��
			calTargetToFixXY(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY, gravity);
		}
		
		//�إߥت��̤W���������s����A�öǦ^
		return new VirtualButton(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				targetAddedTouchScaleX, targetAddedTouchScaleY,
				sEnd, tEnd, //sEnd�BtEnd
        		upTexId//upTexId
        );
	}
	//�إߵ������s�����R�A��k---2--���U�ɤ����Ϥ�
	public static VirtualButtonChangePic newButtonChangePicInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //�������s�����ù��������W����m�y��
    		float width2D, float height2D, //���s�b�ù������e�שM���׭�
    		float addedTouchScaleX, float addedTouchScaleY,//������Ĳ���d��
    		float sEnd, float tEnd, //���s�k�U����s�Bt�ȡC���W����s�Bt�Ȭ�0�C�ҥH���z�Ϥ����s���b���W��
    		int upTexId, int downTexId, //���U�M��_�����s�Ϥ�id
    		boolean fixXYRatioFlag, //�O�_�O�����s����l���e��
    		Gravity gravity
    ){
		//�Y�G�ù��e�M���٨S���_�l�ơA�ߥX�ҥ~
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//�_�l�ƭ�l�̤ؤo
		initOriginalScreenScale();
		
		//���ܭ�l���s�����e��A�H�A�����ù�
		if(fixXYRatioFlag == false){
			//�ھڭ�l�̤����s��2D�y�СA�p����
			calTargetToFillScreen(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY);	
		}
		else{//�O����l���s���e��A���A�����ù��A�N���s�Y��øm��
			calTargetToFixXY(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY, gravity);
		}
		
		//�إߥت��̤W���������s����A�öǦ^
		return new VirtualButtonChangePic(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				targetAddedTouchScaleX, targetAddedTouchScaleY,
				sEnd, tEnd, //sEnd�BtEnd
        		upTexId, downTexId //upTexId�BdownTexId,
        );
	}
	//�إ߻���O���R�A��k
	public static Board newBoardInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //��������O�����ù��������W����m�y��
    		float width2D, float height2D, //����O�b�ù������e�שM���׭�
    		float addedTouchScaleX, float addedTouchScaleY,//������Ĳ���d��
    		float sEnd, float tEnd, //����O�k�U����s�Bt�ȡC���W����s�Bt�Ȭ�0
    		int texId, //����O�Ϥ�id
    		int isTransparent,//�O�_�z��
    		boolean fixXYRatioFlag, //�O�_�O������O����l���e��
    		Gravity gravity
    ){
		//�Y�G�ù��e�M���٨S���_�l�ơA�ߥX�ҥ~
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//�_�l�ƭ�l�̤ؤo
		initOriginalScreenScale();
		
		//���ܭ�l����O�����e��A�H�A�����ù�
		if(fixXYRatioFlag == false){
			//�ھڭ�l�̤�����O��2D�y�СA�p����
			calTargetToFillScreen(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY);	
		}
		else{//�O����l����O���e��A���A�����ù��A�N����O�Y��øm��
			calTargetToFixXY(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY, gravity);
		}
		
		//�إߥت��̤W����������O����A�öǦ^
		return new Board(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				targetAddedTouchScaleX, targetAddedTouchScaleY,
				sEnd, tEnd, 
        		texId,
        		isTransparent
        );
	}	

	//�إ߱o�����R�A��k
	public static Score newScoreInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //��������O�����ù��������W����m�y��
    		float width2D, float height2D, //����O�b�ù������e�שM���׭�
    		float sEnd, float tEnd, //����O�k�U����s�Bt��
    		int texId, //����O�Ϥ�id
    		boolean fixXYRatioFlag, //�O�_�O������O����l���e��
    		Gravity gravity
    ){
		//�Y�G�ù��e�M���٨S���_�l�ơA�ߥX�ҥ~
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//�_�l�ƭ�l�̤ؤo
		initOriginalScreenScale();
		
		//���ܭ�l����O�����e��A�H�A�����ù�
		if(fixXYRatioFlag == false){
			//�ھڭ�l�̤�����O��2D�y�СA�p����
			calTargetToFillScreen(x2D, y2D, width2D, height2D, 0, 0);	
		}
		else{//�O����l����O���e��A���A�����ù��A�N����O�Y��øm��
			calTargetToFixXY(x2D, y2D, width2D, height2D, 0, 0, gravity);
		}
		
		//�إߥت��̤W����������O����A�öǦ^
		return new Score(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				sEnd, tEnd,
        		texId
        );
	}	

	//�إ߭˭p�ɪ��R�A��k
	public static Timer newTimerInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //��������O�����ù��������W����m�y��
    		float width2D, float height2D, //����O�b�ù������e�שM���׭�
    		float sEnd, float tEnd, //����O�k�U����s�Bt��
    		int numberTexId, //�Ʀrid
    		int breakTexId,//���j��id
    		boolean fixXYRatioFlag, //�O�_�O������O����l���e��
    		Gravity gravity
    ){
		//�Y�G�ù��e�M���٨S���_�l�ơA�ߥX�ҥ~
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//�_�l�ƭ�l�̤ؤo
		initOriginalScreenScale();
		
		//���ܭ�l����O�����e��A�H�A�����ù�
		if(fixXYRatioFlag == false){
			//�ھڭ�l�̤�����O��2D�y�СA�p����
			calTargetToFillScreen(x2D, y2D, width2D, height2D, 0, 0);	
		}
		else{//�O����l����O���e��A���A�����ù��A�N����O�Y��øm��
			calTargetToFixXY(x2D, y2D, width2D, height2D, 0, 0, gravity);
		}
		
		//�إߥت��̤W����������O����A�öǦ^
		return new Timer(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				sEnd, tEnd,
				numberTexId,
				breakTexId
        );
	}
	//�إ߱m�i�����R�A��k
	public static RainbowBar newRainbowBarInstance(
			MySurfaceView mv,
    		float x2D, float y2D, //�m�i�������ù��������W����m�y��
    		float width2D, float height2D, //�m�i���b�ù������e�שM���׭�
    		float addedTouchScaleX, float addedTouchScaleY,//������Ĳ���d��
    		float gapRatio, //�_�إe�����
    		boolean fixXYRatioFlag, //�O�_�O���m�i������l���e��
    		Gravity gravity
    ){
		//�Y�G�ù��e�M���٨S���_�l�ơA�ߥX�ҥ~
		if(Constant.SCREEN_HEIGHT == 0 || Constant.SCREEN_WIDTH == 0){
			throw new RuntimeException("Constant.SCREEN_HEIGHT or Constant.SCREEN_WIDTH has not been initialized !");
		}
		//�_�l�ƭ�l�̤ؤo
		initOriginalScreenScale();
		
		//���ܭ�l�m�i�������e��A�H�A�����ù�
		if(fixXYRatioFlag == false){
			//�ھڭ�l�̤��m�i����2D�y�СA�p����
			calTargetToFillScreen(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY);	
		}
		else{//�O����l�m�i�����e��A���A�����ù��A�N�m�i���Y��øm��
			calTargetToFixXY(x2D, y2D, width2D, height2D, addedTouchScaleX, addedTouchScaleY, gravity);
		}
		
		//�إߥت��̤W���m�i������A�öǦ^
		return new RainbowBar(
				mv,
				targetX, targetY,
				targetWidth, targetHeight,
				targetAddedTouchScaleX, targetAddedTouchScaleY,
				gapRatio
        );
	}
	
	//�_�l�ƭ�l�̤ؤo����k
	private static void initOriginalScreenScale(){
		//�ù��������A�_�l�ƫ������
		if(Constant.SCREEN_WIDTH < Constant.SCREEN_HEIGHT){
			originalScreenWidth = sSpWidth;
			originalScreenHeight = sSpHeight;
		}
		else{//�ù�����̡A�_�l�ƾ�̸��
			originalScreenWidth = sSpHeight;
			originalScreenHeight = sSpWidth;
		}
		//�p���l�̪����e��
		originalScreenRatio = originalScreenWidth/originalScreenHeight;
	}
	
	//�p��ت����s��m�M�ؤo�A���i�ܧΡA�H�A�����ù�����k
	private static void calTargetToFillScreen
	(
			float x2D, float y2D,
			float width2D, float height2D,
			float addedTouchScaleX2D, float addedTouchScaleY2D
	)
	{
		//�ھڭ�l�̩M�ت��̪��ؤo�A�p���Y����
		float xRatio = Constant.SCREEN_WIDTH/originalScreenWidth; //x��V�Y����
		float yRatio = Constant.SCREEN_HEIGHT/originalScreenHeight; //y��V�Y����
		
		//�ھڤ�ҭp��ت��̤����s����m�M�ؤo
		targetX = x2D * xRatio;
		targetY = y2D * yRatio;
		targetWidth = width2D * xRatio;
		targetHeight = height2D * yRatio;

		targetAddedTouchScaleX = addedTouchScaleX2D * xRatio;
		targetAddedTouchScaleY = addedTouchScaleY2D * yRatio;
	}
	
	//�p��ت����s��m�M�ؤo�A�O�����sx�By��Ҥ��ܡA�Y��m���A���A�����ù�����k
	private static void calTargetToFixXY
	(
			float x2D, float y2D, 
			float width2D, float height2D,
			float addedTouchScaleX2D, float addedTouchScaleY2D,
			Gravity gravity
	)
	{
		//�p��ت��̪����e��
		float targetScreenRatio = Constant.SCREEN_WIDTH /(float)Constant.SCREEN_HEIGHT;
		//�ƥ��Y���
		float screenHightRatio = Constant.SCREEN_HEIGHT/originalScreenHeight;//�ù�������
		float screenWidthRatio = Constant.SCREEN_WIDTH/originalScreenWidth;//�ù��e����
		//��ڥΨ쪺�Y���
		float ratio;
		//�Y�ت��̪����e��A���l�̪����e��j�A�Y���H�ù������񬰭�
		if(targetScreenRatio > originalScreenRatio){
			//��ڥΨ쪺�Y���H�������
			ratio = screenHightRatio;
			//��l�̵�����Y��᪺�e��
			float w0 = originalScreenWidth * ratio;
			//��l���Y��øm����A����I�]�ù����W���^�M�ت��̪����Ix��V���e�׮t
			float x0 = (Constant.SCREEN_WIDTH - w0)/2.0f;
			//�ھڤ�ҭp��ت��̤����s����m�M�ؤo
			if(gravity == Gravity.Center){//�m��
				targetX = x2D * ratio + x0;
			}
			else if(gravity == Gravity.Side){//�a��
				//�Y�G���s�b�ù�������A�۰ʻ���
				if(x2D < originalScreenWidth / 2){
					targetX = x2D * ratio;
				}
				//�Y�G���s�b�ù����k��A�۰ʻ��k
				else{
					targetX = x2D * ratio + 2 * x0;
				}
			}
			targetY = y2D * ratio;
			targetWidth = width2D * ratio;
			targetHeight = height2D * ratio;
			targetAddedTouchScaleX = addedTouchScaleX2D * ratio;
			targetAddedTouchScaleY = addedTouchScaleY2D * ratio;
		}
		else{//�Y�ت��̪����e��A���l�̪����e��p�ά۵��A�Y���H�ù��e���񬰭�
			//��ڥΨ쪺�Y���H�e�����
			ratio = screenWidthRatio;
			//��l�̵�����Y��᪺����
			float h0 = originalScreenHeight * ratio;
			//��l���Y��øm����A����I�]�ù����W���^�M�ت��̪����Iy��V�����׮t
			float y0 = (Constant.SCREEN_HEIGHT - h0)/2.0f;
			//�ھڤ�ҭp��ت��̤����s����m�M�ؤo
			targetX = x2D * ratio ;
			if(gravity == Gravity.Center){//�m��
				targetY = y2D * ratio+ y0;
			}
			else if(gravity == Gravity.Side){//�a��
				//�Y�G���s�b�ù����W��A�۰ʳ��ݹ��
				if(y2D < originalScreenHeight / 2){
					targetY = y2D * ratio;
				}
				//�Y�G���s�b�ù����U��A�۰ʩ��ݹ��
				else{
					targetY = y2D * ratio + 2 * y0;
				}
			}
			targetWidth = width2D * ratio;
			targetHeight = height2D * ratio;
			targetAddedTouchScaleX = addedTouchScaleX2D * ratio;
			targetAddedTouchScaleY = addedTouchScaleY2D * ratio;
		}
	}

	/*
	 * ����O����Ҧ��i�۾A���̮ɡA���O��V���C�|���A
	 */
	public static enum Gravity{
		Center,//�m��
		Side//�a��
	}
}
