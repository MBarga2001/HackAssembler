import java.io.*;
import java.util.ArrayList;

public class Parser {

	String cmd = "";
	int current = -1;
	ArrayList<String> commands = new ArrayList<>();

	public Parser(File file) {
		String line;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				line = line.split("//")[0];
				line = line.strip();
				line = line.replace(" ", "");
				if (!line.equals("")) {
					commands.add(line);
				}
			}
			fr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasMoreCommands() {
		return (current + 1) < commands.size();
	}

	public void advance() {
		current += 1;
		cmd = commands.get(current);
	}

	public void restart() {
		cmd = "";
		current = -1;
	}

	public String commandType() {
		if (cmd.charAt(0) == '@') {
			return "A";
		} else if (cmd.charAt(0) == '(') {
			return "L";
		} else {
			return "C";
		}
	}

	public String symbol() {
		if (commandType().equals("A")) {
			return cmd.substring(1);
		}
		if (commandType().equals("L")) {
			return cmd.substring(1, cmd.length() - 1);
		}
		return "";
	}

	public String dest() {
		if (commandType().equals("C")) {
			if (cmd.contains("=")) {
				return cmd.split("=")[0];
			}
		}
		return "";
	}

	public String comp() {
		if (commandType().equals("C")) {
			String tmp = cmd;
			if (tmp.contains("=")) {
				tmp = tmp.split("=")[1];
			}
			return tmp.split(";")[0];
		} else {
			return "";
		}
	}

	public String jump() {
		if (commandType().equals("C")) {
			String temp = cmd;
			if (temp.contains(";")) {
				return temp.split(";")[1];
			}
		}
		return "";
	}
}
