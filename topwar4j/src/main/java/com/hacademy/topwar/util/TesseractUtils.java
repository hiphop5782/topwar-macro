package com.hacademy.topwar.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

public class TesseractUtils {
	private static FilenameFilter tifFilter = (dir,name)->name.endsWith(".tif");
	private static FilenameFilter pngFilter = (dir,name)->name.endsWith(".png");
	private static FilenameFilter boxFilter = (dir,name)->name.endsWith(".box");
	static {
		System.setProperty("tesseract.home", "C:\\Program Files\\Tesseract-OCR");
	}
	
	public static void createBoxFile(File target) {
		
	}
	public static void createBoxInDirectory(File dir) {
		File tesseractExe = new File(System.getProperty("tesseract.home"), "tesseract.exe");
		File[] files = dir.listFiles(tifFilter);
		System.out.println("→ 총 "+files.length+"개의 파일 탐지");
		
		for(File target : files) {
			String fileName = target.getName();
			String boxFileName = fileName.substring(0, fileName.lastIndexOf("."));			
			List<String> commands = List.of(
					tesseractExe.getAbsolutePath(), 
					target.getAbsolutePath(), 
					boxFileName, 
					"batch.nochop", 
					"makebox"
			);
			try {
				ProcessBuilder pb = new ProcessBuilder(commands);
				pb.directory(dir);
				Process process = pb.start();
				
				int exitCode = process.waitFor();
				if (exitCode == 0) {
                    System.out.println("✅ Successfully created " + boxFileName + ".box");
                } else {
                    System.err.println("❌ Failed to create box file for " + target.getName() + ". Exit code: " + exitCode);
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error executing Tesseract command: " + e.getMessage());
            }
		}
	}
}
