package com.lendico;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.lendico.controller.RepaymentPlanController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationPaymentPlanTest {
	
    @InjectMocks
    RepaymentPlanController repaymentPlanController;
    
	@Autowired
    private MockMvc mockMvc;
    
	@Test
	public void testRetrievePaymentPlan() throws Exception {

		String body = "{\"loanAmount\":5000.0,\"nominalRate\":5.0,\"duration\":2,\"startDate\":\"2020-01-01T00:00:01Z\"}";
		
		MvcResult result = this.mockMvc.perform(post("/generate-plan").content(body).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
 		

		String expected = "[\r\n" + 
				"    {\r\n" + 
				"        \"borrowerPaymentAmount\": \"2515.61\",\r\n" + 
				"        \"date\": \"2020-02-01T00:00:01Z\",\r\n" + 
				"        \"initialOutstandingPrincipal\": \"5000.0\",\r\n" + 
				"        \"interest\": \"20.84\",\r\n" + 
				"        \"principal\": \"2494.77\",\r\n" + 
				"        \"remainingOutstandingPrincipal\": \"2505.24\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"        \"borrowerPaymentAmount\": \"2515.68\",\r\n" + 
				"        \"date\": \"2020-03-01T00:00:01Z\",\r\n" + 
				"        \"initialOutstandingPrincipal\": \"2505.24\",\r\n" + 
				"        \"interest\": \"10.44\",\r\n" + 
				"        \"principal\": \"2505.24\",\r\n" + 
				"        \"remainingOutstandingPrincipal\": \"0.0\"\r\n" + 
				"    }\r\n" + 
				"]";
		
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	
	@Test
	public void testPaymentPlanWithDurationOfOneMonth() throws Exception {

		String body = "{\"loanAmount\":5000.0,\"nominalRate\":5.0,\"duration\":1,\"startDate\":\"2020-01-01T00:00:01Z\"}";
		
		MvcResult result = this.mockMvc.perform(post("/generate-plan").content(body).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andReturn();
 		
		String expected = "{\r\n" + 
				"    \"status\": 400,\r\n" + 
				"    \"message\": \"validation error\",\r\n" + 
				"    \"fieldErrors\": [\r\n" + 
				"        {\r\n" + 
				"            \"defaultMessage\": \"Duration can not be less than 2 months.\",\r\n" + 
				"            \"field\": \"duration\"\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
		
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testPaymentPlanWithNegativeAmount() throws Exception {

		String body = "{\"loanAmount\":-5000.0,\"nominalRate\":5.0,\"duration\":2,\"startDate\":\"2020-01-01T00:00:01Z\"}";
		
		MvcResult result = this.mockMvc.perform(post("/generate-plan").content(body).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andReturn();
 		
		String expected = "{\r\n" + 
				"    \"status\": 400,\r\n" + 
				"    \"message\": \"validation error\",\r\n" + 
				"    \"fieldErrors\": [\r\n" + 
				"        {\r\n" + 
				"            \"defaultMessage\": \"loanAmount can not be less than 1 euro.\",\r\n" + 
				"            \"field\": \"loanAmount\"\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
		
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testPaymentPlanWithStarDateInThePast() throws Exception {

		String body = "{\"loanAmount\":5000.0,\"nominalRate\":5.0,\"duration\":2,\"startDate\":\"2019-01-01T00:00:01Z\"}";
		
		MvcResult result = this.mockMvc.perform(post("/generate-plan").content(body).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andReturn();
 		
		String expected = "{\r\n" + 
				"    \"status\": 400,\r\n" + 
				"    \"message\": \"validation error\",\r\n" + 
				"    \"fieldErrors\": [\r\n" + 
				"        {\r\n" + 
				"            \"defaultMessage\": \"must be a future date\",\r\n" + 
				"            \"field\": \"startDate\"\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
		
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testPaymentPlanWithAllWrongValues() throws Exception {

		String body = "{\"loanAmount\":-200,\"nominalRate\":0,\"duration\":1,\"startDate\":\"2019-01-01T00:00:01Z\"}";
		
		MvcResult result = this.mockMvc.perform(post("/generate-plan").content(body).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andReturn();
 		
		String expected = "{\r\n" + 
				"    \"status\": 400,\r\n" + 
				"    \"message\": \"validation error\",\r\n" + 
				"    \"fieldErrors\": [\r\n" + 
				"        {\r\n" + 
				"            \"defaultMessage\": \"Duration can not be less than 2 months.\",\r\n" + 
				"            \"field\": \"duration\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"defaultMessage\": \"nominalRate can not be less than 1 %.\",\r\n" + 
				"            \"field\": \"nominalRate\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"defaultMessage\": \"loanAmount can not be less than 1 euro.\",\r\n" + 
				"            \"field\": \"loanAmount\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"defaultMessage\": \"must be a future date\",\r\n" + 
				"            \"field\": \"startDate\"\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
		
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testPaymentPlanWithWrongDateFormat() throws Exception {

		String body = "{\"loanAmount\":-200,\"nominalRate\":0,\"duration\":1,\"startDate\":\"2020-01-01\"}";
		
		this.mockMvc.perform(post("/generate-plan").content(body).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andReturn();		
	}

}

