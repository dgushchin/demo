package ru.diasoft.micro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.diasoft.micro.controller.dto.SmsVerificationCheckRequest;
import ru.diasoft.micro.controller.dto.SmsVerificationCheckResponse;
import ru.diasoft.micro.controller.dto.SmsVerificationPostRequest;
import ru.diasoft.micro.controller.dto.SmsVerificationPostResponse;
import ru.diasoft.micro.domain.SmsVerification;
import ru.diasoft.micro.repository.SmsVerificationRepository;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Primary
public class SmsVerificationPrimaryService implements SmsVerificationService{

    private final SmsVerificationRepository repository;

    @Override
    public SmsVerificationCheckResponse dsSmsVerificationCheck(SmsVerificationCheckRequest smsVerificationCheckRequest) {
        Optional<SmsVerification> result = repository.findBySecretCodeAndProcessGuidAndStatus(smsVerificationCheckRequest.getCode(),
                smsVerificationCheckRequest.getProcessGUID(), "WAITING");

        return new SmsVerificationCheckResponse(result.isPresent());
    }

    @Override
    public SmsVerificationPostResponse dsSmsVerificationCreate(SmsVerificationPostRequest smsVerificationPostRequest) {
        String guid = UUID.randomUUID().toString();
        String code = String.format("%04d", new Random().nextInt(10000));
        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(smsVerificationPostRequest.getPhoneNumber())
                .processGuid(guid)
                .secretCode(code)
                .status("WAITING")
                .build();

        repository.save(smsVerification);

        return new SmsVerificationPostResponse(guid);
    }
}
