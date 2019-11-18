package com.lendico.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
	
	@JsonValue
	private List<Payment> paymentScheduler = new ArrayList<Payment>();
	
	public void addPayment(Payment payment)
	{
		this.paymentScheduler.add(payment);;
	}
	
	public Payment getPaymentByIndex(int index)
	{
		return this.paymentScheduler.get(index);
	}

}
