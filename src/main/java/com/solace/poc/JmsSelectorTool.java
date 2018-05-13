package com.solace.poc;

import com.solacesystems.jms.SupportedProperty;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Arrays;
import java.util.Hashtable;

import static com.solace.poc.Helper.buildSelectorString;
import static com.solace.poc.Helper.safeSleep;

public class JmsSelectorTool {

    private static void bindTopicAndSelectorToDTE(Session session, String dteName, String topicSub, String selectorString) throws Exception {
        final Topic topic = session.createTopic(topicSub);
        final TopicSubscriber dte = session.createDurableSubscriber(topic, dteName, selectorString, true);

        dte.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) { }
        });
    }

    public static void main(String... args) throws Exception {
        // Check command line arguments
        if (args.length < 9) {
            System.out.println("Usage: JmsSelectorTool <msg_backbone_ip:port> <vpn> <client-username> <client-password> <conn-factory-name> -te <dte-name> <topic> <selector string>");
            System.exit(-1);
        }
        System.out.println("SelectorTool initializing...");

        // The client needs to specify both of the following properties:
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(InitialContext.INITIAL_CONTEXT_FACTORY, "com.solacesystems.jndi.SolJNDIInitialContextFactory");
        env.put(InitialContext.PROVIDER_URL, args[0]);
        env.put(SupportedProperty.SOLACE_JMS_VPN, args[1]);
        env.put(Context.SECURITY_PRINCIPAL, args[2]);
        env.put(Context.SECURITY_CREDENTIALS, args[3]);

        InitialContext context = new InitialContext(env);
        ConnectionFactory cf = (ConnectionFactory) context.lookup(args[4]);
        Connection connection = cf.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        if (args[5].equals("-te")) {
            final String selectorString = buildSelectorString(Arrays.copyOfRange(args, 8, args.length));
            bindTopicAndSelectorToDTE(session, args[6], args[7], selectorString);
        }
        connection.start();
        safeSleep( 1000 );
        connection.stop();

        session.close();
    }
}

