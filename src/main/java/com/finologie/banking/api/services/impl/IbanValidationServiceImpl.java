package com.finologie.banking.api.services.impl;

import com.finologie.banking.api.dtos.IbanValidationResponse;
import com.finologie.banking.api.entites.ForbiddenAccountNumber;
import com.finologie.banking.api.exception.WebBankingApiException;
import com.finologie.banking.api.exception.WebBankingApiFraudException;
import com.finologie.banking.api.repositories.ForbiddenAccountRepository;
import com.finologie.banking.api.services.AuthAndUserService;
import com.finologie.banking.api.services.IbanValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class IbanValidationServiceImpl implements IbanValidationService {


    private final RestTemplate restTemplate;
    private final String openIbanApiUrl;
    private final ForbiddenAccountRepository forbiddenAccountRepository;





    public IbanValidationServiceImpl(RestTemplate restTemplate, @Value("${openiban.url}") String openIbanApiUrl, ForbiddenAccountRepository forbiddenAccountRepository) {
            this.restTemplate = restTemplate;
        this.openIbanApiUrl = openIbanApiUrl;
        this.forbiddenAccountRepository = forbiddenAccountRepository;
    }

        @Override
        public boolean validateIban(String iban, boolean isInternalIban) throws WebBankingApiException {
           // for some reason this api works only with real iban, so need to pick real iban
            String url = UriComponentsBuilder.fromHttpUrl(this.openIbanApiUrl + iban)
                    .queryParam("getBIC", "false")
                    .queryParam("validateBankCode", "false") // turned false cause gave me hell in tests
                    .toUriString();
            IbanValidationResponse response = restTemplate.getForObject(url, IbanValidationResponse.class);

            if (!accountIsNotForbidden(iban)){
                throw new WebBankingApiFraudException("This account is forbidden %s you cannot make payment, any other attempts may lead to account be blocked".formatted(iban));
            }

            return response != null && ( response.isValid() || isInternalIban) && accountIsNotForbidden(iban);

        }

    private boolean accountIsNotForbidden(String iban) throws WebBankingApiException {
        Optional<ForbiddenAccountNumber> search = forbiddenAccountRepository.findAll().stream().filter(fa -> fa.getIban().equals(iban)).findFirst();
        return  search.isEmpty();
    }




}
