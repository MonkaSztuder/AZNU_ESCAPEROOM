/*
 * Travel mircro service
 * Micro service to book a escaperoom
 *
 * OpenAPI spec version: 1.0.0
 * Contact: supportm@bp.org
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package org.bp.escaperoom.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BookRoomRequest
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-12-06T08:44:40.322365400+01:00[Europe/Warsaw]")
public class BookRoomRequest {
  @JsonProperty("person")
  private Person person = null;

  @JsonProperty("room")
  private Room room = null;

  public BookRoomRequest person(Person person) {
    this.person = person;
    return this;
  }

   /**
   * Get person
   * @return person
  **/

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public BookRoomRequest room(Room room) {
    this.room = room;
    return this;
  }

   /**
   * Get room
   * @return room
  **/

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookRoomRequest bookRoomRequest = (BookRoomRequest) o;
    return Objects.equals(this.person, bookRoomRequest.person) &&
        Objects.equals(this.room, bookRoomRequest.room);
  }

  @Override
  public int hashCode() {
    return Objects.hash(person, room);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BookRoomRequest {\n");
    
    sb.append("    person: ").append(toIndentedString(person)).append("\n");
    sb.append("    room: ").append(toIndentedString(room)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}