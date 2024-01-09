package edu.jphoebe.demo.ocr;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.skrstop.framework.components.util.system.process.ProcessUtil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2023-04-14 13:28:27
 */
public class FrameSnapshot {

    public static void pushStream(String mp4, String rtmp, Long timeout) {
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-re");
        command.add("-i");
        command.add(mp4);
        command.add("-c");
        command.add("copy");
        command.add("-f");
        command.add("flv");
        command.add(rtmp);
        ProcessUtil.execute(command, null, null, timeout);
    }

    public static void getFrame(String url, String savePath) throws Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(url);
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
        int second = 1;
        int frameCount = 0;
        while ((frame = grabber.grabImage()) != null) {
            // 每读取一帧，增加计数器
            frameCount++;
            // 如果计数器达到目标帧率，则进行处理
            if (targetFrameRate.contains(frameCount)) {
                // 处理抽取到的帧
                String outputFilename = savePath + "frame_" + second + "_" + frameCount + ".jpg";
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

            }
            // 重置计数器
            if (calFrameRate == frameCount) {
                frameCount = 0;
                second++;
            }
        }
        grabber.stop();
    }

    public static void main(String[] args) throws Exception {
        ProcessUtil.setLogConsole();
        List<File> files = FileUtil.loopFiles("/Users/jphoebe/Downloads/体彩--视频/排列3、排列5/", new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return StrUtil.endWith(pathname.getName(), ".mp4");
            }
        });
        Long timeout = 15L * 60 * 1000;
        for (File file : files) {
            String savePath = StrUtil.removeSuffix(file.getPath().toString(), ".mp4") + "/";
            // 推流
            ThreadUtil.execute(() -> {
                pushStream(file.getPath().toString(), "rtmp://localhost:1935/hls/movie", timeout);
            });

            // 等待20s
            TimeUnit.SECONDS.sleep(20);

            // 取帧
            getFrame("http://localhost:8080/hls/movie.m3u8", savePath);
        }

    }

}
