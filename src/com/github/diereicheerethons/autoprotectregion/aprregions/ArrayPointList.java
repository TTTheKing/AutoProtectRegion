package com.github.diereicheerethons.autoprotectregion.aprregions;

import java.util.ArrayList;

public class ArrayPointList extends ArrayList<XZPoint>{
	private static final long serialVersionUID = 1L;
	
	
	public boolean addPoint(XZPoint point){
		if(getPointAt(point.getX(), point.getZ()) == null){
			add(point);
			return true;
		}
		return false;
	}
	
	protected void addLoaded(XZPoint point){
		add(point);
	}
	
	public boolean containsPoint(long x, long z){
		if(getPointAt(x,z) != null)
			return true;
		return false;
	}
	
	public boolean containsPoint(XZPoint point) {
		return containsPoint(point.getX(),point.getZ());
	}

	public long getBiggestX(){
		if(this.size()<1)
			return Long.MIN_VALUE;
		long biggestX = this.get(0).getX();
		
		for(XZPoint actualPoint: this){
			long x = actualPoint.getX();
			if(x > biggestX){
				biggestX = x;
			}
		}
		
		return biggestX;
	}
	
	public long getSmallestX(){
		if(this.size()<1)
			return Long.MIN_VALUE;
		long smallestX = this.get(0).getX();
		
		for(XZPoint actualPoint: this){
			long x = actualPoint.getX();
			if(x < smallestX){
				smallestX = x;
			}
		}
		
		return smallestX;
	}
	
	public long getBiggestZ(){
		if(this.size()<1)
			return Long.MIN_VALUE;
		long biggestZ = this.get(0).getZ();
		
		for(XZPoint actualPoint: this){
			long z = actualPoint.getZ();
			if(z > biggestZ){
				biggestZ = z;
			}
		}
		
		return biggestZ;
	}
	
	public long getSmallestZ(){
		if(this.size()<1)
			return Long.MIN_VALUE;
		long smallestZ = this.get(0).getZ();
		
		for(XZPoint actualPoint: this){
			long z = actualPoint.getZ();
			if(z < smallestZ){
				smallestZ = z;
			}
		}
		
		return smallestZ;
	}
	
	public XZPoint getPointAt(long x, long z){
		for(XZPoint point: this){
			if(point.getX()==x)
				if(point.getZ()==z)
					return point;
		}
		return null;
	}
	
	public long getMaxY(){
		long max = Long.MIN_VALUE;
		for(XZPoint point: this){
			if(point.getMaxY() > max){
				max = point.getMaxY();
			}
		}
		return max;
	}
	
	public long getMinY(){
		long min = Long.MAX_VALUE;
		for(XZPoint point: this){
			if(point.getMinY() < min){
				min = point.getMinY();
			}
		}
		return min;
	}
}
