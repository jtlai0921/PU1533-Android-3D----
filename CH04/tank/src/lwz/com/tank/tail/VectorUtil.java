package lwz.com.tank.tail;

//�V�q�p���k���ʸ����O
public class VectorUtil 
{
	//�D��ӦV�q���e�n
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2)
	{		
		//�D�X��ӦV�q�e�n�V�q�bXYZ�b�����qABC
        float A=y1*z2-y2*z1;
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
		
		return new float[]{A,B,C};
	}
	
	//�V�q�W���
	public static float[] vectorNormal(float[] vector)
	{
		//�D�V�q����
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
	
	//�D��ӦV�q���I�n
	public static float dotProduct(float[] vec1,float[] vec2)
	{
		return
			vec1[0]*vec2[0]+
			vec1[1]*vec2[1]+
			vec1[2]*vec2[2];
		
	}   
	
	//�D�V�q����
	public static float mould(float[] vec)
	{
		return (float)Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
	}
	
	//�D��ӦV�q������
	public static float angle(float[] vec1,float[] vec2)
	{
		//���D�I�n
		float dp=dotProduct(vec1,vec2);
		//�A�D��ӦV�q����
		float m1=mould(vec1);
		float m2=mould(vec2);
		
		float acos=dp/(m1*m2);
		
		//���F�קK�p��~�t�a�Ӫ����D
		if(acos>1)
		{
			acos=1;
		}
		else if(acos<-1)
		{
			acos=-1;
		}
			
		return (float)Math.acos(acos);
	}
	
	
}
