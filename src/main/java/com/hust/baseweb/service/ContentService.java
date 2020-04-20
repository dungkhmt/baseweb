package com.hust.baseweb.service;

import java.io.IOException;
import java.io.InputStream;

import com.hust.baseweb.entity.Content;

public interface ContentService {
    public Content createContent(InputStream inputStream, String realName) throws IOException;

}