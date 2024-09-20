package ru.mentola.improvableskills.network.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.ImprovableSkills;

@AllArgsConstructor
@Getter
public final class PumpLevelPayload implements CustomPayload {
    public static final CustomPayload.Id<PumpLevelPayload> PAYLOAD_ID = new Id<>(Identifier.of(ImprovableSkills.MOD_ID, "pump_level"));
    public static final PacketCodec<PacketByteBuf, PumpLevelPayload> PAYLOAD_PACKET_CODEC = new Codec();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static class Codec implements PacketCodec<PacketByteBuf, PumpLevelPayload> {
        @Override
        public PumpLevelPayload decode(PacketByteBuf buf) {
            return new PumpLevelPayload();
        }

        @Override
        public void encode(PacketByteBuf buf, PumpLevelPayload value) {

        }
    }
}
