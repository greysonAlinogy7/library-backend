package com.book.library.gateway;


import com.book.library.domain.PaymentType;
import com.book.library.entity.Payment;
import com.book.library.entity.User;
import com.book.library.payload.response.PaymentLinkResponse;
import com.book.library.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NmbPaymentService {
    private  final SubscriptionPlanService subscriptionPlanService;


    public PaymentLinkResponse createPaymentLink(User user, Payment payment) {
        System.out.println("Mock payment link generated for user: " + user.getFullName());
        return PaymentLinkResponse.mock(user, payment);
    }

    public org.json.JSONObject fetchPaymentDetails(String paymentId) throws Exception {
        org.json.JSONObject notes = new org.json.JSONObject();
        notes.put("payment_id", 1);  // dummy id
        notes.put("type", PaymentType.MEMBERSHIP);

        org.json.JSONObject paymentDetails = new org.json.JSONObject();
        paymentDetails.put("status", "captured"); // simulate a captured payment
        paymentDetails.put("amount", 1000 * 100); // amount in paisa
        paymentDetails.put("notes", notes);

        return paymentDetails;
    }


    public boolean isValidPayment(String paymentId) throws Exception {
        return true;
    }
}
