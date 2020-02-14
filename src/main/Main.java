package main;

import se.pricer.interfaces.public_5_0.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@WebServlet("/Main")
public class Main extends HttpServlet {

    private PricerPublicAPI_5_0 pricerInterface;

    @Override
    public void init() {
        try {
            connect();
        } catch (RemoteException | NoSuchAlgorithmException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws RemoteException, MalformedURLException, NotBoundException, NoSuchAlgorithmException {
        final String API_USER = "config";
        final String API_KEY = "llIktpbDKTsYgxEK_zWlaRT-";

        this.pricerInterface = getR5API(API_USER, API_KEY);
        this.pricerInterface.isAlive(0);
    }

    private PricerPublicAPI_5_0 getR5API(final String user, final String key) throws NotBoundException, MalformedURLException, RemoteException, NoSuchAlgorithmException {
        String curl = "rmi://localhost:11096/pricer_5_0";
        PricerPublicApiConnector_5_0 publicApiConnector = (PricerPublicApiConnector_5_0) Naming.lookup(curl);

        String challenge = publicApiConnector.getLoginChallenge(user);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(challenge.getBytes(Charset.forName("UTF-8")));
        digest.update(key.getBytes(Charset.forName("UTF-8")));
        String hash = Base64.getEncoder().encodeToString(digest.digest());

        String apiName = publicApiConnector.getApiName(user, hash);

        String purl = "rmi://localhost:11096/" + apiName;
        return (PricerPublicAPI_5_0) Naming.lookup(purl);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ID = request.getParameter("item_ID");
        String button = request.getParameter("button");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        if(button.equals("flashOFF")){
            String r = this.stopFlash(ID);
            out.write(r);
        }
        else if(button.equals("flashON")){
            String r = this.flashItem(ID);
            out.write(r);
        }
    }

    private String flashItem(String itemID) throws RemoteException {
        String result = "";
        Item_5_0 item = this.pricerInterface.getItem(itemID);
        if(item == null) return "NO ITEM";
        try {
            List<ESL_5_0> labels = this.pricerInterface.getLinkedESLs(itemID);
            if(labels.isEmpty()){
                return "NO LABELS FOUND";
            }
            result = this.flashLED_ON(labels.get(0).getBarcode(), 30, true, false);
            return "Flash ON: " + result + ", item: " + item.getItemProperties().get(2).getValue();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public String stopFlash(String itemID) throws RemoteException {
        String result = "";
        Item_5_0 item = this.pricerInterface.getItem(itemID);
        if(item == null) return "NO ITEM";
        try {
            List<ESL_5_0> labels = this.pricerInterface.getLinkedESLs(itemID);
            if(labels.isEmpty()){
                return "NO LABELS FOUND";
            }
            result = this.flashLED_OFF(labels.get(0).getBarcode(), true);
            return "Flash OFF: " + result + ", item: " + item.getItemProperties().get(2).getValue();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    private String flashLED_ON(String barcode, int time, boolean realTimeTransmission, boolean strongLed){
        try {
            String flash = this.pricerInterface.flashLedOn(barcode, time, realTimeTransmission, strongLed);
            return flash;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    private String flashLED_OFF(String barcode, boolean realTimeTransmission){
        try {
            String flash = this.pricerInterface.flashLedOff(barcode, realTimeTransmission);
            return flash;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
}
