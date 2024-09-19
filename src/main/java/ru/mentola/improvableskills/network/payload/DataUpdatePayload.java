package ru.mentola.improvableskills.network.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.ImprovableSkills;
import ru.mentola.improvableskills.data.PlayerData;

@AllArgsConstructor @Getter
public final class DataUpdatePayload implements CustomPayload {
    public static final CustomPayload.Id<DataUpdatePayload> PAYLOAD_ID = new Id<>(Identifier.of(ImprovableSkills.MOD_ID, "data_update"));
    public static final PacketCodec<PacketByteBuf, DataUpdatePayload> PAYLOAD_PACKET_CODEC = new Codec();

    // packet content
    private final PlayerData playerData;

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static class Codec implements PacketCodec<PacketByteBuf, DataUpdatePayload> {
        @Override
        public DataUpdatePayload decode(PacketByteBuf buf) {
            return new DataUpdatePayload(PlayerData.fromJson(buf.readString()));
        }

        @Override
        public void encode(PacketByteBuf buf, DataUpdatePayload value) {
            buf.writeString(value.getPlayerData().toJson());
        }
    }
}
