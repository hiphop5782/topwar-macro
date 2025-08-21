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
			try {
				cpList.add(
						Double.parseDouble(
							str.replace("M", "")
								.replace("O", "0").replace("D", "0").replace("Q","0")
								.replace("I", "1").replace("l", "1").replace("]", "1")
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
			catch(Exception e) {
				System.err.println("숫자 변환 오류 : " + e.getMessage());
			}
		}
				
		this.diffList = new ArrayList<>();
		
		for(int i=0; i < cpList.size()-1; i++) {
			diffList.add(
				cpList.get(i) - cpList.get(i+1)
			);
		}
	}
	
//	소수점이 사라지는 경우만 처리
//	소수점은 100M 미만에서 발생하며 소수점이 사라지는 경우 숫자가 3자리로 변경됨
//	앞이나 뒷 데이터보다 5배 이상 커지면 99%확률로 소수점이 사라졌다고 판정
	public void adjust() { adjust(1); }
	public void adjust(int count) {
		final int FIRST = 0;
		for(int k=0; k < count; k++) {
			for(int i=0; i < cpList.size(); i++) {
				//처음이나 마지막인 경우는 따로 처리
				double origin = cpList.get(i);
				double replacement;
				if(i == FIRST) 
					replacement = adjustFirstValue(cpList.get(i), cpList.get(i+1));
				else
					replacement = adjustValue(cpList.get(i-1), cpList.get(i));
			
				if(origin != replacement) {
					cpList.set(i, replacement);
					System.out.println("<CP조정> origin : "+origin+", replacement : " + replacement);
				}
			}
		}
	}
	
	private double adjustFirstValue(double value, double next) {
		System.out.println("→ 맨 앞자리 = " + value);
		Double current = Double.valueOf(value);
		if(next >= 100 && current.intValue() / 100 == 7) {//7xx인 경우 (1xx의 오인식)
			return value - 600d;
		}
		
		if(value >= 400) {
			return value / 10;
		}
		return value;
	}
	private double adjustValue(double prev, double value) {
		System.out.println("→ prev = " + prev+", current = " + value);
		if(value > prev * 5) 
			return value / 10;
		return value;
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
