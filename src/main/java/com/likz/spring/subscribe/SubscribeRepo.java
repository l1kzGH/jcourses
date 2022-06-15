package com.likz.spring.subscribe;

import com.likz.spring.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SubscribeRepo extends JpaRepository<Subscribe, Long> {

    @Transactional
    @Query("FROM Subscribe s WHERE s.subUser.id = :user_id AND s.subCourse.id = :course_id")
    Subscribe findByIds(@Param("user_id") Long user_id, @Param("course_id") Long course_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Subscribe s WHERE s.subUser.id = :user_id AND s.subCourse.id = :course_id")
    void delete(@Param("user_id") Long user_id, @Param("course_id") Long course_id);

    @Transactional
    @Query("FROM Subscribe s WHERE s.subUser.id = :user_id")
    List<Subscribe> findByUserId(@Param("user_id")Long user_id);

}
