package uz.dachatop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.AppLanguage;

import java.util.List;

@Slf4j
@Tag(name = "Apartment Type")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/apartment-type")
public class ApartmentTypeController {

    @Operation(summary = "Get apartment type ", description = "Method returns apartment type")
    @GetMapping("/public/")
    public ResponseEntity<List<String>> apartmentById(@PathVariable("id") String id,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(PlaceType.getLanguage(AppLanguage.getLanguage(lang)));
    }
}
