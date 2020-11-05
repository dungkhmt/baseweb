package com.hust.baseweb.applications.education.classmanagement.service.storage;

import com.hust.baseweb.applications.education.classmanagement.service.storage.exception.StorageException;
import com.hust.baseweb.applications.education.classmanagement.service.storage.exception.StorageFileNotFoundException;
import com.hust.baseweb.applications.education.classmanagement.utils.ZipOutputStreamUtils;
import com.hust.baseweb.applications.education.entity.Assignment;
import com.hust.baseweb.applications.education.entity.AssignmentSubmission;
import com.hust.baseweb.applications.education.repo.AssignmentSubmissionRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.extern.log4j.Log4j2;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class FileSystemStorageServiceImpl implements StorageService {

    private final String rootPath;

    @Autowired
    AssignmentSubmissionRepo submissionRepo;

    @Autowired
    public FileSystemStorageServiceImpl(StorageProperties properties) {
        rootPath = properties.getRootPath() + properties.getClassManagementDataPath();
        init(Paths.get(rootPath));
    }

    @Override
    @Transactional
    public void store(MultipartFile file, UUID assignmentId, String studentId) {
        log.info("store, StudentId = " + studentId);

        Path path = Paths.get(rootPath + assignmentId.toString() + "\\");
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + originalFileName);
            }

            if (originalFileName.contains("..")) {
                // This is a security check
                throw new StorageException(
                    "Cannot store file with relative path outside current directory "
                    + originalFileName);
            }

            // Save meta-data.
            UserLogin student = new UserLogin();
            Assignment assignment = new Assignment();
            AssignmentSubmission submission = submissionRepo.findByAssignmentIdAndStudentUserLoginId(
                assignmentId,
                studentId);

            if (null == submission) {
                submission = new AssignmentSubmission();
            } else {
                deleteIfExists(path.resolve(studentId + getFileExtension(submission.getOriginalFileName())));
            }

            student.setUserLoginId(studentId);
            assignment.setId(assignmentId);

            submission.setAssignment(assignment);
            submission.setOriginalFileName(originalFileName);
            submission.setStudent(student);
            submission.setLastUpdatedStamp(new Date());

            submissionRepo.save(submission);

            // Save uploaded file.
            Files.copy(file.getInputStream(), path.resolve(studentId + getFileExtension(originalFileName)),
                       StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originalFileName, e);
        }
    }

    /*@Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootPath, 1)
                        .filter(path -> !path.equals(this.rootPath))
                        .map(this.rootPath::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }*/

    @Override
    public Path load(String fileName, String folder) {
        return Paths.get(rootPath + folder + "\\").resolve(fileName);
    }

    @Override
    public InputStream loadFileAsResource(String fileName, String folder) throws IOException {
        try {
            Path filePath = load(fileName, folder);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource.getInputStream();
            } else {
                throw new StorageFileNotFoundException(
                    "Could not read file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + fileName, e);
        }
    }

    /**
     * Delete a file or directory. Does not handle symbolic links in Linux.
     *
     * @param path
     * @return
     * @throws IOException
     */
    @Override
    public boolean deleteAll(Path path) throws IOException {
        return FileSystemUtils.deleteRecursively(path);
    }

    @Override
    public void deleteIfExists(Path path) throws IOException {
        try {
            Files.deleteIfExists(path);
        } catch (DirectoryNotEmptyException e) {
            FileUtils.deleteDirectory(path.toFile());
            log.info("Diẻctory");
        }
    }

    @Override
    public void init(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    // Completed.
    public String getFileExtension(String originalFileName) {
        String fileExtension = "";

        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
        }

        return fileExtension;
    }

    public void zipFiles(File outputZipFile, List<File> filesToAdd) throws IOException {
        ZipOutputStreamUtils utils = new ZipOutputStreamUtils();

        utils.zipOutputStream(
            outputZipFile,
            filesToAdd,
            "admin:123".toCharArray(),
            CompressionMethod.DEFLATE,
            true,
            EncryptionMethod.AES,
            AesKeyStrength.KEY_STRENGTH_256);
    }
}
