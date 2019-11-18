package com.lendico.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.lendico.dto.PaymentRequest;
import com.lendico.dto.PaymentResponse;
import com.lendico.model.PaymentBean;
import com.lendico.service.PaymentPlanService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RepaymentPlanControllerTest {

    @InjectMocks
    RepaymentPlanController repaymentPlanController;

    @Mock
	private PaymentPlanService paymentService;
    

		
	private static PaymentRequest loan;
	private static List<PaymentBean> paymentPlan;
	
	@Autowired
    private MockMvc mockMvc;
    
	
	@Test
	public void testPaymentPan()
	{	
		when(paymentService.createPaymentPlan(loan)).thenReturn(paymentPlan);
		
        PaymentResponse result = repaymentPlanController.createPaymentPlan(loan);
        
        verify(paymentService, times(1)).createPaymentPlan(loan);

        assertNotNull(result);
        assertThat(result.getPaymentScheduler().size()).isEqualTo(loan.getDuration());
	}

	@Test
	public void retrievePaymentPlan() throws Exception {

		when(paymentService.createPaymentPlan(loan)).thenReturn(paymentPlan);

		String body = "{\"loanAmount\":5000.0,\"nominalRate\":5.0,\"duration\":3,\"startDate\":\"2020-01-01T00:00:01Z\"}";
		
		MvcResult result = this.mockMvc.perform(post("/generate-plan").content(body).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
 		

		String expected = "[\r\n" + 
				"    {\r\n" + 
				"        \"borrowerPaymentAmount\": \"1680.56\",\r\n" + 
				"        \"date\": \"2020-02-01T00:00:01Z\",\r\n" + 
				"        \"initialOutstandingPrincipal\": \"5000.0\",\r\n" + 
				"        \"interest\": \"20.84\",\r\n" + 
				"        \"principal\": \"1659.72\",\r\n" + 
				"        \"remainingOutstandingPrincipal\": \"3340.29\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"        \"borrowerPaymentAmount\": \"1680.56\",\r\n" + 
				"        \"date\": \"2020-03-01T00:00:01Z\",\r\n" + 
				"        \"initialOutstandingPrincipal\": \"3340.29\",\r\n" + 
				"        \"interest\": \"13.92\",\r\n" + 
				"        \"principal\": \"1666.64\",\r\n" + 
				"        \"remainingOutstandingPrincipal\": \"1673.66\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"        \"borrowerPaymentAmount\": \"1680.64\",\r\n" + 
				"        \"date\": \"2020-04-01T00:00:01Z\",\r\n" + 
				"        \"initialOutstandingPrincipal\": \"1673.66\",\r\n" + 
				"        \"interest\": \"6.98\",\r\n" + 
				"        \"principal\": \"1673.66\",\r\n" + 
				"        \"remainingOutstandingPrincipal\": \"0.0\"\r\n" + 
				"    }\r\n" + 
				"]";
		
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void retrievePaymentPlanWithEmptyBody() throws Exception {

		when(paymentService.createPaymentPlan(loan)).thenReturn(paymentPlan);
		
		this.mockMvc.perform(post("/generate-plan").content("").contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().is(400));
	}
	
	@Test
	public void retrievePaymentPlanWithWrongBody() throws Exception {

		when(paymentService.createPaymentPlan(loan)).thenReturn(paymentPlan);
		
		String body = "{\"loanAmount\":5000.0,\"nominalRate\":5.0,\"duration\":,\"startDate\":\"2020-01-01T00:00:01Z\"}";
		this.mockMvc.perform(post("/generate-plan").content(body).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().is(400));
	}
	
	@Test
	public void retrievePaymentPlanWithWrongMethod() throws Exception {

		when(paymentService.createPaymentPlan(loan)).thenReturn(paymentPlan);
		
		this.mockMvc.perform(get("/generate-plan"))
        .andExpect(status().is(405));
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
	
	@BeforeAll
	public static void createPaymentResponse()
	{
		paymentPlan = new ArrayList<PaymentBean>();
		paymentPlan.add(PaymentBean
					.builder()
					.borrowerPaymentAmount(1680.56)
					.date(Instant.now())
					.initialOutstandingPrincipal(5000.0)
					.interest(20.84)
					.principal(1659.72)
					.remainingOutstandingPrincipal(3340.29)
					.build());

		paymentPlan.add(PaymentBean
					.builder()
					.borrowerPaymentAmount(1680.56)
					.date(Instant.now())
					.initialOutstandingPrincipal(3340.29)
					.interest(13.92)
					.principal(1666.64)
					.remainingOutstandingPrincipal(1673.66)
					.build());
			
		paymentPlan.add(PaymentBean
					.builder()
					.borrowerPaymentAmount(1680.64)
					.date(Instant.now())
					.initialOutstandingPrincipal(1673.66)
					.interest(6.98)
					.principal(1673.66)
					.remainingOutstandingPrincipal(0.0)
					.build());
	
	}
}
