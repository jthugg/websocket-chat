package neo.chat.rest.util;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class ApiRoute {

    public static final String USERNAME_CHECK = "/members/username"; // GET
    public static final String REGISTER = "/members"; // POST
    public static final String LOGIN = "/login"; // POST

    public static final AntPathRequestMatcher[] PERMIT_ALL = {
    };
    public static final AntPathRequestMatcher[] ANONYMOUS = {
            new AntPathRequestMatcher(USERNAME_CHECK, HttpMethod.GET.name()),
            new AntPathRequestMatcher(REGISTER, HttpMethod.POST.name()),
            new AntPathRequestMatcher(LOGIN, HttpMethod.POST.name()),
    };
    public static final AntPathRequestMatcher[] AUTHENTICATED = {
    };

}
