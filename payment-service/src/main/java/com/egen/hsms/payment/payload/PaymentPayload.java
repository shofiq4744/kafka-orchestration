package com.egen.hsms.payment.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PaymentPayload {
	
	private long id;
	private String patientUid;
	private double amount;

}
