package assign4.parser ;

import assign4.visitor.* ;

public class AdditionNode extends Node {

    public LiteralNode left  ;
    public LiteralNode right ;

    public AdditionNode () {

    }
    
    public AdditionNode (LiteralNode left, LiteralNode right) {

        this.left  = left  ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
