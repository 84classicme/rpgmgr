package io.festerson.rpgvault.domain;

public enum CharacterType {

    PC  ("PlayerCharacter"),
    NPC  ("Non-player PlayerCharacter");

    private final String name;

    CharacterType(String name){
        this.name = name;
    }
}
