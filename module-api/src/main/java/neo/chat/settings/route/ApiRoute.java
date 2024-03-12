package neo.chat.settings.route;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class ApiRoute {

    // Path "/ping/{scope}" only available when testing
    public static final String PING_PERMIT_ALL = "/ping/public";
    public static final String PING_ANONYMOUS = "/ping/anonymous";
    public static final String PING_AUTHENTICATED = "/ping/authenticated";

    public static final String AUTH_CHECK_USERNAME = "/auth/username";
    public static final String AUTH_REGISTER = "/auth/register";
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_REISSUE = "/auth/reissue";
    public static final String AUTH_LOGOUT = "/auth/logout";
    public static final String AUTH_WITHDRAW = "/auth/withdraw";

    public static final AntPathRequestMatcher[] PERMIT_ALL = {
            new AntPathRequestMatcher(PING_PERMIT_ALL, HttpMethod.GET.name()),
            new AntPathRequestMatcher(AUTH_REISSUE, HttpMethod.POST.name()),
    };
    public static final AntPathRequestMatcher[] ANONYMOUS = {
            new AntPathRequestMatcher(PING_ANONYMOUS, HttpMethod.GET.name()),
            new AntPathRequestMatcher(AUTH_CHECK_USERNAME, HttpMethod.GET.name()),
            new AntPathRequestMatcher(AUTH_REGISTER, HttpMethod.POST.name()),
            new AntPathRequestMatcher(AUTH_LOGIN, HttpMethod.POST.name())
    };
    public static final AntPathRequestMatcher[] AUTHENTICATED = {
            new AntPathRequestMatcher(PING_AUTHENTICATED, HttpMethod.GET.name()),
            new AntPathRequestMatcher(AUTH_LOGOUT, HttpMethod.POST.name()),
            new AntPathRequestMatcher(AUTH_WITHDRAW, HttpMethod.DELETE.name())
    };

}
