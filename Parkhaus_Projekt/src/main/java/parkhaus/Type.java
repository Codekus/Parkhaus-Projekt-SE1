package parkhaus;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;

public enum Type implements Serializable {

    NORMAL, FAMILY, SPECIAL_NEED;

    private static final List<Type> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final SecureRandom RANDOM = new SecureRandom();

    public static Type randomType()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}


