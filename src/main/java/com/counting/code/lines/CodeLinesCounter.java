package com.counting.code.lines;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CodeLinesCounter {
	private long getCodeLinesCountForFile(Path path) throws IOException {
		return Files.lines(path)
				.map(String::trim)
				.filter(s -> !s.startsWith("/"))
				.filter(s -> !s.startsWith("*"))
				.filter(s -> !s.isEmpty())
				.count();
	}
	
	private Map<Path, Long> proceedFolders(List<Path> folders) throws IOException {
		Map<Path, Long> folderCount = new TreeMap<>();
		for (Path p : folders) {
			folderCount.putAll(countCodeLines(p));
		}
		return folderCount;
	}
	
	private Map<Path, Long> proceedFiles(List<Path> files) throws IOException {
		Map<Path, Long> fileCounts = new TreeMap<>();
		for (Path p : files) {
			fileCounts.put(p, getCodeLinesCountForFile(p));
		}
		return fileCounts;
	}
	
	private long aggregatedCodeLinesCount(Map<Path, Long> temp) {
		return temp.entrySet().stream()
				.filter(pair -> !Files.isDirectory(pair.getKey()))
				.mapToLong(Map.Entry::getValue)
				.sum();
	}
	
	public Map<Path, Long> countCodeLines(Path startPath) throws IOException {
		if (!Files.isDirectory(startPath)) {
			return proceedFiles(List.of(startPath));
		}
		Map<Boolean, List<Path>> collect = Files.list(startPath)
				.collect(Collectors.partitioningBy(path -> Files.isDirectory(path)));
		Map<Path, Long> fileCounts = proceedFiles(collect.get(false));
		Map<Path, Long> folderCount = proceedFolders(collect.get(true));
		Map<Path, Long> result = new TreeMap<>();
		result.put(startPath, aggregatedCodeLinesCount(fileCounts) + aggregatedCodeLinesCount(folderCount));
		result.putAll(folderCount);
		result.putAll(fileCounts);
		return result;
	}
}
