package com.lendico.service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lendico.dto.PaymentRequest;
import com.lendico.model.PaymentBean;
import com.lendico.util.RoundDouble;

@Service
public class PaymentPlanService {

	private static final int DAYS = 30;
	private static final int YEARS = 360;

	/**
	 * Calculate payment plan scheduler for giving loan and duration by month. 
	 * @param loan
	 * @return payment plan
	 */
	public List<PaymentBean> createPaymentPlan(PaymentRequest loan) {
		List<PaymentBean> payments = new ArrayList<PaymentBean>();
		double remain = loan.getLoanAmount();
		double principal = 0.0;
		final double annuity = monthlyPayment(loan.getLoanAmount(), loan.getNominalRate(), loan.getDuration());

		for (int month = 0; month < loan.getDuration(); month++) {
			if (loan.getDuration() > 1 && month == loan.getDuration() - 1) {
				principal = payments.get(month - 1).getRemainingOutstandingPrincipal();
			}
			else if (loan.getDuration()== 1)
			{
				principal = loan.getLoanAmount();
			}
				
			payments.add(calculateMonthlyPayment(remain, loan.getNominalRate(), annuity, principal));
			payments.get(month).setDate(increaseDateByMonths(loan.getStartDate(), month + 1));
			remain = payments.get(month).getRemainingOutstandingPrincipal();
		}

		return payments;
	}

	private PaymentBean calculateMonthlyPayment(double remain, float rate, double annuity, double lastPrincipal) {
		double initialPrincipal = remain;
		double interest = calculateInterest(initialPrincipal, rate);
		double principal = lastPrincipal;
		
		if (principal == 0) {
			if (interest > initialPrincipal) {
				principal = annuity - initialPrincipal;
			} else {
				principal = annuity - interest;
			}
		}

		double borrowerPayment = principal + interest;
		double remainPrincipal = initialPrincipal - principal;

		return PaymentBean
				.builder()
				.initialOutstandingPrincipal(RoundDouble.round(initialPrincipal, 2))
				.interest(RoundDouble.round(interest, 2))
				.principal(RoundDouble.round(principal, 2))
				.borrowerPaymentAmount(RoundDouble.round(borrowerPayment, 2))
				.remainingOutstandingPrincipal(RoundDouble.round(remainPrincipal, 2))
				.build();
	}

	private double calculateInterest(double initialPrincipal, float norminalRate) {
		return ((DAYS * norminalRate * initialPrincipal) / YEARS) / 100;
	}

	private double monthlyPayment(double loanAmount, float norminalRate, int duration) {
		norminalRate = norminalRate * DAYS / YEARS / 100;
		return loanAmount / ((1 - Math.pow(1 + norminalRate, -duration)) / norminalRate);

	}

	private Instant increaseDateByMonths(Instant startDate, int month) {
		return startDate.atOffset(ZoneOffset.UTC).plus(month, ChronoUnit.MONTHS).toInstant();
	}
}
