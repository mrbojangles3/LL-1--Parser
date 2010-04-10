package parser;

import java.util.LinkedList;

/**
 * Class for a nonterminal rule.
 *   i.e. A -> B | C
 * 
 * @author Robert Rayborn, James McCarty, & Logan Blyth
 *
 */

public class NonterminalRule extends GrammarRule
{
	/**
	 * Stores all of the production rules in a two dimensional linked list.
	 * The first dimension represents the production rules separated by an or.
	 *   i.e. A -> BC | DE
	 *   dimension one entries = { listBC, listDE }
	 * The second dimension represents the grammar rules contained inside each production rule
	 *   i.e A -> BC | DE
	 *   dimension two entries in A.get(0) = { B, C }
	 */
	private LinkedList<LinkedList<GrammarRule>> productionRules;
	// might want to make this a hash table
	/**
	 * The first set of the grammar rule.
	 */
	private LinkedList<GrammarRule> first; 
	/**
	 * The follow set of the grammar rule.
	 */
	private LinkedList<GrammarRule> follow;
	/**
	 * The epsilon token.
	 */
	private static GrammarRule epsilon = new TerminalRule("epsilon");
	/**
	 * Keeps track of the number of "primed" versions of the grammar rule.
	 * Used for cases of indirect recursion.
	 */
	private GlobalInteger primeCount;
	
	/**
	 * Sets up the epsilon token.  Needs to be done if there are any epsilons in our original grammar.
	 * @param eps
	 */
	public static void setEpsilon(GrammarRule eps)
	{
		epsilon = eps;
	}
	
	public NonterminalRule(String sym)
	{
		this(sym,0);
	}

	public NonterminalRule(String sym, int pc)
	{
		this(sym, null);
		this.primeCount = new GlobalInteger(pc);
	}

	public NonterminalRule(String sym, GlobalInteger primeCount)
	{
		this.symbol = sym;
		this.primeCount = primeCount;
		this.productionRules = new LinkedList<LinkedList<GrammarRule>>();
		this.first = new LinkedList<GrammarRule>();
		this.follow = new LinkedList<GrammarRule>();
	}
	
	/**
	 * Adds a new production rule to the grammar rule.
	 * i.e. if we have A -> BC and want to add the production rule for A -> DE
	 *      then we input a linked list productionRule = {D, E}
	 *      and it will be added so that A -> BC | DE.
	 * @param productionRule - the rule to be added.
	 */
	public void addProductionRule(LinkedList<GrammarRule> productionRule)
	{
		this.productionRules.add(productionRule);
	}
	
	/**
	 * Adds a group of new production rules to the grammar rule in
	 * the same way as the addProductionRule function.
	 * @param productionRule - the rules to be added.
	 */
	public void addProductionRules(LinkedList<LinkedList<GrammarRule>> productionRules)
	{
		this.productionRules.addAll(productionRules);
	}

	/**
	 * Sets the grammar rule's production rules to the input list.
	 * @param productionRules - the new production rules.
	 */
	private void setProductionRules(LinkedList<LinkedList<GrammarRule>> productionRules) 
	{
		for(LinkedList<GrammarRule> productionRule : productionRules)
			if (productionRule.contains(null))
				System.out.println("HERE4");		
		this.productionRules = productionRules;
	}
	
	/**
	 * Gets the production rules.
	 * @return - production rules.
	 */
	public LinkedList<LinkedList<GrammarRule>> getProductionRules() 
	{
		return productionRules;
	}

