package parser;
import java.util.Hashtable;
import java.util.LinkedList;

public class RulesHash 
{
	private Hashtable<String, NonterminalRule> nonterminalRuleHT;
	private Hashtable<String, TerminalRule> terminalRuleHT;
	private LinkedList<NonterminalRule> nonterminalRules;
	private LinkedList<TerminalRule> terminalRules;
	private TerminalRule epsilon;
	private TerminalRule dollar;
	
	public RulesHash()
	{
		this.nonterminalRuleHT = new Hashtable<String, NonterminalRule>();
		this.terminalRuleHT = new Hashtable<String, TerminalRule>();
		this.nonterminalRules = new LinkedList<NonterminalRule>();
		this.terminalRules = new LinkedList<TerminalRule>();

		this.epsilon = new TerminalRule("epsilon");
		NonterminalRule.setEpsilon(epsilon);
		this.terminalRuleHT.put("epsilon", this.epsilon);
		this.dollar = new TerminalRule("$");
		this.terminalRuleHT.put("$", this.dollar);
	}
	
	public void addGrammarRule(String newSymbol, String[] productionRuleStrings)
	{
		// generate a production rule for the new symbol
		LinkedList<GrammarRule> productionRule = new LinkedList<GrammarRule>(); 
		for(String productionRuleString : productionRuleStrings)
		{
			if( productionRuleString.startsWith("<") &&
					productionRuleString.endsWith(">") )
			{
				if (!this.nonterminalRuleHT.containsKey(productionRuleString))
					this.nonterminalRuleHT.put(productionRuleString, new NonterminalRule(productionRuleString));
				productionRule.add(this.nonterminalRuleHT.get(productionRuleString));
			}
			else
			{
				if (!this.terminalRuleHT.containsKey(productionRuleString))
					this.terminalRuleHT.put(productionRuleString, new TerminalRule(productionRuleString));
				productionRule.add(this.terminalRuleHT.get(productionRuleString));				
			}			
		}
		
		
		if( !this.nonterminalRuleHT.containsKey(newSymbol) ) // the symbol doesn't have a grammar rule yet 
		{
			// a create new grammar rule 
			this.nonterminalRuleHT.put(newSymbol, new NonterminalRule(newSymbol));
		}
		// add the production rule to the grammar rule
		this.nonterminalRuleHT.get(newSymbol).addProductionRule(productionRule);
	}
	
	public void printHash()
	{
		for( int i = 0 ; i < this.nonterminalRules.size() ; i++ )
		{
			System.out.println(this.nonterminalRules.get(i).toString());
		}
	}
	
	// ============== Step II(a) stuff ==============
	
	public void stepIIa()
	{
		this.convertHashTablesToLists();
		this.removeSelfLeftRecursion();
		this.removeCommonPrefixes();
	}
	
	public void convertHashTablesToLists()
	{
		this.nonterminalRules.addAll(this.nonterminalRuleHT.values());
		this.terminalRules.addAll(this.terminalRuleHT.values());
	}
	
	private void removeLeftRecursion(boolean hasIndirectRecursion)
	{
		NonterminalRule newRule, current;
		for(int i = 0 ; i < this.nonterminalRules.size() ; i++)
		{
			current = this.nonterminalRules.get(i);
			if(hasIndirectRecursion)
			{
			// Remove indirect Recursion
				for(int j = 0 ; j < i ; j++)
				{
					current.removeLeftRecursion(this.nonterminalRules.get(j));
				}
			}				
			// Remove self recursion
			newRule = current.removeSelfLeftRecursion();
			if(newRule != null){
				this.nonterminalRules.addLast(newRule);
			}
		}
	}
	
	public void removeSelfLeftRecursion()
	{
		this.removeLeftRecursion(false);
	}
	
	public void removeLeftRecursion()
	{
		this.removeLeftRecursion(true);
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

	// ============== Step II(b) stuff ==============

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
			// this.printFollow();
			// System.out.println();
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
}
