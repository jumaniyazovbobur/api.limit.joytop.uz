package uz.dachatop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.dachatop.dto.KeyValueDTO;
import uz.dachatop.dto.region.RegionDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.RoomType;

import java.util.List;

/*@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/room-type")
@Tag(name = "Room Type")
public class RoomTypeController {
    *//**
     * PUBLIC
     *//*
    @Operation(summary = "Get room type list ", description = "Method: returns all room type list")
    @GetMapping({"/public"})
    public ResponseEntity<ApiResponse<List<KeyValueDTO>>> findAll(@RequestHeader(value = "Accept-Language", defaultValue = "uz") String language) {
        return ResponseEntity.ok(ApiResponse.ok(RoomType.getByLang(AppLanguage.getLanguage(language))));
    }
}*/
