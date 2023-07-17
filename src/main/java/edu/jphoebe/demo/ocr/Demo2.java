package edu.jphoebe.demo.ocr;

import cn.hutool.core.date.LocalDateTimeUtil;
import net.sourceforge.tess4j.Tesseract;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author 蒋时华
 * @date 2023-04-06 20:44:01
 */
public class Demo2 {

    public static void main(String[] args) throws Exception {


        Tesseract tessreact = new Tesseract();
//        tessreact.setDatapath("/Users/jphoebe/Downloads/traineddata");
        tessreact.setLanguage("eng");
        Tesseract tessreact2 = new Tesseract();
//        tessreact2.setDatapath("/Users/jphoebe/Downloads/traineddata");
        tessreact2.setLanguage("chi_sim");

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("https://livevideopull.lottery.gov.cn/live/lottery_PAL.m3u8");
        grabber.setOption("rtsp_transport", "tcp");
        grabber.start();

        Frame frame;
        Java2DFrameConverter converter = new Java2DFrameConverter();

        // 控制读取帧数
        int frameRate = (int) grabber.getFrameRate();
        int targetFrameRateNum[] = {1, 1};
        SortedSet<Integer> targetFrameRate = new TreeSet<>();
        // 计算需要抽取的目标帧
        int calFrameRate = frameRate * targetFrameRateNum[0];
        int partSize = calFrameRate / targetFrameRateNum[1];
        int remainder = calFrameRate % targetFrameRateNum[1];
        int current = 1;
        for (int i = 0; i < targetFrameRateNum[1]; i++) {
            // 避免出现帧数多一个
            if (current <= calFrameRate) {
                targetFrameRate.add(current);
            }
            current += partSize;
            if (i < remainder) {
                current++;
            }
        }

        // 计数器
        int frameCount = 0;
        while ((frame = grabber.grabImage()) != null) {
            // 每读取一帧，增加计数器
            frameCount++;
            // 如果计数器达到目标帧率，则进行处理
            if (targetFrameRate.contains(frameCount)) {
                // 处理抽取到的帧
                String timeStr = LocalDateTimeUtil.formatNormal(LocalDateTime.now());
                String outputFilename = "/Users/jphoebe/Downloads/3/frame_" + timeStr + ".jpg";
                converter.convert(frame).createGraphics().dispose();
                File f = new File(outputFilename);
                if (!f.exists()) {
                    f.mkdirs();
                }
                BufferedImage convert = converter.convert(frame);
                BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_BGR);
                Graphics graphics = image.createGraphics();
                graphics.drawImage(convert, 0, 0, 1920, 1080, null);
                ImageIO.write(convert, "jpg", f);

//                // 尝试识别
//                File imageFile = new File(outputFilename);
//                //下载的识别库路径
//                String result;
//                try {
//                    result = "识别结果eng：" + tessreact.doOCR(imageFile);
//                    System.out.println("文件eng：" + outputFilename + " "+result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("文件eng：" + outputFilename + "  失败");
//                }
//                try {
//                    result = "识别结果chi：" + tessreact2.doOCR(imageFile);
//                    System.out.println("文件chi：" + outputFilename + " "+result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("文件chi：" + outputFilename + "  失败");
//                }
            }
            // 重置计数器
            if (calFrameRate == frameCount) {
                frameCount = 0;
            }
        }
        grabber.stop();
    }

}
