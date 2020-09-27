package com.hust.baseweb.applications.education.classmanagement.utils;

import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ZipOutputStreamUtils {

    public void zipOutputStream(
        File outputZipFile, List<File> filesToAdd, char[] password,
        CompressionMethod compressionMethod, boolean encrypt,
        EncryptionMethod encryptionMethod, AesKeyStrength aesKeyStrength
    )
        throws IOException {

        ZipParameters zipParameters = buildZipParameters(compressionMethod, encrypt, encryptionMethod, aesKeyStrength);
        byte[] buff = new byte[4096];
        int readLen;

        try (ZipOutputStream zos = initializeZipOutputStream(outputZipFile, encrypt, password)) {
            for (File fileToAdd : filesToAdd) {

                // Entry size has to be set if you want to add entries of STORE compression method (no compression)
                // This is not required for deflate compression
                if (zipParameters.getCompressionMethod() == CompressionMethod.STORE) {
                    zipParameters.setEntrySize(fileToAdd.length());
                }

                zipParameters.setFileNameInZip(fileToAdd.getName());
                zos.putNextEntry(zipParameters);

                try (InputStream inputStream = new FileInputStream(fileToAdd)) {
                    while ((readLen = inputStream.read(buff)) != -1) {
                        zos.write(buff, 0, readLen);
                    }
                }
                zos.closeEntry();
            }
        }
    }

    private ZipOutputStream initializeZipOutputStream(File outputZipFile, boolean encrypt, char[] password)
        throws IOException {

        FileOutputStream fos = new FileOutputStream(outputZipFile);

        if (encrypt) {
            return new ZipOutputStream(fos, password);
        }

        return new ZipOutputStream(fos);
    }

    private ZipParameters buildZipParameters(
        CompressionMethod compressionMethod, boolean encrypt,
        EncryptionMethod encryptionMethod, AesKeyStrength aesKeyStrength
    ) {
        ZipParameters zipParameters = new ZipParameters();

        zipParameters.setCompressionMethod(compressionMethod);
        zipParameters.setEncryptionMethod(encryptionMethod);
        zipParameters.setAesKeyStrength(aesKeyStrength);
        zipParameters.setEncryptFiles(encrypt);

        return zipParameters;
    }
}
