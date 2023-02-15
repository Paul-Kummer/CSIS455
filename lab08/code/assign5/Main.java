/*Setting the CLASSPATH
export CLASSPATH=/home/remote/kummerpa/Documents/CSIS455_compilers/lab08/code/
*/

package assign5 ;

import java.io.FileNotFoundException;
import java.io.IOException;

import assign5.lexer.* ;
import assign5.parser.* ;
import assign5.unparser.* ;
    
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

        System.out.println("\n\t[ Unparsing ]");
        Unparser unparser = new Unparser(parser);
        System.out.println("\t--- Complete ---");

        System.out.println("\n\t[ Tree Printer ]");
        TreePrinter treeprinter = new TreePrinter(parser);
        System.out.println("\t--- Complete ---");
    }
}
