package com.github.diereicheerethons.autoprotectregion.aprregions;

import java.util.ArrayList;

import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion.PointNotInRangeException;

public class Main {
	public static void main(String[] args) {
		
		int[][] map = {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,1,1,1,1,0,0,0,0,1,1,1,1,0,0,0,0,0},
					   {0,0,0,1,1,1,1,0,0,0,0,1,1,0,1,0,0,0,0,0},
					   {0,0,0,1,1,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0},
					   {0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,1,1,0},
					   {0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,1,1,0},
					   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
		
		
		printMap(map);
		
		APRRegion region = new APRRegion(null, "TEST", null, 20, 20, 0);
		
		for(int x = 0; x < 20; x++){
			for(int z = 0; z < 20; z++){
				if(map[x][z] == 1){
					try {
						region.addPoint(x, z, 0);
					} catch (PointNotInRangeException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		printMap(getMapFromList(region.allBorderPoints, 20));
		
		printSortedMap(getSortedMapFromList(region.allBorderPointsSorted, 20));
		
	}

	private static int[][] getMapFromList(ArrayList<XZPoint> list, int size) {
		int map[][] = new int[size][size];
		
		for(int x = 0; x < size; x++){
			for(int z = 0; z < size; z++){
				map[x][z] = 0;
			}
		}
		
		for(XZPoint point:list){
			map[(int) point.getX()][(int) point.getZ()] = 1;
		}
		return map;
	}
	
	private static int[][] getSortedMapFromList(ArrayList<XZPoint> list, int size) {
		int map[][] = new int[size][size];
		
		for(int x = 0; x < size; x++){
			for(int z = 0; z < size; z++){
				map[x][z] = 0;
			}
		}
		int counter = 0;
		for(XZPoint point:list){
			counter++;
			map[(int) point.getX()][(int) point.getZ()] = counter;
		}
		return map;
	}

	private static void printMap(int[][] map) {
		System.out.println("==================MAP====================");
		for(int x = 0; x < map[0].length; x++){
			for(int y = 0; y < map[0].length; y++){
				System.out.print(","+map[x][y]+"");
			}
			System.out.println("");
		}
	}
	
	private static void printSortedMap(int[][] map){
		System.out.println("==================MAP====================");
		for(int x = 0; x < map[0].length; x++){
			for(int y = 0; y < map[0].length; y++){
				System.out.print(","+map[x][y]);
				if(map[x][y]<10){
					System.out.print(" ");
				}
			}
			System.out.println("");
		}
	}
}
