package org.bp.room;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.3.2
 * 2025-01-19T19:58:31.984+01:00
 * Generated source version: 3.3.2
 *
 */
@WebServiceClient(name = "RoomBookingEndpointService",
                  wsdlLocation = "file:/C:/Users/msztu/Documents/githubRepos/TPDII/AZNU_ESCAPEROOM/room-ui/src/main/resources/room.wsdl",
                  targetNamespace = "http://www.bp.org/room/")
public class RoomBookingEndpointService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.bp.org/room/", "RoomBookingEndpointService");
    public final static QName RoomBookingPort = new QName("http://www.bp.org/room/", "RoomBookingPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/msztu/Documents/githubRepos/TPDII/AZNU_ESCAPEROOM/room-ui/src/main/resources/room.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RoomBookingEndpointService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/msztu/Documents/githubRepos/TPDII/AZNU_ESCAPEROOM/room-ui/src/main/resources/room.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RoomBookingEndpointService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RoomBookingEndpointService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RoomBookingEndpointService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public RoomBookingEndpointService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public RoomBookingEndpointService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public RoomBookingEndpointService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns RoomPortType
     */
    @WebEndpoint(name = "RoomBookingPort")
    public RoomPortType getRoomBookingPort() {
        return super.getPort(RoomBookingPort, RoomPortType.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RoomPortType
     */
    @WebEndpoint(name = "RoomBookingPort")
    public RoomPortType getRoomBookingPort(WebServiceFeature... features) {
        return super.getPort(RoomBookingPort, RoomPortType.class, features);
    }

}