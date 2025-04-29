package com.farmdora.farmdora.category.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.GET_CATEGORIES_SUCCESS;

import com.farmdora.farmdora.category.dto.CategoryResponseDto;
import com.farmdora.farmdora.category.service.CategoryService;
import com.farmdora.farmdora.common.response.HttpResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<?> getCategories() {
        List<CategoryResponseDto> categories = categoryService.getCategories();
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_CATEGORIES_SUCCESS.getMessage(), categories));
    }
}
