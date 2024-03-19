package neo.chat.util;

import jakarta.persistence.criteria.Predicate;
import neo.chat.application.service.room.model.ChatRoomSortOption;
import neo.chat.application.service.room.model.SearchChatRoomRequest;
import neo.chat.persistence.entity.room.Room;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TestRoomSearchSpecification {

    private static final String REMOVED_AT = "removedAt";
    private static final String PASSWORD = "password";
    private static final String TITLE = "title";
    private static final String LIKE_FORMAT = "%%%s%%";

    public static Specification<Room> getSpecification(SearchChatRoomRequest request) {
        return switch (request.sortOption()) {
            case DEFAULT -> whenSortWithDefault(request);
            case DATE -> whenSortWithDate(request);
            case TITLE, CAPACITY, SATURATION, ATTENDING -> whenSortWith(request);
        };
    }

    private static Specification<Room> whenSortWithDefault(SearchChatRoomRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isNull(root.get(REMOVED_AT)));
            if (request.isPublicOnly()) {
                predicates.add(criteriaBuilder.isNull(root.get(PASSWORD)));
            }
            predicates.add(criteriaBuilder.or(request.keywords()
                    .stream()
                    .map(keyword -> criteriaBuilder.like(root.get(TITLE), String.format(LIKE_FORMAT, keyword)))
                    .toArray(Predicate[]::new)));
            if (request.hasCursor()) {
                predicates.add(criteriaBuilder.lessThan(
                        root.get(request.sortOption().getFieldName()),
                        request.cursor().id()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static Specification<Room> whenSortWithDate(SearchChatRoomRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isNull(root.get(REMOVED_AT)));

            if (request.isPublicOnly()) {
                predicates.add(criteriaBuilder.isNull(root.get(PASSWORD)));
            }

            predicates.add(criteriaBuilder.or(request.keywords()
                    .stream()
                    .map(keyword -> criteriaBuilder.like(root.get(TITLE), String.format(LIKE_FORMAT, keyword)))
                    .toArray(Predicate[]::new)));

            if (request.hasCursor()) {
                String sortFieldName = request.sortOption().getFieldName();
                String defaultSortFieldName = ChatRoomSortOption.DEFAULT.getFieldName();
                Instant cursorValue = Instant.parse(request.cursor().value());

                if (request.direction().isDescending()) {
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.and(
                                    criteriaBuilder.equal(root.get(sortFieldName), cursorValue),
                                    criteriaBuilder.lessThan(root.get(defaultSortFieldName), request.cursor().id())
                            ),
                            criteriaBuilder.lessThan(root.get(sortFieldName), cursorValue)
                    ));

                    return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
                }

                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get(sortFieldName), cursorValue),
                                criteriaBuilder.lessThan(root.get(defaultSortFieldName), request.cursor().id())
                        ),
                        criteriaBuilder.greaterThan(root.get(sortFieldName), cursorValue)
                ));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static Specification<Room> whenSortWith(SearchChatRoomRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isNull(root.get(REMOVED_AT)));

            if (request.isPublicOnly()) {
                predicates.add(criteriaBuilder.isNull(root.get(PASSWORD)));
            }

            predicates.add(criteriaBuilder.or(request.keywords()
                    .stream()
                    .map(keyword -> criteriaBuilder.like(root.get(TITLE), String.format(LIKE_FORMAT, keyword)))
                    .toArray(Predicate[]::new)));

            if (request.hasCursor()) {
                String sortFieldName = request.sortOption().getFieldName();
                String defaultSortFieldName = ChatRoomSortOption.DEFAULT.getFieldName();

                if (request.direction().isDescending()) {
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.and(
                                    criteriaBuilder.equal(root.get(sortFieldName), request.cursor().value()),
                                    criteriaBuilder.lessThan(root.get(defaultSortFieldName), request.cursor().id())
                            ),
                            criteriaBuilder.lessThan(root.get(sortFieldName), request.cursor().value())
                    ));

                    return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
                }

                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get(sortFieldName), request.cursor().value()),
                                criteriaBuilder.lessThan(root.get(defaultSortFieldName), request.cursor().id())
                        ),
                        criteriaBuilder.greaterThan(root.get(sortFieldName), request.cursor().value())
                ));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
