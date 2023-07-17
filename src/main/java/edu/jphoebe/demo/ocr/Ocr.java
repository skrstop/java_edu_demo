package edu.jphoebe.demo.ocr;

import net.sourceforge.tess4j.Tesseract;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Size;

import java.io.File;
import java.io.IOException;

import static org.bytedeco.opencv.global.opencv_core.bitwise_not;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;


/**
 * @author 蒋时华
 * @date 2023-04-06 21:38:44
 */
public class Ocr {

    //灰度处理
    public static void grayProcess(String filePath, String newPath) {
        Mat image = imread(filePath); // 加载图像
//        imshow("原图", image);

        //高斯滤波器（GaussianFilter）对图像进行平滑处理。
        //GaussianBlur(image, image, new Size(3, 3), 0);

        //这里可以new一个Mat来接收，也可以直接使用原来的image接收cvtColor(image, image, CV_BGRA2GRAY);
        Mat gray = new Mat();
        cvtColor(image, gray, CV_BGRA2GRAY);// 灰度化
//        imshow("灰度", gray);

        //medianBlur(gray, gray, 3);  //中值滤波

        Mat bin = new Mat();
        //第三参数thresh，要根据自己的实际情况改变大小调试打印看一下。一般取100-200
        threshold(gray, bin, 155, 255, THRESH_BINARY);// 二值化
//        imshow("二值化", bin);

        // 反色，即黑色变白色，白色变黑色
        bitwise_not(bin, bin);
        Mat corrode = new Mat();
        Mat expand = new Mat();
        Mat kelner = getStructuringElement(MORPH_RECT, new Size(3, 3), new Point(-1, -1));
        // 腐蚀
        erode(bin, corrode, kelner);
        // 膨胀
        dilate(corrode, expand, kelner);
        // 反色，既黑色变白色，白色变黑色
        bitwise_not(expand, expand);
//        imshow("膨胀", expand);

//        // 反色，即黑色变白色，白色变黑色
//        bitwise_not(bin, bin);

        //保存二值化图像
        imwrite(newPath, bin);

        waitKey(0);
    }

    public static void main(String[] args) throws IOException {

        Tesseract tessreact = new Tesseract();
//        tessreact.setDatapath("/Users/jphoebe/Downloads/traineddata");
        tessreact.setLanguage("eng");
        Tesseract tessreact2 = new Tesseract();
//        tessreact2.setDatapath("/Users/jphoebe/Downloads/traineddata");
        tessreact2.setLanguage("chi_sim");
        String result;
        String outputFilename = "/Users/jphoebe/Downloads/test.png";
        String outputFilenameNew = "/Users/jphoebe/Downloads/test-new.png";

        // 二值化处理
//        grayProcess(outputFilename, outputFilenameNew);

//        File imageFile = new File("/Users/jphoebe/Downloads/iShot_2023-04-06_22.06.03.png");
//        try {
//            result = "识别结果eng：" + tessreact.doOCR(imageFile);
//            System.out.println("文件eng：" + outputFilename + " " + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("文件eng：" + outputFilename + "  失败");
//        }
//        imageFile = new File("/Users/jphoebe/Downloads/iShot_2023-04-06_22.10.27.png");
//        try {
//            result = "识别结果eng：" + tessreact.doOCR(imageFile);
//            System.out.println("文件eng：" + outputFilename + " " + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("文件eng：" + outputFilename + "  失败");
//        }
//         imageFile = new File("/Users/jphoebe/Downloads/iShot_2023-04-06_22.06.12.png");
//        try {
//            result = "识别结果eng：" + tessreact.doOCR(imageFile);
//            System.out.println("文件eng：" + outputFilename + " " + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("文件eng：" + outputFilename + "  失败");
//        }
//         imageFile = new File("/Users/jphoebe/Downloads/iShot_2023-04-06_22.06.15.png");
//        try {
//            result = "识别结果eng：" + tessreact.doOCR(imageFile);
//            System.out.println("文件eng：" + outputFilename + " " + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("文件eng：" + outputFilename + "  失败");
//        }
//         imageFile = new File("/Users/jphoebe/Downloads/iShot_2023-04-06_22.06.19.png");
//        try {
//            result = "识别结果eng：" + tessreact.doOCR(imageFile);
//            System.out.println("文件eng：" + outputFilename + " " + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("文件eng：" + outputFilename + "  失败");
//        }
//         imageFile = new File("/Users/jphoebe/Downloads/iShot_2023-04-06_22.06.23.png");
//        try {
//            result = "识别结果eng：" + tessreact.doOCR(imageFile);
//            System.out.println("文件eng：" + outputFilename + " " + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("文件eng：" + outputFilename + "  失败");
//        }
//         imageFile = new File("/Users/jphoebe/Downloads/iShot_2023-04-06_22.06.27.png");
//        try {
//            result = "识别结果eng：" + tessreact.doOCR(imageFile);
//            System.out.println("文件eng：" + outputFilename + " " + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("文件eng：" + outputFilename + "  失败");
//        }


        File imageFile = new File("/Users/jphoebe/Downloads/test-new.png");
        try {
            result = "识别结果chi：" + tessreact2.doOCR(imageFile);
            System.out.println("文件chi：" + outputFilename + " " + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件chi：" + outputFilename + "  失败");
        }

    }

}
