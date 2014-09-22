package rest.sample.codingfingers.com.androidrestapisample.api.request;

import org.codehaus.jackson.annotate.JsonProperty;

public class LoginRequest {

    @JsonProperty("email")
    public String email;

    @JsonProperty("password")
    public String password;

}