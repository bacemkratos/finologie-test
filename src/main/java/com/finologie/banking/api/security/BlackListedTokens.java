package com.finologie.banking.api.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.HashSet;
import java.util.Set;

@ApplicationScope
@Component
public class BlackListedTokens {
    Set<String> blackList = new HashSet<>();


    public void invalidateToken(String token) {
        blackList.add(token);
    }

    public boolean isBlackListed(String token) {
        return blackList.contains(token);
    }
}
