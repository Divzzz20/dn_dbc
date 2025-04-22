package com.Databse_Crawler.DbCrawler.Service;

import com.Databse_Crawler.DbCrawler.Model.ColumnModel;
import com.Databse_Crawler.DbCrawler.Model.DatabaseModel;
import com.Databse_Crawler.DbCrawler.Model.ForeignKeyModel;
import com.Databse_Crawler.DbCrawler.Model.TableModel;
import com.Databse_Crawler.DbCrawler.dto.DBConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.DriverManager;


@Service
public class DatabaseCrawlerService {

    private final DataSource dataSource;

    public DatabaseCrawlerService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = connection.getCatalog();

            System.out.println("Connected to DB: " + catalog);
            System.out.println("------ Tables and Columns ------");

            DatabaseModel databaseModel = new DatabaseModel();
            databaseModel.setName(catalog);

            ResultSet tables = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                TableModel tableModel = new TableModel();
                tableModel.setName(tableName);

                ResultSet columns = metaData.getColumns(catalog, null, tableName, "%");
                while (columns.next()) {
                    ColumnModel column = new ColumnModel();
                    column.setName(columns.getString("COLUMN_NAME"));
                    column.setType(columns.getString("TYPE_NAME"));
                    column.setSize(columns.getInt("COLUMN_SIZE"));
                    column.setNullable(columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    tableModel.getColumns().add(column);
                }

                ResultSet pk = metaData.getPrimaryKeys(catalog, null, tableName);
                while (pk.next()) {
                    tableModel.getPrimaryKeys().add(pk.getString("COLUMN_NAME"));
                }

                ResultSet fk = metaData.getImportedKeys(catalog, null, tableName);
                while (fk.next()) {
                    ForeignKeyModel foreignKey = new ForeignKeyModel();
                    foreignKey.setFkColumn(fk.getString("FKCOLUMN_NAME"));
                    foreignKey.setPkTable(fk.getString("PKTABLE_NAME"));
                    foreignKey.setPkColumn(fk.getString("PKCOLUMN_NAME"));
                    tableModel.getForeignKeys().add(foreignKey);
                }

                databaseModel.getTables().add(tableModel);
                generateJavaClass(tableModel);
            }

            printDatabaseModel(databaseModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateJavaClass(TableModel tableModel) {
        String className = capitalize(tableModel.getName());
        StringBuilder classContent = new StringBuilder();

        // Class declaration
        classContent.append("package com.generated;\n\n");
        classContent.append("public class ").append(className).append(" {\n");

        // Generating fields for columns
        for (ColumnModel column : tableModel.getColumns()) {
            String fieldName = column.getName();

            if (fieldName.equalsIgnoreCase("class")) {
                column.setName("studentClass");
                System.out.println("Changed");
            }

            classContent.append("\tprivate ").append(getJavaType(column.getType())).append(" ")
                    .append(column.getName()).append(";\n");
        }

        // Constructor
        classContent.append("\n\tpublic ").append(className).append("() {\n\t}\n");

        // Getters and Setters
        for (ColumnModel column : tableModel.getColumns()) {
            String capitalizedField = capitalize(column.getName());
            classContent.append("\n\tpublic ").append(getJavaType(column.getType())).append(" get")
                    .append(capitalizedField).append("() {\n\t\treturn ")
                    .append(column.getName()).append(";\n\t}\n");

            classContent.append("\n\tpublic void set").append(capitalizedField).append("(")
                    .append(getJavaType(column.getType())).append(" ").append(column.getName())
                    .append(") {\n\t\tthis.").append(column.getName()).append(" = ")
                    .append(column.getName()).append(";\n\t}\n");
        }

        // ToString method (optional)
        classContent.append("\n\t@Override\n\tpublic String toString() {\n\t\treturn ")
                .append("\"").append(className).append(" {\" +\n\t\t\t\t")
                .append(tableModel.getColumns().stream()
                        .map(column -> "\"" + column.getName() + ": \" + " + column.getName())
                        .reduce((s1, s2) -> s1 + " + \", \" + " + s2).orElse("\"\""))
                .append(" + \"}\";\n\t}\n");

        // Closing the class
        classContent.append("}");

        // Write the generated class to a file in src/main/java/com/generated/
        try {
            String outputDir = "src/main/java/com/generated/";
            File dir = new File(outputDir);
            if (!dir.exists()) dir.mkdirs(); // Create directory if it doesn't exist

            File file = new File(outputDir + className + ".java");
            System.out.println("📄 Generating class file at: " + file.getAbsolutePath());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(classContent.toString());
            }

            System.out.println("✅ Generated class for table: " + tableModel.getName());
        } catch (Exception e) {
            System.err.println("❌ Failed to generate class for table: " + tableModel.getName());
            e.printStackTrace();
        }
    }


