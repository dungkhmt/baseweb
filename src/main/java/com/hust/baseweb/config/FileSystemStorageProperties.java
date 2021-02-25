package com.hust.baseweb.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "upload")
/**
 * Properties of this class are immutable.
 * @author Le Anh Tuan
 */
public class FileSystemStorageProperties {

    @NotBlank
    private String rootPath;

    @NotBlank
    private String classManagementDataPath;

    @NotBlank
    private String programSubmissionDataPath;
}
