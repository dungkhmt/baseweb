package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.entity.sequenceid.DeliveryPlanSequenceId;
import com.hust.baseweb.applications.tms.model.DeliveryPlanModel;
import com.hust.baseweb.applications.tms.repo.DeliveryPlanRepo;
import com.hust.baseweb.repo.DeliveryPlanSequenceIdRepo;
import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class DeliveryPlanServiceImpl implements DeliveryPlanService {

    private DeliveryPlanRepo deliveryPlanRepo;
    private FacilityRepo facilityRepo;
    private DeliveryPlanSequenceIdRepo deliveryPlanSequenceIdRepo;

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
        deliveryPlan = save(deliveryPlan);

        return deliveryPlan;
    }

    @Override
    public DeliveryPlan save(DeliveryPlan deliveryPlan) {
        if (deliveryPlan.getDeliveryPlanId() == null) {
            DeliveryPlanSequenceId id = deliveryPlanSequenceIdRepo.save(new DeliveryPlanSequenceId());
            deliveryPlan.setDeliveryPlanId(DeliveryPlan.convertSequenceIdToDeliveryPlanId(id.getId()));
        }
        return deliveryPlanRepo.save(deliveryPlan);
    }

    @Override
    public Page<DeliveryPlan> findAll(Pageable pageable) {
        return deliveryPlanRepo.findAll(pageable);
    }

    @Override
    public DeliveryPlan findById(String deliveryPlanId) {
        return deliveryPlanRepo.findById(deliveryPlanId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Double getTotalWeightShipmentItems(String deliveryPlanId) {
        return deliveryPlanRepo
            .findById(deliveryPlanId)
            .orElseThrow(NoSuchElementException::new)
            .getTotalWeightShipmentItems();
    }

}
