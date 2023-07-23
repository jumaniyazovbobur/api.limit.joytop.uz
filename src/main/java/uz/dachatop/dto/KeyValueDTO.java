package uz.dachatop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyValueDTO {
    private String key;
    private String value;

    public KeyValueDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
