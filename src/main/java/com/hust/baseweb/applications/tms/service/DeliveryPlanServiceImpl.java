package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.model.createdeliveryplan.CreateDeliveryPlanInputModel;
import com.hust.baseweb.applications.tms.repo.DeliveryPlanRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeliveryPlanServiceImpl implements DeliveryPlanService {

    private DeliveryPlanRepo deliveryPlanRepo;

    @Override
    public DeliveryPlan save(CreateDeliveryPlanInputModel input) {

        DeliveryPlan deliveryPlan = new DeliveryPlan();
        deliveryPlan.setCreatedByUserLoginId(input.getCreatedByUserLoginId());
        deliveryPlan.setDescription(input.getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date deliveryDate = null;
        try {
            deliveryDate = formatter.parse(input.getDeliveryDate());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        deliveryPlan.setDeliveryDate(deliveryDate);
        deliveryPlan = deliveryPlanRepo.save(deliveryPlan);

        return deliveryPlan;
    }

    @Override
    public Page<DeliveryPlan> findAll(Pageable pageable) {
        return deliveryPlanRepo.findAll(pageable);
    }

    @Override
    public DeliveryPlan findById(UUID deliveryPlanId) {
        return deliveryPlanRepo.findById(deliveryPlanId).orElseThrow(NoSuchElementException::new);
    }

}
