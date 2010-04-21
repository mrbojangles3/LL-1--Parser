import java.util.LinkedList;

/**
 * Abstract class for a grammar rule.
 * Implementations include both terminal rules (i.e. tokens) and nonterminal rules (i.e. A -> B | C).
 * 
 * @author Logan Blyth, James McCarty, & Robert Rayborn 
 *
 */

public abstract class GrammarRule 
{
	/**
	 * The string associated with the grammar rule.
	 * For terminals this is the string value itself (i.e. "INT").
	 * For nonterminals this is the value of the name of the grammar rule.
	 *    i.e. if the grammar rule is A -> B | C then symbol is "A".
	 */
	protected String symbol;

	/**
	 * Tells whether the grammar rule is terminal.
	 * @return - true if the grammar rule is terminal, false otherwise
	 */
	public abstract boolean isTerminal();
	
	/**
	 * Gets the first set.
	 * @return a linked list of the first set for the grammar rule.
	 */
	public abstract LinkedList<TerminalRule> getFirst();
	
	/**
	 * Gets the follow set.
	 * @return a linked list of the follow set for the grammar rule.
	 */
	public abstract LinkedList<TerminalRule> getFollow();
	
	/**
	 * Gets the string associated with the grammar rule.
	 * @return - the symbol
	 */
	public String getSymbol() 
	{
		return symbol;
	}
	
	/**
	 * Returns true if the rules have the same symbol.
	 * @param other the other GrammarRule
	 * @return
	 */
	public boolean equals(GrammarRule other)
	{
		return ( other.getSymbol().equals(this.getSymbol()) );
	}
	
	@Override
	public abstract String toString();
}
