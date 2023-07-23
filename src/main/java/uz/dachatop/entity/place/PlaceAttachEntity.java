package uz.dachatop.entity.place;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import uz.dachatop.entity.AttachEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "place_attach")
@Getter
@Setter
public class PlaceAttachEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "place_id")
    private String placeId;

    @Column(name = "attach_id")
    private String attachId;
    @ManyToOne
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
