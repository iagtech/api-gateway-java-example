import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.Base64;

public class Main {

    // your unique identifier (provided by IAG)
    private static final String CLIENT_ID = "";

    // your unique secret (provided by IAG)
    private static final String CLIENT_SECRET = "";

    // the target API's unique identifier (provided by IAG)
    private static final String TARGET_API_ID = "";

    // A comma separated list of permissions required (provided by IAG)
    private static final String PERMISSIONS = "";


    /* Connect to the EPortalAuth API endpoints to
     * obtain a 1 hour timed bearer token.
     */
    private static String obtainBearerToken() {
        String authorizationToken = Base64
            .getEncoder()
            .encodeToString(
                String
                    .format("%s:%s", CLIENT_ID, CLIENT_SECRET)
                    .getBytes()
            );

        HttpResponse<JsonNode> response = Unirest.post("https://eportalauth.com/oauth2/token")
            .header("Authorization", "Basic " + authorizationToken)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(
                String
                    .format("grant_type=client_credentials&scope=target-entity:%s:%s", TARGET_API_ID, PERMISSIONS)
            ).asJson();

        if(response.isSuccess()) {
            return response.getBody().getObject().getString("access_token");
        }
        throw new RuntimeException("could not obtain Bearer Token");
    }

    public static void main(String[] args) {
        String bearerToken = obtainBearerToken();

        // after obtaining the bearer token, connect to the API endpoint
        Unirest.get(("https://test.com/sample/api/endpoint"))
            .header("Authorization", "Bearer " + bearerToken)
            .asJson();

        // after obtaining the bearer token, connect to the API endpoint
        Unirest.post(("https://test.com/sample/api/endpoint"))
            .header("Authorization", "Bearer " + bearerToken)
            .header("Content-Type", "application/json")
            .asJson();
    }
}
