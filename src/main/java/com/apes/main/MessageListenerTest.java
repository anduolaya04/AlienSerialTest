package com.apes.main;

import com.alien.enterpriseRFID.notify.Message;
import com.alien.enterpriseRFID.notify.MessageListener;
import com.alien.enterpriseRFID.notify.MessageListenerServiceSerial;
import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.tags.Tag;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class MessageListenerTest implements MessageListener {

    public MessageListenerTest() throws Exception {

        SimpleDateFormat sdf;
        Locale loc;
        // Instantiate a new reader object, and open a connection to it on COM13
        loc = new Locale("EN", "CO");
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", loc);
        sdf.setTimeZone( TimeZone.getTimeZone("America/Bogota"));
        AlienClass1Reader reader = new AlienClass1Reader();
        reader.setConnection( "COM13" );
        reader.open();
        reader.autoModeReset();
        reader.close();

        reader.open();
        System.out.println( "Configuring Reader" );
        //General commands
      //  reader.autoModeReset();
      /*  reader.setNotifyMode( AlienClass1Reader.OFF );*/
        reader.setAntennaSequence( "0" );
        /*reader.setReaderBaudRate( 115200 );
        reader.setReaderName("SerialAlien");
        */
        reader.setTime(sdf.format( Calendar.getInstance().getTime()));
        reader.setRFAttenuation( 0,0 );

        //Tag List Format
        /*
        reader.setTagListFormat( "Custom" );
        reader.setTagListCustomFormat( "Tag:${TAGIDW}, USERMEM:${G2DATA1}, Disc:${DATE1} ${TIME1} ${MSEC1}, Last:${DATE2} ${TIME2} ${MSEC2}, Ant:${RX}, Dir:${DIR}" );
        reader.setTagListMillis(1);*/
        reader.setPersistTime( 0 );/*
        reader.setTagListAntennaCombine(1);
        reader.setTagStreamMode(0);
        reader.setTagStreamFormat("Custom");
        reader.setTagStreamCustomFormat("Tag:${TAGIDW}, USERMEM:${G2DATA1}, Disc:${DATE1} ${TIME1} ${MSEC1}, Last:${DATE2} ${TIME2} ${MSEC2}, Ant:${RX}, Dir:${DIR}");
        reader.setTagStreamKeepAliveTime(30);
        reader.setTagStreamCountFilter(0);
        reader.setStreamHeader(0);
        */
//--------------------------------------------------------------------------------------
        //acquire BASIC COMMANDS
       /* reader.setAcquireMode("Inventory");
        reader.setTagType(16);
        reader.setAcquireG2Cycles(1);
        reader.setAcquireG2Count(2);
        reader.setAcquireG2Q(6);
        reader.setAcquireG2Selects(1);
        reader.setAcquireG2Session(2);
        reader.setAcqG2Mask( "All" );
        reader.setAcqG2MaskAction( "Include" );
        reader.setAcqG2MaskAntenna( "0F" );
        reader.setAcqG2SL( "All" );
        reader.setAcqG2AccessPwd( "1E C7 04 93" );
        reader.setAcquireG2TagData( "3 0 4" );
        reader.setAcquireG2OpsMode(0);
        reader.setAcquireG2Target( "A" );
        reader.setTagListAntennaCombine( 1 );
        reader.setAcquireTime(0);
        reader.setSpeedFilter( "0" );
        reader.setRSSIFilter( "0" );
        reader.setTagAuth("OFF");*/

        //IO Commands
     /*   reader.setExternalOutput( 0 );
        reader.setInvertExternalInput( 0 );
        reader.setInvertExternalOutput( 0 );
        reader.setIOType( "DI" );
        reader.setIOListFormat( "Text" );
        reader.setIOListCustomFormat( "The new %E value is %v." );
        reader.setIOStreamMode( 0 );
        reader.setIOStreamKeepAliveTime( 30 );
        reader.setIOStreamFormat( "Custom" );
        reader.setIOStreamCustomFormat( "The new %E value is %v." );

        //Automode Commands
        reader.setAutoWaitOutput(0);
        */
        reader.setAutoStartTrigger( "0 0" );
        /*reader.setAutoStartPause(0);
        reader.setAutoWorkOutput(0);*/
        reader.setAutoStopTrigger( "0 0" );
        //reader.setAutoStopTimer(2000);
        /*
        reader.setAutoStopPause(0);
        reader.setAutoTrueOutput(0);
        reader.setAutoFalseOutput(0);
        reader.setAutoTruePause(0);
        reader.setAutoFalsePause(0);
        reader.setAutoErrorOutput("-1");
        reader.setAutoProgError(255);

        //Notify commands
        reader.setNotifyMode(0);
        reader.setNotifyAddress("SERIAL");
        reader.setNotifyFormat( "Custom" );
        reader.setNotifyHeader(1);
        reader.setNotifyTime(0);
        reader.setNotifyTrigger( "TrueFalse" );
        reader.setNotifyKeepAliveTime(30);
        reader.setNotifyRetryCount(-1);
        reader.setNotifyRetryPause(10);
        reader.setNotifyQueueLimit(1000);
        reader.setNotifyInclude("Tags");*/
      //  reader.close();
        //reader.saveSettings();

        //reader.open();
        reader.setNotifyAddress( "SERIAL");

        // Make sure service can decode it.
        //reader.setNotifyFormat( AlienClass1Reader.XML_FORMAT );

        reader.setNotifyTrigger( "TrueFalse" );
        reader.setNotifyMode( AlienClass1Reader.ON );

        // Set up AutoMode
        reader.autoModeReset();
        reader.setAutoStopTimer(1000);

        reader.setAutoMode(AlienClass1Reader.ON);

        //reader.saveSettings();
        // Close the connection and spin while messages arrive
        reader.close();

        long runTime = 10000; // milliseconds
        long startTime = System.currentTimeMillis();

        MessageListenerServiceSerial service = new MessageListenerServiceSerial();
        service.setMessageListener( this );
        service.setPORT( "COM13" );
        service.startService();
        System.out.println( "Message Listener has Started" );

         do {

            Thread.sleep(1000);

         } while(service.isRunning() && (System.currentTimeMillis()-startTime) < runTime);

         service.stopService();

         //Thread.sleep( 1000 );

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
