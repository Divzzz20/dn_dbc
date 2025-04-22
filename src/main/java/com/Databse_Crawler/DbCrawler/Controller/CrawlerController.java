package com.Databse_Crawler.DbCrawler.Controller;

import com.Databse_Crawler.DbCrawler.dto.DBConfig;
import com.Databse_Crawler.DbCrawler.Model.DatabaseModel;
import com.Databse_Crawler.DbCrawler.Service.DatabaseCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CrawlerController {

    @Autowired
    private DatabaseCrawlerService service;

    @GetMapping("/schema")
    public ResponseEntity<?> getSchema() {
        try {
            DatabaseModel db = service.crawlSchema(); // You need to implement this
            return ResponseEntity.ok(db);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/schema/config")
    public ResponseEntity<?> getSchemaWithConfig(@RequestBody DBConfig config) {
        try {
            DatabaseModel db = service.crawlSchemaWithConfig(config); // Optional method
            return ResponseEntity.ok(db);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }





}
