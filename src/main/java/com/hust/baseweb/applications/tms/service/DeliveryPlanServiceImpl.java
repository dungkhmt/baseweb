package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.model.DeliveryPlanModel;
import com.hust.baseweb.applications.tms.repo.DeliveryPlanRepo;
import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeliveryPlanServiceImpl implements DeliveryPlanService {

    private DeliveryPlanRepo deliveryPlanRepo;
    private FacilityRepo facilityRepo;

    @Override
    public DeliveryPlan save(DeliveryPlanModel.Create input) {
        DeliveryPlan deliveryPlan = new DeliveryPlan();
        deliveryPlan.setCreatedByUserLoginId(input.getCreatedByUserLoginId());
        deliveryPlan.setDescription(input.getDescription());
        Date deliveryDate = null;
        try {
            deliveryDate = Constant.DATE_FORMAT.parse(input.getDeliveryDate());
        } catch (ParseException e) {
            e.printStackTrace();
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

    @Override
    public Double getTotalWeightShipmentItems(UUID deliveryPlanId) {
        return deliveryPlanRepo
            .findById(deliveryPlanId)
            .orElseThrow(NoSuchElementException::new)
            .getTotalWeightShipmentItems();
    }

}
