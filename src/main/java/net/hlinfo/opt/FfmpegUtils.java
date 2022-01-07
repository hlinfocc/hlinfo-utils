package net.hlinfo.opt;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.fasterxml.jackson.databind.JsonNode;
/**
 * ffmpeg命令操作工具
 * @Example
 * <pre>
 * 使用示例：<br>
 * FfmpegUtils ffmpegUtils = new FfmpegUtils();
 * 获取编码信息：<br>
 * ffmpegUtils.initEncodingFormat("/opt/test.mp4");
 * System.out.println(ffmpegUtils.getFormatParams().getVideoCodecLongName());
 * System.out.println(ffmpegUtils.getFormatParams().getAudioCodecLongName());
 * System.out.println(ffmpegUtils.getFormatParams().isMP4H264());
 * <br>
 * 转码：<br>
 * ffmpegUtils.transcodeH264("/opt/test.mp4", "/opt/out-test.mp4");
 * </pre>
 */
public class FfmpegUtils {
	/**
	 * ffmpeg命令路径
	 */
	private String ffmpegBin = "/usr/bin/ffmpeg";
	/**
	 * ffprobe命令路径
	 */
	private String ffprobeBin = "/usr/bin/ffprobe";
	/**
	 * 从ffprobe命令结果提取streams信息
	 */
	private static final String regexStreams ="\"streams\": \\[.*?\\]";
//	private static final String regexDuration ="Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";  
//	private static final String regexVideo ="Video: (.*?),";
//	private static final String regexVideo2 ="Video: (.*?), (.*?), (.*?)[,\\s]";  
//	private static final String regexAudio ="Audio: (\\w*)";
	/**
	 * 视频编码参数
	 */
	public FormatParams formatParams;
	
