package uz.dachatop.enums;

public enum PlaceSubType {
    RENT("Rent", "Аренда", "Arenda"),  // arenda
    SALE("Sale", "Продажа", "Sotish"); // sotuv

    private String nameUz;
    private String nameEN;
    private String nameRU;

    PlaceSubType(String nameEN, String nameRU, String nameUz) {
        this.nameUz = nameUz;
        this.nameEN = nameEN;
        this.nameRU = nameRU;
    }
}
