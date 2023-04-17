package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {

    //firmas de metodos
    List<BeerCSVRecord> convertCSV(File csvFile);
}
