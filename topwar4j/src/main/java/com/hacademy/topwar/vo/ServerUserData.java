package com.hacademy.topwar.vo;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class ServerUserData {
	@JsonIgnore
	public static final int MAXIMUM_CP = 300;
	private long time;
	private int server;
	private float total;
	private float average;
	private List<Float> cpValues = new ArrayList<>();
	private List<String> cpList;
	private List<String> okList = new ArrayList<>();
	private List<String> nokList = new ArrayList<>();
	
	@JsonIgnore
	private transient final DecimalFormat fmt = new DecimalFormat("#,##0.00");
	
	public ServerUserData(int server, List<String> cpList) { 
		this.server = server;
		this.cpList = cpList;
		this.time = System.currentTimeMillis();
		this.analyze();
	};
	
	public void analyze() {
		if(cpList == null) return;
		
		for(int i=0; i < cpList.size(); i++) {
			try {
				String cur = cpList.get(i)
						.strip()
						.replace(" ", "")
						.replace("M", "");
				float value = Float.parseFloat(cur);
				if(value > MAXIMUM_CP) throw new Exception();
				
//				if(i > 0 && i < cpList.size()-1) {
//					String prev = cpList.get(i-1).replace("M", "");
//					String next = cpList.get(i+1).replace("M", "");
//					float prevValue = Float.parseFloat(prev);
//					float nextValue = Float.parseFloat(next);
//					if(!(prevValue <= value && value <= nextValue)) {
//						value = (prevValue + nextValue) / 2;
//					}
//				}
				
				total += value;
				okList.add(cpList.get(i)+"M");
			}
			catch(Exception e) {
				nokList.add(cpList.get(i));
				System.err.println("에러 : "+e.getMessage());
			}
		}
		
		average = total / okList.size();
	}
	public void saveToJson(File dir) throws StreamWriteException, DatabindException, IOException {
		File target = new File(dir, server+".json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(target, this);
	}
	public void saveToJson() throws StreamWriteException, DatabindException, IOException {
		File target = new File(System.getProperty("user.home"), "tw-macro/ocr/"+server+".json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(target, this);
	}
	
	public void print() {
		System.out.println("<"+server+" 분석 결과>");
		System.out.println("- 분석 정확도 : " + okList.size() + "% ("+okList.size()+"/"+nokList.size()+")");
		System.out.println("- 전투력 구간 : " + okList.get(0) + " ~ " + okList.get(okList.size()-1));
		System.out.println("- Top 100 평균 전투력 : " + fmt.format(average) + "M");
	}
	public void printAll() {
		System.out.println(cpList);
	}
	public void printError() {
		System.out.println(nokList);
	}
	public void printCorrect() {
		System.out.println(okList);
	}
}
