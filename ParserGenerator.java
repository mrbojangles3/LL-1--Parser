package parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Class generates the parse table.
 * 
 * @author Logan Blyth, James McCarty, & Robert Rayborn 
 *
 */

public class ParserGenerator
{
	private Hashtable<String, NonterminalRule> nonterminalRuleHT;
	private Hashtable<String, TerminalRule> terminalRuleHT;
	
	private LinkedList<NonterminalRule> nonterminalRules;
	private LinkedList<TerminalRule> terminalRules;

	private NonterminalRule startingRule;
	
	private ParseTable parseTable;
	
	/**
	 * Sets up class variables
	 * @param terminals - names of the terminals used
	 * @param nonterminals - names of the nonterminals used
	 * @param start - name of the starting nonterminal
	 */
	public ParserGenerator(String[] terminals, String[] nonterminals, String start)
	{
		// Create variables
		this.nonterminalRuleHT = new Hashtable<String, NonterminalRule>();
		this.terminalRuleHT = new Hashtable<String, TerminalRule>();
		this.nonterminalRules = new LinkedList<NonterminalRule>();
		this.terminalRules = new LinkedList<TerminalRule>();
		this.parseTable = new ParseTable();
		
		// set up terminal dollar rule and add it to the hash table
		TerminalRule dollar = new TerminalRule("$");
		this.terminalRuleHT.put("$", dollar);

		// add terminals to hash table
		for(String terminal : terminals)
		{
			if (terminal.equals(EpsilonRule.getEpsilonRule().getSymbol()))
				this.terminalRuleHT.put(terminal, EpsilonRule.getEpsilonRule());
			else
				this.terminalRuleHT.put(terminal, new TerminalRule(terminal));
		}
		
		// add nonterminals to hash table
		for(String nonterminal : nonterminals)
		{
			this.nonterminalRuleHT.put(nonterminal, new NonterminalRule(nonterminal));
		}
		
		// set up starting rule
		this.startingRule = this.nonterminalRuleHT.get(start);
		this.startingRule.getFollow().add(dollar);
	}
	
	public void addGrammarRule(String symbol, String[] productionRuleStrings)
	{
		if( !this.nonterminalRuleHT.containsKey(symbol) ) // the symbol doesn't have a grammar rule yet 
		{
			//TODO: Throw Error
		}
		else if(symbol.equals("$"))
		{
			//TODO: Throw Error
		}
		
		// generate a production rule for the new symbol
		LinkedList<GrammarRule> productionRule = new LinkedList<GrammarRule>(); 
		for(String productionRuleString : productionRuleStrings)
		{
			if(this.nonterminalRuleHT.containsKey(productionRuleString))
				productionRule.add(this.nonterminalRuleHT.get(productionRuleString));
			else if(this.terminalRuleHT.containsKey(productionRuleString))
				productionRule.add(this.terminalRuleHT.get(productionRuleString));				
			else
			{
				//TODO: Throw Error
			}
		}
		// add the production rule to the symbol's grammar rule
		this.nonterminalRuleHT.get(symbol).addProductionRule(productionRule);
	}
	
	public void buildParseTable()
	{
		this.stepIIa();
		this.stepIIb();
	}
	
	public ParseTable getParseTable()
	{
		return this.parseTable;
	}
	
	//=======PRINTING FUNCTIONS=======================
	
	public void printHash()
	{
		for( int i = 0 ; i < this.nonterminalRules.size() ; i++ )
		{
			System.out.println(this.nonterminalRules.get(i).toString());
		}
	}
	
	public void printFirst()
	{
		for(NonterminalRule nonterminal : this.nonterminalRules)
		{
			nonterminal.printFirst();
		}		
	}
	
	public void printFollow()
	{
		for(NonterminalRule nonterminal : this.nonterminalRules)
		{
			nonterminal.printFollow();
		}		
	}
	
	public void printParseTable()
	{
		for(NonterminalRule nonterminal : this.nonterminalRules)
		{
			System.out.println("NONTERM: "+nonterminal.getSymbol());
			for(TerminalRule terminal : this.terminalRules)
			{
				System.out.println("  TERM: "+terminal.getSymbol());
				System.out.println("    "+this.parseTable.get(nonterminal, terminal));
			}
			System.out.println();
		}
	}
	
