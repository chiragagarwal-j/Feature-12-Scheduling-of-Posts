package com.learning.learningSpring.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.learning.learningSpring.entity.LikeRecord;
import com.learning.learningSpring.entity.Post;

public interface LikeCRUDRepository extends CrudRepository<LikeRecord, Integer> {
    public Integer countByLikeIdPost(Post post);

    @Modifying
    @Query(value = "DELETE FROM like_record WHERE post_id = ?1", nativeQuery = true)
    void deleteByPostId(Integer postId);
}
