package assign4.parser ;

import assign4.visitor.* ;

public class LiteralNode extends Node {

    public String literal ;

    public LiteralNode () {

    }
    
    public LiteralNode (String literal) {

        this.literal = literal ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }

    void printNode () {

        System.out.println("LiteralNode: " + literal) ;
    }
}
