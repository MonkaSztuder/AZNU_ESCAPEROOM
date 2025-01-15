package org.bp.escaperoom;


import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.bp.escaperoom.model.BookEscapeRoomRequest;
import org.bp.escaperoom.model.BookingInfo;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	private HashMap<String, PaymentData> payments;
	
	@PostConstruct
	void init() {
		payments=new HashMap<>();
	}
	
	public static class PaymentData {
		BookEscapeRoomRequest bookEscapeRoomRequest;
		BookingInfo roomBookingInfo;
		BookingInfo roommServiceBookingInfo;
		public boolean isReady() {
			return bookEscapeRoomRequest !=null && roomBookingInfo !=null && roommServiceBookingInfo !=null;
		}
	}
	
	public synchronized boolean addBookEscapeRoomRequest(String bookEscapeRoomId, BookEscapeRoomRequest bookEscapeRoomRequest) {
		PaymentData paymentData = getPaymentData(bookEscapeRoomId);
		paymentData.bookEscapeRoomRequest = bookEscapeRoomRequest;
		return paymentData.isReady();
	}
	

	public synchronized boolean addBookingInfo(String bookEscapeRoomId, BookingInfo bookingInfo, String serviceType) {
		PaymentData paymentData = getPaymentData(bookEscapeRoomId);
		if (serviceType.equals("roomService"))
			paymentData.roommServiceBookingInfo =bookingInfo;
		else 
			paymentData.roomBookingInfo =bookingInfo;
		return paymentData.isReady();
	}	
	
	
	public synchronized PaymentData getPaymentData(String bookEscapeRoomId) {
		PaymentData paymentData = payments.get(bookEscapeRoomId);
		if (paymentData==null) {
			paymentData = new PaymentData();
			payments.put(bookEscapeRoomId, paymentData);
		}
		return paymentData;
	}

	


	

}
