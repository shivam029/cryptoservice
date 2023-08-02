package com.xm.cryptoservice.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.xm.cryptoservice.service.CryptoappService;


@ExtendWith(MockitoExtension.class)
public class CryptoappServiceImplTest {

    @Mock
    CryptoappService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllCryptoSortByDescendingTest() throws ParseException {

        List<String> mocklist = new ArrayList<String>();
        
        // Adding the Mock data
        mocklist.add( "ETH");
        mocklist.add( "XRP");
        mocklist.add( "DOGE");
        mocklist.add( "LTC");
        mocklist.add( "BTC");
       
       
       when(service.getAllCryptoSortByDescending()).thenReturn(mocklist);

        //test
        List<String> resList = service.getAllCryptoSortByDescending();
        
        assertEquals(5, resList.size());
        assertThat(service.getAllCryptoSortByDescending()).isEqualTo(resList);

 }
    
    @Test
    public void getRequestedCryptoDataByNameTest() throws ParseException {

        Map<String,Double> hmMockdata = new HashMap();
        
        // Adding the mock data
        hmMockdata.put("OLDEST", 46813.21);
        hmMockdata.put("NEWEST", 33225.59);
        hmMockdata.put("MIN", 47722.66);
        hmMockdata.put("MAX", 38415.79);
       
       String cryptoName = "BTC";
       
       when(service.getRequestedCryptoDataByName(cryptoName)).thenReturn(hmMockdata);

        //test
       Map<String,Double> resList = service.getRequestedCryptoDataByName(cryptoName);
        
        assertEquals(4, resList.size());
        assertThat(service.getRequestedCryptoDataByName(cryptoName)).isEqualTo(resList);

 }
    
    @Test
    public void getRequestedCryptoDataByDateTest() throws ParseException {

        String mockRes = "XRP";
        
              
       String cryptoDate = "01-31-2022";
       
       when(service.getRequestedCryptoDataByDate(cryptoDate)).thenReturn(mockRes);

        //test
       String resVal = service.getRequestedCryptoDataByDate(cryptoDate);
        
        assertEquals("XRP", resVal);
        assertThat(service.getRequestedCryptoDataByDate(cryptoDate)).isEqualTo(resVal);

 }
    
}
