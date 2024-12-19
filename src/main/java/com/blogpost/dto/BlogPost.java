package com.blogpost.dto;

import java.util.ArrayList;
import java.util.List;

import io.micrometer.common.lang.NonNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
		description = "Represents a blog post containing a title, content, and associated comments.")
public class BlogPost {
	private static long idCounter = 0;

	@Schema(description = "Unique identifier of the blog post", example = "1")
	private final long id;

	@Schema(description = "Title of the blog post", example = "My First Blog")
	@NonNull
	private String title;

	@Schema(
			description = "Content of the blog post",
			example = "This is the content of my first blog post.")
	private String content;

	@Schema(
			description = "List of comments associated with the blog post",
			example = "[{\"id\": 1, \"content\": \"Great post!\"}, {\"id\": 2, \"content\": \"Very informative.\"}]")
	private final List<Comment> comments = new ArrayList<>();

	public BlogPost(String title, String content) {
		this.id = ++idCounter;
		this.title = title;
		this.content = content;
	}

	public BlogPost() {
		this.id = ++idCounter;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}
}
