package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostOrderRepo extends JpaRepository<PostOrder, UUID> {
    List<PostOrder> findAll();
    PostOrder save(PostOrder postOrder);

    @Modifying
    @Transactional
    @Query("update PostOrder set status_id = ?2 where post_ship_order_id = ?1")
    void updatePostOrderStatus(UUID post_ship_order_id, String status_id);

}
