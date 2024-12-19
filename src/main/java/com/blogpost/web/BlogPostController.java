package com.blogpost.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogpost.dto.BlogPost;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class BlogPostController {

	private Map<Long, BlogPost> blogPosts = new HashMap<>();

	public BlogPostController() {
		this.blogPosts = new HashMap<>();
	}

	// GET /api/posts
	@GetMapping
	@Operation(
			summary = "Retrieve all blog posts",
			responses = {@ApiResponse(
					responseCode = "200",
					description = "List of blog posts",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(
									type = "array",
									implementation = BlogPost.class)),
					links = {
							@Link(
									name = "self",
									description = "Link to this resource",
									operationId = "getAllPosts"),
							@Link(
									name = "create",
									description = "Link to create a new blog post",
									operationId = "createPost")})})
	public List<EntityModel<Map<String, Object>>> getAllPosts() {
		List<EntityModel<Map<String, Object>>> response = new ArrayList<>();
		for (BlogPost post : blogPosts.values()) {
			Map<String, Object> postInfo = new HashMap<>();
			postInfo.put("id", post.getId());
			postInfo.put("title", post.getTitle());
			postInfo.put("commentCount", post.getComments().size());

			// Add HATEOAS links
			EntityModel<Map<String, Object>> postModel = EntityModel.of(
					postInfo,
					WebMvcLinkBuilder
							.linkTo(WebMvcLinkBuilder
									.methodOn(BlogPostController.class)
									.getPostById(post.getId()))
							.withRel("details"),
					WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
							.methodOn(BlogPostController.class).getAllPosts())
							.withRel("allPosts"));
			response.add(postModel);
		}
		return response;
	}
	// public List<Map<String, Object>> getAllPosts() {
	// List<Map<String, Object>> response = new ArrayList<>();
	// for (BlogPost post : blogPosts.values()) {
	// Map<String, Object> postInfo = new HashMap<>();
	// postInfo.put("id", post.getId());
	// postInfo.put("title", post.getTitle());
	// postInfo.put("commentCount", post.getComments().size());
	// response.add(postInfo);
	// }
	// return response;
	// }

	// POST /api/posts
	@PostMapping
	@Operation(
			summary = "Create a new blog post",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Blog post details",
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = BlogPost.class))),
			responses = {

					@ApiResponse(
							responseCode = "200",
							description = "Blog post created successfully",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(
											implementation = BlogPost.class)),
							links = {@Link(
									name = "self",
									description = "Link to this resource",
									operationId = "createPost"),
									@Link(
											name = "allPosts",
											description = "Link to retrieve all posts",
											operationId = "getAllPosts")})}

	)
	public ResponseEntity<BlogPost> createPost(
			@RequestBody @Valid BlogPost blogPost) {
		blogPosts.put(blogPost.getId(), blogPost);
		return ResponseEntity.ok(blogPost);
	}

	// GET /api/posts/{id}
	@GetMapping("/{id}")
	@Operation(
			summary = "Get a blog post by ID",
			parameters = {@io.swagger.v3.oas.annotations.Parameter(
					in = ParameterIn.PATH,
					name = "id",
					description = "ID of the blog post",
					required = true,
					schema = @Schema(type = "integer", example = "1"))},
			responses = {@ApiResponse(
					responseCode = "200",
					description = "Blog post found",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = BlogPost.class)),
					links = {
							@Link(
									name = "self",
									description = "Link to this blog post",
									operationId = "getPostById"),
							@Link(
									name = "allPosts",
									description = "Link to retrieve all posts",
									operationId = "getAllPosts")}),
					@ApiResponse(
							responseCode = "404",
							description = "Blog post not found")})
	public ResponseEntity<BlogPost> getPostById(@PathVariable("id") long id) {
		BlogPost post = blogPosts.get(id);
		if (post == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(post);
	}

	public Map<Long, BlogPost> getBlogPosts() {
		return blogPosts;
	}

	public void setBlogPosts(Map<Long, BlogPost> blogPosts) {
		this.blogPosts.clear();
		this.blogPosts.putAll(blogPosts);
	}
}
