package parser;

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
		
		String rulesFile, scannerFile, parseTableFile;
		ParserGenerator parserGenerator;
		LinkedList<String> input;

		// Assign file names
		if(args.length == 0)
		{
			// Set default paramenters
			scannerFile = "scan.txt";
			rulesFile = "parse.txt";
			parseTableFile = "parseTable.csv";
		}
		else if(args.length == 3)
		{
			// Assign any input parameter
			scannerFile = args[0];
			rulesFile = args[1];
			parseTableFile = args[2];
		}
		else
		{
			return;
			//TODO: Throw error
		}		
		
		//======================================================================
		//=======STEP 1: LOADING DATA FOR PARSE TABLE BUILD=====================
		//======================================================================
		try
		{
			String line;
			String lineTokens[], terminalTokens[], nonterminalTokens[], startToken;
			
			// Set up file input
			FileInputStream fstream = new FileInputStream(rulesFile);
			DataInputStream dstream = new DataInputStream(fstream); 
			BufferedReader br = new BufferedReader(new InputStreamReader(dstream));

			// Read "Tokens" line
			line = br.readLine();
			line = removeMultipleSpaces(line);
			lineTokens = line.trim().split(" ");
			if (!lineTokens[0].equals("%Tokens"))
				//TODO: Specify exception
				throw new Exception();
			terminalTokens = new String[lineTokens.length-1];
			for(int i=0; i<terminalTokens.length; i++)
				terminalTokens[i] = lineTokens[i+1].trim();
			
			// Read "Non-terminal" line
			line = br.readLine();
			line = removeMultipleSpaces(line);
			lineTokens = line.trim().split(" ");
			if (!lineTokens[0].equals("%Non-terminals"))
				//TODO: Specify exception
				throw new Exception();
			nonterminalTokens = new String[lineTokens.length-1];
			for(int i=0; i<nonterminalTokens.length; i++)
				nonterminalTokens[i] = lineTokens[i+1].trim();
			
			// Start line
			line = br.readLine();
			line = removeMultipleSpaces(line);
			lineTokens = line.trim().split(" ");
			if (!lineTokens[0].equals("%Start"))
				//TODO: Specify exception
				throw new Exception();
			startToken = lineTokens[1].trim();

			// Create parse table builder			
			parserGenerator = new ParserGenerator(terminalTokens,nonterminalTokens,startToken);
			
			// Rule line
			line = br.readLine();
			line = removeMultipleSpaces(line);
			lineTokens = line.trim().split(" ");
			if (!lineTokens[0].equals("%Rules"))
				//TODO: Specify exception
				throw new Exception();
			
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
		catch(Exception e)
		{
			//TODO: Specify exception handling
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
			return;
		}

		//======================================================================
		//=======STEP 2: BUILD THE PARSE TABLE==================================
		//======================================================================
		try
		{
			parserGenerator.buildParseTable();
			parserGenerator.saveParseTable(parseTableFile);
		}
		catch(Exception e)
		{
			//TODO: Specify exception handling
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
			return;
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
			
			if( br.readLine() != null ) // File should only be one line long
				//TODO: Specify exception 
				;
			
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
		catch(Exception e)
		{
			//TODO: Specify exception handling
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
			return;
		}
		

		//======================================================================
		//=======STEP 3: LOAD THE INPUT DATA====================================
		//======================================================================
		
		try
		{
			Parser.parse(input, parserGenerator.getParseTable());			
		}
		catch(Exception e)
		{
			//TODO: Specify exception handling
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
			return;
		}
		System.out.println("Complete");
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
