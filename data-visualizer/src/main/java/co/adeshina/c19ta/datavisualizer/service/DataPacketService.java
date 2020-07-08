package co.adeshina.c19ta.datavisualizer.service;

import co.adeshina.c19ta.common.dto.TweetAggregate;
import co.adeshina.c19ta.datavisualizer.dto.DataPacket;
import java.util.List;
import java.util.Optional;

public interface DataPacketService {

    void buildPacket(List<TweetAggregate> tweets);

    // todo: Javadoc
    Optional<DataPacket> getPacket();
}
