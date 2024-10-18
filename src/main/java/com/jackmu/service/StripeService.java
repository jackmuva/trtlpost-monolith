package com.jackmu.service;

import com.jackmu.model.Series;
import com.stripe.exception.StripeException;

public interface StripeService {
    Series sessionPaid(String sessionId) throws StripeException;
}
