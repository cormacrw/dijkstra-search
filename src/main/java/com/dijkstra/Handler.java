package com.dijkstra;

import java.util.*;

import com.dijkstra.models.Cave;
import com.dijkstra.request.Node;
import com.dijkstra.request.SearchParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			String body = (String) input.get("body");

			ObjectMapper objectMapper = new ObjectMapper();
			SearchParams searchParams = objectMapper.readValue(body, SearchParams.class);

			Map<Integer, Cave> caves = new HashMap<>();
			for(Node node : searchParams.getNodes()) {
				caves.put(node.getId(), new Cave(node.getX(), node.getY(), node.getId()));
			}

			for(Node node : searchParams.getNodes()) {
				for(int neighbour : node.getConnections()) {
					caves.get(node.getId()).addCave(caves.get(neighbour));
				}
			}

			PathFinder pathFinder = new PathFinder(caves);
			pathFinder.execute(caves.get(1));

			List<Cave> path = pathFinder.getPath(caves.get(searchParams.getTarget()));

			List<Integer> pathArr = new ArrayList<>();

			if(path != null) {
				for(int i = 0; i < path.size(); i++) {
					pathArr.add(path.get(i).getCaveNumber());
				}
			}

			Map<String, Object> response = new HashMap<>();
			response.put("status", 200);
			response.put("path", pathArr);


			Map<String, String> responseHeaders = new HashMap<>();
			responseHeaders.put("Access-Control-Allow-Origin","*");
			responseHeaders.put("Access-Control-Allow-Credentials", "true");

			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(response)
					.setHeaders(responseHeaders)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
			return ApiGatewayResponse.builder().setStatusCode(500).build();
		}

	}
}
