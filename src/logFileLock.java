import java.util.concurrent.locks.ReentrantLock;

public class logFileLock extends ReentrantLock{
    private static logFileLock instance;
    private logFileLock(){}

    static public logFileLock getInstance()
    {
        if (instance==null)
            instance=new logFileLock();
        return instance;
    }

}
