package parser;

import java.util.LinkedList;

/**
 * Class for a nonterminal rule.
 * 
 * @author Logan Blyth, James McCarty, & Robert Rayborn 
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
	private LinkedList<ProductionRule> productionRules;
	/**
	 * The follow set of the grammar rule.
	 */
	private LinkedList<TerminalRule> follow;
	/**
	 * Keeps track of the number of "primed" versions of the grammar rule.
	 * Used for cases of indirect recursion.
	 */
	private GlobalInteger primeCount;
	
	/**
	 * Chains to NonterminalRule(String sym, int pc) by assuming 
	 * the rule has no primed versions (i.e. A', B'', etc.)
	 * @param sym - name of grammar rule (ex A -> B C has sym = "A".). 
	 */
	public NonterminalRule(String sym)
	{
		this(sym,0);
	}
	
	/**
	 * Chains to NonterminalRule(String sym, GlobalInteger primeCount)
	 * then set the primeCount to input parameter pc.
	 * @param sym - name of grammar rule (ex. A -> B C has sym = "A".). 
	 * @param pc - the prime count (ex. if A'' exists then primeCount of A = 2).
	 */
	public NonterminalRule(String sym, int pc)
	{
		this(sym, null);
		this.primeCount = new GlobalInteger(pc);
	}

	/**
	 * Create a nonterminal node.
	 * @param sym - name of grammar rule (ex. A -> B C has sym = "A".). 
	 * @param primeCount - prime counter data structure
	 */
	private NonterminalRule(String sym, GlobalInteger primeCount)
	{
		this.symbol = sym;
		this.primeCount = primeCount;
		this.productionRules = new LinkedList<ProductionRule>();
		this.follow = new LinkedList<TerminalRule>();
	}
	
 	//=======Production Rule Functions========================================================
	
	/**
	 * Adds a new production rule to the grammar rule.
	 * @param productionRuleList - the rule to be added.
	 */
	public void addProductionRule(LinkedList<GrammarRule> productionRuleList)
	{
		this.productionRules.add(new ProductionRule(productionRuleList));
	}/**
	 * Adds a new production rule to the grammar rule.
	 * @param productionRule - the rule to be added.
	 */
	private void addProductionRule(ProductionRule productionRule)
	{
		this.productionRules.add(productionRule);
	}
	
	/**
	 * Sets the grammar rule's production rules to the input list.
	 * @param newRules - the new production rules.
	 */	
	private void setProductionRules(LinkedList<ProductionRule> newRules) 
	{
		this.productionRules = newRules;
	}
	
	/**
	 * Gets the production rules.
	 * @return - production rules.
	 */
	public LinkedList<ProductionRule> getProductionRules() 
	{
		return productionRules;
	}


 	//=======PRINTING/STRING FUNCTIONS==========================================
	
	//TODO: Decide what to print
	@Override
	public String toString()
	{
		return this.symbol;
		/*String ret = this.symbol+":  ";
		for(ProductionRule productionRule : this.productionRules)
		{
			for(GrammarRule rule : productionRule.getRule())
			{
				ret += rule.getSymbol() + " ";
			}
			ret += "| ";
		}
		return ret.substring(0, ret.length() - 3);
		*/
	}
	
 	public void printFirst()
 	{
 		
 		for(ProductionRule productionRule : this.productionRules)
 		{
 	 		System.out.println("First set for "+this.getSymbol()+" -> "+productionRule.toString()+":");
	 		System.out.print("    ");
	 		for(TerminalRule first : productionRule.getFirstList())
	 		{
	 	 		System.out.print(first.getSymbol()+", ");
	 		}
	 		System.out.println();
 		}
 	}
 	
 	public void printFollow()
 	{
 		System.out.print("Follow set for "+this.getSymbol()+": ");
 		
 		for(TerminalRule element : this.follow)
 		{
 	 		System.out.print(element.getSymbol()+", ");
 		}
 		System.out.println();
 	}
 	
	//======OTHER FUNCTIONS====================================================
 	
	@Override
	public boolean isTerminal()
	{
		return false;
	}
	
	@SuppressWarnings("unused")
	private GlobalInteger getPrimeCount() 
	{
		return primeCount;
	}

	private void setPrimeCount(GlobalInteger primeCount) 
	{
		this.primeCount = primeCount;
	}
 	
 	//=======SUB CLASSES========================================================

	/**
 	 * A wrapper for a simple integer.  Used to keep track
 	 * of the number of times we have primed a grammar rule.
 	 */
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
 	
 	/**
 	 * Data structure for a production rule.
 	 * Contains a linked list of rules and their corresponding firsts.
 	 */
 	@SuppressWarnings("unused")
	private class ProductionRule 
 	{
 		private LinkedList<GrammarRule> rule;
 		private LinkedList<TerminalRule> first;
 		
 		public ProductionRule()
 		{
 			this.rule = new LinkedList<GrammarRule>();
 			this.first = new LinkedList<TerminalRule>();
 		}

		ProductionRule(LinkedList<GrammarRule> rule)
 		{
 			this.rule = rule;
 			this.first = new LinkedList<TerminalRule>();
 		}
 		
 		//=======RULE STUFF===========
		
		public void add(GrammarRule rule)
 		{
 			this.rule.add(rule);
 		}
 		
 		public void addFirst(GrammarRule rule)
 		{
 			this.rule.addFirst(rule);
 		}
 		
 		public void addLast(GrammarRule rule)
 		{
 			this.rule.addLast(rule);
 		}
 		
 		public GrammarRule removeFirst()
 		{
 			return this.rule.removeFirst();
 		}
 		
 		public GrammarRule remove()
 		{
 			return this.rule.remove();
 		}
 		
 		public GrammarRule removeLast()
 		{
 			return this.rule.removeLast();
 		}
 		
 		public GrammarRule get(int i)
 		{
 			return this.rule.get(i);
 		}
 		
 		public GrammarRule getFirst()
 		{
 			return this.rule.getFirst();
 		}
 		
 		public GrammarRule getLast()
 		{
 			return this.rule.getLast();
 		}
 		
 		public boolean contains(GrammarRule rule)
 		{
 			return this.rule.contains(rule);
 		}
 		
 		public int size() {
			return this.rule.size();
		}
 		
 		public boolean isEmpty() {
			return this.rule.isEmpty();
		}
 		
 		//TODO: How to form string
 		public String toString()
 		{
 			String ret = "";
 			for(GrammarRule currentRule : this.rule)
 			{
 				ret += currentRule.getSymbol()+", ";
 			}
 			return ret.substring(0, ret.length()-2);
 		}
 		
 		//=========FIRST STUFF==============
 		
 		/**
 		 * unions first and (newFirsts - EPSILON) then returns whether there was an update.
 		 * @param update - if true function returns true, else function returns 
 		 * 			true if there was an update. Basically default update value.
 		 * @param newFirsts - new first elements.
 		 */
 		private boolean firstListUnion(boolean update, LinkedList<TerminalRule> newFirsts)
 		{
 			for(TerminalRule newFirst : newFirsts)
 			{
 				if ( (newFirst != EpsilonRule.getEpsilonRule()) && !this.first.contains(newFirst))
 				{
 					this.first.addLast(newFirst);
 					update = true;
 				}
 			}
 			return update;
 		}
 		
 		public void firstListAdd(TerminalRule rule)
 		{
 			this.first.add(rule);
 		}
 		
 		public boolean firstListContains(GrammarRule rule)
 		{
 			return this.first.contains(rule);
 		}
 		
 		//=========GETTERS AND SETTERS===========

		public LinkedList<GrammarRule> getRule() 
		{
			return rule;
		}

		public void setRule(LinkedList<GrammarRule> rule) 
		{
			this.rule = rule;
		}

		public LinkedList<TerminalRule> getFirstList() 
		{
			return first;
		}

		public void setFirstList(LinkedList<TerminalRule> first) 
		{
			this.first = first;
		} 		
 	}
 	
 	// =======================================================================
	// ============= Step II(a) functions ====================================
 	// =======================================================================
	
	/**
	 * Removes the self left recursion from a grammar rule, x, 
	 *   and returns the new additional grammar rule x-prime. 
	 * @return - rule-prime.
	 */
	public NonterminalRule removeSelfLeftRecursion()
	{
		LinkedList<ProductionRule> nonLeftRules = new LinkedList<ProductionRule>();
		LinkedList<ProductionRule> leftRules    = new LinkedList<ProductionRule>();
		
		// Collect the left recursion entries and non-left recursion entries
		for(ProductionRule productionRule : this.productionRules)
		{
			if(productionRule.get(0) == this) // self left recursion present
				leftRules.add(productionRule);
			else
				nonLeftRules.add(productionRule);
		}
		
		if(leftRules.isEmpty()) // no self-recursion present
			return null;
		
		// create rule-prime entry
		NonterminalRule rulePrime = 
			new NonterminalRule( this.primeString(this.symbol, this.primeCount.getInteger()+1) );		
		
		// add rule-prime entry to the end of the non left recursive rules
		//  i.e. A -> BC   becomes  A -> BCA'
		for(ProductionRule nonLeftRule : nonLeftRules)
		{
			if(!nonLeftRule.getFirst().equals(EpsilonRule.getEpsilonRule()))
				nonLeftRule.addLast(rulePrime);
		}
		
		ProductionRule epsilonProduction = new ProductionRule(EpsilonRule.getEpsilonList());
	
		// modify rules of the form A -> AB  to  A' -> BA'
		for(ProductionRule leftRule : leftRules)
		{
			leftRule.removeFirst();
			leftRule.addLast(rulePrime);
		}
		// add epsilon as a production rule of (what will be) rule-prime's production rules
		leftRules.add(epsilonProduction);
		// set rule-prime's production rules
		rulePrime.setProductionRules(leftRules);
		// set this grammar rule's new production rules
		this.setProductionRules(nonLeftRules);
		
		// increment the rules' prime count
		this.primeCount.increment();
		// set the new rule's prime count to be correct
		rulePrime.setPrimeCount(this.primeCount);
		
		// return the prime rule
		return rulePrime;
	}

	/**
	 * Remove the common prefixes from the production rules of the grammar rule.
	 * @return - new grammar rules, "primed" versions of the current grammar rule.
	 */
	public LinkedList<NonterminalRule> removeCommonPrefixes()
	{
		//TODO: Change to work with variable length similarity???
		
		LinkedList<ProductionRule> newProductionRules;
		ProductionRule prod1, prod2, newProductionRule;
		GrammarRule firstProd1;
		NonterminalRule newRule;
		LinkedList<NonterminalRule> newGrammarRules;
		boolean hasCommonPrefixes;
		
		newGrammarRules = new LinkedList<NonterminalRule>();
		
		// Loop through each combination of prod1 and prod2 and correct them if they have the same prefix
		for(int i = 0 ; i < this.productionRules.size() ; i++)
		{
			prod1 = this.productionRules.get(i);
			System.out.println("1: "+prod1);
			firstProd1 = prod1.getFirst();
			newProductionRules = new LinkedList<ProductionRule>();
			newRule = new NonterminalRule(this.primeString(this.symbol, this.primeCount.getInteger()));
			hasCommonPrefixes = false;
			
			for(int j = 0 ; j < i ; j++)
			{
				prod2 = this.productionRules.get(j);
				if(firstProd1.equals(prod2.getFirst())) // common prefix found
				{
					if(!hasCommonPrefixes)	// haven't yet found a common prefix
					{
						// update
						hasCommonPrefixes = true;
						// Create newProductionRule to be added to newProductionRules 
						newProductionRule = new ProductionRule();
						newProductionRule.add(newRule);
						newProductionRule.addFirst(firstProd1);
						// Add newProductionRule to newProductionRules
						newProductionRules.add(newProductionRule);
						// Remove the common prefix
						prod1.removeFirst();
						prod2.removeFirst();
						// Add the production rules to the new grammar rule
						if(prod1.isEmpty())
							newRule.addProductionRule(EpsilonRule.getEpsilonList());
						else
							newRule.addProductionRule(prod1);
						if(prod1.isEmpty())
							newRule.addProductionRule(EpsilonRule.getEpsilonList());
						else
							newRule.addProductionRule(prod2);
						// Increment the prime count
						this.primeCount.increment();
						newRule.setPrimeCount(this.primeCount);
					}
					else	// another common prefix found
					{
						// Remove the common prefix
						prod2.removeFirst();
						// Add the production rule to the new grammar rule
						if(prod2.isEmpty())
							newRule.addProductionRule(EpsilonRule.getEpsilonList());
						else
							newRule.addProductionRule(prod2);
					}
				}
				else // not a common prefix
				{
					// add the production rule to the new production rules unchanged
					newProductionRules.add(prod2);
				}
			}
			if(hasCommonPrefixes) // there were common prefixes
			{
				System.out.println("2: "+newProductionRules);
				// update the current production rules
				this.setProductionRules(newProductionRules);
				// add the new rule to the new grammar rules
				newGrammarRules.add(newRule);
			}
		}
		
		if(newGrammarRules.isEmpty()) // no changes have been made
			return null;
		else // need to update grammar rules
			return newGrammarRules;
	}

	/**
	 * Generate a primed version of the input string. 
	 * (i.e. if s = "A" and num = 2 then returns "A''".
	 * @param s - string to be primed
	 * @param num - number of primes to add
	 * @return
	 */
	private String primeString(String s, int num)
	{
		String removedPrimes = s.substring(0,s.length()- 1);
		int lengthMinOne = removedPrimes.length()-1;
		// remove the previous primes
		//   i.e. if s = "<ADD''> then removedPrimes will become "<ADD"
		while(removedPrimes.endsWith("'"))
		{
			removedPrimes = removedPrimes.substring(0,lengthMinOne);
			lengthMinOne--;
		}
		return removedPrimes + this.nPrimes(num) + ">";
	}
	
	/**
	 * Returns a string of n primes.
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


 	// =======================================================================
	// ============= Step II(b) functions ====================================
 	// =======================================================================
	
	/**
	 * Constructs the first set for the grammar rule.
	 * @param wasChanged - default value of the return boolean
	 * @return - true if the first set was modified or the input was already true.
	 */
	public boolean constructFirst(boolean wasChanged)
	{
		boolean containsEpsilon;
		
		for(ProductionRule productionRule : this.productionRules)
		{
			containsEpsilon = true;
			for(GrammarRule grammarRule : productionRule.getRule())
			{
				// union the current production rule's current grammar rule's first set with this nonterminal's first set
				wasChanged = productionRule.firstListUnion(wasChanged, grammarRule.getFirst());
				// if the current grammar rule doesn't contain EPSION
				if(!grammarRule.getFirst().contains(EpsilonRule.getEpsilonRule())) 
				{
					// then it is safe to not examine future grammar rules
					containsEpsilon = false;
					break;
				}
			}
			if(containsEpsilon) // all grammar rules in the current production rules contained EPSILON in their first set
			{
				productionRule.firstListAdd(EpsilonRule.getEpsilonRule());
			}
		}
		return wasChanged;
	}
	
	/**
	 * Constructs the follow set for the grammar rule.
	 * @param wasChanged - default value of the return boolean
	 * @return - true if the follow set was modified or the input was already true.
	 */
	public boolean constructFollow(boolean wasChanged)
	{
		int i, j, n;
		GrammarRule grammarRuleI, grammarRuleIPlus;
		boolean containsEpsilon;

		grammarRuleIPlus = null;		
		
		for(ProductionRule productionRule : this.productionRules)
		{
			n = productionRule.size();
			for(i = 0; i < n; i++)
			{
				grammarRuleI = productionRule.get(i);
				if(!grammarRuleI.isTerminal())
				{
					containsEpsilon = true;
					for(j = i+1; j < n; j++)
					{
						grammarRuleIPlus = productionRule.get(j);
						wasChanged = this.unionFollow(wasChanged, grammarRuleI.getFollow(), grammarRuleIPlus.getFirst());
						containsEpsilon = containsEpsilon && grammarRuleIPlus.getFirst().contains(EpsilonRule.getEpsilonRule());
						if(!containsEpsilon)
						{
							break;
						}
					}
					if(containsEpsilon)
					{
						wasChanged = this.unionFollow(wasChanged, grammarRuleI.getFollow(), this.getFollow());
					}
				}
			}
		}
		return wasChanged;
	}
	
	/**
	 * Unions the follow set with the new follow set
	 * @param update - default value of the return boolean.
	 * @param followList - follow set to be added to. 
	 * @param newFollowList - follow set to add.
	 * @return - true if the follow set was modified or the input was already true.
	 */
	private boolean unionFollow(boolean update, LinkedList<TerminalRule> followList, LinkedList<TerminalRule> newFollowList)
	{
		for(TerminalRule newFollow : newFollowList)
		{
			if ( !followList.contains(newFollow) 
					&& (newFollow!=EpsilonRule.getEpsilonRule()) )
			{
				followList.addLast(newFollow);
				update = true;
			}
		}
		return update;	
	}
	
	@Override
	public LinkedList<TerminalRule> getFirst()
	{
		LinkedList<TerminalRule> first = new LinkedList<TerminalRule>();
		for(ProductionRule productionRule : this.productionRules)
		{
			first.addAll(productionRule.getFirstList());
		}
		return first;
	}
	
	/**
	 * If the input terminal is in the first set of any of the production rules 
	 * it returns that production rule.  Otherwise returns null.
	 * @param terminal - terminal rule to find.
	 * @return - production rule that contains the input terminal rule.
	 */
	public LinkedList<GrammarRule> getProductionRuleForTerminal(TerminalRule terminal)
	{
		for(ProductionRule productionRule : this.productionRules)
		{
			if(productionRule.firstListContains(terminal))
				return productionRule.getRule();			
		}
		return null;
	}
	
	@Override
	public LinkedList<TerminalRule> getFollow()
	{
		return this.follow;
	}	
}
