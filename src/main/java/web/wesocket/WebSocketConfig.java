package web.wesocket;

import midi.LgSequencer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    SongStatusHandler songStatusHandler;



    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketTextHandler(), "/user").setAllowedOrigins("*");
        registry.addHandler(songStatusHandler, "/song_status").setAllowedOrigins("*");
        registry.addHandler(songStatusHandler, "/socket.io").setAllowedOrigins("*");
    }

}