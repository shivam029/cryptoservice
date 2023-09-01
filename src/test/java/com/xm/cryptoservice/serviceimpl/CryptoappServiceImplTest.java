package com.xm.cryptoservice.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.xm.cryptoservice.model.CryptoResponseDTO;
import com.xm.cryptoservice.model.MinmaxResponseDTO;
import com.xm.cryptoservice.service.CryptoCsvLoadService;
import com.xm.cryptoservice.service.CryptoappService;

@ExtendWith(MockitoExtension.class)
public class CryptoappServiceImplTest {

	@Mock
	CryptoappService service;

	@Mock
	CryptoCsvLoadService cryptoCsvLoadService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void getAllCryptoSortByDescendingTest() throws ParseException {

		List<String> mocklist = new ArrayList<String>();

		CryptoResponseDTO responseDTOallCryptoDesc = new CryptoResponseDTO();

		// Adding the Mock data
		mocklist.add("ETH");
		mocklist.add("XRP");
		mocklist.add("DOGE");
		mocklist.add("LTC");
		mocklist.add("BTC");

		responseDTOallCryptoDesc.setData(mocklist);

		when(service.getAllCryptoSortByDescending(cryptoCsvLoadService.getAllCryptoDetails()))
				.thenReturn(responseDTOallCryptoDesc);

		// Test
		CryptoResponseDTO resList = service.getAllCryptoSortByDescending(cryptoCsvLoadService.getAllCryptoDetails());

		assertEquals(5, resList.getData().size());
		assertThat(service.getAllCryptoSortByDescending(cryptoCsvLoadService.getAllCryptoDetails())).isEqualTo(resList);

	}

	@Test
	public void getRequestedCryptoDataByNameTest() throws ParseException {

		MinmaxResponseDTO resDtoMock = new MinmaxResponseDTO();

		// Adding the mock data

		resDtoMock.setOldest(46813.21);

		String cryptoName = "BTC";

		when(service.getRequestedCryptoDataByName(cryptoName)).thenReturn(resDtoMock);

		// test
		MinmaxResponseDTO resDtoTest = service.getRequestedCryptoDataByName(cryptoName);

		boolean res = resDtoTest.getOldest().equals(46813.21);

		assertEquals(true, res);
		assertThat(service.getRequestedCryptoDataByName(cryptoName)).isEqualTo(resDtoTest);

	}

	@Test
	public void getRequestedCryptoDataByDateTest() throws ParseException {

		String mockRes = "XRP";

		String cryptoDate = "01-31-2022";

		when(service.getRequestedCryptoDataByDate(cryptoDate)).thenReturn(mockRes);

		// test
		String resVal = service.getRequestedCryptoDataByDate(cryptoDate);

		assertEquals("XRP", resVal);
		assertThat(service.getRequestedCryptoDataByDate(cryptoDate)).isEqualTo(resVal);

	}

}
