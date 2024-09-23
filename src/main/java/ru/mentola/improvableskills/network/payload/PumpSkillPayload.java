package ru.mentola.improvableskills.network.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.ImprovableSkills;

@AllArgsConstructor @Getter
public final class PumpSkillPayload implements CustomPayload {
    public static final CustomPayload.Id<PumpSkillPayload> PAYLOAD_ID = new Id<>(Identifier.of(ImprovableSkills.MOD_ID, "pump_skill"));
    public static final PacketCodec<PacketByteBuf, PumpSkillPayload> PAYLOAD_PACKET_CODEC = new Codec();

    // packet content
    private final Identifier idSkill;

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static class Codec implements PacketCodec<PacketByteBuf, PumpSkillPayload> {
        @Override
        public PumpSkillPayload decode(PacketByteBuf buf) {
            return new PumpSkillPayload(buf.readIdentifier());
        }

        @Override
        public void encode(PacketByteBuf buf, PumpSkillPayload value) {
            buf.writeIdentifier(value.getIdSkill());
        }
    }
}