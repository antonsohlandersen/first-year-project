package bfst22.vector.exceptions;

import bfst22.vector.Data.TSTree.TSTNode;

public class TreeStructureException extends RuntimeException{
    String message;
    public TreeStructureException(char rootData, char childData, String lessOrMore){
        message = "TreeStructureException: childData " + childData + " should be " + lessOrMore + " than rootdata " + rootData;
    }

    public TreeStructureException(TSTNode r){
        message = "Node has no streets and no children, but has this data: " + r.dataAsString();
    }

    @Override
    public String getMessage(){
        return message;
    }
}