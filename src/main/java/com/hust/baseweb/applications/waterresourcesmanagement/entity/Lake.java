package com.hust.baseweb.applications.waterresourcesmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lake {

    @Id
    @Column(name = "lake_id")
    private String lakeId;

    @Column(name = "lake_name")
    private String lakeName;

    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;


    @Column(name = "cap_cong_trinh")
    private String capCongTrinh;

    @Column(name = "dien_tich_luu_vuc")
    private String dienTichLuuVuc;


    @Column(name = "muc_dam_bao_tuoi")
    private String mucDamBaoTuoi;


    @Column(name = "dien_tich_tuoi")
    private String dienTichTuoi;


    @Column(name = "muc_nuoc_chet")
    private String mucNuocChet;


    @Column(name = "muc_nuoc_dang_binh_thuong")
    private String mucNuocDangBinhThuong;

    @Column(name = "muc_nuoc_lu_thiet_ke")
    private String mucNuocLuThietKe;

    @Column(name = "muc_nuoc_lu_kiem_tra")
    private String mucNuocLuKiemTra;


    @Column(name = "dung_tich_toan_bo")
    private String dungTichToanBo;

    @Column(name = "dung_tich_huu_ich")
    private String dungTichHuuIch;

    @Column(name = "dung_tich_chet")
    private String dungTichChet;

    @Column(name = "luu_luong_xa_lu_thiet_ke")
    private String luuLuongXaLuThietKe;


    @Column(name = "luu_luong_xa_lu_kiem_tra")
    private String luuLuongXaLuKiemTra;

    /*
    muc_nuoc_ho numeric,
    dung_tich_ho numeric,
    dong_chay_den numeric,
    luong_mua_trung_binh numeric,
    do_mo_tran_so_1 numeric,
    do_mo_tran_so_2 numeric,
    do_mo_tran_so_3 numeric,
    do_mo_tran_so_4 numeric,
    do_mo_tran_so_5 numeric,

    luu_luong_tran_so_1 numeric,
    luu_luong_tran_so_2 numeric,
    luu_luong_tran_so_3 numeric,
    luu_luong_tran_so_4 numeric,
    luu_luong_tran_so_5 numeric,

    do_mo_cong numeric,
    muc_nuoc_kenh numeric,
    luu_luong_cong numeric,
    muc_nuoc_kenh numeric,
    tong_luu_luong_xa numeric,

    luong_mua numeric,
    nhiet_do numeric,
    do_am numeric,
    toc_do_gio numeric,
    huong_gio numeric,
    buc_xa_mat_troi numeric,
    */


}
