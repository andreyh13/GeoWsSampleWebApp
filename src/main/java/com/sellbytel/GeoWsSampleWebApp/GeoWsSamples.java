package com.sellbytel.GeoWsSampleWebApp;
import com.google.maps.errors.*;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import com.google.maps.model.*;
import com.google.maps.*;
import com.google.maps.DirectionsApi.RouteRestriction;

public class GeoWsSamples {

	private static String CLIENT_ID = "PUT_YOUR_CLIENTID_HERE";
	private static String CRYPTO_KEY = "PUT_YOUR_CKEY_HERE";
	
	private static int QPS = 10;
	
	private List<String> addresses;
	private List<LatLng> coordinates;
	
	private javax.servlet.jsp.JspWriter jspout; 
	
	/**
	 * Constructor
	 */
	public GeoWsSamples() {
		this.addresses = new ArrayList<String>();
		this.addresses.add("1600 Amphitheatre Parkway, Mountain View, CA");
		
		this.coordinates = new ArrayList<LatLng>();
		this.coordinates.add(new LatLng(37.423021,-122.083739));
	}
	
	/**
	 * Returns random integer for some range 
	 * @param min
	 * @param max
	 * @return
	 */
	private static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public static String showMethod(Object o, String name) {
	    Class<?> c = o.getClass();
	    String output = "";
	    try {
	    	Method[] theMethods = c.getMethods();
	    	for(Method m : theMethods){
	    		if(m.getName().equals(name)){
	    			String code = m.toString();
	    			output += "<div class='source-code'>"+code+"</div>";
	    			break;
	    		}	
	    	}  	
	    } catch(Exception e){
	    	System.out.println(e.getMessage());
	    }	
	    return output;
	}

	
	/**
	 * Returns random address 
	 * @return
	 */
	public String getTestAddress(){
		int index = GeoWsSamples.randInt(0, this.addresses.size()-1);
		return this.addresses.get(index);
	}
	
	/**
	 * Print one geocoding result
	 * @param result
	 * @return
	 */
	private String printGeocodingResult(GeocodingResult result){
		String output = "<div class='geocoding-result'>";
		output += "<h3 class='title'>"+result.formattedAddress+"</h3>";
		if(result.partialMatch){
			output += "<div class='partial-match'>Partial Match</div>";
		}
		output += this.printAddressComponents(result.addressComponents);
		
		String types = "";
		for(AddressType t : result.types){
			types += (types.equals("")?"":", ") + t.toString();
		}
		output += "<h4>Types</h4>";
		output += "<div class='address-types'>"+types+"</div>";
		
		output += this.printGeometry(result.geometry);
		
		if(result.postcodeLocalities != null && result.postcodeLocalities.length >0){
			String pcloc = "";
			for(String pcl : result.postcodeLocalities){
				pcloc += (pcloc.equals("")?"":", ") + pcl;
			}
			output += "<h4>Postcode Localities</h4>";
			output += "<div class='postcode-localities'>"+pcloc+"</div>";
		}
		
		output += "</div>";
		return output;
	}
	
	/**
	 * Print address components for geocoding result
	 * @param addressComponents
	 * @return
	 */
	private String printAddressComponents(AddressComponent[] addressComponents){
		String output = "";
		if(addressComponents!=null && addressComponents.length > 0){
			output += "<h4>Address components</h4>";
			output += "<ul>";
			for(AddressComponent ac : addressComponents){
				output += "<li>"+this.printAddressComponent(ac)+"</li>";
			}
			output += "</ul>";
		}
		return output;
	}
	
	/**
	 * Print address component
	 * @param ac
	 * @return
	 */
	private String printAddressComponent(AddressComponent ac){
		String output = "<dl>";
		output += "<dt>Long name</dt><dd>"+ac.longName+"</dd>";
		output += "<dt>Short name</dt><dd>"+ac.shortName+"</dd>";
		output += "<dt>Types</dt><dd>";
		String types = "";
		for(AddressComponentType t : ac.types){
			types += (types.equals("")?"":", ") + t.toString(); 
		}
		output += types + "</dd>";
		output += "</dl>";
		return output; 
	}
	
