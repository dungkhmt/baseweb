package com.hust.baseweb.repo;

import java.io.IOException;
import java.io.InputStream;

public interface FileRepo {
    public String create(InputStream input, String name,String  realName)throws IOException;

}