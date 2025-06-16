package Game;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ThreadMenager {
    private static final Set<Thread> threads = Collections.synchronizedSet(new HashSet<>());

    public static void register(Thread t) {
        threads.add(t);
    }

    public static void unregister(Thread t) {
        threads.remove(t);
    }

    public static void killAll() {
        synchronized (threads) {
            for (Thread t : threads) {
                if (t != null && t.isAlive()) {
                    t.interrupt();
                }
            }
            threads.clear();
        }
    }
}
