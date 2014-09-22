package rest.sample.codingfingers.com.androidrestapisample.api;

import org.apache.http.params.CoreConnectionPNames;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import rest.sample.codingfingers.com.androidrestapisample.api.request.LoginRequest;
import rest.sample.codingfingers.com.androidrestapisample.api.response.TokenResponse;

public class ApiClient {

    public static int TIMEOUT = 60000;


    private final ObjectMapper mapper;

    public ApiClient() {
        this.mapper = new ObjectMapper();
    }

    protected String apiUrl() {
        return "http://path.to.your.api.example.com/endpoint";
    }


    protected <T> ApiResponse<T> processResponse(String url, HttpMethod method, Object params, final Class<?> responseClass) {
        final ResponseExtractor<ApiResponse<T>> extractor = new ResponseExtractor<ApiResponse<T>>() {
            @Override
            public ApiResponse<T> extractData(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().value() >= 200 && response.getStatusCode().value() < 400) {
                    try {
                        T value = null;
                        if (responseClass != Void.class) {
                            value = (T) mapper.readValue(response.getBody(), responseClass);
                        }
                        return new ApiResponse<T>(value, response.getStatusCode().value(), null, null);
                    } catch (Exception ex) {
                        return new ApiResponse<T>(null, response.getStatusCode().value(), new ApiError(ApiError.ErrorType.SERIALIZER, response.getStatusCode().value(), ex), null);
                    }
                } else {
                    return new ApiResponse<T>(null, response.getStatusCode().value(), new ApiError(ApiError.ErrorType.HTTP, response.getStatusCode().value(), null), null);
                }
            }
        };

        try {
            ApiResponse<T> resp = restTemplate().execute(url, method, getRequestCallback(params), extractor);

            return resp;
        } catch (Exception ex) {
            return new ApiResponse<T>(null, 0, new ApiError(ApiError.ErrorType.IO, 0, ex), null);
        }
    }


    protected RequestCallback getRequestCallback(final Object requestBody) {
        return new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {

                //If needed, you can set authorization headers here:
                // clientHttpRequest.getHeaders().set(HEADER_NAME, HEADER_VALUE);

                writeRequest(clientHttpRequest, requestBody);
            }
        };
    }

    protected void writeRequest(ClientHttpRequest clientHttpRequest, Object requestBody) throws IOException {
        if (requestBody != null) {
            try {
                clientHttpRequest.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                clientHttpRequest.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                mapper.writeValue(clientHttpRequest.getBody(), requestBody);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

    protected RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.getHttpClient().getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                TIMEOUT);
        requestFactory.getHttpClient().getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
                TIMEOUT);
        template.setRequestFactory(requestFactory);

        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        converters.add(new MappingJacksonHttpMessageConverter());
        template.setMessageConverters(converters);

        template.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse resp) throws IOException {
                return false; //we do error handling on our own
            }

            @Override
            public void handleError(ClientHttpResponse resp) throws IOException {
            }
        });

        return template;
    }



    public ApiResponse<TokenResponse> login(String email, String password) {

        LoginRequest requestObject = new LoginRequest();
        requestObject.email = email;
        requestObject.password = password;

        return processResponse(apiUrl() + "/session", HttpMethod.POST, requestObject, TokenResponse.class);
    }


}
