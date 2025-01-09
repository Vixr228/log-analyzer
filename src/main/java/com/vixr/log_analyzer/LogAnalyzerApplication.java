package com.vixr.log_analyzer;

import com.vixr.log_analyzer.parser.LogParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogAnalyzerApplication.class, args);
	}


    //TODO Стоит ли хранить статистику
    //TODO АПИ для получения статистики или логов отдельно через запрос


}
