package parser;

import java.util.LinkedList;

/**
 * Class for a terminal node a.k.a. a token.
 * 		i.e. EPSILON, INT, PLUS, etc.
 * 
 * @author Robert Rayborn, James McCarty, & Logan Blyth
 *
 */

public class TerminalRule extends GrammarRule 
{
	public TerminalRule(String sym)
	{
		this.symbol = sym;
	}

	@Override
	public boolean isTerminal() 
	{
		return true;
	}
	
	@Override
	public LinkedList<GrammarRule> getFirst()
	{
		LinkedList<GrammarRule> first = new LinkedList<GrammarRule>();
		first.add(this);
		return first;
	}

	/**
	 * This function should never be called. 
	 */
	@Override
	public LinkedList<GrammarRule> getFollow() 
	{
		return null;
	}

	@Override
	public String toString()
	{
		return this.symbol;
	}
}
