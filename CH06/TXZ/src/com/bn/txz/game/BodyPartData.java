package com.bn.txz.game;

//�C�Ӥ��󪺸��
public class BodyPartData 
{
	//�l���f�b�_�l�y�Шt���������I
	public float[] bdd={0,0,0};	
	//�l���f�b�����f�y�Шt��������
	public float[] py={0,0,0};
	//�l���f�b�����f�y�Шt��������
	public float[] xz={0,0,1,0};
	//�l���f�b�����f�y�Шt�������઺���U����
	public float[] pyfz={0,0,0};
	
	public void copyTo(BodyPartData other)
	{
		other.bdd[0]=this.bdd[0];
		other.bdd[1]=this.bdd[1];
		other.bdd[2]=this.bdd[2];
		
		other.py[0]=this.py[0];
		other.py[1]=this.py[1];
		other.py[2]=this.py[2];
		
		other.xz[0]=this.xz[0];
		other.xz[1]=this.xz[1];
		other.xz[2]=this.xz[2];
		other.xz[3]=this.xz[3];
		
		other.pyfz[0]=this.pyfz[0];
		other.pyfz[1]=this.pyfz[1];
		other.pyfz[2]=this.pyfz[2];
	}
}
