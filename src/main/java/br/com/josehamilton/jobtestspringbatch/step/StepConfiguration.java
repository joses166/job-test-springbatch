package br.com.josehamilton.jobtestspringbatch.step;

import br.com.josehamilton.jobtestspringbatch.domain.Car;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class StepConfiguration {

    @Bean
    public Step stepExecution(
            @Qualifier("jobRepositoryConfigurationMysqlMeta") JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("readerExecution") ItemReader<Car> itemReader,
            @Qualifier("writerExecution")  ItemWriter<Car> itemWriter
    ) {
        return new StepBuilder("stepExecution", jobRepository)
                .<Car, Car>chunk(1, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Car> readerExecution(
            @Qualifier("appDataSource") DataSource dataSource,
            @Qualifier("queryProvider") PagingQueryProvider pagingQueryProvider
    ) {
        return new JdbcPagingItemReaderBuilder<Car>()
                .name("readerExecution")
                .dataSource(dataSource)
                .queryProvider(pagingQueryProvider)
                .pageSize(10)
                .rowMapper(new BeanPropertyRowMapper<Car>(Car.class))
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean queryProvider(
            @Qualifier("appDataSource") DataSource dataSource
    ) {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();

        queryProvider.setDataSource(dataSource);

        queryProvider.setSelectClause("SELECT id, brand, model");
        queryProvider.setFromClause("FROM tb_car");
        queryProvider.setSortKey("id");

        return queryProvider;
    }

    @Bean
    public ItemWriter<Car> writerExecution(){
        return carList -> {
            carList.getItems().forEach(item -> System.out.println("Item: " + item));
        };
    }

}
