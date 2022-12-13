package net.hlinfo.vo;

import java.io.Serializable;


public class VideoConverInfoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String errorMessage;
	private String inputMessage;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getInputMessage() {
		return inputMessage;
	}
	public void setInputMessage(String inputMessage) {
		this.inputMessage = inputMessage;
	}
	
}
