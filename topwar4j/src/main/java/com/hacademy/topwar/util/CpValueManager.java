package com.hacademy.topwar.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CpValueManager {
	private List<String> strList;
	private List<Double> cpList;
	private List<Double> diffList;
	public CpValueManager(List<String> strList) {
		this.strList = strList;
		createSubList();
	}
	
	private void createSubList() {
		this.cpList = new ArrayList<>();
		
		for(String str : strList) {
			if(str.matches("7[0-9][0-9]M")) {
				str = "1" + str.substring(1);
			}
			cpList.add(
				Double.parseDouble(
					str.replace("M", "")
						.replace("O", "0").replace("D", "0").replace("Q","0")
						.replace("I", "1").replace("l", "1")
						.replace("Z", "2").replace("z", "2")
						.replace("B", "3").replace("E", "3")
						.replace("S", "5")
						.replace("b", "6")
						.replace(")", "7").replace("T", "7")
						.replace("A", "8")
						.replace("N", "9").replace("#", "9")
				)
			);
		}
				
		this.diffList = new ArrayList<>();
		
		for(int i=0; i < cpList.size()-1; i++) {
			diffList.add(
				cpList.get(i) - cpList.get(i+1)
			);
		}
	}
	
	public void adjust() {
		for(int i=0; i < diffList.size(); i++) {
			double diff = diffList.get(i);
			if(diff >= 0) continue;
			
			int pos = findErrorPositionFrom(i);
			double gap = cpList.get(pos) - cpList.get(i);//차이
			long digit = (long)Math.abs(gap);//gap의 정수부
			int digitCount = String.valueOf(digit).length();//digit의 자리수
			double offset = Math.pow(10, digitCount-1);//더할 값

			boolean error = true;
			while(error) {
				int count = 0;
				for(int k=pos+1; k <= i; k++) {
					cpList.set(k, cpList.get(k) + offset);
				}
				for(int k=pos+1; k <= i+1; k++) {
					double d = cpList.get(k-1) - cpList.get(k);
					if(d < 0) count++;
					diffList.set(k-1, d);
				}
//				System.out.println((pos+1)+" ~ " + (i));
//				System.out.println(cpList);
//				System.out.println(diffList);
//				System.out.println("count = " + count);
				error = count > 0;
			}
		}
	}
	
	private int findErrorPositionFrom(int index) {
		double diff = diffList.get(index);
		for(int i=index-1; i >= 0; i--) {
			if(cpList.get(i) >= -diff) {
				return i;
			}
		}
		return -1;
	}
}
