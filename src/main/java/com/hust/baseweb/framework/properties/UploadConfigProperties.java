package com.hust.baseweb.framework.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("upload")
public class UploadConfigProperties {
    private String rootPath;
    private String backlogDataPath;
    private String programSubmissionDataPath;
}
