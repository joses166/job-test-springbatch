package br.com.josehamilton.jobtestspringbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

    @Bean
    public Job jobExecution(
            @Qualifier("jobRepositoryConfigurationMysqlMeta") JobRepository jobRepository,
            @Qualifier("stepExecution") Step step
    ) {
        return new JobBuilder("jobExecution", jobRepository)
                .start(step)
                .build();
    }

}
