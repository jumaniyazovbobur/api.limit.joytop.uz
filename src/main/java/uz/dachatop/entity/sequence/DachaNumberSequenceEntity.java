package uz.dachatop.entity.sequence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import uz.dachatop.entity.base.BaseStringIdEntity;

/**
 * @author 'Mukhtarov Sarvarbek' on 28.03.2023
 * @project api.dachatop
 * @contact @sarvargo
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dacha_number_sequence")
public class DachaNumberSequenceEntity {

    @Id
    private String id;

    @Column(name = "seq_value", nullable = false)
    private Long seqValue;

}
