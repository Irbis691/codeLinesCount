package com.counting.code.lines;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class CodeLinesCountApp {

	private static Path input() throws IOException {
		System.out.println("Enter start folder path:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return Paths.get(reader.readLine());
	}

	private static void output(Map<Path, Long> fileCounts) {
		for (Map.Entry<Path, Long> e : fileCounts.entrySet()) {
			System.out.println(e.getKey().toString() + ": " + e.getValue());
		}
	}

	public static void main(String[] args) throws IOException {
		CodeLinesCounter counter = new CodeLinesCounter();
		output(counter.countCodeLines(input()));
	}

}