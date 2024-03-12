package neo.chat.unit.security.util;

import neo.chat.presentation.util.Response;
import neo.chat.settings.route.ApiRoute;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping({ApiRoute.PING_PERMIT_ALL, ApiRoute.PING_ANONYMOUS, ApiRoute.PING_AUTHENTICATED})
    public ResponseEntity<Response<Void>> pong() {
        return Response.voidResponseEntityOf(HttpStatus.OK);
    }

}
