package neo.chat.kafka.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import neo.chat.persistence.command.util.JpaEntity;

import java.util.Objects;

public record EventPayload<T extends JpaEntity>(
        T before,
        T after,
        String target,
        OperationType operationType,
        TransactionMetaData transactionMetaData
) {
    @JsonCreator
    public EventPayload(
            @JsonProperty("before") T before,
            @JsonProperty("after") T after,
            @JsonProperty("op") String op,
            @JsonProperty("transaction") TransactionMetaData transaction
    ) throws NoSuchFieldException, IllegalAccessException {
        this(
                before,
                after,
                Objects.requireNonNullElse(before, after).getClass().getField("TABLE_NAME").get(null).toString(),
                OperationType.getType(before, after, op),
                transaction
        );
    }
}
