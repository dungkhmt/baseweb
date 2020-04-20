package com.hust.baseweb.repo;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

public interface FileRepo {
    public String create(InputStream input, String name,String  realName,String contentType)throws IOException;
    public Response get(String url) throws IOException;  

}