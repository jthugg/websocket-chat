package neo.chat.rest.util;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class ApiRoute {

    public static final String USERNAME_CHECK = "/members/username"; // GET
    public static final String REGISTER = "/members"; // POST
    public static final String LOGIN = "/login"; // POST
    public static final String REISSUE = "/reissue"; // POST
    public static final String LOGOUT = "/logout"; // POST

    public static final String ROOM_CREATE = "/rooms"; // POST
    public static final String ROOM_ENTER = "/rooms/{targetRoomId}/participants"; // POST
    public static final String ROOM_LEAVE = "/rooms/{targetRoomId}/participants"; // DELETE
    public static final String ROOM_UPDATE = "/rooms/{targetRoomId}"; // PUT

    public static final AntPathRequestMatcher[] PERMIT_ALL = {
            new AntPathRequestMatcher(REISSUE, HttpMethod.POST.name()),
            new AntPathRequestMatcher(LOGOUT, HttpMethod.POST.name()),
    };
    public static final AntPathRequestMatcher[] ANONYMOUS = {
            new AntPathRequestMatcher(USERNAME_CHECK, HttpMethod.GET.name()),
            new AntPathRequestMatcher(REGISTER, HttpMethod.POST.name()),
            new AntPathRequestMatcher(LOGIN, HttpMethod.POST.name()),
    };
    public static final AntPathRequestMatcher[] AUTHENTICATED = {
            new AntPathRequestMatcher(ROOM_CREATE, HttpMethod.POST.name()),
            new AntPathRequestMatcher(ROOM_ENTER, HttpMethod.POST.name()),
            new AntPathRequestMatcher(ROOM_LEAVE, HttpMethod.DELETE.name()),
            new AntPathRequestMatcher(ROOM_UPDATE, HttpMethod.PUT.name()),
    };

}
