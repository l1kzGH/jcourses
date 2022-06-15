package com.likz.spring.course.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.id = :notification_id")
    void delete(@Param("notification_id") Long notification_id);

}
