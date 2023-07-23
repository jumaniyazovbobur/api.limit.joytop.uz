package uz.dachatop.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampMapperDTO extends PlaceMapperDTO {
    // main information
    private String name;
    private Long price; // обычная
    private Long priceOnSale; // сумма со скидкой
    private String createdDate; // сумма со скидкой
    @JsonProperty("tariff_result")
    private String tariffResult;
}
