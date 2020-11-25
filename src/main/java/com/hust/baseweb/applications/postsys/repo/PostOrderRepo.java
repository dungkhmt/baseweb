package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
@Repository
public interface PostOrderRepo extends JpaRepository<PostOrder, UUID> {
    List<PostOrder> findAll();

    List<PostOrder> findByStatusId(String statusId);

    @Query(value = "select * from post_ship_order pso where pso.from_post_office_id = ?1 and pso.status_id not in ('ORDER_CANCELLED')", nativeQuery = true)
    List<PostOrder> findByFromPostOffice(String postOfficeId);

    @Query(value = "select * from post_ship_order pso where pso.to_post_office_id = ?1 and pso.status_id not in ('ORDER_CANCELLED')", nativeQuery = true)
    List<PostOrder> findByToPostOffice(String postOfficeId);

    @Modifying
    @Query("update PostOrder set status_id = ?2 where post_ship_order_id = ?1")
    void updatePostOrderStatus(UUID post_ship_order_id, String status_id);
}

