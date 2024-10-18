package com.jackmu.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleSendService {
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 5 * * *")
    public void scheduleSend() throws Exception {
        emailService.scheduleSendEmails();
        emailService.decrementReaderCount();
        emailService.deleteFinishedSeries();
    }
}
