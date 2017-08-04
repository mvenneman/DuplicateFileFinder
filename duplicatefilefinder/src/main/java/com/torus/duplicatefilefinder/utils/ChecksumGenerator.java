package com.torus.duplicatefilefinder.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumGenerator {
	private String filename;
	private String algorithm;
	
	public ChecksumGenerator(String filename, String algorithm){
		this.filename = filename;
		this.algorithm = algorithm;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getChecksum() {
		StringBuffer stringBuffer = new StringBuffer();
		RandomAccessFile raf = null;
		FileChannel fileChannel = null;
		try {
			File file =  new File(filename);
			raf = new RandomAccessFile(file, "r");
			fileChannel = raf.getChannel();
			MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
			
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(buffer);
			
			byte[] digestedBytes = messageDigest.digest();
			
			for (int i=0; i < digestedBytes.length; i++) {
				stringBuffer.append(Integer.toString( ( digestedBytes[i] & 0xff ) + 0x100, 16).substring( 1 ));
			}
		} catch (FileNotFoundException e) {
			if (!e.getMessage().contains("Access is denied") &&
				!e.getMessage().contains("The process cannot access the file because it is being used by another process")) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				raf.close();
				fileChannel.close();
			} catch (Exception e) {}
		}
		return stringBuffer.toString();
	}
}
