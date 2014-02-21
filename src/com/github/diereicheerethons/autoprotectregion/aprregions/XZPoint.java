package com.github.diereicheerethons.autoprotectregion.aprregions;

public class XZPoint {
	
	private long x,z;
	
	private long maxY;
	private long minY;
	
	private boolean serachVisited = false;
	
	XZPoint(long x, long z, long y){
		this.z=z;
		this.x=x;
		
		this.maxY = y;
		this.minY = y;
	}
	
	XZPoint(long x, long z, long maxy, long miny){
		this.z=z;
		this.x=x;
		
		this.maxY = maxy;
		this.minY = miny;
	}
	
	public void setY(long y) {
		if(y > maxY)
			maxY = y;
		if(y < minY)
			minY = y;
	}

	public long getX(){
		return x;
	}
	
	public long getZ(){
		return z;
	}
	
	public long getMinY(){
		return minY;
	}
	
	public long getMaxY(){
		return maxY;
	}

	public boolean isSerachVisited() {
		return serachVisited;
	}

	public void setSerachVisited(boolean serachVisited) {
		this.serachVisited = serachVisited;
	}
}
