package com.example.memo.dto;

import com.example.memo.entity.Memo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemoDto {
    private Long id;
    private String title;
    private String content;

    public static Memo toEntity(MemoDto memoDto) {
        return new Memo(null,memoDto.getTitle(),memoDto.getContent());
    }
}
