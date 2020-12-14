package com.hust.baseweb.applications.backlog.service.Storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("upload")
public class BacklogStorageProperties {
    private String rootPath;
    private String backlogDataPath;
}
