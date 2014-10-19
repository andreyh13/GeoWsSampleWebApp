<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.sellbytel.GeoWsSampleWebApp.*" %>    
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Geocode viewport biasing</title>
</head>
<body>
	<header>
		<h1>Geocode viewport biasing</h1>
	</header>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
<%  GeoWsSamples s = new GeoWsSamples();
	String source = GeoWsSamples.showMethod(s, "testGeocodeViewportBias");
	String result = s.testGeocodeViewportBias();
%>	
	<p>In this example we try to geocode the 'Winnetka' address applying the viewport biasing</p>
	
	<p>
		Source code:
	</p>
	
	<code>
		<%= source %>
		<br/>
		<pre>
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