package neo.chat.kafka.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionMetaData (
        String id,
        long totalOrder,
        long dataCollectionOrder
) {
    @JsonCreator
    public TransactionMetaData(
            @JsonProperty("id") String id,
            @JsonProperty("total_order") long totalOrder,
            @JsonProperty("data_collection_order") long dataCollectionOrder
    ) {
        this.id = id;
        this.totalOrder = totalOrder;
        this.dataCollectionOrder = dataCollectionOrder;
    }
}