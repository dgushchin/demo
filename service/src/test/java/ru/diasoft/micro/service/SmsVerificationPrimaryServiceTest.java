package ru.diasoft.micro.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.diasoft.micro.controller.dto.SmsVerificationCheckRequest;
import ru.diasoft.micro.controller.dto.SmsVerificationCheckResponse;
import ru.diasoft.micro.controller.dto.SmsVerificationPostRequest;
import ru.diasoft.micro.controller.dto.SmsVerificationPostResponse;
import ru.diasoft.micro.domain.SmsVerification;
import ru.diasoft.micro.repository.SmsVerificationRepository;

import java.util.Optional;
import java.util.UUID;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
class SmsVerificationPrimaryServiceTest {

    @Mock
    private SmsVerificationRepository repository;

    private SmsVerificationPrimaryService service;

    private SmsVerificationPostRequest req;
    private SmsVerificationCheckRequest checkRequestValid;
    private SmsVerificationCheckRequest checkRequestInvalid;

    private final String PHONE_NUMBER = "+12345678911";
    private final String VALID_SECRET_CODE = "7777";
    private final String INVALID_SECRET_CODE = "9999";
    private final String GUID = UUID.randomUUID().toString();
    private final String STATUS = "OK";

    @BeforeEach
    public void init() {
        this.service = new SmsVerificationPrimaryService(this.repository);

        this.req = new SmsVerificationPostRequest(PHONE_NUMBER);
        this.checkRequestValid = new SmsVerificationCheckRequest(GUID, VALID_SECRET_CODE);
        this.checkRequestInvalid = new SmsVerificationCheckRequest(GUID, INVALID_SECRET_CODE);

        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(this.GUID).phoneNumber(PHONE_NUMBER)
                .secretCode(VALID_SECRET_CODE)
                .status(STATUS)
                .build();

        Mockito.when(this.repository.findBySecretCodeAndProcessGuidAndStatus(VALID_SECRET_CODE, this.GUID, STATUS)).thenReturn(Optional.of(smsVerification));
    }

    @Test
    void dsSmsVerificationCheckValidCode() {
        Assertions.assertThat(this.service).isNotNull();
        Assertions.assertThat(this.checkRequestValid).isNotNull();
        SmsVerificationCheckResponse response = this.service.dsSmsVerificationCheck(this.checkRequestValid);
        Assertions.assertThat(response.getCheckResult()).isTrue();
    }

    @Test
    void dsSmsVerificationCheckInvalidCode() {
        Assertions.assertThat(this.service).isNotNull();
        Assertions.assertThat(this.checkRequestInvalid).isNotNull();
        SmsVerificationCheckResponse response = this.service.dsSmsVerificationCheck(this.checkRequestInvalid);
        Assertions.assertThat(response.getCheckResult()).isFalse();
    }

    @Test
    void dsSmsVerificationCreate() {
        Assertions.assertThat(this.service).isNotNull();
        Assertions.assertThat(this.req).isNotNull();
        SmsVerificationPostResponse response = this.service.dsSmsVerificationCreate(this.req);
        Assertions.assertThat(response.getProcessGUID()).isNotEmpty();
    }

}