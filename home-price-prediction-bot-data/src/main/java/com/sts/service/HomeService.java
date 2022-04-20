package com.sts.service;

import java.util.stream.Stream;
import com.sts.model.Home;

public interface HomeService {
	
	Stream<Home> findAll();
	Stream<Home> budgetHomes(double minPrice, double maxPrice);
	Stream<Home> sqftHomes(float minSqft);
	Stream<Home> ageHomes(int year);
}
