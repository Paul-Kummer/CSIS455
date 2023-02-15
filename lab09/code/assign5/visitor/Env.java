package assign5.visitor;

import assign5.lexer.*;
import assign5.ast.*;
import java.util.*;

public class Env
{
    //For the parser, Hashtable<String, Node> should it be something else???
    private Hashtable table;
    protected Env prev;
    
    public Env () 
    {
        this.table = new Hashtable();
    }

    public Env (Env n) 
    {
        table = new Hashtable();
        prev = n;
    }

    public void put(IdentifierNode id, Node n) 
    {
        //n = n == null? new IdentifierNode(Word.Null): n;
        table.put(id.lexeme, n);
    }

    public void put(String lexeme, Node n) 
    {
        //n = n == null? new IdentifierNode(Word.Null): n;
        table.put(lexeme, n);
    }

    public Node get(IdentifierNode id)
    {
        if(id.lexeme != "")
        {
            for (Env e = this; e != null; e = e.prev)
            {
                Node found = (Node)(e.table.get(id.lexeme));
                if (found != null)
                {
                    return found;
                }     
            }
        }
        
        return null;
    }

    public Node get(String lexeme)
    {
        for (Env e = this; e != null; e = e.prev)
        {
            Node found = (Node)(e.table.get(lexeme));
            if (found != null)
            {
                return found;
            }        
        }
        
        return null;
    }
}
