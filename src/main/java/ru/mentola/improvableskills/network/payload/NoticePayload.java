package ru.mentola.improvableskills.network.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.ImprovableSkills;

@AllArgsConstructor @Getter
public final class NoticePayload implements CustomPayload {
    public static final CustomPayload.Id<NoticePayload> PAYLOAD_ID = new Id<>(Identifier.of(ImprovableSkills.MOD_ID, "notice"));
    public static final PacketCodec<PacketByteBuf, NoticePayload> PAYLOAD_PACKET_CODEC = new Codec();

    // packet content
    private final String text;
    private final Identifier identifier;

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static class Codec implements PacketCodec<PacketByteBuf, NoticePayload> {
        @Override
        public NoticePayload decode(PacketByteBuf buf) {
            return new NoticePayload(buf.readString(), buf.readIdentifier());
        }

        @Override
        public void encode(PacketByteBuf buf, NoticePayload value) {
            buf.writeString(value.getText());
            buf.writeIdentifier(value.getIdentifier());
        }
    }
}