package uz.dachatop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.base.BaseStringIdEntity;

@Getter
@Setter
@Entity
@Table(name = "attach")
public class AttachEntity extends BaseStringIdEntity {


    @Column(name = "original_name")
    private String originalName;


    @Column(name = "size")
    private Long size;


    @Column(name = "extension")
    private String extension;


    @Column(name = "path")
    private String path;
}
