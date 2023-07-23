package uz.dachatop.entity.place.travel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.CountryEntity;
import uz.dachatop.entity.place.PlaceEntity;

@Getter
@Setter
@Entity
@Table(name = "travel")
public class TravelEntity extends PlaceEntity { // tur
    // Address
    @Column(name = "country_id")
    private Long countryId;
    @ManyToOne
    @JoinColumn(name = "country_id", insertable = false, updatable = false)
    private CountryEntity country;
    // main information
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "company_address", columnDefinition = "TEXT")
    private String companyAddress;
    @Column(name = "standards_price")
    private Double standardsPrice;
    @Column(name = "price_on_sale")
    private Double priceOnSale;
    //street,house,apartmentNum - x

}
