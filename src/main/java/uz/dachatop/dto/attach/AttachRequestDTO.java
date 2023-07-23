package uz.dachatop.dto.attach;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachRequestDTO {

    private String originName;
    private String size;
    private String webContentLInk;
    private String extension;
    private String webViewLink;
}