package com.hacademy.topwar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.hacademy.topwar.util.CpValueManager;

public class Test27_OCR결과보정2 {
	public static void main(String[] args) {
		//데이터 설정
		List<String> list = new ArrayList<>();
		list.addAll(List.of(
				"62.2M",
		        "57.7M",
		        "52.7M",
		        "51.2M",
		        "48.4M",
		        "48M",
		        "47.2M",
		        "46.3M",
		        "46.3M",
		        "45.8M",
		        "42.9M",
		        "42.8M",
		        "42.3M",
		        "42.2M",
		        "41.8M",
		        "40.6M",
		        "40.2M",
		        "39M",
		        "38.9M",
		        "38.9M",
		        "38.4M",
		        "37.9M",
		        "37.4M",
		        "37.3M",
		        "37.3M",
		        "37.1M",
		        "A6.6M",
		        "36.5M",
		        "34.7M",
		        "34.5M",
		        "34.2M",
		        "33.5M",
		        "33.4M",
		        "33.2M",
		        "33.2M",
		        "32.9M",
		        "32.5M",
		        "32.5M",
		        "32.3M",
		        "31.8M",
		        "31.6M",
		        "31.3M",
		        "31.2M",
		        "31.1M",
		        "31M",
		        "30.6M",
		        "30.6M",
		        "30.1M",
		        "30.1M",
		        "30M",
		        "30M",
		        "29.8M",
		        "29.3M",
		        "29.2M",
		        "29.1M",
		        "28.9M",
		        "28.6M",
		        "28.4M",
		        "28.2M",
		        "28M",
		        "27.9M",
		        "27.9M",
		        "27.7M",
		        "27.2M",
		        "27.1M",
		        "27M",
		        "26.6M",
		        "26.6M",
		        "26.5M",
		        "26.5M",
		        "26.3M",
		        "26.3M",
		        "26.2M",
		        "26.2M",
		        "26M",
		        "25.QM",
		        "25.QM",
		        "25.6M",
		        "25.6M",
		        "25.6M",
		        "25.3M",
		        "25.2M",
		        "25M",
		        "24.8M",
		        "24.5M",
		        "24.5M",
		        "24.4M",
		        "24.2M",
		        "24.1M",
		        "24.1M",
		        "24.1M",
		        "24M",
		        "23.9M",
		        "23.8M",
		        "23.7M",
		        "23.5M",
		        "23.5M",
		        "23.1M",
		        "22.9M",
		        "22.7M"
		));
		
		CpValueManager manager = new CpValueManager(list);
		manager.adjust();
		List<Double> cpList = manager.getCpList();
		for(int i=0; i < cpList.size(); i++) {
			System.out.println("["+i+"] cp = " + cpList.get(i));
		}
		
	}
}
