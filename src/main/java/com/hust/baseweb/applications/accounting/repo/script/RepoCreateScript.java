package com.hust.baseweb.applications.accounting.repo.script;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public class RepoCreateScript {

    public static void main(String[] args) throws IOException {
        String[] classes = {"Invoice",
                "InvoiceItem",
                "InvoiceItemType",
                "InvoiceStatus",
                "InvoiceType",
                "OrderItemBilling",
                "Payment",
                "PaymentApplication",
                "PaymentMethod",
                "PaymentType"};
        for (String aClass : classes) {
            writeClassToFile(aClass);
        }

    }

    static void writeClassToFile(String name) throws IOException {
        String s = "package com.hust.baseweb.applications.accounting.repo;\n" +
                "\n" +
                "import com.hust.baseweb.applications.accounting.document." + name + ";\n" +
                "import org.springframework.data.mongodb.repository.MongoRepository;\n" +
                "\n" +
                "/**\n" +
                " * @author Hien Hoang (hienhoang2702@gmail.com)\n" +
                " */\n" +
                "public interface " + name + "Repo extends MongoRepository<" + name + ", String> {\n" +
                "}\n";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(name + "Repo.java"));
        bufferedWriter.write(s);
        bufferedWriter.close();
    }

}
