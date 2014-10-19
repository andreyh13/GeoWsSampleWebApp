<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.sellbytel.GeoWsSampleWebApp.*" %>    
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Reverse Geocoding Restricted by Type</title>
</head>
<body>
	<header>
		<h1>Reverse Geocoding Restricted by Type</h1>
	</header>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
<%  GeoWsSamples s = new GeoWsSamples();
	String source = GeoWsSamples.showMethod(s, "testReverseGeocodeRestricted");
	String result = s.testReverseGeocodeRestricted();
%>	
	<p>In this example we try to reverse geocode the coordinates 40.714224,-73.961452 applying restrictions for location type and result type.</p>
	
	<p>
		Source code:
	</p>
	
	<code>
		<%= source %>
		<br/>
		<pre>
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
		</pre>
	</code>
	
	<p>
		Results:
	</p>
	<p>
		<%= result %>
	</p>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
</body>
</html>