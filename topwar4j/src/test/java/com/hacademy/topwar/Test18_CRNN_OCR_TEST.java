//package com.hacademy.topwar;
//
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.nio.file.Paths;
//
//import javax.imageio.ImageIO;
//
//import ai.djl.Model;
//import ai.djl.inference.Predictor;
//import ai.djl.modality.cv.Image;
//import ai.djl.modality.cv.ImageFactory;
//import ai.djl.ndarray.NDArray;
//import ai.djl.ndarray.NDList;
//import ai.djl.ndarray.NDManager;
//import ai.djl.ndarray.types.Shape;
//import ai.djl.translate.Batchifier;
//import ai.djl.translate.Translator;
//import ai.djl.translate.TranslatorContext;
//
//public class Test18_CRNN_OCR_TEST {
//	public static void main(String[] args) throws Exception {
//		Model model = Model.newInstance("crnn-model", "OnnxRuntime");
//		model.load(Paths.get("onnx/crnn_model.onnx")); // crnn_model.onnx가 들어있는 폴더
//		
//		// 이미지 불러오기
//		BufferedImage buf = ImageIO.read(new File("ocr/kartz/kartz-1-server.png"));
//		Image img = ImageFactory.getInstance().fromImage(resizeImage(buf, 320, 48));
//
//		// 전처리 & 추론
//		Translator<Image, NDArray> translator = new Translator<Image, NDArray>() {
//		    @Override
//		    public NDList processInput(TranslatorContext ctx, Image input) {
//		    	NDManager manager = ctx.getNDManager();
//
//		        BufferedImage bufferedImage = (BufferedImage) input.getWrappedImage(); // DJL 이미지 → BufferedImage
//		        int width = bufferedImage.getWidth();
//		        int height = bufferedImage.getHeight();
//
//		        float[] data = new float[3 * height * width];
//		        int index = 0;
//
//		        for (int y = 0; y < height; y++) {
//		            for (int x = 0; x < width; x++) {
//		                int pixel = bufferedImage.getRGB(x, y);
//		                Color color = new Color(pixel); // R, G, B 추출
//
//		                data[index++] = color.getRed() / 255.0f;
//		                data[index++] = color.getGreen() / 255.0f;
//		                data[index++] = color.getBlue() / 255.0f;
//		            }
//		        }
//
//		        // [1, 3, height, width] 형태로 NDArray 생성
//		        NDArray array = manager.create(data, new Shape(1, 3, height, width));
//		        System.out.println(array.getShape());
//		        return new NDList(array);
//		    }
//
//		    @Override
//		    public NDArray processOutput(TranslatorContext ctx, NDList list) {
//		        return list.singletonOrThrow();
//		    }
//
//		    @Override
//		    public Batchifier getBatchifier() {
//		        return null;
//		    }
//		};
//
//
//
//		try (Predictor<Image, NDArray> predictor = model.newPredictor(translator)) {
//		    NDArray outputRaw = predictor.predict(img);
//
//		    // DJL 메모리로 복사
//		    NDArray output = outputRaw.duplicate();  // <- 이거 중요!!
//
//		    System.out.println("출력 shape: " + output.getShape());
//		    System.out.println("데이터 타입: " + output.getDataType());
//		    //System.out.println(output.toDebugString(10, 10));  // 데이터 일부 출력
//		}
//
//	}
//	
//	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
//	    java.awt.Image tmp = originalImage.getScaledInstance(targetWidth, targetHeight, BufferedImage.SCALE_SMOOTH);
//	    BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
//
//	    Graphics2D g2d = resized.createGraphics();
//	    g2d.drawImage(tmp, 0, 0, null);
//	    g2d.dispose();
//
//	    return resized;
//	}
//
//}
