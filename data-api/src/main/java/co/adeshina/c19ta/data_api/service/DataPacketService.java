package co.adeshina.c19ta.data_api.service;

import co.adeshina.c19ta.data_api.dto.DataPacket;
import java.util.Optional;

public interface DataPacketService {

    Optional<DataPacket> buildPacket();
}
