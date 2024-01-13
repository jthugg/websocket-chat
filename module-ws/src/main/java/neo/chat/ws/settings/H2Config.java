package neo.chat.ws.settings;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.sql.SQLException;

@Profile("test")
@Configuration
public class H2Config {

    private final Server server;

    public H2Config(@Value("${h2.console.port}") String port) throws SQLException {
        this.server = Server.createWebServer("-webPort", port, "-tcpAllowOthers");
    }

    @EventListener(ContextRefreshedEvent.class)
    public void start() throws SQLException {
        server.start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        server.stop();
    }

}
