import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class SimpleCode {
  //variables
	static int charClass;
	static char[] lexeme = new char[100];
	static char nextChar;
	static int lexLen;
	static int tok;
	static int nextTok;
	static File in_fp;
	static int states = 0;

	//Char Types
	static final int LETTER = 0;
	static final int DIGIT = 1;
	static final int UNKNOWN = 99;
	static final int EOF = -1;

	//char values
	static final int STR_LIT = 1;
	static final int IDENT = 2;
	static final int INT_LIT = 3;
	static final int FLO_LIT = 4;
	static final int dot = 5;

	static final int QUOTES = 6;
	static final int DUB_EQ = 10;
	static final int NOT_EQ = 11;
	static final int EQ = 12;
	static final int LE = 13;
	static final int GR = 15;
	static final int LE_EQ = 16;
	static final int GR_EQ = 17;
	static final int ADD_OP = 18;
	static final int SUB_OP = 19;
	static final int MULT_OP = 20;
	static final int DIV_OP = 21;
	static final int MOD_OP = 22;
	static final int L_PARE = 25;
	static final int R_PARE = 26;
	static final int AND_OP =27;
	static final int OR_OP = 28;

	//special characters
	static int Flag = 0;
  
  public static void assign(BufferedReader br) throws IOException {
		//System.out.println("Enter <assign>\n");
		logic(br);
		while (nextTok == EQ) {
			lex(br);
			logic(br);
		}
		//System.out.println("Exit <assign>\n");
	}

	public static void logic(BufferedReader br) throws IOException {
		//for debugging purposes
		//System.out.println("Enter <logic>\n");
		equal(br);
		while (nextTok == AND_OP || nextTok == OR_OP) {
			lex(br);
			equal(br);
		}
		//System.out.println("Enter <logic>\n");
	}
	public static void equal(BufferedReader br) throws IOException {
		//System.out.println("Enter <equal>\n");
		rel(br);
		while (nextTok == DUB_EQ || nextTok == NOT_EQ) {
			lex(br);
			rel(br);
		}
		//System.out.println("Enter <equal>\n");
	}

	public static void rel(BufferedReader br) throws IOException {
		//System.out.println("Enter <rel>\n");
		add(br);
		while (nextTok == LE || nextTok == LE_EQ || nextTok == GR || nextTok == GR_EQ) {
			lex(br);
			add(br);
		}
		//System.out.println("Enter <rel>\n");
	}

	public static void add(BufferedReader br) throws IOException {
		//System.out.println("Enter <add>\n");
		mult(br);
		while (nextTok == ADD_OP || nextTok == SUB_OP) {
			lex(br);
			mult(br);
		}
		//System.out.println("Enter <add>\n");
	}
	public static void mult(BufferedReader br) throws IOException {
		//System.out.println("Enter <mult>\n");
		term(br);
		while (nextTok == MULT_OP || nextTok == DIV_OP || nextTok == MOD_OP) {
			lex(br);
			term(br);
		}
		//System.out.println("Enter <mult>\n");
	}

	public static void term(BufferedReader br) throws IOException {
		//System.out.println("Enter <term>\n");
		if (nextTok == IDENT || nextTok == INT_LIT) {
			lex(br);
		} else {
			if (nextTok == L_PARE) {
				lex(br);
				assign(br);
				if (nextTok == R_PARE) {
					lex(br);
				}
				else {
					error();
				}
			} else
				error();
		}
		//System.out.println("Exit <term>\n");
	}
	
	static void error() {
		System.out.println("Error = Symbol not found");
	}
