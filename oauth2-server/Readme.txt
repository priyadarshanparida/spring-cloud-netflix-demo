Command:
curl -XPOST -H "Content-Type: application/x-www-form-urlencoded" -H "Authorization: Basic YWNtZTphY21lc2VjcmV0" http://localhost:9999/uaa/oauth/token -d "grant_type=password&username=citi&password=password"
Response:
{"access_token":"2f7a627c-18e9-45a9-b4c7-205f64298150","token_type":"bearer","refresh_token":"c294971e-74c7-4cee-bbe9-a4c2b3d846f3","expires_in":35644,"scope":"openID"}
Note: YWNtZTphY21lc2VjcmV0 Base64 encoding of acme:acmesecret
@Override
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
            .withClient("acme")
            .secret("acmesecret")
            .authorizedGrantTypes("authorization_code","refresh_token","password")
            .scopes("openID");
}

Command:
curl -H 'Authorization: bearer 2f7a627c-18e9-45a9-b4c7-205f64298150' http://localhost:9999/uaa/user
Response:
{"password":null,"username":"citi","authorities":[{"authority":"ROLE_ADMIN"},{"authority":"ROLE_USER"}],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true}
Note: Bearer token "2f7a627c-18e9-45a9-b4c7-205f64298150" is what we received above.

Postman
POST:
localhost:9999/uaa/oauth/token
Header:
Authorization: Basic YWNtZTphY21lc2VjcmV0
Note: YWNtZTphY21lc2VjcmV0 Base64 encoding of acme:acmesecret
Request Body:
grant_type: password
username: citi
password: password
Grab the access token from the response:
{
  "access_token": "63849260-df4a-4ff5-a163-a1a6d59ff83a",
  "token_type": "bearer",
  "refresh_token": "97bebe7a-f0a3-40c2-8612-2300953d8c05",
  "expires_in": 40100,
  "scope": "openID"
}

GET:
localhost:9999/uaa/user
Header (using access token grabbed from above)
Authorization: bearer 63849260-df4a-4ff5-a163-a1a6d59ff83a

GET:
localhost:9080/oauth2client
Header (using access token grabbed from above)
Authorization: bearer 63849260-df4a-4ff5-a163-a1a6d59ff83a

oAuth2 way:
Authorization URL: localhost:9999/uaa/oauth/authorize
Access Token URL: localhost:9999/uaa/oauth/token
Client ID: acme
Clioent Secret: acmesecret
Scope: openID
Grant type: Not sure!