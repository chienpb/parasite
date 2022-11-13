package client;

import java.io.IOException;


public class ClientSystemController {
    public ClientController clientController;

    public void clickedShutdown() throws IOException
    {
        clientController.clickedShutdown();
    }

    public void clickedLogout() throws IOException
    {
        clientController.clickedLogout();
    }
}
