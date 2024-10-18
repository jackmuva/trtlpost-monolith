package com.jackmu.service.email;

import com.jackmu.model.EntryEmailDTO;

import java.util.List;

public interface EmailService {
    void sendEmails(List<EntryEmailDTO> entryEmailDTOList);
    void scheduleSendEmails();
    void decrementReaderCount();
    void deleteFinishedSeries();
}
