package com.poly.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Mail {
	String from;
	String to;
	String subject;
	String content;

	List<String> cc = new ArrayList<String>();
	List<String> bcc = new ArrayList<String>();
	List<File> files = new ArrayList<File>();
	
	
	public Mail(String from, String to, String subject, String content) {
		super();
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
	}
}
