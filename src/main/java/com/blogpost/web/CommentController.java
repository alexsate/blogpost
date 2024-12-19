package com.blogpost.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogpost.dto.BlogPost;
import com.blogpost.dto.Comment;
import com.blogpost.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

	@Autowired
	private BlogPostController blogPostController;

	@Autowired
	private CommentService commentService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	// POST /api/posts/{postId}/comments
	@PostMapping
	@Operation(
			summary = "Add a comment to a blog post",
			parameters = {@io.swagger.v3.oas.annotations.Parameter(
					in = ParameterIn.PATH,
					name = "postId",
					description = "ID of the blog post to add the comment to",
					required = true,
					schema = @Schema(type = "integer", example = "1"))},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Comment details",
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(
									implementation = CommentRequest.class))),
			responses = {@ApiResponse(
					responseCode = "201",
					description = "Comment added successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = Comment.class)),
					links = {@Link(
							name = "self",
							description = "Link to this comment resource",
							operationId = "addCommentToPost"),
							@Link(
									name = "postDetails",
									description = "Link to view blog post details",
									operationId = "getPostById",
									parameters = {
											@io.swagger.v3.oas.annotations.links.LinkParameter(
													name = "postId",
													expression = "$request.path.postId")})}),
					@ApiResponse(
							responseCode = "404",
							description = "Blog post not found"),
					@ApiResponse(
							responseCode = "400",
							description = "Invalid comment request")})
	public ResponseEntity<String> addCommentToPost(
			@PathVariable("postId") long postId,
			@RequestBody @Valid CommentRequest request) {
		Map<Long, BlogPost> blogPosts = blogPostController.getBlogPosts();
		BlogPost post = blogPosts.get(postId);
		if (post == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("{\"error\": \"Blog post not found\"}");
		}
		if (request.getContent() == null || request.getContent().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"error\": \"Invalid comment request\"}");

		}
		Comment newComment = new Comment(request.getContent());
		post.addComment(newComment);

		commentService.notifyPostOwner(post, newComment);

		try {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(objectMapper.writeValueAsString(newComment));
		} catch (JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"Failed to serialize response\"}");
		}
	}

	// Request DTO for adding comments
	@Schema(description = "Request body for adding a comment")
	public static class CommentRequest {
		@Schema(
				description = "Content of the comment",
				example = "This is a great post!")
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
