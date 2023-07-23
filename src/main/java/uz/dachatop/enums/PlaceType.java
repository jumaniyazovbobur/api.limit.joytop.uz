package uz.dachatop.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum PlaceType {
    COTTAGE("Cottage", "Коттедж", "Dacha"),  // Dacha
    APARTMENT("Apartment", "Квартира", "Kvartira"),  // Kvartira
    HOTEL("Hotel", "Отель", "Mehmonhona"),  // Mehmonhona
    TRAVEL("Travel", "Путешествовать", "Sayohat"),  // Sayohat
    EXTREME("Extreme", "Экстремальный", "Extrim"),  // Extrim
    CAMPING("Camping", "Кемпинг", "Oromgoh"); // Oromgoh

    private String nameUz;
    private String nameEN;
    private String nameRU;

    PlaceType(String nameEN, String nameRU, String nameUz) {
        this.nameUz = nameUz;
        this.nameEN = nameEN;
        this.nameRU = nameRU;
    }

    public static PlaceType getLanguageEnum(String text) {
        return switch (text) {
            case "Cottage" -> COTTAGE;
            case "Коттедж" -> COTTAGE;
            case "Dacha" -> COTTAGE;
            case "Apartment" -> APARTMENT;
            case "Квартира" -> APARTMENT;
            case "Kvartira" -> APARTMENT;
            case "Hotel" -> HOTEL;
            case "Отель" -> HOTEL;
            case "Mehmonhona" -> HOTEL;
            case "Travel" -> TRAVEL;
            case "Путешествовать" -> TRAVEL;
            case "Sayohat" -> TRAVEL;
            case "Extreme" -> EXTREME;
            case "Экстремальный" -> EXTREME;
            case "Extrim" -> EXTREME;
            case "Camping" -> CAMPING;
            case "Кемпинг" -> CAMPING;
            case "Oromgoh" -> CAMPING;
            default -> null;
        };
    }

    public static List<String> getLanguage(AppLanguage language) {
        return switch (language) {
            case en -> en();
            case ru -> ru();
            case uz -> uz();
        };
    }

    public static List<String> en() {
        List<String> enLise = new ArrayList<>();
        enLise.add("Cottage");
        enLise.add("Apartment");
        enLise.add("Hotel");
        enLise.add("Travel");
        enLise.add("Extreme");
        enLise.add("Camping");
        return enLise;
    }

    public static List<String> ru() {
        List<String> enLise = new ArrayList<>();
        enLise.add("Коттедж");
        enLise.add("Квартира");
        enLise.add("Отель");
        enLise.add("Путешествовать");
        enLise.add("Экстремальный");
        enLise.add("Кемпинг");
        return enLise;
    }

    public static List<String> uz() {
        List<String> enLise = new ArrayList<>();
        enLise.add("Dacha");
        enLise.add("Kvartira");
        enLise.add("Mehmonhona");
        enLise.add("Sayohat");
        enLise.add("Extrim");
        enLise.add("Oromgoh");
        return enLise;
    }

}
