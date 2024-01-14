package neo.chat.sync.dto;

import neo.chat.persistence.command.util.JpaEntity;

public enum OperationType {

    CREATE,
    UPDATE,
    DELETE,
    ;

    public static <T extends JpaEntity> OperationType getType(T before, T after, String op) {
        return switch (op) {
            case "c" -> CREATE;
            case "u" -> (!before.isRemoved()) && after.isRemoved() ? DELETE : UPDATE;
            case "d" -> DELETE;
            default -> throw new UnsupportedOperationException();
        };
    }

}
