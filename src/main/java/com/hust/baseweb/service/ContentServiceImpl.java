package com.hust.baseweb.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.hust.baseweb.constant.ContentTypeConstant;
import com.hust.baseweb.entity.Content;
import com.hust.baseweb.repo.ContentRepo;
import com.hust.baseweb.repo.FileRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentRepo contentRepo;
    @Autowired
    private FileRepo fileRepo;

    @Override
    public Content createContent(InputStream inputStream, String realName) throws IOException {
        Content content = new Content(ContentTypeConstant.DOCUMENT.name(), null, new Date());
        content = contentRepo.save(content);
        String url = fileRepo.create(inputStream, content.getContentId().toString(), realName);
        content.setUrl(url);
        content.setLastUpdatedAt(new Date());
        content = contentRepo.save(content);
        return content;
    }

}