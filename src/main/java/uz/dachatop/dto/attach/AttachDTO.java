package uz.dachatop.dto.attach;
//PROJECT NAME -> api.you.go
//TIME -> 19:12
//DAY -> 31
//MONTH -> 07

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {

    private String id;
    private String originalName;
    private long size;
    private String extension;
    private LocalDateTime createdDate;
    private String url;
    private String path;
}
