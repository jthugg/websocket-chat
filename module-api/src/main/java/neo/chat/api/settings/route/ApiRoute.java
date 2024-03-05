package neo.chat.api.settings.route;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class ApiRoute {

    // path for "/ping/{scope}" only available when testing
    public static final String PING_PERMIT_ALL = "/ping/permit-all";
    public static final String PING_ANONYMOUS = "/ping/anonymous";
    public static final String PING_AUTHENTICATED = "/ping/authenticated";

    public static final RequestMatcher[] PERMIT_ALL = {
            new AntPathRequestMatcher(PING_PERMIT_ALL, HttpMethod.GET.name())
    };
    public static final RequestMatcher[] ANONYMOUS = {
            new AntPathRequestMatcher(PING_ANONYMOUS, HttpMethod.GET.name())
    };
    public static final RequestMatcher[] AUTHENTICATED = {
            new AntPathRequestMatcher(PING_AUTHENTICATED, HttpMethod.GET.name())
    };

}
