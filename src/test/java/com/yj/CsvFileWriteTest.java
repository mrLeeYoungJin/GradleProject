package com.yj;

import com.google.common.base.Stopwatch;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
public class CsvFileWriteTest {

    private static CellProcessor[] getProcessors() {
        //new NotNull();
        final CellProcessor[] processors = new CellProcessor[] {
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional()
                };
        return processors;
     }

    @Test
    public void csvFileReadTest_테스트() {
        String readFile = "E:/development/fileSource201812R/GradleProject/csvDumpTest.csv";

        String writeFile = "E:/development/fileSource201812R/GradleProject/csvDumpWriteTest.csv";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try(ICsvBeanReader beanReader = new CsvBeanReader(new InputStreamReader(new FileInputStream(readFile),"MS949"), CsvPreference.STANDARD_PREFERENCE);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(readFile));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(writeFile));
        ) {

            // the header elements are used to map the values to the bean (names must match)
            //final String[] header = beanReader.getHeader(true);
            final String[] header = new String[] { "id","name","hanName","engName","nameMean","desc","hanja","wordTypeName","workType","startDate","expiredDate" };
            final CellProcessor[] processors = getProcessors();

            log.info("header : {}", Arrays.toString(header));
            log.info("beanReader.getUntokenizedRow() : {}", beanReader.getUntokenizedRow());
            log.info("processors : {}", Arrays.toString(processors));

            CsvDump csvDump;
            while ((csvDump = beanReader.read(CsvDump.class, header, processors)) != null) {
                //log.info(String.format("lineNo=%s, rowNo=%s, csvDump=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), csvDump.toString()));
                log.info(String.format("lineNo=%s, rowNo=%s getUntokenizedRow=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), beanReader.getUntokenizedRow()));

                log.info("bufferedReader : {}", bufferedReader.readLine());
                //log.info("Csv Data : {}", csvDump.toCsv());
                bufferedWriter.write(csvDump.toCsv());
                bufferedWriter.newLine();
            }

//            String line = "";
//            while((line = bufferedReader.readLine()) != null) {
//
//                CsvDump csvDump = new CsvDump();
//                csvDump.setId(line.);
//
//                bufferedWriter.write(line);
//                bufferedWriter.newLine();
//            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        stopwatch.stop(); // optional

        long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        log.info("millis: " + millis);  // formatted string like "12.3 ms"
        log.info("time: " + stopwatch);  // formatted string like "12.3 ms"
    }

}

