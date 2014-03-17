package org.ambient.rest;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;

public class CreateSceneryTask extends AsyncTask<Object, Void, Void> {

	private final String URL = "/sceneries/new";

	@Override
	protected Void doInBackground(Object... params) {

		String url = Rest.getBaseUrl((String) params[0]) + URL + (String) params[1];

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		RestTemplate restTemplate = new RestTemplate(true, requestFactory);

		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

		restTemplate.put(url, params[1]);
		return null;
	}
}