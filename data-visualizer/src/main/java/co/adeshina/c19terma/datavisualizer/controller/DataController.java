package co.adeshina.c19terma.datavisualizer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class DataController {

    // todo: To build a DataPacket, batch 

    // todo: DOn't use a DB: instead ensure the offset of a consumed TweetAggregate is not
    //       committed until the DataPacket the TA went into has been sent via SSE.
    //       to do this, gather commits into a COllection that is later sent to Kafka

    @GetMapping("data-stream")
    public SseEmitter eventSource() {
        SseEmitter sseEmitter = new SseEmitter();

        // todo: see => https://www.baeldung.com/spring-server-sent-events

        // todo: in another thread, loop and check if new a DataPacket is available

        // todo: use an SseEmitterBuilder

        return sseEmitter;
    }

}
