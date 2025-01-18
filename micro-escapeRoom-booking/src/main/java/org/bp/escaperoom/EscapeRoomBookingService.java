package org.bp.escaperoom;

import static org.apache.camel.model.rest.RestParamType.body;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.bp.escaperoom.exceptions.RoomServiceException;
import org.bp.escaperoom.exceptions.RoomException;
import org.bp.escaperoom.model.BookEscapeRoomRequest;
import org.bp.escaperoom.model.BookingInfo;
import org.bp.escaperoom.model.ExceptionResponse;
import org.bp.escaperoom.model.Utils;
import org.bp.escaperoom.state.ProcessingEvent;
import org.bp.escaperoom.state.ProcessingState;
import org.bp.escaperoom.state.StateService;
import org.springframework.stereotype.Component;


@Component
public class EscapeRoomBookingService extends RouteBuilder {
	
	@org.springframework.beans.factory.annotation.Autowired
	BookingIdentifierService bookingIdentifierService;
	
	@org.springframework.beans.factory.annotation.Autowired
	PaymentService paymentService;
//
	@org.springframework.beans.factory.annotation.Autowired
	StateService roomServiceStateService;

	@org.springframework.beans.factory.annotation.Autowired
	StateService roomStateService;

	@Override
	public void configure() throws Exception {
//		bookRoomExceptionHandlers();
//		bookRoomServiceExceptionHandlers();
		gateway();
		room();
		roomService();
		payment();
	}
	
	private void bookRoomExceptionHandlers() {
		onException(RoomException.class)
		.process((exchange) -> {
					ExceptionResponse er = new ExceptionResponse();
					er.setTimestamp(OffsetDateTime.now());
					Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
					er.setMessage(cause.getMessage());
					exchange.getMessage().setBody(er);
				}
				)
        .marshal().json()
		.to("stream:out")
		.setHeader("serviceType", constant("room"))
		.to("kafka:EscapeRoomBookingFailTopic?brokers=localhost:9092")
		.handled(true)
		;	
	}	

	private void bookRoomServiceExceptionHandlers() {
		onException(RoomServiceException.class)
		.process((exchange) -> {
					ExceptionResponse er = new ExceptionResponse();
					er.setTimestamp(OffsetDateTime.now());
					Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
					er.setMessage(cause.getMessage());
					exchange.getMessage().setBody(er);
				}
				)
	    .marshal().json()
		.to("stream:out")
		.setHeader("serviceType", constant("roomService"))
		.to("kafka:EscapeRoomBookingFailTopic?brokers=localhost:9092")
		.handled(true)
		;	
	}
	
	private void gateway() {
        restConfiguration()
        .component("servlet")
        .bindingMode(RestBindingMode.json)
        .dataFormatProperty("prettyPrint", "true")
        .enableCORS(true)
        .contextPath("/api")
        // turn on swagger api-doc
        .apiContextPath("/api-doc")
        .apiProperty("api.title", "Micro EscapeRoom booking API")
        .apiProperty("api.version", "1.0.0");
        
		rest("/escaperoom").description("Micro EscapeRoom booking REST service")
		.consumes("application/json")
		.produces("application/json")        
		.post("/booking").description("Book a escaperoom").type(BookEscapeRoomRequest.class).outType(BookingInfo.class)
		.param().name("body").type(body).description("The escaperoom to book").endParam()
		.responseMessage().code(200).message("EscapeRoom successfully booked").endResponseMessage()
		.to("direct:bookEscapeRoom")
		;
		
		from("direct:bookEscapeRoom").routeId("bookEscapeRoom")
		.log("bookEscapeRoom fired")
		.process((exchange) -> {
				exchange.getMessage().setHeader("bookingEscapeRoomId", bookingIdentifierService.getBookingIdentifier());
		})
		.to("direct:EscapeRoomBookRequest")
		.to("direct:bookRequester");

		from("direct:bookRequester").routeId("bookRequester")
		.log("bookRequester fired")
		.process(
			(exchange) -> {
				exchange.getMessage().setBody(Utils.prepareBookingInfo(
					exchange.getMessage().getHeader("bookingEscapeRoomId", String.class), null));
				}
				)
		;

		from("direct:EscapeRoomBookRequest").routeId("EscapeRoomBookRequest")
		.log("brokerTopic fired")
		.marshal().json()
		.to("kafka:EscapeRoomReqTopic?brokers=localhost:9092")
		;

	}

