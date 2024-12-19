package com.blogpost.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a comment on a blog post.")
public class Comment {

	private static long idCounter = 0;

	@Schema(description = "Unique identifier of the comment", example = "1")
	@JsonIgnore
	private final long id;

	@Schema(description = "Content of the comment", example = "This is an amazing post!")
	private String content;

	public Comment() {
		this.id = ++idCounter;
	}

	@JsonCreator
	public Comment(@JsonProperty("content") String content) {
		this.id = ++idCounter;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
