<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.sellbytel.GeoWsSampleWebApp.*" %>    
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Distance Matrix sample</title>
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</head>
<body>
	<header>
		<h1>Distance Matrix sample</h1>
	</header>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
<%  GeoWsSamples s = new GeoWsSamples();
	String source = GeoWsSamples.showMethod(s, "sampleDistanceMatrix");
	String result = s.sampleDistanceMatrix();
%>	
	<p>In this example we are requesting distance and duration from Vancouver, BC, Canada and from Seattle, WA, USA, to San Francisco, CA, USA and to Victoria, BC, Canada.</p>
	
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