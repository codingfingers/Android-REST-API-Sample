package rest.sample.codingfingers.com.androidrestapisample.api.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse implements Serializable {

    @JsonProperty("token")
    public String token;

}
