package com.hacademy.topwar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.hacademy.topwar.util.CpValueManager;

public class Test27_OCR결과보정3 {
	public static void main(String[] args) {
		//데이터 설정
		List<String> list = new ArrayList<>();
		list.addAll(List.of(
				
		));
		
		CpValueManager manager = new CpValueManager(list);
		manager.adjust();
		List<Double> cpList = manager.getCpList();
		for(int i=0; i < cpList.size(); i++) {
			System.out.println("["+i+"] cp = " + cpList.get(i));
		}
		
	}
}
