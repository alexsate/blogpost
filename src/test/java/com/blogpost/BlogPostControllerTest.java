package com.blogpost;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogPostControllerTest {

	// @LocalServerPort
	@Value(value = "${local.server.port}")
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String baseUrl;

	@BeforeEach
	void setUp() {
		baseUrl = "http://localhost:" + port + "/api/posts";
	}

	@Test
	void testCreatePost() {
		Map<String, String> request = Map.of("title", "Test Post", "content",
				"This is a test content.");
		ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl,
				request, Map.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().get("id")).isNotNull();
		assertThat(response.getBody().get("title")).isEqualTo("Test Post");
	}

	@Test
	void testGetAllPosts() {
		// Create a post first
		Map<String, String> request = Map.of("title", "Another Test Post",
				"content", "Content for testing.");
		restTemplate.postForEntity(baseUrl, request, Map.class);

		// Fetch all posts
		ResponseEntity<List> response = restTemplate.getForEntity(baseUrl,
				List.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().size()).isGreaterThan(0);
	}

	@Test
	void testGetPostById() {
		// Create a post first
		Map<String, String> request = Map.of("title", "Post by ID", "content",
				"Retrieve this post by ID.");
		ResponseEntity<Map> postResponse = restTemplate.postForEntity(baseUrl,
				request, Map.class);

		Long postId = ((Number) postResponse.getBody().get("id")).longValue();

		// Fetch the post by ID
		ResponseEntity<Map> response = restTemplate
				.getForEntity(baseUrl + "/" + postId, Map.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().get("title")).isEqualTo("Post by ID");
	}

	@Test
	void testAddCommentToPost() {
		// Create a post first
		Map<String, String> request = Map.of("title", "Post with Comments",
				"content", "Post to add comments.");
		ResponseEntity<Map> postResponse = restTemplate.postForEntity(baseUrl,
				request, Map.class);

		Long postId = ((Number) postResponse.getBody().get("id")).longValue();

		// Add a comment
		Map<String, String> commentRequest = Map.of("content",
				"This is a test comment.");
		ResponseEntity<Map> response = restTemplate.postForEntity(
				baseUrl + "/" + postId + "/comments", commentRequest,
				Map.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().get("content"))
				.isEqualTo("This is a test comment.");

		// Verify the comment count
		ResponseEntity<Map> postResponseWithComments = restTemplate
				.getForEntity(baseUrl + "/" + postId, Map.class);
		List<Map> comments = (List<Map>) postResponseWithComments.getBody()
				.get("comments");
		assertThat(comments.size()).isEqualTo(1);
	}
}
