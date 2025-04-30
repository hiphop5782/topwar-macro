package com.hacademy.topwar;

import java.util.ArrayList;
import java.util.List;

public class Test27_OCR결과보정 {
	public static void main(String[] args) {
		//데이터 설정
		List<String> list = new ArrayList();
		list.addAll(List.of(
			"107M", "103M", "100M", "25.4M", "23.7M", "N1.1M", "20.7M", "A8M", "86.7M", "86.4M",
			"85.6M", "85.2M", "85.1M", "83.7M", "82.3M", "82.3M", "82.1M", "81.7M", "81.4M", "81.1M", "79.7M",
			"77.9M", "77.3M", "77.1M", "76.8M", "76.7M", "75.9M", "75.5M", "75.4M", "74.6M", "74.5M", "74.5M",
			"74.2M", "73.9M", "73.7M", "73.5M", "73.4M", "73.2M", "72.5M", "71.4M", "71.4M", "70.7M", "70.7M",
			"70.1M", "69.4M", "69.3M", "69.3M", "69.1M", "69M", "68.5M", "68.4M", "68.4M", "67.8M", "66.9M",
			"66.8M", "66.4M", "66.1M", "65.9M", "65.9M", "65.8M", "65.7M", "65.2M", "65M", "65M", "64.7M", "64.2M",
			"63.7M", "63.5M", "63.3M", "62.9M", "60.8M", "60.7M", "59.8M", "59.8M", "59.8M", "59.5M", "59.4M",
			"59.4M", "59.1M", "59M", "58.9M", "58.8M", "58.7M", "58.3M", "58.1M", "58.1M", "57.QM", "57.8M",
			"57.7M", "57.7M", "57.7M", "57.6M", "57.4M", "57.4M", "57.2M", "5).2M", "57.1M", "57M", "57M",
			"56.9M"
		));
		
		//보정
		//[1] 내림차순으로 - 문제가 있을 경우 가장 가까운 위 숫자를 보고 원인을 파악하여 고친다
		//[2] 오름차순으로 - 문제가 있을 경우 가장 가까운 아래 숫자를 보고 원인을 파악하여 고친다
//		List<String> once = new ArrayList<>();
//		once.add(fixed(list.get(0)));
//		for(int i=1; i < list.size-1(); i++) {
//			String cpStr = list.get(i);
//			String replaceStr = fixed(cpStr);
//		}
	}
	
	public static String deleteM(String origin) {
		return origin.replace("M", "");
	}
	public static double closestUpperValue(List<String> list, int index) {
		String str = list.get(index-1);
		try {
			return Double.parseDouble(deleteM(str));
		}
		catch(NumberFormatException e) {
			return closestUpperValue(list, index-1);
		}
	}
	public static double closestLowerValue(List<String> list, int index) {
		String str = list.get(index+1);
		try {
			return Double.parseDouble(deleteM(str));
		}
		catch(NumberFormatException e) {
			return closestUpperValue(list, index+1);
		}
	}
	
	public static boolean isCorrectCombatPower(String cpStr) {
		String regex = "^[1-9]([0-9]?){2}(\\.?[0-9])[MmTt]$";
		return cpStr.matches(regex);
	}
	
//	public static double fixed(String cpStr, double min, double max) {
//		try {
//			double cp = Double.parseDouble(deleteM(cpStr));
//		}
//		catch(NumberFormatException e) {
//			
//		}
//	}
	
}
