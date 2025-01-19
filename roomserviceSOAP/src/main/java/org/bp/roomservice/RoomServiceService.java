package org.bp.roomservice;

import org.bp.types.BookingInfo;
import org.bp.types.Fault;
import org.bp.types.RoomService;
import org.bp.types.RoomServiceTicket;

@javax.jws.WebService
@org.springframework.stereotype.Service
public class RoomServiceService {

	public BookingInfo bookRoomService(RoomServiceTicket roomServiceTicket) throws Fault {
		if (roomServiceTicket != null) {
			RoomService roomService = roomServiceTicket.getRoomService();
			if (roomService != null && roomService.getTypeOfService() != null
					&& roomService.getLevelOfService() != null) {
				int levelOfService = roomService.getLevelOfService();
				if (levelOfService < 0 || levelOfService > 3) {
					Fault fault = new Fault();
					fault.setCode(5);
					fault.setText("Our Service level is between 0 and 3");
					throw fault;
				}
			}
		}
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setId(1);
		bookingInfo.setCost(new java.math.BigDecimal(345));
		return bookingInfo;
	}

	public BookingInfo cancelBooking(int bookingId) throws Fault {
		return null;
	}
}