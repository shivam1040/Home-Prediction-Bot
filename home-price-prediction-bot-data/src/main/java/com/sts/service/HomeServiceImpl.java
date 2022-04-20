package com.sts.service;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sts.model.Home;

@Service
public class HomeServiceImpl implements HomeService {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static boolean priceCheck=false;
	
	//Query generation and execution method using JDBC
	private Stream<Home> queryGenerator(String sql){ 
		jdbcTemplate.setFetchSize(10);
        return jdbcTemplate.queryForStream(sql,
                (resultSet, rowNum) ->
        				
                        new Home(resultSet.getLong("id"), resultSet.getString("date"), resultSet.getFloat("price"), resultSet.getFloat("bedrooms"), 
                        		resultSet.getFloat("bathrooms"), resultSet.getFloat("sqft_living"), resultSet.getFloat("sqft_lot"), resultSet.getFloat("floors"), 
                        		resultSet.getInt("waterfront"), resultSet.getInt("view"), resultSet.getInt("conditions"), resultSet.getFloat("sqft_above"), resultSet.getFloat("sqft_basement"), 
                        		resultSet.getInt("yr_built"), resultSet.getInt("yr_renovated"), resultSet.getString("street"), 
                        		resultSet.getString("city"), resultSet.getString("statezip"), resultSet.getString("country")));
	}
	@Override
	public Stream<Home> findAll() {
		priceCheck=true;
		String sql="select * from home";
        return queryGenerator(sql);
	}
	
public float newPrice(Home home) {
		float f=(((home.getBedrooms()*home.getBathrooms()*(home.getSqft_living()/home.getSqft_lot())*home.getFloors())+home.getWaterfront()+home.getView())
				*home.getCondition()*(home.getSqft_above()+home.getSqft_basement())-10*(2022-Math.max(home.getYr_built(), home.getYr_renovated())))*100;
		return f;
	}

	//Internal operation done for StreamingResponseBody used in Controller
	public StreamingResponseBody streamingResponseBody(Stream<Home> stream) {
		StreamingResponseBody responseBody = httpResponseOutputStream -> {
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonGenerator jg = objectMapper.getFactory().createGenerator(writer); //Initiallizing properties for JSON objects
				jg.useDefaultPrettyPrinter();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				objectMapper.setDateFormat(df);
				// Write the start array token
				jg.writeStartArray();
				stream.forEach(employee -> {
					try {
						if(priceCheck)
							employee.setPrice(newPrice(employee));

						objectMapper.writeValue(jg, employee);

						writer.flush();
						entityManager.detach(employee);
					} catch (IOException exception) {

					}
				});
				writer.write(" ]");
				
			} catch (Exception exception) {

			}
			finally {
				priceCheck=false;
				stream.close();
			}

		};
		return responseBody;
	}

	@Override
	public Stream<Home> budgetHomes(double minPrice, double maxPrice) {
		
		//jdbcTemplate.setFetchSize(10);
		String sql="select * from home where price between "+minPrice+" and "+maxPrice;
		//System.out.println(sql);
        return queryGenerator(sql);
	}
	
	@Override
	public Stream<Home> sqftHomes(float minSqft) {
		//jdbcTemplate.setFetchSize(10);
				String sql="select * from home where sqft_living>"+minSqft;
				//System.out.println(sql);
		        return queryGenerator(sql);
	}
	
	@Override
	public Stream<Home> ageHomes(int year) {
		String sql="select * from home where yr_built>"+year+" or yr_renovated>"+year;
		//System.out.println(sql);
        return queryGenerator(sql);
	}

}

	
/*
 * long  id,String date, float price, float bedrooms, float bathrooms, float sqft_living, float sqft_lot,
			float floors, int waterfront, int view, int condition, float sqft_above, float sqft_basement, int yr_built,
			int yr_renovated, String street, String city, String statezip, String country
 
resultSet.getLong("id"), resultSet.getString("date"), resultSet.getFloat("price"), resultSet.getFloat("bedrooms"), 
                        		resultSet.getFloat("sqft_living"),
                        		resultSet.getFloat("sqft_lot"), resultSet.getFloat("floors"), resultSet.getInt("waterfront"), resultSet.getInt("view"),
                        		resultSet.getInt("conditions"), resultSet.getFloat("sqft_above"), resultSet.getFloat("sqft_basement"), 
                        		resultSet.getInt("yr_built"),
                        		resultSet.getInt("yr_renovated"), resultSet.getString("street"), resultSet.getString("city"), 
                        		resultSet.getString("statezip"), resultSet.getString("country")
*/