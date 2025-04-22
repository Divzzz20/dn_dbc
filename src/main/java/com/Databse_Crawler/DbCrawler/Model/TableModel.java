package com.Databse_Crawler.DbCrawler.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableModel {
    private String name;
    private List<ColumnModel> columns = new ArrayList<>();
    private List<String> primaryKeys = new ArrayList<>();
    private List<ForeignKeyModel> foreignKeys = new ArrayList<>();
}
