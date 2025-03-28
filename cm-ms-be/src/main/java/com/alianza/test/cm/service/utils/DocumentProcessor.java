package com.alianza.test.cm.service.utils;

import com.alianza.test.cm.model.Client;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class DocumentProcessor {

    private static final String[] CSV_HEADERS = {"Shared Key", "Business ID", "Name", "Email", "Phone", "Date Added"};

    public static byte[] generateClientsCsv(List<Client> clients) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CSV_HEADERS))) {
            for (Client client : clients) {
                csvPrinter.printRecord(
                        client.getSharedKey(),
                        client.getSharedKey(),
                        client.getName(),
                        client.getEmail(),
                        client.getPhone(),
                        ProcessorUtil.convertDate(client.getDataAdded().toString(), ProcessorUtil.DATE_FORMAT_DDMMYYYY)
                );
            }
        }

        return out.toByteArray();
    }
}
