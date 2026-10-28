package com.example.memo.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemoResponse {
    @NotBlank
    private String title;
    private String content;
}
