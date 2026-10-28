package com.example.memo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemoRequest {
    @NotBlank
    private String title;

    private String content;
}
