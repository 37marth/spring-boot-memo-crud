package com.example.memo.entity;

import com.example.memo.dto.MemoDto;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String content;




    public MemoDto toDto() {
        return new MemoDto(this.getId(),this.getTitle(), this.getContent());
    }

    public void patch(MemoDto memoDto) {

        if(memoDto.getTitle()!=null)//수정할 닉네임이 있다면
            this.title = memoDto.getTitle();
        if(memoDto.getContent()!=null)//수정할 닉네임이 있다면
            this.content = memoDto.getContent();

    }
}
