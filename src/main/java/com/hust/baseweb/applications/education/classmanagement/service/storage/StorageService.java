package com.hust.baseweb.applications.education.classmanagement.service.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@Service
public interface StorageService {

    void init(Path path);

    void store(MultipartFile file, UUID assignmentId, String studentId);

    /*Stream<Path> loadAll();*/

    Path load(String fileName, String folder);

    InputStream loadFileAsResource(String fileName, String folder) throws IOException;

    /**
     * Delete file or directory.
     *
     * @param path
     * @return
     * @throws IOException
     */
    boolean deleteAll(Path path) throws IOException;

    void deleteIfExists(Path path) throws IOException;
}
