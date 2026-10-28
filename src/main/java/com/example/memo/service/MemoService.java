package com.example.memo.service;

import com.example.memo.dto.MemoDto;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
@Service
public class MemoService {
    @Autowired
    private MemoRepository memoRepository;


    public List<MemoDto> findAllMemos() {
        List<Memo> memoList = memoRepository.findAll();
        List<MemoDto> memoDtoList = new ArrayList<>();
        for (Memo memo : memoList) {
            memoDtoList.add(new MemoDto(memo.getId(),memo.getTitle(),memo.getContent()));
        }
        return  memoDtoList;
    }

    public MemoDto findByIdMemo(Long id) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"id가"+id+"인 메모가 존재하지 않습니다."));
        MemoDto memoDto = memo.toDto();
        return memoDto;
    }

    public MemoDto createMemo(MemoDto memoDto) {
        Memo memo = MemoDto.toEntity(memoDto);
        Memo saved = memoRepository.save(memo);
        return saved.toDto();
    }

    public MemoDto updateMemo(Long id, MemoDto memoDto) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"수정할 대상을 못찾음"));
        memo.patch(memoDto);
        Memo savedmemo=memoRepository.save(memo);
        return savedmemo.toDto();
    }

    public Memo deleteMemo(Long id) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"삭제할 대상이 없습니다."));
        memoRepository.delete(memo);
        return memo;
    }
}
