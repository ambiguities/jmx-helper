package ca.ambiguities.java.helpers.jmx.utility;

import ca.ambiguities.java.helpers.jmx.JMXHelper;
import org.apache.commons.cli.*;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class CLI {
    private static final String[][] mandatoryProperties = {
        {"hosts", "hostname string (comma separated)"},
        {"port", "port number"},
        {"user", "username string"},
        {"pass", "password string"},
        {"routine", "IRoutine class name string"}
    };

    /**
     * @param property     property name string
     * @throws Exception
     */
    private static void exceptUndefined(String property) throws Exception {
        throw new Exception("Property " + property + " is not defined!");
    }

    /**
     * Builds options from mandatoryProperties and adds config-file and help options 
     * 
     * @return Options
     */
    @NotNull
    public static Options getOptions() {
        Options options = new Options();

        for (String[] property : CLI.mandatoryProperties)
            options.addOption(buildOption(property[0], property[0], property[1]));

        options.addOption(buildOption(
            "config-file",
            "file",
            "file for configs (overridden by args)")
        );

        options.addOption(
        Option.builder("h")
            .longOpt("help")
            .argName("help")
            .desc("Help menu")
            .build()
        );

        options.addOption(
        Option.builder("q")
            .longOpt("quiet")
            .argName("quiet")
            .desc("Reduced output")
            .build()
        );

        return options;
    }

    /**
     * Check if a property exists
     *
     * @param property      property name string
     * @param commandLine   Populated CommandLine class
     * @param properties    Populated Properties class
     * @return boolean
     */
    public static boolean isPropertyDefined(String property, @NotNull CommandLine commandLine, Properties properties) {
        return commandLine.hasOption(property) && !commandLine.getOptionValue(property).isEmpty() || 
            properties.containsKey(property) && !properties.getProperty(property).isEmpty();
    }

    /**
     * Handle help doc request
     * Parse command line arguments 
     * Checks for required arguments
     * Build and return properties hashmap
     *
     * @param arguments     command line arguments
     * @return HashMap<String, String>
     * @throws Exception
     */
    @NotNull
    public static HashMap<String, String> parse(String[] arguments) throws Exception {
        CommandLineParser parser = new DefaultParser();
        Options options = CLI.getOptions();
        CommandLine commandLine = parser.parse(options, arguments);

        if (commandLine.hasOption("help"))
            throw new Exception("");

        Properties properties = new Properties();
        HashMap<String, String> parsedProperties = new HashMap<>();

        if (commandLine.hasOption("q") || commandLine.hasOption("quiet"))
            parsedProperties.put("quiet", "true");

        if (commandLine.hasOption("config-file")) {
            InputStream input = new FileInputStream(commandLine.getOptionValue("config-file"));
            properties.load(input);
        }

        for (String[] property : CLI.mandatoryProperties) {
            if (!CLI.isPropertyDefined(property[0], commandLine, properties))
                CLI.exceptUndefined(property[0]);
            else
                parsedProperties.put(property[0], commandLine.hasOption(property[0]) ? commandLine.getOptionValue(property[0]) : properties.getProperty(property[0]));
        }

        return parsedProperties;
    }

    /**
     * Builds and displays help menu
     *
     * @param options       command line options
     */
    public static void buildHelpMenu(Options options) {
        String jarName = new java.io.File(JMXHelper.class.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath())
            .getName();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar " + jarName + " ...", options);
    }

    /**
     * Helper to build standard options
     *
     * @param option        option name string
     * @param argument      argument name string
     * @param description   option description string
     * @return Option
     */
    @NotNull
    private static Option buildOption(String option, String argument, String description) {
        return Option.builder()
            .longOpt(option)
            .argName(argument)
            .valueSeparator()
            .hasArg()
            .desc(description)
            .build();
    }
}
