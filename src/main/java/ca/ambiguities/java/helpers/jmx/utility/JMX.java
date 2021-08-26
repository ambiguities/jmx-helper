package ca.ambiguities.java.helpers.jmx.utility;

import ca.ambiguities.java.helpers.jmx.routine.IRoutine;


import javax.management.MBeanServerConnection;
import javax.management.remote.*;

import java.io.IOException;
import java.lang.SecurityException;
import java.net.MalformedURLException;
import java.util.*;

public class JMX {
    /**
     * @param host     hostname
     * @param port     jmx port
     * @param username jmx username
     * @param password jmx password
     * @return JMXConnector
     * @throws MalformedURLException
     * @throws IOException
     */
    private static JMXConnector createJMXConnection(String host, String port, String username, String password)
            throws MalformedURLException, IOException {
        String urlForJMX = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";
        Map<String, String[]> environment = null;

        if (username != null && password != null) {
            String[] credentials = new String[2];
            credentials[0] = username;
            credentials[1] = password;
            environment = new HashMap<String, String[]>();
            environment.put(JMXConnector.CREDENTIALS, credentials);
        }

        return JMXConnectorFactory.connect(new JMXServiceURL(urlForJMX), environment);
    }

    /**
     * @param properties     required properties
     */
    @SuppressWarnings("unchecked")
    public static void run(String host, HashMap<String, String> properties) {
        try {
            JMXConnector jmxc = JMX.createJMXConnection(
                    host,
                    properties.get("port"),
                    properties.get("user"),
                    properties.get("pass"));
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            Class<IRoutine> routineClass =
                    (Class<IRoutine>) Class.forName("ca.ambiguities.java.helpers.jmx.routine." + properties.get("routine"));
            IRoutine targetRoutine = routineClass.getDeclaredConstructor().newInstance();

            boolean quiet = properties.containsKey("quiet");

            if (!quiet)
                System.out.println("Target host: " + host);

            targetRoutine.run(mbsc, quiet);
            jmxc.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Routine " + properties.get("routine") + " does not exist.");
        } catch (IOException e) {
            System.out.println("Host does not exist or is not reachable: " + host);
        } catch (SecurityException e) {
            System.out.println(e.getMessage());    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
