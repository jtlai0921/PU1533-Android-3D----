package com.bn.txz.manager;

public class CompareDis implements Comparable<CompareDis>
{
	public float dis;
	public int row;
	public int col;
	public CompareDis(float dis,int row,int col)
	{
		this.dis=dis;
		this.row=row;
		this.col=col;
	}
	@Override
	public int compareTo(CompareDis another) {//�ڭ̻ݭn�q�j��p�ƧǡA�ҥH�Ǧ^�Ȭۤ�
		if(dis<another.dis)//-1��ܤp��
			return 1;
		if(dis>another.dis)//1��ܤj��
			return -1;
		return 0;
	}

}
