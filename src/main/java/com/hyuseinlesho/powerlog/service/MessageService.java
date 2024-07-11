package com.hyuseinlesho.powerlog.service;

import java.util.Locale;

public interface MessageService {
    String getMessage(String code, Object[] args, Locale locale);

    String getMessage(String code, Locale locale);
}
