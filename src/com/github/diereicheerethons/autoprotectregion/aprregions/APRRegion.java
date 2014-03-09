package com.github.diereicheerethons.autoprotectregion.aprregions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import com.github.diereicheerethons.autoprotectregion.APR;
import com.github.diereicheerethons.autoprotectregion.AutoProtectRegion;
import com.github.diereicheerethons.autoprotectregion.Translator;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException;

public class APRRegion {

	private long maxXWidth = 20;
	private long maxZWidth = 20;
	private long maxYWidth = 15;

	private String wgRegionID;
	private World world = null;
	private OfflinePlayer owner;

	private ArrayList<OfflinePlayer> invites = new ArrayList<OfflinePlayer>();

	ArrayPointList allPoints = new ArrayPointList();
	ArrayPointList allBorderPoints = new ArrayPointList();
	ArrayPointList allBorderPointsSorted = new ArrayPointList();
	ArrayPointList edgePointsSorted = new ArrayPointList();

	public APRRegion(OfflinePlayer owner, String regionID, World world,
			long maxXWidth, long maxZWidth, long maxYWidth) {
		this.maxXWidth = maxXWidth;
		this.maxZWidth = maxZWidth;
		this.wgRegionID = regionID;
		this.world = world;
		this.owner = owner;
		this.maxYWidth = maxYWidth;
		APRRegionList.list.add(this);
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

	public APRRegion(OfflinePlayer owner, String regionID, World world) {
		this(owner, regionID, world, 20L, 20L, 15L);
	}

	public boolean addPoint(long x, long z, long y)
			throws PointNotInRangeException, PointInBlacklistException,
			PointNotInWhitelistException {
		return addPoint(new XZPoint(x, z, y), y);
	}

	public class PointNotInRangeException extends Exception {
		private static final long serialVersionUID = 1L;

		public PointNotInRangeException(String text) {
			super(text);
		}

	}

	public class PointInBlacklistException extends Exception {
		private static final long serialVersionUID = 1L;

		public PointInBlacklistException(String text) {
			super(text);
		}

	}

	public class PointNotInWhitelistException extends Exception {
		private static final long serialVersionUID = 1L;

		public PointNotInWhitelistException(String text) {
			super(text);
		}

	}

	public boolean addPoint(XZPoint point, long y)
			throws PointNotInRangeException, PointInBlacklistException,
			PointNotInWhitelistException {
		if (!pointInRange(point, y))
			throw new PointNotInRangeException(point.getX() + "," + y + ","
					+ point.getZ());
		if (pointInBlacklist(point, y))
			throw new PointInBlacklistException(point.getX() + "," + y + ","
					+ point.getZ());
		if (!pointInWhitelist(point, y))
			throw new PointNotInWhitelistException(point.getX() + "," + y + ","
					+ point.getZ());
		if (!allPoints.addPoint(point))
			allPoints.getPointAt(point.getX(), point.getZ()).setY(y);
			
		if (APR.config.getInt("calculationVersion") == 2) {
			return recalculateListsV2();
		} else if (APR.config.getInt("calculationVersion") == 3) {
			return recalculateLists();
		} else
			return recalculateLists();
	}

	private boolean pointInWhitelist(XZPoint point, long y) {
		if (APR.config.getStringList("wgRegionWhitelist").isEmpty())
			return true;
		List<String> whiteList = APR.config.getStringList("wgRegionWhitelist");
		return pointInList(point, y, whiteList);
	}

	private boolean pointInBlacklist(XZPoint point, long y) {
		if (APR.config.getStringList("wgRegionBlacklist").isEmpty())
			return false;
		List<String> blacklist = APR.config.getStringList("wgRegionBlacklist");
		return pointInList(point, y, blacklist);
	}

	public boolean pointInList(XZPoint point, long y, List<String> list) {
		RegionManager regionManager = WGBukkit.getRegionManager(world);

		long x = point.getX();
		long z = point.getZ();

		for (String rgName : list) {
			ProtectedRegion region = regionManager.getRegionExact(rgName);
			if (region == null) {
				continue;
			}
			if (region.contains((int) x, (int) y, (int) z)) {
				return true;
			}
		}

		return false;
	}

	private boolean pointInRange(XZPoint point, long y) {
		if (allPoints.size() == 0)
			return true;
		if (Math.abs(allPoints.getBiggestX() - point.getX()) > maxXWidth)
			return false;
		if (Math.abs(allPoints.getSmallestX() - point.getX()) > maxXWidth)
			return false;
		if (Math.abs(allPoints.getBiggestZ() - point.getZ()) > maxZWidth)
			return false;
		if (Math.abs(allPoints.getSmallestZ() - point.getZ()) > maxZWidth)
			return false;
		if (Math.abs(allPoints.getMaxY() - y) > maxYWidth)
			return false;
		if (Math.abs(allPoints.getMinY() - y) > maxYWidth)
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

	protected boolean recalculateListsV2() {
		resetCalcLists();
		calculateBorderPoints();
		sortBorderPointsV2();
		filterBorderPoints();

		updateOrCreateWGRegion();
		return true;
	}

	private void updateOrCreateWGRegion() {
		RegionManager regionManager = WGBukkit.getRegionManager(world);
		try {
			ProtectedPolygonalRegion wgRegion = (ProtectedPolygonalRegion) regionManager
					.getRegionExact(wgRegionID);
			if (wgRegion == null) {
				createNewWGRegion(regionManager);
				return;
			}
			updateWGRegion(wgRegion, regionManager);
			return;
		} catch (Exception e) {
			createNewWGRegion(regionManager);
			return;
		}

	}

	private void createNewWGRegion(RegionManager regionManager) {
		List<BlockVector2D> points = new ArrayList<BlockVector2D>();
		for (XZPoint point : edgePointsSorted) {
			points.add(new BlockVector2D(point.getX(), point.getZ()));
		}
		ProtectedPolygonalRegion newWGRegion = new ProtectedPolygonalRegion(
				wgRegionID, points, (int) allPoints.getMinY(),
				(int) allPoints.getMaxY());

		DefaultDomain domain = new DefaultDomain();
		domain.addPlayer(owner.getName());
		newWGRegion.setOwners(domain);
		newWGRegion.setPriority(AutoProtectRegion.config
				.getInt("defaultRegionSettings.priority"));
		Map<Flag<?>, Object> flags = APR.config.getDefaultRegionFlags(world);
		newWGRegion.setFlags(flags);

		regionManager.addRegion(newWGRegion);
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			e.printStackTrace();
			AutoProtectRegion.instance.getServer().getConsoleSender()
				.sendMessage(ChatColor.RED + "[APR] [ERROR]: "
				+ Translator.translate("failedSaving")+ "!");
		}
	}

	private void updateWGRegion(ProtectedPolygonalRegion oldWGRegion,
			RegionManager regionManager) {
		List<BlockVector2D> points = new ArrayList<BlockVector2D>();
		for (XZPoint point : edgePointsSorted) {
			points.add(new BlockVector2D(point.getX(), point.getZ()));
		}
		ProtectedPolygonalRegion newWGRegion = new ProtectedPolygonalRegion(
				wgRegionID, points, (int) allPoints.getMinY(),
				(int) allPoints.getMaxY());

		newWGRegion.setMembers(oldWGRegion.getMembers());
		newWGRegion.setOwners(oldWGRegion.getOwners());
		newWGRegion.setFlags(oldWGRegion.getFlags());
		newWGRegion.setPriority(oldWGRegion.getPriority());
		try {
			newWGRegion.setParent(oldWGRegion.getParent());
		} catch (CircularInheritanceException ignore) {
			// This should not be thrown
		}

		regionManager.addRegion(newWGRegion);
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			e.printStackTrace();
			AutoProtectRegion.instance.getServer().getConsoleSender()
					.sendMessage(ChatColor.RED + "[APR] [ERROR]: "
					+ Translator.translate("failedSaving")+ "!");
		}
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
		// ArrayPointList tempList;
		XZPoint lastPoint = new XZPoint(Long.MIN_VALUE, Long.MIN_VALUE, 0);
		long maximalZ = Long.MIN_VALUE;

		xLoop: for (long x = smallestX; x <= biggestX; x++) {
			zLoop: for (long z = smallestZ; z <= biggestZ; z++) {
				if (allBorderPoints.containsPoint(x, z)) {

					if (lastPoint.getZ() > z)
						maximalZ = lastPoint.getZ();
					if (z == lastPoint.getZ())
						maximalZ = Long.MIN_VALUE;

					allBorderPointsSorted.addPoint(allBorderPoints.getPointAt(
							x, z));
					lastPoint = allBorderPoints.getPointAt(x, z);
					if (!isFirst)
						if ((!allBorderPoints.containsPoint(x + 1, z))
								&& (allBorderPoints.containsPoint(x, z + 1)))
							continue zLoop;
					isFirst = false;
					continue xLoop;
				}

				if (z == maximalZ) {
					continue xLoop;
				}

			}
		}
		isFirst = true;
		xLoop: for (long x = biggestX; x >= smallestX; x--) {
			zLoop: for (long z = biggestZ; z >= smallestZ; z--) {
				if (allBorderPoints.containsPoint(x, z)) {

					if (lastPoint.getZ() < z)
						maximalZ = lastPoint.getZ();
					if (z == lastPoint.getZ())
						maximalZ = Long.MIN_VALUE;

					allBorderPointsSorted.addPoint(allBorderPoints.getPointAt(
							x, z));
					lastPoint = allBorderPoints.getPointAt(x, z);
					if (!isFirst)
						if ((!allBorderPoints.containsPoint(x - 1, z))
								&& (allBorderPoints.containsPoint(x, z - 1)))
							continue zLoop;
					isFirst = false;
					continue xLoop;
				}
				// TODO TEST THIS HERE
				if (maximalZ == z) {
					continue xLoop;
				}
			}
		}
	}

