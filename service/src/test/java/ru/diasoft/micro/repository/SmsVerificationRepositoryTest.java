package ru.diasoft.micro.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.diasoft.micro.domain.SmsVerification;

import java.util.UUID;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsVerificationRepositoryTest {

    @Autowired
    private SmsVerificationRepository repository;

    @Test
    public void smsVerificationCreateTest() {
        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(UUID.randomUUID().toString())
                .phoneNumber("12345678911")
                .secretCode("7777")
                .status("WAITING")
                .build();

        SmsVerification entity = repository.save(smsVerification);

        Assertions.assertThat(smsVerification).isEqualToComparingOnlyGivenFields(entity, "verificationId");
        Assertions.assertThat(entity.getVerificationId()).isNotNull();
    }

    @Test
    public void findBySecretCodeAndProcessGuidAndStatusTest() {
        String guid = UUID.randomUUID().toString();
        String status = "WAITING";
        String secretCode = "7777";

        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(guid)
                .phoneNumber("123456789")
                .secretCode(secretCode)
                .status(status)
                .build();

        SmsVerification createdEntity = this.repository.save(smsVerification);

        Assertions.assertThat(this.repository.findBySecretCodeAndProcessGuidAndStatus(secretCode, guid, status).orElse(SmsVerification.builder().build())).isEqualTo(createdEntity);
        Assertions.assertThat(this.repository.findBySecretCodeAndProcessGuidAndStatus(secretCode + "!", guid, status)).isEmpty();
    }
}