	/**
	 * 获取视频编码信息
	 * @param filePath 视频文件路径
	 * @throws Exception 
	 */
	public void initEncodingFormat(String filePath) throws Exception {
		if(filePath==null) {throw new Exception("视频不存在");}
		if(!new File(filePath).exists()) {throw new Exception("视频不存在");}
		String result = this.processVideo(ffprobeParam(filePath));
		if(result==null || "".equals(result)) {throw new Exception("没有获取到视频参数");}
		Pattern pv =Pattern.compile(regexStreams,Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
		Matcher mv=pv.matcher(result);
		while (mv.find()) {	
			String resJsonString = mv.group().replace("\"streams\": ", "");
			JsonNode jsonNodeObj = Jackson.toJsonObject(resJsonString);
			if(jsonNodeObj.isArray()) {
				this.setFormatParams(null);
				for (JsonNode jsonNodeItem : jsonNodeObj) {
					if("video".equals(jsonNodeItem.get("codec_type").asText().toLowerCase())) {
						this.getFormatParams().setVideoCodecName(jsonNodeItem.get("codec_name").asText());
						this.getFormatParams().setVideoCodecLongName(jsonNodeItem.get("codec_long_name").asText());
						this.getFormatParams().setVideoCodecTagString(jsonNodeItem.get("codec_tag_string").asText());
						this.getFormatParams().setWidth(jsonNodeItem.get("width").asInt());
						this.getFormatParams().setHeight(jsonNodeItem.get("height").asInt());
					}
					if("audio".equals(jsonNodeItem.get("codec_type").asText().toLowerCase())) {
						this.getFormatParams().setAudioCodecName(jsonNodeItem.get("codec_name").asText());
						this.getFormatParams().setAudioCodecLongName(jsonNodeItem.get("codec_long_name").asText());
						this.getFormatParams().setAudioCodecTagString(jsonNodeItem.get("codec_tag_string").asText());
					}
				}
			}
		}
	}
	/**
	 * 简单转码，转码为H264编码
	 * @param inputPath 源视频文件地址
	 * @param outputPath 输出视频文件地址
	 * @return 转码结果信息
	 */
	public String transcodeH264(String inputPath,String outputPath) {
		return this.transcode(inputPath, outputPath, VCodecs.h264,ACodecs.aac);
	}
	/**
	 * 简单转码，默认音频编码为AAC
	 * @param inputPath 源视频文件地址
	 * @param outputPath 输出视频文件地址
	 * @param vcodec 视频编码
	 * @return 转码结果信息
	 */
	public String transcode(String inputPath,String outputPath,VCodecs vcodec) {
		return this.transcode(inputPath, outputPath, vcodec,ACodecs.aac);
	}
	/**
	 * 简单转码
	 * @param inputPath 源视频文件地址
	 * @param outputPath 输出视频文件地址
	 * @param vcodec 视频编码
	 * @param acodec 音频编码
	 * @return 转码结果信息
	 */
	public String transcode(String inputPath,String outputPath,VCodecs vcodec,ACodecs acodec) {
		List<String> paramList = this.ffmpegTranscodeParam(inputPath, outputPath, vcodec, acodec);
		String result = this.processVideo(paramList);
		return result;
	}
	/**
	 * 设置ffprobe参数
	 * @param inputPath 视频文件地址
	 * @return 命令参数对象
	 */
	private List<String> ffprobeParam(String inputPath){
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(ffprobeBin);
		commend.add("-show_streams");
		commend.add("-of");
		commend.add("json");
		commend.add(inputPath);
		return commend;
	}
	/**
	 * 设置ffmpeg转码参数
	 * @param inputPath 源视频文件地址
	 * @param outputPath 输出视频文件地址
	 * @param vcodec 视频编码
	 * @param acodec 音频编码
	 * @return 命令参数对象
	 */
	private List<String> ffmpegTranscodeParam(String inputPath,String outputPath,VCodecs vcodec,ACodecs acodec){
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(ffmpegBin);
		commend.add("-i");
		commend.add(inputPath);
		commend.add("-vcodec");
		commend.add(vcodec.name());
		commend.add("-acodec");
		commend.add(acodec.name());
		commend.add(outputPath);
		return commend;
	}
	/**
	 * 执行命令
	 * @param commend 参数
	 * @return 输出结果
	 */
	private String processVideo(List<String> commend) {
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.redirectErrorStream(true);
			Process p = builder.start();
	
			//start
			BufferedReader buf = null; // 保存ffmpeg的输出结果流
			String line = null;
			//read the standard output
	
			buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
	
			StringBuffer sb = new StringBuffer();
			while ((line = buf.readLine()) != null) {
			//System.out.println(line);
			sb.append(line);
			continue;
			}
			int ret = p.waitFor();//这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行
			// end
			return sb.toString();
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}
	
	public String getFfmpegBin() {
		return ffmpegBin;
	}
	public void setFfmpegBin(String ffmpegBin) {
		this.ffmpegBin = ffmpegBin;
	}
	public String getFfprobeBin() {
		return ffprobeBin;
	}
	public void setFfprobeBin(String ffprobeBin) {
		this.ffprobeBin = ffprobeBin;
	}
		
	public FormatParams getFormatParams() {
		if(this.formatParams==null) {
			this.formatParams = new FormatParams();
		}
		return formatParams;
	}
	public void setFormatParams(FormatParams formatParams) {
		if(formatParams==null) {
			formatParams = new FormatParams();
		}
		this.formatParams = formatParams;
	}

	/**
	 * 视频编码信息
	 */
	public class FormatParams {
		/**
		 * 视频编码名称
		 */
		private String videoCodecName;
		/**
		 * 视频编码完整名称
		 */
		private String videoCodecLongName;
		/**
		 * 视频编码编码的标签数据
		 */
		private String videoCodecTagString;
		
		/**
		 * 音频编码名称
		 */
		private String audioCodecName;
		/**
		 * 音频编码完整名称
		 */
		private String audioCodecLongName;
		/**
		 * 音频编码标签数据
		 */
		private String audioCodecTagString;
		/**
		 * 分辨率宽
		 */
		private int width;
		/**
		 * 分辨率高
		 */
		private int height;
		/**
		 * @return 是H.264编码返回true
		 */
		public boolean isMP4H264() {
			if(this.videoCodecName.contains("h264") || this.videoCodecTagString.contains("avc")) {
				return true;
			}
			return false;
		}
		/**
		 * @return 是H.265编码返回true
		 */
		public boolean isMP4H265() {
			if(this.videoCodecName.contains("hevc") || this.videoCodecTagString.contains("hev")) {
				return true;
			}
			return false;
		}
		
		/**
		 * @return 视频编码名称
		 */
		public String getVideoCodecName() {
			return videoCodecName;
		}
		/**
		 * 
		 * @param videoCodecName
		 */
		public void setVideoCodecName(String videoCodecName) {
			this.videoCodecName = videoCodecName;
		}
		/**
		 * @return 视频编码完整名称
		 */
		public String getVideoCodecLongName() {
			return videoCodecLongName;
		}
		/**
		 * 设置视频编码完整名称
		 * @param videoCodecLongName 视频编码完整名称
		 */
		public void setVideoCodecLongName(String videoCodecLongName) {
			this.videoCodecLongName = videoCodecLongName;
		}
		/**
		 * @return 视频编码编码的标签数据
		 */
		public String getVideoCodecTagString() {
			return videoCodecTagString;
		}
		/**
		 * 设置视频编码编码的标签数据
		 * @param videoCodecTagString 视频编码编码的标签数据
		 */
		public void setVideoCodecTagString(String videoCodecTagString) {
			this.videoCodecTagString = videoCodecTagString;
		}
		/**
		 * @return 音频编码名称
		 */
		public String getAudioCodecName() {
			return audioCodecName;
		}
		/**
		 * 设置音频编码名称
		 * @param audioCodecName 音频编码名称
		 */
		public void setAudioCodecName(String audioCodecName) {
			this.audioCodecName = audioCodecName;
		}
		/**
		 * @return 音频编码完整名称
		 */
		public String getAudioCodecLongName() {
			return audioCodecLongName;
		}
		/**
		 * 设置音频编码完整名称
		 * @param audioCodecLongName 音频编码完整名称
		 */
		public void setAudioCodecLongName(String audioCodecLongName) {
			this.audioCodecLongName = audioCodecLongName;
		}
		/**
		 * @return 音频编码标签数据
		 */
		public String getAudioCodecTagString() {
			return audioCodecTagString;
		}
		/**
		 * 设置音频编码标签数据
		 * @param audioCodecTagString 音频编码标签数据
		 */
		public void setAudioCodecTagString(String audioCodecTagString) {
			this.audioCodecTagString = audioCodecTagString;
		}
		/**
		 * @return 分辨率宽
		 */
		public int getWidth() {
			return width;
		}
		/**
		 * 设置分辨率宽
		 * @param width 分辨率宽
		 */
		public void setWidth(int width) {
			this.width = width;
		}
		/**
		 * @return 分辨率高
		 */
		public int getHeight() {
			return height;
		}
		/**
		 * 设置分辨率高
		 * @param height 分辨率高
		 */
		public void setHeight(int height) {
			this.height = height;
		}
	}
	/**
	 * 视频编码格式
	 */
	public enum VCodecs{
		/**
		 * FLV
		 */
		flv1,
		/**
		 * H.263
		 */
		h263,
		/**
		 * H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10
		 */
		h264,
		/**
		 * H.265 / HEVC
		 */
		hevc,
		/**
		 * mpeg4编码并不等于mp4格式
		 */
		mpeg4,
		/**
		 * Windows Media Video 7
		 */
		wmv1,
		/**
		 * Windows Media Video 8
		 */
		wmv2,
		/**
		 * Windows Media Video 9
		 */
		wmv3,
		/**
		 * 表示使用跟原视频一样的视频编解码器
		 */
		copy
	}
	/**
	 * 音频编码格式
	 */
	public enum ACodecs{
		/**
		 * AAC编码
		 */
		aac,
		/**
		 * MP3编码
		 */
		mp3
	}
}