	private void room() {
		from("kafka:EscapeRoomReqTopic?brokers=localhost:9092").routeId("bookRoom")
		.log("fired bookRoom")
		.unmarshal().json(JsonLibrary.Jackson, BookEscapeRoomRequest.class)
		.process(
				(exchange) -> {
					String bookingEscapeRoomId = exchange.getMessage().getHeader("bookingEscapeRoomId", String.class);
					ProcessingState previousState = roomStateService.sendEvent(bookingEscapeRoomId, ProcessingEvent.START);
					if (previousState!=ProcessingState.CANCELLED) {
		    			BookingInfo bi = new BookingInfo();
		    			bi.setId(bookingIdentifierService.getBookingIdentifier());

		    			BookEscapeRoomRequest btr= exchange.getMessage().getBody(BookEscapeRoomRequest.class);
		    			if (btr!=null && btr.getRoom()!=null
		    					&& btr.getRoom().getDifficulty()!=null ) {
		    				String difficulty = btr.getRoom().getDifficulty();
		    				if (difficulty.equals("hard")) {
		    					bi.setCost(new BigDecimal(999));
		    				}
		    				else if (difficulty.equals("medium")){
		    					throw new RoomException("Not room available for "+difficulty);
		    				}
		    				else {
		    					bi.setCost(new BigDecimal(888));
		    				}
		    			}
		    			exchange.getMessage().setBody(bi);
		    			previousState = roomStateService.sendEvent(bookingEscapeRoomId, ProcessingEvent.FINISH);
		    			}
					exchange.getMessage().setHeader("previousState", previousState);        		}
				)
		.marshal().json()
		.to("stream:out")
//		.choice()
//			.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
//			.to("direct:bookRoomCompensationAction")
//		.otherwise()
			.setHeader("serviceType", constant("room"))
			.to("kafka:BookingInfoTopic?brokers=localhost:9092")
//		.endChoice()
		;

//		from("kafka:EscapeRoomBookingFailTopic?brokers=localhost:9092").routeId("bookRoomCompensation")
//		.log("fired bookRoomCompensation")
//		.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class)
//		.choice()
//			.when(header("serviceType").isNotEqualTo("room"))
//		    .process((exchange) -> {
//				String bookingEscapeRoomId = exchange.getMessage().getHeader("bookingEscapeRoomId", String.class);
//				ProcessingState previousState = roomStateService.sendEvent(bookingEscapeRoomId, ProcessingEvent.CANCEL);
//				exchange.getMessage().setHeader("previousState", previousState);
//		    })
//		    .choice()
//		    	.when(header("previousState").isEqualTo(ProcessingState.FINISHED))
//				.to("direct:bookRoomCompensationAction")
//			.endChoice()
//		 .endChoice();
//
//		from("direct:bookRoomCompensationAction").routeId("bookRoomCompensationAction")
//		.log("fired bookRoomCompensationAction")
//		.to("stream:out");
	}
//
	private void roomService() {
		from("kafka:EscapeRoomReqTopic?brokers=localhost:9092").routeId("bookRoomService")
		.log("fired bookRoomService")
		.unmarshal().json(JsonLibrary.Jackson, BookEscapeRoomRequest.class)
		.process(
				(exchange) -> {
//					String bookingEscapeRoomId = exchange.getMessage().getHeader("bookingEscapeRoomId", String.class);
//					ProcessingState previousState = roomServiceStateService.sendEvent(bookingEscapeRoomId, ProcessingEvent.START);
//					if (previousState!=ProcessingState.CANCELLED) {
		    			BookingInfo bi = new BookingInfo();
		    			bi.setId(bookingIdentifierService.getBookingIdentifier());
		    			BookEscapeRoomRequest btr= exchange.getMessage().getBody(BookEscapeRoomRequest.class);
		    			if (btr!=null && btr.getRoomService()!=null && btr.getRoomService().getTypeOfService()!=null
		    					&& btr.getRoomService().getTypeOfService()!=null) {
		    				String typeOfService=btr.getRoomService().getTypeOfService();
		    				if (typeOfService.equals("Alcohol")) {
		    					throw new RoomServiceException("Not serviced during game: "+typeOfService);
		    				}
		    				else if (typeOfService.equals("Drinks")) {
		    					bi.setCost(new BigDecimal(700));
		    				}
		    				else {
		    					bi.setCost(new BigDecimal(500));
		    				}
		    			}

		    			exchange.getMessage().setBody(bi);
//		    			previousState = roomServiceStateService.sendEvent(bookingEscapeRoomId, ProcessingEvent.FINISH);
//		    			}
//					exchange.getMessage().setHeader("previousState", previousState);
				}
				)
		.marshal().json()
		.to("stream:out")
//		.choice()
//			.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
//			.to("direct:bookRoomServiceCompensationAction")
//		.otherwise()
			.setHeader("serviceType", constant("roomService"))
			.to("kafka:BookingInfoTopic?brokers=localhost:9092")
//		.endChoice()
		;

		from("kafka:EscapeRoomBookingFailTopic?brokers=localhost:9092").routeId("bookRoomServiceCompensation")
		.log("fired bookRoomServiceCompensation")
		.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class)
        .choice()
    		.when(header("serviceType").isNotEqualTo("roomService"))
            .process((exchange) -> {
    			String bookingEscapeRoomId = exchange.getMessage().getHeader("bookingEscapeRoomId", String.class);
    			ProcessingState previousState = roomServiceStateService.sendEvent(bookingEscapeRoomId, ProcessingEvent.CANCEL);
    			exchange.getMessage().setHeader("previousState", previousState);
            })
            .choice()
            	.when(header("previousState").isEqualTo(ProcessingState.FINISHED))
    			.to("direct:bookRoomServiceCompensationAction")
    		.endChoice()
         .endChoice();

