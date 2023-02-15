package assign5.lexer ;

public class Tag 
{
    //null
    public final static int NULL    = 0;

    //boolean
    public final static int TRUE    = 300;
    public final static int FALSE   = 301;

    //terminals, variables, types
    public final static int ID      = 400;
    public final static int NUM     = 401;
    public final static int BASIC   = 402;
    public final static int REAL    = 403;
    public final static int INDEX   = 404;
    
    //loops
    public final static int DO      = 500;
    public final static int WHILE   = 501;
    public final static int FOR     = 502;
    public final static int BREAK   = 503;
    public final static int CONTINUE= 504;

    //conditional
    public final static int IF      = 600;
    public final static int ELSE    = 601;
    public final static int SWITCH  = 602;

    //comparision
    public final static int EQ      = 700; // ==
    public final static int NE      = 701; // !=
    public final static int GE      = 702; // >=
    public final static int LE      = 703; // <=
    public final static int LT      = 704; // <
    public final static int GT      = 705; // >
    public final static int AND     = 706; // &&
    public final static int OR      = 707; // ||

    //operational
    public final static int POSTINC = 800; // ++
    public final static int POSTDEC = 801; // --
    public final static int ADDEQ   = 802; // +=
    public final static int MINEQ   = 803; // -=
    public final static int DIVEQ   = 804; // /-
    public final static int MODEQ   = 805; // %=
    public final static int MULEQ   = 806; // *=
    public final static int RL      = 807; // <<
    public final static int RR      = 808; // >>
    public final static int LRR     = 809; // >>>
}