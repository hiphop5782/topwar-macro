package com.hacademy.topwar.util;
public class CaptureUtils {
//	private static Robot robot;
//	static {
//		try {
//			robot = new Robot();
//		}
//		catch(AWTException e) {
//			System.err.println(e.getMessage());
//		}
//	}
//	public static BufferedImage captureBfi(Rectangle rect) {
//		System.out.println("[Capture] " + rect);
//		return robot.createScreenCapture(rect);
//	}
//	public static void saveTempBfi(Rectangle rect) throws IOException {
//		String home = System.getProperty("user.dir");
//		ImageIO.write(captureBfi(rect), "png", new File(home, "images/temp/capture.png"));
//	}
//	public static Mat captureMat(Rectangle rect) throws IOException {
//		return bfi2mat(captureBfi(rect));
//	}
//	public static Mat captureMatGrayscale(Rectangle rect) throws IOException {
//		Mat origin = captureMat(rect);
//		Mat grayscale = new Mat(origin.size(), CV_8UC1);
//		cvtColor(origin, grayscale, COLOR_BGR2GRAY);
//		return grayscale;
//	}
//	public static Mat bfi2mat(BufferedImage bfi) throws IOException {
//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//	    ImageIO.write(bfi, "jpg", byteArrayOutputStream);
//	    byte[] data = byteArrayOutputStream.toByteArray();
//	    Mat mat = imdecode(new Mat(new BytePointer(data)), IMREAD_ANYDEPTH | IMREAD_ANYCOLOR);
//	    return mat;
//	}
}
