package neo.chat.persistence.query;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.query.document.QMember;
import neo.chat.persistence.query.document.QRoom;
import neo.chat.rest.domain.room.dto.request.Search;
import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class RoomAggregationRepositoryImpl implements RoomAggregationRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Slice<QRoom> searchOnTitle(Search dto) {
        Criteria criteria = new Criteria();
        if (!dto.keywords().isEmpty()) {
            criteria.orOperator(dto.keywords().stream().map(keyword ->
                    Criteria.where("title").regex(Pattern.compile(keyword, Pattern.CASE_INSENSITIVE))).toList());
        }
        if (dto.isPublicOnly()) {
            criteria = new Criteria().andOperator(Criteria.where("password").exists(false), criteria);
        }
        List<QRoom> results = mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.addFields()
                        .addFieldWithValue("attending", new Document("$bsonSize", "$participants"))
                        .build(),
                Aggregation.sort(dto.sort()),
                Aggregation.skip((long) dto.page() * dto.size()),
                Aggregation.limit(dto.size() + 1)
        ), QRoom.DOCUMENT_NAME, QRoom.class).getMappedResults();
        return new SliceImpl<>(
                results.stream().limit(dto.size()).toList(),
                PageRequest.of(dto.page(), dto.size(), dto.sort()),
                results.size() > dto.size()
        );
    }

    @Override
    public Slice<QRoom> searchOnHost(Search dto) {
        Criteria criteria = new Criteria();
        if (!dto.keywords().isEmpty()) {
            criteria.orOperator(dto.keywords().stream().map(keyword ->
                    Criteria.where("host.username").regex(Pattern.compile(keyword))).toList());
        }
        if (dto.isPublicOnly()) {
            criteria = new Criteria().andOperator(Criteria.where("password").exists(false), criteria);
        }
        List<QRoom> results = mongoTemplate.aggregate(Aggregation.newAggregation(
                LookupOperation.newLookup()
                        .from(QMember.DOCUMENT_NAME)
                        .localField("host.$id")
                        .foreignField("_id")
                        .as("host"),
                Aggregation.unwind("$host"),
                Aggregation.match(criteria),
                Aggregation.addFields()
                        .addFieldWithValue("attending", new Document("$bsonSize", "$participants"))
                        .build(),
                Aggregation.sort(dto.sort()),
                Aggregation.skip((long) dto.page() * dto.size()),
                Aggregation.limit(dto.size() + 1)
        ), QRoom.DOCUMENT_NAME, QRoom.class).getMappedResults();
        return new SliceImpl<>(
                results.stream().limit(dto.size()).toList(),
                PageRequest.of(dto.page(), dto.size(), dto.sort()),
                results.size() > dto.size()
        );
    }

}
