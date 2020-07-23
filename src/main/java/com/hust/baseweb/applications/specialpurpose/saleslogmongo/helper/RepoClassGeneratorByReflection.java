package com.hust.baseweb.applications.specialpurpose.saleslogmongo.helper;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.*;
import org.springframework.data.annotation.Id;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public class RepoClassGeneratorByReflection {

    public static void main(String[] args) {
        List<Class<?>> documentClasses = Arrays.asList(
            Customer.class,
            Facility.class,
            InventoryItem.class,
            InventoryItemDetail.class,
            OrderItem.class,
            Organization.class,
            Person.class,
            Product.class,
            ProductPrice.class,
            PurchaseOrder.class,
            SalesOrder.class);

        String repoFilePath = "src/main/java/com/hust/baseweb/applications/specialpurpose/saleslogmongo/repository";
        String repoPackagePath = "com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository";

        for (Class<?> documentClass : documentClasses) {
            writeRepositoryClass(repoFilePath, repoPackagePath, documentClass);
        }
    }

    public static void writeRepositoryClass(String repoFilePath, String repoPackagePath, Class<?> type) {
        String classPath = type.getName();
        String className = classPath.substring(classPath.lastIndexOf(".") + 1);
        String idFieldTypeName = null;
        String idFieldTypePath = null;
        for (Field declaredField : type.getDeclaredFields()) {
            for (Annotation declaredAnnotation : declaredField.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType() == Id.class) {
                    idFieldTypePath = declaredField.getType().getName();
                    idFieldTypeName = idFieldTypePath.substring(idFieldTypePath.lastIndexOf(".") + 1);
                    break;
                }
            }
        }

        if (idFieldTypeName == null) {
            return;
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(repoFilePath +
                                                                              "/" +
                                                                              className +
                                                                              "Repository.java"));

            String s = String.format(
                "package %s;\n" +
                "\n" +
                "import %s;\n" +
                "import %s;\n" +
                "import org.springframework.data.mongodb.repository.MongoRepository;\n" +
                "\n" +
                "/**\n" +
                " * @author Hien Hoang (hienhoang2702@gmail.com)\n" +
                " */\n" +
                "public interface %sRepository extends MongoRepository<%s, %s> {\n" +
                "\n" +
                "}\n", repoPackagePath, classPath, idFieldTypePath, className, className, idFieldTypeName
            );

            bufferedWriter.write(s);

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
