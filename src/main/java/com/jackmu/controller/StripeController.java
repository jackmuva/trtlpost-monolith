package com.jackmu.controller;

import com.jackmu.model.Series;
import com.jackmu.service.StripeService;
import com.jackmu.util.GenericHttpResponse;
import com.stripe.exception.StripeException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
public class StripeController {
    @Autowired
    StripeService stripeService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/checkStatus")
    public GenericHttpResponse checkStripeSessionStatue(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestParam String checkout_session_id){
        try{
            Series series = stripeService.sessionPaid(checkout_session_id);
            if(series.getMaxCurrentReaders().equals(Integer.MAX_VALUE)){
                return new GenericHttpResponse(HttpStatus.SC_OK, "Purchase Successful!");
            } else{
                return new GenericHttpResponse(HttpStatus.SC_OK, "Purchase was not made");
            }
        } catch (StripeException e) {
            return new GenericHttpResponse(HttpStatus.SC_NOT_ACCEPTABLE, e.getMessage());
        }
    }

}
