package com.solace.poc;

import com.solacesystems.jcsmp.*;
import java.util.Arrays;

import static com.solace.poc.Helper.buildSelectorString;
import static com.solace.poc.Helper.safeSleep;

public class SelectorTool {


    private static void bindSelectorToQueue(JCSMPSession session, String queueName, String selectorString) throws JCSMPException {
        final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);
        final ConsumerFlowProperties flowProps = new ConsumerFlowProperties();
        flowProps.setEndpoint(queue);
        flowProps.setSelector(selectorString);
        flowProps.setStartState(false);

        System.out.println("Binding to endpoint (Queue): " + queue);
        System.out.println("Setting Selector: " + selectorString);

        final Consumer cons = session.createFlow(
                new XMLMessageListener() {
                    @Override
                    public void onReceive(BytesXMLMessage bytesXMLMessage) { }
                    @Override
                    public void onException(JCSMPException e) { }
                }, flowProps);
        System.out.println("Connected!");

        cons.start();
        safeSleep( 500 );
        cons.close();
    }

    private static void bindTopicAndSelectorToDTE(JCSMPSession session, String dteName, String topicSub, String selectorString) throws JCSMPException {
        final DurableTopicEndpoint dte = JCSMPFactory.onlyInstance().createDurableTopicEndpoint(dteName);
        final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicSub);

        final ConsumerFlowProperties flowProps = new ConsumerFlowProperties();
        flowProps.setEndpoint(dte);
        flowProps.setSelector(selectorString);
        flowProps.setNewSubscription(topic);
        flowProps.setStartState(false);

        System.out.println("Binding to endpoint (DTE): " + dte);
        System.out.println("Setting Topic: " + topic);
        System.out.println("Setting Selector: " + selectorString);

        final Consumer cons = session.createFlow(
                new XMLMessageListener() {
                    @Override
                    public void onReceive(BytesXMLMessage bytesXMLMessage) { }
                    @Override
                    public void onException(JCSMPException e) { }
                }, flowProps);

        cons.start();
        safeSleep( 500 );
        cons.close();
    }

    public static void main(String... args) throws JCSMPException {
        // Check command line arguments
        if (args.length < 7) {
            System.out.println("Usage: SelectorTool <msg_backbone_ip:port> <vpn> <client-username> <client-password> [ -q <qname> | -te <dte-name> <topic> ] <selector string>");
            System.exit(-1);
        }
        System.out.println("SelectorTool initializing...");

        // Create a JCSMP Session
        final JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, args[0]);      // msg-backbone ip:port
        properties.setProperty(JCSMPProperties.VPN_NAME, args[1]);  // message-vpn
        properties.setProperty(JCSMPProperties.USERNAME, args[2]);  // client-username (assumes no password)
        properties.setProperty(JCSMPProperties.PASSWORD, args[3]);  // client-username (assumes no password)
        final JCSMPSession session =  JCSMPFactory.onlyInstance().createSession(properties);
        session.connect();

        if (args[4].equals("-te")) {
            final String selectorString = buildSelectorString( Arrays.copyOfRange(args,7, args.length) );
            bindTopicAndSelectorToDTE( session, args[5], args[6], selectorString );
        }
        else {
            final String selectorString = buildSelectorString( Arrays.copyOfRange(args,6, args.length) );
            bindSelectorToQueue( session, args[5], selectorString );
        }

        session.closeSession();
    }
}