	/**
	 * Print geometry
	 * @param geom
	 * @return
	 */
	private String printGeometry(Geometry geom){
		String output = "<h4>Geometry</h4>";
		output += "<dl>";
		output += "<dt>Location</dt><dd>"+this.printLocation(geom.location)+"</dd>";
		output += "<dt>Location type</dt><dd>"+geom.locationType.toString()+"</dd>";
		output += "<dt>Viewport</dt><dd>"+this.printBounds(geom.viewport)+"</dd>";
		if(geom.bounds != null){
			output += "<dt>Bounds</dt><dd>"+this.printBounds(geom.bounds)+"</dd>";
		}	
		output += "</dl>";
		return output;
	} 
	
	/**
	 * Print coordinates
	 * @param latlng
	 * @return
	 */
	private String printLocation(LatLng latlng){
		String output = "("+latlng.lat+", "+latlng.lng+")";
		return output;
	}
	
	private String printBounds(Bounds bounds){
		String output = "Northeast: "+this.printLocation(bounds.northeast)+"; Southwest: "+this.printLocation(bounds.southwest);
		return output;
	}
	
	/**
	 * Print geocoding results
	 * @param results
	 * @return
	 */
	private String printGeocodingResults(GeocodingResult[] results){
		String output = "";
		if(results!=null && results.length>0){
			output += "<ul class='geocoding-results'>";
			for(GeocodingResult r: results){
				System.out.println(r.formattedAddress);
				output += "<li>"+this.printGeocodingResult(r)+"</li>";
			}
			output += "</ul>";
		}
		return output;
	}
	
	/**
	 * Print error message
	 * @param e
	 * @return
	 */
	private String printError(ApiException e){
		return "<div class='api-exception'>" + e.getMessage() + "</div>";
	} 
	
