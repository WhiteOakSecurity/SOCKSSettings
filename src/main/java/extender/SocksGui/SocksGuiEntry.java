/*
 * Copyright (c) 2022-2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package extender.SocksGui;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

//Burp will auto-detect and load any class that extends BurpExtension.
public class SocksGuiEntry implements BurpExtension
{
    public String socksHost = "";
    public String socksPort = "";
    public String useDNS = "";
    public String useProxy = "";
    private MontoyaApi api;
    
    @Override
    public void initialize(MontoyaApi api)
    {
        // set extension name
        api.extension().setName("SOCKS Settings");
        Logging logging = api.logging();
        

        // write a message to our output stream
        //logging.logToOutput("Hello output.");
        logging.logToOutput("Retrieving JSON configuration.");
        
        getSocksSettings(api);
        logging.logToOutput("SOCKS Proxy in Use: " + useProxy);
        logging.logToOutput("Current host: "+ socksHost);
        logging.logToOutput("Current port: "+ socksPort);
        logging.logToOutput("Current DNS use: " + String.valueOf(useDNS));
        //setSocksSettings(api);
        // write a message to our error stream
        //logging.logToError("Hello error.");

        // write a message to the Burp alerts tab
        //logging.raiseInfoEvent("Hello info event.");
        //logging.raiseDebugEvent("Hello debug event.");
        //logging.raiseErrorEvent("Hello error event.");
        //logging.raiseCriticalEvent("Hello critical event.");

        // throw an exception that will appear in our error stream
        //throw new RuntimeException("Hello exception.");
        
        
        //create tab for our extension
        socksGui socksGUI = new socksGui(api);
        api.userInterface().registerSuiteTab("SOCKS Settings", socksGUI);
        
    }
    public void getSocksSettings(MontoyaApi api){
        //String substitution is easier than importing additional libraries.
        String config = api.burpSuite().exportUserOptionsAsJson("user_options.connections.socks_proxy");

        socksHost = api.burpSuite().exportUserOptionsAsJson("user_options.connections.socks_proxy.host");
        String fixedSocksHost = socksHost.replaceAll("(?s).*\"host\":\"","");
        fixedSocksHost = fixedSocksHost.replaceAll("(?s)\".*","");
        socksHost = fixedSocksHost;

        socksPort = api.burpSuite().exportUserOptionsAsJson("user_options.connections.socks_proxy.port");
        String fixedSocksPort = socksPort.replaceAll("(?s).*\"port\":","");
        fixedSocksPort = fixedSocksPort.replaceAll("(?s)\n.*","");
        socksPort = fixedSocksPort;
        
        useDNS = api.burpSuite().exportUserOptionsAsJson("user_options.connections.socks_proxy.dns_over_socks");
        String fixedUseDNS = useDNS.replaceAll("(?s).*dns_over_socks\":", "");
        fixedUseDNS = fixedUseDNS.replaceAll("(?s)\n.*","");
        useDNS = fixedUseDNS;
        
        useProxy = api.burpSuite().exportUserOptionsAsJson("user_options.connections.socks_proxy.use_proxy");
        String fixedUseProxy = useProxy.replaceAll("(?s).*use_proxy\":", "");
        fixedUseProxy = fixedUseProxy.replaceAll("(?s)\n.*","");
        useProxy = fixedUseProxy;
        //return socksHost, socksPort, useDNS, useProxy;
        
    }
    
    public void setSocksSettings(MontoyaApi api){
      
        //Reconstruct each variable back into its previous format. 
        String socksHostPrefix = "{\"user_options\":{\"connections\":{\"socks_proxy\":{\"host\":\"";
        String socksHostSuffix = "\"}}}}";
        String reconstitutedSocksHost = socksHostPrefix + socksHost + socksHostSuffix;
        
        
        String socksPortPrefix = "{\"user_options\":{\"connections\":{\"socks_proxy\":{\"port\":";
        String socksPortSuffix = "}}}}";
        String reconstitutedSocksPort = socksPortPrefix + socksPort + socksPortSuffix;
        
        
        String useProxyPrefix = "{\"user_options\":{\"connections\":{\"socks_proxy\":{\"use_proxy\":";
        String useProxySuffix = "}}}}";
        String reconstitutedUseProxy = useProxyPrefix + useProxy + useProxySuffix;
        
        
        String useDNSPrefix = "{\"user_options\":{\"connections\":{\"socks_proxy\":{\"dns_over_socks\":";
        String useDNSSuffix = "}}}}";
        String reconstitutedUseDNS = useDNSPrefix + useDNS + useDNSSuffix;
        
        api.burpSuite().importUserOptionsFromJson(reconstitutedSocksHost);
        api.burpSuite().importUserOptionsFromJson(reconstitutedSocksPort);
        api.burpSuite().importUserOptionsFromJson(reconstitutedUseDNS);
        api.burpSuite().importUserOptionsFromJson(reconstitutedUseProxy);
        
        //Logging logging = api.logging();
        //logging.logToOutput(reconstitutedSocksHost);
        //logging.logToOutput(reconstitutedSocksPort);
        //logging.logToOutput(reconstitutedUseProxy);
        //logging.logToOutput(reconstitutedUseDNS);
    }
    

}
