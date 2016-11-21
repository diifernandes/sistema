package br.customercare.tcc.util;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * Created by JeanThomas on 04/09/2016.
 */
public class Conexao {

    static EnterpriseConnection connection;

    public static boolean conectaSF(String usuario, String senha){
        boolean success = false;
        String username = usuario;
        String password = senha + "dsXgBQ7kRVequd3DJfNiVGMT";
        String authEndPoint = "https://login.salesforce.com/services/Soap/c/37.0/";

            try {
                ConnectorConfig config = new ConnectorConfig();
                config.setUsername(username);
                config.setPassword(password);

                System.out.println("AuthEndPoint: " + authEndPoint);
                config.setAuthEndpoint(authEndPoint);

                connection = Connector.newConnection(config);
                success = true;
            } catch (ConnectionException ce) {
                ce.printStackTrace();
            }

        return success;
        }

    public static boolean desconectaSF(){
        boolean success = false;
        try {
            connection.logout();
            success = true;
        } catch (ConnectionException e) {
            e.printStackTrace();
        }

        return success;
    }

    public static EnterpriseConnection getConnection(){
        return connection;
    }
}
