package ru.mentola.improvableskills.network;

import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.mentola.improvableskills.network.payload.side.Side;

@UtilityClass
public final class Network {
    public <T extends CustomPayload> void registerPayload(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec, Side side) {
        (side == Side.SERVER ? PayloadTypeRegistry.playS2C() : PayloadTypeRegistry.playC2S()).register(id, codec);
    }

    public <T extends CustomPayload, H extends ClientPlayNetworking.PlayPayloadHandler<T>> void registerClientReceiver(CustomPayload.Id<T> id, H handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, handler);
    }

    public <T extends CustomPayload, H extends ServerPlayNetworking.PlayPayloadHandler<T>> void registerServerReceiver(CustomPayload.Id<T> id, H handler) {
        ServerPlayNetworking.registerGlobalReceiver(id, handler);
    }

    public <T extends CustomPayload> void sendTo(ServerPlayerEntity receiver, T payload) {
        ServerPlayNetworking.send(receiver, payload);
    }

    public <T extends CustomPayload> void send(T payload) {
        ClientPlayNetworking.send(payload);
    }
}