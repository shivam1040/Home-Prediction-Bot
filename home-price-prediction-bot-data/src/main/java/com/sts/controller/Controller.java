package com.sts.controller;


import java.io.IOException;
import java.io.OutputStream;

import java.util.stream.Stream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.batch.core.Job;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;




import com.sts.model.Home;
import com.sts.service.HomeServiceImpl;

@RestController
public class Controller {
	public static long progress;
	public static boolean status = false;
	public static long id = 1;
	@Value("${sqlCount}")
	private String sqlCount;
	@Autowired
	@Qualifier("myJobLauncher")
	JobLauncher jobLauncher;
	@Autowired
	Job job;
	@Autowired
	private HomeServiceImpl homeService;

	@RequestMapping(value = "/job", method = RequestMethod.GET)
	public StreamingResponseBody getStreamingResponse() {

		try {
			JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
					.toJobParameters(); 
			jobLauncher.run(job, jobParameters); //running and building a batch job
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream out) throws IOException {
				while (true) {
					if (status == true)
						break;
				}
			}
		};
	}

	@GetMapping(value = "/all")
	public ResponseEntity<StreamingResponseBody> findAll() {
		Stream<Home> employees = homeService.findAll(); //opening a stream between controller and db

		StreamingResponseBody responseBody = homeService.streamingResponseBody(employees); //Defining stream properties and output

		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_STREAM_JSON).body(responseBody);
	}
	
	@GetMapping(value = "/budgetHomes")
	public ResponseEntity<StreamingResponseBody> budgetHomes(@RequestParam float min, @RequestParam float max) {
		Stream<Home> employees = homeService.budgetHomes(min, max);
		StreamingResponseBody responseBody = homeService.streamingResponseBody(employees);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_STREAM_JSON).body(responseBody);
	}
	
	@GetMapping(value = "/sqftHomes")
	public ResponseEntity<StreamingResponseBody> sqftHomes(@RequestParam float minSqft) {
		Stream<Home> employees = homeService.sqftHomes(minSqft);
		StreamingResponseBody responseBody = homeService.streamingResponseBody(employees);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_STREAM_JSON).body(responseBody);
	}
	
	@GetMapping(value = "/ageHomes")
	public ResponseEntity<StreamingResponseBody> ageHomes(@RequestParam int year) {
		Stream<Home> employees = homeService.ageHomes(year);
		StreamingResponseBody responseBody = homeService.streamingResponseBody(employees);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_STREAM_JSON).body(responseBody);
	}
	

	@RequestMapping(value = "/t", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public void standardPriceToCSV(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=todos.csv");
		response.setCharacterEncoding("UTF-8");
		/*
		 * try(Stream<Home> todoStream = homeRepository.getAll()) { PrintWriter out =
		 * response.getWriter(); todoStream.forEach(home -> { String line =
		 * home.toString(); out.write(line); out.write("\n");
		 * entityManager.detach(home); }); out.flush(); } catch (IOException e) { throw
		 * new RuntimeException("Exception occurred while exporting results", e); }
		 */
	}

}
