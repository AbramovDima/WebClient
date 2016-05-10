package webclient;

import java.io.*;
import static java.lang.System.in;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WebClient {

    static int N = 200;
    static String Resultat;
    static String NName[] = new String[N];
    static String PathN[] = new String[N];
    static String TypeN[] = new String[N];
    static String Id[] = new String[N];

    public static void connect(String Com, String Path, String Name) throws Exception {

        URL serverURL = new URL("http://127.0.0.1:8181");
        URLConnection connection = serverURL.openConnection();
        JSONObject Command = new JSONObject();
        Command.put("command", Com);
        Command.put("path", Path);
        Command.put("name", Name);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        String inputLine;

        try (OutputStreamWriter out = new OutputStreamWriter(
                connection.getOutputStream())) {
            out.write(Command.toString());
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            while ((inputLine = in.readLine()) != null) {
                String ans = inputLine;
                switch (Com) {
                    case "go":
                        try {
                            JSONParser parserA = new JSONParser();
                            JSONArray List = (JSONArray) parserA.parse(ans);
                            for (int i = 0; i < List.size(); i++) {
                                JSONObject Str = new JSONObject();
                                String str = List.get(i).toString();
                                String strO = str.substring(str.indexOf("{")) + "\n";
                                JSONParser parserO = new JSONParser();
                                Str = (JSONObject) parserO.parse(strO);
                                PathN[i] = (String) Str.get("Path");
                                NName[i] = (String) Str.get("Name");
                                TypeN[i] = (String) Str.get("Type");
                                Id[i] = (String) Str.get("Id");

                            }
                            for (int i = 0; i < NName.length; i++) {
                                if (NName[i] != null) {
                                    System.out.println(NName[i] + "," + TypeN[i] + "," + PathN[i]);
                                }
                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(WebClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "mkdir":
                        try {
                            JSONParser parser1 = new JSONParser();
                            JSONObject Result = (JSONObject) parser1.parse(ans);
                            Resultat = (String) Result.get("result");
                            System.out.println("Result " + Resultat);
                        } catch (ParseException ex) {
                            Logger.getLogger(WebClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "del":
                        try {
                            JSONParser parser1 = new JSONParser();
                            JSONObject Result = (JSONObject) parser1.parse(ans);
                            Resultat = (String) Result.get("result");
                            System.out.println("Result " + Resultat);
                            break;
                        } catch (ParseException ex) {
                            Logger.getLogger(WebClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    default:
                        break;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WebInterface().setVisible(true);
            }
        });
        connect("go", "", "");

    }
}
