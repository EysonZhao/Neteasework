package com.netease.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUploadHelper {
	
	public static void upload(CommonsMultipartFile file, File filePath)
			throws IOException, FileNotFoundException {
		if(!filePath.exists()){
			filePath.mkdirs();
		}
		
		byte[] bytes = file.getBytes();
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filePath + File.separator + 
				EncodeString.encodeStr(file.getOriginalFilename())));
		stream.write(bytes);
		stream.close();
	}
}
