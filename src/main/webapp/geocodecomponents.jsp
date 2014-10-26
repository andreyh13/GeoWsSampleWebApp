<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.sellbytel.GeoWsSampleWebApp.*" %>    
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Geocode components filtering</title>
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</head>
<body>
	<header>
		<h1>Geocode components filtering</h1>
	</header>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
<%  GeoWsSamples s = new GeoWsSamples();
	String source = GeoWsSamples.showMethod(s, "testGeocodeComponentFiltering");
	String result = s.testGeocodeComponentFiltering();
%>	
	<p>In this example we try to geocode the 'Torun' address applying the components filtering for administrative area and country</p>
	
	<p>
		Source code:
	</p>
	
	<code>
		<%= source %>
		<br/>
		<pre class="prettyprint">
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