		from("direct:bookRoomServiceCompensationAction").routeId("bookRoomServiceCompensationAction")
		.log("fired bookRoomServiceCompensationAction")
		.to("stream:out");
	}

	private void payment() {
		from("kafka:BookingInfoTopic?brokers=localhost:9092").routeId("paymentBookingInfo")
		.log("fired paymentBookingInfo")
		.unmarshal().json(JsonLibrary.Jackson, BookingInfo.class)
		.process(
				(exchange) -> {
					String bookingEscapeRoomId = exchange.getMessage().getHeader("bookingEscapeRoomId", String.class);
					boolean isReady= paymentService.addBookingInfo(
							bookingEscapeRoomId,
							exchange.getMessage().getBody(BookingInfo.class),
							exchange.getMessage().getHeader("serviceType", String.class));
					exchange.getMessage().setHeader("isReady", isReady);
				}
				)
		.choice()
			.when(header("isReady").isEqualTo(true)).to("direct:finalizePayment")
		    .endChoice();

		from("kafka:EscapeRoomReqTopic?brokers=localhost:9092").routeId("paymentEscapeRoomReq")
		.log("fired paymentEscapeRoomReq")
		.unmarshal().json(JsonLibrary.Jackson, BookEscapeRoomRequest.class)
		.process(
				(exchange) -> {
					String bookingEscapeRoomId = exchange.getMessage().getHeader("bookingEscapeRoomId", String.class);
					 boolean isReady= paymentService.addBookEscapeRoomRequest(
							bookingEscapeRoomId,
							exchange.getMessage().getBody(BookEscapeRoomRequest.class));
					 exchange.getMessage().setHeader("isReady", isReady);
				}
				)
		.choice()
			.when(header("isReady").isEqualTo(true)).to("direct:finalizePayment")
		.endChoice();
//
	from("direct:finalizePayment").routeId("finalizePayment")
	.log("fired finalizePayment")
	.process(
			(exchange) -> {
				String bookingEscapeRoomId = exchange.getMessage().getHeader("bookingEscapeRoomId", String.class);
				 PaymentService.PaymentData paymentData = paymentService.getPaymentData(bookingEscapeRoomId);
				 BigDecimal roomCost=paymentData.roomBookingInfo.getCost();
				 BigDecimal roomServiceCost=paymentData.roommServiceBookingInfo.getCost();
				 BigDecimal totalCost=roomCost.add(roomServiceCost);
				 BookingInfo escapeRoomBookingInfo = new BookingInfo();
				 escapeRoomBookingInfo.setId(bookingEscapeRoomId);
				 escapeRoomBookingInfo.setCost(totalCost);
				 exchange.getMessage().setBody(escapeRoomBookingInfo);
			}
			)
	.to("direct:notification")
	;
//
	from("direct:notification").routeId("notification")
	.log("fired notification")
	.to("stream:out");
//
		}


}
