import java.util.concurrent.locks.ReentrantLock;

public class memoryLock extends ReentrantLock{
    private static memoryLock instance;
    private memoryLock(){}

    static public memoryLock getInstance()
    {
        if (instance==null)
            instance=new memoryLock();
        return instance;
    }
}
