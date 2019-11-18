package com.lendico.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lendico.dto.PaymentRequest;
import com.lendico.model.PaymentBean;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PaymentPlanServiceTest {
	
	@InjectMocks
	PaymentPlanService service;
	
	@Autowired
	static PaymentRequest loan;
	

	@Test
	public void testCreatePaymentPlan()
	{		
		List<PaymentBean> paymentPlan = createPaymentResponse();
		
		loan.setDuration(3);
		List<PaymentBean> payments = service.createPaymentPlan(loan);

		assertNotNull(payments);
		assertThat(payments.size()).isEqualTo(3);
		
		assertThat(payments.size()).isEqualTo(3);
		assertThat(payments.get(0).getBorrowerPaymentAmount()).isEqualTo(paymentPlan.get(0).getBorrowerPaymentAmount());
		assertThat(payments.get(0).getDate()).isEqualTo(paymentPlan.get(0).getDate());
		assertThat(payments.get(0).getInitialOutstandingPrincipal()).isEqualTo(paymentPlan.get(0).getInitialOutstandingPrincipal());
		assertThat(payments.get(0).getInterest()).isEqualTo(paymentPlan.get(0).getInterest());
		assertThat(payments.get(0).getPrincipal()).isEqualTo(paymentPlan.get(0).getPrincipal());
		assertThat(payments.get(0).getRemainingOutstandingPrincipal()).isEqualTo(paymentPlan.get(0).getRemainingOutstandingPrincipal());


		assertThat(payments.get(2).getRemainingOutstandingPrincipal()).isEqualTo(0.0);
	}
	
	@Test
	public void testCreatePaymentPlanWithSingleDuration()
	{
		loan.setDuration(1);
		
		List<PaymentBean> paymentPlan = new ArrayList<PaymentBean>();
		paymentPlan.add(PaymentBean
				.builder()
				.borrowerPaymentAmount(5020.84)
				.date(Instant.parse("2020-02-01T00:00:01Z"))
				.initialOutstandingPrincipal(5000.0)
				.interest(20.84)
				.principal(5000.0)
				.remainingOutstandingPrincipal(0.0)
				.build());
		
		List<PaymentBean> payments = service.createPaymentPlan(loan);
		
		assertNotNull(payments);
		assertThat(payments.size()).isEqualTo(1);
		
		assertThat(payments.size()).isEqualTo(1);
		assertThat(payments.get(0).getBorrowerPaymentAmount()).isEqualTo(paymentPlan.get(0).getBorrowerPaymentAmount());
		assertThat(payments.get(0).getDate()).isEqualTo(paymentPlan.get(0).getDate());
		assertThat(payments.get(0).getInitialOutstandingPrincipal()).isEqualTo(paymentPlan.get(0).getInitialOutstandingPrincipal());
		assertThat(payments.get(0).getInterest()).isEqualTo(paymentPlan.get(0).getInterest());
		assertThat(payments.get(0).getPrincipal()).isEqualTo(paymentPlan.get(0).getPrincipal());
		assertThat(payments.get(0).getRemainingOutstandingPrincipal()).isEqualTo(paymentPlan.get(0).getRemainingOutstandingPrincipal());
	}

	@BeforeAll
	public static void createPaymentRequest()
	{
		loan = PaymentRequest
		.builder()
		.loanAmount(5000.0)
		.duration(3)
		.nominalRate(5)
		.startDate(Instant.parse("2020-01-01T00:00:01Z"))
		.build();
		
	}
	
	
	public List<PaymentBean> createPaymentResponse()
	{
		List<PaymentBean> paymentPlan = new ArrayList<PaymentBean>();
		paymentPlan.add(PaymentBean
					.builder()
					.borrowerPaymentAmount(1680.56)
					.date(Instant.parse("2020-02-01T00:00:01Z"))
					.initialOutstandingPrincipal(5000.0)
					.interest(20.84)
					.principal(1659.72)
					.remainingOutstandingPrincipal(3340.29)
					.build());

		paymentPlan.add(PaymentBean
					.builder()
					.borrowerPaymentAmount(1680.56)
					.date(Instant.parse("2020-03-01T00:00:01Z"))
					.initialOutstandingPrincipal(3340.29)
					.interest(13.92)
					.principal(1666.64)
					.remainingOutstandingPrincipal(1673.66)
					.build());
			
		paymentPlan.add(PaymentBean
					.builder()
					.borrowerPaymentAmount(1680.64)
					.date(Instant.parse("2020-04-01T00:00:01Z"))
					.initialOutstandingPrincipal(1673.66)
					.interest(6.98)
					.principal(1673.66)
					.remainingOutstandingPrincipal(0.0)
					.build());
		
		return paymentPlan;
	
	}
	
}