	//================================================================
	//============== Step II(a) stuff ================================
	//================================================================
	
	/**
	 * Runs all the step IIa stuff
	 */
	public void stepIIa()
	{
		System.out.println("IIa1");
		this.convertHashTablesToLists();
		System.out.println("IIa2");
		this.removeSelfLeftRecursion();
		System.out.println("IIa3");
		this.removeCommonPrefixes();
	}
	
	public void convertHashTablesToLists()
	{
		this.nonterminalRules.addAll(this.nonterminalRuleHT.values());
		this.terminalRules.addAll(this.terminalRuleHT.values());
		// no longer need hash tables
		this.nonterminalRuleHT.clear();
		this.terminalRuleHT.clear();
	}
	
	public void removeSelfLeftRecursion()
	{
		NonterminalRule newRule, current;
		for(int i = 0 ; i < this.nonterminalRules.size() ; i++)
		{
			current = this.nonterminalRules.get(i);
			newRule = current.removeSelfLeftRecursion();
			if(newRule != null){
				this.nonterminalRules.addLast(newRule);
			}
		}
	}
	
	public void removeCommonPrefixes()
	{
		LinkedList<NonterminalRule> newGrammarRules;
		for(int i = 0 ; i < this.nonterminalRules.size() ; i++)		// can't do for each since list is changing
		{
			newGrammarRules = this.nonterminalRules.get(i).removeCommonPrefixes();
			if(newGrammarRules != null){
				this.nonterminalRules.addAll(newGrammarRules);
			}
		}
	}

	//================================================================
	//============== Step II(b) stuff ================================
	//================================================================
	
	/**
	 * Runs all the step IIb stuff
	 */
	public void stepIIb()
	{
		System.out.println("IIb1");
		this.constructFirst();
		System.out.println("IIb2");
		this.constructFollow();
		System.out.println("IIb3");
		this.createParseTable();
	}
	
	public void constructFirst()
	{
		boolean wasChanged = true;
		
		while(wasChanged)
		{
			wasChanged = false;
			for(NonterminalRule nonterminal : this.nonterminalRules)
			{
				wasChanged = nonterminal.constructFirst(wasChanged);
			}
		}
		this.printFirst();
	}
	
	public void constructFollow()
	{
		boolean wasChanged = true;
		while(wasChanged)
		{
			wasChanged = false;
			for(NonterminalRule nonterminal : this.nonterminalRules)
			{
				wasChanged = nonterminal.constructFollow(wasChanged);
			}
		}
	}
	
	public void createParseTable()
	{		
		this.terminalRules.remove(EpsilonRule.getEpsilonRule());
		this.parseTable = new ParseTable();
		for(NonterminalRule nonterminal : this.nonterminalRules)
		{
			for(TerminalRule terminal : this.terminalRules)
			{
				this.parseTable.put(nonterminal, terminal, nonterminal.getProductionRuleForTerminal(terminal));
			}
			if(nonterminal.getProductionRuleForTerminal(EpsilonRule.getEpsilonRule()) != null)
			{
				LinkedList<TerminalRule> followList = nonterminal.getFollow();
				for(TerminalRule followEntry : followList)
				{
					this.parseTable.put(nonterminal, followEntry, EpsilonRule.getEpsilonList());
				}
			}
		}
	}
	
	public void saveParseTable(String fileName) throws IOException
	{
	    FileWriter fstream = new FileWriter(fileName);
	    BufferedWriter out = new BufferedWriter(fstream);
	    
	    // Line 1
	    for(TerminalRule terminal : this.terminalRules)
		{
			out.write(","+terminal.getSymbol());
		}
	    out.write("\n");
	    
	    // Other lines
	    LinkedList<GrammarRule> productionRule;
		for(NonterminalRule nonterminal : this.nonterminalRules)
		{
		    out.write(nonterminal.getSymbol());
			for(TerminalRule terminal : this.terminalRules)
			{
				productionRule = this.parseTable.get(nonterminal,terminal);
				if(productionRule == null)
					out.write(",");
				else
					out.write(","+productionRule.toString().replace(",", " "));
			}
		    out.write("\n");
		}
	    out.close();
	}
}

