package org.bp.roomservice;

import org.bp.BookRoomRequest;
import org.bp.room.FaultMsg;
import org.bp.room.RoomPortType;
import org.bp.types.BookingInfo;
import org.springframework.stereotype.Service;


@Service
public class RoomBookingEndpoint implements RoomPortType {

    @Override
    public BookingInfo bookRoom(BookRoomRequest payload) throws FaultMsg {
        if (payload != null && payload.getRoom() != null
                && "Haunted Mansion".equals(payload.getRoom().getName())) {
            org.bp.types.Fault roomIsNotAvailable = new org.bp.types.Fault();
            roomIsNotAvailable.setCode(20);
            roomIsNotAvailable.setText("Haunted Mansion is temporarily closed for maintenance");
            FaultMsg fault = new FaultMsg("Room is not available ", roomIsNotAvailable);
            throw fault;
        }
        BookingInfo response = new BookingInfo();
        response.setId(1);
        response.setCost(new java.math.BigDecimal(199));
        return response;
    }

    @Override
    public BookingInfo cancelBooking(org.bp.CancelBookingRequest payload) throws FaultMsg {
        BookingInfo response = new BookingInfo();
        response.setId(1);
        response.setCost(new java.math.BigDecimal(199));
        return response;
    }
}