	private void sortBorderPointsV2() {
		long biggestX = allBorderPoints.getBiggestX();
		long biggestZ = allBorderPoints.getBiggestZ();
		long smallestX = allBorderPoints.getSmallestX();
		long smallestZ = allBorderPoints.getSmallestZ();

		long middleX = Math.round((biggestX - smallestX) / 2.0);
		long middleZ = Math.round((biggestZ - smallestZ) / 2.0);

		ArrayPointList tmpallBorderPoints = new ArrayPointList();
		for (XZPoint point : allBorderPoints) {
			tmpallBorderPoints.add(point);
		}

		while (tmpallBorderPoints.size() > 0) {
			double maxAngle = Double.MIN_VALUE;
			ArrayPointList maxAnglePoints = new ArrayPointList();
			for (XZPoint point : tmpallBorderPoints) {
				double deltaZ = middleZ - point.getZ();
				double deltaX = middleX - point.getX();
				double angle = Math.atan2(deltaZ, deltaX) * 180 / Math.PI;

				if (angle > maxAngle) {
					maxAngle = angle;
					maxAnglePoints = new ArrayPointList();
					maxAnglePoints.add(point);
				}
				if (angle == maxAngle) {
					maxAnglePoints.add(point);
				}
			}

			for (XZPoint point : maxAnglePoints) {
				long x = point.getX();
				long z = point.getZ();
				allBorderPointsSorted
						.addPoint(allBorderPoints.getPointAt(x, z));
				tmpallBorderPoints.remove(maxAnglePoints.getPointAt(x, z));
			}
		}
	}

