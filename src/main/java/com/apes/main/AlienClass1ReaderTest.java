package com.apes.main;

import com.alien.enterpriseRFID.reader.*;
import com.alien.enterpriseRFID.tags.*;

public class AlienClass1ReaderTest {

    public AlienClass1ReaderTest() throws AlienReaderException {
        AlienClass1Reader reader = new AlienClass1Reader();
        reader.setConnection( "COM13" );

        reader.open();

        Tag[] tagList = reader.getTagList();
        if (tagList == null) {
            System.out.println( "No Tags Found" );
        } else {
            System.out.println( "Tag(s) found:" );
            for (int i = 0; i < tagList.length; i++) {
                Tag tag = tagList[i];
                System.out.println( "ID:" + tag.getTagID() +
                        ", Discovered:" + tag.getDiscoverTime() +
                        ", Last Seen:" + tag.getRenewTime() +
                        ", Antenna:" + tag.getAntenna() +
                        ", Reads:" + tag.getRenewCount()
                );
            }
        }
        reader.close();
    }


    public static final void main(String args[]) {
        try {
            new AlienClass1ReaderTest();
        } catch (AlienReaderException e) {
            System.out.println("Error: " + e.toString());
        }
    }

}