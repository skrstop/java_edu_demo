package edu.jphoebe.demo.ocr.util;

import cn.auntec.framework.components.util.system.process.ProcessUtil;
import cn.auntec.framework.components.util.value.data.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.FFmpegLogCallback;

/**
 * @author 蒋时华
 * @date 2021-11-24 13:40:59
 */
@Slf4j
public class FFmpegProcessUtil extends ProcessUtil {

    public static final String FFMPEG = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
    public static final String FFPROBE = Loader.load(org.bytedeco.ffmpeg.ffprobe.class);

    static {
        // ready
        avutil.av_log_set_level(avutil.AV_LOG_VERBOSE);
        FFmpegLogCallback.set();
        ProcessUtil.setErrorMessageKey(CollectionUtil.newArrayList("error"
                , "no "
                , "failed"
                , "invalid"
                , "using cpu capabilities: none"
                , "incorrect"));
    }

}
