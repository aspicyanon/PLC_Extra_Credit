import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//lexical Analyzer portion
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


	static int lookup(char ch){
		switch (ch) {
		case '=':
			addChar();
			nextTok = EQ;
			break;
		case '.':
			addChar();
			nextTok = dot;
			break;
		case '"':
			addChar();
			nextTok = QUOTES;
			break;
		case '(':
			addChar();
			nextTok = L_PARE;
			break;
		case ')':
			addChar();
			nextTok = R_PARE;
			break;
		case '+':
			addChar();
			nextTok = ADD_OP;
			break;
		case '-':
			addChar();
			nextTok = SUB_OP;
			break;
		case '*':
			addChar();
			nextTok = MULT_OP;
			break;
		case '/':
			addChar();
			nextTok = DIV_OP;
			break;
		case '%':
			addChar();
			nextTok = MOD_OP;
			break;
		default:
			addChar();
			nextTok = 0;
			break;
		}
		return nextTok;
	}

	static void addChar(){
		if (lexLen <= 98) {
			lexeme[lexLen++] = nextChar;
			lexeme[lexLen] = 0;
		} else {
			System.out.println("Error - lexeme is too long \n");
		}
	}
	static void getChar(BufferedReader br) throws IOException{
		int nc;
		char f = 'f';
		char e = 'e';
		if ((nc = br.read()) != -1) { 
			nextChar = (char) nc;
			if (Character.isLetter(nextChar)) {
				if(nextChar == e) {
					Flag = 1;
				}
				if(nextChar == f) {
					Flag = 2;
				}
				charClass = LETTER;
			}
			else if (Character.isDigit(nextChar)) {
				charClass = DIGIT;
			}
			else {
				charClass = UNKNOWN;
			}
		} else {
			charClass = EOF;
		}
	}

	static void getNonBlank(BufferedReader br) throws IOException {
		while (Character.isWhitespace(nextChar)) {
			getChar(br);
		}
	}

	static int lex(BufferedReader br) throws IOException {
		lexeme = new char[lexeme.length];
		lexLen = 0;
		getNonBlank(br);
		switch (charClass) {
		/* Identifiers */
		case LETTER:
			addChar();
			getChar(br);
			while (charClass == LETTER || charClass == DIGIT) {
				addChar();
				getChar(br);
			}
			nextTok = IDENT;
			break;
			/* Integer literals and Float Literals*/
		case DIGIT:
			addChar();
			getChar(br);
			while (charClass == DIGIT) {
				addChar();
				getChar(br);
			}
			if(charClass == UNKNOWN) {
				lookup(nextChar);
				if(nextTok == dot) {
					getChar(br);
					while(charClass == DIGIT || (charClass == LETTER && Flag == 1)) {
						addChar();
						getChar(br);
					}
					if(charClass == LETTER && Flag == 2) {
						addChar();
						getChar(br);
					}
					nextTok = FLO_LIT;
					break;
				}		
			}
			nextTok = INT_LIT;
			break;
			/*quotes for Strings*/
		case UNKNOWN:
			lookup(nextChar);
			if(nextTok == QUOTES) {
				getChar(br);
				while (charClass == LETTER || charClass == DIGIT) {
					addChar();
					getChar(br);
				}
				addChar();
				getChar(br);
				nextTok = STR_LIT;	
			}
			if(nextTok == dot) {
				getChar(br);
				while(charClass == DIGIT || (charClass == LETTER && Flag == 1)) {
					addChar();
					getChar(br);
				}
				if(charClass == LETTER && Flag == 2) {
					addChar();
					getChar(br);
				}
				nextTok = FLO_LIT;
			}
			if(nextTok == EQ) {
				getChar(br);
				while(charClass == DIGIT || charClass == LETTER) {
					addChar();
					getChar(br);
				}
				nextTok = IDENT;
			} else {
				getChar(br);
			}
			break;
	    /* EOF */
		case EOF:
			nextTok = 0;
			lexeme[0] = 0;
			break;
		} /* End of switch */
		System.out.print("Lexeme is: ");
		for(int i=0; i<lexeme.length;i++) {
			System.out.print(lexeme[i]);
		}
		System.out.print("\n");
		System.out.print("\n");
		return nextTok;
	}
