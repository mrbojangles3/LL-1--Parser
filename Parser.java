

import java.util.LinkedList;

/**
 * Class that parses the input string using the built parse table.
 * 
 * @author Logan Blyth, James McCarty, & Robert Rayborn 
 *
 */

public class Parser 
{
	public static boolean parse(LinkedList<String> input, ParseTable parseTable)
	{		
		// prepare stack
		LinkedList<GrammarRule> stack = new LinkedList<GrammarRule>(); 
		stack.push(parseTable.getDollar());
		stack.push(parseTable.getStart());
		
		// set up variables
		int tokenNumber = 1;
		GrammarRule topRule;
		String inputToken;
		while (! (stack.isEmpty() && input.isEmpty()) )
		{
			topRule = stack.peek();
			inputToken = input.peek();
			
			// if terminal look up 
			if(topRule.isTerminal())
			{
				if(topRule.symbol.equals(inputToken)) // matching input and stack tokens
				{
					// pop off matching symbols
					input.pop();
					stack.pop();
					// update token number
					tokenNumber++;
				}
				else if(!topRule.equals(EpsilonRule.getEpsilonRule())) // the tokens are mismatched
					throw new RuntimeException("\n Parse error at input token " + tokenNumber + ": " +
							"Input token " + inputToken +
							" does not match with stack token " + topRule.getSymbol() + ".\n " + 
							currentStackString(stack));
				else //the token is epsilon so we should only pop the stack
					stack.pop();
			}
			
			// if nonterminal add rule to stack
			else
			{
				// get the parse table entry
				LinkedList<GrammarRule> expandedRule = parseTable.get( (NonterminalRule) topRule, inputToken);
				
				if(expandedRule == null) // there is no entry in the parse table
					throw new RuntimeException("\n Parse error at input token " + tokenNumber + ": " +
							"A parse table entry for " + inputToken + 
							" does not exist for grammar rule "+ topRule.getSymbol() + ".\n " + 
							currentStackString(stack));

				// add the new grammar rules to the stack
				stack.pop();
				for(int i = expandedRule.size() - 1; i>=0; i--)
				{
					stack.push(expandedRule.get(i));
				}
			}
		}
		// stack and input are both empty		
		return true;
	}
	
	/**\
	 * Returns a string representing the current stack
	 * @param list - the stack
	 * @return - string representation
	 */
	private static String currentStackString(LinkedList<GrammarRule> list)
	{
		String ret = "  Current Stack: \n    ";
		for(GrammarRule entry : list)
		{
			ret += entry.getSymbol() + " \n    "; 
		}
		return ret;
	}
}
