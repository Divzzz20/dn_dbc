package com.Databse_Crawler.DbCrawler.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignKeyModel {
    private String fkColumn;
    private String pkTable;
    private String pkColumn;

}
