package com.hacademy.topwar;

import java.util.ArrayList;
import java.util.List;

public class Test27_OCR결과보정 {
	public static void main(String[] args) {
		//데이터 설정
		List<String> list = new ArrayList<>();
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
		
		//[1] 이상 글자 치환
		List<String> replaceList = new ArrayList<>();
		for(String str : list) {
			replaceList.add(
				str
					.replace("M", "")
					.replace("O", "0").replace("D", "0").replace("Q","0")
					.replace("I", "1").replace("l", "1")
					.replace("Z", "2").replace("z", "2")
					.replace("B", "3").replace("E", "3")
					.replace("S", "5")
					.replace("b", "6")
					.replace(")", "7").replace("T", "7")
					.replace("A", "8")
					.replace("N", "9")
			);
		}
		
		//[2] 변환
		List<Double> cpList = new ArrayList<>();
		for(String str : replaceList) {
			cpList.add(Double.parseDouble(str));
		}
		
		//[3] 차이 구하기
		List<Double> diffList = new ArrayList<>();
		for(int i=0; i < cpList.size()-1; i++) {
			diffList.add(cpList.get(i) - cpList.get(i+1));
		}
		
		//출력
		for(int i=0; i < cpList.size(); i++) {
			System.out.println("["+i+"] cp = " + cpList.get(i));
			
			if(i < cpList.size()-1)
				System.out.println("     → gap = " + diffList.get(i));
		}
		
		//차이가 음수인 경우는 존재할 수 없으므로 발견되면 해당 숫자만큼 차이가 나는 곳이 존재한다는 의미
		for(int i=0; i < diffList.size(); i++) {
			double diff = diffList.get(i);
			if(diff < 0) {
				int pos = findErrorPosition(diffList, i, diff);
				if(pos == -1) continue;
				System.out.println("이상데이터 위치 = " + pos + "("+diffList.get(pos)+")");
				
				double gap = cpList.get(pos) - cpList.get(i);
				System.out.println("gap = " + gap);
				long digit = (long)Math.abs(gap);
				int digitCount = String.valueOf(digit).length();
				System.out.println((pos+1)+"부터 "+(i)+"까지 증가 처리");
				double offset = Math.pow(10, digitCount-1);
				System.out.println("offset = " + offset);
				
				boolean error = true;
				while(error) {
					for(int k=pos+1; k <= i; k++) {
						cpList.set(k, cpList.get(k) + offset);
					}
					for(int k=pos+1; k <= i+1; k++) {
						double d = cpList.get(k-1) - cpList.get(k);
						error = d < 0;
						diffList.set(k-1, d);
					}
					System.out.println("cpList = " + cpList);
					System.out.println("diffList = " + diffList);
				}
			}
		}
		
		System.out.println("<재출력>");
		//출력
		for(int i=0; i < cpList.size(); i++) {
			System.out.println("["+i+"] cp = " + cpList.get(i));
		}
	}
	
	public static int findErrorPosition(List<Double> list, int index, double diff) {
		for(int i=index-1; i >= 0; i--) {
			if(list.get(i) >= -diff) {
				return i;
			}
		}
		return -1;
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
	public static double fixed(String cpStr) {
		try {
			return Double.parseDouble(cpStr);
		}
		catch(NumberFormatException e) {
			return 10000d;
		}
	}
	public static double fixed(String cpStr, double prev, double next) {
		cpStr = deleteM(cpStr);
		try {//정상숫자
			double cp = Double.parseDouble(cpStr);
			System.out.println("[정상] cpStr = " + cpStr + " , cp = " + cp);
			if(next <= cp && cp <= prev) return cp;
			
			while(cp <= prev) cp -= 10d;
			while(cp >= next) cp += 10d;
			if(next <= cp && cp <= prev) return cp;
			
			while(cp <= prev) cp -= 1d;
			while(cp >= next) cp += 1d;
			return cp;
		}
		catch(NumberFormatException e) {
			System.out.println("[이상] cpStr = " + cpStr);
			//글자가 포함되거나 형식이 이상한 경우
			//[1] .M으로 끝나는 경우
			if(cpStr.endsWith(".")) 
				cpStr = cpStr.replace(".", "");
			//[2] 자주 틀리는 글자들 치환
			System.out.println(10.5 % 10);
			return 0d;
		}
	}
	
}
