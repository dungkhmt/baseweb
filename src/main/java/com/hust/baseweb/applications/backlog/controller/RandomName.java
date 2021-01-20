package com.hust.baseweb.applications.backlog.controller;

import java.util.Random;

public class RandomName {

    private final static String[] firstNameList = {"Chu", "Dương", "Đàm", "Hà", "Hoàng", "Lý", "Liêu", "Lương", "Ninh", "Trần", "Trưởng", "Vi", "Đinh", "Hồ", "Lê", "Trương", "An", "Âu", "Bá", "Bạch", "Bùi", "Cao", "Công", "Chân", "Chu", "Diệp", "Doãn", "Đoàn", "Giang", "Đào", "Đặng", "Đông", "Hàn", "Hạng", "Hoa", "Hùng", "Hứa", "Hướng", "Kiều", "Kim", "Kha", "Khổng", "Mã", "Mạc", "Mai", "Mao", "Mẫn", "Mộc", "Ngô", "Nguỵ", "Nguyễn", "Nghị", "Nghiêm", "Nhữ", "Ông", "Phạm", "Phan", "Phó", "Võ", "Vũ", "Tô", "Tôn", "Quách", "Quan", "Quang", "Thân", "Thiệu", "Trác", "Trang", "Triệu", "Trịnh", "Ung", "Vu", "Vương", "Cao", "Chân", "Chu", "Chung", "Chử", "Chương", "Công Tôn", "Danh", "Dân", "Diệu", "Dung", "Đa", "Đạo", "Hoài", "Mã", "Mục", "Yết"};
    private final static String[] middleNameList = {"Ái", "An", "Anh", "Bảo", "Công", "Đức", "Đình", "Duy", "Gia", "Hà", "Hải", "Hiếu", "Hoài", "Hoàng", "Huy", "Khải", "Khánh", "Lan", "Mai", "Mạnh", "Minh", "Ngọc", "Nhật", "Như", "Quỳnh", "Thảo", "Thanh", "Thành", "Thiện", "Thu", "Thuỷ", "Trâm", "Tuấn", "Vân", "Vi", "Xuân", "Yên", "Văn", "Thị", "Quang", "Minh", "Gia", "Hương", "Kiến", "Sơn", "Bá", "Đình", "Kiến", "Bội", "Tuyết", "Duyên", "Nguyên", "Khắc", "Thiên", "Phúc", "Việt", "Bích", "Kim", "Hồng", "Đông", "Diễm", "Mộng", "Trúc", "Ánh", "Nguyệt", "Thái", "Quốc", "Trí"};
    private final static String[] lastNameList = {"Cường", "Tùng", "Thắng", "Sơn", "Huy", "Dũng", "Hưng", "Linh", "Ninh", "Hiếu", "Toàn", "Hiệp", "Luân", "Hậu", "Lộc", "Nam", "Long", "Minh", "Lâm", "Đạt", "Cương", "Nguyên", "Duy", "Tuấn", "Quang", "Trung", "Gia Huy", "Bảo Nam", "Anh Tuấn", "Thành", "Tuân", "Hoàng Anh", "Khải", "Chiến", "Đức", "Vương", "Phong", "Lợi", "Thuận", "Hùng", "Tân", "Dương", "Quyền", "Thi", "Quân", "Nhân", "Huỳnh", "An", "Bảo Long", "Khiêm", "Khang", "Vinh", "Tiến", "Tuyên", "Khánh", "Sang", "Hải", "Khôi", "Hảo", "Hiển", "Khoa", "Thái", "Kiên", "Vũ", "Hưởng", "Việt", "Trường", "Hoà", "Thịnh", "Bách", "Huân", "Thanh", "Kiệt", "Vượng", "Tâm", "Vĩnh", "Phương", "Thiện", "Phát", "Đăng", "Yên", "Bình", "Trọng", "Linh", "Huyền", "Nhi", "Lan Anh", "Anh Thư", "Phương Anh", "Vân Anh", "Khánh Linh", "Hiền", "Quyên", "Trang", "Ly", "Ngọc Anh", "Hằng", "Kim Ngân", "Yến", "Uyên", "Hân", "Trà My", "Như", "Duyên", "Vy", "Huệ", "Nhung", "Nguyệt", "Minh Anh", "Huyền Trang", "Hà Trang", "Thảo", "Thanh Thảo", "Hân", "Trâm", "Ánh", "Tiên", "Giang", "Trân", "Thương", "Trúc", "Mai", "Ngọc", "Hà", "Ngân", "Châu", "Hạnh", "Uyên", "Tú", "Thuý", "Loan", "Trinh", "Trang", "Chi", "Oanh", "Xuân", "Trang", "Thắm", "Liên", "Thoa", "Nhàn"};

    public static String getFirstName() {
        Random rand = new Random();
        return firstNameList[rand.nextInt(firstNameList.length) % firstNameList.length];
    }

    public static String getMiddleName() {
        Random rand = new Random();
        return middleNameList[rand.nextInt(middleNameList.length) % middleNameList.length];
    }

    public static String getLastName() {
        Random rand = new Random();
        return lastNameList[rand.nextInt(lastNameList.length) % lastNameList.length];
    }

    public static String convertNameToUserName(String firstName, String middleName, String lastName){
        String first = VNCharacterUtils.removeAccent(firstName);
        String middle = VNCharacterUtils.removeAccent(middleName);
        String last = VNCharacterUtils.removeAccent(lastName);

        Random rand = new Random();
        String randomNumber = String.valueOf(rand.nextInt(999));

        return (last + first.substring(0, 1) + middle.substring(0, 1) + randomNumber).toLowerCase().replace(" ", "");
    }

    public static String randomEmail(String e) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        int targetStringLength = random.nextInt(8);

        String domain = random.ints(leftLimit, rightLimit + 1)
                                       .limit(targetStringLength)
                                       .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                       .toString();
        return e + "@" + domain + ".com";
    }
}