	@Override
	public boolean isTerminal()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		String ret = this.symbol+":  ";
		for(LinkedList<GrammarRule> current1 : this.productionRules)
		{
			if (current1.contains(null))
				System.out.println("HERE3");
			for(GrammarRule current2 : current1)
			{
				ret += current2.getSymbol() + " ";
			}
			ret += "| ";
		}
		return ret.substring(0, ret.length() - 3);
	}

	// ============= Step II(a) functions ===============================
	
	/**
	 * THIS PERTAINS TO INDIRECT LEFT RECURSION AND IS NOT NECESSARY FOR EXAMPLE GRAMMARS
	 * Removes the left recursion from an this grammar rule for a given input grammar rule.
	 *  
	 * @return - the rule to eliminate from the production rules of this rule.
	 */
	@SuppressWarnings("unchecked")
	public void removeLeftRecursion(NonterminalRule rule)
	{
		LinkedList<LinkedList<GrammarRule>> nonLeftRules = new LinkedList<LinkedList<GrammarRule>>();
		LinkedList<LinkedList<GrammarRule>> leftRules    = new LinkedList<LinkedList<GrammarRule>>();
		
		// Find left recursion and non-left recursion production rules
		for(LinkedList<GrammarRule> productionRule : this.productionRules)
		{
			if(productionRule.get(0) == rule) // left recursion present
				leftRules.add(productionRule);
			else
				nonLeftRules.add(productionRule);
		}
		
		if(leftRules.isEmpty()) // no left recursion
			return;
		
		// remove the left recursion
		for(LinkedList<GrammarRule> leftRule : leftRules)
		{
			leftRule.removeFirst();
			for(LinkedList<GrammarRule> productionRuleToCopy : rule.getProductionRules())
			{
				LinkedList<GrammarRule> productionRule = (LinkedList<GrammarRule>) productionRuleToCopy.clone();
				productionRule.addAll(leftRule);
				nonLeftRules.add(productionRule);
			}		
		}
		this.setProductionRules(nonLeftRules);
	}
	
	/**
	 * Removes the self left recursion from a grammar rule, x, 
	 *   and returns the new additional grammar rule x-prime. 
	 * @return - rule-prime
	 */
	public NonterminalRule removeSelfLeftRecursion()
	{
		LinkedList<LinkedList<GrammarRule>> nonLeftRules = new LinkedList<LinkedList<GrammarRule>>();
		LinkedList<LinkedList<GrammarRule>> leftRules    = new LinkedList<LinkedList<GrammarRule>>();
		
		// Find left recursion and non-left recursion entries
		for(LinkedList<GrammarRule> current : this.productionRules)
		{
			if(current.get(0) == this) // self left recursion present
				leftRules.add(current);
			else
				nonLeftRules.add(current);
		}
		
		if(leftRules.isEmpty()) // no self-recursion present
			return null;
		
		// create rule-prime entry
		NonterminalRule rulePrime = 
			new NonterminalRule(this.primeString(this.symbol, this.primeCount.getInteger()+1));		
		
		// add rule-prime entry to the end of the non left recursive rules
		//  i.e. A -> BC   becomes  A -> BCA'
		for(LinkedList<GrammarRule> nonLeftRule : nonLeftRules)
		{
			nonLeftRule.addLast(rulePrime);
		}
		
		LinkedList<GrammarRule> epsilonList = new LinkedList<GrammarRule>();
		epsilonList.add(epsilon);		
		if(nonLeftRules.size() > 0) // we need a new rule-prime entry to eliminate self-recursion
		{
			// modify rules of the form A -> AB  to  A' -> BA'
			for(LinkedList<GrammarRule> leftRule : leftRules)
			{
				leftRule.removeFirst();
				leftRule.addLast(rulePrime);
			}
			// add epsilon as a production rule of rule-prime
			leftRules.add(epsilonList);
			// set rule-prime to point at its production rules
			rulePrime.setProductionRules(leftRules);
			// set the grammar rule to point at its production rules
			this.setProductionRules(nonLeftRules);
			// increment the rules' prime count
			this.primeCount.increment();
			// return the prime rule
			return rulePrime;
		}
		else // we don't need a new rule-prime entry to eliminate self-recursion
			 // instead we can just overwrite the current grammar rule's production rules
		{
			for(LinkedList<GrammarRule> leftRule : leftRules)
			{
				leftRule.removeFirst();
				leftRule.addLast(rulePrime);
			}
			// add epsilon as a production rule of the grammar rule
			leftRules.add(epsilonList);
			// set the grammar rule to point at its production rules
			this.addProductionRule(epsilonList);
			return null;
		}
	}

	/**
	 * Remove the common prefixes from the production rules of the grammar rule.
	 * @return - new grammar rules, "primed" versions of the current grammar rule.
	 */
	public LinkedList<NonterminalRule> removeCommonPrefixes()
	{
		LinkedList<LinkedList<GrammarRule>> newOrList;
		LinkedList<GrammarRule> c1, c2, newRuleProductionRule;
		GrammarRule firstC1;
		NonterminalRule newRule;
		
		LinkedList<NonterminalRule> newGrammarRules = new LinkedList<NonterminalRule>();
		newRuleProductionRule = new LinkedList<GrammarRule>();
		
		for(int i = 0 ; i < this.productionRules.size() ; i++)
		{
			c1 = this.productionRules.get(i);
			newRule = new NonterminalRule(this.primeString(this.symbol, this.primeCount.getInteger()+1));
			firstC1 = null;
			newOrList = new LinkedList<LinkedList<GrammarRule>> ();
			
			for(int j = 0 ; j < i ; j++)
			{
				c2 = this.productionRules.get(j);
				if( (firstC1 == null) && (c1.getFirst() == c2.getFirst()) )
				{
					newRuleProductionRule = new LinkedList<GrammarRule>();
					newRuleProductionRule.add(newRule);
					newRuleProductionRule.addFirst(c1.getFirst());
					this.primeCount.increment();					
					newOrList.add(newRuleProductionRule);
					firstC1 = c1.removeFirst();
					c2.removeFirst();
					newRule.addProductionRule(c1);
					newRule.addProductionRule(c2);
				}
				else if(firstC1 == c2.getFirst())
				{
					c2.removeFirst();
					newRule.addProductionRule(c2);
				}
				else
				{
					newOrList.add(c2);
				}
			}
			if(firstC1 != null)
			{
				this.setProductionRules(newOrList);
				newGrammarRules.add(newRule);
			}
		}
		
		if(newGrammarRules.isEmpty())
			return null;
		else
			return newGrammarRules;
	}

	/**
	 * Generate a primed version of the input string.
	 * @param s
	 * @param num
	 * @return
	 */
	private String primeString(String s, int num)
	{
		String removedPrimes = s.substring(0,s.length()-2);
		int lengthMinTwo = removedPrimes.length() - 2;
		// remove the previous primes
		//   i.e. if s = "<ADD''> then removedPrimes will become "<ADD"
		while(removedPrimes.endsWith("'"))
		{
			removedPrimes = removedPrimes.substring(lengthMinTwo);
			lengthMinTwo--;
		}
		return removedPrimes + this.nPrimes(num) + ">";
	}
	
	/**
	 * Returns a string with n primes.
	 * @param n - number of ' characters
	 * @return - string of prime characters  i.e. '''''
	 */
	private String nPrimes(int n)
	{
		String ret = "";
		for(int i = 1 ; i <= n ; i++)
		{
			ret = ret+"'";
		}
		return ret;
	}

	// ============= Step II(b) functions ===============================
	
	/**
	 * Constructs the first set for the grammar rule.
	 * @param wasChanged - default value of the return boolean
	 * @return - true if the first set was modified or the input was already true.
	 */
	public boolean constructFirst(boolean wasChanged)
	{
		int i,n;
		
		for(LinkedList<GrammarRule> productionRule : this.productionRules)
		{
			i = 0;
			n = productionRule.size();
			for(GrammarRule grammarRule : productionRule)
			{
				wasChanged = this.unionNoEpsilon(wasChanged, this.first, grammarRule.getFirst());
				if(!grammarRule.getFirst().contains(epsilon))
				{
					break;
				}
				i++;
			}
			if( (i == n) && !(this.first.contains(epsilon)) )
			{
				this.first.addFirst(epsilon);
			}
		}
		return wasChanged;
	}
	
	public boolean constructFollow(boolean wasChanged)
	{
		int i, j, n;
		GrammarRule grammarRule1, grammarRule2;
		grammarRule2 = null;
		
		for(LinkedList<GrammarRule> productionRule : this.productionRules)
		{
			n = productionRule.size();
			for(i = 0; i < n-1; i++)
			{
				grammarRule1 = productionRule.get(i);
				if(!grammarRule1.isTerminal())
				{
					for(j = i+1; j < n; j++)
					{
						grammarRule2 = productionRule.get(j);
						wasChanged = this.unionNoEpsilon(wasChanged, grammarRule1.getFollow(), grammarRule2.getFirst());
						if(!grammarRule2.getFirst().contains(epsilon))
						{
							break;
						}
					}
					if(grammarRule2.getFirst().contains(epsilon))
					{
						System.out.println("HERE: "+grammarRule1.getSymbol()+" "+this.getSymbol());
						wasChanged = this.unionNoEpsilon(wasChanged, grammarRule1.getFollow(), this.getFollow());
					}
				}
			}
		}
		return wasChanged;
	}
	
	private boolean unionNoEpsilon(boolean update, LinkedList<GrammarRule> l1, LinkedList<GrammarRule> l2)
	{
		l1.addFirst(epsilon);
		update = this.union(update, l1, l2);
		l1.removeFirst();
		return update;		
	}
	
	private boolean union(boolean update, LinkedList<GrammarRule> l1, LinkedList<GrammarRule> l2)
	{
		for(GrammarRule element : l2)
		{
			if (!l1.contains(element))
			{
				l1.addLast(element);
				update = true;
			}
		}
		return update;
	}
	
	@Override
	public LinkedList<GrammarRule> getFirst()
	{
		return this.first;
	}
	
	@Override
	public LinkedList<GrammarRule> getFollow()
	{
		return this.follow;
	}
	
	// ====================================================
	
 	private class GlobalInteger
	{
		private int integer;

		public GlobalInteger(int integer)
		{
			this.integer = integer;
		}
		
		public void increment()
		{
			this.integer++;
		}

		public int getInteger()
		{
			return integer;
		}		
	}
 	
 	public void printFirst()
 	{
 		System.out.print("First set for "+this.getSymbol()+": ");
 		
 		for(GrammarRule element : this.first)
 		{
 	 		System.out.print(element.getSymbol()+", ");
 		}
 		System.out.println();
 	}
 	
 	public void printFollow()
 	{
 		System.out.print("Follow set for "+this.getSymbol()+": ");
 		
 		for(GrammarRule element : this.follow)
 		{
 	 		System.out.print(element.getSymbol()+", ");
 		}
 		System.out.println();
 	}
}
