package neo.chat.persistence.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.chat.persistence.query.document.QRoom;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoomAggregationQueryRepositoryImpl implements RoomAggregationQueryRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public boolean checkSubscribable(UUID roomId, UUID memberId) {
        List<QRoom> rooms = mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.addFields()
                        .addFieldWithValue("members", new Document("$objectToArray", "$participants"))
                        .build(),
                Aggregation.unwind("members"),
                Aggregation.match(new Criteria().andOperator(
                        Criteria.where("_id").is(roomId),
                        Criteria.where("members.v.member.$id").is(memberId)
                ))
        ), QRoom.DOCUMENT_NAME, QRoom.class).getMappedResults();
        return rooms.size() == 1;
    }

}
