package com.hust.baseweb.applications.waterresourcesmanagement.service;

import com.hust.baseweb.applications.waterresourcesmanagement.entity.Lake;
import com.hust.baseweb.applications.waterresourcesmanagement.model.LakeLiveInfoModel;
import com.hust.baseweb.applications.waterresourcesmanagement.model.LakeModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LakeService {

    public Lake findLakeById(String lakeId);

    public List<Lake> getLakesOwnedByUserLogin(UserLogin userLogin);

    public List<Lake> findAll();

    public Lake save(UserLogin userLogin, LakeModel lakeModel);

    public LakeLiveInfoModel getLiveInfoLake(String lakeId);

    public Lake edit(UserLogin userLogin, LakeModel lakeModel, String lakeId);
}
