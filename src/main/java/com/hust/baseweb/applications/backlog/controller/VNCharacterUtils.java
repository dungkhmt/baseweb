package com.hust.baseweb.applications.backlog.controller;

import java.util.Arrays;

public class VNCharacterUtils {

    private static final char[] SOURCE_CHARACTERS = {'á', 'à', 'ả', 'ã', 'ạ',
                                                     'ă', 'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ',
                                                     'â', 'ấ', 'ầ', 'ẩ', 'ẫ', 'ậ',
                                                     'é', 'è', 'ẻ', 'ẽ', 'ẹ',
                                                     'ê', 'ế', 'ề', 'ể', 'ễ', 'ệ',
                                                     'í', 'ì', 'ỉ', 'ĩ', 'ị',
                                                     'đ',
                                                     'ó', 'ò', 'ỏ', 'õ', 'ọ',
                                                     'ô', 'ố', 'ồ', 'ổ', 'ỗ', 'ộ',
                                                     'ơ', 'ớ', 'ờ', 'ở', 'ỡ', 'ợ',
                                                     'ú', 'ù', 'ủ', 'ũ', 'ụ',
                                                     'ư', 'ứ', 'ừ', 'ử', 'ữ', 'ự',
                                                     'ý', 'ỳ', 'ỷ', 'ỹ', 'ỵ',
                                                     'Á', 'À', 'Ả', 'Ã', 'Ạ',
                                                     'Ă', 'Ắ', 'Ằ', 'Ẳ', 'Ẵ', 'Ặ',
                                                     'Â', 'Ấ', 'Ầ', 'Ẩ', 'Ẫ', 'Ậ',
                                                     'É', 'È', 'Ẻ', 'Ẽ', 'Ẹ',
                                                     'Ê', 'Ế', 'Ề', 'Ể', 'Ễ', 'Ệ',
                                                     'Í', 'Ì', 'Ỉ', 'Ĩ', 'Ị',
                                                     'Đ',
                                                     'Ó', 'Ò', 'Ỏ', 'Õ', 'Ọ',
                                                     'Ô', 'Ố', 'Ồ', 'Ổ', 'Ỗ', 'Ộ',
                                                     'Ơ', 'Ớ', 'Ờ', 'Ở', 'Ỡ', 'Ợ',
                                                     'Ú', 'Ù', 'Ủ', 'Ũ', 'Ụ',
                                                     'Ư', 'Ứ', 'Ừ', 'Ử', 'Ữ', 'Ự',
                                                     'Ý', 'Ỳ', 'Ỷ', 'Ỹ', 'Ỵ',};

    private static final char[] DESTINATION_CHARACTERS = {'a', 'a', 'a', 'a', 'a',
                                                          'a', 'a', 'a', 'a', 'a', 'a',
                                                          'a', 'a', 'a', 'a', 'a', 'a',
                                                          'e', 'e', 'e', 'e', 'e',
                                                          'e', 'e', 'e', 'e', 'e', 'e',
                                                          'i', 'i', 'i', 'i', 'i',
                                                          'd',
                                                          'o', 'o', 'o', 'o', 'o',
                                                          'o', 'o', 'o', 'o', 'o', 'o',
                                                          'o', 'o', 'o', 'o', 'o', 'o',
                                                          'u', 'u', 'u', 'u', 'u',
                                                          'u', 'u', 'u', 'u', 'u', 'u',
                                                          'y', 'y', 'y', 'y', 'y',
                                                          'A', 'A', 'A', 'A', 'A',
                                                          'A', 'A', 'A', 'A', 'A', 'A',
                                                          'A', 'A', 'A', 'A', 'A', 'A',
                                                          'E', 'E', 'E', 'E', 'E',
                                                          'E', 'E', 'E', 'E', 'E', 'E',
                                                          'I', 'I', 'I', 'I', 'I',
                                                          'D',
                                                          'O', 'O', 'O', 'O', 'O',
                                                          'O', 'O', 'O', 'O', 'O', 'O',
                                                          'O', 'O', 'O', 'O', 'O', 'O',
                                                          'U', 'U', 'U', 'U', 'U',
                                                          'U', 'U', 'U', 'U', 'U', 'U',
                                                          'Y', 'Y', 'Y', 'Y', 'Y',};

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }
}
