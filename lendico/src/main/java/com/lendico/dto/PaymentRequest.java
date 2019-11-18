package com.lendico.dto;

import java.time.Instant;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lendico.util.CustomInstantDeserializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

	@NotNull(message = "loanAmount cannot be null")
	@Min(value = 1, message = "loanAmount can not be less than 1 euro.")
	private double loanAmount;
	
	@NotNull(message = "nominalRate cannot be null")
	@Min(value = 1, message = "nominalRate can not be less than 1 %.")
	private float nominalRate;
	
	@NotNull(message = "duration cannot be null")
	@Min(value = 2, message = "Duration can not be less than 2 months.")
	@PositiveOrZero
	private int duration;
	
	@JsonDeserialize(using = CustomInstantDeserializer.class)
	@Future
	private Instant startDate;
}
