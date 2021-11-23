package com.egen.hsms.payment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.egen.hsms.payment.payload.PaymentPayload;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class PaymentController {
	
    private final KafkaTemplate<String, Object> template;
    private final String TIPIC;

    public PaymentController(
            final KafkaTemplate<String, Object> template,
            @Value("${somch.topic-name}") final String TIPIC) {
        this.template = template;
        this.TIPIC = TIPIC;
    }
	
	@PostMapping("/add")
	public ResponseEntity<?> addPayment(@RequestBody PaymentPayload payment){
		try {
		this.template.send(TIPIC,String.valueOf("new-payment"),
							new PaymentPayload()
							.setId(payment.getId())
							.setPatientUid(payment.getPatientUid())
							.setAmount(payment.getAmount()));
		return ResponseEntity.ok("SUCCESS");
		} catch (Exception e) {
			log.error("FAIL TO SAVE MESSAGE {} ",e.getMessage());
			return ResponseEntity.ok("ERROR");
		}		
	}


}
