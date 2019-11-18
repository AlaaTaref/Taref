package com.lendico.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.lendico.dto.Payment;
import com.lendico.dto.PaymentResponse;
import com.lendico.model.PaymentBean;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentMapper {
	

	/**
	 * 
	 * @param paymentDTO
	 * @return
	 */
    public PaymentBean makePaymentBean(Payment paymentDTO)
    {
        return PaymentBean.builder()
        		.borrowerPaymentAmount(Double.valueOf(paymentDTO.getBorrowerPaymentAmount()))
        		.date(paymentDTO.getDate())
        		.initialOutstandingPrincipal((Double.valueOf(paymentDTO.getInitialOutstandingPrincipal())))
        		.interest(Double.valueOf(paymentDTO.getInterest()))
        		.principal(Double.valueOf(paymentDTO.getPrincipal()))
        		.remainingOutstandingPrincipal(Double.valueOf(paymentDTO.getRemainingOutstandingPrincipal()))
        		.build();
        		
    }
    
    /**
     * 
     * @param paymentBean
     * @return
     */
    public Payment makePayments(PaymentBean paymentBean)
    {
    	return Payment.builder()
    	.borrowerPaymentAmount(Double.toString(paymentBean.getBorrowerPaymentAmount()))
    	.date(paymentBean.getDate())
    	.initialOutstandingPrincipal(Double.toString(paymentBean.getInitialOutstandingPrincipal()))
    	.interest(Double.toString(paymentBean.getInterest()))
    	.principal(Double.toString(paymentBean.getPrincipal()))
    	.remainingOutstandingPrincipal(Double.toString(paymentBean.getRemainingOutstandingPrincipal()))
    	.build();        

    }
    
    /**
     * 
     * @param payments
     * @return
     */
    public List<Payment> makePaymentDTOList(Collection<PaymentBean> payments)
    {
        return payments.stream()
            .map(PaymentMapper::makePayments)
            .collect(Collectors.toList());
    }
    
    /**
     * 
     * @param payments
     * @return
     */
    public PaymentResponse makePaymentDTO(Collection<PaymentBean> payments)
    {
    	
    	PaymentResponse paymentResponse = new PaymentResponse();
    	paymentResponse.setPaymentScheduler(makePaymentDTOList(payments));
        return paymentResponse;
    }
}
