package minusk.interp;

import minusk.interp.exec.Executable;
import minusk.interp.parse.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TerminalInterface {
	public static void main(String[] args) throws IOException {
		args = new String[] {"test.dsl"};
		
		ArrayList<String> paths = new ArrayList<>();
		for (String arg : args) {
			paths.add(arg);
		}
		
		ArrayList<File> files = new ArrayList<>();
		for (String path : paths) {
			File file = new File(path);
			if (!file.isFile()) {
				System.err.printf("%s is not a file.%n",path);
				continue;
			} else if (!file.canRead()) {
				System.err.printf("Cannot read %s.%n", path);
				continue;
			}
			files.add(file);
		}
		if (files.size() == 0) {
			System.err.println("No files given");
			System.exit(1);
		}
		
		for (File file : files) {
			Scanner scanner = new Scanner(file).useDelimiter("\\Z");
			String s = scanner.hasNext() ? scanner.next() : "";
			scanner.close();
			ParseTree syntree = new ParseTree(s);
			Executable code = new Executable(syntree);
			code.execute();
		}
	}
}
