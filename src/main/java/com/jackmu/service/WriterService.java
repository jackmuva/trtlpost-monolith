package com.jackmu.service;

import com.jackmu.model.Writer;

import java.util.List;

public interface WriterService {
    void deleteWriter(Long id);
    Writer fetchWriterByPenName(String penName);
    List<Writer> fetchWriterByWriterId(Long id);
    List<Writer> fetchWriterByEmail(String email);
    Boolean createWriter(Writer writer);
}
