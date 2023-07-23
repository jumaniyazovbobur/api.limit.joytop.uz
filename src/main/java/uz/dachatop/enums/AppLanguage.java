package uz.dachatop.enums;


public enum AppLanguage {
    uz, en, ru;

    public static AppLanguage getLanguage(String lang) {
        return switch (lang) {
            case "ru" -> ru;
            case "en" -> en;
            default -> uz;
        };
    }
}
