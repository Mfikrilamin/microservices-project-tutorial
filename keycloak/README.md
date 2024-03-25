# Setting up Postman to Call an Endpoint with Keycloak Authentication

1. **Launch Keycloak**: Start Keycloak and navigate to the "REALM SETTINGS" section.
   ![Realm setting](https://github.com/Mfikrilamin/microservices-project-tutorial/blob/feature/oauth2-authentication/assets/Realm%20setting.png)
2. **Open "OpenID Endpoint Configuration"**: In the "REALM SETTINGS" section, click on the URL labeled "OpenID Endpoint Configuration".
   ![OpenID Endpoint Configuration](https://github.com/Mfikrilamin/microservices-project-tutorial/blob/feature/oauth2-authentication/assets/OpenID%20full%20configuration.png)
3. **Open Postman**: Open Postman, a popular API client tool.
4. **Go to Authorization Subtab**: In Postman, navigate to the "Authorization" subtab, located in the request setup area.
5. **Select OAuth 2.0 Type**: From the "Type" dropdown menu, select "OAuth 2.0" as the authorization type.
6. **Pass Client ID and Client Secret**: Enter the appropriate values for the "Client ID" and "Client Secret" fields. These credentials are typically provided by the OAuth 2.0 provider (in this case, Keycloak).
   ![Client ID location](https://github.com/Mfikrilamin/microservices-project-tutorial/blob/feature/oauth2-authentication/assets/Keycloak%20client%20id.png)
   ![Client Secret location](https://github.com/Mfikrilamin/microservices-project-tutorial/blob/feature/oauth2-authentication/assets/Keycloak%20client%20secret.png)
   ![Postman Client ID and secret](https://github.com/Mfikrilamin/microservices-project-tutorial/blob/feature/oauth2-authentication/assets/Postman%20client%20ID%20and%20secret.png)
7. **Configure Authorization URL**: Find the "authorization_endpoint" in the "OpenID Endpoint Configuration" page and copy its URL. Paste this URL into the "Authorization URL" field in Postman.
8. **Configure Access Token URL**: Similarly, find the "token_endpoint" in the "OpenID Endpoint Configuration" page and copy its URL. Paste this URL into the "Access Token URL" field in Postman.
   ![OpenID configuration authorization and token endpoint](https://github.com/Mfikrilamin/microservices-project-tutorial/blob/feature/oauth2-authentication/assets/OpenID%20full%20configuration%20-%20Copy.png)
   ![Postman authorization and token endpoint](https://github.com/Mfikrilamin/microservices-project-tutorial/blob/feature/oauth2-authentication/assets/Auth%20and%20Acess%20Token%20URL.png)
9. **Generate Token**: Click on the "Get New Access Token" button in Postman to generate an access token. Postman will prompt you to log in to the OAuth 2.0 provider (Keycloak) and authorize the request.
10. **Send the Request**: Once the access token is successfully generated, you can now send the request to the desired endpoint. Postman will automatically include the access token in the request headers for authentication.