    private String getJavaType(String dbType) {
        // A simple type mapping; you can expand this as needed
        switch (dbType) {
            case "int":
            case "int4":
                return "int";
            case "varchar":
            case "text":
                return "String";
            case "date":
            case "timestamp":
                return "java.util.Date";
            case "bpchar":
                return "char";
            default:
                return "String";
        }
    }

    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private void printDatabaseModel(DatabaseModel databaseModel) {
        System.out.println("Database: " + databaseModel.getName());
        for (TableModel table : databaseModel.getTables()) {
            System.out.println("Table: " + table.getName());
            for (ColumnModel column : table.getColumns()) {
                System.out.println("    Column: " + column.getName() + " | Type: " + column.getType() + " | Size: " + column.getSize() + " | Nullable: " + column.isNullable());
            }
            for (String pk : table.getPrimaryKeys()) {
                System.out.println("    Primary Key: " + pk);
            }
            for (ForeignKeyModel fk : table.getForeignKeys()) {
                System.out.println("    Foreign Key: " + fk.getFkColumn() + " -> " + fk.getPkTable() + "." + fk.getPkColumn());
            }
        }
    }

    // Return database structure
    public DatabaseModel crawlSchema() {
        // Extracted from your init() method, but return databaseModel
        // You'll need to refactor your code slightly
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = connection.getCatalog();

            DatabaseModel databaseModel = new DatabaseModel();
            databaseModel.setName(catalog);

            ResultSet tables = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                TableModel tableModel = new TableModel();
                tableModel.setName(tableName);

                ResultSet columns = metaData.getColumns(catalog, null, tableName, "%");
                while (columns.next()) {
                    ColumnModel column = new ColumnModel();
                    column.setName(columns.getString("COLUMN_NAME"));
                    column.setType(columns.getString("TYPE_NAME"));
                    column.setSize(columns.getInt("COLUMN_SIZE"));
                    column.setNullable(columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    tableModel.getColumns().add(column);
                }

                ResultSet pk = metaData.getPrimaryKeys(catalog, null, tableName);
                while (pk.next()) {
                    tableModel.getPrimaryKeys().add(pk.getString("COLUMN_NAME"));
                }

                ResultSet fk = metaData.getImportedKeys(catalog, null, tableName);
                while (fk.next()) {
                    ForeignKeyModel foreignKey = new ForeignKeyModel();
                    foreignKey.setFkColumn(fk.getString("FKCOLUMN_NAME"));
                    foreignKey.setPkTable(fk.getString("PKTABLE_NAME"));
                    foreignKey.setPkColumn(fk.getString("PKCOLUMN_NAME"));
                    tableModel.getForeignKeys().add(foreignKey);
                }

                databaseModel.getTables().add(tableModel);
                generateJavaClass(tableModel); // If you want generation here
            }

            return databaseModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Or throw a custom exception
        }
    }

    // Handle config-based crawling
    public DatabaseModel crawlSchemaWithConfig(DBConfig config) {
        // Connect using custom config and return databaseModel
        String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase() + "?useSSL=false";

        try (Connection connection = DriverManager.getConnection(url, config.getUsername(), config.getPassword())) {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = connection.getCatalog();

            DatabaseModel databaseModel = new DatabaseModel();
            databaseModel.setName(catalog);

            ResultSet tables = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                TableModel tableModel = new TableModel();
                tableModel.setName(tableName);

                ResultSet columns = metaData.getColumns(catalog, null, tableName, "%");
                while (columns.next()) {
                    ColumnModel column = new ColumnModel();
                    column.setName(columns.getString("COLUMN_NAME"));
                    column.setType(columns.getString("TYPE_NAME"));
                    column.setSize(columns.getInt("COLUMN_SIZE"));
                    column.setNullable(columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    tableModel.getColumns().add(column);
                }

                ResultSet pk = metaData.getPrimaryKeys(catalog, null, tableName);
                while (pk.next()) {
                    tableModel.getPrimaryKeys().add(pk.getString("COLUMN_NAME"));
                }

                ResultSet fk = metaData.getImportedKeys(catalog, null, tableName);
                while (fk.next()) {
                    ForeignKeyModel foreignKey = new ForeignKeyModel();
                    foreignKey.setFkColumn(fk.getString("FKCOLUMN_NAME"));
                    foreignKey.setPkTable(fk.getString("PKTABLE_NAME"));
                    foreignKey.setPkColumn(fk.getString("PKCOLUMN_NAME"));
                    tableModel.getForeignKeys().add(foreignKey);
                }

                databaseModel.getTables().add(tableModel);
                generateJavaClass(tableModel);
            }

            return databaseModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}