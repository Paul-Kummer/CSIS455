package intercode;

import intercode.lexer.*;
import intercode.parser.*;
import intercode.ast.*;

import java.lang.reflect.Array;
import java.util.*;

public class Env
{
    private Hashtable<Token, IdentifierNode> table;
    public Env prev;
    
    public Env () 
    {
        this.table = new Hashtable<Token, IdentifierNode>();
    }

    public Env (Env n) 
    {
        this.table = new Hashtable<Token, IdentifierNode>();
        this.prev = n;
    }

    public void put(Token w, IdentifierNode id) 
    {
        this.table.put(w, id);
    }

    public void putArray(ArrayAccessNode ar, IdentifierNode id)
    {
        Word wordSearch = ar.id.w;  //The word for the array variable
        Token w = ar.w;             //The word to assign to id, array index

        //search for the table with the word from the variable of the array
        IdentifierNode check;
        Env checkTable = this;
        
        while(checkTable != null)
        {
            System.out.println("Checking table");
            //checkTable.print(true);

            check = checkTable.table.get(wordSearch);
            if(check != null) //found the table with the array
            {
                checkTable.put(w, id); //use other put method
                System.out.println("Array Value Added");
            }
            checkTable = checkTable.prev;
        }
    }

    public IdentifierNode get(Token w)
    {
        if(w != null)
        {
            for (Env e = this; e != null; e = e.prev)
            {
                IdentifierNode found = (IdentifierNode)(e.table.get(w));
                if (found != null)
                {
                    return found;
                }     
            }
        }
        
        return null;
    }

    public Word getWord(String inputStr)
    {
        if(inputStr != null)
        {
            for (Env e = this; e != null; e = e.prev)
            {
                Set<Token> setOfTokens = e.table.keySet();

                for(Token key : setOfTokens) 
                {
                    if(key instanceof Word)
                    {
                        String testWord = ((Word)key).toString();
                        if(testWord.equals(inputStr))
                        {
                            return (Word)key;
                        }
                    }
                }  
            }
        }
        return null;
    }

    public void print(Boolean curTableOnly)
    {
        System.out.println("\n\t[ Symbol Table ]");

        Enumeration<Token> i = table.keys();
        IdentifierNode curId;
        Token key;

        while(i.hasMoreElements())
        {
            key = i.nextElement();
            curId = table.get(key);

            if(curId != null)
            {
                System.out.println( "Word: " + key + " | Value: " + curId.toString());
            }
            else
            {
                System.out.println( "Word: " + key + "   |   null entry");
            }   
        }

        if(!curTableOnly)
        {
            Env nextTable = this.prev;
            
            while(nextTable != null)
            {
                System.out.println("\t****** Next Table ******");
                i = nextTable.table.keys();
    
                while(i.hasMoreElements())
                {
                    key = i.nextElement();
                    curId = nextTable.table.get(key);
    
                    if(curId != null)
                    {
                        System.out.println( "Word: " + key + " | Value: " + curId.toString());
                    }
                    else
                    {
                        System.out.println( "Word: " + key + "   |   null entry");
                    }   
                }

                nextTable = nextTable.prev;
            }
        }
    }
}
