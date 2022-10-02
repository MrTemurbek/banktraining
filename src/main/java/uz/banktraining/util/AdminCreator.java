package uz.banktraining.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class AdminCreator {
    @Value("${database.url}")
    private String url1;
    @Value("${database.username}")
    private String username1;
    @Value("${database.password}")
    private String password1;

    public static Connection connection = null;
    public static Statement statement = null;

    @Bean
    public void createDatabase() throws SQLException {
        connection = DriverManager.getConnection(url1, username1, password1);
        System.out.println("Connection to database is success");
        statement = connection.createStatement();
        try{
            statement.execute("create database 'banktraining'" );
            System.out.println("BankTraining Database created successfully");
        }
        catch (Exception e){
            System.out.println("BankTraining Database is already created");
        }
        try {
            statement.execute("create table admins\n" +
                    "(\n" +
                    "    id       bigserial\n" +
                    "        primary key,\n" +
                    "    username varchar(255),\n" +
                    "     password varchar(255)\n" +
                    ");\n" +
                    "\n" +
                    "alter table admins\n" +
                    "    owner to postgres;\n" +
                    "\n" );
            System.out.println("Admin table is created");

        } catch (Exception e) {
            System.out.println("Admin table is already created");
        }

        try {
            statement.execute("INSERT INTO admins(id, username, password)" +
                    "values (0, 'admin123', '$2a$10$1ihBfWwwCegQUguuSSibc.4YBsVkAFSs3cF/04mYKsCp/Gd1MVPF.')");

        }
        catch (Exception e) {
            System.out.println("Admin is already created");
        }
        try {
            statement.execute("create table participants\n" +
                    "(\n" +
                    "    id               bigserial\n" +
                    "        primary key,\n" +
                    "    certificate_date varchar(255),\n" +
                    "    certificate_id   varchar(255)\n" +
                    "        constraint uk_bm71u8tvs3athyjjcpvb6xctv\n" +
                    "            unique,\n" +
                    "    course           varchar(255),\n" +
                    "    created_at       timestamp,\n" +
                    "    link             varchar(255),\n" +
                    "    name             varchar(255),\n" +
                    "    phone_number     varchar(255),\n" +
                    "    path             varchar(255),\n" +
                    "    surname          varchar(255)\n" +
                    ");\n" +
                    "\n" +
                    "alter table participants\n" +
                    "    owner to postgres;\n" +
                    "\n");
            System.out.println("Participants table is created");
        } catch (Exception e) {
            System.out.println("Participants table is already created");
        }


    }
}
