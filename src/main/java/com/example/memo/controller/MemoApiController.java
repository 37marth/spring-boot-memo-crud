package com.example.memo.controller;

import com.example.memo.dto.MemoDto;
import com.example.memo.entity.Memo;
import com.example.memo.service.MemoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoApiController {
    @Autowired
    private MemoService memoService;


    @GetMapping("/memos")
    public ResponseEntity<List<MemoDto>> findAllMemos() {
        List<MemoDto> memoList = memoService.findAllMemos();
        return ResponseEntity.status(HttpStatus.OK).body(memoList);
    }

    @GetMapping("/memos/{id}")
    public ResponseEntity<MemoDto> findByIdMemo(@PathVariable Long id) {
        MemoDto memodto = memoService.findByIdMemo(id);
        return ResponseEntity.status(HttpStatus.OK).body(memodto);
    }

    @PostMapping("/memos")
    public ResponseEntity<MemoDto> createMemo(@RequestBody MemoDto memoDto) {
        MemoDto created = memoService.createMemo(memoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/memos/{id}")
    public ResponseEntity<MemoDto> updateMemo(@PathVariable Long id,  @RequestBody MemoDto memoDto) {
        MemoDto updated = memoService.updateMemo(id,memoDto);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/memos/{id}")
    public ResponseEntity<Memo> deleteMemo(@PathVariable Long id) {
        Memo deleted = memoService.deleteMemo(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
