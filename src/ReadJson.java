import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Program for print data in JSON format.
    public class ReadJson {
        private JFrame background;
        private JPanel topPannel;
        private JPanel bottomPannel;
        private JButton button;
        private JTextArea input;
        private JTextArea output;
        private int SIZE = 800;

    private void prepareGUI() {
        background = new JFrame("Test");
        background.setSize(SIZE, SIZE);
        background.setLayout(new GridLayout(2, 1));


        background.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });


        topPannel = new JPanel();
        topPannel.setLayout(new GridLayout(1, 2));

        bottomPannel = new JPanel();
        bottomPannel.setLayout(new BorderLayout());


        input = new JTextArea("Enter your input here");

        output = new JTextArea();
        output.setEditable(false);



        button = new JButton("Search");
        button.setActionCommand("Search");
        button.addActionListener(new ButtonClickListener());


        bottomPannel.add(button);
        topPannel.add(input);
        topPannel.add(output);


        background.add(topPannel);
        background.add(bottomPannel);
        background.setVisible(true);
    }


    public static void main(String args[]) throws ParseException {
        ReadJson read = new ReadJson();
        }

        public ReadJson() {
            prepareGUI();
        }
        

        public  void pull(String pokemon) throws ParseException {
            String out = "abc";
            String totlaJson = "";
                try {
                    URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + pokemon.toLowerCase());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() != 200) {

                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));


                    while ((out = br.readLine()) != null) {
                        totlaJson += out;
                    }
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(totlaJson);

            try {


                JSONArray msg = (JSONArray) jsonObject.get("abilities");
                int n = msg.size();

                for (int i = 0; i < n; ++i) {
                    JSONObject myO = (JSONObject) msg.get(i);
                    int t = myO.size();
                        JSONObject myAbility = (JSONObject) myO.get("ability");

                        output.append((String)myAbility.get("name"));
                        output.append("\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Search")) {
                try {
                    pull(input.getText());
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    }

