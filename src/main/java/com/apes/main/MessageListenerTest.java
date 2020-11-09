package com.apes.main;

import com.alien.enterpriseRFID.notify.Message;
import com.alien.enterpriseRFID.notify.MessageListener;
import com.alien.enterpriseRFID.notify.MessageListenerService;
import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.tags.Tag;

import java.net.InetAddress;

public class MessageListenerTest implements MessageListener {

    public MessageListenerTest() throws Exception {

        MessageListenerService service = new MessageListenerService( 4000 );
        service.setMessageListener( this );
        service.startService();
        System.out.println( "Message Listener has Started" );

        // Instantiate a new reader object, and open a connection to it on COM13
        AlienClass1Reader reader = new AlienClass1Reader( "COM13" );
        reader.open();
        System.out.println( "Configuring Reader" );


        String myIP = InetAddress.getLocalHost().getHostAddress();
        reader.setNotifyAddress( myIP, service.getListenerPort() );

        // Make sure service can decode it.
        reader.setNotifyFormat( AlienClass1Reader.XML_FORMAT );

        reader.setNotifyTrigger( "TrueFalse" );
        reader.setNotifyMode( AlienClass1Reader.ON );

        // Set up AutoMode
        reader.autoModeReset();
        reader.setAutoStopTimer(1000);

        reader.setAutoMode(AlienClass1Reader.ON);

        // Close the connection and spin while messages arrive
        reader.close();

        long runTime = 10000; // milliseconds
        long startTime = System.currentTimeMillis();

         do {

            Thread.sleep(1000);
         } while(service.isRunning() && (System.currentTimeMillis()-startTime) < runTime);

         // Reconnect to the reader and turn off AutoMode and TagStreamMode.
         System.out.println("\nResetting Reader");
         reader.open();
         reader.autoModeReset();
         reader.setNotifyMode(AlienClass1Reader.OFF);
         reader.close();
    }

    @Override
    public void messageReceived(Message message) {
        System.out.println("\nMessage Received:");
         if (message.getTagCount() == 0) {
             System.out.println("(No Tags)");
         } else {
             for (int i=0; i<message.getTagCount(); i++) {
                 Tag tag = message.getTag(i);
                 System.out.println(tag.toLongString());
             }
         }
    }

    public static final void main(String args[]){
        try {
            new MessageListenerTest();
        } catch (Exception e) {
            System.out.println("Error:" + e.toString());
        }
    }
}
