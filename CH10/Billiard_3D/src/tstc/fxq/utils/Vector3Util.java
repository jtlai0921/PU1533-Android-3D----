package tstc.fxq.utils;
//3D�y�Шt���k�V�q���u�����O
public class Vector3Util {
	//�p��T���γW��ƪk�V�q����k
	public static float[] calTriangleAverageNormal
	(//�T���ΤT�ӳ��I�y�СA�]�n�D�H�f�ɰw���U¶�^
			float x0,float y0,float z0,//A
			float x1,float y1,float z1,//B
			float x2,float y2,float z2 //C
	)
	{
		float[] a={x1-x0, y1-y0, z1-z0};//�V�qAB
		float[] b={x2-x1, y2-y1, z2-z1};//�V�qBC
		/*
		 * �p��T���Ϊ��k�V�q�A
		 * �V�qAB�BBC���e���A
		 * �YABC�T�I���f�ɰw���U¶�A
		 * �V�qAB�MBC�����۳s�A
		 * �hAB�e��BC�ұo�k�V�q����V�ŦX�k��w�h
		 */
		float[] c=crossTwoVectors(a,b);
		return normalizeVector(c);//�Ǧ^�W��ƫ᪺�k�V�q
	}
	//�p��W�@�����k�V�q����k
	public static float[] calConeNormal
	(//�T���ΤT�ӳ��I�y��
			float x0,float y0,float z0,//A�A�����I
			float x1,float y1,float z1,//B�A������W�@�I
			float x2,float y2,float z2 //C�A���I
	)
	{
		float[] a={x1-x0, y1-y0, z1-z0};//�V�qAB
		float[] b={x2-x1, y2-y1, z2-z1};//�V�qBC
		//���y�����󥭭�ABC���V�q
		float[] c=crossTwoVectors(a,b);
		//�Nb�Mc���e���A�o�X�Ҳy�V�qd
		float[] d=crossTwoVectors(b,c);
		return normalizeVector(d);//�Ǧ^�W��ƫ᪺�k�V�q
	}
	
	//�p��y�찼���k�V�q����k
	public static float[] calBallArmNormal
	(//�T���ΤT�ӳ��I�y��
			float r,float R,float hight,float scale,
			float x1,float y1,float z1,
			float x2,float y2,float z2,
			float angle		//�ثe�����׭�
	)
	{
		
		float[] a={0,1,0};//�y�줤�߬W���V�q
		float[] b={x2-x1,y2-y1,z2-z1};//�����y�찼�����V�q
		
		//���y�����󥭭�ABC���V�q
		float[] c=crossTwoVectors(b,a);
		//�Nb�Mc���e���A�o�X�Ҳy�V�qd
		float[] d=crossTwoVectors(b,c);
		return normalizeVector(d);//�Ǧ^�W��ƫ᪺�k�V�q
	}
	
	
	
	//�N�@�ӦV�q�W��ƪ���k
	public static float[] normalizeVector(float x, float y, float z){
		float mod=module(x,y,z);
		return new float[]{x/mod, y/mod, z/mod};//�Ǧ^�W��ƫ᪺�V�q
	}
	public static float[] normalizeVector(float [] vec){
		float mod=module(vec);
		return new float[]{vec[0]/mod, vec[1]/mod, vec[2]/mod};//�Ǧ^�W��ƫ᪺�V�q
	}
	//�N�@�s�զV�q�W��ƪ���k�A�}�C�������Ӽ����O3������
	public static void normalizeAllVectors(float[] allVectors){
		for(int i=0;i<allVectors.length;i+=3){
			float[] result=Vector3Util.normalizeVector(allVectors[i],allVectors[i+1],allVectors[i+2]);
			allVectors[i]=result[0];
			allVectors[i+1]=result[1];
			allVectors[i+2]=result[2];
		}
	}
	//�D�V�q���Ҫ���k
	public static float module(float x, float y, float z){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
	public static float module(float [] vec){
		return (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
	}
	//��ӦV�q�e������k
	public static float[] crossTwoVectors(float[] a, float[] b)
	{
		float x=a[1]*b[2]-a[2]*b[1];
		float y=a[2]*b[0]-a[0]*b[2];
		float z=a[0]*b[1]-a[1]*b[0];
		return new float[]{x, y, z};//�Ǧ^�e�����G
	}
	//��ӦV�q�I������k
	public static float dotTwoVectors(float[] a, float[] b)
	{
		return a[0]*b[0]+a[1]*b[1]+a[2]*b[2];//�Ǧ^�I�����G
	}
	//��ӦV�q�ۥ[����k
	public static float[] addVector(float[] a, float b[]){
		return new float[]{a[0]+b[0], a[1]+b[1], a[2]+b[2]};
	}
	//�D��ӦV�q��������k
	public static double calVector3Angrad(float[] a, float[] b){
		float[] aNormal=Vector3Util.normalizeVector(a);
		float[] bNormal=Vector3Util.normalizeVector(b);
		return Math.acos(Vector3Util.dotTwoVectors(aNormal, bNormal));
	}
}