	private void filterBorderPoints() {
		// TODO Implement This!!!!
		for (XZPoint point : allBorderPointsSorted) {
			edgePointsSorted.add(point);
		}
	}

	private void calculateBorderPoints() {
		long maxLenght = maxXWidth;
		if (maxZWidth > maxXWidth)
			maxLenght = maxZWidth;
		for (XZPoint point : allPoints) {
			long x = point.getX();
			long z = point.getZ();
			boolean isBorderXPlus = true;
			boolean isBorderXMinus = true;
			boolean isBorderZPlus = true;
			boolean isBorderZMinus = true;
			for (int abweichung = 1; abweichung <= maxLenght; abweichung++) {
				if (allPoints.containsPoint(x + abweichung, z))
					isBorderXPlus = false;
				if (allPoints.containsPoint(x - abweichung, z))
					isBorderXMinus = false;
				if (allPoints.containsPoint(x, z + abweichung))
					isBorderZPlus = false;
				if (allPoints.containsPoint(x, z - abweichung))
					isBorderZMinus = false;
			}
			if (isBorderXPlus || isBorderZPlus || isBorderXMinus
					|| isBorderZMinus)
				if (!allBorderPoints.containsPoint(point.getX(), point.getZ()))
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

	public long getMaxYWidth() {
		return maxYWidth;
	}

	public void setMaxYWidth(long maxYWidth) {
		this.maxYWidth = maxYWidth;
	}

	public String getWgRegionID() {
		return wgRegionID;
	}

	public void setWgRegionID(String wgRegionID) {
		this.wgRegionID = wgRegionID;
	}

	public World getWorld() {
		return world;
	}

	public ArrayList<OfflinePlayer> getInvites() {
		return invites;
	}

}
