package com.github.diereicheerethons.autoprotectregion.aprregions;

public class APRRegion {
	
	private int maxXWidth = 20;
	private int maxZWidth = 20;
	
	private String wgRegionID;
	
	ArrayPointList allPoints = new ArrayPointList();
	ArrayPointList allBorderPoints = new ArrayPointList();
	ArrayPointList allBorderPointsSorted = new ArrayPointList();
	ArrayPointList edgePointsSorted = new ArrayPointList();
	
	public APRRegion(String regionID, int maxXWidth, int maxZWidth){
		this.maxXWidth=maxXWidth;
		this.maxZWidth=maxZWidth;
		this.wgRegionID = regionID;
	}
	
	public APRRegion(String regionID){
		this(regionID,20,20);
	}
	
	public boolean addPoint(long x, long z){
		return addPoint(new XZPoint(x,z));
	}
	
	public boolean addPoint(XZPoint point){
		if(!pointInRange(point))
			return false;
		if(allPoints.addPoint(point))	
			return recalculateLists();
		
		return false;
	}
	
	
	
	private boolean pointInRange(XZPoint point) {
		if(allPoints.size() == 0)
			return true;
		if(Math.abs(allPoints.getBiggestX() - point.getX()) > maxXWidth)
			return false;
		if(Math.abs(allPoints.getSmallestX() - point.getX()) > maxXWidth)
			return false;
		if(Math.abs(allPoints.getBiggestZ() - point.getZ()) > maxZWidth)
			return false;
		if(Math.abs(allPoints.getBiggestZ() - point.getZ()) > maxZWidth)
			return false;
		return true;
	}

	private boolean recalculateLists() {
		resetCalcLists();
		calculateBorderPoints();
		sortBorderPoints();
		filterBorderPoints();
		return true;
	}
	
	private void resetCalcLists() {
		allBorderPoints = new ArrayPointList();
		allBorderPointsSorted = new ArrayPointList();
		edgePointsSorted = new ArrayPointList();
	}

	private void sortBorderPoints() {
		long biggestX = allBorderPoints.getBiggestX();
		long biggestZ = allBorderPoints.getBiggestZ();
		long smallestX = allBorderPoints.getSmallestX();
		long smallestZ = allBorderPoints.getSmallestZ();
		boolean isFirst = true;
		//ArrayPointList tempList;
		XZPoint lastPoint = new XZPoint(Long.MIN_VALUE,Long.MIN_VALUE);
		long maximalZ = Long.MIN_VALUE;
		
		xLoop:for(long x=smallestX; x <= biggestX; x++){
			zLoop:for(long z=smallestZ; z<=biggestZ; z++){
				if(allBorderPoints.containsPoint(x, z)){
					
					if(lastPoint.getZ() > z)
						maximalZ = lastPoint.getZ();
					if(z == lastPoint.getZ())
						maximalZ = Long.MIN_VALUE;
					
					allBorderPointsSorted.addPoint(new XZPoint(x,z));
					lastPoint = new XZPoint(x,z);
					if(!isFirst)
						if((!allBorderPoints.containsPoint(x+1, z))
								&&(allBorderPoints.containsPoint(x, z+1)))
							continue zLoop;
					isFirst = false;	
					continue xLoop;
				}
				
				if(z == maximalZ){
					continue xLoop;
				}
				
			}
		}
		isFirst = true;
		xLoop:for(long x = biggestX; x >= smallestX; x--){
			zLoop:for(long z = biggestZ; z >= smallestZ; z--){
				if(allBorderPoints.containsPoint(x, z)){
					
					if(lastPoint.getZ() < z)
						maximalZ = lastPoint.getZ();
					if(z == lastPoint.getZ())
						maximalZ = Long.MIN_VALUE;
					
					allBorderPointsSorted.addPoint(new XZPoint(x,z));
					lastPoint = new XZPoint(x,z);
					if(!isFirst)
						if((!allBorderPoints.containsPoint(x-1, z))
								&&(allBorderPoints.containsPoint(x, z-1)))
							continue zLoop;
					isFirst = false;
					continue xLoop;
				}
				//TODO TEST THIS HERE
				if(maximalZ == z){
					continue xLoop;
				}
			}
		}
		/*
			tempList = new ArrayPointList();
			for(XZPoint point:allBorderPoints){
				if(point.getX() == x){
					tempList.addPoint(point);
				}
			}
			if(tempList.size()>0){
				long z = tempList.getSmallestZ();
				allBorderPointsSorted.add(new XZPoint(x,z));
			}
		}
		
		for(long z=smallestZ; z <= biggestZ; z++){
			tempList = new ArrayPointList();
			for(XZPoint point:allBorderPoints){
				if(point.getZ() == z){
					tempList.addPoint(point);
				}
			}
			if(tempList.size()>0){
				long x = tempList.getBiggestX();
				allBorderPointsSorted.add(new XZPoint(x,z));
			}
		}
		
		for(long x=biggestX; x >= smallestX; x--){
			tempList = new ArrayPointList();
			for(XZPoint point:allBorderPoints){
				if(point.getX() == x){
					tempList.addPoint(point);
				}
			}
			if(tempList.size()>0){
				long z = tempList.getBiggestZ();
				allBorderPointsSorted.add(new XZPoint(x,z));
			}
		}
		
		for(long z=biggestZ; z >= smallestZ; z--){
			tempList = new ArrayPointList();
			for(XZPoint point:allBorderPoints){
				if(point.getZ() == z){
					tempList.addPoint(point);
				}
			}
			if(tempList.size()>0){
				long x = tempList.getSmallestX();
				allBorderPointsSorted.add(new XZPoint(x,z));
			}
		}
		*/
	}

	private void filterBorderPoints() {
		// TODO Implement This!!!!
		for(XZPoint point: allBorderPointsSorted){
			edgePointsSorted.add(point);
		}
	}

	private void calculateBorderPoints(){
		int maxLenght = maxXWidth;
		if(maxZWidth>maxXWidth)
			maxLenght = maxZWidth;
		for(XZPoint point: allPoints){
			long x = point.getX();
			long z = point.getZ();
			boolean isBorderXPlus = true;
			boolean isBorderXMinus = true;
			boolean isBorderZPlus = true;
			boolean isBorderZMinus = true;
			for(int abweichung = 1; abweichung <= maxLenght;abweichung++){
				if(allPoints.containsPoint(x+abweichung, z))
					isBorderXPlus = false;
				if(allPoints.containsPoint(x-abweichung, z))
					isBorderXMinus = false;
				if(allPoints.containsPoint(x, z+abweichung))
					isBorderZPlus = false;
				if(allPoints.containsPoint(x, z-abweichung))
					isBorderZMinus = false;
			}
			if(isBorderXPlus || isBorderZPlus || isBorderXMinus || isBorderZMinus)
				if(!allBorderPoints.containsPoint(point.getX(),point.getZ()))
					allBorderPoints.add(point);
		}
	}



	
	public int getMaxXWidth() {
		return maxXWidth;
	}

	public void setMaxXWidth(int maxXWidth) {
		this.maxXWidth = maxXWidth;
	}

	public int getMaxZWidth() {
		return maxZWidth;
	}

	public void setMaxZWidth(int maxZWidth) {
		this.maxZWidth = maxZWidth;
	}
	

	public String getWgRegionID() {
		return wgRegionID;
	}

	public void setWgRegionID(String wgRegionID) {
		this.wgRegionID = wgRegionID;
	}
	
}
