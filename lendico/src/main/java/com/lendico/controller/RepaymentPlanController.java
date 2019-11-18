package com.lendico.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lendico.dto.PaymentRequest;
import com.lendico.dto.PaymentResponse;
import com.lendico.mapper.PaymentMapper;
import com.lendico.service.PaymentPlanService;

@RestController
public class RepaymentPlanController {

	@Autowired
	private PaymentPlanService paymentService;
	
	@PostMapping("/generate-plan")
	public PaymentResponse createPaymentPlan(@Valid @RequestBody PaymentRequest loan)
	{
		return PaymentMapper.makePaymentDTO(paymentService.createPaymentPlan(loan));
	}
}
