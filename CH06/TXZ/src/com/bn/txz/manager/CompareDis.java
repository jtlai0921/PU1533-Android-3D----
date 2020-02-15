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
	public int compareTo(CompareDis another) {//我們需要從大到小排序，所以傳回值相反
		if(dis<another.dis)//-1表示小於
			return 1;
		if(dis>another.dis)//1表示大於
			return -1;
		return 0;
	}

}
