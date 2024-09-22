# Improvable Skills
With this mod you can learn unique skills and pump up their characteristics called *attributes*.
# Screenshots
[![image.png](https://i.postimg.cc/bwDmZR9S/image.png)](https://postimg.cc/21YQJhJC)
[![image.png](https://i.postimg.cc/1RBC2XzX/image.png)](https://postimg.cc/WtDwJ2gc)
# API
Using the API you can create your skills and attributes.
### Get API instance
```java
ImprovableSkillsAPI API = ImprovableSkillsProvider.getAPI();
```
### Registering new skill
```java
// use on your onInitializeClient
Skill skill = new Skill(Identifier.of("example_skill"), Identifier.of("example_skill_texture"), Text.of("ExampleSkill"), Text.of("ExampleSkillDescription"), 15000, 10, Set.of());
API.registerSkill(skill);
```
### Registering new attribute
```java
// use on your onInitializeClient
NumberAttribute<?> EXAMPLE_SKILL_ATTRIBUTE = AttributeFactory.createNumAttribute(Identifier.of("example_skill_attribute"), Text.of("ExampleSkillAttribute"), Text.of("ExampleSkillAttributeDescription"), 1000, 1, 10, 1);
API.registerAttribute(EXAMPLE_SKILL_ATTRIBUTE);
```
### Attach attribute to skill
```java
// use on your onInitializeClient
Skill skill = new Skill(Identifier.of("example_skill"), Identifier.of("example_skill_texture"), Text.of("ExampleSkill"), Text.of("ExampleSkillDescription"), 15000, 10, Set.of(
    EXAMPLE_SKILL_ATTRIBUTE
));
API.registerSkill(skill);
```
### Get player data
#### On client
```java
PlayerData playerData = API.getData(PlayerData.class);
```
#### On server
```java
ServerPlayerEntity serverPlayerEntity = ...;
PlayerData playerData = API.getPlayerData(serverPlayerEntity);
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
// sending notice to client (clientbound api method)
void sendClientNotice(Notice notice);
```
