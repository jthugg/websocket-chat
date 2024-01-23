package neo.chat.rest.domain.room.dto.request;

import org.springframework.data.domain.Sort;

import java.util.Set;

/**
 * <h1>채팅방 검색 파라미터</h1>
 *
 * 웹 요청 시 {@link neo.chat.rest.domain.room.dto.RoomSearchHandlerMethodArgumentResolver}에서 입력받은 쿼리파라미터로 바인딩
 *
 * @see neo.chat.rest.domain.room.dto.RoomSearchHandlerMethodArgumentResolver
 * @param isPublicOnly 공개방만 보기 여부. 필수값
 * @param scope 검색 범위. 제목, 호스트 이름 필수값
 * @param keywords 검색 키워드. 문자열 입력을 받아 집합으로 매핑. 입력값은 문자열. 필수값
 * @param sort 정렬 기준이 될 속성. {제목, 호스트 이름, 최대인원, 현재 인원, 생성일} 각각 정, 역순 정렬 필수값
 * @param size 페이지당 채팅방 수. 필수값
 * @param page 페이지 번호. 필수값
 * @Todo 커서기반 페이징 마이그레이션
 */
public record Search(
        boolean isPublicOnly,
        Scope scope,
        Set<String> keywords,
        Sort sort,
        int size,
        int page
) {
    public enum Scope {
        TITLE,
        HOST
    }
}
