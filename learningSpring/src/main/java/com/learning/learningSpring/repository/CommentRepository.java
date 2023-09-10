package com.learning.learningSpring.repository;

import com.learning.learningSpring.entity.Comment;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
	@Query(value = "select * from comment c where post_id = ?1", nativeQuery = true)
	List<Comment> findAllByPostId(Integer postId);

	void deleteByPostId(Integer id);

	@Modifying
	@Transactional
	@Query("UPDATE Comment c SET c.content = ?2 WHERE c.id = ?1")
	void updateContentById(Integer id, String content);

}
