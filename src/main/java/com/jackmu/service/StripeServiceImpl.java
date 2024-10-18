package com.jackmu.service;

import com.jackmu.config.StripeConfig;
import com.jackmu.model.Series;
import com.jackmu.repository.SeriesRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImpl implements  StripeService{
    @Autowired
    StripeConfig stripeConfig;
    @Autowired
    SeriesRepository seriesRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(StripeServiceImpl.class);

    @Override
    public Series sessionPaid(String sessionId) throws StripeException {
        Stripe.apiKey = stripeConfig.getStripeApiKey();
        Session session = Session.retrieve(sessionId);

        Series series = seriesRepository.findBySeriesId(Long.valueOf(session.getClientReferenceId()));
        if(session.getStatus().equals("complete")){
            series.setMaxCurrentReaders(Integer.MAX_VALUE);
            seriesRepository.save(series);
        }
        return series;
    }
}
