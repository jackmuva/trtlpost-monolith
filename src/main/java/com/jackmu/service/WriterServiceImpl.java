package com.jackmu.service;

import com.jackmu.model.Writer;
import com.jackmu.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WriterServiceImpl implements WriterService{
    @Autowired
    private WriterRepository writerRepository;

    public Boolean createWriter(Writer writer){
        if(writerRepository.findByPenName(writer.getPenName()) != null){
            writerRepository.save(writer);
            return Boolean.TRUE;
        } else{
            return Boolean.FALSE;
        }
    }

    public void deleteWriter(Long id){
        writerRepository.deleteById(id);
    }

    public Writer fetchWriterByPenName(String penName){
        return writerRepository.findByPenName(penName);
    }
    public List<Writer> fetchWriterByWriterId(Long id) {
        return writerRepository.findAllByWriterId(id);
    }
    public List<Writer> fetchWriterByEmail(String email) {
        return writerRepository.findAllByEmail(email);
    }
}