	/**
	 * Simple geocode
	 * @param address
	 * @return
	 */
	public String testGeocodeSimple(String address){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			GeocodingResult[] results =  GeocodingApi.geocode(context, address).await();
			output += this.printGeocodingResults(results);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	/**
	 * Geocoding with viewport biasing
	 * @param address
	 * @return
	 */
	public String testGeocodeViewportBias(){
		String output = "";
		String address = "Winnetka";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			GeocodingApiRequest req = GeocodingApi.newRequest(context);
			LatLng southWestBound = new LatLng(34.172684,-118.604794);
			LatLng northEastBound = new LatLng(34.236144,-118.500938);
			GeocodingResult[] results = req.address(address).bounds(southWestBound, northEastBound).await();
			output += this.printGeocodingResults(results);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	
	/**
	 * Geocoding with region biasing
	 * @param address
	 * @return
	 */
	public String testGeocodeRegionBias(){
		String output = "";
		String address = "Toledo";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			GeocodingApiRequest req = GeocodingApi.newRequest(context);
			GeocodingResult[] results = req.address(address).region("es").await();
			output += this.printGeocodingResults(results);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	/**
	 * Geocoding with component filtering 
	 * @return
	 */
	public String testGeocodeComponentFiltering(){
		String output = "";
		String address = "Torun";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			GeocodingApiRequest req = GeocodingApi.newRequest(context);
			GeocodingResult[] results = req.address(address).components(GeocodingApi.ComponentFilter.administrativeArea("TX"),
					GeocodingApi.ComponentFilter.country("US")).await();
			output += this.printGeocodingResults(results);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	/**
	 * Geocoding with component filtering asynchronous 
	 * @return
	 */
	public void testGeocodeComponentFilteringAsync(javax.servlet.jsp.JspWriter out){
		this.jspout = out;
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		GeocodingApiRequest req = GeocodingApi.newRequest(context)
				.components(GeocodingApi.ComponentFilter.route("Annegatan"),
						GeocodingApi.ComponentFilter.administrativeArea("Helsinki"),
						GeocodingApi.ComponentFilter.country("Finland"));
		
		// Asynchronous
		req.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
		  @Override
		  public void onResult(GeocodingResult[] result) {
			 try {
				jspout.println(printGeocodingResults(result));
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }

		  @Override
		  public void onFailure(Throwable e) {
			  System.out.println(e.getMessage());
		  }
		});
	}
	
	/**
	 * Reverse geocoding 
	 * @return
	 */
	public String testReverseGeocode(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			GeocodingApiRequest req = GeocodingApi.newRequest(context);
			GeocodingResult[] results = req.latlng(new LatLng(40.714224,-73.961452)).await();
			output += this.printGeocodingResults(results);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	/**
	 * Reverse geocoding restricted by type 
	 * @return
	 */
	public String testReverseGeocodeRestricted(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			GeocodingApiRequest req = GeocodingApi.newRequest(context);
			GeocodingResult[] results = req.latlng(new LatLng(40.714224,-73.961452))
					.locationType(LocationType.ROOFTOP).resultType(AddressType.STREET_ADDRESS).await();
			output += this.printGeocodingResults(results);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	/**
	 * Simple directions 
	 * @return
	 */
	public String sampleDirections(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			DirectionsRoute[] routes = DirectionsApi.getDirections(context, "Toronto", "Montreal").await();
			output += this.printDirectionsRoutes(routes);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	private String printDirectionsRoutes(DirectionsRoute[] routes){
		String output = "";
		if(routes!=null && routes.length > 0){
			output += "<ul class='directions-routes'>";
			for(DirectionsRoute r : routes){
				output += "<li>"+this.printDirectionsRoute(r)+"</li>";
			}
			output += "</ul>";
		}
		return output;
	}
	
	private String printDirectionsRoute(DirectionsRoute r){
		String output = "<h3>"+r.summary+"</h3>";
		output += "<h4>"+r.copyrights+"</h4>";
		output += this.printDirectionsLegs(r.legs);
		output += "<h4>Bounds</h4>";
		output += this.printBounds(r.bounds);
		output += "<h4>Polyline</h4>";
		output += r.overviewPolyline.getEncodedPath();
		if(r.warnings!=null && r.warnings.length>0){
			output += "<h4>Warnings</h4>";
			for(String w : r.warnings){
				output += "<div class='warning'>"+w+"</div>";
			}
		}
		if(r.waypointOrder!=null && r.waypointOrder.length>0){
			output += "<h4>Waypoint order</h4>";
			String wp = "";
			for(int i : r.waypointOrder){
				wp += (wp.equals("")?"":", ")+i;
			}
			output += wp;
		}
		
		return output;
	}
	
	private String printDirectionsLegs(DirectionsLeg[] legs){
		String output = "";
		if(legs!=null && legs.length>0){
			output += "<h4>Directions Legs</h4>";
			output += "<ul class='directions-legs'>";
			for(DirectionsLeg l : legs){
				output += "<li>"+this.printDirectionsLeg(l)+"</li>";
			}
			output += "</ul>";
		}
		return output;
	}
	
	private String printDirectionsLeg(DirectionsLeg l){
		String output = "<dl>";
		output += "<dt>Start address</dt><dd>"+l.startAddress+"</dd>";
		output += "<dt>Start location</dt><dd>"+this.printLocation(l.startLocation)+"</dd>";
		output += "<dt>End address</dt><dd>"+l.endAddress+"</dd>";
		output += "<dt>End location</dt><dd>"+this.printLocation(l.endLocation)+"</dd>";
		output += "<dt>Distance</dt><dd>"+this.printDistance(l.distance)+"</dd>";
		output += "<dt>Duration</dt><dd>"+this.printDuration(l.duration)+"</dd>";
		if(l.durationInTraffic!=null){
			output += "<dt>Duration in traffic</dt><dd>"+this.printDuration(l.durationInTraffic)+"</dd>";
		}
		if(l.departureTime!=null){
			output += "<dt>Departure time</dt><dd>"+l.departureTime.toString()+"</dd>";
		}
		if(l.arrivalTime!=null){
			output += "<dt>Arrival time</dt><dd>"+l.arrivalTime.toString()+"</dd>";
		}
		output += "<dt>Steps</dt><dd>"+this.printDirectionsSteps(l.steps)+"</dd>";
		output += "</dl>";
		return output;
	} 
	
	private String printDirectionsSteps(DirectionsStep[] steps){
		String output = "";
		if(steps!=null && steps.length>0){
			output += "<ul>";
			for(DirectionsStep s : steps){
				output += "<li>"+this.printDirectionsStep(s)+"</li>";
			}
			output += "</ul>";
		}
		return output;
	}
	
	private String printDirectionsStep(DirectionsStep s){
		String output = "<dl>";
		output += "<dt>Start location</dt><dd>"+this.printLocation(s.startLocation)+"</dd>";
		output += "<dt>End location</dt><dd>"+this.printLocation(s.endLocation)+"</dd>";
		output += "<dt>Distance</dt><dd>"+this.printDistance(s.distance)+"</dd>";
		output += "<dt>Duration</dt><dd>"+this.printDuration(s.duration)+"</dd>";
		output += "<dt>Travel mode</dt><dd>"+s.travelMode.toString()+"</dd>";
		output += "<dt>Instructions</dt><dd>"+s.htmlInstructions+"</dd>";
		output += "<dt>Polyline</dt><dd>"+s.polyline.getEncodedPath()+"</dd>";
		if(s.subSteps!=null && s.subSteps.length>0){
			output += "<dt>Steps</dt><dd>"+this.printDirectionsSteps(s.subSteps)+"</dd>";
		}
		output += "</dl>";
		
		return output;
	} 
	
	private String printDistance(Distance d){
		String output = "<dl>";
		output += "<dt>Human readable</dt><dd>"+d.humanReadable+"</dd>";
		output += "<dt>Value</dt><dd>"+d.inMeters+"</dd>";
		output += "</dl>";
		return output;
	}
	
	private String printDuration(Duration d){
		String output = "<dl>";
		output += "<dt>Human readable</dt><dd>"+d.humanReadable+"</dd>";
		output += "<dt>In seconds</dt><dd>"+d.inSeconds+"</dd>";
		output += "</dl>";
		return output;
	}
	
	public String sampleDirectionsBicycle(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			DirectionsApiRequest req = DirectionsApi.newRequest(context); 
			DirectionsRoute[] routes = req.origin("Toronto").destination("Montreal")
					.mode(TravelMode.BICYCLING).avoid(RouteRestriction.HIGHWAYS).await();
			output += this.printDirectionsRoutes(routes);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	public String sampleDirectionsTransit(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			DirectionsApiRequest req = DirectionsApi.newRequest(context); 
			DirectionsRoute[] routes = req.origin("Brooklyn").destination("Queens")
					.mode(TravelMode.TRANSIT)
					.departureTime(org.joda.time.DateTime.now().plusMinutes(5)).await();
			output += this.printDirectionsRoutes(routes);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	public String sampleDirectionsWaypoints(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			DirectionsApiRequest req = DirectionsApi.newRequest(context); 
			DirectionsRoute[] routes = req.origin("Boston,MA").destination("Concord,MA")
					.mode(TravelMode.DRIVING)
					.waypoints("Charlestown,MA","Lexington,MA").await();
			output += this.printDirectionsRoutes(routes);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	public String sampleDirectionsWaypointsNoStopover(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			DirectionsApiRequest req = DirectionsApi.newRequest(context); 
			DirectionsRoute[] routes = req.origin("Boston,MA").destination("Concord,MA")
					.mode(TravelMode.DRIVING)
					.waypoints("Charlestown,MA","via:Lexington,MA").await();
			output += this.printDirectionsRoutes(routes);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	public String sampleDirectionsWaypointsOptimize(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			DirectionsApiRequest req = DirectionsApi.newRequest(context); 
			DirectionsRoute[] routes = req.origin("Adelaide,SA").destination("Adelaide,SA")
					.mode(TravelMode.DRIVING)
					.optimizeWaypoints(true)
					.waypoints("Barossa Valley,SA","Clare,SA","Connawarra,SA","McLaren Vale,SA")
					.await();
			output += this.printDirectionsRoutes(routes);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	public String sampleDirectionsRegion(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			DirectionsApiRequest req = DirectionsApi.newRequest(context); 
			DirectionsRoute[] routes = req.origin("Toledo").destination("Madrid")
					.mode(TravelMode.DRIVING)
					.region("es")
					.await();
			output += this.printDirectionsRoutes(routes);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	public String sampleDistanceMatrix(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context); 
			DistanceMatrix trix = req.origins("Vancouver BC","Seattle")
					.destinations("San Francisco","Victoria BC")
					.mode(TravelMode.DRIVING)
					.avoid(RouteRestriction.HIGHWAYS)
					.language("fr-FR")
					.await();
			output += this.printDistanceMatrix(trix);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	private String printDistanceMatrix(DistanceMatrix trix){
		String output = "<dl>";
		output += "<dt>Origin addresses</dt><dd>";
		if(trix.originAddresses!=null && trix.originAddresses.length>0){
			String oa = "";
			for(String a : trix.originAddresses){
				oa += (oa.equals("")?"":", ")+a; 
			}
			output += oa;
		}
		output += "</dd>";
		output += "<dt>Destination addresses</dt><dd>";
		if(trix.destinationAddresses!=null && trix.destinationAddresses.length>0){
			String da = "";
			for(String a : trix.destinationAddresses){
				da += (da.equals("")?"":", ")+a; 
			}
			output += da;
		}
		output += "</dd>";
		output += "<dt>Rows</dt><dd>"+this.printDistanceMatrixRows(trix.rows)+"</dd>";
		output += "</dl>";
		
		return output;
	}
	
	private String printDistanceMatrixRows(DistanceMatrixRow[] rows){
		String output = "";
		if(rows!=null && rows.length>0){
			output += "<ul class='distance-matrix-rows'>";
			for(DistanceMatrixRow r : rows){
				output += "<li>" + this.printDistanceMatrixRows(r)+"</li>";
			}
			output += "</ul>";
		}
		return output;
	}
	
	private String printDistanceMatrixRows(DistanceMatrixRow r){
		String output = "";
		if(r.elements!=null && r.elements.length>0){
			output += "<ul class='distance-matrix-elements'>";
			for(DistanceMatrixElement e : r.elements){
				output += "<li>"+this.printDistanceMatrixElement(e)+"</li>";
			}
			output += "</ul>";
		}
		return output;
	}
	
	private String printDistanceMatrixElement(DistanceMatrixElement e){
		String output = "<dl>";
		output += "<dt>Distance</dt><dd>"+this.printDistance(e.distance)+"</dd>";
		output += "<dt>Duration</dt><dd>"+this.printDuration(e.duration)+"</dd>";
		output += "<dt>Status</dt><dd>"+e.status+"</dd>";
		output += "</dl>";
		return output;
	}
	
	public String sampleElevationByPoint(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			ElevationResult res = ElevationApi.getByPoint(context, new LatLng(39.7391536,-104.9847034))
					.await();
			output += this.printElevationResult(res);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	public String sampleElevationByPoints(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			ElevationResult[] res = ElevationApi.getByPoints(context, 
					new LatLng(39.7391536,-104.9847034), new LatLng(36.455556,-116.866667))
					.await();
			output += this.printElevationResults(res);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	public String sampleElevationByPath(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			ElevationResult[] res = ElevationApi.getByPath(context, 3,  
					new LatLng(36.578581,-118.291994), new LatLng(36.455556,-116.866667))
					.await();
			output += this.printElevationResults(res);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	public String sampleElevationByPolyline(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			ElevationResult[] res = ElevationApi.getByPath(context, 3,  
					new EncodedPolyline("gfo}EtohhUxD@bAxJmGF"))
					.await();
			output += this.printElevationResults(res);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
	
	private String printElevationResults(ElevationResult[] res){
		String output = "";
		if(res!=null && res.length>0){
			output += "<ul class='elevation-results'>";
			for(ElevationResult r : res){
				output += "<li>"+this.printElevationResult(r)+"<li>";
			}
			output += "<ul>";
		}
		return output;
	}
	
	private String printElevationResult(ElevationResult r){
		String output = "<dl>";
		output += "<dt>Location</dt><dd>"+this.printLocation(r.location)+"</dd>";
		output += "<dt>Elevation</dt><dd>"+r.elevation+"</dd>";
		output += "<dt>Resolution</dt><dd>"+r.resolution+"</dd>";
		output += "</dl>";
		return output;
	}
	
	public String sampleTimezone(){
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			java.util.TimeZone res = TimeZoneApi.getTimeZone(context,  
					new LatLng(39.6034810,-119.6822510))
					.await();
			output += res.toString();
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
	}
}
