package neo.chat.api.persistence.entity.util;

import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.tsid.TsidFactory;

public class IDGenerator {

    public static final int DEFAULT_NODE_ID = 0;
    public static final int DEFAULT_NODE_BIT_SIZE = 16;

    // Instance for test
    private static final IDGenerator INSTANCE = new IDGenerator();

    private final TsidFactory factory;

    private IDGenerator() {
        int nodeId;
        try {
            nodeId = Integer.parseInt(System.getenv("TSID_NODE_ID"));
        } catch (NumberFormatException | NullPointerException | SecurityException exception) {
            nodeId = DEFAULT_NODE_ID;
        }
        this.factory = TsidFactory.builder()
                .withNodeBits(DEFAULT_NODE_BIT_SIZE)
                .withNode(nodeId)
                .build();
    }

    // Instance for test
    public static IDGenerator getInstance() {
        return INSTANCE;
    }

    protected static IDGenerator newInstance() {
        return new IDGenerator();
    }

    public Tsid generate() {
        return factory.create();
    }

}
