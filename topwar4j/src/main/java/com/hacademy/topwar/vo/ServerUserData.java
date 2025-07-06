package com.hacademy.topwar.vo;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.util.CpValueManager;

import lombok.Data;

@Data
public class ServerUserData {
	private long time;
	private int server;
	private List<String> cpList;
	private List<String> okList = new ArrayList<>();
	private List<String> nokList = new ArrayList<>();
	
	@JsonIgnore
	private transient final DecimalFormat fmt = new DecimalFormat("#,##0.00");
	
	public ServerUserData() {}
	public ServerUserData(int server, List<String> cpList) { 
		this.server = server;
		this.cpList = cpList;
		this.time = System.currentTimeMillis();
		this.analyze();
	};
	
	public void analyze() {
		if(cpList == null) return;
		CpValueManager manager = new CpValueManager(cpList);
		//manager.adjust();
		this.okList = manager.getCpList().stream().map(cp->cp+"M").toList();
	}
	public void saveToJson(File dir) throws StreamWriteException, DatabindException, IOException {
		if(!dir.exists()) dir.mkdirs();
		File target = new File(dir, server+".json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(target, this);
		System.out.println(target.getAbsolutePath()+" 저장 완료");
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
