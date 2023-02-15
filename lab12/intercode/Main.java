/*Setting the CLASSPATH
export CLASSPATH=/home/remote/kummerpa/Documents/CSIS455_compilers/lab12/
*/

package intercode ;

import java.io.FileNotFoundException;
import java.io.IOException;

import intercode.lexer.* ;
import intercode.parser.* ;
import intercode.typechecker.*;
import intercode.inter.*;
import intercode.unparser.*;
    
public class Main 
{
    public static void main (String[] args) throws IOException, FileNotFoundException
    {
        System.out.println("\n\t[ LEXING ]");
        Lexer lexer = new Lexer() ;
        System.out.println("\t--- Complete ---");

        System.out.println("\n\t[ PARSING ]");
        Parser parser = new Parser(lexer);
        System.out.println("\t--- Complete ---");

        System.out.println("\n\t[ Tree Printer ]");
        TreePrinter treeprinter = new TreePrinter(parser);
        System.out.println("\t--- Complete ---");

        System.out.println("\n\t[ Type Checker ]");
        TypeChecker typechecker = new TypeChecker(parser);
        System.out.println("\t--- Complete ---");

        System.out.println("\n\t[ Intermediate Code Generation ]");
        InterCode intercode = new InterCode(typechecker);
        System.out.println("\t--- Complete ---");

        System.out.println("\n\t[ Unparsing ]");
        Unparser unparser = new Unparser(intercode);
        System.out.println("\n\t--- Complete ---");
    }
}
