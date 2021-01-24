package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.model.postoffice.CreatePostOfficeInputModel;
import com.hust.baseweb.applications.postsys.model.postoffice.OfficeOrderDetailOutput;
import com.hust.baseweb.applications.postsys.model.postoffice.PostOfficeOrderStatusOutputModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface PostOfficeService {

    PostOffice save(CreatePostOfficeInputModel input);

    List<PostOffice> findAll();

    PostOffice findByPostOfficeId(String postOfficeId);

    void deleteByPostOfficeId(String postOfficeId);

    List<PostOffice> save(List<CreatePostOfficeInputModel> input);

    OfficeOrderDetailOutput getOfficeOrderDetailOutput(String postOfficeId, Date startDate, Date endDate);

    List<PostOfficeOrderStatusOutputModel> getPostOfficeOrderStatus(Date fromDate, Date toDate, boolean from);
}
