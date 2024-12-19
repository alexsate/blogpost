package com.blogpost.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.blogpost.dto.BlogPost;
import com.blogpost.dto.Comment;

@Service
public class CommentService {

	private static final Logger logger = LoggerFactory
			.getLogger(CommentService.class);

	@Async
	public void notifyPostOwner(BlogPost post, Comment comment) {
		// TODO: Simulate sending a notification
		logger.error("TODO: feature not available {}",
				"Notifying post owner about a new comment: "
						+ comment.getContent());

	}
}
