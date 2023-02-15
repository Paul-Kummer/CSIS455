/*Setting the CLASSPATH
export CLASSPATH=/home/remote/kummerpa/Documents/CSIS455_compilers/assign04
*/

package assign4 ;

import java.io.*;
import assign4.lexer.* ;
import assign4.parser.* ;
import assign4.pretty.* ;
    
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

        System.out.println("\n\t[ PRETTY PRINTING ]");
        PrettyPrinter pretty = new PrettyPrinter(parser);
        System.out.println("\t--- Complete ---");
    }
}
