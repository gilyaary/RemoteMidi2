package web.wesocket;

import midi.LgSequencer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;

@Component
public class SongStatusHandler implements WebSocketHandler{

    @Autowired
    LgSequencer lgSequencer;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        int seq = 0;
        while(session.isOpen()){
            try {
                if(lgSequencer != null) {
                    long position = lgSequencer.getTickPosition();
                    String msg = Long.toString(position);
                    WebSocketMessage<?> msm = new TextMessage(msg);
                    session.sendMessage(msm);

                    //it looks like javascript cannot keep up with higher speeds than once/second
                    //is this a Java issue? or Browser/Javascript/Vue/Canvas issue?
                    //One thing to consider is that event notification is serial
                    //so if the canvas takes a long time to refresh then the position too will be updated late
                    //Also: consider putting messages in a queue to ensure the following:
                    //    1. In order notification
                    //    2. Removal of old messages when queue is full
                    Thread.sleep(1000);
                }
            }catch(Exception e){
                session.close();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            handleTextMessage(session, (TextMessage) message);
        }
        else if (message instanceof BinaryMessage) {
            handleBinaryMessage(session, (BinaryMessage) message);
        }
        else if (message instanceof PongMessage) {
            handlePongMessage(session, (PongMessage) message);
        }
        else {
            throw new IllegalStateException("Unexpected WebSocket message type: " + message);
        }
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        String payload = message.getPayload();
        JSONObject jsonObject = new JSONObject(payload);
        session.sendMessage(new TextMessage("Hi " + jsonObject.get("user") + " how may we help you?"));
    }

    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
    }

    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


}