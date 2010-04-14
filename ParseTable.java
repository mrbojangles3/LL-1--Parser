package parser;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Data structure for the parse table
 * 
 * @author Logan Blyth, James McCarty, & Robert Rayborn 
 *
 */

public class ParseTable
{
	/**
	 * Hash table that contains all the entries in the table
	 * Key is given by the concatenation of the nonterminal symbol string 
	 * and the terminal symbol string.
	 */
	private Hashtable<String,LinkedList<GrammarRule>> entries;
	
	public ParseTable()
	{
		entries = new Hashtable<String,LinkedList<GrammarRule>>();
	}
	
	/**
	 * Adds the grammar rule to the slot corresponding to the keys. Does not add if the input rule is null.
	 * @param nonterminalKey - nonterminal rule.
	 * @param terminalKey - terminal rule.
	 * @param value - production rule list.
	 */
	public void put(NonterminalRule nonterminalKey, TerminalRule terminalKey, LinkedList<GrammarRule> value)
	{
		if(value==null)
			return;
		this.entries.put(nonterminalKey.getSymbol()+terminalKey.getSymbol(), value);
	}
	
	/**
	 * Gets the production rule list at the slot corresponding to the keys.
	 * @param nonterminalKey - nonterminal list.
	 * @param terminalKey - terminal list.
	 * @return production rule list.
	 */
	public LinkedList<GrammarRule> get(NonterminalRule nonterminalKey, TerminalRule terminalKey)
	{
		return this.entries.get(nonterminalKey.getSymbol()+terminalKey.getSymbol());
	}
	
	/**
	 * Removes the production rule list at the slot corresponding to the keys.
	 * @param nonterminalKey - nonterminal list.
	 * @param terminalKey - terminal list.
	 * @return production rule list.
	 */
	public LinkedList<GrammarRule> remove(NonterminalRule nonterminalKey, TerminalRule terminalKey)
	{
		return this.entries.remove(nonterminalKey.getSymbol()+terminalKey.getSymbol());
	}
}
