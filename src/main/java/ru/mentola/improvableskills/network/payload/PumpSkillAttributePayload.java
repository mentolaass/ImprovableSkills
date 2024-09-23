package ru.mentola.improvableskills.network.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.ImprovableSkills;

@AllArgsConstructor @Getter
public final class PumpSkillAttributePayload implements CustomPayload {
    public static final CustomPayload.Id<PumpSkillAttributePayload> PAYLOAD_ID = new Id<>(Identifier.of(ImprovableSkills.MOD_ID, "pump_attribute"));
    public static final PacketCodec<PacketByteBuf, PumpSkillAttributePayload> PAYLOAD_PACKET_CODEC = new Codec();

    // packet content
    private final Identifier idSkill;
    private final Identifier idAttribute;
    private final int count;

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static class Codec implements PacketCodec<PacketByteBuf, PumpSkillAttributePayload> {
        @Override
        public PumpSkillAttributePayload decode(PacketByteBuf buf) {
            return new PumpSkillAttributePayload(buf.readIdentifier(), buf.readIdentifier(), buf.readInt());
        }

        @Override
        public void encode(PacketByteBuf buf, PumpSkillAttributePayload value) {
            buf.writeIdentifier(value.getIdSkill());
            buf.writeIdentifier(value.getIdAttribute());
            buf.writeInt(value.getCount());
        }
    }
}