package com.misterd.gosolar.network;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class GSNetwork {
    public static void register(IEventBus eventBus) {
        eventBus.addListener(GSNetwork::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("gosolar");

        registrar.playToServer(
                TransmitterTogglePacket.TYPE,
                TransmitterTogglePacket.STREAM_CODEC,
                TransmitterTogglePacket::handle
        );
    }
}