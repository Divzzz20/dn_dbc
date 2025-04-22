package com.Databse_Crawler.DbCrawler.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnModel {
    private String name;
    private String type;
    private int size;
    private boolean isNullable;


}
