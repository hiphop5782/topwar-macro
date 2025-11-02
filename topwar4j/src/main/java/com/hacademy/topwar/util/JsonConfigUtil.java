package com.hacademy.topwar.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.ui.LogDialog;

import java.io.File;
import java.io.IOException;

public class JsonConfigUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File root = new File(System.getProperty("user.home"), "tw-macro");
    static {
    	if(!root.exists()) {
    		root.mkdirs();
    	}
    	
    	//mapper에 정의되지 않은 속성을 무시하도록 설정
    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T load(Class<T> clazz) {
    	String filename = clazz.getSimpleName().toLowerCase()+".json";
    	System.out.println("["+filename+"] 설정 로드 중...");
        File file = new File(root, filename);
        if (!file.exists()) {
        	System.out.println("["+filename+"] 설정 파일이 존재하지 않습니다");
            try {
                T instance = clazz.getDeclaredConstructor().newInstance();
                System.out.println("["+filename+"] 신규 설정 생성중...");
                save(instance); // 초기값 저장
                return instance;
            } catch (Exception e) {
                throw new RuntimeException("Failed to create default instance", e);
            }
        }

        try {
            return mapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config file", e);
        }
    }

    public static void save(Object config) {
    	String filename = config.getClass().getSimpleName().toLowerCase()+".json";
		LogUtils.println("["+filename+"] 저장중...");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(root, filename), config);
        	LogUtils.println("["+filename+"] 저장 성공");
        	LogUtils.println(config);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config file : ", e);
        }
    }
}
