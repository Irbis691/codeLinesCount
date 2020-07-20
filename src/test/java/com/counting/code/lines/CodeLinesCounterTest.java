package com.counting.code.lines;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CodeLinesCounterTest {
	
	private final CodeLinesCounter subject = new CodeLinesCounter();
	
	@Test(expected = IOException.class)
	public void testWrongPath() throws IOException {
		subject.countCodeLines(Paths.get("qwe"));
	}
	
	@Test
	public void testEmptyFile() throws IOException {
		testTotalCodeLinesCount("src\\test\\resources\\emptyFile.java", 0);
	}
	
	@Test
	public void testFileWithCode() throws IOException {
		testTotalCodeLinesCount("src\\test\\resources\\test.java", 3);
	}
	
	@Test
	public void testEmptyFolder() throws IOException {
		testTotalCodeLinesCount("src\\test\\resources\\emptyFolder", 0);
	}
	
	@Test
	public void testTotalCountForFolderWithFiles() throws IOException {
		testTotalCodeLinesCount("src\\test\\resources\\folderWithFiles", 8);
	}
	
	@Test
	public void testFilesCountForFolderWithFiles() throws IOException {
		Path path = Paths.get("src\\test\\resources\\folderWithFiles");
		Map<Path, Long> result = subject.countCodeLines(path);
		Path firstFilePath = Paths.get("src\\test\\resources\\folderWithFiles\\test.java");
		Path secondFilePath = Paths.get("src\\test\\resources\\folderWithFiles\\test1.java");
		assertEquals(3L, result.get(firstFilePath).longValue());
		assertEquals(5L, result.get(secondFilePath).longValue());
	}
	
	@Test
	public void testTotalCountForFolderWithFilesAndFolders() throws IOException {
		testTotalCodeLinesCount("src\\test\\resources\\fullFolder", 19);
	}
	
	@Test
	public void testFilesAndFolderCountForFolderWithFilesAndFolders() throws IOException {
		Path path = Paths.get("src\\test\\resources\\fullFolder");
		Map<Path, Long> result = subject.countCodeLines(path);
		Path firstFilePath = Paths.get("src\\test\\resources\\fullFolder\\f1\\test.java");
		Path secondFilePath = Paths.get("src\\test\\resources\\fullFolder\\f2\\test1.java");
		Path firstFolderPath = Paths.get("src\\test\\resources\\fullFolder\\f1");
		Path secondFolderPath = Paths.get("src\\test\\resources\\fullFolder\\f2");
		assertEquals(3L, result.get(firstFolderPath).longValue());
		assertEquals(8L, result.get(secondFolderPath).longValue());
		assertEquals(3L, result.get(firstFilePath).longValue());
		assertEquals(5L, result.get(secondFilePath).longValue());
	}
	
	private void testTotalCodeLinesCount(String pathString, long expectedTotalCodeLinesCount) throws IOException {
		Path path = Paths.get(pathString);
		Long actual = subject.countCodeLines(path).get(path);
		assertEquals(expectedTotalCodeLinesCount, actual.longValue());
	}
	
}