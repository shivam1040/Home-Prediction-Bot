/*
 * Global session-less filter for API based authentication
 */

package com.sts.config;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.sts.entity.Api;
import com.sts.repository.ApiRepository;

import reactor.core.publisher.Mono;

@Component
public class Filter implements GlobalFilter, Ordered {
	@Autowired
	private ApiRepository apiRepository;
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;	
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		List<String> apiKey=exchange.getRequest().getHeaders().get("apiKey");
		Optional<Api> optional=apiRepository.findById(apiKey.get(0));
		if(!CollectionUtils.isEmpty(apiKey) && !optional.isEmpty()) {
			Api api=optional.get();
			if(api.getKey().equals(apiKey.get(0))) 
				return chain.filter(exchange);
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}
}
