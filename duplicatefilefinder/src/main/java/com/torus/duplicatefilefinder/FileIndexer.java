package com.torus.duplicatefilefinder;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import com.torus.duplicatefilefinder.utils.ChecksumGenerator;

public class FileIndexer extends SimpleFileVisitor<Path> {
	private Map<String, String> fileChecksumMap;
	private Map<String, List<String>> duplicateFileListMap;
	private List<String> skipDirs;
	
	public FileIndexer() {
		fileChecksumMap = new HashMap<String, String>();
		duplicateFileListMap = new HashMap<String, List<String>>();
	}
	
	public Map<String, String> getFileChecksumMap() {
		return fileChecksumMap;
	}
	
	public Map<String, List<String>> getDuplicateFileListMap() {
		return duplicateFileListMap;
	}
	
	public void setSkipDirs(List<String> skipDirs) {
		this.skipDirs = skipDirs;
	}
	
	@Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		if (skipDirs.contains(dir.getFileName().toString())) {
			return FileVisitResult.SKIP_SUBTREE;
		}
        return FileVisitResult.CONTINUE;
    }

	@Override
    public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
    	String checksum = new ChecksumGenerator(filePath.toString(), "MD5").getChecksum();
    	if(isSkippable(filePath.toFile()) || checksum.isEmpty()) {
    		return FileVisitResult.CONTINUE;
    	}
    	String previousEntryFileName = fileChecksumMap.put(checksum, filePath.toString());
    	
    	if (previousEntryFileName != null && areDuplicates(filePath, Paths.get(previousEntryFileName))){
    		List<String> addList = new ArrayList<String>(Arrays.asList(filePath.toString(), previousEntryFileName));
    		List<String> dupList = duplicateFileListMap.put(checksum, addList);
    		if (dupList != null) {
    			dupList.add(filePath.toString());
    			duplicateFileListMap.put(checksum, dupList);
    		}
    	}
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
    	return FileVisitResult.SKIP_SUBTREE;
    }
    
    private boolean isSkippable(File file) {
    	return file.length() == 0L ||
    		   file.isHidden();
    }
    
    private boolean areDuplicates(Path p1, Path p2) {
		if (p1.toFile().isDirectory() || p2.toFile().isDirectory()) {
			return false;
		} else {
			try {
				return FileUtils.contentEquals(p1.toFile(), p2.toFile());
			} catch (IOException e) {
				return false;
			}
		}
	}
}
