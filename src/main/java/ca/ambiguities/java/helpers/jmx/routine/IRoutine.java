package ca.ambiguities.java.helpers.jmx.routine;

import javax.management.MBeanServerConnection;

public interface IRoutine {
    public abstract void run(MBeanServerConnection mbsc, boolean quiet);
}
