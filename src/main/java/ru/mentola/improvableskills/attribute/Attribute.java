package ru.mentola.improvableskills.attribute;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.data.Data;
import ru.mentola.improvableskills.attribute.provider.AttributeProvider;

import java.lang.reflect.Type;

@AllArgsConstructor @Getter
public abstract class Attribute<T> implements Data {
    @SerializedName("id")
    @Expose private final Identifier identifier;
    @Expose private final T minValue;
    @Expose private final T maxValue;
    @Expose private final T stepLevel;
    @Expose private final int price;
    @Expose private final Text name;
    @SerializedName("type")
    private final AttributeType type;
    @Setter @SerializedName("level")
    private int level;

    @Override
    public NbtCompound asNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("id", identifier.toString());
        nbtCompound.putInt("level", level);
        return nbtCompound;
    }

    public abstract Attribute<T> copy(boolean saveLevel);

    @Override
    public String toString() {
        return String.format("Attribute{id=%s,level=%s}", this.identifier.toString(), this.level);
    }

    public static class Deserializer implements JsonDeserializer<Attribute<?>> {
        @Override
        public Attribute<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String type = json.getAsJsonObject().get("type").getAsString();
            int level = json.getAsJsonObject().get("level").getAsInt();
            Identifier id = Identifier.of(json.getAsJsonObject().get("id").getAsString());
            if (type.equals(AttributeType.NUMBER.name())) {
                NumberAttribute<?> attribute = (NumberAttribute<?>) AttributeProvider.getById(id);
                attribute.setLevel(level);
                return attribute;
            }
            return null;
        }
    }

    public static class Serializer implements JsonSerializer<Attribute<?>> {
        @Override
        public JsonElement serialize(Attribute<?> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject element = new JsonObject();
            element.addProperty("type", src.getType().name());
            element.addProperty("level", src.getLevel());
            element.addProperty("id", src.getIdentifier().toString());
            return element;
        }
    }
}