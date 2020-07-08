package co.adeshina.c19ta.datavisualizer.controller;

import co.adeshina.c19ta.datavisualizer.dto.DataPacket;
import co.adeshina.c19ta.datavisualizer.service.DataPacketService;
import co.adeshina.c19ta.datavisualizer.service.DataPacketServiceImpl;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class DataPacketController {

    private DataPacketService dataPacketService;
    private Logger logger = LoggerFactory.getLogger(DataPacketController.class);

    private static final String SSE_EVENT_NAME = "data-update";

    @Autowired
    public DataPacketController(DataPacketServiceImpl dataPacketService) {
        this.dataPacketService = dataPacketService;
    }

    @GetMapping("/covid19-data")
    public SseEmitter eventSource() {

        SseEmitter sseEmitter = new SseEmitter();

        ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
        sseExecutor.execute(() -> {

            while (true) {
                try {
                    Optional<DataPacket> dataPacketOptional = dataPacketService.getPacket();
                    if (dataPacketOptional.isPresent()) {
                        DataPacket packet = dataPacketOptional.get();
                        String packetId = packet.getBuildTime().toString();
                        SseEmitter.SseEventBuilder builder = SseEmitter.event();
                        builder.name(SSE_EVENT_NAME);
                        builder.id(packetId);
                        builder.data(packet);
                        sseEmitter.send(builder);
                        logger.info("Sent a new packet with id: " + packetId);
                    }
                    Thread.sleep(35000);
                } catch (IOException | InterruptedException e) {
                    sseEmitter.completeWithError(e);
                }
            }
        });
        return sseEmitter;
    }
}
