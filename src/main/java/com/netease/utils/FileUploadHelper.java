package com.netease.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUploadHelper {
	
	public static String upload(CommonsMultipartFile file, File filePath)
			throws IOException, FileNotFoundException {
		if(!filePath.exists()){
			filePath.mkdirs();
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM-ddHHmmss");
		String datetime = df.format(new Date());
		String filename = datetime+".jpg";
		
		byte[] bytes = file.getBytes();
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filePath + File.separator + 
				EncodeString.encodeStr(filename)));
		stream.write(bytes);
		stream.close();
		return filename;
	}
}
