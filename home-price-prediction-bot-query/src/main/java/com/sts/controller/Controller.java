package com.sts.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.sts.service.HomeService;


@RestController
public class Controller {
	@Value("${url}")
	private String url;
	@Autowired
	private HomeService homeService;
	@Autowired
	private RestTemplate restTemplate;
	/*
	@GetMapping("/get")
	public String findAl() {
		HttpClient client=HttpClient.newHttpClient();
		HttpRequest request=HttpRequest.newBuilder().uri(URI.create(url+"/all")).GET().build();
		HttpResponse<String> response=null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.body();
	}
	*/
	@GetMapping("/all")
	public ResponseEntity<StreamingResponseBody> findAll() throws MalformedURLException, IOException{
		InputStream inputStream=new URL(url+"/all").openStream();
		StreamingResponseBody responseBody=homeService.streamingResponseBody(inputStream);
	    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_STREAM_JSON).body(responseBody);
	}
	
	@PostMapping("/budgetHomes")
	public ResponseEntity<StreamingResponseBody> budgetHomes(@RequestBody Map<String, Float> request) throws MalformedURLException, IOException{
	   InputStream inputStream=new URL(url+"/budgetHomes?min="+request.get("min")+"&max="+request.get("max")).openStream();
		StreamingResponseBody responseBody=homeService.streamingResponseBody(inputStream);
	    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_STREAM_JSON).body(responseBody);
	}
	
	@PostMapping("/minSqft")
	public ResponseEntity<StreamingResponseBody> minSqft(@RequestBody Map<String, Float> request) throws MalformedURLException, IOException{
		try {
	   InputStream inputStream=new URL(url+"/sqftHomes?minSqft="+request.get("minSqft")).openStream();
		StreamingResponseBody responseBody=homeService.streamingResponseBody(inputStream);
	    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_STREAM_JSON).body(responseBody);
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_STREAM_JSON).body(null);
		}
	}
	
	@PostMapping("/ageHomes")
	public ResponseEntity<StreamingResponseBody> ageYear(@RequestBody Map<String, Integer> request) throws MalformedURLException, IOException{
	   InputStream inputStream=new URL(url+"/ageHomes?year="+request.get("year")).openStream();
		StreamingResponseBody responseBody=homeService.streamingResponseBody(inputStream);
	    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_STREAM_JSON).body(responseBody);
	}
	
	
	//endpoint for streaming through rest controller for load balancing without a standard load balancer
	@GetMapping("/a")
	public ResponseEntity<StreamingResponseBody> findAl(HttpServletResponse response) throws MalformedURLException, IOException, RestClientException, URISyntaxException{
		ServletOutputStream servletOutputStream=response.getOutputStream();

        StreamingResponseBody downloadFile = out -> {
             

                  RequestCallback requestCallBack=request->{
                        //request.getHeaders().add(//Add headers here);
                  };

                  ResponseExtractor<ServletOutputStream> responseExtractor = clientHttpResponse -> {

                          //code snippet if you want to write response stream to HttpServletResponse
                          byte[] buff = new byte[800000];
                          int bytesRead = 0;
                              while ((bytesRead = clientHttpResponse.getBody().read(buff)) != -1) {
                                servletOutputStream.write(buff, 0, bytesRead);
                                }
                          return servletOutputStream;


                          //Incase if you want to copy file to you local
                           //File ret = File.createTempFile("download", ".zip",new File("Add Local system directory address here"));
                           //StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));

                           //You can copy the the clientHttpResponse.getBody() to ByteArrayInputStream and return
                           // Don't return clientHttpResponse.getBody() directly because rest template will close the inputStream(clientHttpResponse.getBody()) after .execute completed

                           //if you want to write restTemplate.execute in dao layer , pass servletOutputStream as a argument to method

                          };

               try {
				restTemplate.execute(new URI("http://Data"+"/all"),HttpMethod.GET,requestCallBack,responseExtractor);
			} catch (RestClientException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


        };


        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_STREAM_JSON).body(downloadFile);
	}
}