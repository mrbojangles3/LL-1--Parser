package parser;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RulesHash rh = new RulesHash();
		/*
		String[][] entrys = {
				// <Tiny-program>
				{"begin", "<statement-list>", "end"},
				// <statement-list>
				{"<statement-list>", "<statement>"}, 
				{"<statement>"}, 
				// <statement>
				{"print", "(", "<exp-list>", ")", ";"}, 
				{"ID", ":=", "<exp>", ";"}, 
				{"read", "(", "<id-list>", ")", ";"}, 
				// <id-list>
				{"<id-list>", ",", "ID"}, 
				{"ID"}, 
				// <exp-list>
				{"<exp-list>", ",", "<exp>"}, 
				{"<exp>"},
				// <exp>
				{"ID"}, 
				{"INTNUM"}, 
				{"(", "<exp>", ")"}, 
				{"<exp>", "<bin-op>", "<exp>"}, 
				// <bin-op>
				{"+"}, 
				{"-"}, 
				{"*"}, 
				{"%"}, 
				};
		rh.addGrammarRule("<Tiny-program>", entrys[0]);
		rh.addGrammarRule("<statement-list>", entrys[1]);
		rh.addGrammarRule("<statement-list>", entrys[2]);
		rh.addGrammarRule("<statement>", entrys[3]);
		rh.addGrammarRule("<statement>", entrys[4]);
		rh.addGrammarRule("<statement>", entrys[5]);
		rh.addGrammarRule("<id-list>", entrys[6]);
		rh.addGrammarRule("<id-list>", entrys[7]);
		rh.addGrammarRule("<exp-list>", entrys[8]);
		rh.addGrammarRule("<exp-list>", entrys[9]);
		rh.addGrammarRule("<exp>", entrys[10]);
		rh.addGrammarRule("<exp>", entrys[11]);
		rh.addGrammarRule("<exp>", entrys[12]);
		rh.addGrammarRule("<exp>", entrys[13]);
		rh.addGrammarRule("<bin-op>", entrys[14]);
		rh.addGrammarRule("<bin-op>", entrys[15]);
		rh.addGrammarRule("<bin-op>", entrys[16]);
		rh.addGrammarRule("<bin-op>", entrys[17]);
		
		*/
		/*
		String[][] entrys = {
				{"<A>", "c", "c"},
				{"a", "b", "a"},
				{"a", "b"},
				{"c"}
				};
		rh.addGrammarRule("<A>", entrys[0]);
		rh.addGrammarRule("<A>", entrys[1]);
		rh.addGrammarRule("<A>", entrys[2]);
		rh.addGrammarRule("<A>", entrys[3]);
		*/
		
		String[][] entrys = {
				{"<if-stmt>"},
				{"OTHER"},
				{"IF","(","<exp>",")","<statement>","<else-part>"},
				{"ELSE","<statement>"},
				{"epsilon"},
				{"0"},
				{"1"},
				{"<statement>","$"},
				};
		rh.addGrammarRule("<statement>", entrys[0]);
		rh.addGrammarRule("<statement>", entrys[1]);
		rh.addGrammarRule("<if-stmt>", entrys[2]);
		rh.addGrammarRule("<else-part>", entrys[3]);
		rh.addGrammarRule("<else-part>", entrys[4]);
		rh.addGrammarRule("<exp>", entrys[5]);
		rh.addGrammarRule("<exp>", entrys[6]);
		rh.addGrammarRule("<start>", entrys[7]);
		
		rh.convertHashTablesToLists();
		rh.printHash();
		System.out.println("=====================");
		rh.removeSelfLeftRecursion();
		rh.printHash();
		System.out.println("=====================");
		rh.removeCommonPrefixes();
		rh.printHash();
		System.out.println("=====================");
		rh.constructFirst();
		rh.printFirst();
		System.out.println("=====================");
		rh.constructFollow();
		rh.printFollow();
		
	}

}
