package com.albo.comics.marvel.domain;

public enum CreatorType {

    EDITOR("editor"), WRITER("writer"), COLORIST("colorist");

    private String text;

    CreatorType(String text) {
        this.text = text;
    }

    public static CreatorType fromString(String text) {
        for (CreatorType type : CreatorType.values()) {
            if (type.text.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
