package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Payment;
import com.hust.baseweb.applications.accounting.document.PaymentMethod;
import com.hust.baseweb.applications.accounting.document.PaymentType;
import com.hust.baseweb.applications.accounting.document.StatusItem;
import com.hust.baseweb.applications.accounting.entity.PaymentSequenceId;
import com.hust.baseweb.applications.accounting.repo.PaymentApplicationRepo;
import com.hust.baseweb.applications.accounting.repo.PaymentRepo;
import com.hust.baseweb.applications.accounting.repo.sequenceid.PaymentSequenceIdRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.repo.PartyRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@org.springframework.transaction.annotation.Transactional
@javax.transaction.Transactional
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepo paymentRepo;
    private PartyRepo partyRepo;
    private PaymentSequenceIdRepo paymentSequenceIdRepo;
    private PaymentApplicationRepo paymentApplicationRepo;


    @Override
    public Payment.Model createPayment(Payment.CreateModel paymentCreateModel) {
        Date now = new Date();

        Payment payment = new Payment();
        payment.setPaymentType(PaymentType.COMPANY_PAYMENT);
        payment.setPaymentMethod(PaymentMethod.BANK);
        payment.setFromCustomerId(UUID.fromString(paymentCreateModel.getPartyId()));
        payment.setAmount(paymentCreateModel.getAmount());
        payment.setAppliedAmount(0.0);
        payment.setCurrencyUomId("CUR_vnd");
        payment.setEffectiveDate(now);
        payment.setCreatedStamp(now);
        payment.setLastUpdatedStamp(now);
        payment.setStatusId(StatusItem.PAYMENT_CREATED);
        return save(payment).toModel();
    }

    @Override
    public Payment.Model getPayment(String paymentId) {
        Payment payment = paymentRepo.findById(paymentId).orElseThrow(NoSuchFieldError::new);
        Party party = partyRepo.findById(payment.getFromCustomerId()).orElse(new Party());
        return payment.toModel(party.getName());
    }

    @Override
    public List<Payment.Model> getAllPayment() {
        return paymentRepo.findAll().stream().map(Payment::toModel).collect(Collectors.toList());
    }

    @Override
    public Payment save(Payment payment) {
        if (payment.getPaymentId() == null) {
            PaymentSequenceId id = paymentSequenceIdRepo.save(new PaymentSequenceId());
            payment.setPaymentId(Payment.convertSequenceIdToPaymentId(id.getId()));
        }
        return paymentRepo.save(payment);
    }

    @Override
    public List<Payment> saveAll(List<Payment> payments) {
        List<Payment> newPayments = payments.stream()
                .filter(payment -> payment.getPaymentId() == null).collect(Collectors.toList());
        if (!newPayments.isEmpty()) {
            List<PaymentSequenceId> ids = paymentSequenceIdRepo.saveAll(newPayments.stream()
                    .map(payment -> new PaymentSequenceId())
                    .collect(Collectors.toList()));
            for (int i = 0; i < newPayments.size(); i++) {
                payments.get(i).setPaymentId(Payment.convertSequenceIdToPaymentId(ids.get(i).getId()));
            }
        }
        return paymentRepo.saveAll(payments);
    }
}
