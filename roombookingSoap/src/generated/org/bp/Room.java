
package org.bp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.bp.types.BookingInfo;
import org.bp.types.PaymentCard;
import org.bp.types.Person;
import org.bp.types.RoomService;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="room" type="{http://www.bp.org/types}Room"/&gt;
 *         &lt;element name="person" type="{http://www.bp.org/types}Person"/&gt;
 *         &lt;element name="paymentCard" type="{http://www.bp.org/types}PaymentCard"/&gt;
 *         &lt;element name="roomService" type="{http://www.bp.org/types}RoomService"/&gt;
 *         &lt;element name="bookingInfo" type="{http://www.bp.org/types}BookingInfo"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "room",
    "person",
    "paymentCard",
    "roomService",
    "bookingInfo"
})
@XmlRootElement(name = "room")
public class Room {

    @XmlElement(required = true)
    protected org.bp.types.Room room;
    @XmlElement(required = true)
    protected Person person;
    @XmlElement(required = true)
    protected PaymentCard paymentCard;
    @XmlElement(required = true)
    protected RoomService roomService;
    @XmlElement(required = true)
    protected BookingInfo bookingInfo;

    /**
     * Gets the value of the room property.
     * 
     * @return
     *     possible object is
     *     {@link org.bp.types.Room }
     *     
     */
    public org.bp.types.Room getRoom() {
        return room;
    }

    /**
     * Sets the value of the room property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.bp.types.Room }
     *     
     */
    public void setRoom(org.bp.types.Room value) {
        this.room = value;
    }

    /**
     * Gets the value of the person property.
     * 
     * @return
     *     possible object is
     *     {@link Person }
     *     
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets the value of the person property.
     * 
     * @param value
     *     allowed object is
     *     {@link Person }
     *     
     */
    public void setPerson(Person value) {
        this.person = value;
    }

    /**
     * Gets the value of the paymentCard property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentCard }
     *     
     */
    public PaymentCard getPaymentCard() {
        return paymentCard;
    }

    /**
     * Sets the value of the paymentCard property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentCard }
     *     
     */
    public void setPaymentCard(PaymentCard value) {
        this.paymentCard = value;
    }

    /**
     * Gets the value of the roomService property.
     * 
     * @return
     *     possible object is
     *     {@link RoomService }
     *     
     */
    public RoomService getRoomService() {
        return roomService;
    }

    /**
     * Sets the value of the roomService property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoomService }
     *     
     */
    public void setRoomService(RoomService value) {
        this.roomService = value;
    }

    /**
     * Gets the value of the bookingInfo property.
     * 
     * @return
     *     possible object is
     *     {@link BookingInfo }
     *     
     */
    public BookingInfo getBookingInfo() {
        return bookingInfo;
    }

    /**
     * Sets the value of the bookingInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BookingInfo }
     *     
     */
    public void setBookingInfo(BookingInfo value) {
        this.bookingInfo = value;
    }

}
