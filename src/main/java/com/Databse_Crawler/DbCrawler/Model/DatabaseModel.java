package com.Databse_Crawler.DbCrawler.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseModel {
    private String name;
    private List<TableModel> tables = new ArrayList<>();
}
