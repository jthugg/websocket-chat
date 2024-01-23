package neo.chat.rest.domain.room.dto;

import neo.chat.rest.domain.room.dto.request.Search;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <h1>채팅방 검색 쿼리 파라미터 바인더</h1>
 *
 * 채팅방 검색 시 각 쿼리파라미터 유효성 검사 및 Search 객체로 바인딩
 *
 * @see Search
 */
public class RoomSearchHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String IS_PUBLIC_ONLY = "isPublicOnly";
    private static final String SCOPE = "scope";
    private static final String KEYWORDS = "keywords";
    private static final String SORT = "sort";
    private static final String DIRECTION = "direction";
    private static final String SIZE = "size";
    private static final String PAGE = "page";

    private static final int MAX_PARAMETER_COUNT = 1;
    private static final int PARAMETER_INDEX = 0;
    private static final Set<String> BOOLEAN_VALUES = Set.of("true", "false");
    private static final int MAX_KEYWORDS_LENGTH = 100;
    private static final String KEYWORD_SPLIT_REGEXP = " ";
    private static final Set<String> SORTABLE_PROPERTIES = Set.of("title", "capacity", "host", "attending", "createdAt");
    private static final String SORT_PROPERTY_NOT_MATCHED_MESSAGE = "정렬 가능한 옵션을 확인해주세요. 대/소문자를 구분합니다.";
    private static final String DEFAULT_SORT_PROPERTY = "createdAt";
    private static final int MAX_SIZE = 30;
    private static final int MIN_SIZE = 5;
    private static final int FIRST_PAGE = 0;

    private static final String NO_PARAMETER_MESSAGE = "필수값을 확인해주세요.";
    private static final String PARAMETER_NOT_UNIQUE = "유일한 값을 입력해주세요.";
    private static final String NO_BOOLEAN_VALUE_MESSAGE = "boolean 값을 입력해주세요.";
    private static final String KEYWORDS_LENGTH_MESSAGE = "키워드 길이는 100자 이하 입니다.";
    private static final String NUMBER_FORMAT_MESSAGE = "숫자만 입력해주세요.";
    private static final String SIZE_RANGE_MESSAGE = "한번에 불러올 수 있는 데이터는 5 ~ 30개 입니다.";
    private static final String PAGE_NUMBER_MESSAGE = "0 페이지 부터 시작 합니다. 음수를 입력할 수 없습니다.";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Search.class);
    }

    @Override
    public Search resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Map<String, String[]> requestParams = webRequest.getParameterMap();
        return new Search(
                getIsPublicOnly(requestParams.get(IS_PUBLIC_ONLY)),
                getScope(requestParams.get(SCOPE)),
                getKeywords(requestParams.get(KEYWORDS)),
                getSort(requestParams.get(SORT), requestParams.get(DIRECTION)),
                getSize(requestParams.get(SIZE)),
                getPage(requestParams.get(PAGE))
        );
    }

    private boolean getIsPublicOnly(String[] values) throws IllegalArgumentException {
        Assert.notNull(values, NO_PARAMETER_MESSAGE);
        Assert.isTrue(values.length == MAX_PARAMETER_COUNT, PARAMETER_NOT_UNIQUE);
        Assert.isTrue(BOOLEAN_VALUES.contains(values[PARAMETER_INDEX]), NO_BOOLEAN_VALUE_MESSAGE);
        return Boolean.parseBoolean(values[PARAMETER_INDEX]);
    }

    private Search.Scope getScope(String[] values) throws IllegalArgumentException {
        Assert.notNull(values, NO_PARAMETER_MESSAGE);
        Assert.isTrue(values.length == MAX_PARAMETER_COUNT, PARAMETER_NOT_UNIQUE);
        return Search.Scope.valueOf(values[PARAMETER_INDEX].toUpperCase());
    }

    private Set<String> getKeywords(String[] values) throws IllegalArgumentException {
        Assert.notNull(values, NO_PARAMETER_MESSAGE);
        Assert.isTrue(values.length == MAX_PARAMETER_COUNT, PARAMETER_NOT_UNIQUE);
        String value = values[PARAMETER_INDEX];
        Assert.isTrue(value.length() <= MAX_KEYWORDS_LENGTH, KEYWORDS_LENGTH_MESSAGE);
        if (value.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(value.trim().split(KEYWORD_SPLIT_REGEXP)).collect(Collectors.toSet());
    }

    private Sort getSort(String[] sorts, String[] directions) throws IllegalArgumentException {
        Assert.notNull(sorts, NO_PARAMETER_MESSAGE);
        Assert.notNull(directions, NO_PARAMETER_MESSAGE);
        Assert.isTrue(sorts.length == MAX_PARAMETER_COUNT, PARAMETER_NOT_UNIQUE);
        Assert.isTrue(directions.length == MAX_PARAMETER_COUNT, PARAMETER_NOT_UNIQUE);
        String sort = sorts[PARAMETER_INDEX];
        String direction = directions[PARAMETER_INDEX];
        Assert.isTrue(SORTABLE_PROPERTIES.contains(sort), SORT_PROPERTY_NOT_MATCHED_MESSAGE);
        if (sort.equals(DEFAULT_SORT_PROPERTY)) {
            return Sort.by(Sort.Direction.fromString(direction), sort);
        }
        return Sort.by(Sort.Direction.fromString(direction), sort, DEFAULT_SORT_PROPERTY);
    }

    private int getSize(String[] values) throws IllegalArgumentException {
        Assert.notNull(values, NO_PARAMETER_MESSAGE);
        Assert.isTrue(values.length == MAX_PARAMETER_COUNT, PARAMETER_NOT_UNIQUE);
        try {
            int value = Integer.parseInt(values[PARAMETER_INDEX]);
            if (value > MAX_SIZE || value < MIN_SIZE) {
                throw new IllegalArgumentException(SIZE_RANGE_MESSAGE);
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(NUMBER_FORMAT_MESSAGE, exception);
        }
    }

    private int getPage(String[] values) throws IllegalArgumentException {
        Assert.notNull(values, NO_PARAMETER_MESSAGE);
        Assert.isTrue(values.length == MAX_PARAMETER_COUNT, PARAMETER_NOT_UNIQUE);
        try {
            int value = Integer.parseInt(values[PARAMETER_INDEX]);
            if (value < FIRST_PAGE) {
                throw new IllegalArgumentException(PAGE_NUMBER_MESSAGE);
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(NUMBER_FORMAT_MESSAGE, exception);
        }
    }

}
