package com.torus.duplicatefilefinder;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.torus.duplicatefilefinder.FileIndexer;

import junit.framework.TestCase;

public class FileIndexerTest extends TestCase {
	private FileIndexer fileIndexer = new FileIndexer();
	
	@Override
	protected void setUp() throws Exception {
		List<String> skipDirs = new ArrayList<String>(Arrays.asList("skipDirectoryOne", "skipDirectoryTwo"));
		fileIndexer.setSkipDirs(skipDirs);
	}
	
	public void testPreVistDirectory() {
		assertEquals(FileVisitResult.SKIP_SUBTREE, fileIndexer.preVisitDirectory(Paths.get("skipDirectoryOne"), null));
		assertEquals(FileVisitResult.SKIP_SUBTREE, fileIndexer.preVisitDirectory(Paths.get("skipDirectoryTwo"), null));
		assertEquals(FileVisitResult.CONTINUE, fileIndexer.preVisitDirectory(Paths.get("skipDirectoryThree"), null));
	}
	
	public void testVisitFile() throws IOException {
		int originalChecksumMapSize = fileIndexer.getFileChecksumMap().size();
		int originalDuplicateFileListMapSize = fileIndexer.getDuplicateFileListMap().size();
		
		assertEquals(FileVisitResult.CONTINUE, fileIndexer.visitFile(Paths.get("./GreenDot.png"), null));
		assertEquals(originalChecksumMapSize+1, fileIndexer.getFileChecksumMap().size());
		assertTrue(fileIndexer.getFileChecksumMap().containsKey("2eca8130cb3b85c1ae2e7cc1f4508bc7"));
		
		assertEquals(FileVisitResult.CONTINUE, fileIndexer.visitFile(Paths.get("./GreenDotSame.png"), null));
		assertEquals(originalChecksumMapSize+1, fileIndexer.getFileChecksumMap().size());
		assertEquals(originalDuplicateFileListMapSize+1, fileIndexer.getFileChecksumMap().size());
		assertTrue(fileIndexer.getDuplicateFileListMap().containsKey("2eca8130cb3b85c1ae2e7cc1f4508bc7"));
	}
}
