package com.github.diereicheerethons.autoprotectregion.aprregions;

import org.bukkit.World;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class APRRegion {
	
	private long maxXWidth = 20;
	private long maxZWidth = 20;
	
	private String wgRegionID;
	private World world = null;
	
	
	ArrayPointList allPoints = new ArrayPointList();
	ArrayPointList allBorderPoints = new ArrayPointList();
	ArrayPointList allBorderPointsSorted = new ArrayPointList();
	ArrayPointList edgePointsSorted = new ArrayPointList();
	
	public APRRegion(String regionID, World world, long maxXWidth, long maxZWidth){
		this.maxXWidth=maxXWidth;
		this.maxZWidth=maxZWidth;
		this.wgRegionID = regionID;
		this.world = world;
		APRRegionList.list.add(this);
	}
	
	public APRRegion(String regionID, World world){
		this(regionID, world,20L,20L);
	}
	
	public boolean addPoint(long x, long z, long y){
		return addPoint(new XZPoint(x,z,y), y);
	}
	
	public boolean addPoint(XZPoint point, long y){
		if(!pointInRange(point))
			return false;
		if(allPoints.addPoint(point))	
			return recalculateLists();
		else
			allPoints.getPointAt(point.getX(), point.getZ()).setY(y);
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

	protected boolean recalculateLists() {
		resetCalcLists();
		calculateBorderPoints();
		sortBorderPoints();
		filterBorderPoints();
		
		updateOrCreateWGRegion();
		return true;
	}
	
	private void updateOrCreateWGRegion() {
		RegionManager regionManager = WGBukkit.getRegionManager(world);
		try{
			ProtectedPolygonalRegion wgRegion = (ProtectedPolygonalRegion) regionManager.getRegionExact(wgRegionID);
			updateWGRegion(wgRegion);
		}catch(Exception e){
			createNewWGRegion();
		}
				
	}

	private void createNewWGRegion() {
		// TODO Auto-generated method stub
		
	}

	private void updateWGRegion(ProtectedPolygonalRegion wgRegion) {
		// TODO LOOOS!
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
		XZPoint lastPoint = new XZPoint(Long.MIN_VALUE,Long.MIN_VALUE, 0);
		long maximalZ = Long.MIN_VALUE;
		
		xLoop:for(long x=smallestX; x <= biggestX; x++){
			zLoop:for(long z=smallestZ; z<=biggestZ; z++){
				if(allBorderPoints.containsPoint(x, z)){
					
					if(lastPoint.getZ() > z)
						maximalZ = lastPoint.getZ();
					if(z == lastPoint.getZ())
						maximalZ = Long.MIN_VALUE;
					
					allBorderPointsSorted.addPoint(allBorderPoints.getPointAt(x, z));
					lastPoint = allBorderPoints.getPointAt(x, z);
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
					
					allBorderPointsSorted.addPoint(allBorderPoints.getPointAt(x, z));
					lastPoint = allBorderPoints.getPointAt(x, z);
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
	}

	private void filterBorderPoints() {
		// TODO Implement This!!!!
		for(XZPoint point: allBorderPointsSorted){
			edgePointsSorted.add(point);
		}
	}

	private void calculateBorderPoints(){
		long maxLenght = maxXWidth;
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



	
	public long getMaxXWidth() {
		return maxXWidth;
	}

	public void setMaxXWidth(long maxXWidth) {
		this.maxXWidth = maxXWidth;
	}

	public long getMaxZWidth() {
		return maxZWidth;
	}

	public void setMaxZWidth(long maxZWidth) {
		this.maxZWidth = maxZWidth;
	}
	

	public String getWgRegionID() {
		return wgRegionID;
	}

	public void setWgRegionID(String wgRegionID) {
		this.wgRegionID = wgRegionID;
	}
	
	public World getWorld(){
		return world;
	}
	
}
