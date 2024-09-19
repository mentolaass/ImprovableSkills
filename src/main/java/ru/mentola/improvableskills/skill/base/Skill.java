package ru.mentola.improvableskills.skill.base;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.data.Data;
import ru.mentola.improvableskills.skill.attribute.Attribute;
import ru.mentola.improvableskills.skill.provider.SkillProvider;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor @Getter
public class Skill implements Data {
    @SerializedName("id")
    private final Identifier id;
    @Expose private final Text name;
    @Expose private final Text description;
    @Expose private final int pricePoints;
    @Expose private final int needLevel;
    @Setter @SerializedName("attributes")
    private Set<Attribute<?>> attributes;

    @Override
    public NbtCompound asNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("id", this.id.toString());
        NbtList attributesList = new NbtList();
        attributes.forEach((attribute) -> {
            if (attribute != null) {
                NbtCompound attributeCompound = new NbtCompound();
                attributeCompound.putString("id", attribute.getIdentifier().toString());
                attributeCompound.putInt("level", attribute.getLevel());
                attributesList.add(attributeCompound);
            }
        });
        nbtCompound.put("attributes", attributesList);
        return nbtCompound;
    }

    @Nullable
    public Attribute<?> getAttribute(Identifier id) {
        return this.attributes.stream()
                .filter((attribute) -> attribute.getIdentifier().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean containsAttribute(Identifier id) {
        return this.getAttribute(id) != null;
    }

    public boolean upgradeAttribute(Identifier id) {
        if (!this.containsAttribute(id)) return false;
        Attribute<?> attribute = this.getAttribute(id);
        attribute.setLevel(attribute.getLevel() + 1);
        return true;
    }

    @Override
    public String toString() {
        return String.format("Skill{id=%s,attributes=%s}", this.id.toString(),
                String.join(",", this.attributes.stream()
                        .map(Object::toString)
                        .collect(Collectors.toSet())));
    }

    public static class Deserializer implements JsonDeserializer<Skill> {
        @Override
        public Skill deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Skill skill = SkillProvider.getById(Identifier.of(json.getAsJsonObject().get("id").getAsString()));
            if (skill == null) return null;
            Set<Attribute<?>> attributes = new HashSet<>();
            for (JsonElement element : json.getAsJsonObject().get("attributes").getAsJsonArray())
                attributes.add(new Attribute.Deserializer().deserialize(element, null, null));
            skill.setAttributes(attributes);
            return skill;
        }
    }

    public static class Serializer implements JsonSerializer<Skill> {
        @Override
        public JsonElement serialize(Skill src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject element = new JsonObject();
            JsonArray array = new JsonArray();
            element.addProperty("id", src.getId().toString());
            for (Attribute<?> attribute : src.getAttributes())
                array.add(new Attribute.Serializer().serialize(attribute, null, null));
            element.add("attributes", array);
            return element;
        }
    }
}