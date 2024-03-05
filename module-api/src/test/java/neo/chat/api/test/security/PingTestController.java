package neo.chat.api.test.security;

import neo.chat.api.presentation.util.Response;
import neo.chat.api.settings.route.ApiRoute;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingTestController {

    @GetMapping({ApiRoute.PING_PERMIT_ALL, ApiRoute.PING_ANONYMOUS, ApiRoute.PING_AUTHENTICATED})
    public ResponseEntity<Response<Void>> pong() {
        return Response.voidResponseEntityOf(HttpStatus.OK);
    }

}
