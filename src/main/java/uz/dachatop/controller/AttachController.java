package uz.dachatop.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.dachatop.dto.attach.AttachDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.service.AttachServer;

import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "File")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/attach")
public class AttachController {

    private final AttachServer attachService;


    @Operation(summary = "Upload file ",description = "Upload file")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<List<AttachDTO>>> upload(@RequestParam() Map<String, MultipartFile> file) {
        log.info("/upload");
        return ResponseEntity.ok(attachService.upload(file));
    }

    @GetMapping(value = "/open/{id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> open(@PathVariable("id") String id) {
        log.info("/open/{id} {}", id);
        return ResponseEntity.ok(attachService.open(id));
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> download(@PathVariable("id") String id) {
        log.info("/download/{id} {}", id);
        return attachService.download(id);
    }


//    @ApiOperation(value = "List", notes = "Method used for get list of files from database")
//    @GetMapping("")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<ApiResponse<AttachPaginationDTO>> list(@RequestParam(value = "page", defaultValue = "0") int page,
//                                                                 @RequestParam(value = "size", defaultValue = "5") int size) {
//        log.info("LIST page={} size={}", page, size);
//        return ResponseEntity.ok(attachService.list(page, size));
//    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        log.info("DELETE {}", id);
        return ResponseEntity.ok(attachService.delete(id));
    }
}
