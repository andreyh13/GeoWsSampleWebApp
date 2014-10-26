<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.sellbytel.GeoWsSampleWebApp.*" %>    
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Directions way points</title>
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</head>
<body>
	<header>
		<h1>Directions way points</h1>
	</header>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
<%  GeoWsSamples s = new GeoWsSamples();
	String source = GeoWsSamples.showMethod(s, "sampleDirectionsWaypoints");
	String result = s.sampleDirectionsWaypoints();
%>	
	<p>In this example we search directions for a route between Boston, MA and Concord, MA with stopovers in Charlestown and Lexington</p>
	
	<p>
		Source code:
	</p>
	
	<code>
		<%= source %>
		<br/>
		<pre class="prettyprint">
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