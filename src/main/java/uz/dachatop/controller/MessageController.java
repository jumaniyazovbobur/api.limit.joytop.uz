package uz.dachatop.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.message.MessagePaginationDTO;
import uz.dachatop.dto.message.MessageRequestDTO;
import uz.dachatop.dto.message.MessageResponseDTO;
import uz.dachatop.dto.profile.ProfileRequestDTO;
import uz.dachatop.dto.profile.ProfileResponseDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.service.MessageService;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 01
// TIME --> 22:07
@Slf4j
@Tag(name = "Message")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    private final MessageService messageService;

    @Operation(description = "Create Messages for All Users ")
    @PostMapping("/public")
    public ApiResponse<MessageResponseDTO> create(@RequestBody MessageRequestDTO dto) {
        log.info("Create Message");
        return messageService.create(dto);
    }

    @Operation(description = "Delete Message")
    @DeleteMapping("/public/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        log.info("Delete Message");
        return messageService.delete(id);
    }

    @Operation(description = "Pagination Messages")
    @GetMapping("/pagination")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessagePaginationDTO> pagination(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok().body(messageService.pagination(page, size));
    }

}
