package neo.chat.api.persistence.entity.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 각 엔티티별 식별자 생성기
 */
@Getter
@RequiredArgsConstructor
public enum EntityIDGenerator {

    MEMBER(IDGenerator.newInstance()),
    ;

    private final IDGenerator generator;

}
