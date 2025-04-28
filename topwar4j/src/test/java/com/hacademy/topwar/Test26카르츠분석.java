package com.hacademy.topwar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacademy.topwar.util.OcrUtils;
import com.hacademy.topwar.vo.KartzUserData;

public class Test26카르츠분석 {
	public static void main(String[] args) throws IOException, InterruptedException {
		final File dir = new File("ocr/kartz");
		analyze(dir);
	}
	
	public static void analyze(File dir) throws StreamWriteException, DatabindException, IOException {
    	List<KartzUserData> list = new ArrayList<>();
    	for(int rank=101; rank <= 500; rank++) {
    		try {
    			KartzUserData data = new KartzUserData();
    			data.setRank(rank);
    			
    			File serverFile = new File(dir, "kartz-"+rank+"-server.png");
        		String serverResult = OcrUtils.doOcrFile(serverFile.toPath());
        		File stageFile = new File(dir, "kartz-"+rank+"-stage.png");
        		String stageResult = OcrUtils.doOcrFile(stageFile.toPath());
        		File damageFile = new File(dir, "kartz-"+rank+"-damage.png");
        		String damageResult = damageFile.exists() ? OcrUtils.doOcrFile(damageFile.toPath()) : null;
        		if(damageResult != null) {
        			damageResult = damageResult
        					.replace("..", ".")
        					.replace(" ", ".")
        					.replace("l", "1").replace("I", "1")
        					.toLowerCase();
        		}
        		
        		try { data.setServer(Integer.parseInt(serverResult)); } catch(Exception e) {e.printStackTrace();}
        		try { data.setStage(Integer.parseInt(stageResult)); } catch(Exception e) {e.printStackTrace();}
        		data.setDamage(damageResult);
        		
        		list.add(data);
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.writeValue(new File("ocr/kartz-rank.json"), list);
    	
    	for(KartzUserData data : list) {
    		System.out.println(data);
    	}
    }
}
