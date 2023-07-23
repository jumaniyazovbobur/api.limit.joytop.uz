package uz.dachatop.dto.attach;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachResponseDTO {

    private String id;
    private Boolean visible;
    private LocalDateTime createdDate;

    private String originName;
    private String size;
    private String webContentLInk;
    private String extension;
    private String webViewLink;

    public AttachResponseDTO(String id, String url) {

        this.id = id;
        this.webContentLInk = url;
    }
}
