package uz.dachatop.entity.base;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseLongIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    private Boolean visible = true;

    @Column
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column
    public LocalDateTime updatedDate = LocalDateTime.now();

}
