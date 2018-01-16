package exception;

public class MirrorMatchException extends Exception{
    public MirrorMatchException(){
        super("Same team defined for both sides");
    }
}
