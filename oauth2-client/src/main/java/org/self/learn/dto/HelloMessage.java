package org.self.learn.dto;

public class HelloMessage {
	private String content;

	public HelloMessage(String message){
		this.content = message;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
