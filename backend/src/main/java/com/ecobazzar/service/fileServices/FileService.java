package com.ecobazzar.service.fileServices;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


// an interface for file handling to create file handling api
public interface FileService {
	// uploading the file to specified path with filetype Multipart
	String uploadFile(String path, MultipartFile file) throws IOException;

	// method to serve the file using the path and file name
	InputStream getResourceFile(String path, String fileName) throws IOException;
}
