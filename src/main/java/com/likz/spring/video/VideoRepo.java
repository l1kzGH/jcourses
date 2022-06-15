package com.likz.spring.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VideoRepo extends JpaRepository<Video, Long> {

    List<Video> findAllByCourseId(Long course_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Video v WHERE v.id = :video_id")
    void delete(@Param("video_id") Long video_id);

}
