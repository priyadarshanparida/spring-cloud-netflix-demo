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