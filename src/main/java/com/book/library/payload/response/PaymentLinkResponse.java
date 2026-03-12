package com.book.library.payload.response;

import com.book.library.entity.Payment;
import com.book.library.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkResponse {
    private String payment_link_url;
    private String payment_link_id;


    public static PaymentLinkResponse mock(User user, Payment payment) {
        PaymentLinkResponse response = new PaymentLinkResponse();
        response.setPayment_link_id("plink_dummy_" + payment.getId() + user.getFullName());
        response.setPayment_link_url("http://localhost:8080/fake-payment/" + payment.getId());
        return response;
    }
}