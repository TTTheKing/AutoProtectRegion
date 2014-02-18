package com.github.diereicheerethons.autoprotectregion.aprregions;

public class XZPoint {
	
	private long x,z;
	
	private boolean serachVisited = false;
	
	XZPoint(long x, long y){
		this.z=y;
		this.x=x;
	}
	
	public long getX(){
		return x;
	}
	
	public long getZ(){
		return z;
	}

	public boolean isSerachVisited() {
		return serachVisited;
	}

	public void setSerachVisited(boolean serachVisited) {
		this.serachVisited = serachVisited;
	}
}
