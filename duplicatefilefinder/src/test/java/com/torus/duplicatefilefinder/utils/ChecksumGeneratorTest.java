package com.torus.duplicatefilefinder.utils;

import junit.framework.TestCase;

public class ChecksumGeneratorTest extends TestCase{
	public void testGetChecksumMD5() {
		ChecksumGenerator checksumGenerator = new ChecksumGenerator("./GreenDot.png", "MD5");
		String checksumMD5 = "2eca8130cb3b85c1ae2e7cc1f4508bc7";
		String fileChecksum = checksumGenerator.getChecksum();
		assertEquals(checksumMD5, fileChecksum);
	}

	public void testGetChecksumSHA256() {
		ChecksumGenerator checksumGenerator = new ChecksumGenerator("./GreenDot.png", "SHA-256");
		String checksum256 = "694c9597a6c483075fb3baf1e6d21279bbb461dcda34b99aa692414d8787ef0b";
		String fileChecksum = checksumGenerator.getChecksum();
		assertEquals(checksum256, fileChecksum);
	}

	public void testGetChecksumSHA512() {
		ChecksumGenerator checksumGenerator = new ChecksumGenerator("./GreenDot.png", "SHA-512");
		String checksum512 = "a410fc0aaa5fef99cf2edf93d35363c760642390c3ccfdeab6ecdec51661a97aaa60b55a57283cae5b21e280f4167555fc6758f5f6c9e5c2f160e7a8805d4c14";
		String fileChecksum = checksumGenerator.getChecksum();
		assertEquals(checksum512, fileChecksum);
	}
}
