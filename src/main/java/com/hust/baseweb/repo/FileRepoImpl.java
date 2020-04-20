package com.hust.baseweb.repo;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Repository
public class FileRepoImpl implements FileRepo {
    @Value("${content-repo.url}")
    private String contentRepoUrl;
    private static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");

    @Override
    public String create(InputStream input, String name, String realName) throws IOException {
        byte[] bytes = new byte[input.available()];
        input.read(bytes);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("id", name)
                .addFormDataPart("file", realName, RequestBody.create(MEDIA_TYPE_IMAGE, bytes)).build();

        Request request = new Request.Builder()

                .url(contentRepoUrl).post(requestBody).build();
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            return response.body().string();
        }

    }

}