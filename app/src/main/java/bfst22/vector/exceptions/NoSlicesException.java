package bfst22.vector.exceptions;

//Exception for when a node in the RTree contains no slices
public class NoSlicesException extends RuntimeException {
    public NoSlicesException(){
        super("This node contains no slices");
    }
}
