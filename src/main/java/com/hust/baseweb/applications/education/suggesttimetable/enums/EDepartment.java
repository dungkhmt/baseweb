package com.hust.baseweb.applications.education.suggesttimetable.enums;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public enum EDepartment {
    BGDTC("Khoa Giáo dục Thể chất"),
    KCK("Khoa cơ khí"),
    KCNDMVTT("Viện Dệt may - Da giầy và Thời trang"),
    KCNHH("Viện kỹ thuật hóa học"),
    KCNTT("Khoa Công nghệ Thông tin và Truyền thông"),
    KD("Khoa Điện"),
    KDTVT("Khoa Điện tử Viễn thông"),
    KGDQP("Khoa Giáo dục Quốc phòng & An Ninh"),
    KKHVCNVL("Viện Khoa học và Công nghệ Nhiệt lạnh"),
    KKTVQL("Viện Kinh tế và Quản lý"),
    KML("Khoa Lý luận Chính trị"),
    KNN("Viện Ngoại ngữ"),
    KSPKT("Viện Sư phạm Kỹ thuật"),
    KTTD("Viện Toán ứng dụng và Tin học"),
    TTNNHT("Trung tâm ngôn ngữ & trao đổi học thuật"),
    VCKDL("Viện Cơ khí Động lực"),
    VCNSHVTP("Viện Công nghệ Sinh học và Công nghệ Thực phẩm"),
    VKHVCNMT("Viện Khoa học và Công nghệ Môi trường"),
    VKHVCNNL("Viện Khoa học và Công nghệ Nhiệt lạnh"),
    VMICA("Viện nghiên cứu quốc tế về thông tin đa phương tiện, truyền thông và ứng dụng"),
    VVLKT("Viện Vật lý Kỹ thuật");

    private final String name;

    EDepartment(@NotNull String name) {
        this.name = name;
    }

    public static EDepartment of(@NotNull String id) {
        if (null == id) {
            throw new IllegalArgumentException("No constant with text \"null\" found");
        }

        String normalizeId = StringUtils.deleteWhitespace(id).toUpperCase();
        switch (normalizeId) {
            case "BGDTC":
                return BGDTC;
            case "KCK":
                return KCK;
            case "KCNDMVTT":
                return KCNDMVTT;
            case "KCNHH":
                return KCNHH;
            case "KCNTT":
                return KCNTT;
            case "KD":
                return KD;
            case "KDTVT":
                return KDTVT;
            case "KGDQP":
                return KGDQP;
            case "KKHVCNVL":
                return KKHVCNVL;
            case "KKTVQL":
                return KKTVQL;
            case "KML":
                return KML;
            case "KNN":
                return KNN;
            case "KSPKT":
                return KSPKT;
            case "KTTD":
                return KTTD;
            case "TTNNHT":
                return TTNNHT;
            case "VCKDL":
                return VCKDL;
            case "VCNSHVTP":
                return VCNSHVTP;
            case "VKHVCNMT":
                return VKHVCNMT;
            case "VKHVCNNL":
                return VKHVCNNL;
            case "VMICA":
                return VMICA;
            case "VVLKT":
                return VVLKT;
            default:
                throw new IllegalArgumentException("No constant with text \"" + id + "\" found");
        }
    }
}
