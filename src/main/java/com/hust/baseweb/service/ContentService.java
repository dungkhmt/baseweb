package com.hust.baseweb.service;

import java.io.IOException;
import java.io.InputStream;

import com.hust.baseweb.entity.Content;

import okhttp3.Response;

public interface ContentService {
    public Content createContent(InputStream inputStream, String realName,String contentType) throws IOException;
    public Response getContentData(String contentId)throws IOException;
}