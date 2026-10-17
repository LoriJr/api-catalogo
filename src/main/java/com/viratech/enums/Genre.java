package com.viratech.enums;

public enum Genre {
    ACTION, ADVENTURE, ANIMATION, COMEDY, DRAMA;

    public static Genre fromApi(String valor){
        return switch(valor){
            case "action" -> ACTION;
            case "adventure" -> ADVENTURE;
            case "animation" -> ANIMATION;
            case "comedy" -> COMEDY;
            case "drama" -> DRAMA;
            default -> throw new IllegalArgumentException("Genre invalid");
        };
    }

    public String toApi(){
        return switch(this){
            case ACTION -> "action";
            case ADVENTURE -> "adventure";
            case ANIMATION -> "animation";
            case COMEDY -> "comedy";
            case DRAMA -> "drama";
        };
    }
}
