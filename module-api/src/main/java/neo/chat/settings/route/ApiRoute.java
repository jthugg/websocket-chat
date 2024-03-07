package neo.chat.settings.route;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class ApiRoute {

    // Path "/ping/{scope}" only available when testing
    public static final String PING_PERMIT_ALL = "/ping/public";
    public static final String PING_ANONYMOUS = "/ping/anonymous";
    public static final String PING_AUTHENTICATED = "/ping/authenticated";

    public static final AntPathRequestMatcher[] PERMIT_ALL = {
            new AntPathRequestMatcher(PING_PERMIT_ALL, HttpMethod.GET.name())
    };
    public static final AntPathRequestMatcher[] ANONYMOUS = {
            new AntPathRequestMatcher(PING_ANONYMOUS, HttpMethod.GET.name())
    };
    public static final AntPathRequestMatcher[] AUTHENTICATED = {
            new AntPathRequestMatcher(PING_AUTHENTICATED, HttpMethod.GET.name())
    };

}
