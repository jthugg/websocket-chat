package neo.chat.application.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityIdGenerator {

    MEMBER(IdGenerator.newInstance()),
    ;

    private final IdGenerator idGenerator;

}
