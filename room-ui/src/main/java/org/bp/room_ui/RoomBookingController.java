package org.bp.room_ui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.bp.BookRoomRequest;
import org.bp.room.FaultMsg;
//import org.bp.room.RoomBookingEndpointService;
import org.bp.roomservice.RoomBookingEndpointService;

import org.bp.room.RoomPortType;
import org.bp.types.BookingInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class RoomBookingController {

	@GetMapping("/bookRoom")
	public String bookRoomForm(Model model) {
		model.addAttribute("bookRoomRequest", new BookRoomRequest());
		return "bookRoom";
	}

	private static final QName SERVICE_NAME = new QName("http://roomservice.bp.org/", "RoomBookingEndpointService");


	@PostMapping("/bookRoom")
	public String bookRoom(@ModelAttribute BookRoomRequest brf, Model model) {
		URL wsdlURL = RoomBookingEndpointService.WSDL_LOCATION;


		RoomBookingEndpointService ss = new RoomBookingEndpointService(wsdlURL, SERVICE_NAME);
		RoomPortType port = ss.getRoomBookingEndpointPort();

		{
			org.bp.BookRoomRequest _bookRoom_payload = brf;
			try {
				org.bp.types.BookingInfo _bookRoom__return = port.bookRoom(_bookRoom_payload);
				System.out.println("bookRoom.result=" + _bookRoom__return);
				model.addAttribute("bookingInfo", _bookRoom__return);
				return "result";
			} catch (FaultMsg e) {
				System.out.println("Expected exception: faultMsg has occurred.");
				System.out.println(e.toString());
				model.addAttribute("hotelFaultMsg", e);
				return "fault";
			}

		}
	}

}