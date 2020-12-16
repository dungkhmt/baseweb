package com.hust.baseweb.applications.waterresourcesmanagement.service;

import com.hust.baseweb.applications.waterresourcesmanagement.entity.Lake;
import com.hust.baseweb.applications.waterresourcesmanagement.entity.LakeRole;
import com.hust.baseweb.applications.waterresourcesmanagement.model.LakeLiveInfoModel;
import com.hust.baseweb.applications.waterresourcesmanagement.model.LakeModel;
import com.hust.baseweb.applications.waterresourcesmanagement.repo.LakeRepo;
import com.hust.baseweb.applications.waterresourcesmanagement.repo.LakeRoleRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LakeServiceImpl implements LakeService {
    private LakeRepo lakeRepo;
    private LakeRoleRepo lakeRoleRepo;

    @Override
    public Lake findLakeById(String lakeId) {
        return lakeRepo.findByLakeId(lakeId);
    }

    @Override
    public Lake edit(UserLogin userLogin, LakeModel lakeModel, String lakeId){
        Lake lake = lakeRepo.findByLakeId(lakeId);

        lake.setLakeName(lakeModel.getLakeName());
        try {
            String[] latlng = lakeModel.getPosition().split(",");
            if (latlng[0].trim() != "")
            lake.setLatitude(latlng[0].trim());
            if (latlng[1].trim() != "")
                lake.setLatitude(latlng[0].trim());
            lake.setLongitude(latlng[1].trim());

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        lake.setCapCongTrinh(lakeModel.getCapCongTrinh());

        lake.setDienTichLuuVuc(lakeModel.getDienTichLuuVuc());

        lake = lakeRepo.save(lake);

        /*List<LakeRole> lakeRoleList = lakeRoleRepo.findAllByUserLoginIdAndLakeAndThruDate(userLogin.getUserLoginId(), lake, null);
        if(lakeRoleList != null && lakeRoleList.size() > 0){
            log.info("save, lakeId " + lake.getLakeId() + " owned by userloginId " + userLogin.getUserLoginId() + " EXISTS!!!");
            return null;
        }

        LakeRole lakeRole = new LakeRole();
        lakeRole.setLake(lake);
        lakeRole.setUserLoginId(userLogin.getUserLoginId());
        lakeRole.setFromDate(new Date());
        lakeRole.setRoleTypeId("LAKE_OWNER");
        lakeRole = lakeRoleRepo.save(lakeRole);
        log.info("save, saved lakeRole successfully, lakeRoleId = " + lakeRole.getLakeRoleId());
        */
        return lake;
    }

    @Override
    public List<Lake> getLakesOwnedByUserLogin(UserLogin userLogin) {
        List<LakeRole> lakeRoles = lakeRoleRepo.findAllByUserLoginIdAndThruDate(userLogin.getUserLoginId(),null);
        log.info("getLakesOwnedByUserLogin, list.sz = " + lakeRoles.size());

        List<Lake> lakes = new ArrayList<Lake>();
        for(LakeRole lakeRole: lakeRoles){
            Lake lake = lakeRole.getLake();
            lakes.add(lake);
        }
        return lakes;
    }


    @Override
    public List<Lake> findAll() {
        return lakeRepo.findAll();
    }


    @Override
    @Transactional
    public Lake save(UserLogin userLogin, LakeModel lakeModel) {
        log.info("save lake, position = " + lakeModel.getPosition() + " lakeId = " + lakeModel.getLakeId());
        Lake lake = null;

        lake = lakeRepo.findByLakeId(lakeModel.getLakeId());
        log.info("save lake, findByLakeId, lake = " + lake);
        if(lake != null){
            log.info("save, lakeId " + lakeModel.getLakeId() + " EXISTS!!!");
            return null;
        }

        lake = new Lake();
        log.info("save lake, new Lake() OK");
        lake.setLakeId(lakeModel.getLakeId());
        log.info("save lake, setLakeId OK");
        lake.setLakeName(lakeModel.getLakeName());

        log.info("save lake setLakeName OK");

        try {
            String[] latlng = lakeModel.getPosition().split(",");
            log.info("save lake split OK");
            lake.setLatitude(latlng[0].trim());
            log.info("save lake, setLatitude OK");
            lake.setLongitude(latlng[1].trim());
            log.info("save lake setLongitude OK");
            log.info("save lake, latitude = " + lake.getLatitude() + " longitude = " + lake.getLongitude());

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        lake.setCapCongTrinh(lakeModel.getCapCongTrinh());
        lake.setDienTichLuuVuc(lakeModel.getDienTichLuuVuc());
        /*
        lake.setMucDamBaoTuoi(lakeModel.getMucDamBaoTuoi());
        lake.setDienTichTuoi(lakeModel.getDienTichTuoi());
        lake.setMucNuocChet(lakeModel.getMucNuocChet());
        lake.setMucNuocDangBinhThuong(lakeModel.getMucNuocDangBinhThuong());
        lake.setMucNuocLuThietKe(lakeModel.getMucNuocLuThietKe());
        lake.setMucNuocLuKiemTra(lakeModel.getMucNuocLuKiemTra());
        lake.setDungTichToanBo(lakeModel.getDungTichToanBo());
        lake.setDungTichHuuIch(lakeModel.getDungTichHuuIch());
        lake.setDungTichChet(lakeModel.getDungTichChet());
        lake.setLuuLuongXaLuThietKe(lakeModel.getLuuLuongXaLuThietKe());
        lake.setLuuLuongXaLuKiemTra(lakeModel.getLuuLuongXaLuKiemTra());
        */

        log.info("save lake, dienTichTuoi = " + lakeModel.getDienTichTuoi() + ", mucNuocChet = " + lakeModel.getMucNuocChet());

        lake = lakeRepo.save(lake);
        log.info("save, saved lake successfully");
        List<LakeRole> lakeRoleList = lakeRoleRepo.findAllByUserLoginIdAndLakeAndThruDate(userLogin.getUserLoginId(), lake, null);
        if(lakeRoleList != null && lakeRoleList.size() > 0){
            log.info("save, lakeId " + lake.getLakeId() + " owned by userloginId " + userLogin.getUserLoginId() + " EXISTS!!!");
            return null;
        }



        LakeRole lakeRole = new LakeRole();
        lakeRole.setLake(lake);
        lakeRole.setUserLoginId(userLogin.getUserLoginId());
        lakeRole.setFromDate(new Date());
        lakeRole.setRoleTypeId("LAKE_OWNER");
        lakeRole = lakeRoleRepo.save(lakeRole);
        log.info("save, saved lakeRole successfully, lakeRoleId = " + lakeRole.getLakeRoleId());

        return lake;
    }

    @Override
    public LakeLiveInfoModel getLiveInfoLake(String lakeId) {
        /*
        pseudo generate time series information
         */
        Lake lake = lakeRepo.findByLakeId(lakeId);
        Random R = new Random();
        double mucDamBaoTuoi = (1000 + R.nextInt(1000)) * 1.0 / 100;
        double dienTichTuoi = (10000 + R.nextInt(10000)) * 1.0 / 100;
        double mucNuocLuKiemTra = (10000 + R.nextInt(10000)) * 1.0 / 100;
        ;
        double luuLuongXaLuKiemTra = (10000 + R.nextInt(10000)) * 1.0 / 100;
        ;
        double mucNuocDangBinhThuong = (10000 + R.nextInt(10000)) * 1.0 / 100;
        ;

        lake.setMucDamBaoTuoi(mucDamBaoTuoi + "");
        lake.setDienTichTuoi(dienTichTuoi + "");
        lake.setMucNuocLuKiemTra(mucNuocLuKiemTra + "");
        lake.setLuuLuongXaLuKiemTra(luuLuongXaLuKiemTra + "");
        lake.setMucNuocDangBinhThuong(mucNuocDangBinhThuong + "");

        int[] mucNuocLuKiemTraHistory = new int[10];
        int[] luuLuongLuKiemTraHistory = new int[10];
        for (int i = 0; i < mucNuocLuKiemTraHistory.length; i++) {
            mucNuocLuKiemTraHistory[i] = R.nextInt(100) + 10;
        }
        for (int i = 0; i < luuLuongLuKiemTraHistory.length; i++) {
            luuLuongLuKiemTraHistory[i] = R.nextInt(100) + 50;
        }
        return new LakeLiveInfoModel(lake, mucNuocLuKiemTraHistory, luuLuongLuKiemTraHistory);
    }
}
