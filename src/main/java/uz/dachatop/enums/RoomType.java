package uz.dachatop.enums;

import uz.dachatop.dto.KeyValueDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RoomType {
    SINGLE("Bir xonali", "Single room", "Одноместный номер"),
    DOUBLE("Ikki xonali", "Double room", "Двухместный номер"),
    TRIPLE("Uch xonali", "Triple room", "Трехместный номер"),
    TWIN("Egizak xonali", "Twin room", "Twin номер"),
    FAMILY("Oilaviy xonali", "Family room", "Cемейный номер"),
    KING("Shoxona xonali", "King room", "King номер"),
    PRESIDENTIAL("Shoxona xonali", "President room", "Президентская комната");

    private String nameUz;
    private String nameEn;
    private String nameRu;

    RoomType(String nameUz, String nameEn, String nameRu) {
        this.nameUz = nameUz;
        this.nameEn = nameEn;
        this.nameRu = nameRu;
    }

    public static List<KeyValueDTO> getByLang(AppLanguage language) {
        return Stream.of(values())
                .map(roomType -> new KeyValueDTO(roomType.name(), getValueByLang(roomType, language)))
                .collect(Collectors.toList());
    }

    public static String getValueByLang(RoomType type, AppLanguage language) {
        return switch (language) {
            case en -> type.getNameEn();
            case ru -> type.getNameRu();
            default -> type.getNameUz();
        };
    }

    public String getNameUz() {
        return nameUz;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameRu() {
        return nameRu;
    }
}
