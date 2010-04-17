

import java.io.*;
import java.util.LinkedList;

/**
 * Class that runs the parsing segment of the project.
 * 
 * @author Logan Blyth, James McCarty, & Robert Rayborn 
 *
 */

public class ParserDriver
{
	
	public static void main(String[] args)
	{
		
		String scannerFile, grammarFile, parseTableFile;
		ParserGenerator parserGenerator;
		LinkedList<String> input;

		// Assign file names
		if(args.length == 0)
		{
			// Set default paramenters
			scannerFile = "scan.txt";
			grammarFile = "grammar.txt";
			parseTableFile = "parseTable.csv";
		}
		else if(args.length == 3)
		{
			// Assign any input parameter
			scannerFile = args[0];
			grammarFile = args[1];
			parseTableFile = args[2];
		}
		else
		{
			throw new RuntimeException("\n Wrong input parameters to ParseDriver.java.  Input parameters are: \n"
					+"   Scanner File Name \n"
					+"   Grammar File Name \n"
					+"   Parse Table File Name (to save to)");
		}		
		
		//======================================================================
		//=======STEP 1: LOADING DATA FOR PARSE TABLE BUILD=====================
		//======================================================================
		
		try
		{
			String line;
			String lineTokens[], terminalTokens[], nonterminalTokens[], startToken;
			
			// Set up file input
			FileInputStream fstream = new FileInputStream(grammarFile);
			DataInputStream dstream = new DataInputStream(fstream); 
			BufferedReader br = new BufferedReader(new InputStreamReader(dstream));

			// Read "Tokens" line
			line = br.readLine();
			line = removeMultipleSpaces(line);
			lineTokens = line.trim().split(" ");
			if (!lineTokens[0].equals("%Tokens"))
				throw new RuntimeException("\n Grammar file malformed.");
			terminalTokens = new String[lineTokens.length-1];
			for(int i=0; i<terminalTokens.length; i++)
				terminalTokens[i] = lineTokens[i+1].trim();
			
			// Read "Non-terminal" line
			line = br.readLine();
			line = removeMultipleSpaces(line);
			lineTokens = line.trim().split(" ");
			if (!lineTokens[0].equals("%Non-terminals"))
				throw new RuntimeException("\n Grammar file malformed.");
			nonterminalTokens = new String[lineTokens.length-1];
			for(int i=0; i<nonterminalTokens.length; i++)
				nonterminalTokens[i] = lineTokens[i+1].trim();
			
			// Start line
			line = br.readLine();
			line = removeMultipleSpaces(line);
			lineTokens = line.trim().split(" ");
			if (!lineTokens[0].equals("%Start"))
				throw new RuntimeException("\n Grammar file malformed.");
			startToken = lineTokens[1].trim();

			// Create parse table builder			
			parserGenerator = new ParserGenerator(terminalTokens,nonterminalTokens,startToken);
			
			// Rule line
			line = br.readLine();
			line = removeMultipleSpaces(line);
			lineTokens = line.trim().split(" ");
			if (!lineTokens[0].equals("%Rules"))
				throw new RuntimeException("\n Grammar file malformed.");
			
			String rule;
			String productionRules[];
			String splitString[];
			// Set up rules	
			while( (line = br.readLine()) != null )
			{
				line = removeMultipleSpaces(line);
				splitString = line.split(":"); 					// "RULE : A B | C D " -> {"RULE " , " A B | C D "}
				rule = splitString[0].trim(); 					// "RULE " -> "RULE"
				productionRules = splitString[1].split("\\|");	// " A B | C D " -> {" A B " , " C D "} 
				for(String productionRule : productionRules)
				{
					parserGenerator.addGrammarRule(rule, productionRule.trim().split(" "));
				}	
			}
			
			dstream.close();
		}
		catch(IOException e)
		{
			throw new RuntimeException("\n Grammar file read failed.");
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw new RuntimeException("\n Grammar file malformed.");			
		}

		//======================================================================
		//=======STEP 2: BUILD THE PARSE TABLE==================================
		//======================================================================
		
		parserGenerator.buildParseTable();
		try
		{
			parserGenerator.saveParseTable(parseTableFile);
		}
		catch(IOException e)
		{
			throw new RuntimeException("\n Save parse table failed.");
		}

		//======================================================================
		//=======STEP 3: LOAD THE INPUT DATA====================================
		//======================================================================
		
		try
		{
			String line;
			String lineTokens[];
			
			//========SET UP INPUT=============================================
			// Set up data input
			FileInputStream fstream = new FileInputStream(scannerFile);
			DataInputStream dstream = new DataInputStream(fstream); 
			BufferedReader br = new BufferedReader(new InputStreamReader(dstream));

			// Set up input list
			input = new LinkedList<String>();
			
			// Read line
			line = br.readLine();
			line = removeMultipleSpaces(line);
			
			if(line.contains("$"))
				throw new RuntimeException("\n The symbol $ found in the scanner output. $ is a reserved symbol.");							
			if( br.readLine() != null ) // File should only be one line long
				throw new RuntimeException("\n Scanner file greater than one line long.");
			
			// end file read
			dstream.close();
			
			lineTokens = line.split(" ");			
			for(String token : lineTokens)
			{
				input.addLast(token);
			}
			// Make sure input ends with $
			input.addLast("$");

		}
		catch(IOException e)
		{
			throw new RuntimeException("\n Scanner file read failed.");
		}
		

		//======================================================================
		//=======STEP 4: Parse the input========================================
		//======================================================================
		
		Parser.parse(input, parserGenerator.getParseTable());			
		System.out.println("Parse completed successfully! No errors detected.");
		
	}
	
	/**
	 * Removes repeated spaces from a string (i.e. if string contains "  " it will be shortened to " "
	 * @param string - string to have multiple spaces removed
	 * @return string without multiple spaces
	 */
	private static String removeMultipleSpaces(String string)
	{
		String oldString;
		
		do
		{
			oldString = string;
			string = string.replace("  ", " ");
		}while( !string.equals(oldString) );
		
		return string;
	}
}
