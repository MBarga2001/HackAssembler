import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HackAssembler {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Error: Please provide a Hack assembly file.");
			return;
		}

		String inputFileName = args[0];
		File fileIn = new File(inputFileName);

		Parser parser = new Parser(fileIn);
		SymbolTable symbols = new SymbolTable();

		int counter = 0;
		while (parser.hasMoreCommands()) {
			parser.advance();
			if (parser.commandType().equals("L")) {
				symbols.addEntry(parser.symbol(), counter);
			} else {
				counter += 1;
			}
		}

		parser.restart();

		Code coder = new Code();

		String outputFileName = inputFileName.replace(".asm", ".hack");
		try {
			FileWriter fileOut = new FileWriter(outputFileName);
			int variable = 16;
			while (parser.hasMoreCommands()) {
				parser.advance();
				if (parser.commandType().equals("A")) {
					int num;
					String symbol = parser.symbol();

					if (symbol.matches("[0-9]+")) {
						num = Integer.parseInt(symbol);
					} else if (symbols.contains(symbol)) {
						num = symbols.GetAddress(symbol);
					} else {
						num = variable;
						symbols.addEntry(symbol, num);
						variable += 1;
					}
					fileOut.write(Integer.toBinaryString(0x10000 | num).substring(1));
					fileOut.write("\n");
				} else if (parser.commandType().equals("C")) {
					String comp = coder.comp(parser.comp());
					String dest = coder.dest(parser.dest());
					String jump = coder.jump(parser.jump());
					fileOut.write("111" + comp + dest + jump);
					fileOut.write("\n");
				}
			}
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}



