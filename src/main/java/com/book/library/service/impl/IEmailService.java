package com.book.library.service.impl;

public interface IEmailService {
    void sendEmail(String to, String subject, String body);
}
