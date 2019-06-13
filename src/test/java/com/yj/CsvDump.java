package com.yj;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 객체ID	객체명	한글명	영문명	영문의미	설명	한자	단어유형명	단어유형	시작일시	만료일시
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CsvDump {
    private String id;
    private String name;
    private String hanName;
    private String engName;
    private String nameMean;
    private String desc;
    private String hanja;
    private String wordTypeName;
    private String workType;
    private String startDate;
    private String expiredDate;

    public String toCsv() {
        return
                id + ',' +
                name + ',' +
                hanName + ',' +
                engName + ',' +
                nameMean + ',' +
                desc + ',' +
                hanja + ',' +
                wordTypeName + ',' +
                workType + ',' +
                startDate + ',' +
                expiredDate;
    }
}
