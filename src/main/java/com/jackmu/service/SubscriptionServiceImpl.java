package com.jackmu.service;

import com.jackmu.model.Subscription;
import com.jackmu.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription saveSubscription(Subscription subscription){
        return subscriptionRepository.save(subscription);
    }
    @Override
    public void deleteSubscription(String email, Long seriesId) {
        subscriptionRepository.deleteBySubscriberEmailAndSeriesId(email, seriesId);
    }

    @Override
    public List<Subscription> fetchSubscriptions(){
        return subscriptionRepository.findAll();
    }
}
