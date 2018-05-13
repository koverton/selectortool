# selectortool
Simple JMS client that adds/removes topics and selectors from JMS durable subscribers.

For more info about JMS and Solace Messaging see the [reference links](#references) below.

## Building via Maven

There is a script to build a 'fat' jarfile containing all dependencies:

`scripts/make-fat-jar.sh`

Provided you have access to internet Maven repositories, this build should 
work without any modifications. If you are not connected to the internet, see
the [pom.xml](./pom.xml) for a list of external dependencies.

## Running in Shell

You can either run the JMS client or native client. There are wrapper scripts for 
each to make it easier to run. The wrapper scripts will also run the build 
script if the required jarfile is not found.

### Running the Native Client

This simply requires that you know the coordinates and credentials to connect 
to your Solace message router.

```bash
koverton$ scripts/run-native.sh
Usage: SelectorTool <msg_backbone_ip:port> <vpn> <client-username> <client-password> [ -q <qname> | -te <dte-name> <topic> ] <selector string>
```

* `msg-backbone-ip:port` : IP and port that messaging clients connect to
* `vpn` : Solace messaging-VPN or virtual broker you wish to connect to
* `client-username` : messaging client identity you wish to connect as
* `client-password` : password for the above messaging client
* `-te <dte-name>` : where dte-name is the name of the durable topic-endpoint being modified
* `<topic>` : topic-subscription string you wish to subscribe this endpoint to
* `<selector string>` : arbitrary number of arguments that expresses a valid selector

NOTE: The `-q <queue-name>` option does not yet work as Solace selectors are only bind-time
attributes that are removed as soon as the client disconnects.

### Running the JMS Client

Running the JMS client is very similar to running the native client except 
that the JMS client requires you specify the name of the JMS Connection-Factory 
you wish to connect to the JMS bus with.

```bash
koverton$ scripts/run-jms.sh
Usage: JmsSelectorTool <msg_backbone_ip:port> <vpn> <client-username> <client-password> <conn-factory-name> -te <dte-name> <topic> <selector string>
```

* `msg-backbone-ip:port` : IP and port that messaging clients connect to
* `vpn` : Solace messaging-VPN or virtual broker you wish to connect to
* `client-username` : messaging client identity you wish to connect as
* `client-password` : password for the above messaging client
* `conn-factory-name` : name of the JMS connection-factory you wish to connect with; _THIS MUST BE PROVISIONED IN THE BROKER JNDI STORE IN ADVANCE_
* `-te <dte-name>` : where dte-name is the name of the durable topic-endpoint being modified
* `<topic>` : topic-subscription string you wish to subscribe this endpoint to
* `<selector string>` : arbitrary number of arguments that expresses a valid selector

NOTE: The `-q <queue-name>` option does not yet work as Solace selectors are only bind-time
attributes that are removed as soon as the client disconnects.

## References

* [Getting Started with Solace Messaging](http://dev.solace.com/get-started/start-up-solace-messaging/)
* [Sending and Receiving Messages via JMS](http://dev.solace.com/samples/solace-samples-jms/)
* [Solace JMS API Reference](https://docs.solace.com/API-Developer-Online-Ref-Documentation/jms/index.html)
* [JMS 1.1 Specification](https://docs.oracle.com/cd/E19957-01/816-5904-10/816-5904-10.pdf)