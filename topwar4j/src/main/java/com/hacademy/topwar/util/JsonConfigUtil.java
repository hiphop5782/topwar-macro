package com.hacademy.topwar.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonConfigUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File root = new File(System.getProperty("user.home"), "tw-macro");

    public static <T> T load(Class<T> clazz) {
    	String filename = clazz.getSimpleName().toLowerCase()+".json";
        File file = new File(root, filename);
        if (!file.exists()) {
            try {
                T instance = clazz.getDeclaredConstructor().newInstance();
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
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(root, filename), config);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config file", e);
        }
    }
}
