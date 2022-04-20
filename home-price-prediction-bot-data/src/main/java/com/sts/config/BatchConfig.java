/*
 * Synchronous/Asynchronous configuration for batch processing using Spring Boot
 */

package com.sts.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.sts.controller.Controller;
import com.sts.model.Home;

@Configuration
public class BatchConfig {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Value("${file}")
	private String file;
	@Value("${fields}")
	private String[] fields;
	@Value("${indexes}")
	private int[] indexes;
	@Value("${sql}")
	private String sql;
	public int chunk=500;
	 @Autowired
	private JobRepository jobRepository;
	
	@Bean
	public ItemReader<Home> reder(){
		FlatFileItemReader<Home> fileItemReader=new FlatFileItemReader<Home>();
		fileItemReader.setResource(new ClassPathResource(file));
		fileItemReader.setLineMapper(getLineMapper());
		fileItemReader.setLinesToSkip(1);
		return fileItemReader;
		
	}

	private LineMapper<Home> getLineMapper() {
		DelimitedLineTokenizer delimiter=new DelimitedLineTokenizer();
		delimiter.setNames(fields);
		delimiter.setIncludedFields(indexes);
		BeanWrapperFieldSetMapper<Home> beanWrapperFieldSetMapper=new BeanWrapperFieldSetMapper<Home>();
		beanWrapperFieldSetMapper.setTargetType(Home.class);
		DefaultLineMapper<Home> lineMapper=new DefaultLineMapper<Home>();
		lineMapper.setLineTokenizer(delimiter);
		lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return lineMapper;
	}
	
	@Bean
	public ItemProcessor<Home, Home> homeItemProcessor() {
		return new HomeItemProcessor();
	}
	
	@Bean
	public ItemWriter<Home> writer(){
		JdbcBatchItemWriter<Home> jdbcBatchItemWriter=new JdbcBatchItemWriter<Home>();
		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Home>());
		jdbcBatchItemWriter.setSql(sql);
		jdbcBatchItemWriter.setDataSource(dataSource);
		return jdbcBatchItemWriter;
	}
	
	@Bean
	public Step step() {
		return stepBuilderFactory.get("step")
				.<Home, Home>chunk(chunk)
				.reader(reder())
				.processor(homeItemProcessor())
				.writer(writer())
				.build();
	}
	
	@Bean
    public JobExecutionListener myjoblistener() {

        JobExecutionListener listener = new JobExecutionListener() {

            @Override
            public void beforeJob(JobExecution jobExecution) {

            }

            @Override
            public void afterJob(JobExecution jobExecution) {
               Controller.status=true;
            }
        };

        return listener ;
    }
	
	@Bean
	public Job job() {
		Flow flowA = new FlowBuilder<Flow>("flowA").start(step()).build();
		Flow parallelFlow = new FlowBuilder<Flow>("parellelFlow").split(new SimpleAsyncTaskExecutor())
				.add(flowA).build();
		
		
		return jobBuilderFactory.get("USER-IMPORT-JOB")
				.incrementer(new RunIdIncrementer())
				.flow(step())
				.end()
				.listener(myjoblistener())
				.build();
			
				
		
		/*
		return jobBuilderFactory.get("USER-IMPORT-JOB").incrementer(new RunIdIncrementer()).start(parallelFlow).end().listener(myjoblistener())
				.build();
			*/	
	}
	
	
	@Bean(name = "myJobLauncher")
    public JobLauncher simpleJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}	
