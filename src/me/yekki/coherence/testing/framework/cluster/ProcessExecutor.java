package me.yekki.coherence.testing.framework.cluster;

import java.util.ArrayList;
import java.util.Properties;

public class ProcessExecutor {
    public static int COHERERENCE_PROCESS_MEMORY = 128;
    private final ArrayList<Process> runningProcesses = new ArrayList<Process>();
    private Properties defaultProperties;
    int countOfStartedNodes = 0;

    public ProcessExecutor(Properties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    private String convertToMinusD(Properties props) {
        StringBuilder sb = new StringBuilder();
        props.forEach((k,v)->{
            sb.append(String.format("-D%s=%s ", k, v));
        });

        return sb.toString();
    }

    private String classpath() {

        String sep = System.getProperty("path.separator");
        String[] split = System.getProperty("java.class.path").split(" ");

        String found = "";

        for (String s : split) {
            if (s.contains("coherence")) { //hack to identify the actual entry for the classpath
                found = s;
            }
        }
        return String.format("-cp classes%slib/coherence-utils.jar%sconfig%s%s ", sep, sep, sep, found);
    }

    private void checkForSuccesfulStart(Process process) throws InterruptedException {
        try {
            process.exitValue();
            throw new RuntimeException("Coherence pro  cess failed to start!!");
        } catch (Exception hopedFor) {
        }
        runningProcesses.add(process);
        Thread.sleep(5000);
    }

    public static void main(String[] args) {

    }
}
