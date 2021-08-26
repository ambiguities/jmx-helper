package ca.ambiguities.java.helpers.jmx;

import ca.ambiguities.java.helpers.jmx.utility.CLI;
import ca.ambiguities.java.helpers.jmx.utility.JMX;

import java.util.HashMap;


public class JMXHelper {
    public static void main(String[] args) {
        try {
            HashMap<String, String> properties = CLI.parse(args);
            for (String host:  properties.get("hosts").split(","))
                JMX.run(host, properties);
        } catch (Exception e) {
            if (!e.getMessage().isEmpty()) {
                System.out.println(e);
            }
            CLI.buildHelpMenu(CLI.getOptions());
        }
    }
}
