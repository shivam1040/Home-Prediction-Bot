package com.sts.service;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Service
public class HomeService {
	
	public StreamingResponseBody streamingResponseBody(InputStream inputStream) {
		StreamingResponseBody responseBody = httpResponseOutputStream -> {

	    	try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
	    		int numberOfBytesToWrite=inputStream.read();
	    		
	    		while (numberOfBytesToWrite!=-1) {
	    			if(numberOfBytesToWrite!=-1)
	    				writer.write(numberOfBytesToWrite);
	    			numberOfBytesToWrite=inputStream.read();
	            }
	    		
			}
	    	catch (Exception exception) {
			}
	    	inputStream.close();
	    };
	    return responseBody;
	}
}
