package com.hust.baseweb.applications.education.classmanagement.service.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("upload")
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */
	private String rootPath;

	private String classManagementDataPath;
}
