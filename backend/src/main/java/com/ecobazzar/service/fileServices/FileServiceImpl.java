package com.ecobazzar.service.fileServices;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadFile(String path, MultipartFile file) throws IOException {
		// first we need the filename to store
		String filename = file.getOriginalFilename();
		// then we need the filepath
		String filepath = path + File.separator + filename;
		// creating a file object with the path specified to store it
		File f = new File(path);
		// check if the directory to store file exists or not
		if(!(f.exists())){
			f.mkdirs();
		}
		// copying the InputStream that we get after reading contents from the file through getInputStream() into the specified path
		Files.copy(file.getInputStream(), Paths.get(filepath));
		return filename;
	}


	// method to fetch the uploaded files in server
	@Override
	public InputStream getResourceFile(String path, String fileName) throws IOException {
		// we need to specify the full path of the file
		String filepath = path + File.separator + fileName;
		return new FileInputStream(filepath);
	}
}
