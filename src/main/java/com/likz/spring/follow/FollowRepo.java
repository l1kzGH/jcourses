package com.likz.spring.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface FollowRepo extends JpaRepository<Follow, Long> {

    @Transactional
    @Query("FROM Follow f WHERE f.followUser.id = :user_id AND f.followAuthor.id = :author_id")
    Follow findByIds(@Param("user_id") Long user_id, @Param("author_id") Long author_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Follow f WHERE f.followUser.id = :user_id AND f.followAuthor.id = :author_id")
    void delete(@Param("user_id") Long user_id, @Param("author_id") Long author_id);

    @Transactional
    @Query("FROM Follow f WHERE f.followUser.id = :user_id")
    List<Follow> findByUserId(@Param("user_id")Long user_id);

    @Transactional
    @Query("FROM Follow f WHERE f.followAuthor.id = :author_id")
    List<Follow> findByAuthorId(@Param("author_id")Long author_id);

}
