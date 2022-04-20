/*
 * Class for optimised insertion of data from CSV to DB
 */

package com.sts.config;

import org.springframework.batch.item.ItemProcessor;

import com.sts.controller.Controller;
import com.sts.model.Home;

public class HomeItemProcessor implements ItemProcessor<Home, Home> {
	
	@Override
	public Home process(Home item) throws Exception {
		item.setId(Controller.id++);
		//System.out.println(item.getId());
		return item;
	}

}
