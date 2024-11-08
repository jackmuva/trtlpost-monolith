package com.jackmu.service.email;

import com.jackmu.model.EntryEmailDTO;
import com.jackmu.model.FinishedSeriesCountsDTO;
import com.jackmu.model.Subscription;
import com.jackmu.repository.SeriesRepository;
import com.jackmu.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@Profile("!local-profile")
public class EmailServiceAws implements EmailService{
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private JavaMailSender mailSender;
    private static final Logger LOGGER = Logger.getLogger(EmailServiceAws.class.getName());


    public void sendEmails(List<EntryEmailDTO> entryEmailDTOList) {
        for (EntryEmailDTO entryEmail : entryEmailDTOList) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);
                helper.setBcc(getEmails(entryEmail));
                helper.setFrom("trtlpost@trtlpost.com", "Trtlpost");
                helper.setSubject(entryEmail.getSeriesTitle() + " : " + entryEmail.getEntryTitle());
                helper.setText(
                        appendHtmlStyling(
                            appendUnsubscribeHtml(
                                parseEmails(entryEmail.getEntryText()),
                                entryEmail.getSeriesId()
                            )
                        ),
                        true
                );

                mailSender.send(message);

                subscriptionRepository.updateSendDate(entryEmail.getArticleNum(), entryEmail.getSeriesId());
                subscriptionRepository.incrementArticleNum(entryEmail.getArticleNum(), entryEmail.getSeriesId());
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public String appendHtmlStyling(String emailHtml){
        String prefix = "<head><style type=\"text/css\">" +
                ".content{ " +
                    "padding: 1.5rem; " +
                    "flex-direction: column; " +
                    "width: 50%; " +
                    "border-radius: 1rem; " +
                    "border-color: #C9C9F7; " +
                    "border-style: inset; " +
                "} " +
                "@media only screen and (max-device-width: 479px) { " +
                    ".content{ "+
                        "width: 100%; " +
                        "padding: 1.5rem; " +
                        "flex-direction: column; " +
                        "border-radius: 1rem; " +
                        "border-color: #C9C9F7; " +
                        "border-style: inset; " +
                    "} " +
                " }" +
                "</style></head>" +
                "<div class = \"content\">";
        String suffix = "</div>";
        return prefix + emailHtml + suffix;
    }

    public String appendUnsubscribeHtml(String emailHtml, Long seriesId){
        String unsubMessage = "<br/><br/><p>If you'd like to unsubscribe from this series, please click on the link " +
                "below and input the <strong>Series Id: " + seriesId + "<strong/></p>" + "<p><a href = \"https://trtlpost.com/unsubscribe\"> " +
                "https://trtlpost.com/unsubscribe</a></p>";
        return emailHtml + unsubMessage;
    }

    public String[] getEmails(EntryEmailDTO entryEmail){
        List<String> emails = new ArrayList<>();
        List<Subscription> subscriptions = subscriptionRepository.findAllByArticleNumAndSeriesId(entryEmail.getArticleNum(), entryEmail.getSeriesId());
        for(Subscription subscription : subscriptions){
            emails.add(subscription.getSubscriberEmail());
        }
        return emails.toArray(new String[0]);
    }

    public String parseEmails(String html){
        String resHtml = html.substring(2, html.length() - 2);

        resHtml = resHtml.replaceAll("\",\"(?=[^>]*>)", "");
        resHtml = resHtml.replace("\\\"", "\"");

        return resHtml;
    }

    @Override
    public void scheduleSendEmails(){
        List<EntryEmailDTO> readyEmails = subscriptionRepository.findEmailsBySendDate();
        sendEmails(readyEmails);
    }

    @Override
    public void decrementReaderCount(){
        for(FinishedSeriesCountsDTO finishedSeriesCounts : subscriptionRepository.findFinishedCounts()){
            seriesRepository.decrementCurrentReaders(finishedSeriesCounts.getSeriesId(), finishedSeriesCounts.getNumFinishedSeries());
        }
    }

    @Override
    public void deleteFinishedSeries(){
        subscriptionRepository.deleteFinishedSubscriptions();
    }

}
