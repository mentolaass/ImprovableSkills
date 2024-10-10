# Improvable Skills
With this mod you can learn unique skills and pump up their characteristics called *attributes*.
# Screenshots
[![image.png](https://i.postimg.cc/CK7FQqdJ/image.png)](https://postimg.cc/nssbsM4Q)
[![image.png](https://i.postimg.cc/1RBC2XzX/image.png)](https://postimg.cc/WtDwJ2gc)
# Gradle
Fabric
```gradle
repositories {
    maven {
        url "https://cursemaven.com"
    }
}

modImplementation "curse.maven:improvableskills-1118828:${fileId}"
```
# API
Using the API you can create your skills and attributes.
### Registering new skill
```java
Skill skill = SkillFactory.createNew(Identifier.of("example_skill"), Identifier.of("example_skill_texture"), Text.of("ExampleSkill"), Text.of("ExampleSkillDescription"), 15000, 10, Set.of());
ImprovableSkillsAPI.registerSkill(skill);
```
### Registering new attribute
```java
NumberAttribute<?> EXAMPLE_SKILL_ATTRIBUTE = AttributeFactory.createNumAttribute(Identifier.of("example_skill_attribute"), Text.of("ExampleSkillAttribute"), Text.of("ExampleSkillAttributeDescription"), 1000, 1, 10, 1);
ImprovableSkillsAPI.registerAttribute(EXAMPLE_SKILL_ATTRIBUTE);
```
### Attach attribute to skill
```java
Skill skill = SkillFactory.createNew(Identifier.of("example_skill"), Identifier.of("example_skill_texture"), Text.of("ExampleSkill"), Text.of("ExampleSkillDescription"), 15000, 10, Set.of(
    EXAMPLE_SKILL_ATTRIBUTE
));
ImprovableSkillsAPI.registerSkill(skill);
```
### Get player data
#### On client
```java
PlayerData playerData = ImprovableSkillsAPI.getData(PlayerData.class);
```
#### On server
```java
ServerPlayerEntity serverPlayerEntity = ...;
PlayerData playerData = ImprovableSkillsAPI.getPlayerData(serverPlayerEntity);
```
### Get all registered skills and attributes
```java
SkillProvider.getSkills();
AttributeProvider.getAttributes();
```
### Other API methods
```java
// sending notice to player (serverbound api method)
void sendServerNotice(ServerPlayerEntity player, Notice notice);
// registering listener (serverbound api method)
void registerListener(Listener listener);
// sending notice to client (clientbound api method)
void sendClientNotice(Notice notice);
```
