
package com.yj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
public class CsvFileWriteTest {

    private String readFile = "/Users/cpu/workspace/sources/TestProject/CsvDownload/settleDump.csv";
    private String writeFile = "/Users/cpu/workspace/sources/TestProject/CsvDownload/settleWriteDump.csv";

    private List<SettleEntityMock> settleEntityMocks;

    @Before
    public void csvToJson() {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            File file = new File(readFile);
            //MappingIterator<SettleEntityMock> readValues = mapper.readerWithSchemaFor(SettleEntityMock.class).with(bootstrapSchema).readValues(new InputStreamReader(new FileInputStream(file), "MS949"));
            MappingIterator<SettleEntityMock> readValues = mapper.readerWithSchemaFor(SettleEntityMock.class).with(bootstrapSchema).readValues(file);

            settleEntityMocks = readValues.readAll();

        } catch (Exception e) {
            log.error("Error : {} | {}", readFile, e);
        }
    }

    @Test
    //@Ignore
    public void csvFileWriteTest() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        try(
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(new FileOutputStream(writeFile), StandardCharsets.UTF_8)))
        ) {

            // UTF-8의 BOM인 "EF BB BF"를 UTF-16BE 로 변환하면 65279 - csv 에서 utf-8의 경우 bom을 해줘야 깨지지 않는다.
            bufferedWriter.write(65279);
            for (int i=0; i<=10; i++) {
                for (SettleEntityMock settleEntityMock : settleEntityMocks) {
                    //log.info("Csv Data : {}", settleEntityMock.toCsv());

                    bufferedWriter.write(settleEntityMock.toCsv());
                    bufferedWriter.newLine();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        stopwatch.stop(); // optional

        long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        log.info("millis: " + millis);  // formatted string like "12.3 ms"
        log.info("time: " + stopwatch);  // formatted string like "12.3 ms"
    }

    @Test
    //@Ignore
    public void csvFileWriteTestOfFileWrite() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Path path = Paths.get(writeFile);

        List<String> strings = new ArrayList<>();
        for (int i=0; i<=10; i++) {
            for (SettleEntityMock settleEntityMock : settleEntityMocks) {
                strings.add(settleEntityMock.toCsv());
            }
        }

        try {

            Files.write(path, strings, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException ioException) {
            log.warn("An IOException occured while trying to write the file \"" + path.toAbsolutePath() + "\" to disk.", ioException);
        }

        stopwatch.stop(); // optional

        long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        log.info("millis: " + millis);  // formatted string like "12.3 ms"
        log.info("time: " + stopwatch);  // formatted string like "12.3 ms"
    }

    @Test
    public void timeCheckToStream() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        List<String> strings = settleEntityMocks.stream().map(SettleEntityMock::toCsv).collect(Collectors.toList());
        for (int i=0; i<20; i++) {
            strings.addAll(settleEntityMocks.stream().map(SettleEntityMock::toCsv).collect(Collectors.toList()));
        }

        stopwatch.stop(); // optional

        long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        log.info("Stream millis: " + millis);
        log.info("Stream time: {} | size : {}", stopwatch, strings.size());
    }

    @Test
    public void timeCheckToforEach() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        List<String> strings = new ArrayList<>();
        for (int i=0; i<=20; i++) {
            for (SettleEntityMock settleEntityMock : settleEntityMocks) {
                strings.add(settleEntityMock.toCsv());
            }
        }

        stopwatch.stop(); // optional

        long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        log.info("Foreach millis: " + millis);
        log.info("Foreach time: {} | size : {}", stopwatch, strings.size());
    }
}
